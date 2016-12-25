package com.at.drgrep.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPref
{
	private SharedPreferences			pref;
	private Editor						editor;
	private Context						_context;
	private int							PRIVATE_MODE	= 0;

	private static final String	PREF_NAME		= "DrGrep_LatLong";
	private static final String	LATITUDE		= "DrGrep_Latitude";
	private static final String	LONGITUDE		= "DrGrep_Longitude";

	public SharedPref(Context context)
	{
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
	}

	public void saveUsersLastLatLong(String latitude, String longitude)
	{
		editor = pref.edit();
		editor.putString(LATITUDE, latitude);
		editor.putString(LONGITUDE, longitude);
		editor.commit();
	}

	public String getUsersLastLatitude()
	{
		return pref.getString(LATITUDE, "0.0");
	}
	
	public String getUsersLastLongitude()
	{
		return pref.getString(LONGITUDE, "0.0");
	}

}
