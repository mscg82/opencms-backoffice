package com.classmeteo.dialog;

import java.rmi.RemoteException;

import org.j4me.ext.UpdatablePopupMenuDialog;
import org.j4me.ui.DeviceScreen;
import org.j4me.ui.components.MenuOption;
import org.j4me.ui.components.ProgressBar;

import com.classmeteo.data.Settings;
import com.classmeteo.items.RetrievedCityMenuItem;
import com.classmeteo.ws.ClassMeteoFluxesWS;
import com.classmeteo.ws.ClassMeteoFluxesWS_Stub;
import com.classmeteo.ws.MasterLocList;
import com.classmeteo.ws.MasterLocWS;
import com.mscg.util.Properties;

public class SearchCityDialog extends UpdatablePopupMenuDialog {
	
	private class CitiesSearchThread extends Thread {

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			try {
				ClassMeteoFluxesWS client = new ClassMeteoFluxesWS_Stub();
				MasterLocList locsList = client.getMasterLocByNameLike(Settings.getSelectCityDialog().getSelectedCityName(), new Integer(15));
				synchronized (spinner) {
					spinner.setMaxValue(1);					
					SearchCityDialog.this.deleteAll();
				}
				//SearchCityDialog.this.append(Settings.getWhiteSpace());
				MasterLocWS locs[] = locsList.getLocations();
				for(int i = 0; i < locs.length; i++) {
					MasterLocWS loc = locs[i];
					String name = loc.getPrsntNm() + ", " +
						((loc.getStCd() == null || loc.getStCd().trim().length() == 0 || "*".equals(loc.getStCd())) ? "" : loc.getStCd() + ", ") +
						loc.getCntryCd(); 
					SearchCityDialog.this.append(new MenuOption(new RetrievedCityMenuItem(name, loc.getLocId())));
				}
				SearchCityDialog.this.invalidate();
				SearchCityDialog.this.repaint();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
	}
	
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
		
		Properties tr = Settings.getTranslation();
		setMenuText(tr.getProperty("menu.prev"), tr.getProperty("menu.select"));
		
		deleteAll();
		
		spinner.setMaxValue(0);
		//append(Settings.getWhiteSpace());
		append(spinner);
		
		new CitiesSearchThread().start();
		
	}
	
}