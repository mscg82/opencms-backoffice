/**
 * 
 */
package org.j4me.ext;

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
