package com.mscg.bucket.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.mscg.bucket.BucketManager;

public class TestBucket {

    /**
     * @param args
     */
    public static void main(String[] args) {
        BucketManager bm = null;
        try {
            bm = new BucketManager(2048, 204800, "TestBucket");

            Thread threads[] = new Thread[4];
            threads[0] = new FileWriterThread(bm, "./out1.tmp", 2);
            threads[1] = new FileWriterThread(bm, "./out2.tmp", 2);
//            threads[2] = new FileWriterThread(bm, "./out3.tmp", 2);
//            threads[3] = new FileWriterThread(bm, "./out4.tmp", 2);
            for(int i = 0; i < threads.length; i++) {
                if(threads[i] != null)
                    threads[i].start();
            }
            for(int i = 0; i < threads.length; i++) {
                if(threads[i] != null)
                    threads[i].join();
            }

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bm.close();
            } catch(Exception e){}
        }
    }

    private static class FileWriterThread extends Thread {

        private BucketManager bm;
        private String filename;
        private int mbSize;

        public FileWriterThread(BucketManager bm, String filename, int mbSize) {
            this.bm = bm;
            this.filename = filename;
            this.mbSize = mbSize;
        }

        @Override
        public void run() {
            OutputStream os = null;
            try {
                byte buffer[] = new byte[102400];
                for(int i = 0; i < buffer.length; i++) {
                    buffer[i] = (byte)(Math.random() * 255);
                }
                int totalSize = mbSize * 1024 * 1024;
                int sizeWritten = 0;
                File outFile = new File(filename);
                if(!outFile.exists()) {
                    outFile.getParentFile().mkdirs();
                    outFile.createNewFile();
                }
                os = new FileOutputStream(outFile);
                os = bm.getLimitedSpeedOutputStream(os);
                long start = System.currentTimeMillis();
                while(sizeWritten < totalSize) {
                    int sizeToWrite = Math.min(buffer.length, totalSize - sizeWritten);
                    os.write(buffer, 0, sizeToWrite);
                    sizeWritten += sizeToWrite;
                }
                long elapsed = Math.max(System.currentTimeMillis() - start, 1l);
                os.close();
                double speed = ((double)outFile.length()) / elapsed / 1.024d;

                StringBuilder sb = new StringBuilder();
                sb.append("-----------------------------------").append('\n');
                sb.append("File name:    ").append(filename).append('\n');
                sb.append("Time elapsed: ").append(elapsed).append(" ms").append('\n');
                sb.append("Speed:        ").append(String.format("%1$.2f kB/s", speed)).append('\n');
                sb.append("-----------------------------------");
                System.out.println(sb.toString());

            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    os.close();
                } catch (Exception e) { }
            }
        }

    }

}
