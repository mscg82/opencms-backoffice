package com.mashfrog.backoffice.project;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.file.CmsResourceFilter;
import org.opencms.jsp.util.CmsJspContentAccessBean;
import org.opencms.jsp.util.CmsJspContentAccessValueWrapper;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;

import com.mashfrog.backoffice.CmsBackofficeActionElement;
import com.mashfrog.backoffice.actions.I_BackofficeAction;
import com.mashfrog.backoffice.actions.constants.Constants;
import com.mashfrog.backoffice.project.beans.BackOfficeLanguageBean;
import com.mashfrog.backoffice.project.beans.CommandMenuBean;
import com.mashfrog.backoffice.project.beans.CommandMenuItemBean;
import com.mashfrog.backoffice.project.beans.CommandMenuSectionBean;
import com.mashfrog.backoffice.project.beans.ExportDestinationFolderBean;
import com.mashfrog.backoffice.project.beans.NavigationMenuBean;
import com.mashfrog.backoffice.project.beans.OSWorkflowBean;
import com.mashfrog.backoffice.project.beans.RenderingBean;
import com.mashfrog.backoffice.util.Util;

public class BackofficeProjectBean {
	private static Log LOG = CmsLog.getLog(BackofficeProjectBean.class);

    protected static Map<String, BackofficeProjectBean> projects = new LinkedHashMap<String, BackofficeProjectBean>();

    protected CmsJspContentAccessBean contentAccess;
    protected long dateRead;
    protected String cmsResourcePath;
    protected CmsBackofficeActionElement backofficeActionElement;
    protected Map<String, I_BackofficeAction> actions;
    protected String projectId;
    protected String description;
    protected String module;
	protected Set<BackOfficeLanguageBean> languages;
    protected RenderingBean rendering;
    protected String basePath;
    protected String orgUnit;
    protected String galleriesPath;
    protected CommandMenuBean commandMenu;
    protected List<ExportDestinationFolderBean> exportDestinationFolders;
    protected OSWorkflowBean osWorkflow;
    protected NavigationMenuBean navigationMenu;

    public static synchronized BackofficeProjectBean getInstace(CmsBackofficeActionElement backofficeActionElement) throws CmsException{
    	String projectFolder = backofficeActionElement.getRequestContext().getFolderUri();
    	if(!projectFolder.endsWith("/"))
    		projectFolder += "/";
    	projectFolder += Constants.BACKOFFFICE_PROJECT_FOLDER;
    	if(!projectFolder.endsWith("/"))
    		projectFolder += "/";

        BackofficeProjectBean project = projects.get(projectFolder);
        if(project == null || project.getDateRead() < project.getCmsResource().getDateLastModified()){
        	project = new BackofficeProjectBean(backofficeActionElement, projectFolder);
        	projects.put(projectFolder, project);
        }
        return project;
    }

