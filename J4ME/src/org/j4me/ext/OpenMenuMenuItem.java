package org.j4me.ext;

import org.j4me.ui.DeviceScreen;
import org.j4me.ui.MenuItem;

/**
 * @author Giuseppe Miscione
 *
 */
public class OpenMenuMenuItem implements MenuItem {

	private String text;
	private DeviceScreen screen;
	
	public OpenMenuMenuItem(String text, DeviceScreen screen) {
		this.text = text;
		this.screen = screen;
	}

	/* (non-Javadoc)
	 * @see org.j4me.ui.MenuItem#getText()
	 */
	public String getText() {
		return this.text;
	}

	/* (non-Javadoc)
	 * @see org.j4me.ui.MenuItem#onSelection()
	 */
	public void onSelection() {
		// show new menu
		screen.show();
	}

}