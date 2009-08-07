package com.mashfrog.backoffice.actions;

import java.util.List;

import com.mashfrog.backoffice.project.beans.TabbedContentBean;

public interface BackofficeTabbedContentAction {

    public List<TabbedContentBean> getTabbedContents();

    public void setTabbedContents(List<TabbedContentBean> contents);

    public void addTabbedContent(TabbedContentBean content);

}
