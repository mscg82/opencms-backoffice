package com.mashfrog.backoffice.bean.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class RequestBean {
    private Map<String, List<String>> attributes;
    private String page;

    public RequestBean(){
    	attributes = new LinkedHashMap<String, List<String>>();
    }

    public RequestBean(HttpServletRequest request){
    	this();
    	setPage(request.getRequestURL().toString());
    	Enumeration<String> names = request.getParameterNames();
    	while(names.hasMoreElements()){
    		String name = names.nextElement();
    		addAttributes(name, request.getParameterValues(name));
    	}
    }

    public void addAttribute(String name, String value){
    	List<String> tmp = new ArrayList<String>(1);
    	tmp.add(value);
    	addAttributes(name, tmp);
    }

    public void addAttributes(String name, List<String> values){
    	if(!attributes.containsKey(name)){
    		attributes.put(name, new ArrayList<String>());
    	}
    	attributes.get(name).addAll(values);
    }

    public void addAttributes(String name, String[] values){
    	addAttributes(name, Arrays.asList(values));
    }

    public String getAttribute(String name){
        return attributes.get(name).get(0);
    }

    public Map<String,List<String>> getAttributes(){
        return attributes;
    }

    public List<String> getAttributeValues(String name){
        return attributes.get(name);
    }

    public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String toString(){
        return "Page: " + page + "; Attributes: " + attributes;
    }

}
