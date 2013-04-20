package com.mscg.dyndns.dnsfactory;

import java.lang.reflect.Constructor;

import org.apache.log4j.Logger;

import com.mscg.config.ConfigLoader;
import com.mscg.dyndns.dnsfactory.memory.MemoryDnsProvider;
import com.mscg.util.Util;

public class DnsFactory {
	private static Logger log = Logger.getLogger(DnsFactory.class);

	private static DnsProvider providerSingleton = null;

	@SuppressWarnings({"rawtypes", "unchecked"})
    public static DnsProvider getProvider(){
		if(providerSingleton == null){
			String providerClassName = (String) ConfigLoader.getInstance().get(ConfigLoader.DNS_PROVIDER_CLASS);
			if(providerClassName != null){
				try {
					Class providerClass = Class.forName(providerClassName);
					// get the no argument constructor
					Constructor constr = providerClass.getConstructor();
					// build an instance of the class
					providerSingleton = (DnsProvider)constr.newInstance();
				} catch (ClassNotFoundException e) {
					log.error("Specified class \"" + providerClassName + "\" not found. Using default MemoryDnsProvider.");
					Util.logStackTrace(e, log);
				} catch (SecurityException e) {
					log.error("Specified class \"" + providerClassName + "\" cannot be instantiated. Using default MemoryDnsProvider.");
					Util.logStackTrace(e, log);
				} catch (Exception e) {
					log.error("Specified class \"" + providerClassName + "\" has no suitable constructor. Using default MemoryDnsProvider.");
					Util.logStackTrace(e, log);
				}
			}
			if(providerSingleton == null)
				providerSingleton = new MemoryDnsProvider();
		}
		return providerSingleton;
	}
}
