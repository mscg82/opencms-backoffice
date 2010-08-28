/**
 * 
 */
package org.j4me.ext;

import org.j4me.ui.DeviceScreen;
import org.j4me.ui.Menu;
import org.j4me.ui.MenuItem;
import org.j4me.ui.UIManager;
import org.j4me.ui.components.Component;
import org.j4me.ui.components.MenuOption;

/**
 * The <code>PopupMenu</code> class is used for creating the application's
 * menus.
 * <p>
 * J2ME devices have small screens and are not all very responsive to scrolling.
 * However, they do have keypads. The <code>Menu</code> class respects this and
 * limits menus to a total of 9 possible choices (1-9) plus "Exit". Usually all
 * the choices can be seen on a single screen and selected with a single button.
 * <p>
 * This implementation allows to add more than one functionality to the soft
 * key.
 * <p>
 * Override this class to change how the menu is painted for your application.
 * 
 * @author Giuseppe Miscione
 * 
 */
public class PopupMenu extends PopupMenuDialog {

	/**
	 * The screen that invoked this one or <code>null</code> if there is no
	 * previous screen.
	 */
	protected DeviceScreen previous;
	
	protected MenuOption gotoPrevious;

	protected class DeclineMenuItem implements MenuItem {
		public String getText() {
			return UIManager.getTheme().getMenuTextForCancel();
		}

		public void onSelection() {
			if(previous != null) {
				previous.show();
			}
		}
	}

	protected class AcceptMenuItem implements MenuItem {
		public String getText() {
			return UIManager.getTheme().getMenuTextForOK();
		}

		public void onSelection() {
			// Go to the highlighted screen.
			int highlighted = PopupMenu.this.getSelected();
			PopupMenu.this.selection( highlighted );
		}
	}

	/**
	 * Constructs a menu.
	 */
	public PopupMenu() {
		this(null, null);
	}

	/**
	 * Constructs a menu.
	 * 
	 * @param name
	 *            is the title for this menu, for example "Main Menu". It
	 *            appears at the top of the screen in the title area.
	 */
	public PopupMenu(String name) {
		this(name, null);
	}

	/**
	 * Constructs a menu.
	 * 
	 * @param name
	 *            is the title for this menu, for example "Main Menu". It
	 *            appears at the top of the screen in the title area.
	 * @param previous
	 *            is the screen to return to if the user cancels this.
	 */
	public PopupMenu(String name, DeviceScreen previous) {
		super();

		// No spacing between components.
		//   The MenuOption component will add spacing for us.
		setSpacing( 0 );
		
		this.previous = previous;

		setTitle(name);

		gotoPrevious = new MenuOption(new DeclineMenuItem());
		addLeftItem(gotoPrevious);
		gotoPrevious.visible(false);
		addRightItem(new MenuOption(new AcceptMenuItem()));

		setPrevious(previous);
		
		setDefaultMenuText();
	}

	/**
	 * Sets the screen to return to if the user cancels this menu. If
	 * <code>previous</code> is <code>null</code>, there will be no "Cancel"
	 * button.
	 * 
	 * @param previous
	 *            is the screen to go to if the user presses "Cancel".
	 */
	public void setPrevious(DeviceScreen previous) {
		// Record the previous screen.
		this.previous = previous;

		gotoPrevious.visible(this.previous != null);
		
		if(getLeftMenuItems().size() == 1) {
			String rightMenuText = getRightMenuText();
			setMenuText(
				gotoPrevious.isShown() ? gotoPrevious.getLabel() : null,
				rightMenuText);
		}
	}

	/**
	 * Appends a new menu option to this menu.
	 * 
	 * @param option
	 *            is the menu item to add.
	 * @return The {@link MenuOption} object representing the added item.
	 */
	public MenuOption appendMenuOption(MenuItem option) {
		MenuOption choice = new MenuOption(option);
		append(choice);
		return choice;
	}

	/**
	 * Appends a screen as a menu option. If selected the screen will be shown.
	 * The screen's title is used as its text.
	 * 
	 * @param option
	 *            is screen to add as a menu item.
	 * @return The {@link MenuOption} object representing the added item.
	 */
	public MenuOption appendMenuOption(DeviceScreen option) {
		MenuOption choice = new MenuOption(option);
		append(choice);
		return choice;
	}

	/**
	 * Appends a screen as a menu option. If selected the screen will be shown.
	 * 
	 * @param text
	 *            is string that appears in the menu option.
	 * @param option
	 *            is screen to add as a menu item.
	 * @return The {@link MenuOption} object representing the added item.
	 */
	public MenuOption appendMenuOption(String text, DeviceScreen option) {
		MenuOption choice = new MenuOption(text, option);
		append(choice);
		return choice;
	}

	/**
	 * Appends another menu as a menu option. The submenu will have an arrow
	 * next to it to indicate to the user it is another menu.
	 * <p>
	 * To use a <code>Menu</code> as a screen and not a submenu call the
	 * <code>appendMenuOption</code> method instead.
	 * 
	 * @param submenu
	 *            is the screen to add as a menu item.
	 * @return The {@link MenuOption} object representing the added item.
	 */
	public MenuOption appendSubmenu(PopupMenu submenu) {
		MenuOption choice = new MenuOption(submenu, true);
		append(choice);
		return choice;
	}
	
	/**
	 * Appends another menu as a menu option. The submenu will have an arrow
	 * next to it to indicate to the user it is another menu.
	 * <p>
	 * To use a <code>Menu</code> as a screen and not a submenu call the
	 * <code>appendMenuOption</code> method instead.
	 * 
	 * @param submenu
	 *            is the screen to add as a menu item.
	 * @return The {@link MenuOption} object representing the added item.
	 */
	public MenuOption appendSubmenu(Menu submenu) {
		MenuOption choice = new MenuOption(submenu, true);
		append(choice);
		return choice;
	}

	/**
	 * Appends another menu as a menu option. The submenu will have an arrow
	 * next to it to indicate to the user it is another menu.
	 * <p>
	 * To use a <code>Menu</code> as a screen and not a submenu call the
	 * <code>appendMenuOption</code> method instead.
	 * 
	 * @param text
	 *            is string that appears in the menu option.
	 * @param submenu
	 *            is the screen to add as a menu item.
	 * @return The {@link MenuOption} object representing the added item.
	 */
	public MenuOption appendSubmenu(String text, Menu submenu) {
		MenuOption choice = new MenuOption(text, submenu, true);
		append(choice);
		return choice;
	}
	
	/**
	 * Appends another menu as a menu option. The submenu will have an arrow
	 * next to it to indicate to the user it is another menu.
	 * <p>
	 * To use a <code>Menu</code> as a screen and not a submenu call the
	 * <code>appendMenuOption</code> method instead.
	 * 
	 * @param text
	 *            is string that appears in the menu option.
	 * @param submenu
	 *            is the screen to add as a menu item.
	 * @return The {@link MenuOption} object representing the added item.
	 */
	public MenuOption appendSubmenu(String text, PopupMenu submenu) {
		MenuOption choice = new MenuOption(text, submenu, true);
		append(choice);
		return choice;
	}

	/**
	 * Selects a menu item.
	 * 
	 * @param selection
	 *            is the index of <code>choice</code> that is selected.
	 */
	private void selection(int selection) {
		Component component = get(selection);

		if (component instanceof MenuOption) {
			MenuOption chosen = (MenuOption) component;

			// Record this as the selection.
			setSelected(selection);

			// Perform the selection operation.
			chosen.select();
		}
	}

}
