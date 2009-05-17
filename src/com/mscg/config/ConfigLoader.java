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

/**
 * @author Giuseppe Miscione
 *
 */
public class ConfigLoader implements Serializable {

	private static final long serialVersionUID = -6358464559053983873L;

	/**
     * The system specific file separator.
     */
    protected static final String sysFileSep = System.getProperty("file.separator");

    /**
     * The standard configuration folder.
     */
    protected static final String standardConfigFolder = "config";

    /**
     * The name of the configuration file.
     */
    protected static final String configFileName = "appl-config.xml";

    /**
     * The instance of the configuration loader.
     */
    protected static XMLConfiguration confInstance = null;

    /**
     * The map of properties found int he configuration file.
     */
    protected static Map<String, Object> propList = null;

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
    		URL applConfig = searchConfigFile(configFileName);

    		confInstance = new XMLConfiguration(applConfig);
			confInstance.setEncoding("UTF-8");
			propList = new HashMap<String, Object>();
			for(Iterator it = confInstance.getKeys(); it.hasNext(); ){
	    		String key = (String) it.next();
	    		List<String> values = confInstance.getList(key);
	    		if(values.size() == 1)
	    			propList.put(key, values.get(0));
	    		else
	    			propList.put(key, values);
	    	}
    	}

    	return propList;
    }

    /**
     * Searches the specified configuration file prior in the
     * default configuration folder than, if not found there,
     * in the application resources.
     *
     * @param fileName The name of the configuration file that will
     * be searched.
     * @return An {@link URL} pointing the requested configuration file.
     */
    public static URL searchConfigFile(String fileName){
    	if(!fileName.startsWith("/"))
    		fileName = "/" + fileName;

    	URL ret = null;

    	File configFolder = new File(standardConfigFolder);
		if(configFolder.exists()){
			File configFile = new File(standardConfigFolder + fileName);
			if(configFile.exists()){
				try {
					ret = configFile.toURL();
				} catch (MalformedURLException e) { }
			}
		}

		if(ret == null)
			ret = ConfigLoader.class.getResource(configFileName);

		return ret;
    }
}
