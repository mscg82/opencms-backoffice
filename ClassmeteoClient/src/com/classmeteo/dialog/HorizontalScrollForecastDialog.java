package com.classmeteo.dialog;

import org.j4me.ui.Dialog;

public abstract class HorizontalScrollForecastDialog extends GenericForecastDialog {

	public abstract void scrollRigth();
	public abstract void scrollLeft();


	protected void keyPressed(int keyCode) {
		super.keyPressed(keyCode);
		switch(keyCode) {
			case Dialog.LEFT:
				scrollLeft();
				break;
			case Dialog.RIGHT:
				scrollRigth();
				break;
			default:
		}
		
	}

}
