package com.mashfrog.backoffice.project.beans.rowvalue;

public class IconRowValueBean extends TitledRowValueBean {

	private static final long serialVersionUID = 4075395044220740355L;

	protected String icon;

    public IconRowValueBean(){
    	this(null, null);
    }

    public IconRowValueBean(String icon, String title){
    	super(title);
    	type = "icon";
    	setIcon(icon);
    }

    public String getIcon(){
        return icon;
    }

    public void setIcon(String icon){
    	this.icon = icon;
    }

}
