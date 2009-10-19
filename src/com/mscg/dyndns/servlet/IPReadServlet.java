/**
 *
 */
package com.mscg.dyndns.servlet;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.mscg.dyndns.dnsfactory.DnsFactory;
import com.mscg.util.Util;

/**
 * @author Giuseppe Miscione
 *
 */
public class IPReadServlet extends HttpServlet {

	private static final long serialVersionUID = 5927116102926338955L;

	private static Logger log = Logger.getLogger(IPReadServlet.class);

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try{
			resp.setContentType("text/html");
			String service = req.getParameter("service");
			if(service == null)
				throw new NullPointerException("Invalid service required.");

			String protocol = req.getParameter("proto");
			if(protocol == null)
				protocol = "http";

			String appl = req.getParameter("appl");
			if(appl == null)
				appl = "";

			Util.accessLog.info(
				"Reading IPs for service \"" + service + "\". Request came from " + req.getRemoteAddr());
			log.debug("Retrieving IP for service \"" + service + "\"");
			Collection<String> IPs = DnsFactory.getProvider().getIPs(service);
			log.debug("Retrieved IPs: \"" + IPs + "\"");
			if(IPs == null)
				throw new NullPointerException("No IP found for requested service.");

			StringBuffer html = new StringBuffer(
				"<html>\n" +
				"    <head><title>MscG's Dynamic DNS</title></head>\n" +
				"    <body>\n" +
				"        Service <b>" + service + ":</b>\n" +
				"        <ul>\n");
			for(String IP : IPs){
				String completeUrl = protocol + "://" + IP + "/" + appl + "/";
				log.debug("Complete URL: \"" + completeUrl + "\"");
				html.append(
			    "            <li><a href=\"" + completeUrl + "\">" + completeUrl + "</a></li>\n");
			}
			html.append(
				"        </ul>\n" +
			    "    </body>\n" +
				"</html>");
			resp.getWriter().print(html);
			resp.getWriter().flush();

		} catch(Exception e){
			log.error("Error found while retrieving IP (" + e.getClass() + "): " + e.getMessage());
			Util.logStackTrace(e, log);
			throw new ServletException(e);
		}

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
