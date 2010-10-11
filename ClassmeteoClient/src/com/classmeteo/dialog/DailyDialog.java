package com.classmeteo.dialog;

import org.j4me.ext.EmptyMenuOption;
import org.j4me.ext.ExitApplicationMenuOption;
import org.j4me.ext.OpenMenuMenuOption;

import com.classmeteo.data.Settings;
import com.mscg.util.Properties;

public class DailyDialog extends GenericForecastDialog {

	public DailyDialog() {

	}
	
	public void init() {
		Properties tr = Settings.getTranslation(); 
		
		setTitle(tr.getProperty("forecast.title.daily"));
		
		Settings.getSelectCityDialog().setPrevious(this);
		
		addLeftItem(new OpenMenuMenuOption(tr.getProperty("menu.goto.searchcity"), Settings.getSelectCityDialog()));
		addLeftItem(new EmptyMenuOption());
		addLeftItem(new ExitApplicationMenuOption(tr.getProperty("menu.exit")));
		
		addRightItem(new OpenMenuMenuOption(tr.getProperty("forecast.types.current"), Settings.getCurrentCondDialog()));
		addRightItem(new OpenMenuMenuOption(tr.getProperty("forecast.types.hourly"), Settings.getHourlyDialog()));
		
		setMenuText(tr.getProperty("menu.actionbutton"), tr.getProperty("menu.forecastbutton"));
		inited = true;
	}
	
}
