package com.mashfrog.backoffice.project.beans;

import java.util.LinkedHashSet;
import java.util.Set;

public class BaseCommandMenuBean {
    protected Set<String> groups;
    protected String label;
    protected Set<String> orgUnits;

    public BaseCommandMenuBean(){
    	this(null);
    }

    public BaseCommandMenuBean(String label){
    	groups = new LinkedHashSet<String>();
    	orgUnits = new LinkedHashSet<String>();
    	setLabel(label);
    }

    public void addGroup(String group){
    	groups.add(group);
    }

    public void addOrgUnit(String orgUnit){
    	orgUnits.add(orgUnit);
    }

    public Set<String> getGroups(){
        return groups;
    }

    public Set<String> getOrgUnits(){
        return orgUnits;
    }

    public String getLabel(){
        return label;
    }

    public void setGroups(Set<String> groups){
    	this.groups = groups;
    }

    public void setLabel(String label){
    	this.label = label;
    }

    public void setOrgUnits(Set<String> orgUnits){
    	this.orgUnits = orgUnits;
    }

}
