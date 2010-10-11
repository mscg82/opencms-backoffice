package com.classmeteo.items;

import com.classmeteo.dialog.GenericForecastDialog;

public class UpdateInterfaceMenuItem extends GenericMenuItem {

	private GenericForecastDialog dialog;
	
	public UpdateInterfaceMenuItem(String text, GenericForecastDialog dialog) {
		super(text);
		this.dialog = dialog;
	}

	public void onSelection() {
		dialog.resetCityId();
		dialog.updateComponents();
	}

}
