package org.j4me.ext;

import org.j4me.ui.MenuItem;
import org.j4me.ui.UIManager;

/**
 * @author Giuseppe Miscione
 *
 */
public class ExitApplicationMenuItem implements MenuItem {

	private String text;

	public ExitApplicationMenuItem() {
		this(null);
	}
	
	public ExitApplicationMenuItem(String text) {
		this.text = text;
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
		// exit from the application
		UIManager.getMidlet().notifyDestroyed();
	}

}