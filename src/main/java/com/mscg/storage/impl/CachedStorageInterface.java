package com.mscg.storage.impl;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.mscg.config.ConfigLoader;
import com.mscg.storage.StorageInterface;

public class CachedStorageInterface implements StorageInterface {

    private static Logger log = Logger.getLogger(CachedStorageInterface.class);

    protected StorageInterface storageInterface;
    protected List<String> lastStoredIPs;

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

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void init() throws ConfigurationException {
        String className = (String)ConfigLoader.getInstance().get(ConfigLoader.DYNDNS_CACHED_STORAGE_CLASS);
        try {
            log.debug("Loading storage class " + className + "...");

            Class storageClass = Class.forName(className);
            Constructor<StorageInterface> retrieverConstructor = storageClass.getConstructor();
            storageInterface = retrieverConstructor.newInstance();
            storageInterface.init();
        } catch(Exception e) {
            log.error("Error found while looking for storage class, " +
                      "using default " + DynDnsStorageInterface.class.getCanonicalName(), e);
            storageInterface = new DynDnsStorageInterface();
        }
    }

    public void storeIP(String service, List<String> IPs) throws HttpException, IOException {
        if(checkIfNotStores(IPs)) {
            log.info("IPs are changed, storing new values...");

            storageInterface.storeIP(service, IPs);
            lastStoredIPs = IPs;
        }
        else
            log.debug("IPs are not changed and won't be stored.");
    }

}
