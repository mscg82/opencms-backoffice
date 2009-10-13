package com.mashfrog.backoffice.actions.impl;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.opencms.i18n.CmsMessageException;
import org.opencms.i18n.CmsMessages;
import org.opencms.main.CmsLog;
import org.opencms.security.CmsOrganizationalUnit;
import org.opencms.workplace.CmsLogin;

import com.mashfrog.backoffice.CmsBackofficeActionElement;
import com.mashfrog.backoffice.actions.A_BackofficeAction;
import com.mashfrog.backoffice.actions.RedirectingAction;
import com.mashfrog.backoffice.actions.constants.Constants;
import com.mashfrog.backoffice.project.beans.ActionBean;
import com.mashfrog.backoffice.util.Util;

public class BackofficeLoginAction extends A_BackofficeAction implements RedirectingAction{

	private static Log LOG = CmsLog.getLog(BackofficeLoginAction.class);

	private boolean sendRedirect;
	private boolean hasBody;

	protected CmsMessages cms_message;

	@Override
	public void init(CmsBackofficeActionElement backofficeActionElement, ActionBean actionBean) {
		super.init(backofficeActionElement, actionBean);

		sendRedirect = false;
		hasBody = true;

		// Init CmsMessage object
		cms_message = new CmsMessages(
				Util.getModuleForRequest(backofficeActionElement).getName() + ".workplace",
				backofficeActionElement.getCmsObject().getRequestContext().getLocale());
	}

	public String execute(){
    	Boolean login = new Boolean(backofficeActionElement.getActualRequest().getAttribute(Constants.LOGIN_EXECUTE_PARAM));
    	if(login){
    		String username = backofficeActionElement.getActualRequest().getAttribute(Constants.LOGIN_USERNAME_PARAM);
    		String password = backofficeActionElement.getActualRequest().getAttribute(Constants.LOGIN_PASSWORD_PARAM);
    		String orgUnit = backofficeActionElement.getActualRequest().getAttribute(Constants.LOGIN_ORGUNIT_PARAM);

    		username = (orgUnit == null ? CmsOrganizationalUnit.SEPARATOR : orgUnit) + username;

    		CmsLogin cmsLogin = new CmsLogin(backofficeActionElement.getJspContext(), backofficeActionElement.getRequest(), backofficeActionElement.getResponse());

    		LOG.debug("Trying to login user \"" + username + "\" with password \"" + password + "\"");
    		cmsLogin.login(username, password);
    		if(cmsLogin.isLoginSuccess()){
    			LOG.debug("Login successfully executed");
    		}
    		else{
    			LOG.debug("Wrong credentials provided.");
    			try {
					setErrorMessage(cms_message.getString("login.error.wrongcredentials"));
				} catch (CmsMessageException e) {
					LOG.warn("An error occurred while reading localization string.", e);
					setErrorMessage("Il nome utente o la password immesse non sono corrette.");
				}
    		}

    	}
    	else{
    		sendRedirect = false;
    	}
    	return null;
    }

    public String getExecuteParam() {
		return Constants.LOGIN_EXECUTE_PARAM;
	}

    @Override
	public boolean getHasCommandMenu() {
		return false;
	}

    public String getPasswordParam() {
    	return Constants.LOGIN_PASSWORD_PARAM;
    }

	public String getUsernameParam() {
    	return Constants.LOGIN_USERNAME_PARAM;
    }

	@Override
	public boolean hasBody() {
		return hasBody;
	}

	public void redirect() {
		try {
			backofficeActionElement.getResponse().sendRedirect("?" + Constants.ACTION_PARAM + "=" + Constants.NOACTION_DEFAULT_NAME);
		} catch (IOException e) {
			LOG.error("Cannot redirect to default action.", e);
		}
	}

	public boolean sendRedirect() {
		return sendRedirect;
	}

}
