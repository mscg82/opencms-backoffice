package com.mashfrog.backoffice.actions.impl;

public abstract class BackofficeWFBrowseResourceAction extends com.mashfrog.backoffice.actions.impl.BackofficeBrowseResourceAction {

    public List<TabbedContentBean> getTabbedContents(){
        return null;
    }

    public abstract List<TabbedContentBean> filterContentsByWorkflow(List<TabbedContentBean> origList);

}
