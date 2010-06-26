/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mscg.util.datasource;

import com.mscg.util.connection.HttpMethod;
import java.io.IOException;
import javax.microedition.media.Control;
import javax.microedition.media.protocol.DataSource;
import javax.microedition.media.protocol.SourceStream;
import org.apache.commons.codec.MimeTypes;

/**
 *
 * @author Giuseppe Miscione
 */
public class HttpDataSource extends DataSource {

    protected SourceStream streams[];
    protected boolean connected;
    protected HttpMethod httpMethod;

    public HttpDataSource(HttpMethod httpMethod) {
        super(httpMethod.getUri());
        this.httpMethod = httpMethod;
        streams = new HttpSourceStream[1];
        connected = false;
    }

    public String getContentType() {
        return MimeTypes.getMimeType(getLocator());
    }

    public void connect() throws IOException {
        // if already connected, return
        if (connected) {
            return;
        }

        // if locator is null, then can't actually connect
        if (getLocator() == null) {
            throw new IOException("Locator is null");
        }

        streams[0] = new HttpSourceStream(httpMethod);

        // set flag
        connected = true;

    }

    public void disconnect() {
        // if there are any streams
        if (streams != null && streams[0] != null) {

            // close the individual stream
            try {
               ((HttpSourceStream) streams[0]).close();
            } catch (IOException e) {
            }
        }

        // and set the flag
        connected = false;

    }

    public void start() throws IOException {
        
    }

    public void stop() throws IOException {
        
    }

    public SourceStream[] getStreams() {
        return streams;
    }

    public Control[] getControls() {
        return new Control[0];
    }

    public Control getControl(String controlType) {
        return null;
    }
}
