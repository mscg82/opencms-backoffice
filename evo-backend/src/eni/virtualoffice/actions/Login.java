/**
 *
 */
package eni.virtualoffice.actions;

import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;

import com.opensymphony.xwork2.ModelDriven;

import eni.virtualoffice.actions.models.LoginModel;
import eni.virtualoffice.util.Constants;

/**
 * @author Giuseppe Miscione
 *
 */
public class Login extends GenericAction implements ModelDriven<LoginModel> {

	private static final long serialVersionUID = -5562397326576186633L;
	private static Logger LOG = Red5LoggerFactory.getLogger(Login.class, Constants.contextName);

	protected LoginModel model;

	public Login() {
		super();
		model = new LoginModel();
	}

	@Override
	public String execute() throws Exception {
		LOG.debug("Login action is being executed");

		return "login";
	}

	@Override
	public LoginModel getModel() {
		return model;
	}

}
