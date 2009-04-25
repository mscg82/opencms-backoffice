/**
 *
 */
package com.mscg.quartz.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author Giuseppe Miscione
 *
 */
public class LoggingServlet extends HttpServlet {
	private static Logger log = null;

	private static final long serialVersionUID = -2087276506757775217L;

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String prefix =  config.getServletContext().getRealPath("/");
	    String file = config.getInitParameter("log4j-init-file");
	    // if the log4j-init-file is not set, then no point in trying
	    if(file != null) {
	    	String filePath = (prefix + file).replace("\\", "/");
	    	PropertyConfigurator.configure(filePath);
	    	log = Logger.getLogger(LoggingServlet.class);
	    	log.debug("Log4j initialized corretly from file \"" + filePath + "\"");
	    }
	}

}
