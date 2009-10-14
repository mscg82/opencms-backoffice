package com.mashfrog.backoffice.project.beans;

import com.mashfrog.backoffice.actions.constants.Constants;

public class CommandMenuItemBean extends BaseCommandMenuBean {

	private static final long serialVersionUID = 1888615502615538465L;

	protected String function;
    protected String icon;
    protected CommandMenuSectionBean menuSection;

    public CommandMenuItemBean(){
    	this(null, null, null);
    }

    public CommandMenuItemBean(String label){
    	this(label, null, null);
    }

    public CommandMenuItemBean(String label, String function){
    	this(label, function, null);
    }

    public CommandMenuItemBean(String label, String function, String icon){
    	this(label, function, icon, null);
    }

    public CommandMenuItemBean(String label, String function, String icon, CommandMenuSectionBean menuSection){
    	super(label);
    	setFunction(function);
    	setIcon(icon);
    	setMenuSection(menuSection);
    }

    public CommandMenuItemBean(CommandMenuItemBean orig){
    	super(orig.getLabel());
    	setActual(orig.isActual());
    	setFunction(orig.getFunction());
    	setGroups(orig.getGroups());
    	setIcon(orig.getIcon());
    	setMenuSection(orig.getMenuSection());
    	setOrgUnits(orig.getOrgUnits());
    }

	public String getFunction(){
        return function;
    }

	public String getFunctionLink(){
		return "?" + Constants.ACTION_PARAM + "=" + getFunction();
	}

	public String getIcon(){
        return icon;
    }

    public CommandMenuSectionBean getMenuSection() {
		return menuSection;
	}

    public void setFunction(String function){
    	this.function = function;
    }

    public void setIcon(String icon){
    	this.icon = icon;
    }

    public void setMenuSection(CommandMenuSectionBean menuSection) {
		this.menuSection = menuSection;
	}

	@Override
	public String toString() {
		return "Label: " + getLabel() + "; Icon: " + getIcon() + "; Allowed groups: " + getGroups() + "; Allowed org. units: " + getOrgUnits();
	}

}
