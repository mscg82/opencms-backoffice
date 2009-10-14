package com.mashfrog.backoffice.actions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsLog;

import com.mashfrog.backoffice.CmsBackofficeActionElement;
import com.mashfrog.backoffice.project.beans.ActionBean;

public interface I_BackofficeAction {

	public static class Factory {

		private static Log LOG = CmsLog.getLog(Factory.class);

		public static I_BackofficeAction newInstance(CmsBackofficeActionElement backofficeActionElement, ActionBean actionBean)
				throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException,
				       InstantiationException, IllegalAccessException, InvocationTargetException {

			I_BackofficeAction ret = null;
			Class clazz = null;

			// first try: use the ClassLoader that loaded this class
			try {
				LOG.debug("Loading " + actionBean.getClassName() + " with Factory class ClassLoader...");
				clazz = Factory.class.getClassLoader().loadClass(actionBean.getClassName());
				LOG.debug("Action class loaded.");
			} catch (ClassNotFoundException e) {
				LOG.debug("Factory class ClassLoader can't load action class.", e);
			}

			if(clazz == null) {
				// second try: use the ClassLoader that loaded the servlet context
				try {
					LOG.debug("Loading " + actionBean.getClassName() + " with servlet context ClassLoader...");
					clazz = backofficeActionElement.getJspContext().getServletContext().getClass().getClassLoader().loadClass(actionBean.getClassName());
					LOG.debug("Action class loaded.");
				} catch (ClassNotFoundException e) {
					LOG.debug("Servlet context ClassLoader can't load action class.", e);
				}
			}

			if(clazz  == null) {
				// third try: use the system ClassLoader
				try {
					LOG.debug("Loading " + actionBean.getClassName() + " with system ClassLoader...");
					clazz = ClassLoader.getSystemClassLoader().loadClass(actionBean.getClassName());
					LOG.debug("Action class loaded.");
				} catch (ClassNotFoundException e) {
					LOG.debug("System ClassLoader can't load action class.", e);
				}
			}

			if(clazz == null){
				throw new ClassNotFoundException("Can't load action class " + actionBean.getClassName());
			}

			Class noParamsType[] = {};
			Object noParams[] = {};
			Constructor constr = clazz.getConstructor(noParamsType);
			ret = (I_BackofficeAction)constr.newInstance(noParams);
			ret.init(backofficeActionElement, actionBean);

			LOG.debug("Action object (" + ret.toString() + ") initialized correctly.");

			return ret;
		}

	}

    public void addAllowedGroup(String allowedGroup);

    public String execute();

    public List<String> getAllowedGroups();

    public String getErrorMessage();

    public String getFatalErrorMessage();

    public boolean getHasCommandMenu();

    public String getJspPath();

    public boolean hasBody();

    public void init(CmsBackofficeActionElement backofficeActionElement, ActionBean actionBean);

    public void setAdditonalConfigurationFilePath(String filePath, CmsObject cmsObject);

    public void setErrorMessage(String errorMessage);

    public void setFatalErrorMessage(String fatalErrorMessage);

}
