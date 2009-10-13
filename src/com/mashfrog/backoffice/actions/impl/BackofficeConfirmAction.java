package com.mashfrog.backoffice.actions.impl;

import com.mashfrog.backoffice.actions.A_BackofficeAction;
import com.mashfrog.backoffice.actions.RedirectingAction;
import com.mashfrog.backoffice.bean.request.RequestBean;

public class BackofficeConfirmAction extends A_BackofficeAction implements RedirectingAction{

    public String execute(){
    	return null;
    }

    @Override
	public boolean getHasCommandMenu() {
		return false;
	}

    public RequestBean getNextPageRequest(){
        return null;
    }

	public RequestBean getPreviuosPageRequest(){
        return null;
    }

	public void redirect() {

	}

	public boolean sendRedirect() {
		return false;
	}

}
