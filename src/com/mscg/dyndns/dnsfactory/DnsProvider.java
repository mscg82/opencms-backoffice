/**
 * 
 */
package com.mscg.dyndns.dnsfactory;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Giuseppe Miscione
 *
 */
public interface DnsProvider extends Serializable{
	
	/**
	 * Associates an IP to a service name. If the service has
	 * already an IP, the old IP is overwritten.
	 * 
	 * @param service The service to which the IP will be associated.
	 * @param IP the IP that will be associated to the service.
	 */
	public void addIP(String service, String IP);
	
	/**
	 * Returns the IP associated to the provided service. If no IP is associated,
	 * <code>null</code> is returned.
	 * 
	 * @param service The service name.
	 * @return The IP associated to the provided service, or <code>null</code> if no
	 * IP is associated.
	 */
	public String getIP(String service);
	
	/**
	 * Returns a {@link Set}&lt;{@link String}&gt; with all the stored services.
	 * 
	 * @return A {@link Set}&lt;{@link String}&gt; with all the stored services.
	 */
	public Set<String> getServicesList();
}
