/**
 * 
 */
package com.classmeteo.dialog;

import java.util.Vector;

import org.j4me.ext.EmptyMenuOption;
import org.j4me.ext.ExitApplicationMenuOption;
import org.j4me.ext.OpenMenuMenuOption;
import org.j4me.ext.TableLayout;
import org.j4me.ui.UIManager;
import org.j4me.ui.components.Label;
import org.j4me.ui.components.MenuOption;
import org.j4me.ui.components.Picture;

import com.classmeteo.data.Settings;
import com.classmeteo.data.Util;
import com.classmeteo.data.WebServiceAccessor;
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
	
	public CurrentCondDialog() {

	}

	public void showNotify() {
		super.showNotify();
		updateComponents();
	}

	public void updateComponents() {
		if(lastShownCityId == null || !lastShownCityId.equals(Settings.getActualLocation())) {
			// update the interface
			
			deleteAll();
			
			// add a table with the logo on the left
			// and the city name of the right
			Vector colWidths = new Vector(2);
			int imageWidth = Settings.getLogo().getImage().getWidth();
			int screenWidth = UIManager.getScreen().getWidth();
			double ratio = ((double)imageWidth) / screenWidth + 0.1;
			colWidths.addElement(new Double(ratio));
			colWidths.addElement(new Double(1.0 - ratio));
			TableLayout tl = new TableLayout(colWidths);
			tl.startBulkUpdate();
			tl.addRow();
			tl.addComponent(0, 0, Settings.getLogo());
			Label cityName = new Label(Settings.getActualLocationName());
			cityName.setFont(((ClassMeteoAppTheme)UIManager.getTheme()).getBoldFont());
			tl.addComponent(0, 1, cityName);
			tl.endBulkUpdate();
			append(tl);
			
			append(spinner);
			spinner.setMaxValue(0);
			
			new CurrentCondThread().start();
		}
	}

	public void init() {
		Properties tr = Settings.getTranslation(); 
		setTitle(tr.getProperty("forecast.title.current"));
		
		Settings.getSelectCityDialog().setPrevious(this);
		
		addLeftItem(new OpenMenuMenuOption(tr.getProperty("menu.goto.searchcity"), Settings.getSelectCityDialog()));
		addLeftItem(new MenuOption(new UpdateInterfaceMenuItem(tr.getProperty("menu.update"), this)));
		addLeftItem(new EmptyMenuOption());
		addLeftItem(new ExitApplicationMenuOption(tr.getProperty("menu.exit")));
		
		addRightItem(new OpenMenuMenuOption(tr.getProperty("forecast.types.hourly"), Settings.getHourlyDialog()));
		addRightItem(new OpenMenuMenuOption(tr.getProperty("forecast.types.daily"), Settings.getDailyDialog()));
		
		setMenuText(tr.getProperty("menu.actionbutton"), tr.getProperty("menu.forecastbutton"));
	}
	
	private class CurrentCondThread extends Thread {

		public void run() {
			
			try {
				ClassMeteoFluxesWS client = WebServiceAccessor.getClient();
				CurrentCondWS curr = client.getCurrentByLocId(Settings.getActualLocation());
				synchronized (spinner) {
					spinner.setMaxValue(1);
					CurrentCondDialog.this.delete(spinner);
				}
				
				// add a table with the icon on the left
				// and the forecast description of the right
				Picture icon = new Picture();
				try {
					icon.setImage("/52x52/" + curr.getSky() + ".png");
				} catch(Exception e){
					icon.setImage("/52x52/44.png");
				}
				Vector colWidths = new Vector(2);
				int imageWidth = icon.getImage().getWidth();
				int screenWidth = UIManager.getScreen().getWidth();
				double ratio = ((double)imageWidth) / screenWidth + 0.1;
				colWidths.addElement(new Double(ratio));
				colWidths.addElement(new Double(1.0 - ratio));
				TableLayout tl = new TableLayout(colWidths);
				tl.startBulkUpdate();
				tl.addRow();
				tl.addComponent(0, 0, icon);
				tl.addComponent(0, 1, new Label(curr.getWx()));
				Label tempLabel = new Label(curr.getTmpC() + " °C");
				tempLabel.setFont(((ClassMeteoAppTheme)UIManager.getTheme()).getBigBoldFont());
				tl.addComponent(0, 1, tempLabel);
				tl.endBulkUpdate();
				CurrentCondDialog.this.append(tl);
				
				// add the table with the forecast data
				Properties tr = Settings.getTranslation();
				colWidths = new Vector(2);
				colWidths.addElement(new Double(0.4));
				colWidths.addElement(new Double(0.6));
				TableLayout ftl = new TableLayout(colWidths);
				ftl.startBulkUpdate();
				
				int row = ftl.addRow();
				ftl.addComponent(row, 0, new Label(tr.getProperty("forecast.current.hic"))).setPaddings(ClassMeteoAppTheme.getCurrentCondPaddings());
				ftl.addComponent(row, 1, new Label(curr.getHIC() + " °C")).setPaddings(ClassMeteoAppTheme.getCurrentCondPaddings());
				
				row = ftl.addRow();
				ftl.addComponent(row, 0, new Label(tr.getProperty("forecast.current.wind"))).setPaddings(ClassMeteoAppTheme.getCurrentCondPaddings());
				ftl.addComponent(row, 1, new Label(
					"CALM".equalsIgnoreCase(curr.getWDirAsc()) ?
					tr.getProperty("forecast.current.wind.calm") :
					tr.getProperty("forecast.current.wind.from") + " " + curr.getWDirAsc() + " " +
					tr.getProperty("forecast.current.wind.to") + " " + curr.getWSpdK() + " Km/h")).setPaddings(ClassMeteoAppTheme.getCurrentCondPaddings());
				
				row = ftl.addRow();
				ftl.addComponent(row, 0, new Label(tr.getProperty("forecast.current.uv"))).setPaddings(ClassMeteoAppTheme.getCurrentCondPaddings());
				ftl.addComponent(row, 1, new Label(curr.getUvIdx() + " (" + curr.getUvDes() + ")")).setPaddings(ClassMeteoAppTheme.getCurrentCondPaddings());
				
				row = ftl.addRow();
				ftl.addComponent(row, 0, new Label(tr.getProperty("forecast.current.dewpoint"))).setPaddings(ClassMeteoAppTheme.getCurrentCondPaddings());
				ftl.addComponent(row, 1, new Label(curr.getDwptC() + " °C")).setPaddings(ClassMeteoAppTheme.getCurrentCondPaddings());
				
				row = ftl.addRow();
				ftl.addComponent(row, 0, new Label(tr.getProperty("forecast.current.press"))).setPaddings(ClassMeteoAppTheme.getCurrentCondPaddings());
				ftl.addComponent(row, 1, new Label(
					Util.formatDouble(curr.getAlt().length() == 0 ?
						Double.parseDouble(curr.getPres()) : Util.convertToMb(curr.getAlt()), 2) + " mb")).setPaddings(ClassMeteoAppTheme.getCurrentCondPaddings());
				
				row = ftl.addRow();
				ftl.addComponent(row, 0, new Label(tr.getProperty("forecast.current.humid"))).setPaddings(ClassMeteoAppTheme.getCurrentCondPaddings());
				ftl.addComponent(row, 1, new Label(curr.getRH() + "%")).setPaddings(ClassMeteoAppTheme.getCurrentCondPaddings());
				
				row = ftl.addRow();
				ftl.addComponent(row, 0, new Label(tr.getProperty("forecast.current.visibility"))).setPaddings(ClassMeteoAppTheme.getCurrentCondPaddings());
				ftl.addComponent(row, 1, new Label(curr.getVisK() + " Km")).setPaddings(ClassMeteoAppTheme.getCurrentCondPaddings());
				
				ftl.endBulkUpdate();
				CurrentCondDialog.this.append(ftl);
				
				lastShownCityId = Settings.getActualLocation();
				
				CurrentCondDialog.this.invalidate();
				CurrentCondDialog.this.repaint();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
