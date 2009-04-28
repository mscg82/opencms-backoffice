/**
 * 
 */
package com.mscg.dyndns.dnsfactory.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.mscg.dyndns.dnsfactory.DnsProvider;

/**
 * @author Giuseppe Miscione
 *
 */
public class MemoryDnsProvider implements DnsProvider {

	private static final long serialVersionUID = -7519727804540266539L;
	
	private static Logger log = Logger.getLogger(MemoryDnsProvider.class);
	private Map<String, String> dns = null;
	
	public MemoryDnsProvider(){
		dns = new HashMap<String, String>();
	}

	/**
	 * Associates an IP to a service name. If the service has
	 * already an IP, the old IP is overwritten.
	 * 
	 * @param service The service to which the IP will be associated.
	 * @param IP the IP that will be associated to the service.
	 */
	
	public void addIP(String service, String IP) {
		log.debug("Adding IP \"" + IP + "\" to service \"" + service + "\".");
		dns.put(service, IP);
	}

	/**
	 * Returns the IP associated to the provided service. If no IP is associated,
	 * <code>null</code> is returned.
	 * 
	 * @param service The service name.
	 * @return The IP associated to the provided service, or <code>null</code> if no
	 * IP is associated.
	 */
	public String getIP(String service) {
		String ret = dns.get(service); 
		log.debug("IP for service \"" + service + "\": " +(ret == null ? "not found" : ret) + ".");
		return ret;
	}

	/**
	 * Returns a {@link Set}&lt;{@link String}&gt; with all the stored services.
	 * 
	 * @return A {@link Set}&lt;{@link String}&gt; with all the stored services.
	 */
	public Set<String> getServicesList() {
		return dns.keySet();
	}

}
