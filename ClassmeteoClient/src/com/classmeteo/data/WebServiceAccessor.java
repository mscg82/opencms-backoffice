/**
 * 
 */
package com.classmeteo.data;

import com.classmeteo.ws.ClassMeteoFluxesWS;
import com.classmeteo.ws.ClassMeteoFluxesWS_Stub;

/**
 * @author Giuseppe Miscione
 *
 */
public class WebServiceAccessor {
	
	private static final String SERVER_NAME = "classmeteo.weather.com";//"weather08-dev.mashfrog.com";
	
	private WebServiceAccessor() {
		
	}
	
	public static synchronized ClassMeteoFluxesWS getClient() {
		return new ClassMeteoFluxesWS_Stub(SERVER_NAME);
	}
}
