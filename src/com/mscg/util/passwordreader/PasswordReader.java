/**
 *
 */
package com.mscg.util.passwordreader;

/**
 * @author Giuseppe Miscione
 *
 */
public interface PasswordReader {

	/**
	 * Returns the password to login in eatj control panel,
	 * interpreting the string read in the configuration file.
	 *
	 * @param configString The string read in the configuration file.
	 * @return The password interpreted from the provided string.
	 */
	public String readPassword(String configString);
}
