package com.mashfrog.backoffice.project.beans.rowvalue;

import java.io.Serializable;

public class ComboBoxValueBean implements Serializable{

	private static final long serialVersionUID = 3364226946041041797L;

	private String text;
    private String value;

    public ComboBoxValueBean(){
    	this(null, null);
    }

    public ComboBoxValueBean(String text, String value){
    	setText(text);
    	setValue(value);
    }

    public String getText(){
        return text;
    }

    public String getValue(){
        return value;
    }

    public void setText(String text){
    	this.text = text;
    }

    public void setValue(String value){
    	this.value = value;
    }

}
