package com.classmeteo.dialog;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;

import org.j4me.ext.ChainedMenuItem;
import org.j4me.ext.EmptyMenuOption;
import org.j4me.ext.ExitApplicationMenuOption;
import org.j4me.ext.OpenMenuMenuItem;
import org.j4me.ext.OpenMenuMenuOption;
import org.j4me.ext.TableLayout;
import org.j4me.ui.UIManager;
import org.j4me.ui.components.Label;
import org.j4me.ui.components.MenuOption;
import org.j4me.ui.components.Picture;
import org.j4me.ui.components.RadioButton;
import org.j4me.ui.components.RadioButton.OnChangeListener;

import com.classmeteo.data.InterruptibleThread;
import com.classmeteo.data.Settings;
import com.classmeteo.data.Util;
import com.classmeteo.data.WebServiceAccessor;
import com.classmeteo.items.StopThreadMenuItem;
import com.classmeteo.items.SwitchDayNightMenuItem;
import com.classmeteo.theme.ClassMeteoAppTheme;
import com.classmeteo.ws.ClassMeteoFluxesWS;
import com.classmeteo.ws.DailyForecastWS;
import com.mscg.util.Properties;

public class DailyDialog extends HorizontalScrollForecastDialog implements OnChangeListener {
	
	private class DailyDialogUpdateThread extends InterruptibleThread {

		public void run() {
			Properties tr = Settings.getTranslation(); 		
			try {
				resetCityId();
				
				ClassMeteoFluxesWS client = WebServiceAccessor.getClient();
				setDailies(client.getDailyByLocId(Settings.getActualLocation(), null));
				synchronized (spinner) {
					delete(spinner);
					spinner.setMaxValue(1);
					repaint();
				}
				
				if(!isInterrupted()) {
					daySelector = new RadioButton();
					daySelector.setListener(DailyDialog.this);
					daySelector.setLabel(tr.getProperty("forecast.daily.selectday"));
					for(int i = 0; i < getDailies().length; i++) {
						DailyForecastWS daily = getDailies()[i];
						String upDay = daily.getFcstValDay();
						String month = upDay.substring(4, 6);
						String day = upDay.substring(6);
						daySelector.append(daily.getDow() + " " + day + " / " + month);
						if(daily.getHiTmpC() != null && daily.getHiTmpC().trim().length() != 0) {
							setShowDay(true);
						}
						else {
							setShowDay(false);
						}
					}
					append(daySelector);
					daySelector.setSelectedIndex(0);
					
					setCityId(Settings.getActualLocation());
					Settings.getSelectCityDialog().setPrevious(DailyDialog.this);
					setSelected(daySelector);
					invalidate();
					repaint();
				}
				else {
					initInterface();
					getLeftMenuItems().removeAllElements();
					getRightMenuItems().removeAllElements();
					resetCityId();
				}
			} catch(IOException e) {
				e.printStackTrace();
				synchronized (spinner) {
					delete(spinner);
					spinner.setMaxValue(1);
					repaint();
				}
				append(new Label(tr.getProperty("error.network")));
			} catch(Exception e) {
				e.printStackTrace();
				synchronized (spinner) {
					delete(spinner);
					spinner.setMaxValue(1);
					repaint();
				}
				append(new Label(tr.getProperty("error.generic")));
			}
		}
		
	}
	
	private TableLayout upperTable;
	private InterruptibleThread updateThread;
	private RadioButton daySelector;
	private DailyForecastWS dailies[];
	private TableLayout forecastTable;
	private Label dayLabel;
	private boolean showDay;
	
	public DailyDialog() {
		updateThread = null;
		setShowDay(true);
	}

	/**
	 * @return the dailies
	 */
	public synchronized DailyForecastWS[] getDailies() {
		return dailies;
	}
	
	/**
	 * @return the daySelector
	 */
	public RadioButton getDaySelector() {
		return daySelector;
	}

	public void init() {
		Properties tr = Settings.getTranslation(); 		
		setTitle(tr.getProperty("forecast.title.daily"));		
	}

