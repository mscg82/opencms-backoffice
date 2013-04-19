package com.mscg.httpinterface;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

public abstract class AbstractIpAddressInterface extends AbstractHttpInterface implements IpAddressInterface {

    private static final Logger log = Logger.getLogger(AbstractIpAddressInterface.class);

    public AbstractIpAddressInterface() throws ConfigurationException {
        super();
    }

    protected String getRetrievedIpPageContent(String url) throws HttpException, IOException {
        String ret = null;
        httpGet = null;
        try{
            if(log.isDebugEnabled())
                log.debug("Retrieving IP page content by calling URL: " + url + "...");

            httpGet = new GetMethod(url);
            client.executeMethod(httpGet);
            if (httpGet.getStatusCode() == HttpStatus.SC_OK) {
                ret = httpGet.getResponseBodyAsString();
                log.debug("IP page content correctly retrieved");
            }
            else {
                String failure = httpGet.getStatusLine().toString();
                throw new HttpException("Internal communication error (" + failure + ").");
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
