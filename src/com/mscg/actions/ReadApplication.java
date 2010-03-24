/**
 *
 */
package com.mscg.actions;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ApplicationAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Giuseppe Miscione
 *
 */
public class ReadApplication extends ActionSupport implements ApplicationAware {

	private static final long serialVersionUID = -7631557889079955078L;

	private static Log LOG = LogFactory.getLog(ReadApplication.class);

	private Map<String, Object> application;

	public String execute() {
		LOG.debug("In execute");
		return "result";
	}

	public String lastApp() {
		LOG.debug("In lastApp");
		return "jsp";
	}

	public InputStream getApplValue() {
		return new ByteArrayInputStream(getLastRequestedFile().getBytes());
	}

	public String getInputName() {
		return "applValue";
	}

	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}

	public String getLastRequestedFile() {
		LOG.debug("In getLastRequestedFile");
		return (String)application.get("last-requested-file");
	}

}
