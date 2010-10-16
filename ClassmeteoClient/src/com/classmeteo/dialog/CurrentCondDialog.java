/**
 * 
 */
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

import com.classmeteo.data.InterruptibleThread;
import com.classmeteo.data.Settings;
import com.classmeteo.data.Util;
import com.classmeteo.data.WebServiceAccessor;
import com.classmeteo.items.StopThreadMenuItem;
import com.classmeteo.items.UpdateInterfaceMenuItem;
import com.classmeteo.theme.ClassMeteoAppTheme;
import com.classmeteo.ws.ClassMeteoFluxesWS;
import com.classmeteo.ws.CurrentCondWS;
import com.mscg.util.Properties;

/**
 * @author Giuseppe Miscione
 *
 */
public class CurrentCondDialog extends GenericForecastDialog {
	
	private class CurrentCondThread extends InterruptibleThread {

		public void run() {
			
			Properties tr = Settings.getTranslation();
			ClassMeteoAppTheme theme = (ClassMeteoAppTheme)UIManager.getTheme();
			try {
				resetCityId();
				
				ClassMeteoFluxesWS client = WebServiceAccessor.getClient();
				CurrentCondWS curr = client.getCurrentByLocId(Settings.getActualLocation());
				synchronized (spinner) {
					delete(spinner);
					spinner.setMaxValue(1);
					repaint();
				}
				
				String upDay = curr.getLocObsDay();
				String year = upDay.substring(0, 4);
				String month = upDay.substring(4, 6);
				String day = upDay.substring(6);
				String upTime = curr.getLocObsTm();
				String hour = upTime.substring(0, 2);
				String minute = upTime.substring(2, 4);
				//String second = upTime.substring(4);
				String update = day + "/" + month + "/" + year + " " + hour + ":" + minute;
				
				Label upLabel1 = new Label(tr.getProperty("forecast.current.update") + " " + update);
				upLabel1.setFont(theme.getSmallFont());
				upLabel1.setFontColor(theme.getUpdateColor());
				append(upLabel1);
				
				// add the table with the forecast data
				Vector colWidths = new Vector(2);
				colWidths.addElement(new Double(0.4));
				colWidths.addElement(new Double(0.6));
				TableLayout ftl = new TableLayout(colWidths);
				ftl.startBulkUpdate();
				
				int row = ftl.addRow();
				Picture icon = new Picture();
				try {
					icon.setImage("/52x52/" + curr.getSky() + ".png");
				} catch(Exception e){
					icon.setImage("/52x52/44.png");
				}
				icon.setHorizontalAlignment(Graphics.HCENTER);
				icon.setVerticalAlignment(Graphics.VCENTER);
				Label tempLabel = new Label(curr.getTmpC() + " °C");
				tempLabel.setFont(theme.getBigBoldFont());
				formatCell(ftl.addComponent(row, 0, icon), theme, false);
				formatCell(ftl.addComponent(row, 1, new Label(curr.getWx())), theme, false);
				ftl.addComponent(row, 1, tempLabel);
				
				row = ftl.addRow();
				Label tmp = new Label(tr.getProperty("forecast.current.hic"));
				tmp.setFont(theme.getSmallFont());
				formatCell(ftl.addComponent(row, 0, tmp), theme);
				tmp = new Label(curr.getHIC() + " °C");
				tmp.setFont(theme.getSmallBoldFont());
				formatCell(ftl.addComponent(row, 1, tmp), theme);
				
				row = ftl.addRow();
				tmp = new Label(tr.getProperty("forecast.current.wind"));
				tmp.setFont(theme.getSmallFont());
				formatCell(ftl.addComponent(row, 0, tmp), theme);
				if("CALM".equalsIgnoreCase(curr.getWDirAsc())) {
					tmp = new Label(tr.getProperty("forecast.current.wind.calm"));
					tmp.setFont(theme.getSmallBoldFont());
					formatCell(ftl.addComponent(row, 1, tmp), theme);
				}
				else {
					tmp = new Label(tr.getProperty("forecast.current.wind.from") + " " + curr.getWDirAsc());
					tmp.setFont(theme.getSmallBoldFont());
					ftl.addComponent(row, 1, tmp).setPaddings(theme.getCurrentCondPaddings());
					tmp = new Label(tr.getProperty("forecast.current.wind.to") + " " + curr.getWSpdK() + " Km/h");
					tmp.setFont(theme.getSmallBoldFont());
					formatCell(ftl.addComponent(row, 1, tmp), theme);
				}
				
				row = ftl.addRow();
				tmp = new Label(tr.getProperty("forecast.current.uv"));
				tmp.setFont(theme.getSmallFont());
				formatCell(ftl.addComponent(row, 0, tmp), theme);
				tmp = new Label(curr.getUvIdx() + " (" + curr.getUvDes() + ")");
				tmp.setFont(theme.getSmallBoldFont());
				formatCell(ftl.addComponent(row, 1, tmp), theme);
				
				row = ftl.addRow();
				tmp = new Label(tr.getProperty("forecast.current.dewpoint"));
				tmp.setFont(theme.getSmallFont());
				formatCell(ftl.addComponent(row, 0, tmp), theme);
				tmp = new Label(curr.getDwptC() + " °C");
				tmp.setFont(theme.getSmallBoldFont());
				formatCell(ftl.addComponent(row, 1, tmp), theme);
				
				row = ftl.addRow();
				tmp = new Label(tr.getProperty("forecast.current.press"));
				tmp.setFont(theme.getSmallFont());
				formatCell(ftl.addComponent(row, 0, tmp), theme);
				tmp = new Label(
					Util.formatDouble(curr.getAlt().length() == 0 ?
							Double.parseDouble(curr.getPres()) : Util.convertToMb(curr.getAlt()), 2) + " mb");
				tmp.setFont(theme.getSmallBoldFont());
				formatCell(ftl.addComponent(row, 1, tmp), theme);
				
				row = ftl.addRow();
				tmp = new Label(tr.getProperty("forecast.current.humid"));
				tmp.setFont(theme.getSmallFont());
				formatCell(ftl.addComponent(row, 0, tmp), theme);
				tmp = new Label(curr.getRH() + "%");
				tmp.setFont(theme.getSmallBoldFont());
				formatCell(ftl.addComponent(row, 1, tmp), theme);
				
				row = ftl.addRow();
				tmp = new Label(tr.getProperty("forecast.current.visibility"));
				tmp.setFont(theme.getSmallFont());
				formatCell(ftl.addComponent(row, 0, tmp), theme, false);
				tmp = new Label(curr.getVisK() + " Km");
				tmp.setFont(theme.getSmallBoldFont());
				formatCell(ftl.addComponent(row, 1, tmp), theme, false);
				
				ftl.endBulkUpdate();
				CurrentCondDialog.this.append(ftl);
					
				Settings.getSelectCityDialog().setPrevious(CurrentCondDialog.this);
				
				// build the soft key menus
				getLeftMenuItems().removeAllElements();
				getRightMenuItems().removeAllElements();
				
				addLeftItem(new OpenMenuMenuOption(tr.getProperty("menu.goto.searchcity"), Settings.getSelectCityDialog()));
				addLeftItem(new MenuOption(new UpdateInterfaceMenuItem(tr.getProperty("menu.update"), CurrentCondDialog.this)));
				addLeftItem(new EmptyMenuOption());
				addLeftItem(new ExitApplicationMenuOption(tr.getProperty("menu.exit")));
				
				addRightItem(new OpenMenuMenuOption(tr.getProperty("forecast.types.hourly"), Settings.getHourlyDialog()));
				addRightItem(new OpenMenuMenuOption(tr.getProperty("forecast.types.daily"), Settings.getDailyDialog()));
				
				setMenuText(tr.getProperty("menu.actionbutton"), tr.getProperty("menu.forecastbutton"));
				
				if(!isInterrupted()) {
					setCityId(Settings.getActualLocation());
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

	public CurrentCondDialog() {
		updateThread = null;
	}
	
	public void init() {
		Properties tr = Settings.getTranslation(); 
		setTitle(tr.getProperty("forecast.title.current"));
	}
	
	public void initInterface() {
		Properties tr = Settings.getTranslation();
		Settings.getSelectCityDialog().setPrevious(null);
		
		getLeftMenuItems().removeAllElements();
		getRightMenuItems().removeAllElements();
		
		updateThread = new CurrentCondThread();
		
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
	
}
