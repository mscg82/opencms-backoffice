/**
 *
 */
package eni.virtualoffice.persistence.stores;

import javax.persistence.EntityManager;

import eni.virtualoffice.persistence.manager.EntityManagerStore;

/**
 * This store is the superclass of all backend stores.
 *
 * @author Giuseppe Miscione
 *
 */
public class GenericStore {

	protected EntityManager em;

	public GenericStore() {
		em = EntityManagerStore.getEntityManager();
	}

	@Override
	protected void finalize() throws Throwable {
		closeEntityManager();
	}

	protected void closeEntityManager() {
		if(em != null) {
			try {
				em.close();
			} catch(Exception e){}
			em = null;
		}
	}
}
