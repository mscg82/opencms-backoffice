package com.mscg.virgilio.handlers;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mscg.virgilio.GenericActivity;
import com.mscg.virgilio.R;
import com.mscg.virgilio.VirgilioGuidaTvChannelSelection;
import com.mscg.virgilio.programs.Programs;

public class DownloadProgressHandler extends Handler {

    public static final String TYPE = DownloadProgressHandler.class.getCanonicalName() + ".type";
    public static final String PROGRAMS = DownloadProgressHandler.class.getCanonicalName() + ".programs";
    public static final String MESSAGE = DownloadProgressHandler.class.getCanonicalName() + ".message";

    public static final int START_DOWNLOAD = 0;
    public static final int UPDATE_PROGRESS = 1;
    public static final int END_DOWNLOAD = 2;
    public static final int END_PARSE = 3;
    public static final int START_LOAD = 4;
    public static final int END_SAVE = 5;
    public static final int ERROR = 6;
    public static final int START_DELETE = 7;
    public static final int DELETE_PROGRESS = 8;
    public static final int END_DELETE = 9;

    private GenericActivity context;

    public DownloadProgressHandler(GenericActivity context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        String message = null;
        Bundle b = msg.getData();
        int caseValue = b.getInt(TYPE);
        switch (caseValue) {
        case UPDATE_PROGRESS:
            if (context.getProgressDialog() == null || context.getProgressDialog().isIndeterminate()) {
                if (context.getProgressDialog() != null)
                    context.getProgressDialog().dismiss();
                context.showDownloadDialog(UPDATE_PROGRESS);
                context.getProgressDialog().setProgress(0);
            }
            int position = b.getInt("progress");
            context.getProgressDialog().setProgress(position);
            break;
        case END_DOWNLOAD:
            break;
        case START_DOWNLOAD:
        case START_DELETE:
        case END_PARSE:
            if (context.getProgressDialog() != null)
                context.getProgressDialog().dismiss();
            context.showDownloadDialog(caseValue);
            break;
        case END_SAVE:
            if (context.getProgressDialog() != null)
                context.getProgressDialog().dismiss();
            Intent intent = new Intent(context, VirgilioGuidaTvChannelSelection.class);
            intent.putExtra(PROGRAMS, ((Programs) b.getSerializable(PROGRAMS)).getDate());
            context.startActivity(intent);
            break;
        case ERROR:
            if (context.getProgressDialog() != null)
                context.getProgressDialog().dismiss();
            AlertDialog.Builder ad = new AlertDialog.Builder(context);
            ad.setTitle(context.getString(R.string.error_title));
            ad.setMessage(context.getString(R.string.error_text) + " " + b.getString(MESSAGE));
            ad.setCancelable(true);
            ad.show();
            break;
        case DELETE_PROGRESS:
            message = context.getString(R.string.db_analyze_delete_progress);
            context.getProgressDialog().setMessage(message.replace("${item}", "" + msg.arg1).replace("${total}", "" + msg.arg2));
            break;
        case END_DELETE:
            if (context.getProgressDialog() != null)
                context.getProgressDialog().dismiss();
            context.finish();
            break;
        }
    }

}
