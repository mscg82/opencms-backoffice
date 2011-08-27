package com.mscg.appstarter.server.controller;

import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mscg.appstarter.beans.jaxb.Wrapper;
import com.mscg.appstarter.server.exception.ApplicationAlreadyRunningException;
import com.mscg.appstarter.server.exception.ApplicationLaunchException;
import com.mscg.appstarter.server.exception.ApplicationNotConfiguredException;
import com.mscg.appstarter.server.exception.ApplicationNotRunningException;
import com.mscg.appstarter.server.exception.InvalidRequestException;
import com.mscg.appstarter.server.util.application.ApplicationInfo;
import com.mscg.appstarter.server.util.application.ApplicationsHolder;
import com.mscg.appstarter.util.ResponseCode;

@Controller
@RequestMapping("/applications")
public class Applications extends GenericController {

    private ApplicationsHolder applicationsHolder;

    @Autowired
    public void setApplicationsHolder(ApplicationsHolder applicationsHolder) {
        this.applicationsHolder = applicationsHolder;
    }

    @RequestMapping(value="/list", method={RequestMethod.GET, RequestMethod.POST})
    public String listApplications(Model model, @RequestBody String requestBody) {
        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setResponse(objectFactory.createResponse());
        try {
            Wrapper request = (Wrapper)unmarshaller.unmarshal(new StreamSource(new StringReader(requestBody)));

            // check if the user is correctly logged in
            checkSession(request);

            wrapper.getResponse().setApplications(objectFactory.createApplicationsList());
            for(ApplicationInfo appInfo : applicationsHolder.getApplicationsList().values()) {
                com.mscg.appstarter.beans.jaxb.ApplicationInfo appInfoXML = objectFactory.createApplicationInfo();
                appInfoXML.setId(appInfo.getId());
                appInfoXML.setName(appInfo.getName());
                appInfoXML.setRunning(applicationsHolder.isApplicationRunning(appInfo.getId()));
                wrapper.getResponse().getApplications().getApplication().add(appInfoXML);
            }

            wrapper.getResponse().setStatus(ResponseCode.OK.getStatus());

        } catch (InvalidRequestException e) {
            LOG.error("Invalid login parameters", e);
            wrapper = getWrapperForException(e);
        } catch (Exception e) {
            LOG.error("Cannot execute application list action", e);
            wrapper = getWrapperForException(e);
        }
        model.addAttribute("object", wrapper);
        return "response";
    }

    @RequestMapping(value="/launch/{id}", method={RequestMethod.GET, RequestMethod.POST})
    public String launchApplication(Model model,
                                    @RequestBody String requestBody,
                                    @PathVariable("id") Integer applicationID) {

        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setResponse(objectFactory.createResponse());
        try {
            Wrapper request = (Wrapper)unmarshaller.unmarshal(new StreamSource(new StringReader(requestBody)));

            // check if the user is correctly logged in
            checkSession(request);

            ApplicationInfo appInfo = applicationsHolder.launchApplication(applicationID);

            wrapper.getResponse().setApplications(objectFactory.createApplicationsList());
            com.mscg.appstarter.beans.jaxb.ApplicationInfo appInfoXML = objectFactory.createApplicationInfo();
            appInfoXML.setId(appInfo.getId());
            appInfoXML.setName(appInfo.getName());
            appInfoXML.setRunning(applicationsHolder.isApplicationRunning(appInfo.getId()));
            wrapper.getResponse().getApplications().getApplication().add(appInfoXML);
            wrapper.getResponse().setStatus(ResponseCode.OK.getStatus());

        } catch (InvalidRequestException e) {
            LOG.error("Invalid login parameters", e);
            wrapper = getWrapperForException(e);
        } catch (ApplicationNotConfiguredException e) {
            LOG.error("Cannot launch application", e);
            wrapper = getWrapperForException(e, ResponseCode.ERR_APPLICATION_NOT_CONFIGURED);
        } catch (ApplicationAlreadyRunningException e) {
            LOG.error("Application already running", e);
            wrapper = getWrapperForException(e, ResponseCode.ERR_APPLICATION_ALREADY_RUNNING);
        } catch (ApplicationLaunchException e) {
            LOG.error("Cannot launch application", e);
            wrapper = getWrapperForException(e, ResponseCode.ERR_APPLICATION_LAUNCH_ERROR);
        } catch (Exception e) {
            LOG.error("Cannot execute launch application action", e);
            wrapper = getWrapperForException(e);
        }
        model.addAttribute("object", wrapper);
        return "response";
    }

    @RequestMapping(value="/close/{id}", method={RequestMethod.GET, RequestMethod.POST})
    public String closeApplication(Model model,
                                   @RequestBody String requestBody,
                                   @PathVariable("id") Integer applicationID) {

        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setResponse(objectFactory.createResponse());
        try {
            Wrapper request = (Wrapper)unmarshaller.unmarshal(new StreamSource(new StringReader(requestBody)));

            // check if the user is correctly logged in
            checkSession(request);

            ApplicationInfo appInfo = applicationsHolder.terminateApplication(applicationID, true);

            wrapper.getResponse().setApplications(objectFactory.createApplicationsList());
            com.mscg.appstarter.beans.jaxb.ApplicationInfo appInfoXML = objectFactory.createApplicationInfo();
            appInfoXML.setId(appInfo.getId());
            appInfoXML.setName(appInfo.getName());
            appInfoXML.setRunning(applicationsHolder.isApplicationRunning(appInfo.getId()));
            wrapper.getResponse().getApplications().getApplication().add(appInfoXML);
            wrapper.getResponse().setStatus(ResponseCode.OK.getStatus());

        } catch (InvalidRequestException e) {
            LOG.error("Invalid login parameters", e);
            wrapper = getWrapperForException(e);
        } catch (ApplicationNotConfiguredException e) {
            LOG.error("Cannot stop application", e);
            wrapper = getWrapperForException(e, ResponseCode.ERR_APPLICATION_NOT_CONFIGURED);
        } catch (ApplicationNotRunningException e) {
            LOG.error("Application not running", e);
            wrapper = getWrapperForException(e, ResponseCode.ERR_APPLICATION_NOT_RUNNING);
        } catch (Exception e) {
            LOG.error("Cannot execute terminate application action", e);
            wrapper = getWrapperForException(e);
        }
        model.addAttribute("object", wrapper);
        return "response";
    }

}
