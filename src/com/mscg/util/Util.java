/**
 *
 */
package com.mscg.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;


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

	public static String combineStrings(String nonce, String key){
		return nonce + key;
	}
}
