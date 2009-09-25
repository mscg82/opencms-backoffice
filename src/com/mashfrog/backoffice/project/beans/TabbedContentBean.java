package com.mashfrog.backoffice.project.beans;

import java.util.LinkedList;
import java.util.List;

public class TabbedContentBean {
    private List<ColumnDescriptorBean> colDescriptor;
    private List<TabbedContentRowBean> rows;

    public TabbedContentBean(){
    	colDescriptor = new LinkedList<ColumnDescriptorBean>();
    	rows = new LinkedList<TabbedContentRowBean>();
    }

    public void addColumnDescriptor(ColumnDescriptorBean colDescr){
    	colDescriptor.add(colDescr);
    }

    public List<ColumnDescriptorBean> getColumnDescriptors(){
        return colDescriptor;
    }

    public void addRow(TabbedContentRowBean row){
    	rows.add(row);
    }

    public List<TabbedContentRowBean> getRows(){
        return rows;
    }

}
