/**
 * 
 */
package it.virgilio.guidatv.menu;

import it.virgilio.guidatv.theme.VirgilioTheme;
import it.virgilio.guidatv.util.Constants;
import it.virgilio.guidatv.util.Util;

import java.util.Calendar;

import org.j4me.ui.DeviceScreen;
import org.j4me.ui.UIManager;

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
		super(((VirgilioTheme)UIManager.getTheme()).getDaySelectionTitle(), previous);
		
		Calendar now = Calendar.getInstance();
		int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
		
		int selectedDay = 0;
		for(int i = 0; i < Constants.dayNumbers.length; i++) {
			if(dayOfWeek == Constants.dayNumbers[i]) {
				selectedDay = i;
				break;
			}
		}
		
		// Add a menu item for each day
		for(int i = 0; i < Constants.daysNames.length; i++) {
			Calendar dayCal = Util.addTimeToDate(now, i - selectedDay, Calendar.DAY_OF_MONTH);
			int dayNum = dayCal.get(Calendar.DAY_OF_MONTH);
			int monNum = dayCal.get(Calendar.MONTH) + 1;
			
			String menuItemLabel = Constants.daysNames[i];
			menuItemLabel = Util.replace(menuItemLabel, "${day}", (dayNum < 10 ? "0" : "") + dayNum);			
			menuItemLabel = Util.replace(menuItemLabel, "${month}", (monNum < 10 ? "0" : "") + monNum);			
			
			this.appendSubmenu(menuItemLabel, new WeekDayMenu(
				dayNum, monNum,
				Constants.dayFileNames[i],
				Constants.dayNumbers[i], this));
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
