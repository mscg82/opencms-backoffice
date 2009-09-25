package com.mashfrog.backoffice.project.beans.rowvalue;

public class IconRowValueBean extends TitledRowValueBean {
    protected String icon;

    public IconRowValueBean(){
    	this(null, null);
    }

    public IconRowValueBean(String icon, String title){
    	super(title);
    	setIcon(icon);
    }

    public String getIcon(){
        return icon;
    }

    public void setIcon(String icon){
    	this.icon = icon;
    }

}
