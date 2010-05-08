package com.mscg.httpinterface;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.mscg.config.ConfigLoader;

public class FreeHostingJavaTesterInterface extends AbstractHttpInterface implements TesterInterface {

	private static Logger LOG = Logger.getLogger(FreeHostingJavaTesterInterface.class);

	private String testUrl;

	public FreeHostingJavaTesterInterface() throws ConfigurationException, ClassCastException, IOException {
		super();
		testUrl = (String) config.get(ConfigLoader.FHJ_TEST_URL);
	}

	public boolean startServer() throws HttpException, IOException {
		return false;
	}

	public boolean testIfServerIsRunning() throws HttpException, IOException {
		boolean ret = false;
		strUrl = prepareUrl(testUrl);
		LOG.debug("Testing connection on url: " + strUrl);
		httpGet = null;
		try{
			httpGet = new GetMethod(strUrl);
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
