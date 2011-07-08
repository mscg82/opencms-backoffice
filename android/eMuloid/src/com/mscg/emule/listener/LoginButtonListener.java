package com.mscg.emule.listener;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.mscg.emule.Login;
import com.mscg.emule.net.handler.LoginFormHandler;
import com.mscg.emule.util.Constants;
import com.mscg.emule.util.ContextAware;
import com.mscg.emule.util.Util;
import com.mscg.net.HttpClientManager;

public class LoginButtonListener extends ContextAware<Login> implements OnClickListener {

	public LoginButtonListener(Handler handler, Login context) {
		super(handler, context);
	}

	@Override
	public void onClick(View v) {
		HttpPost post = new HttpPost(context.getUrlBox().getText().toString());

		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("p", context.getPasswordBox().getText().toString()));
		params.add(new BasicNameValuePair("w", "password"));
		try {
			post.setEntity(new UrlEncodedFormEntity(params));

			HttpClientManager.executeAsynchMethod(
					post,
					new LoginFormHandler(handler, Util.getHtmlCleanerStandardProperties()));
		} catch (UnsupportedEncodingException e) {
			Log.e(this.getClass().getCanonicalName(), "Cannot encode parameters", e);
			Message m = handler.obtainMessage(
					Constants.Messages.ERROR,
					Constants.Messages.ARG1_MESSAGE_STRING,
					-1, e.getMessage());
			handler.sendMessage(m);
		}

	}

}
