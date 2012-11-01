/**
 *
 */
package com.mscg.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.mscg.config.ConfigLoader;


/**
 * @author Giuseppe Miscione
 *
 */
public class Util {
	public static final String SECRET_SHARED_KEY = "jSdn32dfd369NCg<i3r89ri]jEKLofhj?$dkjsdf99";

	public static String combineStrings(String nonce, String key){
		return nonce + key;
	}

	/**
	 * Copies the data from the origin {@link InputStream} to the provided {@link OutputStream}
	 * using the specified buffer size.
	 *
	 * @param orig The origin {@link InputStream} from which data are read.
	 * @param dest The destination {@link OutputStream} to which data are written.
	 * @param bufferSize The size of the buffer used in copy.
	 * @throws IOException If an error occurs during data transfer.
	 */
	public static void copyData(InputStream orig, OutputStream dest, int bufferSize) throws IOException{
		byte buffer[] = new byte[bufferSize];
		int byteRead = 0;
		do {
			byteRead = orig.read(buffer);
			if(byteRead > 0)
				dest.write(buffer, 0, byteRead);
		} while(byteRead > 0);
		dest.flush();
	}

	public static void initApplication() throws ConfigurationException{
		URL log4jUrl = Util.class.getResource("/log4j.properties");
		PropertyConfigurator.configure(log4jUrl);
		ConfigLoader.initInstance();
	}

	/**
	 * Creates an instance of the provided class name. The class object is
	 * searched using, as first try, the provided class loader (if its not
	 * <code>null</code>), then the class loader that loaded this class
	 * and, as last try, the system class loader. If the class object
	 * cannot be loaded, or the default constructor cannot be invoked, the
	 * method returns <code>null</code>.
	 *
	 * @param className The name of the class whose instance will be created.
	 * @param firstClassLoader The class loader that will be used as first try.
	 * @return An instance of the provided class or <code>null</code> if an
	 * error occurs during class lookup or object instance creation.
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
    public static Object loadClass(String className, ClassLoader firstClassLoader) {
		Object ret = null;
		Class clazz = null;

		if(firstClassLoader != null) {
			try {
				clazz = firstClassLoader.loadClass(className);
			} catch (ClassNotFoundException e) { }
		}

		if(clazz == null) {
			try {
				clazz = Util.class.getClassLoader().loadClass(className);
			} catch (ClassNotFoundException e) { }
		}

		if(clazz == null) {
			try {
				clazz = ClassLoader.getSystemClassLoader().loadClass(className);
			} catch (ClassNotFoundException e) {}
		}

		if(clazz != null) {
			try {
				Constructor constr = clazz.getConstructor();
				ret = constr.newInstance();
			} catch (Exception e) {}
		}
		return ret;
	}

	/**
	 * Log the complete stack trace of the provided exception in the logger.
	 *
	 * @param e The Exception.
	 * @param log The log in which the stack trace will be printed.
	 */
	public static void logStackTrace(Exception e, Logger log){
		if(log.isDebugEnabled()){
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(stream);
			e.printStackTrace(ps);
			log.debug("\n" + stream.toString());
			try {
				stream.close();
			} catch (IOException e1) {}
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

	/**
	 * Reads a configuration item value as a {@link List}. If the {@link ConfigLoader}
	 * returns the values as a string, this one is inserted in a newly created list.
	 *
	 * @param configItemName The name of the configuration item that will be read.
	 * @return The {@link List} of values associated with the provided name.
	 */
	@SuppressWarnings("unchecked")
    public static List<String> readConfigurationList(String configItemName) {
		List<String> ret = null;
		try {
			ret = (List<String>) ConfigLoader.getInstance().get(configItemName);
		} catch(ClassCastException e) {
			String tmp = (String) ConfigLoader.getInstance().get(configItemName);
			ret = new ArrayList<String>(1);
			ret.add(tmp);
		}
		return ret;
	}

	/**
	 * Reads a configuration item value as a string. If the {@link ConfigLoader}
	 * returns the values as a {@link List}, the items of the list are concatenated
	 * with commas.
	 *
	 * @param configItemName The name of the configuration item that will be read.
	 * @return The string value associated with the provided name.
	 */
	@SuppressWarnings("unchecked")
    public static String readConfigurationString(String configItemName) {
		String ret = null;
		try {
			ret = (String) ConfigLoader.getInstance().get(configItemName);
		} catch(ClassCastException e) {
			List<String> list = (List<String>) ConfigLoader.getInstance().get(configItemName);
			StringBuffer tmp = new StringBuffer();
			boolean first = true;
			for(String str : list) {
				if(!first) {
					tmp.append(",");
				}
				tmp.append(str);
				first = true;
			}
		}
		return ret;
	}

	/**
	 * Loads the resource with the provided name. The resource is
	 * searched using, as first try, the provided class loader (if its not
	 * <code>null</code>), then the class loader that loaded this class
	 * and, as last try, the system class loader. If the resource cannot be
	 * found, the method returns <code>null</code>.
	 *
	 * @param resourceName The name of the resource that will be loaded.
	 * @param firstClassLoader The class loader that will be used as first try.
	 * @return An {@link URL} pointing to the resource or <code>null</code> if
	 * the specified resource cannot be located.
	 */
	public static URL readResource(String resourceName, ClassLoader firstClassLoader) {
		URL ret = null;

		if(firstClassLoader != null) {
			ret = firstClassLoader.getResource(resourceName);
		}

		if(ret == null){
			ret = Util.class.getClassLoader().getResource(resourceName);
		}

		if(ret == null){
			ret = ClassLoader.getSystemResource(resourceName);
		}

		return ret;
	}

	/**
	 * Executes flush and close operations in an null-safe and exception-safe
	 * manner. The object is checked to be instance of {@link Flushable}
	 * and/or {@link Closeable} before operations are executed.
	 *
	 * @param obj The object that will be flushed and closed.
	 */
	public static void safeFlushAndClose(Object obj){
		if(obj != null){
			if(obj instanceof Flushable){
				try{
					((Flushable)obj).flush();
				} catch(IOException e){}
			}

			if(obj instanceof Closeable){
				try{
					((Closeable)obj).close();
				} catch(IOException e){}
			}
		}
	}
}
