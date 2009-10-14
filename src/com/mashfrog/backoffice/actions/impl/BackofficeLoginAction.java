package com.mashfrog.backoffice.actions.impl;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.opencms.db.CmsUserSettings;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsProject;
import org.opencms.i18n.CmsMessageException;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.security.CmsOrganizationalUnit;
import org.opencms.workplace.CmsLogin;

import com.mashfrog.backoffice.actions.A_BackofficeAction;
import com.mashfrog.backoffice.actions.RedirectingAction;
import com.mashfrog.backoffice.actions.constants.Constants;

public class BackofficeLoginAction extends A_BackofficeAction implements RedirectingAction{

	private static Log LOG = CmsLog.getLog(BackofficeLoginAction.class);

	private boolean sendRedirect;
	private boolean hasBody;

	public BackofficeLoginAction() {
		super();
		sendRedirect = false;
		hasBody = true;
	}

	public String execute(){
    	Boolean login = new Boolean(backofficeActionElement.getActualRequest().getAttribute(Constants.LOGIN_EXECUTE_PARAM));
    	if(login){
    		String username = backofficeActionElement.getActualRequest().getAttribute(Constants.LOGIN_USERNAME_PARAM);
    		String password = backofficeActionElement.getActualRequest().getAttribute(Constants.LOGIN_PASSWORD_PARAM);
    		String orgUnit = backofficeActionElement.getActualRequest().getAttribute(Constants.LOGIN_ORGUNIT_PARAM);

    		boolean wrongOU = true;

    		if(backofficeActionElement.getBackofficeProject().getOrgUnit().startsWith(orgUnit)) {
	    		username = (orgUnit == null ? CmsOrganizationalUnit.SEPARATOR : orgUnit) + username;

	    		CmsLogin cmsLogin = new CmsLogin(backofficeActionElement.getJspContext(), backofficeActionElement.getRequest(), backofficeActionElement.getResponse());

	    		LOG.debug("Trying to login user \"" + username + "\" with password \"" + password + "\"");
	    		cmsLogin.login(username, password);

	    		if(cmsLogin.isLoginSuccess()) {
	    			LOG.debug("Login successfully executed");
	    			CmsObject obj = cmsLogin.getCmsObject();
	    			CmsUserSettings settings = new CmsUserSettings(obj);
	    			try {
						CmsProject project = obj.readProject(settings.getStartProject());
						if (obj.getAllAccessibleProjects().contains(project)) {
							cmsLogin.getRequestContext().setCurrentProject(project);
							LOG.debug("Project set to \"" + project + "\"");
							sendRedirect = true;
			    			hasBody = false;
			    			wrongOU = false;
						}

					} catch (CmsException e) {
						LOG.warn("Cannot read project \"" + settings.getStartProject() + "\"");
					}
	    		}
	    		else {
	    			LOG.debug("Wrong credentials provided.");
	    			wrongOU = false;
	    			try {
						setErrorMessage(backofficeActionElement.getMessages().getString("login.error.wrongcredentials"));
					} catch (CmsMessageException e) {
						LOG.warn("An error occurred while reading localization string.", e);
						setErrorMessage("Il nome utente o la password immesse non sono corrette.");
					}
	    		}
    		}

    		if(wrongOU) {
    			LOG.debug("Wrong organizational unit selected.");
    			try {
					setErrorMessage(backofficeActionElement.getMessages().getString("login.error.wrongsite"));
				} catch (CmsMessageException e) {
					LOG.warn("An error occurred while reading localization string.", e);
					setErrorMessage("Al suo account non &egrave; permesso l'accesso a questo backoffice.<br/>Per favore, ritenti il login sul backoffice corretto.");
				}
    		}

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
