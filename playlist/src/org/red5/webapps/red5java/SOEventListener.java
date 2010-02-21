package org.red5.webapps.red5java;

import java.util.List;
import java.util.Map;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.IAttributeStore;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectBase;
import org.red5.server.api.so.ISharedObjectListener;
import org.slf4j.Logger;

public class SOEventListener implements ISharedObjectListener
{
	protected static Logger log = Red5LoggerFactory.getLogger(SOEventListener.class, "playlist");

	/**
	 * Called when a client connects to a shared object.
	 *
	 * @param so
	 *            the shared object
	 */
	public void onSharedObjectConnect(ISharedObjectBase so)
	{

	}

	/**
	 * Called when a client disconnects from a shared object.
	 *
	 * @param so
	 *            the shared object
	 */
	public void onSharedObjectDisconnect(ISharedObjectBase so)
	{

	}
	/**
	 * Called when a shared object attribute is updated.
	 *
	 * @param so
	 *            the shared object
	 * @param key
	 *            the name of the attribute
	 * @param value
	 *            the value of the attribute
	 */
	public void onSharedObjectUpdate(ISharedObject so, String key, Object value)
	{

	}

	/**
	 * Called when multiple attributes of a shared object are updated.
	 *
	 * @param so
	 *            the shared object
	 * @param values
	 *            the new attributes of the shared object
	 */

	public void onSharedObjectUpdate(ISharedObjectBase so, IAttributeStore values)
	{

	}

	/**
	 * Called when a shared object attribute is updated.
	 *
	 * @param so
	 *            the shared object
	 * @param key
	 *            the name of the attribute
	 * @param value
	 *            the value of the attribute
	 */
	public void onSharedObjectUpdate(ISharedObjectBase so, String key, Object value)
	{
		//The attribute <key> of the shared object <so>
		// was changed to <value>.
		// Do something....
		log.debug("---------->"+so.toString() +"| Key:"+key+", Value:"+value);
		log.debug("---------->SO Update has triggered the Listener's onSharedObjectUpdate method.");
	}

	/**
	 * Called when multiple attributes of a shared object are updated.
	 *
	 * @param so
	 *            the shared object
	 * @param values
	 *            the new attributes of the shared object
	 */

	public void onSharedObjectUpdate(ISharedObjectBase so, Map<String, Object> values)
	{

	}

	/**
	 * Called when an attribute is deleted from the shared object.
	 *
	 * @param so
	 *            the shared object
	 * @param key
	 *            the name of the attribute to delete
	 */
	public void onSharedObjectDelete(ISharedObjectBase so, String key)
	{

	}

	/**
	 * Called when all attributes of a shared object are removed.
	 *
	 * @param so
	 *            the shared object
	 */
	public void onSharedObjectClear(ISharedObjectBase so)
	{

	}

	/**
	 * Called when a shared object method call is sent.
	 *
	 * @param so
	 *            the shared object
	 * @param method
	 *            the method name to call
	 * @param params
	 *            the arguments
	 */
	public void onSharedObjectSend(ISharedObjectBase so, String method, List params)
	{

	}
}