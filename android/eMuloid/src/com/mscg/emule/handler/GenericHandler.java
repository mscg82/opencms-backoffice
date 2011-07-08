package com.mscg.emule.handler;

import java.util.Arrays;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;

import com.mscg.emule.R;
import com.mscg.emule.listener.DialogDismisserListener;
import com.mscg.emule.util.Constants;
import com.mscg.emule.util.Preferences;
import com.mscg.emule.util.Util;

public class GenericHandler extends Handler {

	protected Context context;

	protected String errorTitle;
	protected String errorText;
	protected String closeText;
	protected String contactingServer;
	protected String analyzingResponse;

	protected Dialog dialog;

	public GenericHandler(Context context) {
		super();
		this.context = context;

		errorTitle = context.getString(R.string.error_title);
		errorText = context.getString(R.string.error_text);
		closeText = context.getString(R.string.close);
		contactingServer = context.getString(R.string.contacting_server);
		analyzingResponse = context.getString(R.string.analyzing_response);

		dialog = null;
	}

	protected void dismissQuietly() {
		if(dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	@Override
	public void handleMessage(Message msg) {
		switch(msg.what) {
		case Constants.Messages.START_DOWNLOAD:
			dismissQuietly();
			dialog = new ProgressDialog(context);
			dialog.setCancelable(false);
			((ProgressDialog)dialog).setProgressStyle(ProgressDialog.STYLE_SPINNER);
			((ProgressDialog)dialog).setMessage(contactingServer);
			dialog.show();
			break;
		case Constants.Messages.ANALYZING_RESPONSE:
			dismissQuietly();
			dialog = new ProgressDialog(context);
			dialog.setCancelable(false);
			((ProgressDialog)dialog).setProgressStyle(ProgressDialog.STYLE_SPINNER);
			((ProgressDialog)dialog).setMessage(analyzingResponse);
			dialog.show();
			break;
		case Constants.Messages.UPDATE_TERMINATED:
			dismissQuietly();
			break;
		case Constants.Messages.NOT_LOGGED:
			Preferences.getInstance().removeValues(
				Arrays.asList(new String[]{Preferences.WEBSERVER_URL, Preferences.SESSION_ID}));
			dismissQuietly();
			context.startActivity(Util.getHomeIntent(context));
			break;
		case Constants.Messages.ERROR:
		    showMessage(msg);
		    break;
		}
	}

	protected void showMessage(Message msg) {
		showMessage(msg, new DialogDismisserListener());
	}

	protected void showMessage(Message msg, OnClickListener clickListener) {
		dismissQuietly();
		String message = null;
		switch(msg.arg1) {
		case Constants.Messages.ARG1_MESSAGE_CODE:
			message = context.getString(msg.arg2);
			break;
		case Constants.Messages.ARG1_MESSAGE_STRING:
			message = (String) msg.obj;
			break;
		}

		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		if(msg.what == Constants.Messages.ERROR) {
			ad.setTitle(errorTitle);
			message = errorText + " " + message;
		}
		else {
			ad.setTitle(null);
		}
		ad.setMessage(message);
		ad.setCancelable(true);
		ad.setNegativeButton(closeText, clickListener);
		ad.show();
	}
}
