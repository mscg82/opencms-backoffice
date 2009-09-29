package com.mashfrog.backoffice.project.beans;


public class BaseCommandMenuBean extends GroupOUAssociableBean{

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
