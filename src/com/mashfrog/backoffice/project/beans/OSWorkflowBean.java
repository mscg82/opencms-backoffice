package com.mashfrog.backoffice.project.beans;

public class OSWorkflowBean {
    private String workflowName;
    private int initialState;
    private Set<Integer> publishIds;
    private Set<Integer> unpublishIds;
    private Set<Integer> editIds;
    private Set<Integer> deleteIds;
    private Set<Integer> exportIds;

    public OSWorkflowBean(){
    }

    public String getWorkflowName(){
        return null;
    }

    public int getInitialState(){
        return 0;
    }

    public Set<Integer> getPublishIds(){
        return null;
    }

    public Set<Integer> getUnpublishIds(){
        return null;
    }

    public Set<Integer> getEditIds(){
        return null;
    }

    public Set<Integer> getDeleteIds(){
        return null;
    }

    public Set<Integer> getExportIds(){
        return null;
    }

    public void addPublishId(Integer id){
    }

    public void addUnpublishId(Integer id){
    }

    public void addEditId(Integer id){
    }

    public void addDeleteId(Integer id){
    }

    public void addExportId(Integer id){
    }

    public void addPublishId(Integer id){
    }

    public void setWorkflowName(String workflowName){
    }

    public void setInitialState(int initialState){
    }

    public void setPublishIds(Set<String> ids){
    }

    public void setUnpublishIds(Set<String> ids){
    }

    public void setEditIds(Set<String> ids){
    }

    public void setDeleteIds(Set<String> ids){
    }

    public void setExportIds(Set<String> ids){
    }

    public boolean isSpecialId(Integer id){
        return false;
    }

}
