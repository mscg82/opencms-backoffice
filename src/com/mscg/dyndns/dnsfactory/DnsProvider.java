/**
 *
 */
package com.mscg.dyndns.dnsfactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * @author Giuseppe Miscione
 *
 */
public interface DnsProvider extends Serializable{

	/**
	 * Associates an IP to a service name. An arbitrary number of
	 * IPs can be associated to a service.
	 *
	 * @param service The service to which the IP will be associated.
	 * @param IP the IP that will be associated to the service.
	 */
	public void addIP(String service, String IP);

	/**
	 * Removes all the IPs associated to the specified service.
	 *
	 * @param service The service whose IPs will be deleted.
	 */
	public void clearService(String service);

	/**
	 * Returns the IPs associated to the provided service. If no IP is associated,
	 * <code>null</code> is returned.
	 *
	 * @param service The service name.
	 * @return The IPs associated to the provided service, or <code>null</code> if no
	 * IP is associated.
	 */
	public Collection<String> getIPs(String service);

	/**
	 * Returns a {@link Set}&lt;{@link String}&gt; with all the stored services.
	 *
	 * @return A {@link Set}&lt;{@link String}&gt; with all the stored services.
	 */
	public Set<String> getServicesList();
}
