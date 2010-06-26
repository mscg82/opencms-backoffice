/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mscg.util.connection;

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 *
 * @author Giuseppe Miscione
 */
public class GetMethod extends HttpMethod {

    public GetMethod(String id, String uri) {
        super(id, uri);
    }

    public int execute(HttpState state) throws IOException {
        lastAction = "Opening connection";
        conn = (HttpConnection)Connector.open(uri);
        lastAction = "Setting connection method to GET";
        conn.setRequestMethod(HttpConnection.GET);

        // set the parameters for the request
        lastAction = "Setting request parameters";
        setParameterForRequest();

        // set the request cookies
        lastAction = "Setting request cookies";
        setCookiesForRequest(state);

        lastAction = "Sending data to host";
        if(listener != null)
            listener.onSendingData(this);
        int ret = this.getStatusCode();
        lastAction = "Response received";
        if(listener != null)
            listener.onResponse(this);

        // parse response headers
        lastAction = "Parsing headers";
        parseHeaders(state);

        return ret;
    }

}
