package com.mashfrog.backoffice.project.beans;

import java.util.LinkedList;
import java.util.List;

public class RenderingBean {
	private String logo;
    private List<String> cssList;
	private List<String> javascriptList;

	public RenderingBean(){
    	cssList = new LinkedList<String>();
    	javascriptList = new LinkedList<String>();
    }
    public void addCss(String css){
    	cssList.add(css);
    }

    public void addJavascript(String javascript){
    	javascriptList.add(javascript);
    }

    public List<String> getCssList(){
        return cssList;
    }

    public List<String> getJavascriptList(){
        return javascriptList;
    }

    public String getLogo() {
		return logo;
	}

    public void setCssList(List<String> cssList){
    	this.cssList = cssList;
    }

    public void setJavascriptList(List<String> javascriptList){
    	this.javascriptList = javascriptList;
    }

    public void setLogo(String logo) {
		this.logo = logo;
	}

	@Override
	public String toString() {
		return "Logo: " + getLogo() + "; css: " + getCssList() + "; js: " + getJavascriptList();
	}

}
