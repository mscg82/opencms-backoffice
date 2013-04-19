/**
 *
 */
package com.mscg.httpinterface;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.apache.log4j.Logger;

import com.mscg.config.ConfigLoader;

/**
 * @author Giuseppe Miscione
 *
 */
public abstract class AbstractHttpInterface {
	private static final Logger log = Logger.getLogger(AbstractHttpInterface.class);

	private static final int CONN_TIMEOUT = 5000; // in milliseconds
	private static final int INTERVAL_TIMEOUT = 30000; // in milliseconds

	protected String strUrl = null;
	protected HttpClient client = null;
	protected PostMethod httpPost = null;
	protected GetMethod httpGet = null;
	protected Map<String, Object> config = null;

	protected static IdleConnectionTimeoutThread connManagerThread = null;
	protected static MultiThreadedHttpConnectionManager connectionManager = null;
	protected static Integer mutex = new Integer(0);

	public AbstractHttpInterface() throws ConfigurationException {
		config = ConfigLoader.getInstance();

		synchronized(mutex){
			if(connManagerThread == null){
				connManagerThread = new IdleConnectionTimeoutThread();
				connManagerThread.setConnectionTimeout(CONN_TIMEOUT);
				connManagerThread.setTimeoutInterval(INTERVAL_TIMEOUT);
				connManagerThread.start();
			}

			if(connectionManager == null){
				connectionManager =	new MultiThreadedHttpConnectionManager();
				HttpConnectionManagerParams params = new HttpConnectionManagerParams();

				HostConfiguration hostConf1 = new HostConfiguration();
				hostConf1.setHost((String) config.get(ConfigLoader.DYNDNS_SERVER), 80, (String) config.get(ConfigLoader.DYNDNS_PROTOCOL));
				params.setMaxConnectionsPerHost(hostConf1, 50);

				params.setMaxTotalConnections(50);

				connectionManager.setParams(params);

				connManagerThread.addConnectionManager(connectionManager);
			}

		}

		renewClient();
	}

	protected String prepareUrl(String method) {
	    return prepareUrl(method, null);
	}

	protected String prepareUrl(String method, Map<String, String> parameters) {
		StringBuilder builder = new StringBuilder();
		builder.append( (String) config.get(ConfigLoader.DYNDNS_PROTOCOL) );
		builder.append("://");
		builder.append( (String) config.get(ConfigLoader.DYNDNS_SERVER) );
		String port = (String) config.get(ConfigLoader.DYNDNS_PORT);
		if(port != null && port.trim().length() != 0){
			builder.append(":" + port);
		}
		builder.append("/");

		String context = (String) config.get(ConfigLoader.DYNDNS_CONTEXT);
		if(context != null && context.trim().length() != 0) {
			builder.append(context).append("/");
		}

		builder.append(method);

		if(parameters != null) {
		    builder.append("?");
		    boolean first = true;
		    for(Map.Entry<String, String> entry : parameters.entrySet()) {
		        if(!first)
		            builder.append("&");
		        try {
                    builder.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    builder.append(entry.getKey()).append("=").append(entry.getValue());
                }
		        first = false;
		    }
		}

		return builder.toString();
	}

	protected void renewClient() {
		// Get HTTP client instance
	    client = new HttpClient(connectionManager);
	    //establish a connection within 5 seconds
	    client.getHttpConnectionManager().getParams().setConnectionTimeout(CONN_TIMEOUT);
	    if(connManagerThread.isAlive()){
	    	log.trace("Connection manager thread alive, adding connection manager: " + client.getHttpConnectionManager().toString());
	    }
	    else{
	    	log.trace("Connection manager thread dead.");
	    }
	}
}
