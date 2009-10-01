package com.mashfrog.backoffice.actions.impl;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.mashfrog.backoffice.actions.A_BackofficeAction;
import com.mashfrog.backoffice.actions.RedirectingAction;
import com.mashfrog.backoffice.actions.constants.Constants;

public class BackofficeLoginAction extends A_BackofficeAction implements RedirectingAction{

	private static Log LOG = CmsLog.getLog(BackofficeLoginAction.class);

    public String execute(){
    	return null;
    }

	public void redirect() {
		try {
			backofficeActionElement.getResponse().sendRedirect("?" + Constants.ACTION_PARAM + "=" + Constants.NOACTION_DEFAULT_NAME);
		} catch (IOException e) {
			LOG.error("Cannot redirect to default action.", e);
		}
	}

}
