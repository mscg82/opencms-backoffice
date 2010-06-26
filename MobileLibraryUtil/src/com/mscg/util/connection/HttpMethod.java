/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mscg.util.connection;

import com.mscg.util.connection.cookie.Cookie;
import com.mscg.util.connection.cookie.CookieSpec;
import com.mscg.util.net.URL;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.io.HttpConnection;

/**
 *
 * @author Giuseppe Miscione
 */
public abstract class HttpMethod {

    protected String id;
    protected Vector requestHeaders;
    protected Vector responseHeaders;
    protected HttpConnection conn;
    protected String uri;
    protected HttpMethodExecutionListener listener;
    protected String lastAction;

    public HttpMethod(String id, String uri) {
        setId(id);
        lastAction = "Initializing";
        requestHeaders = new Vector();
        responseHeaders = new Vector();
        conn = null;
        setUri(uri);
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLastAction() {
        return lastAction;
    }

    public HttpMethodExecutionListener getListener() {
        return listener;
    }

    public void setListener(HttpMethodExecutionListener listener) {
        this.listener = listener;
    }

    public void addRequestHeader(Header header) {
        requestHeaders.addElement(header);
    }

    public void addRequestHeader(String headerName, String headerValue) {
        addRequestHeader(new Header(headerName, headerValue));
    }

    public Header[] getRequestHeaders() {
        Header ret[] = new Header[requestHeaders.size()];
        for(int i = 0, l = requestHeaders.size(); i < l; i++) {
            ret[i] = (Header)requestHeaders.elementAt(i);
        }
        return ret;
    }

    public Header[] getRequestHeaders(String headerName) {
        Vector tmp = new Vector();
        for(int i = 0, l = requestHeaders.size(); i < l; i++) {
            Header header = (Header)requestHeaders.elementAt(i);
            if(header.getName().equals(headerName))
                tmp.addElement(header);
        }

        Header ret[] = new Header[tmp.size()];
        for(int i = 0, l = tmp.size(); i < l; i++) {
            ret[i] = (Header)tmp.elementAt(i);
        }

        return ret;
    }

    public Header getResponseHeader(String headerName) {
        Header headers[] = getResponseHeaders(headerName);
        if(headers.length != 0) {
            return headers[0];
        }
        else {
            return null;
        }
    }

    public Header[] getResponseHeaders() {
        Header ret[] = new Header[responseHeaders.size()];
        for(int i = 0, l = responseHeaders.size(); i < l; i++) {
            Header header = (Header)responseHeaders.elementAt(i);
            ret[i] = header;
        }
        return ret;
    }

    public Header[] getResponseHeaders(String headerName) {
        Vector tmp = new Vector();
        for(int i = 0, l = responseHeaders.size(); i < l; i++) {
            Header header = (Header)responseHeaders.elementAt(i);
            if(header.getName().equals(headerName))
                tmp.addElement(header);
        }

        Header ret[] = new Header[tmp.size()];
        for(int i = 0, l = tmp.size(); i < l; i++) {
            ret[i] = (Header)tmp.elementAt(i);
        }

        return ret;
    }

    public InputStream getResponseBodyAsStream() throws IOException {
        if(conn == null) {
            throw new IOException("Connection is not initialized.");
        }

        return conn.openInputStream();
    }

    public String getResponseBodyAsString() throws IOException {
        return getResponseBodyAsString(128);
    }

    public String getResponseBodyAsString(int bufferSize) throws IOException {
        if(conn == null) {
            throw new IOException("Connection is not initialized.");
        }

        InputStream is = null;
        try {
            is = getResponseBodyAsStream();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte buffer[] = new byte[bufferSize];
            int bytesRead = 0;
            while(( bytesRead = is.read(buffer)) > 0) {
                bos.write(buffer, 0, bytesRead);
            }
            String text = new String(bos.toByteArray());

            return text;
            
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch(IOException e){}
            }
        }

    }

    public InputStream getResponseBodyAsDataStream() throws IOException {
        if(conn == null) {
            throw new IOException("Connection is not initialized.");
        }

        return conn.openDataInputStream();
    }

    public abstract int execute(HttpState state) throws IOException;

    public int getStatusCode() throws IOException {
        if(conn == null) {
            throw new IOException("Connection is not initialized.");
        }
        return conn.getResponseCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void releaseConnection() throws IOException {
        if(conn != null) {
            conn.close();
            conn = null;
        }
    }

    public String getType() throws IOException {
        if(conn == null) {
            throw new IOException("Connection is not initialized.");
        }
        return conn.getType();
    }

    public long getResponseSize() throws IOException {
        if(conn == null) {
            throw new IOException("Connection is not initialized.");
        }
        return conn.getLength();
    }

    protected void setParameterForRequest() throws IOException {
        for(int i = 0, l = requestHeaders.size(); i < l; i++) {
            Header header = (Header)requestHeaders.elementAt(i);
            conn.setRequestProperty(header.getName(), header.getValue());
        }
    }

    protected void setCookiesForRequest(HttpState state) throws IOException {
        Cookie cookies[] = state.getCookies();
        Vector validCookies = new Vector();
        for(int i = 0, l = cookies.length; i < l; i++) {
            if(CookieSpec.match(cookies[i], uri)) {
                validCookies.addElement(cookies[i]);
            }
        }
        if(validCookies.size() != 0) {
            StringBuffer cookieStr = new StringBuffer();
            for(int i = 0, l = validCookies.size(); i < l; i++) {
                Cookie cookie = (Cookie)validCookies.elementAt(i);
                if(i != 0)
                    cookieStr.append("; ");
                cookieStr.append(cookie);
            }

            conn.setRequestProperty("Cookie", cookieStr.toString());
        }
    }

    protected void parseHeaders(HttpState state) throws IOException {
        for(int i = 0; true; i++) {
            String key = conn.getHeaderFieldKey(i);
            if(key == null) {
                break;
            }
            String value = conn.getHeaderField(i);

            if(key.equalsIgnoreCase("Set-Cookie")) {
                // set the cookie
                Cookie cookie = new Cookie(value);
                if(cookie.getDomain() == null) {
                    URL url = new URL(uri);
                    cookie.setDomain(url.getHost());
                }
                state.addCookie(cookie);
            }
            else {
                responseHeaders.addElement(new Header(key, value));
            }
        }
    }

}
