/**
 *
 */
package com.mscg.dyndns.dnsfactory.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.mscg.dyndns.dnsfactory.DnsProvider;

/**
 * @author Giuseppe Miscione
 *
 */
public class MemoryDnsProvider implements DnsProvider {

	private static final long serialVersionUID = -2411351902707072378L;

	private static Logger log = Logger.getLogger(MemoryDnsProvider.class);
	private Map<String, Collection<String>> dns = null;

	public MemoryDnsProvider(){
		dns = new HashMap<String, Collection<String>>();
	}

	/**
	 * Associates an IP to a service name. An arbitrary number of
	 * IPs can be associated to a service.
	 *
	 * @param service The service to which the IP will be associated.
	 * @param IP the IP that will be associated to the service.
	 */
	public void addIP(String service, String IP) {
		log.debug("Adding IP \"" + IP + "\" to service \"" + service + "\".");
		Collection<String> ips = dns.get(service);
		if(ips == null) {
			ips = new LinkedList<String>();
			dns.put(service, ips);
		}
		ips.add(IP);
	}

	/**
	 * Removes all the IPs associated to the specified service.
	 *
	 * @param service The service whose IPs will be deleted.
	 */
	public void clearService(String service) {
		Collection<String> ret = dns.get(service);
		if(ret != null){
			ret.clear();
			dns.remove(service);
		}
	}

	/**
	 * Returns the IPs associated to the provided service. If no IP is associated,
	 * <code>null</code> is returned.
	 *
	 * @param service The service name.
	 * @return The IPs associated to the provided service, or <code>null</code> if no
	 * IP is associated.
	 */
	public Collection<String> getIPs(String service) {
		Collection<String> ret = dns.get(service);
		log.debug("IPs for service \"" + service + "\": " +(ret == null ? "not found" : ret) + ".");
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
