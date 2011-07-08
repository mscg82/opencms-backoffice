package com.mscg.virgilio.util;

import android.content.Context;
import android.os.Handler;

public class ContextAndHandlerAware extends ContextAware {

    protected Handler guiHandler;

    public ContextAndHandlerAware(Context context, Handler guiHandler) {
        super(context);
        this.guiHandler = guiHandler;
    }

}
