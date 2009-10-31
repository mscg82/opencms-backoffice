/**
 *
 */
package com.mscg.dyndns.main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import com.mscg.config.ConfigLoader;
import com.mscg.dyndns.main.thread.GenericStoreThread;
import com.mscg.dyndns.main.thread.LocalIPStoreThread;
import com.mscg.util.Util;
import com.mscg.util.passwordreader.CryptedPasswordReader;

/**
 * @author Giuseppe Miscione
 *
 */
public class DyndnsClientMain {
	private static Logger log = Logger.getLogger(DyndnsClientMain.class);

	private static void launchAppAndWait() throws Exception {
		String exePath = (String)ConfigLoader.getInstance().get("dyndns.process.exe-path");
		String folderName = (String)ConfigLoader.getInstance().get("dyndns.process.folder");
		File folder = (folderName == null || folderName.trim().length() == 0) ? null : new File(folderName);

		if(exePath != null && exePath.trim().length() != 0 &&
		   folder != null && folder.exists()){

			List<String> parameters = null;
			try{
				parameters = (List<String>)ConfigLoader.getInstance().get("dyndns.process.parameter");
			} catch(ClassCastException e){
				parameters = new LinkedList<String>();
				String param = (String)ConfigLoader.getInstance().get("dyndns.process.parameter");
				if(param != null && param.trim().length() != 0)
					parameters.add(param);
			}

			List<String> command = new LinkedList<String>();
			command.add(exePath);
			for(String parameter : parameters){
				command.add(parameter.trim());
			}

			ProcessBuilder pb = new ProcessBuilder(command);
			pb.directory(folder);

			log.debug("Launching application \"" + command + "\" using \""
				+ pb.directory().getCanonicalPath() + "\" as working directory.");

			Process p = pb.start();
			p.waitFor();
		}
		else {
			log.debug("No application will be launched. Waiting for console input...");

			System.out.println("Press return to stop IP storage...");
			System.in.read();
		}
	}

	public static GenericStoreThread launchStoreThread() throws ConfigurationException, ClassCastException, IOException {
		String className = (String)ConfigLoader.getInstance().get("dyndns.store-thread");
		GenericStoreThread thread = null;
		try{
			log.debug("Loading class " + className + "...");

			Class threadClass = Class.forName(className);
			Constructor<GenericStoreThread> threadConstructor = threadClass.getConstructor();
			thread = threadConstructor.newInstance();
		} catch(Exception e){
			// use default thread class
			log.error("Error found while looking for thread class, " +
					"using default " + LocalIPStoreThread.class.getCanonicalName(), e);
			thread = new LocalIPStoreThread();
		}
		thread.start();

		return thread;
	}

	public static void main(String[] args) {

		if(args.length >= 1){
			if(args.length >= 2){
				if("encode".equals(args[0])){
					String pwd = args[1];
					CryptedPasswordReader cpr = new CryptedPasswordReader();
					try {
						System.out.println(pwd + " -> " + cpr.encodeString(pwd));
					} catch (Exception e) {
						System.out.println("Cannot encode string: " + e.getMessage());
					}
				}
				else if("decode".equals(args[0])){
					String pwd = args[1];
					CryptedPasswordReader cpr = new CryptedPasswordReader();
					try {
						System.out.println(pwd + " -> " + cpr.decodeString(pwd));
					} catch (Exception e) {
						System.out.println("Cannot decode string: " + e.getMessage());
					}
				}
			}
		}
		else{
			GenericStoreThread thread = null;
			try {
				Util.initApplication();

				thread = launchStoreThread();

				launchAppAndWait();

				log.debug("Exiting from application.");

			} catch (Exception e) {
				log.error("Error found (" + e.getClass() + ") while running application.", e);
				Util.logStackTrace(e, log);
				e.printStackTrace();
			} finally{
				thread.setExit(true);
				if(thread != null)
					thread.interrupt();
			}
		}
	}

}
