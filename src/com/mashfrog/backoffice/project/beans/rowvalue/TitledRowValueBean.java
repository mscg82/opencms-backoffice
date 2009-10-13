package com.mashfrog.backoffice.project.beans.rowvalue;

public class TitledRowValueBean implements RowValueBean {

	private static final long serialVersionUID = -4868820202061262817L;

	protected String title;
    protected String type;
    protected boolean enabled;

    public TitledRowValueBean(){
    	this(null);
    }

    public TitledRowValueBean(String title){
    	type = "titled";
    	setTitle(title);
    }

    public String getTitle(){
        return title;
    }

    public String getType(){
        return type;
    }

    public boolean getEnabled(){
        return enabled;
    }

    public void setTitle(String title){
    	this.title = title;
    }

    public void setType(String type){
    	this.type = type;
    }

    public void setEnabled(boolean enabled){
    	this.enabled = enabled;
    }

}
