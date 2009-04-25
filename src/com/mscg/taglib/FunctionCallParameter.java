/**
 *
 */
package com.mscg.taglib;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

/**
 * @author Giuseppe Miscione
 *
 */
public class FunctionCallParameter extends SimpleTagSupport {

	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(FunctionCallParameter.class);

    /** The default argument type */
    public static final String DEFAULT_TYPE = Object.class.getName();

	/**
	 * The fully qualified name of the
	 * type of the argument.
	 */
	private String type;

	/**
	 * The value of the argument.
	 */
	private Object value;

	public FunctionCallParameter(){
		super();
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
		if(this.value instanceof String){
			if(getJspContext().getAttribute((String)this.value) != null)
				this.value = getJspContext().getAttribute((String)this.value);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	public void doTag() throws JspException, IOException {
		A_ClassFunctionCaller caller = (A_ClassFunctionCaller) findAncestorWithClass(this, A_ClassFunctionCaller.class);
		if(caller == null){
			throw new JspException("The function parameter tag must be called inside a method or constructor tag.");
		}
		if(getJspBody() != null){
			StringWriter sw = new StringWriter();
			getJspBody().invoke(sw);
			if(sw.toString().trim().length() != 0)
				setValue(sw.toString());
		}
		if(getType() == null)
			setType(DEFAULT_TYPE);
		LOG.debug("Parameter type: " + getType() + "; value: " + getValue());
		caller.addMethodArgument(this);
	}
}
