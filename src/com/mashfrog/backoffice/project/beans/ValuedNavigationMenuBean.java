package com.mashfrog.backoffice.project.beans;

public class ValuedNavigationMenuBean extends NavigationMenuBean {

    public ValuedNavigationMenuBean(NavigationMenuBean origNavMenu){
    	super();
    	setItems(origNavMenu.getItems());
    	setMaxLevel(origNavMenu.getMaxLevel());
    	setFixedSize(origNavMenu.isFixedSize());
    }

}
