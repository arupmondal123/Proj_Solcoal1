package com.at.drgrep.intentservice;

import com.at.drgrep.constants.AppConstant;
import com.at.drgrep.utility.Toast;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class AddressResultReceiver extends ResultReceiver
{
	private Context	context			= null;
	private String	TAG;
	private String	addressString	= "";
	private String	lineAddress	= "";

	public AddressResultReceiver(Context context, Handler handler)
	{
		super(handler);
		this.context = context;
		this.TAG = "AddressResultReceiver";
	}

	/**
	 * Receives data sent from FetchAddressIntentService and updates the UI in
	 * MainActivity.
	 */
	@Override
	protected void onReceiveResult(int resultCode, Bundle resultData)
	{
		// Display the address string or an error message sent from the
		// intent
		// service.

		// Show a toast message if an address was found.
		if (resultCode == AppConstant.SUCCESS_RESULT)
		{
			addressString = resultData.getString(AppConstant.RESULT_DATA_KEY);
			/**/Toast.showSmallToast(context, addressString);
		}
	}
	
	public String getAddressString()
	{
		return addressString;
	}
}
