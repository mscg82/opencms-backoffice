/**
 * 
 */
package com.classmeteo.items;

import org.j4me.ui.MenuItem;

/**
 * @author Giuseppe Miscione
 *
 */
public abstract class GenericMenuItem implements MenuItem {

	protected String text;
	
	public GenericMenuItem(String text) {
		this.text = text;
	}
	
	/* (non-Javadoc)
	 * @see org.j4me.ui.MenuItem#getText()
	 */
	public String getText() {
		return text;
	}

}
