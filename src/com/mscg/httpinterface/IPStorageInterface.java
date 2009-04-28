/**
 * 
 */
package com.mscg.httpinterface;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import com.mscg.config.ConfigLoader;
import com.mscg.util.Util;

/**
 * @author Giuseppe Miscione
 *
 */
public class IPStorageInterface extends AbstractHttpInterface {

	//private static Logger log = Logger.getLogger(IPStorageInterface.class);
	
	public IPStorageInterface() throws ConfigurationException {
		super();
	}
	
	public void storeIP(String service) throws HttpException, IOException{
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
