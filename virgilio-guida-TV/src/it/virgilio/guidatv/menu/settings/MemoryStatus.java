/**
 * 
 */
package it.virgilio.guidatv.menu.settings;

import it.virgilio.guidatv.menu.BaseMenu;
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
public class MemoryStatus extends BaseMenu {
	
	private Label totalMemoryLabel;
	private Label freeMemoryLabel;

	public MemoryStatus(DeviceScreen previous) {
		super(
			((VirgilioTheme)UIManager.getTheme()).getMemoryStatusTitle(),
			previous);
	}

	public void showNotify() {
		super.showNotify();
		VirgilioTheme vt = (VirgilioTheme)UIManager.getTheme();
		if(drawInterface) {
			// lazily draw the menu interface the
			// first time the menu is shown
			drawInterface = false;

        	totalMemoryLabel = new Label("test");
        	append(totalMemoryLabel);

        	freeMemoryLabel = new Label("test");
        	append(freeMemoryLabel);

        	insertLeftItem(new MenuOption(new CleanMemoryMenuItem()), 0);
        	insertLeftItem(new EmptyMenuOption(), 1);
        	
			setMenuText(vt.getLeftSoftButtonText(), null);
		}
		updateLabels(vt);
	}
	
	private void updateLabels(VirgilioTheme vt) {
		long tmp = Runtime.getRuntime().totalMemory();
        double tmp2 = tmp / (1024.0d * 1024.0d);
        double memory = Util.round(tmp2);
           
        totalMemoryLabel.setLabel(
        	Util.replace(vt.getMemoryStatusTotalMemory(), "${mem}", Double.toString(memory)));
        
        tmp = Runtime.getRuntime().freeMemory();
        tmp2 = tmp / (1024.0d * 1024.0d);
        memory = Util.round(tmp2);
        freeMemoryLabel.setLabel(
        	Util.replace(vt.getMemoryStatusFreeMemory(), "${mem}", Double.toString(memory)));
        
        repaint();
	}
	
	private class CleanMemoryMenuItem implements MenuItem {
		private VirgilioTheme vt;
		
		public CleanMemoryMenuItem() {
			vt = (VirgilioTheme)UIManager.getTheme();
		}

		public String getText() {
			return vt.getMemoryStatusFreeTextAction();
		}

		public void onSelection() {
			System.gc();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) { }
			updateLabels(vt);
		}
	}

}
