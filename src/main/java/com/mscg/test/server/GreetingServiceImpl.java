package com.mscg.test.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mscg.test.client.service.GreetingService;
import com.mscg.test.shared.FieldVerifier;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	protected Logger LOG;

	private String springGreeting;

	public GreetingServiceImpl() {
		LOG = LoggerFactory.getLogger(this.getClass());
	}

	/**
	 * @param springGreeting the springGreeting to set
	 */
	public void setSpringGreeting(String springGreeting) {
		this.springGreeting = springGreeting;
	}

	/**
	 * @return the springGreeting
	 */
	public String getSpringGreeting() {
		return springGreeting;
	}

	public String greetServer(String input) throws IllegalArgumentException {
		LOG.debug("Received greeting from " + input);

		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			LOG.error("The name is invalid");
			throw new IllegalArgumentException( "Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br/><br/>" +
				"I am running " + serverInfo + ".<br/><br/>" +
				"It looks like you are using:<br/>" + userAgent + "<br/><br/>" +
				"Springs greets you: " + springGreeting;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 *
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}
