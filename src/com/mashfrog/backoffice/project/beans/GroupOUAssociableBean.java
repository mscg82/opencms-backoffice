package com.mashfrog.backoffice.project.beans;

import java.util.LinkedHashSet;
import java.util.Set;

public class GroupOUAssociableBean {

	protected Set<String> groups;
    protected Set<String> orgUnits;

    public GroupOUAssociableBean(){
    	groups = new LinkedHashSet<String>();
    	orgUnits = new LinkedHashSet<String>();
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

    public void setGroups(Set<String> groups){
    	this.groups = groups;
    }

    public void setOrgUnits(Set<String> orgUnits){
    	this.orgUnits = orgUnits;
    }

	@Override
	public String toString() {
		return "{groups: " + getGroups() + "; org. units: " + getOrgUnits() + "}";
	}

}
