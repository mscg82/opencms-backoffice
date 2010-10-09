package com.classmeteo.midlet;

import java.io.InputStream;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.j4me.ui.Dialog;
import org.j4me.ui.Theme;
import org.j4me.ui.UIManager;

import com.classmeteo.data.Settings;
import com.classmeteo.theme.ClassMeteoAppTheme;
import com.mscg.util.Properties;

public class ClassmeteoMidlet extends MIDlet {

	public ClassmeteoMidlet() {
		
	}

	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		Settings.close();
	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		Theme theme = new ClassMeteoAppTheme();
		
		InputStream is = null;
		try {
			is = ClassmeteoMidlet.class.getResourceAsStream("/translation-it.properties");
			Settings.init(new Properties(is), "savedLocations");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch(Exception e){}
		}
		
		// Initialize the J4ME UI manager.
		UIManager.init( this );
		
		UIManager.setTheme(theme);
		
		Dialog main = Settings.getSelectCityDialog();
		main.show();
	}

}
