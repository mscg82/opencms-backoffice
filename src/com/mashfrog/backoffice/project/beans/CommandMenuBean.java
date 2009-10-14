package com.mashfrog.backoffice.project.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencms.file.CmsUser;
import org.opencms.jsp.CmsJspActionElement;

import com.mashfrog.backoffice.CmsBackofficeActionElement;
import com.mashfrog.backoffice.actions.constants.Constants;
import com.mashfrog.backoffice.util.Util;

public class CommandMenuBean implements Serializable{

	private static final long serialVersionUID = -2103479202911334584L;

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

    	String actualAction = null;
    	if(cmsAction instanceof CmsBackofficeActionElement){
    		actualAction = ((CmsBackofficeActionElement)cmsAction).getActualRequest().getAttribute(Constants.ACTION_PARAM);
    	}

    	CmsUser user = cmsAction.getRequestContext().currentUser();

    	for(CommandMenuSectionBean menuSection : sectionList){
    		if(Util.canUserAccessBean(user, cmsAction.getCmsObject(), menuSection)){
    			CommandMenuSectionBean sectionBean = new CommandMenuSectionBean(menuSection);
    			for(CommandMenuItemBean menuItem : menuSection.getItems()){
    				if(Util.canUserAccessBean(user, cmsAction.getCmsObject(), menuItem)){
    					CommandMenuItemBean itemBean = new CommandMenuItemBean(menuItem);
    					sectionBean.addItem(itemBean);
    					if(Util.equals(actualAction, itemBean.getFunction())){
    						itemBean.setActual(true);
    						itemBean.getMenuSection().setActual(true);
    					}
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
