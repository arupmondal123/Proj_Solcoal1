package com.at.drgrep.utility;

import com.at.drgrep.constants.AppConstant;

import android.content.Context;

public class SessionManager
{

	public static boolean isUserLoggedIn(Context context)
	{
		return (null != context.getSharedPreferences(AppConstant.LOGIN_PREFERENCE, Context.MODE_PRIVATE)
				.getString(AppConstant.LOGIN_PREFERENCE_ID, null));
	}
}
