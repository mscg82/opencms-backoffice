/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mscg.util.connection;

import com.mscg.util.codec.UrlEncoder;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 *
 * @author Giuseppe Miscione
 */
public class PostMethod extends HttpMethod {

    private Vector parameters;

    public PostMethod(String id, String uri) {
        super(id, uri);
        parameters = new Vector();
    }

    public int execute(HttpState state) throws IOException {
        lastAction = "Opening connection";
        conn = (HttpConnection)Connector.open(uri);
        lastAction = "Setting connection method to POST";
        conn.setRequestMethod(HttpConnection.POST);

        // set the parameters for the request
        lastAction = "Setting request parameters";
        setParameterForRequest();

        // set the request cookies
        lastAction = "Setting request cookies";
        setCookiesForRequest(state);

        lastAction = "Creating POST request body";
        StringBuffer request = new StringBuffer();
        for(int i = 0, l = parameters.size(); i < l; i++) {
            NameValuePair pair = (NameValuePair)parameters.elementAt(i);
            if(i != 0)
                request.append("&");
            request.append(pair.getName() + "=" + UrlEncoder.encode(pair.getValue()));
        }

        String requestString = request.toString();
        byte requestBuffer[] = requestString.getBytes();

        lastAction = "Setting POST request headers";
        conn.setRequestProperty("Content-Length", Integer.toString(requestBuffer.length));
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        lastAction = "Writing POST request body";
        OutputStream os = null;
        try {
            os = conn.openOutputStream();
            os.write(requestBuffer);
        } finally {
            try {
                os.flush();
            } catch(IOException e){}
            try{
                os.close();
            } catch(IOException e){}
            os = null;
        }

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

    public void addParameter(NameValuePair param) {
        parameters.addElement(param);
    }

    public void addParameter(String paramName, String paramValue) {
        addParameter(new NameValuePair(paramName, paramValue));
    }

    public void addParameters(NameValuePair[] parameters) {
        for(int i = 0, l = parameters.length; i < l; i++) {
            this.parameters.addElement(parameters[i]);
        }
    }

    public NameValuePair getParameter(String paramName) {
        for(int i = 0, l = parameters.size(); i < l; i++) {
            NameValuePair pair = (NameValuePair)parameters.elementAt(i);
            if(pair.getName().equals(paramName)) {
                return pair;
            }
        }
        return null;
    }

    public NameValuePair[] getParameters() {
        NameValuePair ret[] = new NameValuePair[parameters.size()];
        for(int i = 0, l = parameters.size(); i < l; i++) {
            NameValuePair pair = (NameValuePair)parameters.elementAt(i);
            ret[i] = pair;
        }
        return ret;
    }

    public boolean removeParameter(String paramName) throws IllegalArgumentException {
        if(paramName == null) {
            throw new IllegalArgumentException("Parameter name is null");
        }
        boolean ret = false;
        for(int i = 0; i < parameters.size(); i++) {
            NameValuePair pair = (NameValuePair)parameters.elementAt(i);
            if(pair.getName().equals(paramName)) {
                ret = true;
                parameters.removeElementAt(i);
            }
        }
        return ret;
    }

}
