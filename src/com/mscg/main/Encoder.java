/**
 *
 */
package com.mscg.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Provider;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import com.mscg.tripledes.TripleDES;

/**
 * @author Giuseppe Miscione
 *
 */
public class Encoder {
	private static final String passPhrasePrefix = "--passphrase:";

	/**
	 * The program. The first argument must be -e, -d, or -g to encrypt,
	 * decrypt, or generate a key. The second argument is the name of a file
	 * from which the key is read or to which it is written for -g. The -e and
	 * -d arguments cause the program to read from standard input and encrypt or
	 * decrypt to standard output.
	 */
	public static void main(String[] args) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			// Check to see whether there is a provider that can do TripleDES
			// encryption. If not, explicitly install the SunJCE provider.
			try {
				/*Cipher c = */Cipher.getInstance("DESede");
			} catch (Exception e) {
				// An exception here probably means the JCE provider hasn't
				// been permanently installed on this system by listing it
				// in the $JAVA_HOME/jre/lib/security/java.security file.
				// Therefore, we have to install the JCE provider explicitly.
				System.err.println("Installing SunJCE provider.");
				Provider sunjce = new com.sun.crypto.provider.SunJCE();
				Security.addProvider(sunjce);
			}

			File keyfile = null;
			File infile = null;
			File outfile = null;
			String userKey = null;

			String action = args[0];
			if(action.equals("-g")){
				keyfile = new File(args[1]);
				if(!keyfile.exists())
					keyfile.createNewFile();

				if(args.length >= 3){
					userKey = readUserkey(args[2]);
					if(userKey == null)
						throw new NullPointerException("Incorrect passphrase parameter syntax.");
				}
			}
			else if(action.equals("-e") || action.equals("-d")){
				if(args.length < 4)
					throw new NullPointerException("Incorrect parameters syntax.");
				userKey = readUserkey(args[1]);
				if(userKey == null){
					keyfile = new File(args[1]);
					if(!keyfile.exists())
						throw new FileNotFoundException("Unable to key file \"" + keyfile.getAbsolutePath() + "\"");
				}

				infile = new File(args[2]);
				if(!infile.exists())
					throw new FileNotFoundException("Unable to find input file \"" + infile.getAbsolutePath() + "\"");
				fis = new FileInputStream(infile);

				outfile = new File(args[3]);
				if(!outfile.exists())
					outfile.createNewFile();
				fos = new FileOutputStream(outfile);
			}
			else{
				throw new NullPointerException("Unknown action specified (" + action + ").");
			}

			// Now check the first arg to see what we're going to do
			if (action.equals("-g")) { // Generate a key
				System.out.print("Generating key. This may take some time...");
				System.out.flush();
				SecretKey key = TripleDES.generateKey(userKey);
				TripleDES.writeKey(key, keyfile);
				System.out.println("done.");
				System.out.println("Secret key written to \"" + keyfile.getAbsolutePath() + "\". " +
					"Protect that file carefully!");
			} else if (action.equals("-e")) { // Encrypt
				SecretKey key = null;
				if(keyfile != null)
					key = TripleDES.readKey(keyfile);
				else
					key = TripleDES.generateKey(userKey);
				TripleDES.encrypt(key, fis, fos);
				System.out.println("File " + args[2] + " encrypted succesfully in \"" + outfile.getAbsolutePath() + "\"!");
			} else if (action.equals("-d")) { // Decrypt
				SecretKey key = null;
				if(keyfile != null)
					key = TripleDES.readKey(keyfile);
				else
					key = TripleDES.generateKey(userKey);
				TripleDES.decrypt(key, fis, fos);
				System.out.println("File " + args[2] + " decrypted succesfully in \"" + outfile.getAbsolutePath() + "\"!");
			}
		} catch(FileNotFoundException e){
			System.err.println(e.getClass() + ": " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass() + ": " + e.getMessage());
			System.err.println(
				"Usage for key generation:\n" +
				"    java " + Encoder.class.getName() + " -g <keyfile> [" + passPhrasePrefix + "<userkey>]\n" +
				"Usage for file encrypting:\n" +
				"    java " + Encoder.class.getName() + " -e <keyfile>|" + passPhrasePrefix + "<userkey> <infile> <outfile>\n" +
				"Usage for file decrypting:\n" +
				"    java " + Encoder.class.getName() + " -d <keyfile>|" + passPhrasePrefix + "<userkey> <infile> <outfile>");
		} finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) { }
			}
			if(fos != null){
				try {
					fos.flush();
				} catch (IOException e) { }
				try {
					fos.close();
				} catch (IOException e) { }
			}
		}
	}

	private static String readUserkey(String parameter){
		String ret = null;
		int index = parameter.indexOf(passPhrasePrefix);
		if(index != -1)
			ret = parameter.substring(index + passPhrasePrefix.length());
		return ret;
	}
}
