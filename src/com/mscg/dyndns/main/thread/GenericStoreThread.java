/**
 *
 */
package com.mscg.dyndns.main.thread;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import com.mscg.config.ConfigLoader;
import com.mscg.httpinterface.IPStorageInterface;
import com.mscg.util.Util;

/**
 * @author Giuseppe Miscione
 *
 */
public abstract class GenericStoreThread extends Thread {

	private static Logger log = Logger.getLogger(GenericStoreThread.class);

	protected IPStorageInterface storageInterface;

	protected String service;

	protected long timeout;

	protected GenericStoreThread() throws ConfigurationException{
		timeout = 300;
		try{
			timeout = Long.parseLong((String)ConfigLoader.getInstance().get(ConfigLoader.DYNDNS_THREAD_TIMEOUT));
		} catch(NumberFormatException e){ /* Bad numeric format, using default */}
		timeout *= 1000;

		storageInterface = new IPStorageInterface();

		service = (String) ConfigLoader.getInstance().get(ConfigLoader.DYNDNS_SERVICE);
		if(service == null)
			service = "mscg";

		log.debug("Thread will run every " + timeout + " milliseconds for service \"" + service + "\".");
	}

	public abstract List<String> retrieveIPs();

	public void run() {
		while(true){

			log.debug("Storing IPs...");

			List<String> IPs = retrieveIPs();

			try {
				storageInterface.storeIP(service, IPs);
				log.debug("IPs stored!");
			} catch (Exception e) {
				log.error("Error found in HTTP comunication: " + e.getMessage());
				Util.logStackTrace(e, log);
			}

			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

}
