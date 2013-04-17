/**
 *
 */
package com.mscg.dyndns.main;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;

import com.mscg.config.ConfigLoader;
import com.mscg.dyndns.main.cli.CommandLineOptions;
import com.mscg.dyndns.main.thread.GenericStoreThread;
import com.mscg.util.Util;
import com.mscg.util.passwordreader.impl.CryptedPasswordReader;

/**
 * @author Giuseppe Miscione
 *
 */
public class DyndnsClientMain {
	private static Logger LOG = Logger.getLogger(DyndnsClientMain.class);

	private static CommandLineOptions commandLineOptions;
	private static CommandLine commandLine;

	@SuppressWarnings("unchecked")
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

			LOG.debug("Launching application \"" + command + "\" using \""
				+ pb.directory().getCanonicalPath() + "\" as working directory.");

			Process p = pb.start();
			p.waitFor();
		}
		else {
		    if(commandLine.hasOption(CommandLineOptions.COMMAND_LINE_NOWAIT)) {
		        LOG.debug("No application will be launched. Exiting main thread...");
		        return;
		    }

		    LOG.debug("No application will be launched. Waiting for console input...");


			System.out.println("Press return to stop IP storage...");
			System.in.read();
		}
	}

	public static void main(String[] args) {

	    commandLineOptions = new CommandLineOptions(args);

	    if(commandLineOptions.isPrintHelp())
	        commandLineOptions.printHelp();
	    else {
	        commandLine = commandLineOptions.getCommandLine();

	        if(commandLine.hasOption(CommandLineOptions.COMMAND_LINE_ENCODE)) {
	            String pwd = commandLine.getOptionValue(CommandLineOptions.COMMAND_LINE_ENCODE);

	            CryptedPasswordReader cpr = new CryptedPasswordReader();
                try {
                    System.out.println(pwd + " -> " + cpr.encodeString(pwd));
                } catch (Exception e) {
                    System.out.println("Cannot encode string: " + e.getMessage());
                }
	        }
	        else if(commandLine.hasOption(CommandLineOptions.COMMAND_LINE_DECODE)) {
	            String pwd = commandLine.getOptionValue(CommandLineOptions.COMMAND_LINE_DECODE);
	            CryptedPasswordReader cpr = new CryptedPasswordReader();
                try {
                    System.out.println(pwd + " -> " + cpr.decodeString(pwd));
                } catch (Exception e) {
                    System.out.println("Cannot decode string: " + e.getMessage());
                }
	        }
	        else {
	            GenericStoreThread thread = null;
	            try {
	                Util.initApplication();

	                thread = new GenericStoreThread();
	                thread.start();

	                launchAppAndWait();

	                LOG.debug("Exiting from application.");

	            } catch (Exception e) {
	                LOG.error("Error found (" + e.getClass().getCanonicalName() + ") while running application.", e);
	                Util.logStackTrace(e, LOG);
	                e.printStackTrace();
	            } finally{
	                thread.setExit(true);
	                if(thread != null)
	                    thread.interrupt();
	            }
	        }
	    }
	}

}
