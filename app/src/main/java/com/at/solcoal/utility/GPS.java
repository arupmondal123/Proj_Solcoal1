
package com.at.solcoal.utility;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;


public class GPS
{

	public static boolean isGPSEnabled(Context context)
	{
		int locationMode = 0;
		String locationProviders;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			try
			{
				locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

			}
			catch (SettingNotFoundException e)
			{
				e.printStackTrace();
			}

			return locationMode != Settings.Secure.LOCATION_MODE_OFF;

		}
		else
		{

			try
			{
				return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE))
						.isProviderEnabled(LocationManager.GPS_PROVIDER);

			}
			catch (Exception e)
			{
				e.printStackTrace();
				try
				{
					locationProviders = Settings.Secure.getString(context.getContentResolver(),
							Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
					return !TextUtils.isEmpty(locationProviders);
				}
				catch (Exception e1)
				{
					e1.printStackTrace();
					return false;
				}

			}
		}

	}
}
