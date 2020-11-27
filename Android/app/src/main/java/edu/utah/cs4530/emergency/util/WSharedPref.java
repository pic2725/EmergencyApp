package edu.utah.cs4530.emergency.util;

import android.app.Activity;
import android.content.SharedPreferences;

import edu.utah.cs4530.emergency.EmergencyApplication;

public class WSharedPref {
	
	private final static String PREF_NAME ="W-Utility";

	public static String getString(String key, String value){
		SharedPreferences pref = EmergencyApplication.context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
		return pref.getString(key, value);
	}

	public static void setString(String key, String value){
		SharedPreferences pref = EmergencyApplication.context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		if(value == null)
			editor.remove(key);
		else
			editor.putString(key, value);

		editor.apply();
	}

	public static Boolean getBoolean(String key, Boolean value){
		SharedPreferences pref = EmergencyApplication.context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
		return pref.getBoolean(key, value);
	}

	public static void setBoolean(String key, Boolean value){
		SharedPreferences pref = EmergencyApplication.context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		if(value == null)
			editor.remove(key);
		else
			editor.putBoolean(key, value);

		editor.apply();
	}

	public static int getInt(String key, int value){
		SharedPreferences pref = EmergencyApplication.context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
		return pref.getInt(key, value);
	}

	public static void setInt(String key, Integer value){
		SharedPreferences pref = EmergencyApplication.context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		if(value == null)
			editor.remove(key);
		else
			editor.putInt(key, value);

		editor.apply();
	}
}