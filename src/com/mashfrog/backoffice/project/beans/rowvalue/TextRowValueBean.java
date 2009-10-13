package com.mashfrog.backoffice.project.beans.rowvalue;

public class TextRowValueBean implements RowValueBean {

	private static final long serialVersionUID = 8210290797371834308L;

	protected String text;
    protected String type;
    protected boolean enabled;

    public TextRowValueBean(){
    	this(null);
    }

    public TextRowValueBean(String text){
    	type = "text";
    	setText(text);
    }

    public String getText(){
        return text;
    }

    public String getType(){
        return type;
    }

    public boolean getEnabled(){
        return enabled;
    }

    public void setText(String text){
    	this.text = text;
    }

    public void setType(String type){
    	this.type = type;
    }

    public void setEnabled(boolean enabled){
    	this.enabled = enabled;
    }

}