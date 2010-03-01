/**
 *
 */
package eni.virtualoffice.actions;

import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;

import com.opensymphony.xwork2.ModelDriven;

import eni.virtualoffice.actions.models.BackofficeModel;
import eni.virtualoffice.util.Constants;

/**
 * @author Giuseppe Miscione
 *
 */
public class Backoffice extends GenericAction implements ModelDriven<BackofficeModel> {

	private static final long serialVersionUID = 5679311247233107683L;
	private static Logger LOG = Red5LoggerFactory.getLogger(Backoffice.class, Constants.contextName);

	protected BackofficeModel model;

	public Backoffice() {
		super();
		model = new BackofficeModel();
	}

	public String users() {
		String ret = "login";
		LOG.debug("Users action in backoffice is being executed");
		if(session.get(userSessionParam) != null) {
			ret = "users";
		}
		return ret;
	}

	@Override
	public BackofficeModel getModel() {
		return model;
	}

}
