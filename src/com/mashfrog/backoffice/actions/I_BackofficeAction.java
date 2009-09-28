package com.mashfrog.backoffice.actions;

import java.util.List;

import org.opencms.file.CmsObject;

import com.mashfrog.backoffice.CmsBackofficeActionElement;
import com.mashfrog.backoffice.project.beans.ActionBean;

public interface I_BackofficeAction {

	public static class Factory {

		public static I_BackofficeAction newInstance(CmsBackofficeActionElement backofficeActionElement, ActionBean actionBean){
			return null;
		}

	}

    public void addAllowedGroup(String allowedGroup);

    public void execute();

    public List<String> getAllowedGroups();

    public String getErrorMessage();

    public String getFatalErrorMessage();

    public String getJspPath();

    public void init(CmsBackofficeActionElement backofficeActionElement, ActionBean actionBean);

    public void setAdditonalConfigurationFilePath(String filePath, CmsObject cmsObject);

}
