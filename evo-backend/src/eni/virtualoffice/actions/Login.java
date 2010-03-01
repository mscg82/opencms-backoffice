/**
 *
 */
package eni.virtualoffice.actions;

import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;

import com.opensymphony.xwork2.ModelDriven;

import eni.virtualoffice.actions.models.LoginModel;
import eni.virtualoffice.persistence.entities.Esaminatore;
import eni.virtualoffice.persistence.stores.UsersStore;
import eni.virtualoffice.util.Constants;

/**
 * Action to manage esaminatori's login.
 *
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

		if(session.get(userSessionParam) != null) {
			return "success";
		}

		if(model.getUsername() != null && model.getPassword() != null) {
			// check for admin existence
			UsersStore us = new UsersStore();
			Esaminatore es = us.checkEsaminatoreCredentials(model.getUsername(), model.getPassword());
			if(es != null) {
				LOG.debug("User " + model.getUsername() + " logged in successfully.");
				session.put(userSessionParam, es);
				return "success";
			}
			else {
				setErrorMessageKey("login.error.wrong.credential");
				LOG.debug("Wrong user credentials provided for user " + model.getUsername());
			}
		}

		return "login";
	}

	@Override
	public LoginModel getModel() {
		return model;
	}

}
