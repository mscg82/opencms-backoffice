package com.mashfrog.backoffice;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.i18n.CmsMessages;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.jsp.util.CmsJspContentAccessBean;
import org.opencms.main.CmsLog;
import org.opencms.module.CmsModule;

import com.mashfrog.backoffice.actions.I_BackofficeAction;
import com.mashfrog.backoffice.actions.constants.Constants;
import com.mashfrog.backoffice.bean.request.RequestBean;
import com.mashfrog.backoffice.project.BackofficeProjectBean;
import com.mashfrog.backoffice.project.beans.ActionBean;
import com.mashfrog.backoffice.util.Util;

public class CmsBackofficeActionElement extends CmsJspActionElement implements Serializable {

	private static final long serialVersionUID = -6617556987018565221L;

	// Logger
	private static Log LOG = CmsLog.getLog(CmsBackofficeActionElement.class);

	private BackofficeProjectBean backofficeProject;
    private RequestBean actualRequest;
    private RequestBean previousRequest;
    private I_BackofficeAction currentAction;
    private String resultJsp;
    protected CmsMessages cms_message;

    public CmsBackofficeActionElement(PageContext context, HttpServletRequest req, HttpServletResponse res){
    	super(context, req, res);
    	// init(context, req, res) will be called by super(context, req, res)
    }

    public String getActionLink(String actionName){
    	return "?" + Constants.ACTION_PARAM + "=" + actionName;
    }

    public RequestBean getActualRequest(){
        return actualRequest;
    }

    public BackofficeProjectBean getBackofficeProject(){
        return backofficeProject;
    }

    public CmsJspContentAccessBean getContentAccess(){
    	if(backofficeProject == null)
    		return null;
    	else
    		return backofficeProject.getContentAccess();
    }

    public I_BackofficeAction getCurrentAction(){
    	return currentAction;
    }

    public int getCurrentPageIndex(){
    	int ret = 0;

    	try{
    		ret = Integer.parseInt(getRequest().getParameter(Constants.PAGE_PARAM));
    	} catch (NumberFormatException e) {}

    	return ret;
    }

    public CmsMessages getMessages(){
    	return cms_message;
    }

    public CmsModule getModule(){
    	return Util.getModuleForRequest(this);
    }

    public RequestBean getPreviousRequest(){
        return previousRequest;
    }

    public String getResultJsp() {
    	return resultJsp;
    }

    public void init(PageContext context, HttpServletRequest req, HttpServletResponse res){
    	super.init(context, req, res);

    	// Init CmsMessage object
		cms_message = new CmsMessages(
				Util.getModuleForRequest(this).getName() + ".workplace",
				getCmsObject().getRequestContext().getLocale());

    	backofficeProject = null;
    	try {
			backofficeProject = BackofficeProjectBean.getInstace(this);

			if(backofficeProject == null)
	    		currentAction = null;
	    	else{
	    		String actionName = getRequest().getParameter(Constants.ACTION_PARAM);

	    		// The old actual request is now the previous request
	    		previousRequest = (RequestBean)getRequest().getSession().getAttribute(Constants.ACTUAL_ACTION_SESSION_PARAM);
	    		getRequest().getSession().setAttribute(Constants.PREVIOUS_ACTION_SESSION_PARAM, previousRequest);

	    		// Build the new request bean
	    		actualRequest = new RequestBean(req);
	    		getRequest().getSession().setAttribute(Constants.ACTUAL_ACTION_SESSION_PARAM, actualRequest);

	    		ActionBean actionBean = null;
	    		if(getRequestContext().currentUser().isGuestUser()){
	    			// redirect to login action
	    			actualRequest.setAttribute(Constants.ACTION_PARAM, Constants.LOGIN_DEFAULT_NAME);
	    			actionBean = backofficeProject.getAction(Constants.LOGIN_DEFAULT_NAME);
	    			LOG.warn("User is not logged in. Redirecting to login action.");
	    		}
	    		else {
	    			actionBean = backofficeProject.getAction(actionName);
	    			if(LOG.isDebugEnabled())
	    				LOG.debug("User " + getRequestContext().currentUser().getFullName() + " requested action \"" + actionName + "\"");
	    			if(actionBean == null){
		    			// redirect to default action
	    				actualRequest.setAttribute(Constants.ACTION_PARAM, Constants.NOACTION_DEFAULT_NAME);
		    			actionBean = backofficeProject.getAction(Constants.NOACTION_DEFAULT_NAME);
		    			LOG.info("Choosen action not mapped. Redirecting to default action.");
		    		}
	    		}

	    		currentAction = I_BackofficeAction.Factory.newInstance(this, actionBean);
	    		if(Util.canUserAccessBean(getRequestContext().currentUser(), getCmsObject(), actionBean)){
	    			resultJsp = currentAction.execute();
	    		}
	    		else{
	    			currentAction.setFatalErrorMessage(cms_message.getString("general.error.action.notallowed"));
	    		}
	    		if(resultJsp == null){
	    			resultJsp = currentAction.getJspPath();
	    		}

	    	}

		} catch (Exception e) {
			LOG.error("Error found while initializing " + this.getClass().getName() + " instance.", e);
		}
    }

}
