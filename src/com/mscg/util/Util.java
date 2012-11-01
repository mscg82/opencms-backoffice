/**
 *
 */
package com.mscg.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import com.mscg.config.ConfigLoader;

/**
 * @author Giuseppe Miscione
 *
 */
public class Util {
	public static final String SECRET_SHARED_KEY = "jSdn32dfd369NCg<i3r89ri]jEKLofhj?$dkjsdf99";

	/**
	 * The access logger.
	 */
	public static Logger accessLog = Logger.getLogger("com.mscg.dyndns.access");

	public static String combineStrings(String nonce, String key){
		return nonce + key;
	}

	/**
	 * Returns a string set in the configuration file. If the string has semicolons, commons-config
	 * builds a <code>List&lt;String&gt;</code> rather than a simple string. This method check the
	 * type of <code>Object</code> in the configuration map and even returns a <code>String</code>.
	 *
	 * @param paramName The name of the configuration parameter that will be read.
	 * @return The <code>String</code> contained in the configuration file under the specified parameter.
	 * @throws ConfigurationException If the configuration file cannot be read.
	 */
	@SuppressWarnings("unchecked")
    public static String getConfigString(String paramName) throws ConfigurationException{
		StringBuffer message = new StringBuffer();
		try{
			ArrayList<String> words = (ArrayList<String>) ConfigLoader.getInstance().get(paramName);
			boolean first = true;
			for(String word : words){
				if(!first)
					message.append(", ");
				message.append(word);
				first = false;
			}
		} catch(NullPointerException e){
			return "";
		} catch(ClassCastException e){
			message.append((String) ConfigLoader.getInstance().get(paramName));
		}
		return message.toString();
	}

	/**
	 * Log the complete stack trace of the provided exception in the logger.
	 *
	 * @param e The Exception.
	 * @param log The log in which the stack trace will be printed.
	 */
	public static void logStackTrace(Exception e, Logger log){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(stream);
		e.printStackTrace(ps);
		log.debug("\n" + stream.toString());
		try {
			stream.close();
		} catch (IOException e1) {}
	}

	/**
	 * Lookup the provided location in the application context. Use this method to retrive
	 * datasources.
	 *
	 * @param location The location to lookup for.
	 * @return The object retrived.
	 * @throws NamingException If an error occurs.
	 */
	public static Object lookup(String location) throws NamingException {
        try {
            InitialContext context = new InitialContext();

            try {
                return context.lookup(location);
            } catch (NamingException e) {
                //ok, couldn't find it, look in env
                return context.lookup("java:comp/env/" + location);
            }
        } catch (NamingException e) {
            throw e;
        }
    }

	/**
	 * Calculates the MD5 checksum of the provided string.
	 *
	 * @param originalString The string whose MD5 checksum will be computed.
	 *
	 * @return the MD5 checksum of the provided string.
	 */
	public static String md5sum(String originalString) {
		byte[] defaultBytes = originalString.getBytes();
		try{
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();

			StringBuffer hexString = new StringBuffer();
			for(int i = 0; i < messageDigest.length; i++){
				int val = 0xFF & messageDigest[i];
				hexString.append((val <= 15 ? "0" : "") + Integer.toHexString(val));
			}
			return hexString.toString();
		}catch(NoSuchAlgorithmException e){
		    return null;
		}
	}
}
