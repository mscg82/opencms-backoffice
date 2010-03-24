package com.mscg.actions;
/**
 *
 */


import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;

import com.mscg.actions.models.TestModel;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author Giuseppe Miscione
 *
 */
public class Test extends ActionSupport implements ModelDriven<TestModel>, SessionAware, ApplicationAware {

	private static final long serialVersionUID = -1229729320390982715L;

	private TestModel model;
	private Map<String, Object> session;
	private Map<String, Object> application;

	private static Log LOG = LogFactory.getLog(Test.class);

	public Test() {
		model = new TestModel();
	}

	public String execute() throws Exception {
		application.put("last-requested-file", model.getFilename());
		return "result";
	}

	public InputStream getFileStream() {
		boolean decode = false;
		URL fileUrl = this.getClass().getResource(model.getFilename());
		if(fileUrl == null) {
			fileUrl = this.getClass().getResource(model.getFilename() + ".txt");
			decode = true;
		}
		InputStream is = null;
		try {
			is = fileUrl.openStream();
			if(decode){
				is = new Base64InputStream(is, false);
			}
		} catch (Exception e) {
			LOG.error("Error found while getting file url stream.", e);
		}
		return is;
	}

	public TestModel getModel() {
		return model;
	}

	/**
	 * @return the session
	 */
	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		LOG.debug("Session class is \"" + session.getClass().getCanonicalName() + "\"");
		this.session = session;
	}

	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}

}
