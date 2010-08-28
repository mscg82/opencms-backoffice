/**
 * 
 */
package it.virgilio.guidatv.menu.settings;

import it.virgilio.guidatv.menu.BaseMenu;
import it.virgilio.guidatv.programs.ProgramsManager;
import it.virgilio.guidatv.theme.VirgilioTheme;
import it.virgilio.guidatv.util.Util;

import org.j4me.ext.EmptyMenuOption;
import org.j4me.ui.DeviceScreen;
import org.j4me.ui.MenuItem;
import org.j4me.ui.UIManager;
import org.j4me.ui.components.Label;
import org.j4me.ui.components.MenuOption;

/**
 * @author Giuseppe Miscione
 *
 */
public class CacheStatus extends BaseMenu {

	private Label elementsInMemory;
	private Label elementsOnDisk;
	
	public CacheStatus(DeviceScreen previous) {
		super(
			((VirgilioTheme)UIManager.getTheme()).getCacheStatusTitle(), 
			previous);
	}
	
	public void showNotify() {
		super.showNotify();
		VirgilioTheme vt = (VirgilioTheme)UIManager.getTheme();
		if(drawInterface) {
			// lazily draw the menu interface the
			// first time the menu is shown
			drawInterface = false;
			
			elementsInMemory = new Label("test");
			append(elementsInMemory);
			
			elementsOnDisk = new Label("test");
			append(elementsOnDisk);
			
			insertLeftItem(new MenuOption(new FreeMemoryCache()), 0);
			insertLeftItem(new MenuOption(new FreeDiskCache()), 1);
			insertLeftItem(new EmptyMenuOption(), 2);
			
			setMenuText(vt.getLeftSoftButtonText(), null);
		}
		updateLabels(vt);
	}
	
	private void updateLabels(VirgilioTheme vt) {
		elementsInMemory.setLabel(
			Util.replace(vt.getItemsInMemoryCache(), "${count}",
				Integer.toString(ProgramsManager.getInstance().getProgramsInMemory())));
		
		elementsOnDisk.setLabel(
				Util.replace(vt.getItemsInDiskCache(), "${count}",
					Integer.toString(ProgramsManager.getInstance().getProgramsOnDisk())));
		
		repaint();
	}
	
	/**
	 * Private inner class to implement
	 * menu action to cleanup memory cache
	 * 
	 * @author Giuseppe Miscione
	 *
	 */
	private class FreeMemoryCache implements MenuItem {
		private VirgilioTheme vt;
		
		public FreeMemoryCache() {
			vt = (VirgilioTheme)UIManager.getTheme();
		}
		
		public String getText() {
			return vt.getDeleteItemsInMemoryCache();
		}

		public void onSelection() {
			ProgramsManager.getInstance().cleanMemoryCache();
			updateLabels(vt);
		}
	}
	
	/**
	 * Private inner class to implement
	 * menu action to cleanup disk cache
	 * 
	 * @author Giuseppe Miscione
	 *
	 */
	private class FreeDiskCache implements MenuItem {
		private VirgilioTheme vt;
		
		public FreeDiskCache() {
			vt = (VirgilioTheme)UIManager.getTheme();
		}
		
		public String getText() {
			return vt.getDeleteItemsInDiskCache();
		}

		public void onSelection() {
			
		}
	}
}
