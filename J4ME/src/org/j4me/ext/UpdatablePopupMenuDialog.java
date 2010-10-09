package org.j4me.ext;

public class UpdatablePopupMenuDialog extends PopupMenuDialog {

	public void updateComponents() {
		// The default implementation does nothing, only reapints interface
		invalidate();
		repaint();
	}
	
}
