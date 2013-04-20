/**
 *
 */
package com.mscg.config;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

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
     * The Web applications standard file separator.
     */
    protected static String webAppFileSep = "/";

    /**
     * The name of the configuration file.
     */
    protected static final String configFileName = "appl-config.xml";

    /**
     * The instance of the configuration loader.
     */
    protected static XMLConfiguration confInstance = null;

    public static final String DNS_PROVIDER_CLASS = "dns.provider-class";

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
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map<String, Object> initInstance(ServletContext context) throws ConfigurationException{
    	if(confInstance == null){
    		String fullPath = context.getRealPath(configFileName);
        	File configFile = new File(fullPath);
        	if(!configFile.exists()){
        		fullPath = context.getRealPath("WEB-INF" + webAppFileSep + configFileName);
        		configFile = new File(fullPath);
        		if(!configFile.exists())
        			throw new ConfigurationException("Configuration file cannot be found.");
        	}

			confInstance = new XMLConfiguration(fullPath);
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

    /**
     * Always returns a configuration parameters as a <code>List&lt;String&gt;</code>.
     * If the parameter is a <code>String</code>, a list with only one element is returned,
     * if the parameter is not present in the configuration file the result is an empty list.
     *
     * @param paramName The name of the parameter that will be retrieved.
     * @return a <code>List&lt;String&gt;</code> corresponding to the provided parameter name.
     */
    @SuppressWarnings("unchecked")
    public static List<String> getParameterAsList(String paramName) {
    	List<String> ret = new LinkedList<String>();
    	Object param = getInstance().get(paramName);
    	if(param != null) {
    		if(param instanceof List) {
    			ret = (List<String>)param;
    		}
    		else if(param instanceof String) {
    			ret.add((String)param);
    		}
    	}
    	return ret;
    }

    /**
     * Always returns a configuration parameters as a <code>String</code>.
     * If the parameter is a <code>List&lt;String&gt;</code>, the values are chained using a comma,
     * if the parameter is not present in the configuration file the result <code>null</code>.
     *
     * @param paramName The name of the parameter that will be retrieved.
     * @return a <code>String</code> corresponding to the provided parameter name.
     */
    @SuppressWarnings("unchecked")
    public static String getParameterAsString(String paramName) {
    	String ret = null;
    	Object param = getInstance().get(paramName);
    	if(param != null) {
    		if(param instanceof List) {
    			List<String> paramsStr = (List<String>)param;
    			StringBuffer tmp = new StringBuffer();
    			boolean first = true;
    			for(String p : paramsStr) {
    				if(!first) tmp.append(",");
    				else first = false;
    				tmp.append(p);
    			}
    			ret = tmp.toString();
    		}
    		else if(param instanceof String) {
    			ret = (String)param;
    		}
    	}
    	return ret;
    }
}
