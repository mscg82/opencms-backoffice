/**
 * 
 */
package it.virgilio.guidatv.menu;

import it.virgilio.guidatv.util.Constants;

import java.util.Calendar;

import org.j4me.ui.DeviceScreen;

/**
 * @author Giuseppe Miscione
 *
 */
public class WeekDaySelectionMenu extends BaseMenu {

	/**
	 * Constructs a week day menu.
	 */
	public WeekDaySelectionMenu() {
		super();
	}
	
	/**
	 * Constructs a week day menu.
	 * 
	 * @param previous is the screen to return to if the user cancels this.
	 */
	public WeekDaySelectionMenu(DeviceScreen previous) {
		super("Seleziona un giorno", previous);
		
		Calendar now = Calendar.getInstance();
		int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
		
		int selectedDay = 0;
		// Add a menu item for each day
		for(int i = 0; i < Constants.daysNames.length; i++) {
			this.appendSubmenu(new WeekDayMenu(
				Constants.daysNames[i], Constants.dayFileNames[i],
				Constants.dayNumbers[i], this));
			
			if(dayOfWeek == Constants.dayNumbers[i])
				selectedDay = i;
		}
		
		// Select the item corresponding to actual day
		setSelected(selectedDay);
	}

	/**
	 * Constructs a week day menu.
	 * 
	 * @param name is the title for this menu. It appears at the top of the screen in the title area.
	 * @param previous is the screen to return to if the user cancels this.
	 */
	public WeekDaySelectionMenu(String name, DeviceScreen previous) {
		super(name, previous);
	}

}
