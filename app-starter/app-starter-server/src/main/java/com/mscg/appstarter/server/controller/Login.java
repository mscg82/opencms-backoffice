package com.mscg.appstarter.server.controller;

import java.io.StringReader;
import java.util.UUID;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mscg.appstarter.beans.jaxb.Wrapper;
import com.mscg.appstarter.server.exception.InvalidRequestException;
import com.mscg.appstarter.server.util.Constants;
import com.mscg.appstarter.server.util.SessionsHolder;
import com.mscg.appstarter.server.util.Settings;
import com.mscg.appstarter.util.Util;

@Controller
@RequestMapping("/auth")
public class Login extends GenericController {

    private SessionsHolder sessionsHolder;

    @Autowired
    public void setSessionsHolder(SessionsHolder sessionHolder) {
        this.sessionsHolder = sessionHolder;
    }

    @RequestMapping(value="/logout",method={RequestMethod.GET, RequestMethod.POST})
    public String logout(Model model, @RequestBody String requestBody) {
        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setResponse(objectFactory.createResponse());
        try {
            Wrapper request = (Wrapper)unmarshaller.unmarshal(new StreamSource(new StringReader(requestBody)));
            com.mscg.appstarter.beans.jaxb.Login login = request.getRequest().getLogin();

            if(Util.isEmptyOrWhitespaceOnly(login.getUsername()))
                throw new InvalidRequestException("Username not specified", 501);

            if(Util.isEmptyOrWhitespaceOnly(login.getSessionID()))
                throw new InvalidRequestException("Session ID not specified", 501);

            sessionsHolder.removeSession(login.getSessionID());

            wrapper.getResponse().setStatus(200);

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
                throw new InvalidRequestException("Username not specified", 501);

            if(Util.isEmptyOrWhitespaceOnly(login.getNonce())) {
                // generate a nonce and send it back to client
                UUID uuid = UUID.randomUUID();
                wrapper.getResponse().setLogin(objectFactory.createLogin());
                wrapper.getResponse().getLogin().setUsername(login.getUsername());
                wrapper.getResponse().getLogin().setNonce(uuid.toString());
                wrapper.getResponse().setStatus(200);
            }
            else {
                // check if the identifier is correct
                if(Util.isEmptyOrWhitespaceOnly(login.getNonce()))
                    throw new InvalidRequestException("Nonce non specified", 502);
                if(Util.isEmptyOrWhitespaceOnly(login.getIdentifier()))
                    throw new InvalidRequestException("Identifier non specified", 502);

                String encPassword = Settings.getConfig().getString(Constants.ENC_PASSWORD.replace("${username}",
                                                                                           login.getUsername()));;
                if(Util.isEmptyOrWhitespaceOnly(encPassword)) {
                    String password = Settings.getConfig().getString(Constants.PLAIN_PASSWORD.replace("${username}",
                                                                                                      login.getUsername()));
                    encPassword = DigestUtils.md5Hex(password);
                }

                String verify = DigestUtils.md5Hex(login.getUsername() + login.getNonce() + encPassword);
                if(!login.getIdentifier().equals(verify))
                    throw new InvalidRequestException("Unauthorized access", 401);

                // login is correct, generate a session id and send to client
                UUID uuid = UUID.randomUUID();
                wrapper.getResponse().setLogin(objectFactory.createLogin());
                wrapper.getResponse().getLogin().setUsername(login.getUsername());
                wrapper.getResponse().getLogin().setSessionID(uuid.toString());
                wrapper.getResponse().setStatus(200);

                sessionsHolder.storeSession(wrapper.getResponse().getLogin().getSessionID(),
                                            wrapper.getResponse().getLogin().getUsername());
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

}