    private BackofficeProjectBean(CmsBackofficeActionElement backofficeActionElement, String projectFolder) throws CmsException{

    	this.backofficeActionElement = backofficeActionElement;

    	int backofficeType = Util.getBackofficeType(backofficeActionElement);
    	CmsResourceFilter filter = CmsResourceFilter.ALL.addRequireFile().addRequireType(backofficeType);
    	List<CmsResource> resources = backofficeActionElement.getCmsObject().readResources(projectFolder, filter, false);
    	if(!resources.isEmpty()){
    		cmsResourcePath = resources.get(0).getRootPath();
    	}

    	CmsObject obj = backofficeActionElement.getCmsObject();

    	LOG.debug("Reading backoffice project from path \"" + cmsResourcePath + "\".");
    	CmsResource projectRes = obj.readResource(cmsResourcePath);

    	LOG.debug("Starting parsing settings...");
    	contentAccess = new CmsJspContentAccessBean(obj, projectRes);

    	projectId = ((CmsJspContentAccessValueWrapper) contentAccess.getValue().get("id")).getStringValue();
    	LOG.debug("Project id: " + projectId);

    	description = ((CmsJspContentAccessValueWrapper) contentAccess.getValue().get("name")).getStringValue();
    	LOG.debug("Project description: " + description);

    	List<CmsJspContentAccessValueWrapper> nodesList = (List<CmsJspContentAccessValueWrapper>) contentAccess.getValueList().get("languages");
    	this.languages = new LinkedHashSet<BackOfficeLanguageBean>();
    	if(nodesList != null){
	    	LOG.debug("Configuring " + nodesList.size() + " languages...");
			for(CmsJspContentAccessValueWrapper language : nodesList){
				String languageId = ((CmsJspContentAccessValueWrapper) language.getValue().get("languageId")).getStringValue();
				String languageName = ((CmsJspContentAccessValueWrapper) language.getValue().get("languageDesc")).getStringValue();
				BackOfficeLanguageBean langBean = new BackOfficeLanguageBean(languageId, languageName);
				this.languages.add(langBean);
			}
			LOG.debug("Configured languages: " + this.languages);
    	}

    	module = ((CmsJspContentAccessValueWrapper) contentAccess.getValue().get("schemaModule")).getStringValue();
    	LOG.debug("Associated module: " + module);

    	CmsJspContentAccessValueWrapper node = (CmsJspContentAccessValueWrapper) contentAccess.getValue().get("rendering");
    	rendering = new RenderingBean();
		if(node != null){
    		CmsJspContentAccessValueWrapper logoNode = (CmsJspContentAccessValueWrapper)node.getValue().get("logo");
    		if(logoNode != null)
    			rendering.setLogo(logoNode.getStringValue());
    		List<CmsJspContentAccessValueWrapper> cssList = (List<CmsJspContentAccessValueWrapper>) node.getValueList().get("css");
    		if(cssList != null){
    			for(CmsJspContentAccessValueWrapper css : cssList){
    				rendering.addCss(css.getStringValue());
    			}
    		}
    		List<CmsJspContentAccessValueWrapper> jsList = (List<CmsJspContentAccessValueWrapper>) node.getValueList().get("javascript");
    		if(jsList != null){
    			for(CmsJspContentAccessValueWrapper js : jsList){
    				rendering.addJavascript(js.getStringValue());
    			}
    		}
    	}

		basePath = ((CmsJspContentAccessValueWrapper) contentAccess.getValue().get("basePath")).getStringValue();
    	LOG.debug("Base path: " + basePath);

    	orgUnit = ((CmsJspContentAccessValueWrapper) contentAccess.getValue().get("orgUnit")).getStringValue();
    	LOG.debug("Organizational unit: " + orgUnit);

    	galleriesPath = ((CmsJspContentAccessValueWrapper) contentAccess.getValue().get("galleriesPath")).getStringValue();
    	LOG.debug("Galleries path: " + galleriesPath);

    	commandMenu = new CommandMenuBean();
    	node = (CmsJspContentAccessValueWrapper) contentAccess.getValue().get("rendering");
		if(node != null){
			List<CmsJspContentAccessValueWrapper> sections = (List<CmsJspContentAccessValueWrapper>) node.getValueList().get("section");
			if(sections != null){
				for(CmsJspContentAccessValueWrapper section : sections){
					String name = ((CmsJspContentAccessValueWrapper) section.getValue().get("name")).getStringValue();
					CommandMenuSectionBean sectionBean = new CommandMenuSectionBean(name);

					List<CmsJspContentAccessValueWrapper> groups = (List<CmsJspContentAccessValueWrapper>) section.getValueList().get("group");
					if(groups != null){
						for(CmsJspContentAccessValueWrapper group : groups){
							sectionBean.addGroup(group.getStringValue());
						}
					}

					List<CmsJspContentAccessValueWrapper> orgUnits = (List<CmsJspContentAccessValueWrapper>) section.getValueList().get("orgUnit");
					if(orgUnits != null){
						for(CmsJspContentAccessValueWrapper orgUnit : orgUnits){
							sectionBean.addOrgUnit(orgUnit.getStringValue());
						}
					}

					List<CmsJspContentAccessValueWrapper> items = (List<CmsJspContentAccessValueWrapper>) section.getValueList().get("item");
					if(items != null){
						for(CmsJspContentAccessValueWrapper item : items){
							String function = ((CmsJspContentAccessValueWrapper) item.getValue().get("url-function")).getStringValue();
							String description = ((CmsJspContentAccessValueWrapper) item.getValue().get("description")).getStringValue();
							String icon = item.getValue().get("icon_path") == null ? null : ((CmsJspContentAccessValueWrapper) item.getValue().get("icon_path")).getStringValue();

							CommandMenuItemBean itemBean = new CommandMenuItemBean(description, function, icon);

							groups = (List<CmsJspContentAccessValueWrapper>) section.getValueList().get("group");
							if(groups != null){
								for(CmsJspContentAccessValueWrapper group : groups){
									itemBean.addGroup(group.getStringValue());
								}
							}

							orgUnits = (List<CmsJspContentAccessValueWrapper>) section.getValueList().get("orgUnit");
							if(orgUnits != null){
								for(CmsJspContentAccessValueWrapper orgUnit : orgUnits){
									itemBean.addOrgUnit(orgUnit.getStringValue());
								}
							}
							sectionBean.addItem(itemBean);
						}
					}
					commandMenu.addCommandMenuSection(sectionBean);
				}
			}
			LOG.debug("Command menu:\n" + commandMenu.toString());
		}

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

    public Set<BackOfficeLanguageBean> getLanguages(){
        return languages;
    }

    public String getModule() {
		return module;
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
