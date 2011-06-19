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
    protected Thread refillerAwakerThread;
    protected Object refillerSemaphore;

    public BucketManager(int tokenSize, int maxBandwidth, String name) throws IllegalArgumentException,
                                                                              BucketManagerInitException {
        refillerSemaphore = new Object();
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

            refillerAwakerThread = new RefillerAwakerThread(threadGroup, "RefillerAwaker");
            refillerAwakerThread.start();

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new BucketManagerInitException(e);
        }
    }

    public OutputStream getLimitedSpeedOutputStream(OutputStream os) throws IOException {
        return new LimitedSpeedOutputStream(os);
    }

    public OutputStream getThreadedLimitedSpeedOutputStream(OutputStream os) throws IOException {
        return new ThreadedLimitedSpeedOutputStream(os);
    }

    public void close() throws IOException {
        refillerAwakerThread.interrupt();
        refillerThread.interrupt();
    }

    protected class RefillerAwakerThread extends Thread {

        public RefillerAwakerThread(String name) {
            super(name);
            setDaemon(true);
        }

        public RefillerAwakerThread(ThreadGroup group, String name) {
            super(group, name);
            setDaemon(true);
        }

        @Override
        public void run() {
            NDC.push(getThreadGroup().getName() + "-" + getName());
            if(LOG.isDebugEnabled())
                LOG.debug("Refiller awaker thread has been started");
            try {
                long elapsed = 0l;
                while(!isInterrupted()) {
                    Thread.sleep(1000l - elapsed);

                    long now = System.currentTimeMillis();
                    if(LOG.isDebugEnabled())
                        LOG.debug("Awaking refiller thread");
                    synchronized (refillerSemaphore) {
                        refillerSemaphore.notifyAll();
                    }
                    elapsed = System.currentTimeMillis() - now;
                }
            } catch (InterruptedException e) {

            } finally {
                if(LOG.isInfoEnabled())
                    LOG.info("Exiting from refiller awaker thread");

                NDC.pop();
                NDC.remove();
            }
        }

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
                int elementsInserted = tokensNumber;
                while(!isInterrupted()) {
                    if(elementsInserted >= tokensNumber) {
                        // we exceeded the maximum number of refill tokens, so wait
                        if(LOG.isDebugEnabled())
                            LOG.debug("Maximum number of refills exceeded, waiting...");
                        synchronized (refillerSemaphore) {
                            refillerSemaphore.wait();
                        }
                        if(LOG.isDebugEnabled())
                            LOG.debug("Restarting refill");
                        elementsInserted = 0;
                    }

                    tokens.put(new Token(tokenSize));
                    elementsInserted++;
                    if(LOG.isDebugEnabled())
                        LOG.debug("Elements refilled so far: " + elementsInserted);

                }
            } catch (InterruptedException e) {

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

    protected class ThreadedLimitedSpeedOutputStream extends FilterOutputStream {

        protected PipedInputStream inStream;
        protected PipedOutputStream outStream;
        protected DataSenderThread dataSenderThread;

        public ThreadedLimitedSpeedOutputStream(OutputStream out) throws IOException {
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

    protected class LimitedSpeedOutputStream extends FilterOutputStream {

        protected int availableBytes;
        protected int tokensUsed;

        public LimitedSpeedOutputStream(OutputStream out) {
            super(out);
        }

        protected int getWriteableBytes(int desiredBytes) throws InterruptedException {
            if(availableBytes == 0) {
                Token token = tokens.take();
                tokensUsed++;
                if(LOG.isDebugEnabled())
                    LOG.debug("A token (" + tokensUsed + ") for " + token.getTokenSize() + " bytes have been acquired.");
                availableBytes = token.getTokenSize();
            }
            int writeable = Math.min(availableBytes, desiredBytes);
            availableBytes -= writeable;
            return writeable;
        }

        @Override
        public void write(int b) throws IOException {
            try {
                int writeable = getWriteableBytes(1);
                if(writeable == 1)
                    super.write(b);
                else
                    throw new IOException("Cannot get enough writable bytes from the bucket manager");
            } catch (InterruptedException e) {
                throw new IOException("Bucket manager was interrupted: " + e.getMessage());
            }
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            try {
                int offset = 0;
                while(offset < len) {
                    int writeable = getWriteableBytes(len - offset);
                    if(writeable > 0) {
                        super.write(b, off + offset, writeable);
                        offset += writeable;
                    } else
                        throw new IOException("Cannot get enough writable bytes from the bucket manager");
                }
            } catch (InterruptedException e) {
                throw new IOException("Bucket manager was interrupted: " + e.getMessage());
            }
        }

    }

}
