/**
 *
 */
package eni.virtualoffice.config;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.struts2.ServletActionContext;
import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;

import eni.virtualoffice.util.Constants;

/**
 * @author Giuseppe Miscione
 */
public class ConfigLoader implements Serializable {

	private static final long serialVersionUID = 6362465098370510528L;

	/**
     * The logger to which the configuration loader registers its actions and problems.
     */
    protected static Logger LOG = Red5LoggerFactory.getLogger(ConfigLoader.class, Constants.contextName);

	/**
     * The system specific file separator.
     */
    public static String sysFileSep = System.getProperty("file.separator");

    /**
     * The Web applications standard information directory.
     */
    public static String webAppInfoDir = "WEB-INF";

    /**
     * The Web applications standard file separator.
     */
    public static String webAppFileSep = "/";

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
     * Returns a map with the configuration parameters found in the configuratin file.
     * Calling this methos is equivalent to {@link #getInstance(ServletContext)} using
     * as parameter <code>ServletActionContext.getServletContext()</code>.
     *
     * @return a <code>Map&lt;String, Object&gt;</code> with the pairs key-values.
     * If a key has only a value, the mapped object is a String, else
     * the value is a <code>List&lt;String&gt;</code>.
     */
    public static Map<String, Object> getInstance() {
    	return getInstance(ServletActionContext.getServletContext());
    }

    /**
     * Returns a map with the configuration parameters found in the configuratin file.
     *
     * @return a <code>Map&lt;String, Object&gt;</code> with the pairs key-values.
     * If a key has only a value, the mapped object is a String, else
     * the value is a <code>List&lt;String&gt;</code>.
     */
    public static Map<String, Object> getInstance(ServletContext context) {
    	try {
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
		    		LOG.debug(key + ": " + values);
		    		if(values.size() == 1)
		    			propList.put(key, values.get(0));
		    		else
		    			propList.put(key, values);
		    	}
	    	}
    	} catch (Exception e) {
			LOG.error("An error occurred while getting configuration: " + e.getMessage(), e);
			propList = new HashMap<String,Object>();
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
     * @throws ConfigurationException
     */
    public static URL searchConfigFile(ServletContext context, String configFileName) throws ConfigurationException
    {
    	URL configFileUrl = ConfigLoader.class.getResource(configFileName);
		if(configFileUrl == null)
		{
			configFileUrl = ConfigLoader.class.getResource("/" + configFileName);
		}

		if(configFileUrl == null)
		{
    		String fullPath = context.getRealPath(configFileName);
    		File configFile = new File(fullPath);
        	if(!configFile.exists())
        	{
        		fullPath = context.getRealPath(webAppInfoDir + webAppFileSep + configFileName);
        		configFile = new File(fullPath);
        		if(!configFile.exists())
        		{
        			throw new ConfigurationException("Configuration file cannot be found.");
        		}
        	}

        	try
        	{
				configFileUrl = configFile.toURI().toURL();
			}
        	catch (MalformedURLException e)
        	{
				throw new ConfigurationException("Configuration file cannot be found: " + e.getMessage());
			}
		}
		return configFileUrl;
	}

}
