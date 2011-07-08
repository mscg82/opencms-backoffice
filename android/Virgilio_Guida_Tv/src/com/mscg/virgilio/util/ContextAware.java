package com.mscg.virgilio.util;

import android.content.Context;

public abstract class ContextAware {

    protected Context context;

    public ContextAware(Context context) {
        this.context = context;
    }

}
