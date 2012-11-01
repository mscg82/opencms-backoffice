/**
 *
 */
package com.mscg.dyndns.main.thread;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import com.mscg.httpinterface.MyIpAddressInterface;
import com.mscg.util.Util;

/**
 * @author Giuseppe Miscione
 *
 */
public class MyIpAddressStoreThread extends GenericStoreThread {

	private static Logger log = Logger.getLogger(MyIpAddressStoreThread.class);

	private Pattern pattern;

	public MyIpAddressStoreThread() throws ConfigurationException, ClassCastException, IOException {
		super();
		pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
	}

	@Override
	public List<String> retrieveIPs() {
		List<String> IPs = new LinkedList<String>();
		try {
			MyIpAddressInterface ipaddrintrf = new MyIpAddressInterface();
			String resp = ipaddrintrf.getMyIpResponse();
			Matcher matcher = pattern.matcher(resp);
			while(matcher.find()){
				IPs.add(matcher.group());
			}

		} catch (Exception e) {
			log.error("Error found while reading IP (" + e.getClass().getCanonicalName() + "): " + e.getMessage());
			Util.logStackTrace(e, log);
			IPs.clear();
		}
		return IPs;
	}

}
