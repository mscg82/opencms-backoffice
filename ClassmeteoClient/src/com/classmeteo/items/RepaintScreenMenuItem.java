package com.classmeteo.items;

import org.j4me.ext.UpdatablePopupMenuDialog;

public class RepaintScreenMenuItem extends GenericMenuItem {

	protected UpdatablePopupMenuDialog screen;
	
	public RepaintScreenMenuItem(String text, UpdatablePopupMenuDialog screen) {
		super(text);
		this.screen = screen;
	}

	public void onSelection() {
		this.screen.updateComponents();
	}

}