	public void initInterface() {
		Properties tr = Settings.getTranslation();
		Settings.getSelectCityDialog().setPrevious(null);
		
		getLeftMenuItems().removeAllElements();
		getRightMenuItems().removeAllElements();
		
		updateThread = new DailyDialogUpdateThread();
		
		addLeftItem(new MenuOption(
			new ChainedMenuItem(
				new StopThreadMenuItem(null, updateThread),
				new OpenMenuMenuItem(tr.getProperty("menu.prev"), Settings.getSelectCityDialog()))));
		setDefaultMenuText();
		
		deleteAll();
		
		// add a table with the logo on the left
		// and the city name of the right
		Vector colWidths = new Vector(2);
		int imageWidth = Settings.getLogo().getImage().getWidth();
		int screenWidth = UIManager.getScreen().getWidth();
		double ratio = ((double)imageWidth) / screenWidth + 0.03;
		colWidths.addElement(new Double(ratio));
		colWidths.addElement(new Double(1.0 - ratio));
		upperTable = new TableLayout(colWidths);
		upperTable.startBulkUpdate();
		upperTable.addRow();
		upperTable.addComponent(0, 0, Settings.getLogo());
		Label cityName = new Label(Settings.getActualLocationName());
		cityName.setFont(((ClassMeteoAppTheme)UIManager.getTheme()).getBoldFont());
		upperTable.addComponent(0, 1, cityName);
		upperTable.endBulkUpdate();
		append(upperTable);
		
		append(spinner);
	}

	/**
	 * @return the showDay
	 */
	public synchronized boolean isShowDay() {
		return showDay;
	}

