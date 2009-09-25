package com.mashfrog.backoffice.project.beans.rowvalue;

public class CheckboxRowValueBean extends TitledRowValueBean {
    protected boolean checked;
    protected String value;

    public CheckboxRowValueBean(){
    	this(false, null);
    }

    public CheckboxRowValueBean(boolean checked, String value){
    	super();
    	setChecked(checked);
    	setValue(value);
    }

    public boolean getChecked(){
        return checked;
    }

    public String getValue(){
        return value;
    }

    public void setChecked(boolean checked){
    	this.checked = checked;
    }

    public void setValue(String value){
    	this.value = value;
    }

}
