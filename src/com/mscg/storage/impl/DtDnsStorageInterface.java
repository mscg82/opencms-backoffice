package com.mscg.storage.impl;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.mscg.config.ConfigLoader;
import com.mscg.httpinterface.AbstractHttpInterface;
import com.mscg.storage.StorageInterface;
import com.mscg.util.Util;
import com.mscg.util.passwordreader.PasswordReader;
import com.mscg.util.passwordreader.impl.PlainPasswordReader;

public class DtDnsStorageInterface extends AbstractHttpInterface implements StorageInterface {

    private static Logger log = Logger.getLogger(DtDnsStorageInterface.class);

    protected PasswordReader pwdReader;

    public DtDnsStorageInterface() throws ConfigurationException {
        super();
        pwdReader = (PasswordReader) Util.loadClass((String) config.get(ConfigLoader.DTDNS_PASSWORD_READER),
                                                    this.getClass().getClassLoader());
        if(pwdReader == null){
            // default password reader
            pwdReader = new PlainPasswordReader();
        }
    }

    public void init() throws ConfigurationException {

    }

    public void storeIP(String service, List<String> IPs) throws HttpException, IOException {
        if(!IPs.isEmpty()) {
            String password = pwdReader.readPassword((String) config.get(ConfigLoader.DTDNS_PASSWORD));

            Map<String, String> params = new LinkedHashMap<String, String>();
            params.put("id", service);
            params.put("pw", password);
            params.put("ip", IPs.get(0));

            String url = prepareUrl("autodns.cfm", params);

            if(log.isDebugEnabled())
                log.debug("Calling URL " + url + "...");
            httpGet = null;
            try {
                httpGet = new GetMethod(url);
                client.executeMethod(httpGet);
                if (httpGet.getStatusCode() == HttpStatus.SC_OK) {
                    if(log.isDebugEnabled())
                        log.debug("Server response: \"" + httpGet.getResponseBodyAsString().trim() + "\"");
                }
            } finally {
                if(httpGet != null){
                    httpGet.releaseConnection();
                    httpGet = null;
                }
            }
        }
    }

}
