/**
 * 
 */
package it.virgilio.guidatv.menu;

import org.j4me.ui.DeviceScreen;
import org.j4me.ui.Menu;
import org.j4me.ui.components.Component;
import org.j4me.ui.components.MenuOption;

/**
 * This class is the superclass of all
 * application menus. This class adds
 * the functionality to open a submenu
 * using the right arrow key.
 * 
 * @author Giuseppe Miscione
 *
 */
public abstract class BaseMenu extends Menu {
	
	protected boolean visible;
	protected boolean preventKeyPropagation;
	protected boolean drawInterface;

	/**
	 * Constructs a menu
	 */
	public BaseMenu() {
		visible = false;
		preventKeyPropagation = false;
		drawInterface = true;
	}

	/**
	 * Constructs a menu.
	 * 
	 * @param name is the title for this menu, for example "Main Menu".  It
	 *  appears at the top of the screen in the title area.
	 * @param previous is the screen to return to if the user cancels this.
	 */
	public BaseMenu(String name, DeviceScreen previous) {
		super(name, previous);
		visible = false;
		preventKeyPropagation = false;
		drawInterface = true;
	}

	protected void keyPressed(int key) {
		boolean propagate = true;
		if(key == Menu.RIGHT) {
			Component component = get(getSelected());
			
			if (component instanceof MenuOption) {
				MenuOption menu = (MenuOption) component;
				if(menu.isSubmenu()) {
					propagate = false;
					menu.select();
				}
			}
		}
		
		if(propagate && !preventKeyPropagation) {
			super.keyPressed(key);
		}
	}
	
	public void showNotify() {
		visible = true;
	}
	
	public void hideNotify() {
		visible = false;
	}

}
