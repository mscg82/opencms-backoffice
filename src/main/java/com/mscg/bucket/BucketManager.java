package com.mscg.bucket;

import java.io.Closeable;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
            int runs = 0;
            int totalCount = 0;
            try {
                while(!isInterrupted()) {
                    long start = System.currentTimeMillis();

                    if(LOG.isDebugEnabled())
                        LOG.debug("Refilling buffer");

                    int counts = 0;
                    while(!isInterrupted() && counts < tokensNumber / 4 && tokens.offer(new Token(tokenSize))){
                        counts++;
                    }
                    totalCount += tokensNumber / 4;

                    runs = (runs + 1) % 4;
                    if(runs == 0) {
                        // its the forth run, so refill the entire buffer, if any space has been left empty
                        while(!isInterrupted() && totalCount < tokensNumber && tokens.offer(new Token(tokenSize))){
                            counts++;
                            totalCount++;
                        }
                        totalCount = 0;
                    }

                    if(LOG.isDebugEnabled())
                        LOG.debug(Integer.toString(counts) + " tokens have been created");

                    if(!isInterrupted()) {
                        long elapsed = System.currentTimeMillis() - start;
                        Thread.sleep(250l - elapsed);
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
            } catch(InterruptedException e) {
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
                    }
                    else
                        throw new IOException("Cannot get enough writable bytes from the bucket manager");
                }
            } catch(InterruptedException e) {
                throw new IOException("Bucket manager was interrupted: " + e.getMessage());
            }
        }

    }

}
