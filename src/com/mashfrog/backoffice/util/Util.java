package com.mashfrog.backoffice.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsGroup;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsUser;
import org.opencms.file.types.A_CmsResourceType;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.module.CmsModule;

import com.mashfrog.backoffice.actions.constants.Constants;
import com.mashfrog.backoffice.project.beans.GroupOUAssociableBean;

public class Util {
	// Logger
	private static Log LOG = CmsLog.getLog(Util.class);

	/**
	 * Finds the module in which the requested resource, wrapped in the
	 * provided {@link CmsJspActionElement}, is contained.
	 *
	 * @param cmsAction The {@link CmsJspActionElement} that wraps the request.
	 * @return The {@link CmsModule} in which the requested resource is contained,
	 * or <code>null</code> if no suitable module is found.
	 */
	public static CmsModule getModuleForRequest(CmsJspActionElement cmsAction){
		CmsModule ret = null;
		String folders[] = cmsAction.getRequest().getRequestURL().toString().split("/");
		for(int i = folders.length - 1; i >= 0; i--){
			CmsModule module = OpenCms.getModuleManager().getModule(folders[i]);
			if(module != null){
				ret = module;
				break;
			}
		}
		return ret;
	}

	/**
	 * Finds the integer id for the resource type of backoffice projects.
	 *
	 * @param cmsAction The {@link CmsJspActionElement} that wraps the request.
	 * @return The integer id for the resource type of backoffice projects.
	 */
	public static int getBackofficeType(CmsJspActionElement cmsAction){
		int ret = -1;
		CmsModule module = getModuleForRequest(cmsAction);
		List<A_CmsResourceType> resourceTypes = module.getResourceTypes();

		for(A_CmsResourceType type : resourceTypes){
			if(type.getTypeName().toLowerCase().startsWith(
			   Constants.BACKOFFICE_RESOURCE_NAME.toLowerCase())){

				ret = type.getTypeId();
			}
		}

		return ret;
	}

	/**
	 * Checks if the provided {@link CmsUser} can access the specified
	 * {@link GroupOUAssociableBean}.
	 *
	 * @param user The user that will be tested.
	 * @param cmsObject The {@link CmsObject} that will be used to read user data.
	 * @param bean The bean on which the access will be tested.
	 * @return <code>true</code> if the user can access, the bean,
	 * <code>false</code> otherwise.
	 */
	public static boolean canUserAccessBean(CmsUser user, CmsObject cmsObject, GroupOUAssociableBean bean) {
		boolean ret = false;

		if(bean.getOrgUnits() != null && bean.getOrgUnits().size() != 0){
			String ou = user.getOuFqn();
			for(String allowedOu : bean.getOrgUnits()){
				if(ou.startsWith(allowedOu)){
					ret = true;
					break;
				}
			}
		}
		else{
			ret = true;
		}

		if(ret){
			ret = false;
			if(bean.getGroups() != null && bean.getGroups().size() != 0){
				try {

					List<CmsGroup> groups = cmsObject.getGroupsOfUser(user.getName(), false, true);
					for(CmsGroup group : groups){
						if(bean.getGroups().contains(group.getName())){
							ret = true;
							break;
						}
					}

				} catch (CmsException e) {
					if(LOG.isWarnEnabled())
						LOG.warn("Cannot read user groups. Ignoring filter.", e);
					ret = true;
				}
			}
			else{
				ret = true;
			}
		}

		return ret;
	}

}
