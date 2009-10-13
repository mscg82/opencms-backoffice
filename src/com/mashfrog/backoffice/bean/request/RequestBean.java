package com.mashfrog.backoffice.bean.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class RequestBean implements Serializable{

	private static final long serialVersionUID = 651753014006485685L;

	private Map<String, List<String>> attributes;
    private String page;

    public RequestBean(){
    	attributes = new LinkedHashMap<String, List<String>>();
    }

    public RequestBean(HttpServletRequest request){
    	this();
    	page = request.getRequestURL().toString();
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
        List<String> values = attributes.get(name);
        return (values == null ? null : values.get(0));
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

	public void setAttribute(String name, String value) {
		List<String> values = new LinkedList<String>();
		values.add(value);
		setAttribute(name, values);
	}

	public void setAttribute(String name, List<String> value) {
		attributes.put(name, value);
	}

	public String toString(){
        return "Page: " + page + "; Attributes: " + attributes;
    }

}
