/**
 *
 */
package com.mscg.util.passwordreader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.mscg.util.Util;

/**
 * @author Giuseppe Miscione
 *
 */
public class CryptedPasswordReader implements PasswordReader {

	private static final int minimumKeySize = DESedeKeySpec.DES_EDE_KEY_LEN;

	private static Cipher cipher;
	private static SecretKey key;

	/**
	 * Use the specified TripleDES key to decrypt bytes ready from the input
	 * stream and write them to the output stream. This method uses uses Cipher
	 * directly to show how it can be done without CipherInputStream and
	 * CipherOutputStream.
	 */
	public static void decrypt(SecretKey key, InputStream in, OutputStream out)
			throws NoSuchAlgorithmException, InvalidKeyException, IOException,
			IllegalBlockSizeException, NoSuchPaddingException,
			BadPaddingException {
		// Create and initialize the decryption engine
		initCipher(Cipher.DECRYPT_MODE, key);

		// Create a special output stream to do the work for us
		CipherOutputStream cos = new CipherOutputStream(out, cipher);

		// Read bytes, decrypt, and write them out.
		byte[] buffer = new byte[2048];
		int bytesRead;
		while ((bytesRead = in.read(buffer)) != -1) {
			cos.write(buffer, 0, bytesRead);
		}

		// Write out the final bunch of decrypted bytes
		cos.close();
	}

	/**
	 * Use the specified TripleDES key to encrypt bytes from the input stream
	 * and write them to the output stream. This method uses CipherOutputStream
	 * to perform the encryption and write bytes at the same time.
	 */
	public static void encrypt(SecretKey key, InputStream in, OutputStream out)
			throws NoSuchAlgorithmException, InvalidKeyException,
			NoSuchPaddingException, IOException {
		// Create and initialize the encryption engine
		initCipher(Cipher.ENCRYPT_MODE, key);

		// Create a special output stream to do the work for us
		CipherOutputStream cos = new CipherOutputStream(out, cipher);

		// Read from the input and write to the encrypting output stream
		byte[] buffer = new byte[2048];
		int bytesRead;
		while ((bytesRead = in.read(buffer)) != -1) {
			cos.write(buffer, 0, bytesRead);
		}
		cos.close();

		// For extra security, don't leave any plaintext hanging around memory.
		java.util.Arrays.fill(buffer, (byte) 0);
	}

	/** Generate a secret TripleDES encryption/decryption key. */
	public static SecretKey generateKey(String userKey) throws NoSuchAlgorithmException,
			InvalidKeyException, InvalidKeySpecException {
		SecretKey key = null;
		if(userKey == null){
			// Get a key generator for Triple DES (a.k.a DESede)
			KeyGenerator keygen = KeyGenerator.getInstance("DESede");
			// Use it to generate a key
			key = keygen.generateKey();
		}
		else{
			// Generate a key from the string bytes
			byte keyBytes[] = userKey.getBytes();
			if(keyBytes.length < minimumKeySize){
				byte tmp[] = keyBytes;
				keyBytes = new byte[minimumKeySize];
				Arrays.fill(keyBytes, (byte)0);
				int length = tmp.length;
				for(int i = 0; i < length; i++)
					keyBytes[i] = tmp[i];
			}
			DESedeKeySpec keyspec = new DESedeKeySpec(keyBytes);
			SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DESede");
			key = keyfactory.generateSecret(keyspec);
		}
		return key;
	}

	public static Cipher initCipher(int opmode, Key key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		if(cipher == null)
			cipher = Cipher.getInstance("DESede");
		cipher.init(opmode, key);
		return cipher;
	}

	public CryptedPasswordReader() {
		if(cipher == null){
			try {
				cipher = Cipher.getInstance("DESede");
			} catch (Exception e) { }
		}

		if(key == null){
			try {
				key = generateKey(Util.SECRET_SHARED_KEY + "_passwordKey");
			} catch (Exception e) {}
		}
	}

	public String encodeString(String str) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IOException {
		String ret = null;

		InputStream is = new ByteArrayInputStream(str.getBytes());
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		encrypt(key, is, os);

		byte encBytes[] = os.toByteArray();
		byte b64Bytes[] = Base64.encodeBase64(encBytes);

		ret = new String(b64Bytes);

		return ret;
	}

	private String decodeString(String str) throws InvalidKeyException,
			NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException,
			BadPaddingException, IOException {
		String ret = null;

		byte encBytes[] = Base64.decodeBase64(str.getBytes());

		InputStream is = new ByteArrayInputStream(encBytes);
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		decrypt(key, is, os);

		byte decBytes[] = os.toByteArray();
		ret = new String(decBytes);

		return ret;
	}

	/* (non-Javadoc)
	 * @see com.mscg.util.passwordreader.PasswordReader#readPassword(java.lang.String)
	 */
	public String readPassword(String configString) {
		String pwd = configString;
		try{
			pwd = decodeString(configString);
		} catch(Exception e){}
		return pwd;
	}

}
