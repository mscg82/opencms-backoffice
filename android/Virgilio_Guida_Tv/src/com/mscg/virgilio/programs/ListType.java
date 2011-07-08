package com.mscg.virgilio.programs;

import android.app.Activity;

public class ListType {

    private String listItemName;
    private Class<? extends Activity> listItemActivity;

    public ListType(String listItemName, Class<? extends Activity> listItemActivity) {
        this.listItemName = listItemName;
        this.listItemActivity = listItemActivity;
    }

    public String getListItemName() {
        return listItemName;
    }

    public Class<? extends Activity> getListItemActivity() {
        return listItemActivity;
    }

}
