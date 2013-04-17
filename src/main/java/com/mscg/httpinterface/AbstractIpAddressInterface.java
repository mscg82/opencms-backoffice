package com.mscg.httpinterface;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public abstract class AbstractIpAddressInterface extends AbstractHttpInterface implements IpAddressInterface {

    public AbstractIpAddressInterface() throws ConfigurationException {
        super();
    }

    protected String getRetrievedIpPageContent(String url) throws HttpException, IOException {
        String ret = null;
        httpGet = null;
        try{
            httpGet = new GetMethod(url);
            client.executeMethod(httpGet);
            if (httpGet.getStatusCode() == HttpStatus.SC_OK) {
                ret = httpGet.getResponseBodyAsString();
            }
            else {
                String failure = httpGet.getStatusLine().toString();
                throw new HttpException("Errore di comunicazione interno (" + failure + ").");
            }
        } finally{
            if(httpGet != null){
                httpGet.releaseConnection();
                httpGet = null;
            }
        }
        return ret;
    }
}
