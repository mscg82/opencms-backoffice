package org.j4me.ext;

import org.j4me.ui.DeviceScreen;
import org.j4me.ui.components.MenuOption;

/**
 * This class creates a menu item that will show a new menu.
 * The text that will appear
 * on the item can be customized.
 * 
 * @author Giuseppe Miscione
 *
 */
public class OpenMenuMenuOption extends MenuOption {
	
	/**
	 * Creates an open menu item using the menu title as text.
	 * @param screen The menu to display.
	 */
	public OpenMenuMenuOption(DeviceScreen screen) {
		super(new OpenMenuMenuItem(screen.getTitle(), screen));
	}
	
	/**
	 * Creates an open menu item with customized text.
	 * 
	 * @param text The text that will appear on the item.
	 * @param screen The menu to display.
	 */
	public OpenMenuMenuOption(String text, DeviceScreen screen) {
		super(new OpenMenuMenuItem(text, screen));
	}
}
