package com.mscg.emule;

import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mscg.emule.handler.GenericSpeedInfoHandler;
import com.mscg.emule.util.Constants;
import com.mscg.emule.util.Util;

public abstract class GenericSpeedInfoActivity extends Activity {

	public static final int MENU_GROUP = 0;
	public static final int MENU_REFRESH = Menu.FIRST;
	public static final int MENU_EXIT = Menu.FIRST + 1;

	protected GenericSpeedInfoHandler handler;

	protected ImageView serverStatusImg;
	protected TextView serverName;
	protected TextView kadStatus;
	protected LinearLayout downloadSpeedWrap;
	protected LinearLayout downloadSpeed;
	protected TextView downloadSpeedTxt;
	protected LinearLayout uploadSpeedWrap;
	protected LinearLayout uploadSpeed;
	protected TextView uploadSpeedTxt;

	protected MenuItem cache;
	protected MenuItem exit;

	protected Map<String, String> parameters;

	public GenericSpeedInfoActivity() {
		parameters = new LinkedHashMap<String, String>();
	}

	public abstract int getActivityLayout();

	public LinearLayout getDownloadSpeed() {
		return downloadSpeed;
	}

	public TextView getDownloadSpeedTxt() {
		return downloadSpeedTxt;
	}

	public LinearLayout getDownloadSpeedWrap() {
		return downloadSpeedWrap;
	}

	public TextView getKadStatus() {
		return kadStatus;
	}

	public TextView getServerName() {
		return serverName;
	}

	public ImageView getServerStatusImg() {
		return serverStatusImg;
	}

	public LinearLayout getUploadSpeed() {
		return uploadSpeed;
	}

	public TextView getUploadSpeedTxt() {
		return uploadSpeedTxt;
	}

	public LinearLayout getUploadSpeedWrap() {
		return uploadSpeedWrap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(getActivityLayout());

		serverStatusImg = (ImageView)findViewById(R.id.server_status_img);
		serverName = (TextView)findViewById(R.id.server_name);

		kadStatus = (TextView)findViewById(R.id.kad_status);

		downloadSpeedWrap = (LinearLayout)findViewById(R.id.download_speed_wrap);
		downloadSpeed = (LinearLayout)findViewById(R.id.download_speed);
		downloadSpeedTxt = (TextView)findViewById(R.id.download_speed_txt);

		uploadSpeedWrap = (LinearLayout)findViewById(R.id.upload_speed_wrap);
		uploadSpeed = (LinearLayout)findViewById(R.id.upload_speed);
		uploadSpeedTxt = (TextView)findViewById(R.id.upload_speed_txt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		cache = menu.add(MENU_GROUP, MENU_REFRESH, Menu.NONE, R.string.refresh);
		cache.setIcon(android.R.drawable.ic_menu_rotate);
		cache.setShortcut('0', 'r');
		exit = menu.add(MENU_GROUP, MENU_EXIT, Menu.NONE, R.string.exit);
		exit.setIcon(android.R.drawable.ic_lock_power_off);
		exit.setShortcut('1', 'e');

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()) {
		case MENU_REFRESH:
			return update();
		case MENU_EXIT:
			Intent closeIntent = Util.getHomeIntent(this);
			closeIntent.putExtra(Constants.Intent.EXIT_PARAM, true);
			startActivity(closeIntent);
			return true;
		}
		return false;
	}

	public abstract boolean update();

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	protected String getParametersString() {
		StringBuffer ret = new StringBuffer();
		for(Map.Entry<String, String> entry : parameters.entrySet()) {
			try {
				ret.append("&" +
					URLEncoder.encode(entry.getKey(), "UTF-8") + "=" +
					URLEncoder.encode(entry.getValue(), "UTF-8"));
			} catch(Exception e){}
		}
		return ret.toString();
	}

}
