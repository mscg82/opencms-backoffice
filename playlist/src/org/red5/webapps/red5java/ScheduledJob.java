package org.red5.webapps.red5java;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.scheduling.IScheduledJob;
import org.red5.server.api.scheduling.ISchedulingService;
import org.slf4j.Logger;

public class ScheduledJob implements IScheduledJob {

	protected static Logger log = Red5LoggerFactory.getLogger(ScheduledJob.class, "playlist");

	public void execute(ISchedulingService service) {
		// Do something
		log.debug("-----------------------");
		log.debug("Executing Scheduled Job!");
		log.debug("It's time to do something here eg for example reload playlist...");
		//try to reload playlist everytime this is called...
		log.debug("-----------------------");
	}
}