package com.mashfrog.backoffice.project.beans;

import java.util.Map;

import org.opencms.file.CmsResource;

import com.mashfrog.backoffice.project.beans.rowvalue.RowValueBean;

public class TabbedContentRowBean {
    private Map<String, RowValueBean> values;
    private CmsResource resource;

    public TabbedContentRowBean(){
    	this(null);
    }

    public TabbedContentRowBean(CmsResource resource){
    	setResource(resource);
    }

    public void addValue(String colType, RowValueBean value){
    	values.put(colType, value);
    }

    public Map<String, RowValueBean> getValues(){
        return values;
    }

    public CmsResource getResource(){
        return resource;
    }

    public void setResource(CmsResource resource){
    	this.resource = resource;
    }

}
