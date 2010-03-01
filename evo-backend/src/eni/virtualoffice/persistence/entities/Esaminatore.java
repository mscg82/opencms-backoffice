/**
 *
 */
package eni.virtualoffice.persistence.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;

import eni.virtualoffice.util.Util;

/**
 * @author Giuseppe Miscione
 *
 */
@Entity
public class Esaminatore implements Serializable {

	private static final long serialVersionUID = 4047008771476900387L;

	private Integer id;
	private String name;
	private String surname;
	private String nickname;
	private String password;

	public static boolean checkAdminExistence(EntityManager em) throws Exception {
		boolean ret = false;
		if(em != null) {
			try {
				em.getTransaction().begin();
				String query =
					"SELECT t " +
					"FROM Esaminatore t " +
					"WHERE nickname = :nick";
				Query pQuery = em.createQuery(query);
				pQuery.setParameter("nick", "admin");
				Esaminatore es = null;
				try {
					es = (Esaminatore)pQuery.getSingleResult();
				} catch(Exception e) {}
				if(es == null) {
					es = new Esaminatore();
					es.setNickname("admin");
					es.setAndEncodePassword("adminadmin");
					em.persist(es);
				}
				ret = true;
				em.getTransaction().commit();

			} catch(Exception e) {
				em.getTransaction().rollback();
				throw e;
			}
		}
		return ret;
	}

	public Esaminatore() {

	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @param password the password to set
	 */
	protected void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param password the password to set
	 */
	public void setAndEncodePassword(String password) {
		this.password = Util.encodeUserPassword(password);
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Override
	public String toString(){
		return
			"Full name: " + getName() + " " + getSurname() + " (id = " + getId() + "). " +
			"Nickname: " + getNickname() + ", encoded password: " + getPassword();
	}

}
