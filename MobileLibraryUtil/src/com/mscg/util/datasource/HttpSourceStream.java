/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mscg.util.datasource;

import com.mscg.util.connection.HttpMethod;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.Control;
import javax.microedition.media.protocol.ContentDescriptor;
import javax.microedition.media.protocol.SourceStream;
import org.apache.commons.codec.MimeTypes;

/**
 *
 * @author Giuseppe Miscione
 */
public class HttpSourceStream implements SourceStream{

    protected HttpMethod httpMethod;
    protected InputStream is;

    public HttpSourceStream(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        try {
            is = this.httpMethod.getResponseBodyAsStream();
        } catch (IOException ex) {
        }
    }

    public ContentDescriptor getContentDescriptor() {
        return new ContentDescriptor( MimeTypes.getMimeType(httpMethod.getUri()));
    }

    public long getContentLength() {
        try {
            return (int) httpMethod.getResponseSize();
        } catch (IOException ex) {
            return 0;
        }
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if(is == null)
            throw new IOException("Cannot open connection stream.");
        return is.read(b, off, len);
    }

    public int getTransferSize() {
        return -1;
    }

    public long seek(long where) throws IOException {
        throw new IOException("Seek is not supported.");
    }

    public long tell() {
        return -1;
    }

    public int getSeekType() {
        return NOT_SEEKABLE;
    }

    public Control[] getControls() {
        return new Control[0];
    }

    public Control getControl(String controlType) {
        return null;
    }

    public void close() throws IOException {
        if(is != null) {
            try {
                is.close();
            } catch(IOException e){}
        }
        httpMethod.releaseConnection();
    }

}
