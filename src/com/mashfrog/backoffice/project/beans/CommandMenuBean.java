package com.mashfrog.backoffice.project.beans;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencms.file.CmsUser;
import org.opencms.jsp.CmsJspActionElement;

import com.mashfrog.backoffice.util.Util;

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

    	CmsUser user = cmsAction.getRequestContext().currentUser();

    	for(CommandMenuSectionBean menuSection : sectionList){
    		if(Util.canUserAccessBean(user, cmsAction.getCmsObject(), menuSection)){
    			CommandMenuSectionBean sectionBean = new CommandMenuSectionBean();
    			sectionBean.setLabel(menuSection.getLabel());
    			sectionBean.setGroups(menuSection.getGroups());
    			sectionBean.setOrgUnits(menuSection.getOrgUnits());
    			for(CommandMenuItemBean menuItem : menuSection.getItems()){
    				if(Util.canUserAccessBean(user, cmsAction.getCmsObject(), menuItem)){
    					sectionBean.addItem(menuItem);
    				}
    			}
    			ret.add(sectionBean);
    		}
    	}

    	return ret;
    }

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("Sections in menu:\n");
		for(CommandMenuSectionBean section : sectionList){
			ret.append(section.toString() + "\n");
		}
		return ret.toString();
	}

}
