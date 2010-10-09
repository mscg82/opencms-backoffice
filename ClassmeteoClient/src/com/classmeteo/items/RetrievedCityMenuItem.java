package com.classmeteo.items;

import com.classmeteo.data.Settings;

public class RetrievedCityMenuItem extends SavedCityMenuItem {

	public RetrievedCityMenuItem(String text, String value) {
		super(text, value);
	}

	/* (non-Javadoc)
	 * @see com.classmeteo.items.SavedCityMenuItem#onSelection()
	 */
	public void onSelection() {
		Settings.getSavedLocations().put(cityId, text);
		Settings.getSelectCityDialog().show();
	}

}
