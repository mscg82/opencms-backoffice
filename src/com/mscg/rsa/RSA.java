/**
 *
 */
package com.mscg.rsa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.mscg.tripledes.TripleDES;

/**
 * @author Giuseppe Miscione
 *
 */
public class RSA implements Serializable{

	private static final long serialVersionUID = -8090624451350275835L;

	private KeyPair keyPair = null;

	private static Cipher rsaCipher = null;

	private static String keyCryptPassphrase = new String("24[ri342 T09j#rgèmNUnnf0 9/%^ 2e1293er23ù§£\"");

	static{
		initRsaCipher();
	}

	private static void initRsaCipher(){
		try {
			// initialize the cipher object
			rsaCipher = Cipher.getInstance("RSA");
		} catch (Exception e) {
			// An exception here probably means the JCE provider hasn't
			// been permanently installed on this system by listing it
			// in the $JAVA_HOME/jre/lib/security/java.security file.
			// Therefore, we have to install the JCE provider explicitly.
			Provider sunjce = new com.sun.crypto.provider.SunJCE();
			Security.addProvider(sunjce);

			try {
				// retry to initialize the cipher
				rsaCipher = Cipher.getInstance("RSA");
			} catch (Exception e2) {

			}
		}
	}

	public RSA(){
		setKeyPair(new KeyPair(null, null));
	}

	public RSA(int bits) throws NoSuchAlgorithmException, NoSuchProviderException{
		this(bits, null);
	}

	public RSA(int bits, byte seed[]) throws NoSuchAlgorithmException, NoSuchProviderException{
		generateKeyPair(bits, seed);
	}

	public boolean canDecrypt(){
		return getPrivateKey() != null;
	}

	public boolean canEncrypt(){
		return getPublicKey() != null;
	}

