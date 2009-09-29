package com.mashfrog.backoffice.actions.impl;

import java.util.List;

import com.mashfrog.backoffice.actions.BackofficeNavMenuAction;
import com.mashfrog.backoffice.actions.BackofficeTabbedContentAction;
import com.mashfrog.backoffice.project.beans.TabbedContentBean;

public class BackofficeBrowseFolderAction extends BackofficeNavMenuAction implements BackofficeTabbedContentAction {
    protected List<TabbedContentBean> contents;

    public void execute(){
    }

    public List<TabbedContentBean> getTabbedContents(){
        return contents;
    }

    public void setTabbedContents(List<TabbedContentBean> contents){
    	this.contents = contents;
    }

    public void addTabbedContent(TabbedContentBean content){
    	contents.add(content);
    }

}
