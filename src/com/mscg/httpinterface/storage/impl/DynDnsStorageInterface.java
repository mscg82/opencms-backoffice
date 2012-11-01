/**
 *
 */
package com.mscg.httpinterface.storage.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import com.mscg.config.ConfigLoader;
import com.mscg.httpinterface.AbstractHttpInterface;
import com.mscg.httpinterface.storage.StorageInterface;
import com.mscg.util.Util;

/**
 * @author Giuseppe Miscione
 *
 */
public class DynDnsStorageInterface extends AbstractHttpInterface implements StorageInterface {

	//private static Logger log = Logger.getLogger(DynDnsStorageInterface.class);

	public DynDnsStorageInterface() throws ConfigurationException {
		super();
	}

	public void storeIP(String service, List<String> IPs) throws HttpException, IOException{
		String method = (String)ConfigLoader.getInstance().get(ConfigLoader.DYNDNS_STORAGE_METHOD);
		strUrl = prepareUrl(method);
		httpPost = null;

		try{
			httpPost = new PostMethod(strUrl);
			httpPost.addParameter("stage", "0");
			httpPost.addParameter("service", service);

			client.executeMethod(httpPost);
			if (httpPost.getStatusCode() == HttpStatus.SC_OK) {
	            String nonce = httpPost.getResponseBodyAsString();
	            httpPost.releaseConnection();

	            String confirm = Util.md5sum(Util.combineStrings(nonce, Util.SECRET_SHARED_KEY));

				httpPost = null;
				httpPost = new PostMethod(strUrl);
				httpPost.addParameter("stage", "1");
				httpPost.addParameter("confirm", confirm);

				for(String IP : IPs) {
					httpPost.addParameter("address", IP);
				}

				client.executeMethod(httpPost);
	        }
	        else {
	        	String failure = httpPost.getStatusLine().toString();
	        	throw new HttpException("Errore di comunicazione interno (" + failure + ").");
	        }
		} finally{
			if(httpPost != null){
				httpPost.releaseConnection();
				httpPost = null;
			}
		}
	}

}
