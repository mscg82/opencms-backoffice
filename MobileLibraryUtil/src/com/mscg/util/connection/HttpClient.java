/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mscg.util.connection;

import com.mscg.util.connection.auth.AuthScope;
import com.mscg.util.connection.auth.Credentials;
import com.mscg.util.net.URL;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Giuseppe Miscione
 */
public class HttpClient {
    
    private HttpState state;
    private HttpMethodExecutionListener listener;
    private boolean autocloseConnection;

    private Hashtable credentials;

    private class MethodExecutorThread extends Thread {

        private HttpMethod method;

        public MethodExecutorThread(HttpMethod method) {
            this.method = method;
        }

        public void run() {
            try {
                // check for credentials match
                URL url = new URL(method.getUri());
                AuthScope reqScope = new AuthScope(url.getHost(), url.getPort());
                Enumeration keys = credentials.keys();
                while(keys.hasMoreElements()) {
                    AuthScope scope = (AuthScope) keys.nextElement();
                    if(scope.match(reqScope) > 0) {
                        // a match was found
                        Credentials cred = (Credentials) credentials.get(scope);
                        String decCredentials = cred.getUserName() + ":" + cred.getPassword();
                        String encCredentials = new String(Base64.encodeBase64(decCredentials.getBytes()));
                        method.addRequestHeader("Authorization", "Basic " + encCredentials);
                    }
                }

                method.execute(state);
                if(listener != null)
                    listener.onMethodExecuted(method);
                
            } catch (IOException ex) {
                ex.printStackTrace();
                if(listener != null)
                    listener.onMethodExecutionError(ex, method);
            } finally {
                if(autocloseConnection) {
                    try {
                        method.releaseConnection();
                    } catch(Exception e) {}
                }
            }
        }

    }

    public HttpClient() {
        this(true);
    }

    public HttpClient(boolean autocloseConnection) {
        state = new HttpState();
        credentials = new Hashtable();
        this.autocloseConnection = autocloseConnection;
    }

    public void setListener(HttpMethodExecutionListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        listener = null;
    }

    public HttpMethodExecutionListener getListener() {
        return listener;
    }

    public Thread executeMethod(HttpMethod method) {
        method.setListener(listener);
        Thread ret = new MethodExecutorThread(method);
        ret.start();
        return ret;
    }

    public void setCredentials(AuthScope scope, Credentials credentials) {
        this.credentials.put(scope, credentials);
    }

    public void clearCredentials() {
        this.credentials.clear();
    }
}
