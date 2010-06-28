package it.virgilio.guidatv.midlet;

import it.virgilio.guidatv.menu.MainMenu;
import it.virgilio.guidatv.theme.VirgilioTheme;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.j4me.ui.Menu;
import org.j4me.ui.UIManager;

public class GuidaTV extends MIDlet {

	public GuidaTV() {
		
	}

	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {

	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		// Initialize the J4ME UI manager.
		UIManager.init( this );
		
		UIManager.setTheme(new VirgilioTheme());
		
		// show main menu and start application
		Menu mainMenu = new MainMenu();
		mainMenu.show();
	}

}
