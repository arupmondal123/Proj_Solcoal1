package com.at.drgrep.utility;

import com.at.drgrep.R;

import android.content.Context;

public class Toast
{
	public static void showSmallToast(Context context, String message)
	{
		android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show();
	}
	
	public static void showLongToast(Context context, String message)
	{
		android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show();
	}
	
	public static void showNetworkErrorMsgToast(Context context)
	{
		showSmallToast(context, context.getResources().getString(R.string.network_error));
	}
	
	public static void showImplementationPending(Context context)
	{
		showSmallToast(context, context.getResources().getString(R.string.implementation_pending));
	}
}
