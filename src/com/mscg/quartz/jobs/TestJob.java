/**
 *
 */
package com.mscg.quartz.jobs;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 * @author Giuseppe Miscione
 *
 */
public class TestJob implements StatefulJob {
	private static Logger log = Logger.getLogger(TestJob.class);

	public static final String VALUE_PARAM = "value";
	public static final String DATE_PARAM  = "date";

	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String jobName = context.getJobDetail().getFullName();
		String instName = context.getJobDetail().getName();
		String instGroup = context.getJobDetail().getGroup();

		long now = System.currentTimeMillis();
		Date nowDate = new Date(now);
		if (context.isRecovering()) {
			log.debug("TestJob: " + jobName + " RECOVERING at " + nowDate);
        } else {
        	log.debug("TestJob: " + jobName + " starting at " + nowDate);
        }

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		int value = 0;
		if(dataMap.containsKey(VALUE_PARAM))
			value = dataMap.getInt(VALUE_PARAM);
		if(dataMap.containsKey(DATE_PARAM)){
			log.debug("Time from last run: " + ((now - dataMap.getLong(DATE_PARAM)) / 1000) + " sec");
		}
		dataMap.put(DATE_PARAM, now);
		log.debug("Job \"" + instGroup + "/" + instName + "\" generated value " + value);
		dataMap.put(VALUE_PARAM, ++value);

	}

}
