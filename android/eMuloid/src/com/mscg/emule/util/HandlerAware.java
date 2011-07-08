package com.mscg.emule.util;

import android.os.Handler;

public abstract class HandlerAware {

	protected Handler handler;

	public HandlerAware(Handler handler) {
		super();
		setHandler(handler);
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}
