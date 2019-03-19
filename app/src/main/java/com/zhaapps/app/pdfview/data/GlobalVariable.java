package com.zhaapps.app.pdfview.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

public class GlobalVariable extends Application{
	

	public static final String B_KEY_FIRST_LAUNCH ="bol_key_frst";
	

	public static final String S_KEY_COLOR ="s_key_color";
	
	public static final String I_KEY_COLOR ="i_key_color";


	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}


	/**
	 * Universal shared preference
	 * for boolean
	 */
	public boolean getBooleanPref(String key_val, boolean def_val) {
		SharedPreferences pref = getSharedPreferences("pref_"+key_val,MODE_PRIVATE);
		return pref.getBoolean(key_val, def_val);
	}
	
	public void setBooleanPref(String key_val, boolean val) {
		SharedPreferences pref = getSharedPreferences("pref_"+key_val,MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.clear();
		prefEditor.putBoolean(key_val, val);
		prefEditor.commit();
	}
	
	/**
	 * Universal shared preference
	 * for integer
	 */
	public int getIntPref(String key_val, int def_val) {
		SharedPreferences pref = getSharedPreferences("pref_"+key_val,MODE_PRIVATE);
		return pref.getInt(key_val, def_val);
	}
	
	public void setIntPref(String key_val, int val) {
		SharedPreferences pref = getSharedPreferences("pref_"+key_val,MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.clear();
		prefEditor.putInt(key_val, val);
		prefEditor.commit();
	}
	
	/**
	 * Universal shared preference
	 * for string
	 */
	public String getStringPref(String key_val, String def_val) {
		SharedPreferences pref = getSharedPreferences("pref_"+key_val,MODE_PRIVATE);
		return pref.getString(key_val, def_val);
	}
	
	public void setStringPref(String key_val, String val) {
		SharedPreferences pref = getSharedPreferences("pref_"+key_val,MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.clear();
		prefEditor.putString(key_val, val);
		prefEditor.commit();
	}


}
