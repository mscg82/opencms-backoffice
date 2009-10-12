package com.mashfrog.backoffice.project.beans;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class OSWorkflowBean implements Serializable{

	private static final long serialVersionUID = 2578291777027814022L;

	private String workflowName;
    private int initialState;
    private Set<Integer> publishIds;
    private Set<Integer> unpublishIds;
    private Set<Integer> editIds;
    private Set<Integer> deleteIds;
    private Set<Integer> exportIds;

    public OSWorkflowBean(){
    	publishIds = new LinkedHashSet<Integer>();
        unpublishIds = new LinkedHashSet<Integer>();
        editIds = new LinkedHashSet<Integer>();
        deleteIds = new LinkedHashSet<Integer>();
        exportIds = new LinkedHashSet<Integer>();
    }

    public String getWorkflowName(){
        return workflowName;
    }

    public int getInitialState(){
        return initialState;
    }

    public Set<Integer> getPublishIds(){
        return publishIds;
    }

    public Set<Integer> getUnpublishIds(){
        return unpublishIds;
    }

    public Set<Integer> getEditIds(){
        return editIds;
    }

    public Set<Integer> getDeleteIds(){
        return deleteIds;
    }

    public Set<Integer> getExportIds(){
        return exportIds;
    }

    public void addUnpublishId(Integer id){
    	unpublishIds.add(id);

    }

    public void addEditId(Integer id){
    	editIds.add(id);
    }

    public void addDeleteId(Integer id){
    	deleteIds.add(id);
    }

    public void addExportId(Integer id){
    	exportIds.add(id);
    }

    public void addPublishId(Integer id){
    	publishIds.add(id);
    }

    public void setWorkflowName(String workflowName){
    	this.workflowName = workflowName;
    }

    public void setInitialState(int initialState){
    	this.initialState = initialState;
    }

    public void setPublishIds(Set<String> ids){
    	for(String id : ids){
    		try{
	    		Integer value = Integer.parseInt(id);
	    		publishIds.add(value);
    		} catch(NumberFormatException e){}
    	}
    }

    public void setUnpublishIds(Set<String> ids){
    	for(String id : ids){
    		try{
	    		Integer value = Integer.parseInt(id);
	    		unpublishIds.add(value);
    		} catch(NumberFormatException e){}
    	}
    }

    public void setEditIds(Set<String> ids){
    	for(String id : ids){
    		try{
	    		Integer value = Integer.parseInt(id);
	    		editIds.add(value);
    		} catch(NumberFormatException e){}
    	}
    }

    public void setDeleteIds(Set<String> ids){
    	for(String id : ids){
    		try{
	    		Integer value = Integer.parseInt(id);
	    		deleteIds.add(value);
    		} catch(NumberFormatException e){}
    	}
    }

    public void setExportIds(Set<String> ids){
    	for(String id : ids){
    		try{
	    		Integer value = Integer.parseInt(id);
	    		exportIds.add(value);
    		} catch(NumberFormatException e){}
    	}
    }

    public boolean isSpecialId(Integer id){
        return
        	publishIds.contains(id) ||
        	unpublishIds.contains(id) ||
        	editIds.contains(id) ||
        	deleteIds.contains(id) ||
        	exportIds.contains(id);
    }

	@Override
	public String toString() {
		return
			"Workflow name: " + getWorkflowName() + ", initial state: " + getInitialState() + ";\n" +
			"    publish ids:   " + publishIds + "\n" +
			"    unpublish ids: " + unpublishIds + "\n" +
			"    edit ids:      " + editIds + "\n" +
			"    delete ids:    " + deleteIds + "\n" +
			"    export ids:    " + exportIds;
	}

}
