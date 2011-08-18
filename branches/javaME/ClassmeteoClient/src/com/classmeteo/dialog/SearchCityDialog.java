package com.classmeteo.dialog;

import java.io.IOException;

import org.j4me.ext.UpdatablePopupMenuDialog;
import org.j4me.ui.DeviceScreen;
import org.j4me.ui.components.Label;
import org.j4me.ui.components.MenuOption;
import org.j4me.ui.components.ProgressBar;

import com.classmeteo.data.InterruptibleThread;
import com.classmeteo.data.Settings;
import com.classmeteo.data.WebServiceAccessor;
import com.classmeteo.items.RetrievedCityMenuItem;
import com.classmeteo.items.StopThreadMenuItem;
import com.classmeteo.ws.ClassMeteoFluxesWS;
import com.classmeteo.ws.MasterLocList;
import com.classmeteo.ws.MasterLocWS;
import com.mscg.util.Properties;

public class SearchCityDialog extends UpdatablePopupMenuDialog {
	
	private class CitiesSearchThread extends InterruptibleThread {

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			Properties tr = Settings.getTranslation(); 		
			try {
				ClassMeteoFluxesWS client = WebServiceAccessor.getClient();
				MasterLocList locsList = client.getMasterLocByNameLike(Settings.getSelectCityDialog().getSelectedCityName(), new Integer(15));
				synchronized (spinner) {
					deleteAll();
					spinner.setMaxValue(1);
					repaint();
				}
				if(!isInterrupted()) {
					//SearchCityDialog.this.append(Settings.getWhiteSpace());
					MasterLocWS locs[] = locsList.getLocations();
					if(locs != null && locs.length != 0) {
						for(int i = 0; i < locs.length; i++) {
							MasterLocWS loc = locs[i];
							String name = loc.getPrsntNm() + ", " +
								((loc.getStCd() == null || loc.getStCd().trim().length() == 0 || "*".equals(loc.getStCd())) ? "" : loc.getStCd() + ", ") +
								loc.getCntryCd(); 
							MenuOption mo = new MenuOption(new RetrievedCityMenuItem(name, loc.getLocId()));
							SearchCityDialog.this.append(mo);
							if(i == 0)
								SearchCityDialog.this.setSelected(mo);
						}
						getLeftMenuItems().removeAllElements();
						getRightMenuItems().removeAllElements();
									
						setMenuText(tr.getProperty("menu.prev"), tr.getProperty("menu.select"));
					}
					else {
						SearchCityDialog.this.append(Settings.getLogo());
						SearchCityDialog.this.append(new Label(tr.getProperty("citysearch.list.empty")));
					}
					SearchCityDialog.this.invalidate();
					SearchCityDialog.this.repaint();
				}
				else {
					SearchCityDialog.this.deleteAll();
				}
			} catch(IOException e) {
				e.printStackTrace();
				synchronized (spinner) {
					deleteAll();
					spinner.setMaxValue(1);
					repaint();
				}
				append(new Label(tr.getProperty("error.network")));
			} catch(Exception e) {
				e.printStackTrace();
				synchronized (spinner) {
					deleteAll();
					spinner.setMaxValue(1);
					repaint();
				}
				append(new Label(tr.getProperty("error.generic")));
			}
		}
		
	}
	
	private InterruptibleThread updateThread;	
	private ProgressBar spinner;
	private DeviceScreen previous;

	public SearchCityDialog() {
		Properties tr = Settings.getTranslation();
		setTitle(tr.getProperty("searchcity.title"));
		
		spinner = new ProgressBar();
	}
	
	/* (non-Javadoc)
	 * @see org.j4me.ext.PopupMenuDialog#declineNotify()
	 */
	protected void declineNotify() {
		if(this.previous != null) {
			this.previous.show();
		}
	}

	/**
	 * @return the previous
	 */
	public DeviceScreen getPrevious() {
		return previous;
	}

	/**
	 * @param previous the previous to set
	 */
	public void setPrevious(DeviceScreen previous) {
		this.previous = previous;
	}

	/* (non-Javadoc)
	 * @see org.j4me.ui.DeviceScreen#showNotify()
	 */
	public void showNotify() {
		super.showNotify();
		
		deleteAll();
		
		spinner.setMaxValue(0);
		//append(Settings.getWhiteSpace());
		append(Settings.getLogo());
		append(spinner);
		
		updateThread = new CitiesSearchThread();
		
		Properties tr = Settings.getTranslation();
		addLeftItem(new MenuOption(new StopThreadMenuItem(tr.getProperty("menu.prev"), updateThread)));
		
		setDefaultMenuText();
		
		updateThread.start();
		
	}
	
}