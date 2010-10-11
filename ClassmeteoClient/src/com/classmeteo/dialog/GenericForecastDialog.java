package com.classmeteo.dialog;

import org.j4me.ext.UpdatablePopupMenuDialog;
import org.j4me.ui.components.ProgressBar;

public abstract class GenericForecastDialog extends UpdatablePopupMenuDialog {

	protected boolean inited;
	protected String lastShownCityId;
	protected ProgressBar spinner;
	
	public GenericForecastDialog() {
		inited = false;
		lastShownCityId = null;
		spinner = new ProgressBar();
	}
	
	public void showNotify() {
		if(!inited) {
			init();
			inited = true;
		}
	}
	
	public void resetCityId() {
		lastShownCityId = null;
	}
	
	public abstract void init();
}
