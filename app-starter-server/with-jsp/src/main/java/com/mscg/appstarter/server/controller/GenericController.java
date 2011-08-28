package com.mscg.appstarter.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Controller;

import com.mscg.appstarter.beans.jaxb.Login;
import com.mscg.appstarter.beans.jaxb.ObjectFactory;
import com.mscg.appstarter.beans.jaxb.Wrapper;
import com.mscg.appstarter.exception.InvalidRequestException;
import com.mscg.appstarter.server.util.session.SessionsHolder;
import com.mscg.appstarter.util.ResponseCode;
import com.mscg.appstarter.util.Util;

@Controller
public abstract class GenericController {

    protected Logger LOG;

    protected Unmarshaller unmarshaller;
    protected ObjectFactory objectFactory;
    protected SessionsHolder sessionsHolder;

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

    @Autowired
    public void setSessionsHolder(SessionsHolder sessionHolder) {
        this.sessionsHolder = sessionHolder;
    }

    protected Wrapper getWrapperForException(InvalidRequestException e) {
        return getWrapperForException(e, e.getErrorCode());
    }

    protected Wrapper getWrapperForException(Exception e) {
        return getWrapperForException(e, ResponseCode.ERR_APPLICATION_ERROR);
    }

    protected Wrapper getWrapperForException(Exception e, ResponseCode code) {
        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setResponse(objectFactory.createResponse());
        wrapper.getResponse().setMessage(objectFactory.createServerMessage());
        wrapper.getResponse().setStatus(code.getStatus());
        wrapper.getResponse().getMessage().setExceptionClass(e.getClass().getCanonicalName());
        wrapper.getResponse().getMessage().setMessageBody(e.getMessage());

        return wrapper;
    }

    protected void checkSession(Wrapper wrapper) throws InvalidRequestException {
        if(wrapper.getRequest() == null)
            throw new InvalidRequestException("Missing request data", ResponseCode.ERR_MISSING_LOGIN_DATA);
        Login login = wrapper.getRequest().getLogin();
        if(login == null)
            throw new InvalidRequestException("Missing login data", ResponseCode.ERR_MISSING_LOGIN_DATA);
        if(Util.isEmptyOrWhitespaceOnly(login.getUsername()) ||
           Util.isEmptyOrWhitespaceOnly(login.getSessionID()))
            throw new InvalidRequestException("Missing session informations", ResponseCode.ERR_UNAUTHORIZED_ACCESS);

        String username = sessionsHolder.getSessionUser(login.getSessionID());
        if(!login.getUsername().equals(username))
            throw new InvalidRequestException("Unauthorized access", ResponseCode.ERR_UNAUTHORIZED_ACCESS);
    }
}
