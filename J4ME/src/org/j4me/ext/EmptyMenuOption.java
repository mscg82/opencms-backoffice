/**
 * 
 */
package org.j4me.ext;

import org.j4me.ui.MenuItem;
import org.j4me.ui.components.MenuOption;

/**
 * This class creates an empty menu item that doesn't accept inputs.
 * It must be used to create blank spaces between items.
 * 
 * @author Giuseppe Miscione
 *
 */
public class EmptyMenuOption extends MenuOption {

	public EmptyMenuOption() {
		super(new MenuItem() {
			
			public void onSelection() { /* Do nothing */ }
			
			public String getText() {
				return "";
			}
		});
	}

	public boolean acceptsInput() {
		return false;
	}

}
