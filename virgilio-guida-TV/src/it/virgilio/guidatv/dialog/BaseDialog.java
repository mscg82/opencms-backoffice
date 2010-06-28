/**
 * 
 */
package it.virgilio.guidatv.dialog;

import org.j4me.ui.DeviceScreen;
import org.j4me.ui.Dialog;

/**
 * This class is the super class for all
 * application dialogs.
 * 
 * @author Giuseppe Miscione
 *
 */
public abstract class BaseDialog extends Dialog {

	/**
	 * The screen to return to once the user is done with this one.
	 */
	protected final DeviceScreen parent;
	
	/**
	 * Create a new base dialog
	 */
	public BaseDialog() {
		super();
		parent = null;
	}
	
	/**
	 * Create a new base dialog.
	 * 
	 * @param parent is the screen to return to when the user
	 *  hits the "Back" menu button.
	 */
	public BaseDialog (DeviceScreen parent) {
		this.parent = parent;
	}
	
	/**
	 * Called when the user presses the left menu button.
	 * This goes back to the previous screen.
	 * 
	 * @see DeviceScreen#declineNotify()
	 */
	protected void declineNotify () {
		if(parent != null) {
			parent.show();
		}
	}

}
