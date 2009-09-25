package com.mashfrog.backoffice.project.beans.rowvalue;

public class ComboBoxValueBean {
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
