package com.mscg.emule.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {

	public static final String PREFERENCES_NAMES = "eMuloidPreferences";

	public static final String LOGIN_URL = "loginUrl";
	public static final String WEBSERVER_URL = "webserverUrl";
	public static final String SESSION_ID = "sessionID";
	public static final String SAVE_URL_ON_LOGIN = "saveUrlOnLogin";

	public static final String UPDATE_TIME = "updateTime";

	private static Preferences instance;

	private SharedPreferences preferences;

	public static Preferences getInstance() {
		return instance;
	}

	public static void init(Context context) {
		instance = new Preferences(context);
	}

	private Preferences(Context context) {
		preferences = context.getSharedPreferences(PREFERENCES_NAMES, Context.MODE_PRIVATE);
	}

	public Map<String, ?> getAll() {
		return preferences.getAll();
	}

	public boolean getBoolean(String key, boolean defValue) {
		return preferences.getBoolean(key, defValue);
	}

	public float getFloat(String key, float defValue) {
		return preferences.getFloat(key, defValue);
	}

	public int getInt(String key, int defValue) {
		return preferences.getInt(key, defValue);
	}

	public long getLong(String key, long defValue) {
		return preferences.getLong(key, defValue);
	}

	public String getString(String key, String defValue) {
		return preferences.getString(key, defValue);
	}

	public synchronized void saveValue(String key, Object value) throws InvalidParameterException {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put(key, value);
		saveValues(values);
	}

	public synchronized void saveValues(Map<String, Object> values) throws InvalidParameterException {
		Editor editor = preferences.edit();
		for(Map.Entry<String, Object> entry : values.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if(value instanceof Boolean)
				editor.putBoolean(key, (Boolean)value);
			else if(value instanceof Float)
				editor.putFloat(key, (Float)value);
			else if(value instanceof Integer)
				editor.putInt(key, (Integer)value);
			else if(value instanceof Long)
				editor.putLong(key, (Long)value);
			else if(value instanceof String)
				editor.putString(key, (String)value);
			else
				throw new InvalidParameterException("Value for key \"" + key + "\" is not a primitive type.");
		}
		editor.commit();
	}

	public synchronized void removeValue(String key) {
		List<String> keys = new ArrayList<String>(1);
		keys.add(key);
		removeValues(keys);
	}

	public synchronized void removeValues(List<String> keys) {
		Editor editor = preferences.edit();
		for(String key : keys) {
			editor.remove(key);
		}
		editor.commit();
	}

}
