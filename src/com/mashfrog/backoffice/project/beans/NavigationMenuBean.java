package com.mashfrog.backoffice.project.beans;

import java.util.ArrayList;
import java.util.List;

public class NavigationMenuBean {
    protected List<NavigationItemBean> items;
    protected Integer maxLevel;
    protected boolean fixedSize;

    public NavigationMenuBean(){
    	items = new ArrayList<NavigationItemBean>();
    }

    public void addItem(NavigationItemBean item){
    	item.setItemLevel(items.size());
    	items.add(item);
    }

    public List<NavigationItemBean> getItems(){
        return items;
    }

    public Integer getMaxLevel(){
        return maxLevel;
    }

    public boolean isFixedSize(){
        return fixedSize;
    }

    public void setFixedSize(boolean fixedSize) {
		this.fixedSize = fixedSize;
	}

    public void setItems(List<NavigationItemBean> items){
    	this.items = items;
    }

	public void setMaxLevel(Integer maxLevel){
    	this.maxLevel = maxLevel;
    }

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("Max level: " + getMaxLevel() + ", fixed size: " + isFixedSize() + ", items:\n");
		for(NavigationItemBean item : getItems()){
			ret.append("    " + item.toString() + "\n");
		}
		return ret.toString();
	}

}
