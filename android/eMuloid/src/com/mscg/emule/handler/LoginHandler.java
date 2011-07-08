package com.mscg.emule.handler;

import java.util.HashMap;
import java.util.Map;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;

import com.mscg.emule.DownloadList;
import com.mscg.emule.Login;
import com.mscg.emule.R;
import com.mscg.emule.listener.DialogDismisserListener;
import com.mscg.emule.util.Constants;
import com.mscg.emule.util.Preferences;

public class LoginHandler extends GenericHandler {

	public LoginHandler(Login context) {
		super(context);
	}

	@Override
	public void handleMessage(Message msg) {
		Intent intent = null;
		String url = null;
		Map<String, Object> values = null;
		Preferences prefs = Preferences.getInstance();
		switch(msg.what) {
		case Constants.Messages.Login.BAD_PASSWORD:
			msg.what = Constants.Messages.ERROR;
			msg.arg1 = Constants.Messages.ARG1_MESSAGE_CODE;
			msg.arg2 = R.string.login_bad_password;
			msg.obj = null;
			showMessage(msg, new DialogDismisserListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					super.onClick(dialog, which);
					((Login)context).getPasswordBox().setText("");
					((Login)context).getPasswordBox().setSelected(true);
				}
			});
			break;
		case Constants.Messages.Login.LOGGED_IN:
			url = ((Login)context).getUrlBox().getText().toString();
			if(prefs.getBoolean(Preferences.SAVE_URL_ON_LOGIN, true)) {
				prefs.saveValue(Preferences.LOGIN_URL, url);
			}
			values = new HashMap<String, Object>();
			values.put(Preferences.WEBSERVER_URL, url);
			values.put(Preferences.SESSION_ID, msg.arg1);
			prefs.saveValues(values);
			intent = new Intent(context, DownloadList.class);
			context.startActivity(intent);
			dismissQuietly();
			break;
		default:
			super.handleMessage(msg);
		}
	}

}
