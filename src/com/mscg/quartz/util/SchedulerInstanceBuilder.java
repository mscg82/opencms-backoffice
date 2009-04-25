/**
 *
 */
package com.mscg.quartz.util;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author Giuseppe Miscione
 *
 */
public class SchedulerInstanceBuilder {

	private static Logger log = Logger.getLogger(SchedulerInstanceBuilder.class);

	private static SchedulerFactory schedFact;
	private static Scheduler sched;

	/**
	 * Inits the scheduler instance reading the quartz configuration from the supplied
	 * file.
	 *
	 * @param configFileName The file name of the quartz configuration file used to
	 * initialize the scheduler instance.
	 * @return The inizialized {@link Scheduler} instance.
	 * @throws SchedulerException If an error occurs while initializing the instance.
	 */
	public static Scheduler initInstance(String configFileName) throws SchedulerException{
		schedFact = new StdSchedulerFactory(configFileName);
		sched = schedFact.getScheduler();

		log.debug("Quartz scheduler correctly initialized from file \"" + configFileName + "\".");

		return getInstance();
	}

	/**
	 * Returns the scheduler instance.
	 *
	 * @return The inizialized {@link Scheduler} instance.
	 */
	public static Scheduler getInstance(){
		return sched;
	}
}
