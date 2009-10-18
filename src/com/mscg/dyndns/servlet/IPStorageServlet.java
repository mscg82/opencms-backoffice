/**
 *
 */
package com.mscg.dyndns.servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.mscg.dyndns.dnsfactory.DnsFactory;
import com.mscg.dyndns.dnsfactory.DnsProvider;
import com.mscg.util.Util;

/**
 * @author Giuseppe Miscione
 *
 */
public class IPStorageServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(IPStorageServlet.class);

	private static final String ADDRESS_PARAM = "address";
	private static final String CONFIRM_PARAM = "confirm";
	private static final String NONCE_PARAM = "nonce";
	private static final String SERVICE_PARAM = "service";
	private static final String STAGE_PARAM = "stage";

	private static final long serialVersionUID = -6825274892417341943L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try{
			HttpSession session = req.getSession(true);
			int stage = 0;
			try{
				stage = Integer.parseInt(req.getParameter(STAGE_PARAM));
			} catch(NumberFormatException e){ /* Bad numeric format, usgin default*/}
			log.debug("Stage of storage: " + stage);

			String nonce = (String)session.getAttribute(NONCE_PARAM);
			if(nonce == null){
				// restart from first stage
				log.debug("No nonce found in session, restarting protocol.");
				stage = 0;
			}
			String service = null;
			String confirm = null;

			switch(stage){
			case 0:
				service = req.getParameter(SERVICE_PARAM);
				if(service == null || service.trim().equals("")){
					throw new ServletException("Invalid service name.");
				}
				nonce = Util.md5sum(Long.toString(new Date().getTime()));

				log.debug("Nonce generated: " + nonce + "; service required: " + service);

				session.setAttribute(NONCE_PARAM, nonce);
				session.setAttribute(SERVICE_PARAM, service);
				resp.setContentType("text/plain");
				resp.getOutputStream().print(nonce);
				break;
			case 1:
				confirm = req.getParameter(CONFIRM_PARAM);
				service = (String)session.getAttribute(SERVICE_PARAM);
				if(service == null || service.trim().equals("")){
					throw new ServletException("Invalid service name.");
				}
				if(confirm == null || !confirm.equals(Util.md5sum(Util.combineStrings(nonce, Util.SECRET_SHARED_KEY)))){
					throw new ServletException("Invalid confirmation string.");
				}
				else{
					DnsProvider provider = DnsFactory.getProvider();
					provider.clearService(service);
					String IPs[] = req.getParameterValues(ADDRESS_PARAM);
					for(String IP : IPs){
						log.debug("Storing IP \"" + IP + "\" for service \"" + service + "\".");
						provider.addIP(service, IP);
					}
					resp.setContentType("text/plain");
					resp.getOutputStream().print("OK");
				}
				break;
			default:
				// invalid state
				throw new ServletException("Invalid state in IP storage.");
			}
		} catch(ServletException e){
			log.error("(" + e.getClass() + "): " + e.getMessage());
			Util.logStackTrace(e, log);
			throw e;
		} catch(IOException e){
			log.error("(" + e.getClass() + "): " + e.getMessage());
			Util.logStackTrace(e, log);
			throw e;
		} catch(Exception e){
			log.error("(" + e.getClass() + "): " + e.getMessage());
			Util.logStackTrace(e, log);
			throw new ServletException(e);
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
