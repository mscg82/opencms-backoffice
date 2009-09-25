package com.mashfrog.backoffice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.jsp.util.CmsJspContentAccessBean;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;

import com.mashfrog.backoffice.actions.I_BackofficeAction;
import com.mashfrog.backoffice.actions.constants.Constants;
import com.mashfrog.backoffice.bean.request.RequestBean;
import com.mashfrog.backoffice.project.BackofficeProjectBean;

public class CmsBackofficeActionElement extends CmsJspActionElement {
	// Logger
	private static Log LOG = CmsLog.getLog(CmsBackofficeActionElement.class);

	private PageContext pageContext;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private BackofficeProjectBean backofficeProject;
    private RequestBean actualRequest;
    private RequestBean previousRequest;

    public CmsBackofficeActionElement(PageContext context, HttpServletRequest req, HttpServletResponse res){
    	super(context, req, res);
    	init(context, req, res);
    }

    public CmsJspContentAccessBean getContentAccess(){
    	if(backofficeProject == null)
    		return null;
    	else
    		return backofficeProject.getContentAccess();
    }

    public PageContext getPageContext(){
        return pageContext;
    }

    public HttpServletRequest getRequest(){
        return request;
    }

    public HttpServletResponse getResponse(){
        return response;
    }

    public RequestBean getActualRequest(){
        return actualRequest;
    }

    public RequestBean getPreviousRequest(){
        return previousRequest;
    }

    public BackofficeProjectBean getBackofficeProject(){
        return backofficeProject;
    }

    public I_BackofficeAction getCurrentAction(){
    	if(backofficeProject == null)
    		return null;
    	else
    		return backofficeProject.getAction(request.getParameter(Constants.ACTION_PARAM));
    }

    public int getCurrentPageIndex(){
    	int ret = 0;

    	try{
    		ret = Integer.parseInt(request.getParameter(Constants.PAGE_PARAM));
    	}catch (NumberFormatException e) {}

    	return ret;
    }

    public void init(PageContext context, HttpServletRequest req, HttpServletResponse res){
    	super.init(context, req, res);

    	pageContext = context;
    	request = req;
    	response = res;

    	backofficeProject = null;
    	try {
			backofficeProject = BackofficeProjectBean.getInstace(this);
		} catch (CmsException e) {
			LOG.error("Error found while initializing " + this.getClass().getName() + " instance.", e);
		}
    }

}
