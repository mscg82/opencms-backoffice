package com.mashfrog.backoffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
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

public class CmsBackofficeActionElement extends CmsJspActionElement {
	// Logger
	private static Log LOG = CmsLog.getLog(CmsBackofficeActionElement.class);

	private PageContext pageContext;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private BackofficeProjectBean backofficeProject;
    private RequestBean actualRequest;
    private RequestBean previousRequest;
    private I_BackofficeAction currentAction;

    public CmsBackofficeActionElement(PageContext context, HttpServletRequest req, HttpServletResponse res){
    	super(context, req, res);
    	// init(context, req, res) will be called by super(context, req, res)
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
    		ret = Integer.parseInt(request.getParameter(Constants.PAGE_PARAM));
    	} catch (NumberFormatException e) {}

    	return ret;
    }

    public CmsModule getModule(){
    	return Util.getModuleForRequest(this);
    }

    public PageContext getPageContext(){
        return pageContext;
    }

    public RequestBean getPreviousRequest(){
        return previousRequest;
    }

    public HttpServletRequest getRequest(){
        return request;
    }

    public HttpServletResponse getResponse(){
        return response;
    }

    public void init(PageContext context, HttpServletRequest req, HttpServletResponse res){
    	super.init(context, req, res);

    	pageContext = context;
    	request = req;
    	response = res;

    	backofficeProject = null;
    	try {
			backofficeProject = BackofficeProjectBean.getInstace(this);

			if(backofficeProject == null)
	    		currentAction = null;
	    	else{
	    		String actionName = request.getParameter(Constants.ACTION_PARAM);

	    		// The old actual request is now the previous request
	    		previousRequest = (RequestBean)request.getSession().getAttribute(Constants.ACTUAL_ACTION_SESSION_PARAM);
	    		request.getSession().setAttribute(Constants.PREVIOUS_ACTION_SESSION_PARAM, previousRequest);

	    		// Build the new request bean
	    		actualRequest = new RequestBean(req);
	    		request.getSession().setAttribute(Constants.ACTUAL_ACTION_SESSION_PARAM, actualRequest);

	    		ActionBean actionBean = null;
	    		if(getRequestContext().currentUser().isGuestUser()){
	    			// redirect to login action
	    			actionBean = backofficeProject.getAction(Constants.LOGIN_DEFAULT_NAME);
	    			LOG.warn("User is not logged in. Redirecting to login action.");
	    		}
	    		else if(actionBean == null){
	    			// redirect to default action
	    			actionBean = backofficeProject.getAction(Constants.NOACTION_DEFAULT_NAME);
	    			LOG.info("Choosen action not mapped. Redirecting to default action.");
	    		}
	    		else{
	    			actionBean = backofficeProject.getAction(actionName);
	    			if(LOG.isDebugEnabled())
	    				LOG.debug("User " + getRequestContext().currentUser().getFullName() + " requested action \"" + actionName + "\"");
	    		}
	    		currentAction = I_BackofficeAction.Factory.newInstance(this, actionBean);
	    		currentAction.execute();

	    	}

		} catch (Exception e) {
			LOG.error("Error found while initializing " + this.getClass().getName() + " instance.", e);
		}
    }

}
