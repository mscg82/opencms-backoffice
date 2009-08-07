package com.mashfrog.backoffice.project.beans.rowvalue;

public class ComboBoxRowValueBean implements com.mashfrog.backoffice.project.beans.rowvalue.RowValueBean {
    protected String label;
    protected List<ComboBoxValueBean> values;
    protected String type;
    protected boolean enabled;

    public ComboBoxRowValueBean(){
    }

    public void addValue(String text, String value){
    }

    public void addValue(ComboBoxValueBean value){
    }

    public List<ComboBoxValueBean> getValues(){
        return null;
    }

    public String getType(){
        return null;
    }

    public boolean getEnabled(){
        return false;
    }

    public void setValue(List<ComboBoxValueBean> values){
    }

    public void setType(String type){
    }

    public void setEnabled(boolean enabled){
    }

}
