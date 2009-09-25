package com.mashfrog.backoffice.project.beans.rowvalue;

public class LinkedIconRowValueBean extends IconRowValueBean {
    protected String action;

    public LinkedIconRowValueBean(){
    	this(null, null, null);
    }

    public LinkedIconRowValueBean(String icon, String title, String action){
    	super(icon, title);
    	setAction(action);
    }

    public String getAction(){
        return action;
    }

    public void setAction(String action){
    	this.action = action;
    }

}
