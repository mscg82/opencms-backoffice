package com.mashfrog.backoffice.project.beans.rowvalue;

public class LinkedIconRowValueBean extends IconRowValueBean {

	private static final long serialVersionUID = 7206030104878712889L;

	protected String action;

    public LinkedIconRowValueBean(){
    	this(null, null, null);
    }

    public LinkedIconRowValueBean(String icon, String title, String action){
    	super(icon, title);
    	type = "linked_icon";
    	setAction(action);
    }

    public String getAction(){
        return action;
    }

    public void setAction(String action){
    	this.action = action;
    }

}
