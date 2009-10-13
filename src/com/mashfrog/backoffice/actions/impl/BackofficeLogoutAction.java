package com.mashfrog.backoffice.actions.impl;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.mashfrog.backoffice.actions.A_BackofficeAction;
import com.mashfrog.backoffice.actions.RedirectingAction;
import com.mashfrog.backoffice.actions.constants.Constants;

public class BackofficeLogoutAction extends A_BackofficeAction implements RedirectingAction{

	private static Log LOG = CmsLog.getLog(BackofficeLogoutAction.class);

	public String execute() {
		HttpSession session = backofficeActionElement.getRequest().getSession(false);
		if(session != null)
			session.invalidate();
		return null;
	}

	@Override
	public boolean getHasCommandMenu() {
		return false;
	}

	@Override
	public boolean hasBody() {
		return true;
	}

	public void redirect() {
		/*
		CmsLogin login = new CmsLogin(backofficeActionElement.getPageContext(), backofficeActionElement.getRequest(), backofficeActionElement.getResponse());
		try {
			login.logout();
		} catch (IOException e) {
			LOG.error("Cannot logout user.", e);
		}
		*/

		try {

			backofficeActionElement.getResponse().sendRedirect("?" + Constants.ACTION_PARAM + "=" + Constants.LOGIN_DEFAULT_NAME);
		} catch (IOException e) {
			LOG.error("Cannot redirect to login action.", e);
		}

	}

	public boolean sendRedirect() {
		return true;
	}

}
