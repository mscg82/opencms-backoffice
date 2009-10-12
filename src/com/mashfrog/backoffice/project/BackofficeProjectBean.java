package com.mashfrog.backoffice.project;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.file.CmsResourceFilter;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.jsp.util.CmsJspContentAccessBean;
import org.opencms.jsp.util.CmsJspContentAccessValueWrapper;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;

import com.mashfrog.backoffice.CmsBackofficeActionElement;
import com.mashfrog.backoffice.actions.constants.Constants;
import com.mashfrog.backoffice.project.beans.ActionBean;
import com.mashfrog.backoffice.project.beans.BackOfficeLanguageBean;
import com.mashfrog.backoffice.project.beans.CommandMenuBean;
import com.mashfrog.backoffice.project.beans.CommandMenuItemBean;
import com.mashfrog.backoffice.project.beans.CommandMenuSectionBean;
import com.mashfrog.backoffice.project.beans.ExportDestinationFolderBean;
import com.mashfrog.backoffice.project.beans.NavigationItemBean;
import com.mashfrog.backoffice.project.beans.NavigationMenuBean;
import com.mashfrog.backoffice.project.beans.OSWorkflowBean;
import com.mashfrog.backoffice.project.beans.RenderingBean;
import com.mashfrog.backoffice.util.Util;

public class BackofficeProjectBean implements Serializable{

	private static final long serialVersionUID = 1828293587121349278L;

	private static Log LOG = CmsLog.getLog(BackofficeProjectBean.class);

    protected static Map<String, BackofficeProjectBean> projects = new LinkedHashMap<String, BackofficeProjectBean>();

    public static synchronized BackofficeProjectBean getInstace(CmsBackofficeActionElement backofficeActionElement) throws CmsException{
    	String projectFolder = backofficeActionElement.getRequestContext().getFolderUri();
    	if(!projectFolder.endsWith("/"))
    		projectFolder += "/";
    	projectFolder += Constants.BACKOFFFICE_PROJECT_FOLDER;
    	if(!projectFolder.endsWith("/"))
    		projectFolder += "/";

        BackofficeProjectBean project = projects.get(projectFolder);
        if(project == null || project.getDateRead() < project.getCmsResource(backofficeActionElement.getCmsObject()).getDateLastModified()){
        	project = new BackofficeProjectBean(backofficeActionElement, projectFolder);
        	projects.put(projectFolder, project);
        }
        return project;
    }

    protected CmsBackofficeActionElement backofficeActionElement;
    protected long dateRead;
    protected String cmsResourcePath;
    protected Map<String, ActionBean> actions;
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

