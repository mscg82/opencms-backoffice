/**
 * 
 */
package com.mscg.dyndns.main;

import org.apache.log4j.Logger;

import com.mscg.dyndns.main.thread.IPStoreThread;
import com.mscg.util.Util;

/**
 * @author Giuseppe Miscione
 *
 */
public class DyndnsClientMain {
	private static Logger log = Logger.getLogger(DyndnsClientMain.class);

	public static void main(String[] args) {
		IPStoreThread thread = null;
		try {
			Util.initApplication();
			
			thread = new IPStoreThread();
			thread.start();
			
			System.out.println("Press a key to stop IP storage...");
			System.in.read();
			
		} catch (Exception e) {
			Util.logStackTrace(e, log);
			e.printStackTrace();
		} finally{
			if(thread != null)
				thread.interrupt();
		}
	}

}
