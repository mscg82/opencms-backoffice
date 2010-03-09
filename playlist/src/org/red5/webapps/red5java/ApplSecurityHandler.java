/**
 *
 */
package org.red5.webapps.red5java;

import java.util.List;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.IScope;
import org.red5.server.api.so.ISharedObject;
import org.red5.server.api.so.ISharedObjectSecurity;
import org.slf4j.Logger;

/**
 * @author Giuseppe Miscione
 *
 */
public class ApplSecurityHandler  implements ISharedObjectSecurity{

	private static Logger LOG = Red5LoggerFactory.getLogger(ApplSecurityHandler.class);

	public boolean isConnectionAllowed(ISharedObject so) {
		LOG.debug("isConnectionAllowed");
		return true;
	}

	public boolean isCreationAllowed(IScope scope, String name,
			boolean persistent) {
		LOG.debug("isCreationAllowed");
		return true;
	}

	public boolean isDeleteAllowed(ISharedObject so, String key) {
		LOG.debug("isDeleteAllowed");
		return true;
	}

	public boolean isSendAllowed(ISharedObject so, String message,
			List arguments) {
		LOG.debug("isSendAllowed");
		return true;
	}

	public boolean isWriteAllowed(ISharedObject so, String key, Object value) {
		LOG.debug("isWriteAllowed");
		return true;
	}

}