	public void onSelectionChanged(RadioButton radioButton) {
		synchronized (DailyDialog.this) {
			Properties tr = Settings.getTranslation();
			ClassMeteoAppTheme theme = (ClassMeteoAppTheme)UIManager.getTheme();
			
			// remove the old forecast infos
			if(dayLabel != null) {
				try {
					delete(dayLabel);
				} catch(Exception e){}
			}
			if(forecastTable != null) {
				try {
					delete(forecastTable);
				} catch(Exception e){}
			}
			
			Vector colWidths = new Vector(2);
			colWidths.addElement(new Double(0.4));
			colWidths.addElement(new Double(0.6));
			forecastTable = new TableLayout(colWidths);
			forecastTable.startBulkUpdate();
			boolean day = isShowDay();
			DailyForecastWS daily = getDailies()[daySelector.getSelectedIndex()];
			if(daily.getHiTmpC() == null || daily.getHiTmpC().trim().length() == 0) {
				day = false;
			}
			
			int row = forecastTable.addRow();
			Picture icon = new Picture();
			try {
				icon.setImage("/52x52/" + (day ? daily.getSky12() : daily.getSky1224()) + ".png");
			} catch(Exception e){
				try {
					icon.setImage("/52x52/44.png");
				} catch(Exception e2){}
			}
			icon.setHorizontalAlignment(Graphics.HCENTER);
			icon.setVerticalAlignment(Graphics.VCENTER);
			Label tempLabel = new Label((day ? daily.getHiTmpC() : daily.getLoTmpC()) + " °C");
			tempLabel.setFont(theme.getBigBoldFont());
			formatCell(forecastTable.addComponent(row, 0, icon), theme, false);
			formatCell(forecastTable.addComponent(row, 1, new Label((day ? daily.getTSnsblWx12() : daily.getTSnsblWx1224()))), theme, false);
			forecastTable.addComponent(row, 1, tempLabel);
			
			row = forecastTable.addRow();
			Label tmp = new Label(tr.getProperty("forecast.daily.prec"));
			tmp.setFont(theme.getSmallFont());
			formatCell(forecastTable.addComponent(row, 0, tmp), theme);
			tmp = new Label((day ? daily.getPOP12() : daily.getPOP1224()) + "%");
			tmp.setFont(theme.getSmallBoldFont());
			formatCell(forecastTable.addComponent(row, 1, tmp), theme);
			tmp = new Label(Util.convertToMM((day ? daily.getQpf12() : daily.getQpf1224())) + " mm");
			tmp.setFont(theme.getSmallBoldFont());
			forecastTable.addComponent(row, 1, tmp);
			
			row = forecastTable.addRow();
			tmp = new Label(tr.getProperty("forecast.current.wind"));
			tmp.setFont(theme.getSmallFont());
			formatCell(forecastTable.addComponent(row, 0, tmp), theme);
			if("CALM".equalsIgnoreCase((day ? daily.getWDirAsc12() : daily.getWDirAsc1224()))) {
				tmp = new Label(tr.getProperty("forecast.current.wind.calm"));
				tmp.setFont(theme.getSmallBoldFont());
				formatCell(forecastTable.addComponent(row, 1, tmp), theme);
			}
			else {
				tmp = new Label(tr.getProperty("forecast.current.wind.from") + " " + (day ? daily.getWDirAsc12() : daily.getWDirAsc1224()));
				tmp.setFont(theme.getSmallBoldFont());
				forecastTable.addComponent(row, 1, tmp).setPaddings(theme.getCurrentCondPaddings());
				tmp = new Label(tr.getProperty("forecast.current.wind.to") + " " + (day ? daily.getWSpdK12() : daily.getWSpdK1224()) + " Km/h");
				tmp.setFont(theme.getSmallBoldFont());
				formatCell(forecastTable.addComponent(row, 1, tmp), theme);
			}
			
			row = forecastTable.addRow();
			tmp = new Label(tr.getProperty("forecast.current.uv"));
			tmp.setFont(theme.getSmallFont());
			formatCell(forecastTable.addComponent(row, 0, tmp), theme);
			tmp = new Label((day ? daily.getUvIdx() + " (" + daily.getUvDes() + ")" : "N/A"));
			tmp.setFont(theme.getSmallBoldFont());
			formatCell(forecastTable.addComponent(row, 1, tmp), theme);
			
			forecastTable.endBulkUpdate();
			dayLabel = new Label(tr.getProperty((day ? "forecast.daily.day" : "forecast.daily.night")));
			dayLabel.setFont(theme.getBoldFont());
			
			append(dayLabel);
			append(forecastTable);
			
			// build the soft key menus
			getLeftMenuItems().removeAllElements();
			getRightMenuItems().removeAllElements();
			
			addLeftItem(new OpenMenuMenuOption(tr.getProperty("menu.goto.searchcity"), Settings.getSelectCityDialog()));
			addLeftItem(new EmptyMenuOption());
			addLeftItem(new ExitApplicationMenuOption(tr.getProperty("menu.exit")));
			
			if(day || (daily.getHiTmpC() != null && daily.getHiTmpC().trim().length() != 0)) {
				addRightItem(new MenuOption(
					new SwitchDayNightMenuItem(
						tr.getProperty((day ? "forecast.daily.night" : "forecast.daily.day")),
						DailyDialog.this)));
				addRightItem(new EmptyMenuOption());
			}
			addRightItem(new OpenMenuMenuOption(tr.getProperty("forecast.types.current"), Settings.getCurrentCondDialog()));
			addRightItem(new OpenMenuMenuOption(tr.getProperty("forecast.types.hourly"), Settings.getHourlyDialog()));
			
			setMenuText(tr.getProperty("menu.actionbutton"), tr.getProperty("menu.forecastbutton"));
		}
	}
	
	/**
	 * @param dailies the dailies to set
	 */
	public synchronized void setDailies(DailyForecastWS[] dailies) {
		this.dailies = dailies;
	}
	
	/**
	 * @param daySelector the daySelector to set
	 */
	public void setDaySelector(RadioButton daySelector) {
		this.daySelector = daySelector;
	}
	
	/**
	 * @param showDay the showDay to set
	 */
	public synchronized void setShowDay(boolean showDay) {
		this.showDay = showDay;
	}

	public void showNotify() {
		super.showNotify();
		updateComponents();
	}

	public void updateComponents() {
		if(getCityId() == null || !getCityId().equals(Settings.getActualLocation())) {
			// reinit the interface
			
			initInterface();
			spinner.setMaxValue(0);
			
			// download forecast data
			updateThread.start();
		}
	}

	public void scrollLeft() {
		try {
			int selectedDay = daySelector.getSelectedIndex();
			if(selectedDay > 0)
				daySelector.setSelectedIndex(selectedDay - 1);
		} catch(Exception e){}
	}

	public void scrollRigth() {
		try {
			int selectedDay = daySelector.getSelectedIndex();
			if(selectedDay < daySelector.getSize() - 1)
				daySelector.setSelectedIndex(selectedDay + 1);
		} catch(Exception e){}
	}
	
}
