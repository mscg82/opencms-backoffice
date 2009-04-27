/**
 *
 */
package com.mscg.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;

import com.mscg.rsa.RSA;

/**
 * @author Giuseppe Miscione
 *
 */
public class RSAEncoder {

	private static String getUsageString(){
		return
			"Usage for key generation:\n" +
			"    RSAencoder -g <bits> <publickeyfile> <privatekeyfile>\n" +
			"Usage for file encrypting:\n" +
			"    RSAencoder -e <publickeyfile> <infile> <outfile>\n" +
			"Usage for file decrypting:\n" +
			"    RSAencoder -d <privatekeyfile> <infile> <outfile>\n" +
			"Usage for file signing:\n" +
			"    RSAencoder -s <privatekeyfile> <infile> <outfile>\n" +
			"Usage for file sign check:\n" +
			"    RSAencoder -c <publickeyfile> <origfile> <signfile>";
	}

	private static String printPercentage(double percentage){
		DecimalFormat format = new DecimalFormat("000%");
		return "\b\b\b\b" + format.format(percentage);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RSA rsa = null;
		int bufferSize = 0;

		System.out.println();

		try {
			String action = args[0];
			File publicKeyFile = null;
			File privateKeyFile = null;


			if(action.equals("-h")){
				System.out.println(getUsageString());
			}
			else if(action.equals("-g")){
				if(args.length < 4)
					throw new NullPointerException("Incorrect parameters syntax.");
				int bits = Integer.parseInt(args[1]);
				publicKeyFile = new File(args[2]);
				if(!publicKeyFile.exists())
					publicKeyFile.createNewFile();
				privateKeyFile = new File(args[3]);
				if(!privateKeyFile.exists())
					privateKeyFile.createNewFile();

				System.out.print("Enter some random characters to generate the keys: ");
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				String seed = reader.readLine();

				rsa = new RSA(bits, seed.getBytes());

				rsa.writeKeyPairToFiles(publicKeyFile, privateKeyFile);

				System.out.println(
					"\n" + bits + " bits long keys generated in files \"" + publicKeyFile.getAbsolutePath() + "\" " +
					"and \"" + privateKeyFile.getAbsolutePath() + "\".\n\n" +
					"Protect \"" + privateKeyFile.getAbsolutePath() + "\" CAREFULLY as it's your private secret key!");
			}
			else if(action.equals("-e")){
				if(args.length < 4)
					throw new NullPointerException("Incorrect parameters syntax.");
				publicKeyFile = new File(args[1]);
				if(!publicKeyFile.exists())
					throw new FileNotFoundException("Unable to find public key file \"" + publicKeyFile.getAbsolutePath() + "\".");

				File inputFile = new File(args[2]);
				if(!inputFile.exists())
					throw new FileNotFoundException("Unable to find input file \"" + inputFile.getAbsolutePath() + "\".");
				long inputFileLength = inputFile.length();

				File outputFile = new File(args[3]);
				if(!outputFile.exists())
					outputFile.createNewFile();

				InputStream is = null;
				OutputStream os = null;
				try{
					rsa = new RSA();
					rsa.readPublicKeyFromFile(publicKeyFile);

					is = new FileInputStream(inputFile);
					os = new FileOutputStream(outputFile);

					bufferSize = rsa.getMaxPlainTextBlockSize();

					System.out.print("Encrypting... 000%");

					byte buffer[] = (byte[])Array.newInstance(byte.class, bufferSize);
					int byteRead = 0;
					long totalBytes = 0;
					while(byteRead >= 0){
						byteRead = is.read(buffer);
						if(byteRead == -1)
							break;
						totalBytes += byteRead;
						if(byteRead < bufferSize){
							byte tmp[] = buffer;
							buffer = (byte[])Array.newInstance(byte.class, byteRead);
							for(int i = 0; i < byteRead; i++)
								buffer[i] = tmp[i];
						}
						byte enc[] = rsa.encrypt(buffer);
						System.out.print(printPercentage((double)totalBytes / inputFileLength));
						os.write(enc);
					}

					System.out.println(
						"\n\nFile \"" + inputFile.getAbsolutePath() + "\" encrypted in " +
						"\"" + outputFile.getAbsolutePath() + "\".");

				} finally{
					if(is != null){
						try{
							is.close();
						} catch(IOException e){ }
					}
					if(os != null){
						try{
							os.flush();
						} catch(IOException e){ }
						try{
							os.close();
						} catch(IOException e){ }
					}
				}
			}
			else if(action.equals("-d")){
				if(args.length < 4)
					throw new NullPointerException("Incorrect parameters syntax.");
				privateKeyFile = new File(args[1]);
				if(!privateKeyFile.exists())
					throw new FileNotFoundException("Unable to find private key file \"" + privateKeyFile.getAbsolutePath() + "\".");

				File inputFile = new File(args[2]);
				if(!inputFile.exists())
					throw new FileNotFoundException("Unable to find input file \"" + inputFile.getAbsolutePath() + "\".");
				long inputFileLength = inputFile.length();

				File outputFile = new File(args[3]);
				if(!outputFile.exists())
					outputFile.createNewFile();

				InputStream is = null;
				OutputStream os = null;
				try{
					rsa = new RSA();
					rsa.readPrivateKeyFromFile(privateKeyFile);

					is = new FileInputStream(inputFile);
					os = new FileOutputStream(outputFile);

					// detect decBufferSize
					bufferSize = rsa.getEncryptedBlockSize();

					System.out.print("Decrypting... 000%");

					byte buffer[] = (byte[])Array.newInstance(byte.class, bufferSize);
					int byteRead = 0;
					long totalBytes = 0;
					while(byteRead >= 0){
						byteRead = is.read(buffer);
						if(byteRead == -1)
							break;
						totalBytes += byteRead;
						byte dec[] = rsa.decrypt(buffer);
						System.out.print(printPercentage((double)totalBytes / inputFileLength));
						os.write(dec);
					}

					System.out.println(
							"\n\nFile \"" + inputFile.getAbsolutePath() + "\" decrypted in " +
							"\"" + outputFile.getAbsolutePath() + "\".");

				} finally{
					if(is != null){
						try{
							is.close();
						} catch(IOException e){ }
					}
					if(os != null){
						try{
							os.flush();
						} catch(IOException e){ }
						try{
							os.close();
						} catch(IOException e){ }
					}
				}
			}
			else if(action.equals("-s")){
				if(args.length < 4)
					throw new NullPointerException("Incorrect parameters syntax.");
				privateKeyFile = new File(args[1]);
				if(!privateKeyFile.exists())
					throw new FileNotFoundException("Unable to find private key file \"" + privateKeyFile.getAbsolutePath() + "\".");

				File inputFile = new File(args[2]);
				if(!inputFile.exists())
					throw new FileNotFoundException("Unable to find input file \"" + inputFile.getAbsolutePath() + "\".");
				long inputFileLength = inputFile.length();

				File outputFile = new File(args[3]);
				if(!outputFile.exists())
					outputFile.createNewFile();

				InputStream is = null;
				OutputStream os = null;
				try{
					rsa = new RSA();
					rsa.readPrivateKeyFromFile(privateKeyFile);

					is = new FileInputStream(inputFile);
					os = new FileOutputStream(outputFile);

					// detect decBufferSize
					bufferSize = rsa.getMaxPlainTextBlockSize();

					System.out.print("Signing... 000%");

					byte buffer[] = (byte[])Array.newInstance(byte.class, bufferSize);
					int byteRead = 0;
					long totalBytes = 0;
					while(byteRead >= 0){
						byteRead = is.read(buffer);
						if(byteRead == -1)
							break;
						totalBytes += byteRead;
						byte signed[] = rsa.sign(buffer);
						System.out.print(printPercentage((double)totalBytes / inputFileLength));
						os.write(signed);
					}

					System.out.println(
							"\n\nFile \"" + inputFile.getAbsolutePath() + "\" signed in " +
							"\"" + outputFile.getAbsolutePath() + "\".");

				} finally{
					if(is != null){
						try{
							is.close();
						} catch(IOException e){ }
					}
					if(os != null){
						try{
							os.flush();
						} catch(IOException e){ }
						try{
							os.close();
						} catch(IOException e){ }
					}
				}
			}
			else if(action.equals("-c")){
				if(args.length < 4)
					throw new NullPointerException("Incorrect parameters syntax.");
				publicKeyFile = new File(args[1]);
				if(!publicKeyFile.exists())
					throw new FileNotFoundException("Unable to find public key file \"" + publicKeyFile.getAbsolutePath() + "\".");

				File origFile = new File(args[2]);
				if(!origFile.exists())
					throw new FileNotFoundException("Unable to find original file \"" + origFile.getAbsolutePath() + "\".");

				File signFile = new File(args[3]);
				if(!signFile.exists())
					throw new FileNotFoundException("Unable to find sign file \"" + signFile.getAbsolutePath() + "\".");
				long inputFileLength = signFile.length();

				InputStream is1 = null;
				InputStream is2 = null;
				try{
					rsa = new RSA();
					rsa.readPublicKeyFromFile(publicKeyFile);

					is1 = new FileInputStream(origFile);
					is2 = new FileInputStream(signFile);

					// detect decBufferSize
					bufferSize = rsa.getEncryptedBlockSize();

					System.out.print("Checking sign... 000%");

					byte bufferSigned[] = (byte[])Array.newInstance(byte.class, bufferSize);
					byte bufferOrig[] = (byte[])Array.newInstance(byte.class, rsa.getMaxPlainTextBlockSize());
					int byteRead = 0;
					long totalBytes = 0;
					boolean checked = true;
					while(byteRead >= 0 && checked){
						byteRead = is2.read(bufferSigned);
						if(byteRead == -1)
							break;
						totalBytes += byteRead;
						byte unsigned[] = rsa.unsign(bufferSigned);
						System.out.print(printPercentage((double)totalBytes / inputFileLength));
						is1.read(bufferOrig);
						if(unsigned.length == bufferOrig.length){
							for(int i = 0; i < unsigned.length && checked; i++){
								if(unsigned[i] != bufferOrig[i])
									checked = false;
							}
						}
						else
							checked = false;
					}

					if(checked){
						System.out.println("\n\nSign for file \"" + origFile.getAbsolutePath() + "\" CHECKED.");
					}
					else{
						System.out.println("\n\nSign for file \"" + origFile.getAbsolutePath() + "\" NOT CHECKED.");
					}

				} finally{
					if(is1 != null){
						try{
							is1.close();
						} catch(IOException e){ }
					}
					if(is2 != null){
						try{
							is2.close();
						} catch(IOException e){ }
					}
				}
			}
			else{
				throw new NullPointerException("Unknown action specified (" + action + ").");
			}

		} catch (Exception e) {
			System.err.println("\n" + e.getClass() + ": " + e.getMessage() + "\n");
			System.err.println(getUsageString());
		}

	}

}
