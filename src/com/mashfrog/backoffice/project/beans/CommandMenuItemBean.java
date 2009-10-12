package com.mashfrog.backoffice.project.beans;

public class CommandMenuItemBean extends BaseCommandMenuBean {

	private static final long serialVersionUID = 1888615502615538465L;

	protected String function;
    protected String icon;

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
    	super(label);
    	setFunction(function);
    	setIcon(icon);
    }

    public String getFunction(){
        return function;
    }

    public String getIcon(){
        return icon;
    }

    public void setFunction(String function){
    	this.function = function;
    }

    public void setIcon(String icon){
    	this.icon = icon;
    }

	@Override
	public String toString() {
		return "Label: " + getLabel() + "; Allowed groups: " + getGroups() + "; Allowed org. units: " + getOrgUnits();
	}

}
