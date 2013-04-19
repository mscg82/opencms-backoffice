package com.mscg.storage.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.mscg.config.ConfigLoader;
import com.mscg.storage.StorageInterface;
import com.mscg.util.Util;

public class CachedStorageInterface implements StorageInterface {

    private static Logger log = Logger.getLogger(CachedStorageInterface.class);

    protected StorageInterface storageInterface;
    protected Set<String> lastStoredIPs;

    protected boolean checkIfNotStores(List<String> IPs) {
        if(lastStoredIPs == null)
            return true;

        for(String ip : IPs) {
            if(!lastStoredIPs.contains(ip))
                return true;
        }

        // All IPs are already stored
        return false;
    }

    protected void saveIPsInCache(List<String> IPs) {
        lastStoredIPs.clear();
        lastStoredIPs.addAll(IPs);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void init() throws ConfigurationException {
        lastStoredIPs = new HashSet<String>();

        String className = (String)ConfigLoader.getInstance().get(ConfigLoader.DYNDNS_CACHED_STORAGE_CLASS);
        try {
            log.debug("Loading storage class " + className + "...");

            storageInterface = (StorageInterface)Util.loadClass(className, getClass().getClassLoader());
            storageInterface.init();
        } catch(Exception e) {
            log.error("Error found while looking for storage class, " +
                      "using default " + DynDnsStorageInterface.class.getCanonicalName(), e);
            storageInterface = new DynDnsStorageInterface();
        }
    }

    public void storeIP(String service, List<String> IPs) throws HttpException, IOException {
        if(checkIfNotStores(IPs)) {
            log.debug("IPs are changed, storing new values...");

            storageInterface.storeIP(service, IPs);

            saveIPsInCache(IPs);
        }
        else
            log.info("IPs are not changed and won't be stored.");
    }

}
