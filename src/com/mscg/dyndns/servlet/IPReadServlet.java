/**
 *
 */
package com.mscg.dyndns.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.mscg.dyndns.dnsfactory.DnsFactory;
import com.mscg.util.Util;

/**
 * @author Giuseppe Miscione
 *
 */
public class IPReadServlet extends HttpServlet {

	public static class Parameters{
		public static final String serviceParam = "service";
		public static final String protocolParam = "proto";
		public static final String applicationParam = "appl";

		public String getApplicationParam() {
			return applicationParam;
		}

		public String getProtocolParam() {
			return protocolParam;
		}

		public String getServiceParam() {
			return serviceParam;
		}
	}

	private static final long serialVersionUID = 5927116102926338955L;

	private static Logger log = Logger.getLogger(IPReadServlet.class);

	private HttpServletRequest  request;
	private HttpServletResponse response;
	private PageContext         pageContext;

	public IPReadServlet() {
		this(null, null, null);
	}

	public IPReadServlet(PageContext pageContext, HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.pageContext = pageContext;
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try{
			resp.setContentType("text/html");

			Collection<String> urls = getUrls(req);

			StringBuffer html = new StringBuffer(
				"<html>\n" +
				"    <head><title>MscG's Dynamic DNS</title></head>\n" +
				"    <body>\n" +
				"        Service <b>" + getService(req) + ":</b>\n" +
				"        <ul>\n");
			for(String completeUrl : urls){
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

	/**
	 * @return the pageContext
	 */
	public PageContext getPageContext() {
		return pageContext;
	}

	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	public String getService() {
		return getService(request);
	}

	public String getService(HttpServletRequest req) {
		return req.getParameter(Parameters.serviceParam);
	}

	public Collection<String> getUrls() {
		return getUrls(request);
	}

	public Collection<String> getUrls(HttpServletRequest req) {
		String service = getService(req);
		if(service == null)
			throw new NullPointerException("Invalid service required.");

		String protocol = req.getParameter(Parameters.protocolParam);
		if(protocol == null)
			protocol = "http";

		String appl = req.getParameter(Parameters.applicationParam);
		if(appl == null)
			appl = "";

		Util.accessLog.info(
			"Reading IPs for service \"" + service + "\". Request came from " + req.getRemoteAddr());
		log.debug("Retrieving IP for service \"" + service + "\"");
		Collection<String> IPs = DnsFactory.getProvider().getIPs(service);
		log.debug("Retrieved IPs: \"" + IPs + "\"");
		if(IPs == null)
			throw new NullPointerException("No IP found for requested service.");

		Collection<String> urls = new LinkedList<String>();
		for(String IP :IPs){
			String completeUrl = protocol + "://" + IP + "/" + appl + "/";
			log.debug("Complete URL: \"" + completeUrl + "\"");
			urls.add(completeUrl);
		}
		return urls;
	}

}
