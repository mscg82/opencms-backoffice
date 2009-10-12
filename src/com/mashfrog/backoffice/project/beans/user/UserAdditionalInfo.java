/**
 *
 */
package com.mashfrog.backoffice.project.beans.user;

import java.io.Serializable;

/**
 * A bean with additional informations on
 * the user that logs to the backoffice.
 *
 * @author Giuseppe Miscione
 *
 */
public class UserAdditionalInfo implements Serializable{

	private static final long serialVersionUID = -5298550319662809553L;

	private String sex;

	/**
	 * Builds an instance of {@link UserAdditionalInfo}
	 * with default values.
	 */
	public UserAdditionalInfo(){
		this("M");
	}

	/**
	 * Builds an instance of {@link UserAdditionalInfo} with
	 * the provided values.
	 *
	 * @param sex The sex of the user.
	 */
	public UserAdditionalInfo(String sex){
		setSex(sex);
	}

	/**
	 * Returns the setted sex for the user. The result
	 * may be <strong>&quot;M&quot;</strong> or
	 * <strong>&quot;F&quot;</strong>, always in upper case.
	 * @return The setted sex for the user.
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * Sets  the sex of the user. If the provided
	 * string is neither <strong>&quot;M&quot;</strong> or
	 * <strong>&quot;F&quot;</strong>,
	 * <strong>&quot;M&quot;</strong> is used as default.
	 * The provided sex string can be in any case and
	 * it will be converted to upper case.
	 * @param sex The sex of the user.
	 */
	public void setSex(String sex) {
		this.sex = sex.toUpperCase();
		if(!this.sex.equals("M") || ! this.sex.equals("F")){
			this.sex = "M";
		}
	}


}
