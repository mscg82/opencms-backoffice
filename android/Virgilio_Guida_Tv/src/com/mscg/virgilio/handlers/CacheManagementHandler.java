package com.mscg.virgilio.handlers;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mscg.virgilio.GenericActivity;
import com.mscg.virgilio.R;
import com.mscg.virgilio.VirgilioGuidaTvCacheManagement;

public class CacheManagementHandler extends Handler {

    public static final String TYPE = CacheManagementHandler.class.getCanonicalName() + ".type";
    public static final String OPERATION = CacheManagementHandler.class.getCanonicalName() + ".operation";

    public static final int REMOVE_START = 0;
    public static final int UPDATE_COUNTS = 1;
    public static final int REMOVE_END = 2;

    public static final int GOTO_HOME = 0;

    private VirgilioGuidaTvCacheManagement context;
    private ProgressDialog dialog;

    public CacheManagementHandler(VirgilioGuidaTvCacheManagement context) {
        super();
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        Bundle b = msg.getData();
        int caseValue = b.getInt(TYPE);

        switch (caseValue) {
        case REMOVE_START:
            if (dialog != null)
                dialog.dismiss();
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.setMessage(context.getString(R.string.deleting_data));
            dialog.show();
            break;
        case UPDATE_COUNTS:
            context.updateCounts();
            break;
        case REMOVE_END:
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            break;
        }

        int operation = b.getInt(OPERATION, -1);
        switch (operation) {
        case GOTO_HOME:
            context.startActivity(GenericActivity.getHomeIntent(context));
            break;
        }
    }
}
