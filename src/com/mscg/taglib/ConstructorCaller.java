/**
 *
 */
package com.mscg.taglib;

import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.servlet.jsp.JspException;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

/**
 * @author Giuseppe Miscione
 *
 */
public class ConstructorCaller extends A_ClassFunctionCaller {

	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(ConstructorCaller.class);

    /**
     * The fully qualified name of the class whose constructor
     * will be called.
     */
    private String className;

    public ConstructorCaller(){
    	super();
    }

    /**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	public void doTag() throws JspException, IOException {
		try {
			LOG.debug("Invoking body of the tag to find method arguments.");
			if(getJspBody() != null)
				getJspBody().invoke(null);
			LOG.debug("Body evalueted. Calling the constructor of the class \"" + getClassName() + "\".");

			Class actualClass = Class.forName(getClassName());
			Constructor c = actualClass.getConstructor(buildParametersClassesArray());
			Object o = c.newInstance(buildParametersValuesArray());

			getJspContext().setAttribute(getVar(), o);

			LOG.debug("Constructor called.");
		} catch (Exception e) {
			throw new JspException(e);
		}
	}
}
