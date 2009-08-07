package com.mashfrog.backoffice.project.beans;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencms.jsp.CmsJspActionElement;

public class CommandMenuBean {
    private List<CommandMenuSectionBean> sectionList;

    public CommandMenuBean(){
    	sectionList = new ArrayList<CommandMenuSectionBean>();
    }

    public void addCommandMenuSection(CommandMenuSectionBean commandMenuSection){
    	sectionList.add(commandMenuSection);
    }

    public List<CommandMenuSectionBean> getSectionList(){
        return sectionList;
    }

    public void setSectionList(List<CommandMenuSectionBean> sectionList){
    	this.sectionList = sectionList;
    }

    public List<CommandMenuSectionBean> filterItemsForUser(CmsJspActionElement cmsAction){
    	List<CommandMenuSectionBean> ret = new LinkedList<CommandMenuSectionBean>();

    	// TODO implement

    	return ret;
    }

}
