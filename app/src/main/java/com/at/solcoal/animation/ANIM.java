package com.at.solcoal.animation;

import com.at.solcoal.R;

import android.app.Activity;

public class ANIM
{
	public static void onStartActivity(Activity activity)
	{
//		activity.overridePendingTransition(R.anim.pull_in_right,
//				R.anim.push_out_left);	
	}

	public static void onBack(Activity activity)
	{
//		activity.overridePendingTransition(R.anim.pull_in_left,
//				R.anim.push_out_right);
	}
	
	public static void goUP(Activity activity)
	{
//		activity.overridePendingTransition(R.anim.stay_still,
//				R.anim.slide_out_up);
	}
	
	public static void comeDown(Activity activity)
	{
//		activity.overridePendingTransition(R.anim.slide_down,
//				R.anim.stay_still);
	}
}
