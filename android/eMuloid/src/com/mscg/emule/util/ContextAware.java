package com.mscg.emule.util;

import android.content.Context;
import android.os.Handler;

public abstract class ContextAware<T extends Context> extends HandlerAware {

	protected T context;

	public ContextAware(Handler handler) {
		this(handler, null);
	}

	public ContextAware(T context) {
		this(null, context);
	}

	public ContextAware(Handler handler, T context) {
		super(handler);
		setContext(context);
	}

	public T getContext() {
		return context;
	}

	public void setContext(T context) {
		this.context = context;
	}
}
