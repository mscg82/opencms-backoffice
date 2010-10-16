package com.classmeteo.items;

import com.classmeteo.dialog.DailyDialog;

public class SwitchDayNightMenuItem extends GenericMenuItem {

	private DailyDialog dialog;
	
	public SwitchDayNightMenuItem(String text, DailyDialog dialog) {
		super(text);
		this.dialog = dialog;
	}

	public void onSelection() {
		dialog.setShowDay(!dialog.isShowDay());
		dialog.onSelectionChanged(dialog.getDaySelector());
	}

}
