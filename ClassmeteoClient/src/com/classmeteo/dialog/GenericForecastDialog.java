package com.classmeteo.dialog;

import org.j4me.ext.TableCell;
import org.j4me.ext.UpdatablePopupMenuDialog;
import org.j4me.ui.components.ProgressBar;

import com.classmeteo.theme.ClassMeteoAppTheme;

public abstract class GenericForecastDialog extends UpdatablePopupMenuDialog {

	protected boolean inited;
	protected String lastShownCityId;
	protected ProgressBar spinner;
	
	public GenericForecastDialog() {
		inited = false;
		lastShownCityId = null;
		spinner = new ProgressBar();
	}
	
	protected void formatCell(TableCell cell, ClassMeteoAppTheme theme) {
		formatCell(cell, theme, true);
	}
	
	protected void formatCell(TableCell cell, ClassMeteoAppTheme theme, boolean withBorders) {
		cell.setPaddings(theme.getCurrentCondPaddings());
		if(withBorders)
			cell.setBorders(theme.getCurrentCondBorders());
	}
	
	public synchronized String getCityId() {
		return lastShownCityId;
	}
	
	public abstract void init();
	
	public synchronized void resetCityId() {
		setCityId(null);
	}
	
	public synchronized void setCityId(String cityId) {
		lastShownCityId = cityId;
	}

	public void showNotify() {
		if(!inited) {
			init();
			inited = true;
		}
	}
}
