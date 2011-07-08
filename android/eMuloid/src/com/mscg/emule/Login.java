package com.mscg.emule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;

import com.mscg.emule.handler.LoginHandler;
import com.mscg.emule.listener.LoginButtonListener;
import com.mscg.emule.util.Constants;
import com.mscg.emule.util.Preferences;
import com.mscg.emule.util.Util;
import com.mscg.net.HttpClientManager;

public class Login extends Activity {

	private Handler loginHandler;
	private EditText urlBox;
	private EditText passwordBox;
	private Button loginButton;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent != null) {
        	if((intent.getFlags() & Intent.FLAG_ACTIVITY_CLEAR_TOP) != 0 &&
        		intent.getBooleanExtra(Constants.Intent.EXIT_PARAM, false)) {

        		finish();
        		return;
        	}
        }

        setContentView(R.layout.login);

        // init the application utilities
        Preferences.init(this);
        HttpClientManager.open();

        loginHandler = new LoginHandler(this);

        passwordBox = (EditText)findViewById(R.id.login_password);
        urlBox = (EditText)findViewById(R.id.login_url);
        String url = Preferences.getInstance().getString(Preferences.LOGIN_URL, "");
        if(Util.isNotEmptyOrWhiteSpaceOnly(url))
        	urlBox.setText(url);


        loginButton = (Button)findViewById(R.id.login_loginbutton);
        loginButton.setOnClickListener(new LoginButtonListener(loginHandler, this));
    }

	@Override
	protected void onResume() {
		super.onResume();
		passwordBox.setText("");
		if(Util.isNotEmptyOrWhiteSpaceOnly(urlBox.getText().toString()))
			passwordBox.requestFocus();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		HttpClientManager.close();
	}

	public EditText getUrlBox() {
		return urlBox;
	}

	public EditText getPasswordBox() {
		return passwordBox;
	}

}