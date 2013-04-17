/**
 *
 */
package com.mscg.httpinterface;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.HttpException;

/**
 * @author Giuseppe Miscione
 *
 */
public class MyIpAddressInterface extends AbstractIpAddressInterface {

	private String myIpAddressUrl;

	public MyIpAddressInterface() throws ConfigurationException {
		super();
		myIpAddressUrl = (String)config.get("dyndns.myipaddress.url");
	}

	public String getRetrievedIpPageContent() throws HttpException, IOException {
		return getRetrievedIpPageContent(myIpAddressUrl);
	}

}
