package com.mashfrog.backoffice.actions;

import java.util.ArrayList;
import java.util.List;

import org.opencms.file.CmsObject;

import com.mashfrog.backoffice.CmsBackofficeActionElement;

public abstract class A_BackofficeAction implements I_BackofficeAction {
    protected List<String> allowedGroups;
    protected String errorMessage;
    protected String fatalErrorMessage;
    protected String jspPath;
    protected CmsBackofficeActionElement backofficeActionElement;

    public A_BackofficeAction(){
    	allowedGroups = new ArrayList<String>();
    }

    public void addAllowedGroup(String allowedGroup){
    	allowedGroups.add(allowedGroup);
    }

    public abstract void execute(CmsBackofficeActionElement backofficeActionElement);

    public List<String> getAllowedGroups(){
        return allowedGroups;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public String getFatalErrorMessage(){
        return fatalErrorMessage;
    }

    public String getJspPath(){
        return jspPath;
    }

    public abstract void setAdditonalConfigurationFilePath(String filePath, CmsObject cmsObject);

    protected void setErrorMessage(String errorMessage){
    	this.errorMessage = errorMessage;
    }

    protected void setFatalErrorMessage(String fatalErrorMessage){
    	this.fatalErrorMessage = fatalErrorMessage;
    }

    protected void setJspPath(String jspPath){
    	this.jspPath = jspPath;
    }

}
