package com.mashfrog.backoffice.project.beans;

import java.io.Serializable;

public class NavigableValueBean implements Serializable{

	private static final long serialVersionUID = -6496528479189238222L;

	private String label;
    private String value;

    public NavigableValueBean(){
    	this(null, null);
    }

    public NavigableValueBean(String label, String value){
    	setLabel(label);
    	setValue(value);
    }

    public String getLabel(){
        return label;
    }

    public String getValue(){
        return value;
    }

    public void setLabel(String label){
    	this.label = label;
    }

    public void setValue(String value){
    	this.value = value;
    }

}
