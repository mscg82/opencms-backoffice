/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mscg.util.io;

import com.mscg.util.connection.HttpMethod;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.file.FileConnection;

/**
 *
 * @author Giuseppe Miscione
 */
public class HttpStreaming implements Runnable {

    private FileConnection file;
    private HttpMethod method;
    private final Object mutex = new Object();

    public HttpStreaming(FileConnection file, HttpMethod method) {
        this.file = file;
        this.method = method;
        new Thread(this).start();
    }

    public InputStream getInputStream() throws IOException, InterruptedException {
        synchronized(mutex) {
            mutex.wait();
            return file.openInputStream();
        }
    }

    public void run() {
        byte buffer[] = new byte[2048];
        int bytesRead = 0;
        OutputStream os = null;
        InputStream source = null;
        try {
            source = method.getResponseBodyAsStream();
            os = file.openOutputStream();
            while ((bytesRead = source.read(buffer)) > 0) {
                os.write(buffer, 0, bytesRead);
                synchronized(mutex) {
                    mutex.notifyAll();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(source != null) {
                try {
                    source.close();
                } catch (IOException ex) { }
            }
            try {
                method.releaseConnection();
            } catch (IOException ex) { }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) { }
            }
        }
    }
}
