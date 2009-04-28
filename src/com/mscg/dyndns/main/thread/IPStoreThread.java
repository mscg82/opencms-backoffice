/**
 * 
 */
package com.mscg.dyndns.main.thread;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.mscg.config.ConfigLoader;
import com.mscg.httpinterface.IPStorageInterface;
import com.mscg.util.Util;

/**
 * @author Giuseppe Miscione
 *
 */
public class IPStoreThread extends Thread {
	
	private static Logger log = Logger.getLogger(IPStoreThread.class);
	
	private IPStorageInterface storageInterface;
	
	private String service;
	
	private long timeout;
	
	public IPStoreThread() throws ConfigurationException{
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
	
	public void run() {
		while(true){
			try {
				log.debug("Storing IP...");
				storageInterface.storeIP(service);
				log.debug("IP stored!");
			} catch (HttpException e) {
				log.error("Error found in HTTP comunication: " + e.getMessage());
				Util.logStackTrace(e, log);
			} catch (IOException e) {
				log.error("Error found while storing IP: " + e.getMessage());
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
