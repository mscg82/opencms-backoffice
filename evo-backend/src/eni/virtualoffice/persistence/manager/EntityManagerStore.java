/**
 *
 */
package eni.virtualoffice.persistence.manager;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;

import eni.virtualoffice.config.ConfigLoader;
import eni.virtualoffice.util.Constants;
import eni.virtualoffice.util.Util;

/**
 * @author Giuseppe Miscione
 *
 */
public class EntityManagerStore {

	private static Logger LOG = Red5LoggerFactory.getLogger(EntityManagerStore.class, Constants.contextName);

	private static EntityManagerFactory emf;

	static {
		try{
			Map<String, String> properties = new LinkedHashMap<String, String>();

			String persistenceUnitName = (String)ConfigLoader.getInstance().get("persistence.persistence-unit");
			if(Util.isEmptyOrWhiteSpaceOnly(persistenceUnitName)) {
				persistenceUnitName = "evo-pu";
			}
			emf = Persistence.createEntityManagerFactory(persistenceUnitName, properties);
			if(emf != null){
				EntityManager em = emf.createEntityManager();
				em.close();
			}
		} catch(Exception e){
			LOG.error("Unable to initialize entity manager.", e);
		}
	}

	public static EntityManager getEntityManager(){
		return (emf != null ? emf.createEntityManager() : null);
	}
}
