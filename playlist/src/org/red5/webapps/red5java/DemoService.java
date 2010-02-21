package org.red5.webapps.red5java;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.so.ISharedObject;
import org.slf4j.Logger;

public class DemoService extends ApplicationAdapter {

	protected static Logger log = Red5LoggerFactory.getLogger(DemoService.class, "playlist");

	/**
	 * This method returns the current counter from the Persistent SharedObject.
	 *
	 * @param None
	 *
	 * @return a <code>String</code> object of the current counter as stored
	 *         within the Scope's Persistent Shared Object.
	 *
	 */
	public Integer getCounter() {
		Integer counter;

		// Get current scope.
		IScope scope = Red5.getConnectionLocal().getScope();

		log.debug("getting the currently connected application scope's Persistent Shared Object...");
		// getting the currently connected application scope's Shared Object...
		ISharedObject counterSO = this.getSharedObject(scope, "counterSO", true);

		// get the value of the counter from the SO.
		counter = (Integer) counterSO.getAttribute("counterValue");

		return counter;
	}

	/**
	 * This method loads the playlist from the scope's playListSO Transient
	 * SharedObject.
	 *
	 * @param None
	 *
	 * @return a <code>Map&lt;String, Object&gt;</code> of the PlayList items
	 *         stored within the playListSO Shared Object.
	 *
	 */
	public Map<String, Object> getPlayList() {
		// get the current scope that the current connection is associated
		// with...
		IScope scope = Red5.getConnectionLocal().getScope();

		// playListInfo gets it's contents from this scope's Shared Object
		Map<String, Object> playListContainer = new HashMap<String, Object>();

		log.debug("------------------------------");
		log.debug("The following shared objects are currently stored....");
		this.listSharedObjectNames(scope);
		log.debug("------------------------------");

		log.debug("--------------------");
		log.debug("getting red5java scope's Transient Shared Object in DemoService...");
		// getting the currently connected application scope's Shared Object...
		ISharedObject playListSO = this.getSharedObject(scope, "playListSO", false);

		playListContainer = playListSO.getAttributes();

		if (playListContainer == null)
			log.debug("playListContainer Object is null.");

		Map<String, Object> playListInfo = new HashMap<String, Object>();
		for (Iterator iter = playListContainer.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<String, Object> container = (Map.Entry<String, Object>) iter.next();

			String key = container.getKey();
			Object value = container.getValue();

			playListInfo.put(key, value);
		}

		log.debug("--------------------");
		for (String name : playListInfo.keySet()) {
			log.debug("Name: " + name + ", Value: " + playListInfo.get(name));
		}
		log.debug("--------------------");

		// playListInfo to be returned to the Flash client...
		return playListInfo;
	}

	/**
	 * This method lists all the Shared Objects currently available under the
	 * current scope...
	 *
	 * @param None
	 *
	 * @return None
	 *
	 */
	public void listSharedObjectNames(IScope scope) {
		Iterator<String> it = this.getSharedObjectNames(scope).iterator();
		while (it.hasNext()) {
			log.debug(it.next());
		}
	}

	/**
	 * This method is called by the Flash client. If objectType is "messageTSO",
	 * then the value of Shared Object messageTSO will be changed to this value.
	 * If objectType is is "messagePSO", then the value of Shared Object
	 * messagePSO will be changed to this value. As a result of this
	 * change/update when we call the setAttribute method, the new contents of
	 * the messageTSO or the messagePSO will call the Listener SOEventListener
	 * onSharedObjectUpdate method to be triggered...
	 *
	 * @param objectType
	 *            Type of SO of type <code>String</code>
	 *
	 * @param message
	 *            The message <code>String</code>
	 *
	 * @return void.
	 *
	 */
	public void sendMessage(String objectType, String message) {
		// Get current scope.
		IScope scope = Red5.getConnectionLocal().getScope();

		if (objectType.equals("TSO")) {
			log.debug("getting the TSO with a Listener...");
			// getting the currently connected application scope's Shared
			// Object...
			ISharedObject TSO = this.getSharedObject(scope, "messageTSO", false);
			// setting the new value of the TSO should trigger
			// SOEventListener...
			log.debug("Message received from flash client for TSO: " + message);
			TSO.setAttribute("message", message);
		} else if (objectType.equals("PSO")) {
			log.debug("getting the PSO with a Listener...");
			// getting the currently connected application scope's Shared
			// Object...
			ISharedObject PSO = this.getSharedObject(scope, "messagePSO", false);
			// /setting the new value of the PSO should trigger
			// SOEventListener...
			log.debug("Message received from flash client for PSO: " + message);
			PSO.setAttribute("message", message);
		}
	}
}