package com.classmeteo.data;

import org.j4me.ui.components.Whitespace;

import com.classmeteo.dialog.CurrentCondDialog;
import com.classmeteo.dialog.SearchCityDialog;
import com.classmeteo.dialog.SelectCityDialog;
import com.mscg.util.Collections;
import com.mscg.util.Map;
import com.mscg.util.Properties;
import com.mscg.util.RecordStoreMap;

public class Settings {

	private static Properties translation;
	private static RecordStoreMap rsm;
	private static Map savedLocations;
	
	private static String actualLocation;
	
	private static Whitespace whitespace;
	
	private static CurrentCondDialog currentCondDialog;
	private static SelectCityDialog selectCityDialog;
	private static SearchCityDialog searchCityDialog;
	
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
	
	public static void setTranslation(Properties translation) {
		Settings.translation = translation;
	}
}
