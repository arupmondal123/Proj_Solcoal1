package com.at.solcoal.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.util.Log;

public class ADDRESS
{

	public static String getAddressLines(Address address)
	{
		Log.i("ADDRESS _ getLastAddressLines(Address address)", "Address Found");

		ArrayList<String> addressFragments = new ArrayList<String>();

		for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
		{
			addressFragments.add(address.getAddressLine(i));
		}

//		return TextUtils.join(System.getProperty("line.separator"), addressFragments);
		return TextUtils.join(",", addressFragments);
	}

	public static String getAddressStr(Address address)
	{
		String display_address = "";

		display_address += address.getAddressLine(0) + "\n";

		for (int i = 1; i < address.getMaxAddressLineIndex(); i++)
		{
			display_address += address.getAddressLine(i) + ",";
		}

		// display_address = display_address.substring(0,
		// display_address.length() - 2);

		return display_address;
	}
	
	public static String getLastAddressLine(Address address)
	{
		Log.i("ADDRESS _ getLastAddressLine(Address address)", "Address Found");

		return address.getAddressLine(address.getMaxAddressLineIndex() - 1).toString().trim();
	}

	public static String getLastAddressLine(Context context, double latitude, double longitude)
	{
		String addressStr = "";

		Geocoder geocoder = new Geocoder(context, Locale.getDefault());

		List<Address> addresses = null;

		try
		{
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
		} catch (IOException ioException)
		{
			addressStr = "No Address Found";
		} catch (IllegalArgumentException illegalArgumentException)
		{
			addressStr = "No Address Found";
		} catch (Exception e)
		{
			addressStr = "No Address Found";
		}

		// Handle case where no address was found.
		if (addresses == null || addresses.size() == 0)
		{
			if (addressStr.isEmpty())
			{
				addressStr = "No Address Found";
			}
		} else
		{
			addressStr = getLastAddressLine(addresses.get(0));
		}

		return addressStr;
	}

}
