package com.mscg.appstarter.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Controller;

import com.mscg.appstarter.beans.jaxb.ObjectFactory;
import com.mscg.appstarter.beans.jaxb.Wrapper;
import com.mscg.appstarter.server.exception.InvalidRequestException;

@Controller
public abstract class GenericController {

    protected Logger LOG;

    protected Unmarshaller unmarshaller;
    protected ObjectFactory objectFactory;

    public GenericController() {
        LOG = LoggerFactory.getLogger(this.getClass());
    }

    @Autowired
    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    @Autowired
    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    protected Wrapper getWrapperForException(InvalidRequestException e) {
        return getWrapperForException(e, e.getErrorCode());
    }

    protected Wrapper getWrapperForException(Exception e) {
        return getWrapperForException(e, 500);
    }

    protected Wrapper getWrapperForException(Exception e, Integer code) {
        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setResponse(objectFactory.createResponse());
        wrapper.getResponse().setMessage(objectFactory.createServerMessage());
        wrapper.getResponse().setStatus(code);
        wrapper.getResponse().getMessage().setExceptionClass(e.getClass().getCanonicalName());
        wrapper.getResponse().getMessage().setMessageBody(e.getMessage());

        return wrapper;
    }
}
