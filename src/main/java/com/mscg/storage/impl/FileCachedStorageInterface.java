package com.mscg.storage.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import com.mscg.config.ConfigLoader;

public class FileCachedStorageInterface extends CachedStorageInterface {

    private static Logger log = Logger.getLogger(FileCachedStorageInterface.class);

    protected File storedIPsFile;

    @Override
    protected void saveIPsInCache(List<String> IPs) {
        super.saveIPsInCache(IPs);

        if(storedIPsFile != null) {
            PrintStream writer = null;
            try {
                writer = new PrintStream(storedIPsFile);
                for(String IP : IPs) {
                    writer.println(IP);
                    if(log.isDebugEnabled())
                        log.debug("IP " + IP + " saved on file");
                }
                writer.flush();
            } catch(Exception e) {
                log.error("Cannot save stored IPs on file", e);
            } finally {
                try {
                    writer.close();
                } catch(Throwable e){}
            }
        }
    }

    @Override
    public void init() throws ConfigurationException {
        super.init();

        BufferedReader reader = null;
        try {
            String filename = (String)ConfigLoader.getInstance().get(ConfigLoader.DYNDNS_CACHED_STORAGE_FILE);
            storedIPsFile = new File(filename);
            if(!storedIPsFile.exists()) {
                storedIPsFile.getParentFile().mkdirs();
                if(!storedIPsFile.createNewFile())
                    throw new FileNotFoundException("Cannot create file " + filename);
            }

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(storedIPsFile)));
            String line;
            while((line = reader.readLine()) != null) {
                line = line.trim();
                if(line.length() == 0 || line.charAt(0) == '#')
                    continue;

                if(log.isDebugEnabled())
                    log.debug("IP " + line + "read from file");
                lastStoredIPs.add(line);
            }
        } catch(Exception e) {
            log.warn("Cannot read stored IPs file", e);
            lastStoredIPs.clear();
            storedIPsFile = null;
        } finally {
            try {
                reader.close();
            } catch(Throwable e){}
        }
    }

}
