package com.mscg.bucket;

import java.io.Closeable;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.NDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mscg.bucket.exception.BucketManagerInitException;

public class BucketManager implements Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(BucketManager.class);

    protected BlockingQueue<Token> tokens;
    protected int tokenSize;
    protected int tokensNumber;
    protected ThreadGroup threadGroup;
    protected Thread refillerThread;


    public BucketManager(int tokenSize, int maxBandwidth, String name) throws IllegalArgumentException,
                                                                              BucketManagerInitException {
        try {
            if(maxBandwidth < tokenSize)
                throw new IllegalArgumentException("Max bandwidth must be greater than the token size");
            if(name == null)
                throw new IllegalArgumentException("Bucket manger name cannot be null");

            tokensNumber = maxBandwidth / tokenSize;
            tokens = new LinkedBlockingQueue<Token>(tokensNumber);

            for(int i = 0; i < tokensNumber; i++) {
                tokens.add(new Token(tokenSize));
            }

            this.tokenSize = tokenSize;
            threadGroup = new ThreadGroup(name);

            refillerThread = new RefillerThread(threadGroup, "Refiller");
            refillerThread.start();

        } catch(IllegalArgumentException e) {
            throw e;
        } catch(Exception e) {
            throw new BucketManagerInitException(e);
        }
    }

    public OutputStream getLimitedSpeedOutputStream(OutputStream os) throws IOException {
        return new LimitedSpeedOutputStream(os);
    }

    public void close() throws IOException {
        refillerThread.interrupt();
    }

    protected class RefillerThread extends Thread {

        public RefillerThread(String name) {
            super(name);
            setDaemon(true);
        }

        public RefillerThread(ThreadGroup group, String name) {
            super(group, name);
            setDaemon(true);
        }

        @Override
        public void run() {
            NDC.push(getThreadGroup().getName() + "-" + getName());
            if(LOG.isDebugEnabled())
                LOG.debug("Refiller thread has been started");
            try {
                while(!isInterrupted()) {
                    long start = System.currentTimeMillis();

                    if(LOG.isDebugEnabled())
                        LOG.debug("Refilling buffer");

                    int counts = 0;
                    while(!isInterrupted() && counts < tokensNumber && tokens.offer(new Token(tokenSize))){
                        counts++;
                    }

                    if(LOG.isDebugEnabled())
                        LOG.debug(Integer.toString(counts) + " tokens have been created");

                    if(!isInterrupted()) {
                        long elapsed = System.currentTimeMillis() - start;
                        Thread.sleep(1000l - elapsed);
                    }
                }
            } catch(InterruptedException e){

            } finally {
                if(LOG.isInfoEnabled())
                    LOG.info("Exiting from refiller thread");

                NDC.pop();
                NDC.remove();
            }

        }

    }

    protected class DataSenderThread extends Thread {

        protected InputStream source;
        protected OutputStream output;
        protected IOException exception;

        public DataSenderThread(String name, InputStream source, OutputStream output) {
            super(name);
            init(source, output);
        }

        public DataSenderThread(ThreadGroup group, String name, InputStream source, OutputStream output) {
            super(group, name);
            init(source, output);
        }

        protected void init(InputStream source, OutputStream output) {
            this.source = source;
            this.output = output;
        }

        @Override
        public void run() {
            NDC.push(getThreadGroup().getName() + "-" + getName());
            if(LOG.isDebugEnabled())
                LOG.debug("Data sender thread has been started");
            byte buffer[] = new byte[tokenSize];
            int bytesRead = 0;
            try {
                while(!isInterrupted()) {
                    tokens.take();

                    if(LOG.isDebugEnabled())
                        LOG.debug("A token for " + tokenSize + " bytes have been acquired.");

                    bytesRead = source.read(buffer, 0, tokenSize);
                    if(bytesRead <= 0)
                        break;
                    output.write(buffer, 0, bytesRead);
                }
            } catch(InterruptedException e) {

            } catch(IOException e) {
                setException(e);
            } finally {
                if(LOG.isInfoEnabled())
                    LOG.info("Exiting from data sender thread");
                NDC.pop();
                NDC.remove();
            }
        }

        /**
         * @return the exception
         */
        public synchronized IOException getException() {
            return exception;
        }

        /**
         * @param exception the exception to set
         */
        protected synchronized void setException(IOException exception) {
            this.exception = exception;
        }

    }

    protected class LimitedSpeedOutputStream extends FilterOutputStream {

        protected PipedInputStream inStream;
        protected PipedOutputStream outStream;
        protected DataSenderThread dataSenderThread;

        public LimitedSpeedOutputStream(OutputStream out) throws IOException {
            super(out);
            inStream = new ExtendedPipedInputStream(10 * tokenSize);
            outStream = new PipedOutputStream(inStream);

            dataSenderThread = new DataSenderThread(threadGroup, "DataSender", inStream, out);
            dataSenderThread.start();
        }

        @Override
        public void write(int b) throws IOException {
            if(dataSenderThread.getException() != null)
                throw dataSenderThread.getException();
            outStream.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            if(dataSenderThread.getException() != null)
                throw dataSenderThread.getException();
            outStream.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            try {
                outStream.flush();
            } catch(Exception e){}
            super.flush();
            if(dataSenderThread.getException() != null)
                throw dataSenderThread.getException();
        }

        @Override
        public void close() throws IOException {
            try {
                outStream.flush();
            } catch(Exception e){}
            try {
                outStream.close();
            } catch(Exception e){}
            try {
                dataSenderThread.join();
            } catch (InterruptedException e) {}
            try {
                inStream.close();
            } catch(Exception e){}
            super.close();
            if(dataSenderThread.getException() != null)
                throw dataSenderThread.getException();
        }

    }

}
