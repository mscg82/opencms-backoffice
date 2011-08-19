package com.mscg.appstarter.server.util;

import java.io.File;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Settings {

    private static final Logger LOG = LoggerFactory.getLogger(Settings.class);

    private static Configuration config;
    private static long lastConfigRead;

    static {
        initConfig();
    }

    public static synchronized void initConfig() {
        config = new CompositeConfiguration();
        File settingsFile = new File(Constants.SETTINGS_FILE_NAME);
        ((CompositeConfiguration)config).addConfiguration(new SystemConfiguration());
        try {
            ((CompositeConfiguration)config).addConfiguration(new XMLConfiguration(settingsFile));
            lastConfigRead = System.currentTimeMillis();
        } catch(Exception e) {
            LOG.warn("Cannot load configuration file", e);
        }
    }

    public static synchronized Configuration getConfig() {
        return config;
    }

    public static synchronized long getLastConfigRead() {
        return lastConfigRead;
    }

}
