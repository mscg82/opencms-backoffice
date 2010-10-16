package com.classmeteo.items;

import com.classmeteo.data.InterruptibleThread;

public class StopThreadMenuItem extends GenericMenuItem {

	private InterruptibleThread thread;
	
	public StopThreadMenuItem(String text, InterruptibleThread thread) {
		super(text);
		this.thread = thread;
	}

	public void onSelection() {
		thread.setInterrupted(true);
	}

}
