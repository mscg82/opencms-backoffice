package com.mscg.appstarter.client.interfacer;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

import com.mscg.appstarter.beans.jaxb.ObjectFactory;


public abstract class GenericAppStarterInterfacer implements AppStarterInterfacer {

    protected Logger LOG;
    protected String baseUrl;
    protected Map<String, String> usernameToSessionID;

    protected ObjectFactory objectFactory;
    protected Marshaller marshaller;
    protected Unmarshaller unmarshaller;

    public GenericAppStarterInterfacer() {
        LOG = LoggerFactory.getLogger(getClass());
        usernameToSessionID = new HashMap<String, String>();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        if(this.baseUrl.endsWith("/"))
            this.baseUrl = this.baseUrl.substring(0, this.baseUrl.length() - 1);
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public Marshaller getMarshaller() {
        return marshaller;
    }

    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public Unmarshaller getUnmarshaller() {
        return unmarshaller;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

}
