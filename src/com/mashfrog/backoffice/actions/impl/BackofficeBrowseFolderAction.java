package com.mashfrog.backoffice.actions.impl;

import java.util.List;

import org.opencms.file.CmsObject;

import com.mashfrog.backoffice.CmsBackofficeActionElement;
import com.mashfrog.backoffice.project.beans.TabbedContentBean;

public class BackofficeBrowseFolderAction extends com.mashfrog.backoffice.actions.BackofficeNavMenuAction implements com.mashfrog.backoffice.actions.BackofficeTabbedContentAction {
    protected List<TabbedContentBean> contents;

    public void execute(CmsBackofficeActionElement backofficeActionElement){
    }

    public void setAdditonalConfigurationFilePath(String filePath, CmsObject cmsObject){
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
