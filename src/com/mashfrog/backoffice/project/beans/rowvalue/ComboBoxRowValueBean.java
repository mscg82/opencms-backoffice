package com.mashfrog.backoffice.project.beans.rowvalue;

import java.util.LinkedList;
import java.util.List;

public class ComboBoxRowValueBean implements RowValueBean {

	private static final long serialVersionUID = -572544735581692900L;

	protected String label;
    protected List<ComboBoxValueBean> values;
    protected String type;
    protected boolean enabled;

    public ComboBoxRowValueBean(){
    	values = new LinkedList<ComboBoxValueBean>();
    }

    public void addValue(String text, String value){
    	addValue(new ComboBoxValueBean(text, value));
    }

    public void addValue(ComboBoxValueBean value){
    	values.add(value);
    }

    public List<ComboBoxValueBean> getValues(){
        return values;
    }

    public String getType(){
        return type;
    }

    public boolean getEnabled(){
        return enabled;
    }

    public void setValue(List<ComboBoxValueBean> values){
    	this.values = values;
    }

    public void setType(String type){
    	this.type = type;
    }

    public void setEnabled(boolean enabled){
    	this.enabled = enabled;
    }

}
