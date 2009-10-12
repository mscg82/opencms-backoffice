package com.mashfrog.backoffice.project.beans;

import java.io.Serializable;

public class ColumnDescriptorBean implements Serializable{

	private static final long serialVersionUID = 3999267818037969254L;

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
