/**
 * 
 */
package com.classmeteo.items;

import com.classmeteo.data.Settings;


/**
 * @author Giuseppe Miscione
 *
 */
public class RemoveCityMenuItem extends GenericMenuItem {

	protected String cityId;
	
	public RemoveCityMenuItem(String text, String cityId) {
		super(text);
		this.cityId = cityId;
	}

	/* (non-Javadoc)
	 * @see org.j4me.ui.MenuItem#onSelection()
	 */
	public void onSelection() {
		Settings.getSavedLocations().remove(cityId);
	}

}