    private BackofficeProjectBean(CmsBackofficeActionElement backofficeActionElement, String projectFolder) throws CmsException{

    	this.backofficeActionElement = backofficeActionElement;

    	int backofficeType = Util.getBackofficeType(backofficeActionElement);
    	CmsResourceFilter filter = CmsResourceFilter.ALL.addRequireFile().addRequireType(backofficeType);
    	List<CmsResource> resources = backofficeActionElement.getCmsObject().readResources(projectFolder, filter, false);
    	if(!resources.isEmpty()){
    		cmsResourcePath = resources.get(0).getRootPath();
    	}

    	if(LOG.isDebugEnabled())
    		LOG.debug("Reading backoffice project from path \"" + cmsResourcePath + "\".");

    	if(LOG.isDebugEnabled())
    		LOG.debug("Starting parsing settings...");

    	CmsJspContentAccessBean contentAccess = getContentAccess();

    	projectId = ((CmsJspContentAccessValueWrapper) contentAccess.getValue().get("id")).getStringValue();
    	if(LOG.isDebugEnabled())
    		LOG.debug("Project id: " + projectId);

    	description = ((CmsJspContentAccessValueWrapper) contentAccess.getValue().get("name")).getStringValue();
    	if(LOG.isDebugEnabled())
    		LOG.debug("Project description: " + description);

    	this.languages = new LinkedHashSet<BackOfficeLanguageBean>();
    	if((Boolean)contentAccess.getHasValue().get("languages")){
    		List<CmsJspContentAccessValueWrapper> nodesList = (List<CmsJspContentAccessValueWrapper>) contentAccess.getValueList().get("languages");
        	if(LOG.isDebugEnabled())
        		LOG.debug("Configuring " + nodesList.size() + " languages...");
			for(CmsJspContentAccessValueWrapper language : nodesList){
				String languageId = ((CmsJspContentAccessValueWrapper) language.getValue().get("languageId")).getStringValue();
				String languageName = ((CmsJspContentAccessValueWrapper) language.getValue().get("languageDesc")).getStringValue();
				BackOfficeLanguageBean langBean = new BackOfficeLanguageBean(languageId, languageName);
				this.languages.add(langBean);
			}
			if(LOG.isDebugEnabled())
	    		LOG.debug("Configured languages: " + this.languages);
    	}

    	module = ((CmsJspContentAccessValueWrapper) contentAccess.getValue().get("schemaModule")).getStringValue();
    	if(LOG.isDebugEnabled())
    		LOG.debug("Associated module: " + module);

    	rendering = new RenderingBean();
		if((Boolean)contentAccess.getHasValue().get("rendering")){
			CmsJspContentAccessValueWrapper node = (CmsJspContentAccessValueWrapper) contentAccess.getValue().get("rendering");
	    	if((Boolean)node.getHasValue().get("logo")){
	    		CmsJspContentAccessValueWrapper logoNode = (CmsJspContentAccessValueWrapper)node.getValue().get("logo");
	    		rendering.setLogo(logoNode.getStringValue());
    		}
    		if((Boolean)node.getHasValue().get("css")){
    			List<CmsJspContentAccessValueWrapper> cssList = (List<CmsJspContentAccessValueWrapper>) node.getValueList().get("css");
        		for(CmsJspContentAccessValueWrapper css : cssList){
    				rendering.addCss(css.getStringValue());
    			}
    		}
    		if((Boolean)node.getHasValue().get("javascript")){
    			List<CmsJspContentAccessValueWrapper> jsList = (List<CmsJspContentAccessValueWrapper>) node.getValueList().get("javascript");
        		for(CmsJspContentAccessValueWrapper js : jsList){
    				rendering.addJavascript(js.getStringValue());
    			}
    		}
    	}

		basePath = ((CmsJspContentAccessValueWrapper) contentAccess.getValue().get("basePath")).getStringValue();
		if(LOG.isDebugEnabled())
    		LOG.debug("Base path: " + basePath);

		if((Boolean)contentAccess.getHasValue().get("orgUnit")){
			CmsJspContentAccessValueWrapper node = (CmsJspContentAccessValueWrapper) contentAccess.getValue().get("orgUnit");
			orgUnit = node.getStringValue();
	    	if(LOG.isDebugEnabled())
	    		LOG.debug("Organizational unit: " + orgUnit);
		}
		else{
			orgUnit = null;
			if(LOG.isDebugEnabled())
	    		LOG.debug("Project will not be bound to organization unit.");
		}

    	galleriesPath = ((CmsJspContentAccessValueWrapper) contentAccess.getValue().get("galleriesPath")).getStringValue();
    	if(LOG.isDebugEnabled())
    		LOG.debug("Galleries path: " + galleriesPath);

    	actions = new LinkedHashMap<String, ActionBean>();
    	loadDefaultActions(backofficeActionElement);
    	if((Boolean)contentAccess.getHasValue().get("action")){
    		List<CmsJspContentAccessValueWrapper> nodesList = (List<CmsJspContentAccessValueWrapper>) contentAccess.getValueList().get("action");
    		for(CmsJspContentAccessValueWrapper action : nodesList){
				String name = ((CmsJspContentAccessValueWrapper) action.getValue().get("name")).getStringValue();
				String className = ((CmsJspContentAccessValueWrapper) action.getValue().get("className")).getStringValue();
				String jspPath = ((CmsJspContentAccessValueWrapper) action.getValue().get("jspPath")).getStringValue();
				String addConf = ((Boolean)action.getHasValue().get("additionalConfigurationFile")) ?
						((CmsJspContentAccessValueWrapper) action.getValue().get("additionalConfigurationFile")).getStringValue() : null;
				ActionBean actionBean = new ActionBean(className, jspPath, addConf);

				if((Boolean)action.getHasValue().get("group")){
					List<CmsJspContentAccessValueWrapper> groups = (List<CmsJspContentAccessValueWrapper>) action.getValueList().get("group");
					for(CmsJspContentAccessValueWrapper group : groups){
						actionBean.addGroup(group.getStringValue());
					}
				}

				if((Boolean)action.getHasValue().get("orgUnit")){
					List<CmsJspContentAccessValueWrapper> orgUnits = (List<CmsJspContentAccessValueWrapper>) action.getValueList().get("orgUnit");
					for(CmsJspContentAccessValueWrapper orgUnit : orgUnits){
						actionBean.addOrgUnit(orgUnit.getStringValue());
					}
				}

				actions.put(name, actionBean);
			}
		}
		if(LOG.isDebugEnabled())
    		LOG.debug("Configured actions: " + actions);

    	commandMenu = new CommandMenuBean();
    	if((Boolean)contentAccess.getHasValue().get("commandMenu")){
    		CmsJspContentAccessValueWrapper node = (CmsJspContentAccessValueWrapper) contentAccess.getValue().get("commandMenu");
    		if((Boolean)node.getHasValue().get("section")){
    			List<CmsJspContentAccessValueWrapper> sections = (List<CmsJspContentAccessValueWrapper>) node.getValueList().get("section");
    			for(CmsJspContentAccessValueWrapper section : sections){
					String name = ((CmsJspContentAccessValueWrapper) section.getValue().get("name")).getStringValue();
					CommandMenuSectionBean sectionBean = new CommandMenuSectionBean(name);

					if((Boolean)section.getHasValue().get("group")){
						List<CmsJspContentAccessValueWrapper> groups = (List<CmsJspContentAccessValueWrapper>) section.getValueList().get("group");
						for(CmsJspContentAccessValueWrapper group : groups){
							sectionBean.addGroup(group.getStringValue());
						}
					}

					if((Boolean)section.getHasValue().get("orgUnit")){
						List<CmsJspContentAccessValueWrapper> orgUnits = (List<CmsJspContentAccessValueWrapper>) section.getValueList().get("orgUnit");
						for(CmsJspContentAccessValueWrapper orgUnit : orgUnits){
							sectionBean.addOrgUnit(orgUnit.getStringValue());
						}
					}

					if((Boolean)section.getHasValue().get("item")){
						List<CmsJspContentAccessValueWrapper> items = (List<CmsJspContentAccessValueWrapper>) section.getValueList().get("item");
						for(CmsJspContentAccessValueWrapper item : items){
							String function = ((CmsJspContentAccessValueWrapper) item.getValue().get("url-function")).getStringValue();
							String description = ((CmsJspContentAccessValueWrapper) item.getValue().get("description")).getStringValue();
							String icon = item.getValue().get("icon_path") == null ? null : ((CmsJspContentAccessValueWrapper) item.getValue().get("icon_path")).getStringValue();

							CommandMenuItemBean itemBean = new CommandMenuItemBean(description, function, icon);

							if((Boolean)item.getHasValue().get("group")){
								List<CmsJspContentAccessValueWrapper> groups = (List<CmsJspContentAccessValueWrapper>) item.getValueList().get("group");
								for(CmsJspContentAccessValueWrapper group : groups){
									itemBean.addGroup(group.getStringValue());
								}
							}

							if((Boolean)item.getHasValue().get("orgUnit")){
								List<CmsJspContentAccessValueWrapper> orgUnits = (List<CmsJspContentAccessValueWrapper>) item.getValueList().get("orgUnit");
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
    	}
		if(LOG.isDebugEnabled())
    		LOG.debug("Command menu:\n" + commandMenu.toString());

		navigationMenu = new NavigationMenuBean();
		if((Boolean)contentAccess.getHasValue().get("navigationMenu")){
			CmsJspContentAccessValueWrapper node = (CmsJspContentAccessValueWrapper) contentAccess.getValue().get("navigationMenu");
			Boolean fixedSize = new Boolean(((CmsJspContentAccessValueWrapper) node.getValue().get("fixedSize")).getStringValue());
			navigationMenu.setFixedSize(fixedSize);

			Integer maxLevel = 3;
			try{
				maxLevel = Integer.parseInt(((CmsJspContentAccessValueWrapper) node.getValue().get("maxLevel")).getStringValue());
			} catch(NumberFormatException e){}
			navigationMenu.setMaxLevel(maxLevel);

			if((Boolean)node.getHasValue().get("navCombos")){
				List<CmsJspContentAccessValueWrapper> combos = (List<CmsJspContentAccessValueWrapper>) node.getValueList().get("navCombos");
				for(CmsJspContentAccessValueWrapper combo : combos){
					String label = ((CmsJspContentAccessValueWrapper) combo.getValue().get("label")).getStringValue();
					String newLabel = ((CmsJspContentAccessValueWrapper) combo.getValue().get("newComboLabel")).getStringValue();
					String listHeader = ((CmsJspContentAccessValueWrapper) combo.getValue().get("listHeader")).getStringValue();
					NavigationItemBean navItem = new NavigationItemBean();
					navItem.setLabel(label);
					navItem.setNewElementLabel(newLabel);
					navItem.setListHeader(listHeader);
					navigationMenu.addItem(navItem);
				}
			}
		}
		if(LOG.isDebugEnabled())
    		LOG.debug("Navigation menu:\n" + navigationMenu);

		exportDestinationFolders = new LinkedList<ExportDestinationFolderBean>();
		if((Boolean)contentAccess.getHasValue().get("exportSettings")){
			List<CmsJspContentAccessValueWrapper> nodesList = (List<CmsJspContentAccessValueWrapper>) contentAccess.getValueList().get("exportSettings");
			for(CmsJspContentAccessValueWrapper expFolder : nodesList){
				String description = ((CmsJspContentAccessValueWrapper) expFolder.getValue().get("exportDescription")).getStringValue();
				List<CmsJspContentAccessValueWrapper> destinations = (List<CmsJspContentAccessValueWrapper>) expFolder.getValueList().get("exportDestFolder");
				ExportDestinationFolderBean expFolderBean  = new  ExportDestinationFolderBean(description);
				for(CmsJspContentAccessValueWrapper destination : destinations){
					expFolderBean.addFolder(destination.getStringValue());
				}
				exportDestinationFolders.add(expFolderBean);
			}
		}
		if(LOG.isDebugEnabled())
    		LOG.debug("Export destionations: " + exportDestinationFolders);

		osWorkflow = new OSWorkflowBean();
		if((Boolean)contentAccess.getHasValue().get("OSWorkflow")){
			CmsJspContentAccessValueWrapper node = (CmsJspContentAccessValueWrapper) contentAccess.getValue().get("OSWorkflow");
			String workflowName = ((CmsJspContentAccessValueWrapper) node.getValue().get("workflowName")).getStringValue();
			osWorkflow.setWorkflowName(workflowName);

			Integer initialState = 100;
			try{
				initialState = Integer.parseInt(((CmsJspContentAccessValueWrapper) node.getValue().get("initialState")).getStringValue());
			} catch(NumberFormatException e){}
			osWorkflow.setInitialState(initialState);

			if((Boolean)node.getHasValue().get("actionId")){
				CmsJspContentAccessValueWrapper actionIdNode = (CmsJspContentAccessValueWrapper) node.getValue().get("actionId");
				List<CmsJspContentAccessValueWrapper> ids = (List<CmsJspContentAccessValueWrapper>) actionIdNode.getValueList().get("publishId");
				for(CmsJspContentAccessValueWrapper id : ids){
					try{
						Integer intId = Integer.parseInt(id.getStringValue());
						osWorkflow.addPublishId(intId);
					} catch(NumberFormatException e){
						if(LOG.isInfoEnabled())
				    		LOG.info("Cannot parse integer value for action id.", e);
					}
				}

				ids = (List<CmsJspContentAccessValueWrapper>) actionIdNode.getValueList().get("unpublishId");
				for(CmsJspContentAccessValueWrapper id : ids){
					try{
						Integer intId = Integer.parseInt(id.getStringValue());
						osWorkflow.addUnpublishId(intId);
					} catch(NumberFormatException e){
						if(LOG.isInfoEnabled())
				    		LOG.info("Cannot parse integer value for action id.", e);
					}
				}

				ids = (List<CmsJspContentAccessValueWrapper>) actionIdNode.getValueList().get("editId");
				for(CmsJspContentAccessValueWrapper id : ids){
					try{
						Integer intId = Integer.parseInt(id.getStringValue());
						osWorkflow.addEditId(intId);
					} catch(NumberFormatException e){
						if(LOG.isInfoEnabled())
				    		LOG.info("Cannot parse integer value for action id.", e);
					}
				}

				ids = (List<CmsJspContentAccessValueWrapper>) actionIdNode.getValueList().get("deleteId");
				for(CmsJspContentAccessValueWrapper id : ids){
					try{
						Integer intId = Integer.parseInt(id.getStringValue());
						osWorkflow.addDeleteId(intId);
					} catch(NumberFormatException e){
						if(LOG.isInfoEnabled())
				    		LOG.info("Cannot parse integer value for action id.", e);
					}
				}

				ids = (List<CmsJspContentAccessValueWrapper>) actionIdNode.getValueList().get("exportId");
				for(CmsJspContentAccessValueWrapper id : ids){
					try{
						Integer intId = Integer.parseInt(id.getStringValue());
						osWorkflow.addExportId(intId);
					} catch(NumberFormatException e){
						if(LOG.isInfoEnabled())
				    		LOG.info("Cannot parse integer value for action id.", e);
					}
				}
			}
		}
		if(LOG.isDebugEnabled())
    		LOG.debug("OSWorkflow bean:\n" + osWorkflow);

		dateRead = System.currentTimeMillis();

		if(LOG.isDebugEnabled())
    		LOG.debug("Parse finished.");

    }

    public ActionBean getAction(String actionName){
    	if(actionName == null)
    		return null;
        return actions.get(actionName);
    }

	public Map<String, ActionBean> getActions(){
        return actions;
    }

    public String getBasePath(){
        return basePath;
    }

    public CmsResource getCmsResource() throws CmsException{
    	return getCmsResource(backofficeActionElement.getCmsObject());
    }

    public CmsResource getCmsResource(CmsObject cmsObject) throws CmsException{
        CmsResource ret = null;
        try{
        	ret = cmsObject.readResource(cmsObject.getRequestContext().removeSiteRoot(cmsResourcePath));
        } catch(CmsException e){
        	ret = cmsObject.readResource(cmsResourcePath);
        }
        return ret;
    }

    public CommandMenuBean getCommandMenu(){
        return commandMenu;
    }

    public CmsJspContentAccessBean getContentAccess(){
    	CmsJspContentAccessBean ret = null;
    	CmsObject obj = backofficeActionElement.getCmsObject();
    	try {
			CmsResource res = getCmsResource(obj);
			ret = new CmsJspContentAccessBean(obj, res);
		} catch (CmsException e) {
			LOG.warn("Cannot obtain content access bean.", e);
		}
    	return ret;
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

    private void loadDefaultActions(CmsJspActionElement cmsAction) {
    	ActionBean loginAction = new ActionBean(Constants.LOGIN_DEFAULT_ACTION_CLASS,
    			Constants.LOGIN_DEFAULT_JSP_PATTERN.replace("${module.name}", Util.getModuleForRequest(cmsAction).getName()) , null);
    	actions.put(Constants.LOGIN_DEFAULT_NAME, loginAction);

    	ActionBean logoutAction = new ActionBean(Constants.LOGOUT_DEFAULT_ACTION_CLASS,
    			Constants.LOGOUT_DEFAULT_JSP_PATTERN.replace("${module.name}", Util.getModuleForRequest(cmsAction).getName()) , null);
    	actions.put(Constants.LOGOUT_DEFAULT_NAME, logoutAction);

    	ActionBean defaultAction = new ActionBean(Constants.NOACTION_DEFAULT_ACTION_CLASS,
    			Constants.NOACTION_DEFAULT_JSP_PATTERN.replace("${module.name}", Util.getModuleForRequest(cmsAction).getName()) , null);
    	actions.put(Constants.NOACTION_DEFAULT_NAME, defaultAction);
	}

}
