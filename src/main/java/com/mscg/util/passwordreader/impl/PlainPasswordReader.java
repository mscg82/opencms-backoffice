/**
 *
 */
package com.mscg.util.passwordreader.impl;

import com.mscg.util.passwordreader.PasswordReader;

/**
 * @author Giuseppe Miscione
 *
 */
public class PlainPasswordReader implements PasswordReader {

	/**
	 * Returns the password to login in eatj control panel,
	 * interpreting the string read in the configuration file.
	 * In this implementation, the password read from the configuration
	 * file is treated as plain text, so the provided string is
	 * always returned unchanged.
	 *
	 * @param configString The string read in the configuration file.
	 * @return The password interpreted from the provided string. In
	 * this implementation, the provided string is returned unchanged.
	 */
	public String readPassword(String configString) {
		return configString;
	}

}
