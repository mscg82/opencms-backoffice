package com.mashfrog.backoffice.actions;

import java.util.List;

import org.opencms.file.CmsObject;

import com.mashfrog.backoffice.CmsBackofficeActionElement;

public interface I_BackofficeAction {

    public void addAllowedGroup(String allowedGroup);

    public void execute(CmsBackofficeActionElement backofficeActionElement);

    public List<String> getAllowedGroups();

    public String getErrorMessage();

    public String getFatalErrorMessage();

    public String getJspPath();

    public void setAdditonalConfigurationFilePath(String filePath, CmsObject cmsObject);

}
