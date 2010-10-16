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
import com.classmeteo.data.WebServiceAccessor;
import com.classmeteo.items.StopThreadMenuItem;
import com.classmeteo.theme.ClassMeteoAppTheme;
import com.classmeteo.ws.ClassMeteoFluxesWS;
import com.classmeteo.ws.HourlyForecastWS;
import com.mscg.util.Properties;

public class HourlyDialog extends HorizontalScrollForecastDialog implements OnChangeListener {
	
	private class HourlyDialogThread extends InterruptibleThread {

		public void run() {
			Properties tr = Settings.getTranslation(); 		
			try {
				resetCityId();
				
				ClassMeteoFluxesWS client = WebServiceAccessor.getClient();
				HourlyForecastWS tmp[] = client.getHourlyByLocId(Settings.getActualLocation(), null);
				HourlyForecastWS firstHour = client.getNextHourlyByLocId(Settings.getActualLocation(), "0")[0];
				synchronized (spinner) {
					delete(spinner);
					spinner.setMaxValue(1);
					repaint();
				}
				
				if(!isInterrupted()) {
					int offset = 0;
					for(;offset < tmp.length; offset++) {
						HourlyForecastWS hourly = tmp[offset];
						if(hourly.getLocValTm().equals(firstHour.getLocValTm()))
							break;
					}
					HourlyForecastWS hourlies[] = new HourlyForecastWS[tmp.length - offset];
					System.arraycopy(tmp, offset, hourlies, 0, hourlies.length);
					setHourlies(hourlies);
					
					hourSelector = new RadioButton();
					hourSelector.setListener(HourlyDialog.this);
					hourSelector.setLabel(tr.getProperty("forecast.hourly.selecthour"));
					for(int i = 0; i < getHourlies().length; i++) {
						HourlyForecastWS hourly = getHourlies()[i];
						String hour = hourly.getLocValTm().substring(0, 2);
						String upDay = hourly.getLocValDay();
						String month = upDay.substring(4, 6);
						String day = upDay.substring(6);
						hourSelector.append(
							hourly.getDow().substring(0, 3).toLowerCase() + " " +
							day + "/" + month + ", " +
							hour + ":00");
					}
					append(hourSelector);
					hourSelector.setSelectedIndex(0);
					
					setCityId(Settings.getActualLocation());
					Settings.getSelectCityDialog().setPrevious(HourlyDialog.this);
					setSelected(hourSelector);
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
	private HourlyForecastWS hourlies[];
	private RadioButton hourSelector;
	private TableLayout forecastTable;

	public HourlyDialog() {
		updateThread = null;
	}

	/**
	 * @return the hourlies
	 */
	public synchronized HourlyForecastWS[] getHourlies() {
		return hourlies;
	}

	/**
	 * @param hourlies the hourlies to set
	 */
	public synchronized void setHourlies(HourlyForecastWS[] hourlies) {
		this.hourlies = hourlies;
	}

	public void initInterface() {
		Properties tr = Settings.getTranslation();
		Settings.getSelectCityDialog().setPrevious(null);
		
		getLeftMenuItems().removeAllElements();
		getRightMenuItems().removeAllElements();
		
		updateThread = new HourlyDialogThread();
		
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
	
	public void init() {
		Properties tr = Settings.getTranslation(); 	
		setTitle(tr.getProperty("forecast.title.hourly"));
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

	public void onSelectionChanged(RadioButton radioButton) {
		synchronized (HourlyDialog.this) {
			Properties tr = Settings.getTranslation();
			ClassMeteoAppTheme theme = (ClassMeteoAppTheme)UIManager.getTheme();
			
			// remove the old forecast infos
			if(forecastTable != null) {
				try {
					delete(forecastTable);
				} catch(Exception e){}
			}
			
			HourlyForecastWS hourly = getHourlies()[hourSelector.getSelectedIndex()];
			
			Vector colWidths = new Vector(2);
			colWidths.addElement(new Double(0.4));
			colWidths.addElement(new Double(0.6));
			forecastTable = new TableLayout(colWidths);
			forecastTable.startBulkUpdate();
			
			int row = forecastTable.addRow();
			Picture icon = new Picture();
			try {
				icon.setImage("/52x52/" + hourly.getSky() + ".png");
			} catch(Exception e){
				try {
					icon.setImage("/52x52/44.png");
				} catch(Exception e2){}
			}
			icon.setHorizontalAlignment(Graphics.HCENTER);
			icon.setVerticalAlignment(Graphics.VCENTER);
			Label tempLabel = new Label(hourly.getTmpC() + " °C");
			tempLabel.setFont(theme.getBigBoldFont());
			formatCell(forecastTable.addComponent(row, 0, icon), theme, false);
			formatCell(forecastTable.addComponent(row, 1, new Label(hourly.getTSnsblWx())), theme, false);
			forecastTable.addComponent(row, 1, tempLabel);
			
			row = forecastTable.addRow();
			Label tmp = new Label(tr.getProperty("forecast.current.hic"));
			tmp.setFont(theme.getSmallFont());
			formatCell(forecastTable.addComponent(row, 0, tmp), theme);
			tmp = new Label(hourly.getHIC() + " °C");
			tmp.setFont(theme.getSmallBoldFont());
			formatCell(forecastTable.addComponent(row, 1, tmp), theme);
			
			row = forecastTable.addRow();
			tmp = new Label(tr.getProperty("forecast.current.wind"));
			tmp.setFont(theme.getSmallFont());
			formatCell(forecastTable.addComponent(row, 0, tmp), theme);
			if("CALM".equalsIgnoreCase(hourly.getWDirAsc())) {
				tmp = new Label(tr.getProperty("forecast.current.wind.calm"));
				tmp.setFont(theme.getSmallBoldFont());
				formatCell(forecastTable.addComponent(row, 1, tmp), theme);
			}
			else {
				tmp = new Label(tr.getProperty("forecast.current.wind.from") + " " + hourly.getWDirAsc());
				tmp.setFont(theme.getSmallBoldFont());
				forecastTable.addComponent(row, 1, tmp).setPaddings(theme.getCurrentCondPaddings());
				tmp = new Label(tr.getProperty("forecast.current.wind.to") + " " + hourly.getWSpdK() + " Km/h");
				tmp.setFont(theme.getSmallBoldFont());
				formatCell(forecastTable.addComponent(row, 1, tmp), theme);
			}
			
			row = forecastTable.addRow();
			tmp = new Label(tr.getProperty("forecast.current.uv"));
			tmp.setFont(theme.getSmallFont());
			formatCell(forecastTable.addComponent(row, 0, tmp), theme);
			tmp = new Label(hourly.getUvIdx() + " (" + hourly.getUvDes() + ")");
			tmp.setFont(theme.getSmallBoldFont());
			formatCell(forecastTable.addComponent(row, 1, tmp), theme);
			
			row = forecastTable.addRow();
			tmp = new Label(tr.getProperty("forecast.daily.prec"));
			tmp.setFont(theme.getSmallFont());
			formatCell(forecastTable.addComponent(row, 0, tmp), theme);
			tmp = new Label(hourly.getPOP() + "%");
			tmp.setFont(theme.getSmallBoldFont());
			formatCell(forecastTable.addComponent(row, 1, tmp), theme);
			
			row = forecastTable.addRow();
			tmp = new Label(tr.getProperty("forecast.current.humid"));
			tmp.setFont(theme.getSmallFont());
			formatCell(forecastTable.addComponent(row, 0, tmp), theme);
			tmp = new Label(hourly.getRH() + "%");
			tmp.setFont(theme.getSmallBoldFont());
			formatCell(forecastTable.addComponent(row, 1, tmp), theme);
			
			row = forecastTable.addRow();
			tmp = new Label(tr.getProperty("forecast.current.dewpoint"));
			tmp.setFont(theme.getSmallFont());
			formatCell(forecastTable.addComponent(row, 0, tmp), theme);
			tmp = new Label(hourly.getDwptC() + " °C");
			tmp.setFont(theme.getSmallBoldFont());
			formatCell(forecastTable.addComponent(row, 1, tmp), theme);
			
			row = forecastTable.addRow();
			tmp = new Label(tr.getProperty("forecast.current.visibility"));
			tmp.setFont(theme.getSmallFont());
			formatCell(forecastTable.addComponent(row, 0, tmp), theme, false);
			tmp = new Label(hourly.getVisK() + " Km");
			tmp.setFont(theme.getSmallBoldFont());
			formatCell(forecastTable.addComponent(row, 1, tmp), theme, false);
			
			forecastTable.endBulkUpdate();
			
			append(forecastTable);
			
			// build the soft key menus
			getLeftMenuItems().removeAllElements();
			getRightMenuItems().removeAllElements();
			
			addLeftItem(new OpenMenuMenuOption(tr.getProperty("menu.goto.searchcity"), Settings.getSelectCityDialog()));
			addLeftItem(new EmptyMenuOption());
			addLeftItem(new ExitApplicationMenuOption(tr.getProperty("menu.exit")));
			
			addRightItem(new OpenMenuMenuOption(tr.getProperty("forecast.types.current"), Settings.getCurrentCondDialog()));
			addRightItem(new OpenMenuMenuOption(tr.getProperty("forecast.types.daily"), Settings.getDailyDialog()));
			
			setMenuText(tr.getProperty("menu.actionbutton"), tr.getProperty("menu.forecastbutton"));
		}
	}

	public void scrollLeft() {
		try {
			int selectedHour = hourSelector.getSelectedIndex();
			if(selectedHour > 0)
				hourSelector.setSelectedIndex(selectedHour - 1);
		} catch(Exception e){}
	}

	public void scrollRigth() {
		try {
			int selectedHour = hourSelector.getSelectedIndex();
			if(selectedHour < hourSelector.getSize() - 1)
				hourSelector.setSelectedIndex(selectedHour + 1);
		} catch(Exception e){}
	}
}
