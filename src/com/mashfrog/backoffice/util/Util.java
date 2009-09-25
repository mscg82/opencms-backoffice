package com.mashfrog.backoffice.util;

import java.util.List;

import org.opencms.file.types.A_CmsResourceType;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.OpenCms;
import org.opencms.module.CmsModule;

import com.mashfrog.backoffice.actions.constants.Constants;

public class Util {

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

}