	public byte[] decrypt(byte crypted[]) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		if(!canDecrypt())
			throw new InvalidKeyException("Private key not initialized.");
		// Initialize the cipher for decryption
		rsaCipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
		byte plain[] = null;
		plain = rsaCipher.doFinal(crypted);
		return plain;
	}

	public byte[] encrypt(byte plain[]) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		if(!canEncrypt())
			throw new InvalidKeyException("Public key not initialized.");
		// Initialize the cipher for encryption
		rsaCipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
		byte ciphertext[] = null;
		ciphertext = rsaCipher.doFinal(plain);
		return ciphertext;
	}

	public KeyPair generateKeyPair(int bits) throws NoSuchAlgorithmException, NoSuchProviderException{
		return generateKeyPair(bits, null);
	}

	public KeyPair generateKeyPair(int bits, byte seed[]) throws NoSuchAlgorithmException, NoSuchProviderException{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		if(seed != null)
			random.setSeed(seed);

		keyGen.initialize(bits, random);

		setKeyPair(keyGen.generateKeyPair());

		return keyPair;
	}

	public int getEncryptedBlockSize() throws InvalidKeyException{
		if(canEncrypt())
			rsaCipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
		else if(canDecrypt())
			rsaCipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
		else
			throw new InvalidKeyException("No key has been loaded and the cipher cannot be initialized.");
		return rsaCipher.getOutputSize(1);
	}

	public KeyPair getKeyPair(){
		return keyPair;
	}

	public int getMaxPlainTextBlockSize() throws InvalidKeyException{
		return getEncryptedBlockSize() - 11;
	}

	public PrivateKey getPrivateKey() {
		return keyPair.getPrivate();
	}

	public PublicKey getPublicKey() {
		return keyPair.getPublic();
	}

	public void readKeyPairFromFiles(File publicKeyFile, File privateKeyFile)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException{
		InputStream publicStream = null;
		InputStream privateStream = null;
		try{
			if(publicKeyFile != null)
				publicStream = new FileInputStream(publicKeyFile);
			if(privateKeyFile != null)
				privateStream = new FileInputStream(privateKeyFile);
			readKeyPairFromStreams(publicStream, privateStream);

		} finally{
			if(publicStream != null){
				try{
					publicStream.close();
				} catch (IOException e) { }
			}
			if(privateStream != null){
				try{
					privateStream.close();
				} catch (IOException e) { }
			}
		}
	}

	public void readKeyPairFromStreams(InputStream publicKeyStream, InputStream privateKeyStream)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException {
		PublicKey pubKey = null;
		PrivateKey prvKey = null;

		Cipher cipher = TripleDES.initCipher(Cipher.DECRYPT_MODE, TripleDES.generateKey(keyCryptPassphrase));

		if(publicKeyStream != null){
			ObjectInputStream ois = null;
			CipherInputStream cis = null;
			try{
				cis = new CipherInputStream(publicKeyStream, cipher);
				ois = new ObjectInputStream(cis);
				try {
					pubKey = (PublicKey)ois.readObject();
				} catch (ClassNotFoundException e) {

				}
			} finally{
				if(cis != null){
					try{
						cis.close();
					} catch (IOException e) { }
				}
				if(ois != null){
					try{
						ois.close();
					} catch (IOException e) { }
				}
			}
		}

		if(privateKeyStream != null){
			ObjectInputStream ois = null;
			CipherInputStream cis = null;
			try{
				cis = new CipherInputStream(privateKeyStream, cipher);
				ois = new ObjectInputStream(cis);
				try {
					prvKey = (PrivateKey)ois.readObject();
				} catch (ClassNotFoundException e) {

				}
			} finally{
				if(cis != null){
					try{
						cis.close();
					} catch (IOException e) { }
				}
				if(ois != null){
					try{
						ois.close();
					} catch (IOException e) { }
				}
			}
		}

		if(pubKey == null){
			pubKey = keyPair.getPublic();
		}
		if(prvKey == null){
			prvKey = keyPair.getPrivate();
		}

		setKeyPair(new KeyPair(pubKey, prvKey));

	}

	public void readPrivateKeyFromFile(File privateKeyFile)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException{
		readKeyPairFromFiles(null, privateKeyFile);
	}

	public void readPrivateKeyFromStream(InputStream privateKeyStream)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException{
		readKeyPairFromStreams(null, privateKeyStream);
	}

	public void readPublicKeyFromFile(File publicKeyFile)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException{
		readKeyPairFromFiles(publicKeyFile, null);
	}

	public void readPublicKeyFromStream(InputStream publicKeyStream)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException{
		readKeyPairFromStreams(publicKeyStream, null);
	}

	public void setKeyPair(KeyPair keyPair){
		this.keyPair = keyPair;
	}

	public byte[] sign(byte plain[]) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		if(!canDecrypt())
			throw new InvalidKeyException("Private key not initialized.");
		// Initialize the cipher for encryption
		rsaCipher.init(Cipher.ENCRYPT_MODE, getPrivateKey());
		byte signed[] = null;
		signed = rsaCipher.doFinal(plain);
		return signed;
	}

	public byte[] unsign(byte signed[]) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		if(!canEncrypt())
			throw new InvalidKeyException("Public key not initialized.");
		// Initialize the cipher for decryption
		rsaCipher.init(Cipher.DECRYPT_MODE, getPublicKey());
		byte plain[] = null;
		plain = rsaCipher.doFinal(signed);
		return plain;
	}

	public void writeKeyPairToFiles(File publicKeyFile, File privateKeyFile)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException{
		OutputStream publicStream = null;
		OutputStream privateStream = null;
		try{
			if(publicKeyFile != null)
				publicStream = new FileOutputStream(publicKeyFile);
			if(privateKeyFile != null)
				privateStream = new FileOutputStream(privateKeyFile);
			writeKeyPairToStreams(publicStream, privateStream);

		} finally{
			if(publicStream != null){
				try{
					publicStream.flush();
				} catch (IOException e) { }
				try{
					publicStream.close();
				} catch (IOException e) { }
			}
			if(privateStream != null){
				try{
					privateStream.flush();
				} catch (IOException e) { }
				try{
					privateStream.close();
				} catch (IOException e) { }
			}
		}
	}

	public void writeKeyPairToStreams(OutputStream publicKeyStream, OutputStream privateKeyStream)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException{
		Cipher cipher = TripleDES.initCipher(Cipher.ENCRYPT_MODE, TripleDES.generateKey(keyCryptPassphrase));
		if(publicKeyStream != null){
			ObjectOutputStream oos = null;
			CipherOutputStream cos = null;
			try{
				cos = new CipherOutputStream(publicKeyStream, cipher);
				oos = new ObjectOutputStream(cos);
				oos.writeObject(getPublicKey());

			} finally{
				if(cos != null){
					try{
						cos.close();
					} catch(IOException e){ }
				}
				if(oos != null){
					try{
						oos.close();
					} catch(IOException e){ }
				}
			}
		}
		if(privateKeyStream != null){
			ObjectOutputStream oos = null;
			CipherOutputStream cos = null;
			try{
				cos = new CipherOutputStream(privateKeyStream, cipher);
				oos = new ObjectOutputStream(cos);
				oos.writeObject(getPrivateKey());

			} finally{
				if(cos != null){
					try{
						cos.close();
					} catch(IOException e){ }
				}
				if(oos != null){
					try{
						oos.close();
					} catch(IOException e){ }
				}
			}
		}
	}

	public void writePrivateKeyToFile(File privateKeyFile)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException{
		writeKeyPairToFiles(null, privateKeyFile);
	}

	public void writePrivateKeyToStream(OutputStream privateKeyStream)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException{
		writeKeyPairToStreams(null, privateKeyStream);
	}

	public void writePublicKeyToFile(File publicKeyFile)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException{
		writeKeyPairToFiles(publicKeyFile, null);
	}

	public void writePublicKeyToStream(OutputStream publicKeyStream)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeySpecException{
		writeKeyPairToStreams(publicKeyStream, null);
	}

}
