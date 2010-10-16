package com.classmeteo.items;

import com.classmeteo.data.Settings;
import com.classmeteo.dialog.SelectCityDialog;

public class RetrievedCityMenuItem extends SavedCityMenuItem {

	public RetrievedCityMenuItem(String text, String value) {
		super(text, value);
	}

	/* (non-Javadoc)
	 * @see com.classmeteo.items.SavedCityMenuItem#onSelection()
	 */
	public void onSelection() {
		Settings.getSavedLocations().put(cityId, text);
		SelectCityDialog dialog = Settings.getSelectCityDialog();
		dialog.setSelectLast(true);
		dialog.show();
	}

}
