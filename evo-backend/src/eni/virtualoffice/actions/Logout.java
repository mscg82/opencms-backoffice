/**
 *
 */
package eni.virtualoffice.actions;

import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;

import eni.virtualoffice.util.Constants;

/**
 * @author Giuseppe Miscione
 *
 */
public class Logout extends GenericAction {

	private static final long serialVersionUID = -3148246284722973812L;

	private static Logger LOG = Red5LoggerFactory.getLogger(Logout.class, Constants.contextName);

	public Logout() {
		super();
	}

	@Override
	public String execute() throws Exception {
		LOG.debug("Logout action is being executed");
		session.clear();
		return "success";
	}
}
