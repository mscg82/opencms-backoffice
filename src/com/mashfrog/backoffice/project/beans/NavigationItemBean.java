package com.mashfrog.backoffice.project.beans;

import java.io.Serializable;

public class NavigationItemBean implements Serializable{

	private static final long serialVersionUID = -7263651790184869629L;

	protected String label;
    protected String newElementLabel;
    protected String listHeader;
    protected int itemLevel;

    public NavigationItemBean(){

    }

    public NavigationItemBean(int itemLevel){
    	setItemLevel(itemLevel);
    }

    public String getLabel(){
        return label;
    }

    public String getNewElementLabel(){
        return newElementLabel;
    }

    public String getListHeader(){
        return listHeader;
    }

    public int getItemLevel(){
        return itemLevel;
    }

    public void setLabel(String label){
    	this.label = label;
    }

    public void setNewElementLabel(String newElementLabel){
    	this.newElementLabel = newElementLabel;
    }

    public void setListHeader(String listHeader){
    	this.listHeader = listHeader;
    }

    public void setItemLevel(int itemLevel){
    	this.itemLevel = itemLevel;
    }

	@Override
	public String toString() {
		return "Label: " + getLabel() + ", new element label: " + getNewElementLabel() + ", " +
			   "level: " + getItemLevel() + ", list header: " + getListHeader();
	}

}
