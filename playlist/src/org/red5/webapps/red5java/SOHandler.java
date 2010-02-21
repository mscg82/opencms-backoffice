package org.red5.webapps.red5java;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.so.ISharedObject;
import org.slf4j.Logger;

public class SOHandler extends ApplicationAdapter {

	protected static Logger log = Red5LoggerFactory.getLogger(SOHandler.class, "playlist");

	/**
	 * This method is called by the Flash client. If objectType is "messageTSO",
	 * then the value of messageTSO will be changed to this value. If objectType
	 * is is "messagePSO", then the value of messagePSO will be changed to this
	 * value. As a result of this change/update, the SOEventListener should be
	 * triggered...
	 *
	 * @param objectType Type of SO of type <code>String</code>
	 *
	 * @param message The message <code>String</code>
	 *
	 * @return void.
	 *
	 */
	public void sendMessage(String objectType, String message) {
		// Now do something
		// Get current scope.
		IScope scope = Red5.getConnectionLocal().getScope();

		if (objectType.equals("TSO")) {
			log.debug("getting the TSO with a Listener from the SO Handler and setAttribute to trigger SOEventListener...");
			log.debug("Message received from the flash client: " + message);
			// getting the currently connected application scope's Shared
			// Object...
			ISharedObject TSO = this.getSharedObject(scope, "messageTSO", false);
			// setting the new value of the TSO should trigger
			// SOEventListener...
			TSO.setAttribute("messageTSO", message);
		} else if (objectType.equals("PSO")) {
			log.debug("getting the PSO with a Listener from the SO Handler and setAttribute to trigger SOEventListener...");
			log.debug("Message received from the flash client: " + message);
			// getting the currently connected application scope's Shared
			// Object...
			ISharedObject PSO = this.getSharedObject(scope, "messagePSO", false);
			// /setting the new value of the PSO should trigger
			// SOEventListener...
			PSO.setAttribute("messagePSO", message);
		}
	}
}