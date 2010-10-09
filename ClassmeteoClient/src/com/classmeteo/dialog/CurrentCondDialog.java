/**
 * 
 */
package com.classmeteo.dialog;

import org.j4me.ext.ExitApplicationMenuOption;
import org.j4me.ext.PopupMenuDialog;

import com.classmeteo.data.Settings;
import com.mscg.util.Properties;

/**
 * @author Giuseppe Miscione
 *
 */
public class CurrentCondDialog extends PopupMenuDialog {

	public CurrentCondDialog() {
		Properties tr = Settings.getTranslation(); 
		
		setTitle(tr.getProperty("currentcond.title"));
		
		this.addLeftItem(new ExitApplicationMenuOption(tr.getProperty("menu.exit")));
		
		this.setMenuText(tr.getProperty("menu.actionbutton"), null);
	}
	
}
