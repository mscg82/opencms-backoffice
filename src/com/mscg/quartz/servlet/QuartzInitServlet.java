/**
 *
 */
package com.mscg.quartz.servlet;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import com.mscg.quartz.util.SchedulerInstanceBuilder;

/**
 * @author Giuseppe Miscione
 *
 */
public class QuartzInitServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(QuartzInitServlet.class);

	private static final long serialVersionUID = -1667931266851402484L;

	private ServletConfig config;

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.config = config;
		try {
			String prefix =  config.getServletContext().getRealPath("/");
			if(this.config.getInitParameter("configuration-file") != null){
				String file = (prefix + this.config.getInitParameter("configuration-file")).replace("\\", "/");
				File f = new File(file);
				if(!f.exists())
					throw new ServletException("Quartz configuration file cannot be found in path \"" + file + "\".");
				SchedulerInstanceBuilder.initInstance(file);
			}
			else{
				throw new ServletException("Configuration file parameter is needed to initialize the quartz scheduler.");
			}
		} catch (SchedulerException e) {
			throw new ServletException(e);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	public void destroy() {
		Boolean wait = new Boolean(config.getInitParameter("wait-for-jobs"));
		try {
			SchedulerInstanceBuilder.getInstance().shutdown(wait);
		} catch (SchedulerException e) {
			log.error("Error found while shutting down the scheduler.", e);
		}
	}

}
