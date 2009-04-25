/**
 *
 */
package com.mscg.taglib;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

/**
 * @author Giuseppe Miscione
 *
 */
public class MethodCaller extends A_ClassFunctionCaller{

	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(MethodCaller.class);

    /**
     * The EL variable which contains the object on which the method
     * will be called.
     */
    private Object object;

    /**
     * The name of the method that will be invoked.
     */
    private String method;

    /**
     * The switch to choose if the output of the method
     * must be printed to the output.
     */
    private Boolean output;

    /**
     * The class or the object which method will be called.
     */
    private Class actualClass;

    public MethodCaller(){
    	super();
    	output = Boolean.FALSE;
    }

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	public void doTag() throws JspException, IOException {
		try {
			LOG.debug("Invoking body of the tag to find method arguments.");
			if(getJspBody() != null)
				getJspBody().invoke(null);
			LOG.debug(
				"Body evalueted. Calling the method \"" + getMethod() + "\" " +
				"on object of type \"" + getObject().getClass() + "\".");

			Method theMethod = actualClass.getMethod(getMethod(), buildParametersClassesArray());
			Object returnVal = theMethod.invoke(getObject(), buildParametersValuesArray());
			if(returnVal != null && getVar() != null){
				getJspContext().setAttribute(getVar(), returnVal);
			}
			if(getOutput())
				getJspContext().getOut().append(returnVal.toString());
			LOG.debug("Method called and output set.");
		} catch (Exception e) {
			throw new JspException(e);
		}
	}

	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @return the output
	 */
	public Boolean getOutput() {
		return output;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
		boolean staticMethod = false;

		try{
			String className = (String) this.object;
			actualClass = resolveType(className);
			staticMethod = true;
		} catch(Exception e){

		}

		if(!staticMethod){
			if(this.object instanceof String){
				if(getJspContext().getAttribute((String)this.object) != null)
					this.object = getJspContext().getAttribute((String)this.object);
			}
			actualClass = this.object.getClass();
		}
	}

	/**
	 * @param output the output to set
	 */
	public void setOutput(Boolean output) {
		this.output = output;
	}

}
