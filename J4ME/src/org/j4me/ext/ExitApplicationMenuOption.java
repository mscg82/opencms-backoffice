/**
 * 
 */
package org.j4me.ext;

import org.j4me.ui.MenuItem;
import org.j4me.ui.UIManager;
import org.j4me.ui.components.MenuOption;

/**
 * This class creates a menu item that causes midlet
 * to terminate execution. The text that will appear
 * on the item can be customized.
 * 
 * @author Giuseppe Miscione
 *
 */
public class ExitApplicationMenuOption extends MenuOption {
	
	/**
	 * @author Giuseppe Miscione
	 *
	 */
	public static class ExitApplicationMenuItem implements MenuItem {

		private String text;
		
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
	
	/**
	 * Creates an exit item with the default text &quot;Exit&quot;.
	 */
	public ExitApplicationMenuOption() {
		this("Exit");
	}

	/**
	 * Creates an exit item with customized text.
	 * 
	 * @param text The text that will appear on the item.
	 */
	public ExitApplicationMenuOption(String text) {
		super(new ExitApplicationMenuItem(text));
	}
}
