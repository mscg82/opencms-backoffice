package com.mashfrog.backoffice.project.beans;

import java.util.LinkedList;
import java.util.List;


public class ValuedNavigationItemBean extends NavigationItemBean {

	private static final long serialVersionUID = -8141546109035409259L;

	private List<NavigableValueBean> values;

    public ValuedNavigationItemBean(NavigationItemBean origNavItem){
    	super(origNavItem.getItemLevel());
    	setLabel(origNavItem.getLabel());
    	setNewElementLabel(origNavItem.getNewElementLabel());
    	setListHeader(origNavItem.getListHeader());
    	values = new LinkedList<NavigableValueBean>();
    }

	public void addValue(NavigableValueBean value) {
		values.add(value);
	}

	public List<NavigableValueBean> getValues() {
		return values;
	}

	public void setValues(List<NavigableValueBean> values) {
		this.values = values;
	}

}
