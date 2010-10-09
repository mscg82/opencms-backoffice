package com.classmeteo.items;

import com.classmeteo.data.Settings;

public class SavedCityMenuItem extends GenericMenuItem {

	protected String cityId;
	
	public SavedCityMenuItem(String text, String cityId) {
		super(text);
		this.cityId = cityId;
	}

	public void onSelection() {
		Settings.setActualLocation(cityId);
		Settings.getCurrentCondDialog().show();
	}

	/**
	 * @return the cityId
	 */
	public String getCityId() {
		return cityId;
	}

}
