package com.mashfrog.backoffice.actions.impl;

import java.util.List;

import com.mashfrog.backoffice.project.beans.TabbedContentBean;

public abstract class BackofficeWFBrowseResourceAction extends com.mashfrog.backoffice.actions.impl.BackofficeBrowseResourceAction {

    public List<TabbedContentBean> getTabbedContents(){
        return null;
    }

    public abstract List<TabbedContentBean> filterContentsByWorkflow(List<TabbedContentBean> origList);

}
