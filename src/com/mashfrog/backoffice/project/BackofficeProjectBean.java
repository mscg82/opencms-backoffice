package com.mashfrog.backoffice.project;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opencms.file.CmsResource;
import org.opencms.file.CmsResourceFilter;
import org.opencms.jsp.util.CmsJspContentAccessBean;
import org.opencms.main.CmsException;

import com.mashfrog.backoffice.CmsBackofficeActionElement;
import com.mashfrog.backoffice.actions.I_BackofficeAction;
import com.mashfrog.backoffice.actions.constants.Constants;
import com.mashfrog.backoffice.project.beans.CommandMenuBean;
import com.mashfrog.backoffice.project.beans.ExportDestinationFolderBean;
import com.mashfrog.backoffice.project.beans.LanguageBean;
import com.mashfrog.backoffice.project.beans.NavigationMenuBean;
import com.mashfrog.backoffice.project.beans.OSWorkflowBean;
import com.mashfrog.backoffice.project.beans.RenderingBean;
import com.mashfrog.backoffice.util.Util;

public class BackofficeProjectBean {
    protected static Map<String, BackofficeProjectBean> projects;

    protected CmsJspContentAccessBean contentAccess;
    protected long dateRead;
    protected String cmsResourcePath;
    protected CmsBackofficeActionElement backofficeActionElement;
    protected Map<String, I_BackofficeAction> actions;
    protected String projectId;
    protected String description;
    protected Set<LanguageBean> languages;
    protected RenderingBean rendering;
    protected String basePath;
    protected String orgUnit;
    protected String galleriesPath;
    protected CommandMenuBean commandMenu;
    protected List<ExportDestinationFolderBean> exportDestinationFolders;
    protected OSWorkflowBean osWorkflow;
    protected NavigationMenuBean navigationMenu;

    public static synchronized BackofficeProjectBean getInstace(CmsBackofficeActionElement backofficeActionElement) throws CmsException{
    	String key = null;

    	String projectFolder = backofficeActionElement.getRequestContext().getFolderUri();
    	if(!projectFolder.endsWith("/"))
    		projectFolder += "/";
    	projectFolder += Constants.BACKOFFFICE_PROJECT_FOLDER;
    	if(!projectFolder.endsWith("/"))
    		projectFolder += "/";

    	int backofficeType = Util.getBackofficeType(backofficeActionElement);
    	CmsResourceFilter filter = CmsResourceFilter.ALL.addRequireFile().addRequireType(backofficeType);
    	List<CmsResource> resources = backofficeActionElement.getCmsObject().readResources(projectFolder, filter, false);
    	if(!resources.isEmpty()){
    		key = resources.get(0).getRootPath();
    	}

        BackofficeProjectBean project = projects.get(key);
        if(project == null || project.getDateRead() < project.getCmsResource().getDateLastModified()){
        	project = new BackofficeProjectBean(backofficeActionElement);
        	projects.put(key, project);
        }
        return project;
    }

    private BackofficeProjectBean(CmsBackofficeActionElement backofficeActionElement){

    	dateRead = System.currentTimeMillis();
    }

    public I_BackofficeAction getAction(String actionName){
        return actions.get(actionName);
    }

    public Map<String, I_BackofficeAction> getActions(){
        return actions;
    }

    public CmsBackofficeActionElement getBackofficeActionElement(){
        return backofficeActionElement;
    }

    public String getBasePath(){
        return basePath;
    }

    public CmsResource getCmsResource() throws CmsException{
        return getBackofficeActionElement().getCmsObject().readResource(cmsResourcePath);
    }

    public CommandMenuBean getCommandMenu(){
        return commandMenu;
    }

    public CmsJspContentAccessBean getContentAccess(){
    	return contentAccess;
    }

    public long getDateRead(){
        return dateRead;
    }

    public String getDescription(){
        return description;
    }

    public List<ExportDestinationFolderBean> getExportDestinationFolders(){
        return exportDestinationFolders;
    }

    public String getGalleriesPath(){
        return galleriesPath;
    }

    public Set<LanguageBean> getLanguages(){
        return languages;
    }

    public NavigationMenuBean getNavigationMenu(){
        return navigationMenu;
    }

    public String getOrgUnit(){
        return orgUnit;
    }

    public OSWorkflowBean getOSWorkflow(){
        return osWorkflow;
    }

    public String getProjectId(){
        return projectId;
    }

    public RenderingBean getRendering(){
        return rendering;
    }

}
