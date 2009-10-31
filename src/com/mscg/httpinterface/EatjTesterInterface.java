/**
 *
 */
package com.mscg.httpinterface;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import com.mscg.config.ConfigLoader;

/**
 * @author Giuseppe Miscione
 *
 */
public class EatjTesterInterface extends AbstractHttpInterface {

	private String testUrl;
	private String userAgent;

	public EatjTesterInterface() throws ConfigurationException {
		super();
		userAgent = (String) config.get(ConfigLoader.DYNDNS_EATJ_USERAGENT);
		testUrl = (String) config.get(ConfigLoader.DYNDNS_EATJ_TESTURL);
	}

	public boolean testIfServerIsRunning() throws HttpException, IOException{
		boolean ret = false;
		strUrl = prepareUrl(testUrl);
		httpGet = null;
		try{
			httpGet = new GetMethod(strUrl);
			if(userAgent != null && userAgent.trim().length() != 0)
				httpGet.setRequestHeader("User-Agent", userAgent);
			client.executeMethod(httpGet);
			// server is running if the http status code is 200/OK and no redirect has been made
			if (httpGet.getStatusCode() == HttpStatus.SC_OK && httpGet.getPath().endsWith(testUrl)) {
				ret = true;
			}
		} finally{
			if(httpGet != null){
				httpGet.releaseConnection();
				httpGet = null;
			}
		}
		return ret;
	}

}
