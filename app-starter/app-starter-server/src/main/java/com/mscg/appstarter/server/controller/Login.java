package com.mscg.appstarter.server.controller;

import java.io.StringReader;
import java.util.Iterator;
import java.util.UUID;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mscg.appstarter.beans.jaxb.Wrapper;
import com.mscg.appstarter.server.exception.InvalidRequestException;
import com.mscg.appstarter.server.util.Constants;
import com.mscg.appstarter.server.util.Settings;
import com.mscg.appstarter.util.ResponseCode;
import com.mscg.appstarter.util.Util;

@Controller
@RequestMapping("/auth")
public class Login extends GenericController {

    @RequestMapping(value="/logout",method={RequestMethod.GET, RequestMethod.POST})
    public String logout(Model model, @RequestBody String requestBody) {
        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setResponse(objectFactory.createResponse());
        try {
            Wrapper request = (Wrapper)unmarshaller.unmarshal(new StreamSource(new StringReader(requestBody)));
            com.mscg.appstarter.beans.jaxb.Login login = request.getRequest().getLogin();

            if(Util.isEmptyOrWhitespaceOnly(login.getUsername()))
                throw new InvalidRequestException("Username not specified", ResponseCode.ERR_MISSING_LOGIN_DATA);

            if(Util.isEmptyOrWhitespaceOnly(login.getSessionID()))
                throw new InvalidRequestException("Session ID not specified", ResponseCode.ERR_MISSING_LOGIN_DATA);

            sessionsHolder.removeSession(login.getSessionID());

            wrapper.getResponse().setStatus(ResponseCode.OK.getStatus());

        } catch (InvalidRequestException e) {
            LOG.error("Invalid logout parameters", e);
            wrapper = getWrapperForException(e);
        } catch (Exception e) {
            LOG.error("Cannot execute logout action", e);
            wrapper = getWrapperForException(e);
        }
        model.addAttribute("object", wrapper);
        return "response";
    }

    @RequestMapping(value="/login",method={RequestMethod.GET, RequestMethod.POST})
    public String login(Model model, @RequestBody String requestBody) {
        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setResponse(objectFactory.createResponse());
        try {
            Wrapper request = (Wrapper)unmarshaller.unmarshal(new StreamSource(new StringReader(requestBody)));
            com.mscg.appstarter.beans.jaxb.Login login = request.getRequest().getLogin();

            if(Util.isEmptyOrWhitespaceOnly(login.getUsername()))
                throw new InvalidRequestException("Username not specified", ResponseCode.ERR_MISSING_LOGIN_DATA);
            Iterator<String> keys = Settings.getConfig().getKeys(Constants.USER_DATA.replace("${username}",
                                                                                             login.getUsername()));
            if(Util.isEmpty(keys))
                throw new InvalidRequestException("Invalid username", ResponseCode.ERR_INVALID_USERNAME);

            if(Util.isEmptyOrWhitespaceOnly(login.getIdentifier())) {
                // generate a nonce and send it back to client
                String nonce = UUID.randomUUID().toString();
                String tempSession = UUID.randomUUID().toString();
                sessionsHolder.storeTempSession(tempSession, nonce);
                wrapper.getResponse().setLogin(objectFactory.createLogin());
                wrapper.getResponse().getLogin().setUsername(login.getUsername());
                wrapper.getResponse().getLogin().setNonce(nonce);
                wrapper.getResponse().getLogin().setSessionID(tempSession);
                wrapper.getResponse().setStatus(ResponseCode.OK.getStatus());
            }
            else {
                // check if the identifier is correct
                if(Util.isEmptyOrWhitespaceOnly(login.getSessionID()))
                    throw new InvalidRequestException("Temporary session ID non specified", ResponseCode.ERR_MISSING_LOGIN_SESSION);
                try {
                    String nonce = sessionsHolder.getTempSessionData(login.getSessionID());
                    if(Util.isEmptyOrWhitespaceOnly(nonce))
                        throw new InvalidRequestException("Invalid login session", ResponseCode.ERR_INVALID_LOGIN_SESSION);

                    String encPassword = Settings.getConfig().getString(Constants.ENC_PASSWORD.replace("${username}",
                                                                                               login.getUsername()));
                    if(Util.isEmptyOrWhitespaceOnly(encPassword)) {
                        String password = Settings.getConfig().getString(Constants.PLAIN_PASSWORD.replace("${username}",
                                                                                                          login.getUsername()));
                        encPassword = DigestUtils.md5Hex(password);
                    }

                    String verify = DigestUtils.md5Hex(login.getUsername() + nonce + encPassword);
                    if(!login.getIdentifier().equals(verify))
                        throw new InvalidRequestException("Unauthorized access", ResponseCode.ERR_UNAUTHORIZED_ACCESS);

                    // login is correct, generate a session id and send to client
                    String uuid = UUID.randomUUID().toString();
                    wrapper.getResponse().setLogin(objectFactory.createLogin());
                    wrapper.getResponse().getLogin().setUsername(login.getUsername());
                    wrapper.getResponse().getLogin().setSessionID(uuid);
                    wrapper.getResponse().setStatus(ResponseCode.OK.getStatus());

                    sessionsHolder.storeSession(wrapper.getResponse().getLogin().getSessionID(),
                                                wrapper.getResponse().getLogin().getUsername());
                } finally {
                    sessionsHolder.removeTempSession(login.getSessionID());
                }
            }
        } catch (InvalidRequestException e) {
            LOG.error("Invalid login parameters", e);
            wrapper = getWrapperForException(e);
        } catch (Exception e) {
            LOG.error("Cannot execute login action", e);
            wrapper = getWrapperForException(e);
        }
        model.addAttribute("object", wrapper);
        return "response";
    }

    @RequestMapping(value="/ping",method={RequestMethod.GET, RequestMethod.POST})
    public String pingSession(Model model, @RequestBody String requestBody) {
        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setResponse(objectFactory.createResponse());
        try {
            Wrapper request = (Wrapper)unmarshaller.unmarshal(new StreamSource(new StringReader(requestBody)));
            com.mscg.appstarter.beans.jaxb.Login login = request.getRequest().getLogin();

            if(Util.isEmptyOrWhitespaceOnly(login.getUsername()))
                throw new InvalidRequestException("Username not specified", ResponseCode.ERR_MISSING_LOGIN_DATA);

            if(Util.isEmptyOrWhitespaceOnly(login.getSessionID()))
                throw new InvalidRequestException("Session ID not specified", ResponseCode.ERR_MISSING_LOGIN_DATA);

            String user = sessionsHolder.getSessionUser(login.getSessionID());

            if(Util.isEmptyOrWhitespaceOnly(user))
                throw new InvalidRequestException("Invalid user session", ResponseCode.ERR_INVALID_USER_SESSION);

            wrapper.getResponse().setStatus(ResponseCode.OK.getStatus());

        } catch (InvalidRequestException e) {
            LOG.error("Invalid ping parameters", e);
            wrapper = getWrapperForException(e);
        } catch (Exception e) {
            LOG.error("Cannot execute ping action", e);
            wrapper = getWrapperForException(e);
        }
        model.addAttribute("object", wrapper);
        return "response";
    }

}
