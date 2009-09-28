package com.mashfrog.backoffice.project.beans;

import java.util.ArrayList;
import java.util.List;

public class ValuedNavigationMenuBean extends NavigationMenuBean {

    public ValuedNavigationMenuBean(NavigationMenuBean origNavMenu){
    	super();

    	for(NavigationItemBean baseItem : origNavMenu.getItems()){
    		items.add(new ValuedNavigationItemBean(baseItem));
    	}

    	setMaxLevel(origNavMenu.getMaxLevel());
    	setFixedSize(origNavMenu.isFixedSize());
    }

    public List<ValuedNavigationItemBean> getValuedItems() {
    	List<ValuedNavigationItemBean> ret =
    		new ArrayList<ValuedNavigationItemBean>(getItems().size());
    	for(NavigationItemBean baseItem : getItems()){
    		ret.add((ValuedNavigationItemBean)baseItem);
    	}
    	return ret;
    }

}
