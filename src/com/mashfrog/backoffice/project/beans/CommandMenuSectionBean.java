package com.mashfrog.backoffice.project.beans;

import java.util.ArrayList;
import java.util.List;

public class CommandMenuSectionBean extends BaseCommandMenuBean {

	private static final long serialVersionUID = -5284699571857842696L;

	protected List<CommandMenuItemBean> items;

    public CommandMenuSectionBean(){
    	this((String)null);
    }

    public CommandMenuSectionBean(String label){
    	super(label);
    	items = new ArrayList<CommandMenuItemBean>();
    }

    public CommandMenuSectionBean(CommandMenuSectionBean orig){
    	super(orig.getLabel());
    	setActual(orig.isActual());
    	setGroups(orig.getGroups());
    	setOrgUnits(orig.getOrgUnits());
    	items = new ArrayList<CommandMenuItemBean>();
    }

    public void addItem(CommandMenuItemBean item){
    	items.add(item);
    	item.setMenuSection(this);
    }

    public List<CommandMenuItemBean> getItems(){
        return items;
    }

    public void setItems(List<CommandMenuItemBean> items){
    	this.items = items;
    	for(CommandMenuItemBean item : this.items){
    		item.setMenuSection(this);
    	}
    }

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("    Items in section \"" + getLabel() + "\":\n");
		for(CommandMenuItemBean item : items){
			ret.append("        " + item.toString() + "\n");
		}
		return ret.toString();
	}

}
