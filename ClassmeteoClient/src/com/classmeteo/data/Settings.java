package com.classmeteo.data;

import java.io.IOException;

import org.j4me.ui.components.Picture;
import org.j4me.ui.components.Whitespace;

import com.classmeteo.dialog.CurrentCondDialog;
import com.classmeteo.dialog.DailyDialog;
import com.classmeteo.dialog.HourlyDialog;
import com.classmeteo.dialog.SearchCityDialog;
import com.classmeteo.dialog.SelectCityDialog;
import com.classmeteo.dialog.SplashDialog;
import com.mscg.util.Collections;
import com.mscg.util.Map;
import com.mscg.util.Properties;
import com.mscg.util.RecordStoreMap;

public class Settings {

	private static Properties translation;
	private static RecordStoreMap rsm;
	private static Map savedLocations;
	
	private static String actualLocation;
	/**
	 * @return the actualLocationName
	 */
	public static String getActualLocationName() {
		return actualLocationName;
	}

	/**
	 * @param actualLocationName the actualLocationName to set
	 */
	public static void setActualLocationName(String actualLocationName) {
		Settings.actualLocationName = actualLocationName;
	}

	private static String actualLocationName;
	
	private static Whitespace whitespace;
	
	private static SplashDialog splashScreen;
	private static CurrentCondDialog currentCondDialog;
	private static HourlyDialog hourlyDialog;
	private static DailyDialog dailyDialog;
	private static SelectCityDialog selectCityDialog;
	private static SearchCityDialog searchCityDialog;
	
	private static Picture logo;
	
	public static void close() {
		try {
			synchronized (savedLocations) {
				rsm.close();				
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @return the actualLocation
	 */
	public static String getActualLocation() {
		return actualLocation;
	}

	/**
	 * @return the currentCondDialog
	 */
	public static CurrentCondDialog getCurrentCondDialog() {
		if(currentCondDialog == null) {
			currentCondDialog = new CurrentCondDialog();
		}
		return currentCondDialog;
	}

	/**
	 * @return the dailyDialog
	 */
	public static DailyDialog getDailyDialog() {
		if(dailyDialog == null) {
			dailyDialog = new DailyDialog();
		}
		return dailyDialog;
	}

	/**
	 * @return the hourlyDialog
	 */
	public static HourlyDialog getHourlyDialog() {
		if(hourlyDialog == null) {
			hourlyDialog = new HourlyDialog();
		}
		return hourlyDialog;
	}

	/**
	 * @return the logo
	 */
	public static Picture getLogo() {
		if(logo == null) {
			logo = new Picture();
			try {
				logo.setImage("/img/logo.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return logo;
	}

	/**
	 * @return the savedLocations
	 */
	public static Map getSavedLocations() {
		return savedLocations;
	}

	/**
	 * @return the searchCityDialog
	 */
	public static SearchCityDialog getSearchCityDialog() {
		if(searchCityDialog == null) {
			searchCityDialog = new SearchCityDialog();
		}
		return searchCityDialog;
	}

	/**
	 * @return the selectCityDialog
	 */
	public static SelectCityDialog getSelectCityDialog() {
		if(selectCityDialog == null) {
			selectCityDialog = new SelectCityDialog(null);
		}
		return selectCityDialog;
	}

	/**
	 * @return the splashScreen
	 */
	public static SplashDialog getSplashScreen() {
		if(splashScreen == null) {
			splashScreen = new SplashDialog();
		}
		return splashScreen;
	}

	public static Properties getTranslation() {
		return translation;
	}

	public static Whitespace getWhiteSpace() {
		if(whitespace == null)
			whitespace = new Whitespace(27);
		return whitespace;
	}

	public static void init(Properties translation, String savedLocationsRecordStore) throws Exception {
		setTranslation(translation);
		rsm = new RecordStoreMap(savedLocationsRecordStore);
		savedLocations = Collections.synchronizedMap(rsm);
	}

	/**
	 * @param actualLocation the actualLocation to set
	 */
	public static void setActualLocation(String actualLocation) {
		Settings.actualLocation = actualLocation;
	}
	
	/**
	 * @param currentCondDialog the currentCondDialog to set
	 */
	public static void setCurrentCondDialog(CurrentCondDialog currentCondDialog) {
		Settings.currentCondDialog = currentCondDialog;
	}
	
	/**
	 * @param dailyDialog the dailyDialog to set
	 */
	public static void setDailyDialog(DailyDialog dailyDialog) {
		Settings.dailyDialog = dailyDialog;
	}

	/**
	 * @param logo the logo to set
	 */
	public static void setLogo(Picture logo) {
		Settings.logo = logo;
	}

	/**
	 * @param savedLocations the savedLocations to set
	 */
	public static void setSavedLocations(Map savedLocations) {
		Settings.savedLocations = savedLocations;
	}

	/**
	 * @param searchCityDialog the searchCityDialog to set
	 */
	public static void setSearchCityDialog(SearchCityDialog searchCityDialog) {
		Settings.searchCityDialog = searchCityDialog;
	}
	
	/**
	 * @param selectCityDialog the selectCityDialog to set
	 */
	public static void setSelectCityDialog(SelectCityDialog selectCityDialog) {
		Settings.selectCityDialog = selectCityDialog;
	}

	/**
	 * @param splashScreen the splashScreen to set
	 */
	public static void setSplashScreen(SplashDialog splashScreen) {
		Settings.splashScreen = splashScreen;
	}
	
	public static void setTranslation(Properties translation) {
		Settings.translation = translation;
	}
}
