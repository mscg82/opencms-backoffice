/**
 * 
 */
package com.mscg.dyndns.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.configuration.ConfigurationException;

import com.mscg.config.ConfigLoader;

/**
 * @author Giuseppe Miscione
 *
 */
public class ConfigurationServlet extends HttpServlet {

	private static final long serialVersionUID = -6067317487341409614L;

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			ConfigLoader.initInstance(config.getServletContext());
		} catch (ConfigurationException e) {
			throw new ServletException(e);
		}
	}

}
