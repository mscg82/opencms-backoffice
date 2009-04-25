/**
 *
 */
package com.mscg.quartz.servlet;

import java.io.File;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;

import com.mscg.quartz.jobs.TestJob;
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

				scheduleJobs();

				SchedulerInstanceBuilder.getInstance().start();
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
			cleanUp(SchedulerInstanceBuilder.getInstance());
			SchedulerInstanceBuilder.getInstance().shutdown(wait);
		} catch (SchedulerException e) {
			log.error("Error found while shutting down the scheduler.", e);
		}
	}

	public void cleanUp(Scheduler inScheduler) throws SchedulerException {
		Boolean cleanEnabled = new Boolean(config.getInitParameter("clean-enabled"));
		if(cleanEnabled){
	        log.warn("***** Deleting existing jobs/triggers *****");

	        // unschedule jobs
	        String[] groups = inScheduler.getTriggerGroupNames();
	        for (int i = 0; i < groups.length; i++) {
	            String[] names = inScheduler.getTriggerNames(groups[i]);
	            for (int j = 0; j < names.length; j++) {
	                inScheduler.unscheduleJob(names[j], groups[i]);
	            }
	        }

	        // delete jobs
	        groups = inScheduler.getJobGroupNames();
	        for (int i = 0; i < groups.length; i++) {
	            String[] names = inScheduler.getJobNames(groups[i]);
	            for (int j = 0; j < names.length; j++) {
	                inScheduler.deleteJob(names[j], groups[i]);
	            }
	        }

	        log.warn("***** Existing jobs/triggers deleted *****");
		}

    }

	private void scheduleJobs(){
		try{
			cleanUp(SchedulerInstanceBuilder.getInstance());

			SimpleTrigger trigger = new SimpleTrigger("myTrigger",
	                null,
	                new Date(),
	                null,
	                SimpleTrigger.REPEAT_INDEFINITELY,
	                10L * 1000L);

			String schedId = SchedulerInstanceBuilder.getInstance().getSchedulerInstanceId();

			JobDetail job = new JobDetail("myJob",
					schedId,
	                TestJob.class);
			job.setRequestsRecovery(true);
			job.getJobDataMap().put(TestJob.VALUE_PARAM, 0);

			try{
				SchedulerInstanceBuilder.getInstance().scheduleJob(job, trigger);
			} catch(SchedulerException e){
				log.debug(e.getMessage());
			}
		} catch(SchedulerException e){
			log.error("Error found while scheduling jobs.", e);
		}
	}

}
