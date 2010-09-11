/**
 * 
 */
package org.j4me.ext;

import org.j4me.ui.MenuItem;

/**
 * This class chains two menu items and executes
 * both of them when selected.
 * 
 * @author Giuseppe Miscione
 *
 */
public class ChainedMenuItem implements MenuItem {
	
	private MenuItem item1;
	private MenuItem item2;
	private String text;
	
	public ChainedMenuItem(MenuItem item1, MenuItem item2) {
		this(null, item1, item2);
	}
	
	public ChainedMenuItem(String text, MenuItem item1, MenuItem item2) {
		this.text = text;
		this.item1 = item1;
		this.item2 = item2;
	}

	/* (non-Javadoc)
	 * @see org.j4me.ui.MenuItem#getText()
	 */
	public String getText() {
		return (text != null ? text : (item1.getText() != null ? item1.getText() : item2.getText()));
	}

	/* (non-Javadoc)
	 * @see org.j4me.ui.MenuItem#onSelection()
	 */
	public void onSelection() {
		try {
			item1.onSelection();
		} catch(Exception e) {}
		try {
			item2.onSelection();
		} catch(Exception e) {}
	}

}
