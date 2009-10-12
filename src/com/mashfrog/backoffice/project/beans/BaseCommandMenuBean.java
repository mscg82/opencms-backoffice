package com.mashfrog.backoffice.project.beans;


public class BaseCommandMenuBean extends GroupOUAssociableBean{

	private static final long serialVersionUID = -7217144436096131361L;

	protected String label;

    public BaseCommandMenuBean(){
    	this(null);
    }

    public BaseCommandMenuBean(String label){
    	super();
    	setLabel(label);
    }

    public String getLabel(){
        return label;
    }

    public void setLabel(String label){
    	this.label = label;
    }

}
