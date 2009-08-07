package com.mashfrog.backoffice.project.beans;

public class NavigationItemBean {
    protected String label;
    protected String newElementLabel;
    protected String listHeader;
    protected int itemLevel;

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

}
