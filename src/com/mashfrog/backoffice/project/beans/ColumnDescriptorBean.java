package com.mashfrog.backoffice.project.beans;

public class ColumnDescriptorBean {
    private String name;
    private String type;

    public ColumnDescriptorBean(){
    	this(null, null);
    }

    public ColumnDescriptorBean(String name, String type){
    	setName(name);
    	setType(type);
    }

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }

    public void setName(String name){
    	this.name = name;
    }

    public void setType(String type){
    	this.type = type;
    }

}
