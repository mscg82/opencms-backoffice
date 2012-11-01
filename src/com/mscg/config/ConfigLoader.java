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

	private static final long serialVersionUID = -7803708266647335688L;

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

    public static final String DYNDNS_RETRIEVER_CLASS      = "dyndns.interface.retriever";
    public static final String DYNDNS_STORAGE_CLASS        = "dyndns.interface.storage";
    public static final String DYNDNS_CACHED_STORAGE_CLASS = "dyndns.interface.cached-storage-inner";

    public static final String DYNDNS_PROTOCOL			= "dyndns.interface.protocol";
    public static final String DYNDNS_SERVER			= "dyndns.interface.server";
    public static final String DYNDNS_PORT				= "dyndns.interface.port";
    public static final String DYNDNS_CONTEXT			= "dyndns.interface.context";
    public static final String DYNDNS_STORAGE_METHOD	= "dyndns.interface.methods.ipstorage";
    public static final String DYNDNS_SERVICE			= "dyndns.service";
    public static final String DYNDNS_THREAD_TIMEOUT	= "dyndns.thread.timeout";
    public static final String DYNDNS_THREAD_ENABLED	= "dyndns.thread.enabled";

    public static final String TESTER_CLASS = "dyndns.interface.tester-class";

    public static final String FHJ_TEST_URL = "dyndns.interface.fhj.methods.test";

    public static final String EATJ_THINKTIME        = "dyndns.interface.eatj.think-time";
    public static final String EATJ_USERAGENT        = "dyndns.interface.eatj.user-agent";
    public static final String EATJ_TESTURL          = "dyndns.interface.eatj.methods.test";
    public static final String EATJ_URL_PREFIX       = "dyndns.interface.eatj.urls.prefix";
    public static final String EATJ_URL_MAIN         = "dyndns.interface.eatj.urls.main-url";
    public static final String EATJ_URL_CAPTCHA      = "dyndns.interface.eatj.urls.captcha-url";
    public static final String EATJ_URL_FORM         = "dyndns.interface.eatj.urls.form-action";
    public static final String EATJ_URL_CONFIRM      = "dyndns.interface.eatj.urls.confirm-url";
    public static final String EATJ_URL_ACCOUNT      = "dyndns.interface.eatj.urls.account-url";
    public static final String EATJ_URL_RESTART      = "dyndns.interface.eatj.urls.restart";
    public static final String EATJ_URL_LOGOUT       = "dyndns.interface.eatj.urls.logout";
    public static final String EATJ_PARAM_USERNAME   = "dyndns.interface.eatj.parameters.username-param";
    public static final String EATJ_PARAM_PASSWORD   = "dyndns.interface.eatj.parameters.password-param";
    public static final String EATJ_PARAM_CAPTCHA    = "dyndns.interface.eatj.parameters.captcha-param";
    public static final String EATJ_PARAM_UUID       = "dyndns.interface.eatj.parameters.uuid-param";
    public static final String EATJ_VALUES_USERNAME  = "dyndns.interface.eatj.parameters.values.username";
    public static final String EATJ_VALUES_PASSWORD  = "dyndns.interface.eatj.parameters.values.password";
    public static final String EATJ_VALUES_PWDREADER = "dyndns.interface.eatj.parameters.values.password-reader";

    public static final String DTDNS_METHOD          = "dyndns.interface.dtdns.method";
    public static final String DTDNS_PASSWORD        = "dyndns.interface.dtdns.password";
    public static final String DTDNS_PASSWORD_READER = "dyndns.interface.dtdns.password-reader";

    public static final String CAPTCHA_PARAMS        = "dyndns.interface.eatj.captcha";
    public static final String CAPTCHA_TMP_FILE      = CAPTCHA_PARAMS + ".tmp-file-name";
    public static final String CAPTCHA_TRAINING_SETS = CAPTCHA_PARAMS + ".training-sets";

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
    @SuppressWarnings({"rawtypes", "unchecked"})
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
						applConfig = configFile.toURI().toURL();
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
	    		log.trace(key + ": " + values);
	    		if(values.size() == 1)
	    			propList.put(key, values.get(0));
	    		else
	    			propList.put(key, values);
	    	}
    	}

    	return propList;
    }
}
