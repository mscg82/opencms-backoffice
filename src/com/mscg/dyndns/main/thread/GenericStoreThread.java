/**
 *
 */
package com.mscg.dyndns.main.thread;

import java.io.IOException;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import com.mscg.config.ConfigLoader;
import com.mscg.httpinterface.EatjTesterInterface;
import com.mscg.httpinterface.IPStorageInterface;
import com.mscg.util.Util;

/**
 * @author Giuseppe Miscione
 *
 */
public abstract class GenericStoreThread extends Thread {

	private static Logger log = Logger.getLogger(GenericStoreThread.class);

	protected IPStorageInterface storageInterface;

	protected EatjTesterInterface testerInterface;

	protected String service;
	protected long timeout;

	protected boolean exit;

	protected GenericStoreThread() throws ConfigurationException, ClassCastException, IOException{
		setExit(false);

		timeout = 300;
		try{
			timeout = Long.parseLong((String)ConfigLoader.getInstance().get(ConfigLoader.DYNDNS_THREAD_TIMEOUT));
		} catch(NumberFormatException e){ /* Bad numeric format, using default */}
		timeout *= 1000;

		storageInterface = new IPStorageInterface();

		testerInterface = new EatjTesterInterface();

		service = (String) ConfigLoader.getInstance().get(ConfigLoader.DYNDNS_SERVICE);
		if(service == null)
			service = "mscg";

		log.debug("Thread will run every " + timeout + " milliseconds for service \"" + service + "\".");
	}

	/**
	 * @return the exit
	 */
	public synchronized boolean isExit() {
		return exit;
	}

	public abstract List<String> retrieveIPs();

	public void run() {
		while(true){

			try {
				boolean store = false;

				log.debug("Checking if server is running...");
				boolean running = testerInterface.testIfServerIsRunning();

				if(running){
					log.debug("Server is running correctly.");
					store = true;
				}
				else{
					log.debug("Server is not running. Trying to start it");
					if(testerInterface.startServer()){
						log.debug("Server started successfully.");
						store = true;
					}
					else
						log.debug("Cannot start server. Aborting IP storage.");
				}

				if(store){
					log.debug("Storing IPs...");
					List<String> IPs = retrieveIPs();

					storageInterface.storeIP(service, IPs);
					log.debug("IPs stored!");
				}
			} catch (Exception e) {
				log.error("Error found in HTTP comunication: " + e.getMessage());
				Util.logStackTrace(e, log);
			}

			if(isExit())
				return;

			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) { }

		}
	}

	/**
	 * @param exit the exit to set
	 */
	public synchronized void setExit(boolean exit) {
		this.exit = exit;
	}



}
