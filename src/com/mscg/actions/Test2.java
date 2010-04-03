package com.mscg.actions;
/**
 *
 */


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Giuseppe Miscione
 *
 */
public class Test2 extends ActionSupport {

	private static final long serialVersionUID = -1229729320390982715L;

	private static Log LOG = LogFactory.getLog(Test2.class);

	public Test2() {

	}

	public String execute() throws Exception {
		LOG.debug("We are in action " + this.getClass());
		return "jsp";
	}

}
