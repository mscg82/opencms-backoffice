/**
 * 
 */
package it.virgilio.guidatv.menu;

import org.j4me.ui.DeviceScreen;

/**
 * @author Giuseppe Miscione
 *
 */
public class SettingsMenu extends BaseMenu {

	/**
	 * Constructs a settings menu.
	 */
	public SettingsMenu() {
		super();
	}
	
	/**
	 * Constructs a settings menu.
	 * 
	 * @param previous is the screen to return to if the user cancels this.
	 */
	public SettingsMenu(DeviceScreen previous) {
		super("Impostazioni", previous);
	}

	/**
	 * Constructs a settings menu.
	 * 
	 * @param name is the title for this menu. It appears at the top of the screen in the title area.
	 * @param previous is the screen to return to if the user cancels this.
	 */
	public SettingsMenu(String name, DeviceScreen previous) {
		super(name, previous);
	}

}
