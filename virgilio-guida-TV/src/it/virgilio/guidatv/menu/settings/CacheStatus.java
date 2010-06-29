/**
 * 
 */
package it.virgilio.guidatv.menu.settings;

import it.virgilio.guidatv.menu.BaseMenu;
import it.virgilio.guidatv.programs.ProgramsManager;
import it.virgilio.guidatv.theme.VirgilioTheme;
import it.virgilio.guidatv.util.Util;

import javax.microedition.lcdui.Graphics;

import org.j4me.ui.DeviceScreen;
import org.j4me.ui.MenuItem;
import org.j4me.ui.UIManager;
import org.j4me.ui.components.HorizontalRule;
import org.j4me.ui.components.Label;
import org.j4me.ui.components.MenuOption;
import org.j4me.ui.components.Whitespace;

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
			
			append(new Whitespace(10));
			
			HorizontalRule hr = new HorizontalRule();
			hr.setHeight(3);
			hr.setHorizontalAlignment(Graphics.HCENTER);
			append(hr);
			
			append(new Whitespace(10));
			
			MenuOption fmc = appendMenuOption(new FreeMemoryCache());	
			appendMenuOption(new FreeDiskCache());
			
			hasVerticalScrollbar();
			
			setSelected(fmc, true);			
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

		public String getText() {
			VirgilioTheme vt = (VirgilioTheme)UIManager.getTheme();
			return vt.getDeleteItemsInMemoryCache();
		}

		public void onSelection() {
			ProgramsManager.getInstance().cleanMemoryCache();
			VirgilioTheme vt = (VirgilioTheme)UIManager.getTheme();
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

		public String getText() {
			VirgilioTheme vt = (VirgilioTheme)UIManager.getTheme();
			return vt.getDeleteItemsInDiskCache();
		}

		public void onSelection() {
			
		}
	}
}
