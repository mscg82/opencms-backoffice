/**
 * 
 */
package com.mscg.dyndns.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

/**
 * @author Giuseppe Miscione
 *
 */
public class LoggingServlet extends HttpServlet {

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
	    	PropertyConfigurator.configure(prefix + file);
	    }
	}

}
