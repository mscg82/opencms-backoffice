/**
 * 
 */
package it.virgilio.guidatv.menu;

import it.virgilio.guidatv.theme.VirgilioTheme;

import org.j4me.ui.Menu;
import org.j4me.ui.Theme;
import org.j4me.ui.UIManager;
import org.j4me.ui.components.Whitespace;

/**
 * @author Giuseppe Miscione
 *
 */
public class MainMenu extends BaseMenu {

	public MainMenu() {
		super();
		
		setTitle(((VirgilioTheme)UIManager.getTheme()).getMainMenuTitle());
		
		// Set the menu text.
		Theme theme = UIManager.getTheme();
		String leftMenuText = theme.getMenuTextForExit();
		String rightMenuText = theme.getMenuTextForOK();
		setMenuText( leftMenuText, rightMenuText );
		
		// Set menu voices
		Menu weekDays = new WeekDaySelectionMenu(this);
		this.appendSubmenu(weekDays);
		
		this.append(new Whitespace(5));
		
		Menu settings = new SettingsMenu(this);
		this.appendSubmenu(settings);
	}
	
	protected void declineNotify () {
		super.declineNotify();
		
		// exit from the application
		UIManager.getMidlet().notifyDestroyed();
	}
}
