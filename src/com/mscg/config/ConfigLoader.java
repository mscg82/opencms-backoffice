/**
 *
 */
package com.mscg.config;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

/**
 * @author Giuseppe Miscione
 *
 */
public class ConfigLoader implements Serializable {

	private static final long serialVersionUID = 9083908460526771377L;

	/**
     * The logger to which the configuration loader registers its actions and problems.
     */
    protected static Logger log = Logger.getLogger(ConfigLoader.class);

    /**
     * The system specific file separator.
     */
    protected static String sysFileSep = System.getProperty("file.separator");

    /**
     * The default configuration folder.
     */
    protected static String configFolder = "config";

    /**
     * The Web applications standard file separator.
     */
    protected static String webAppFileSep = "/";

    /**
     * The name of the configuration file.
     */
    protected static final String configFileName = "/appl-config.xml";

    /**
     * The instance of the configuration loader.
     */
    protected static XMLConfiguration confInstance = null;

    /**
     * The map of properties found int the configuration file.
     */
    protected static Map<String, Object> propList = null;

    public static final String DYNDNS_PROTOCOL			= "dyndns.interface.protocol";
    public static final String DYNDNS_SERVER			= "dyndns.interface.server";
    public static final String DYNDNS_PORT				= "dyndns.interface.port";
    public static final String DYNDNS_CONTEXT			= "dyndns.interface.context";
    public static final String DYNDNS_STORAGE_METHOD	= "dyndns.interface.methods.ipstorage";
    public static final String DYNDNS_SERVICE			= "dyndns.service";
    public static final String DYNDNS_THREAD_TIMEOUT	= "dyndns.thread.timeout";

    /**
     * Returns a map with the configuration parameters found in the configuration file.
     *
     * @return a <code>Map&lt;String, Object&gt;</code> with the pairs key-values.
     * If a key has only a value, the mapped object is a String, else
     * the value is a <code>List&lt;String&gt;</code>.
     */
    public static Map<String, Object> getInstance(){
    	return propList;
    }

	/**
     * Returns a map with the configuration parameters found in the configuration file.
     *
     * @return a <code>Map&lt;String, Object&gt;</code> with the pairs key-values.
     * If a key has only a value, the mapped object is a String, else
     * the value is a <code>List&lt;String&gt;</code>.
     * @throws ConfigurationException If some error occurs.
     */
    public static Map<String, Object> initInstance() throws ConfigurationException{
    	if(confInstance == null){
    		URL applConfig = ConfigLoader.class.getResource(configFileName);

    		if(applConfig == null) {
    			File configFile = new File("." + sysFileSep + configFolder + sysFileSep + configFileName);
    			if(!configFile.exists()){
    				configFile = new File(".." + sysFileSep + configFolder + sysFileSep + configFileName);
    			}

    			if(configFile.exists()){
    				try {
						applConfig = configFile.toURL();
					} catch (MalformedURLException e) { }
    			}
    		}

    		if(applConfig == null) {
    			throw new ConfigurationException("Cannot find configuration file.");
    		}

    		confInstance = new XMLConfiguration(applConfig);
			confInstance.setEncoding("UTF-8");
			propList = new HashMap<String, Object>();
			for(Iterator it = confInstance.getKeys(); it.hasNext(); ){
	    		String key = (String) it.next();
	    		List<String> values = confInstance.getList(key);
	    		log.debug(key + ": " + values);
	    		if(values.size() == 1)
	    			propList.put(key, values.get(0));
	    		else
	    			propList.put(key, values);
	    	}
    	}

    	return propList;
    }
}
