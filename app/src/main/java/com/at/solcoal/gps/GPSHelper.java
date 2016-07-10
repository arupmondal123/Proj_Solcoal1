package com.at.solcoal.gps;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class GPSHelper implements LocationListener
{
	private Context context;
	// flag for GPS Status
	private boolean isGPSEnabled = false;
	// flag for network status
	private boolean isNetworkEnabled = false;
	private LocationManager locationManager;
	private String provider;
	private Location location;
	private double latitude;
	private double longitude;
	private static final long minimumTime = 3600000L;
	private static final float minimumDistance = 5.0f;
	
	private static final String LOG_TAG = "GPSHelper Class";
	
	public GPSHelper(Context context, long minTime, float minDistance)
	{
		this.context = context;
		method(minTime, minDistance);
	}

	public GPSHelper(Context context, long minTime)
	{
		this(context, minTime, minimumDistance);
	}

	public GPSHelper(Context context, float minDistance)
	{
		this(context, minimumTime, minDistance);
	}

	public GPSHelper(Context context)
	{
		this(context, minimumTime, minimumDistance);
	}

	private void method(long minTime, float minDistance)
	{
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		// Getting the name of the provider that meets the criteria
		provider = locationManager.getBestProvider(criteria, false);

		if (provider != null && !provider.equals(""))
		{
			// Get the location from the given provider
			Location location = locationManager.getLastKnownLocation(provider);

			// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
			// minTime, minDistance, this);
			locationManager.requestLocationUpdates(provider, minTime, minDistance, this);

			if (location != null)
			{
				onLocationChanged(location);
			}
			else
			{
				Log.e(LOG_TAG, "Location cannot be retrieved");
			}
		}
		else
		{
			Log.e(LOG_TAG, "No GPS Provider Found");
		}
	}

	public void getMyLocation()
	{
		if (isGPSEnabled())
		{
			List<String> providers = locationManager.getProviders(true);
			{
				{
					List<String> l = locationManager.getAllProviders();
					for (int i = 0; i < l.size(); i++)
					{
						Log.i("Provider [" + i + "]", l.get(i));
					}
				}

				{
					List<String> l = locationManager.getProviders(true);
					for (int i = 0; i < l.size(); i++)
					{
						Log.i("Provider [" + i + "]", l.get(i));
					}
				}
			}
			Location l = null;
			for (int i = 0; i < providers.size(); i++)
			{
				l = locationManager.getLastKnownLocation(providers.get(i));
				if (l != null)
					break;
			}
			if (l != null)
			{
				latitude = l.getLatitude();
				longitude = l.getLongitude();
			}
		}
		else
		{
			Toast.makeText(this.context, "Please enable GPS.", Toast.LENGTH_SHORT).show();
		}
	}

	public boolean isGPSEnabled()
	{
		isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// getting network status
		isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		return (isGPSEnabled || isNetworkEnabled);
	}

	public double getLatitude()
	{
		return latitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public LatLng getLatLong()
	{
		return new LatLng(getLatitude(), getLongitude());
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	@Override
	public void onLocationChanged(Location location)
	{
		setLatitude(location.getLatitude());
		setLongitude(location.getLongitude());
		
		try
		{
			Log.i("location.toString()", location.toString()+"...");
		} catch (Exception e)
		{
			Log.e("location.toString() error", e.getMessage());
		}

		Log.d("Lat-Long : ", "Latitude:" + latitude + ", Longitude:" + longitude);
	}

	@Override
	public void onProviderDisabled(String provider)
	{
		Log.d("Lat-Long : ", " disable " + provider);
		Log.d("Lat-Long : ", "Latitude:" + latitude + ", Longitude:" + longitude);
		Log.d("address", address+".");
	}

	@Override
	public void onProviderEnabled(String provider)
	{
		Log.d("Lat-Long : ", " enable " + provider);
		Log.d("Lat-Long : ", "Latitude:" + latitude + ", Longitude:" + longitude);
		Log.d("address", address+".");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		Log.d("Lat-Long : ", status + "");
		Log.d("Lat-Long : ", "Latitude:" + latitude + ", Longitude:" + longitude);
		Log.d("address", address+".");
	}

	private String address;

	private void getAddressFromLatLong(LatLng latlong)
	{
		LocationAddress locationAddress = new LocationAddress();
		locationAddress.getAddressFromLocation(latitude, longitude, this.context, new Handler() {
			@Override
			public void handleMessage(Message message)
			{
				switch (message.what)
				{
				case 1:
					Bundle bundle = message.getData();
					address = bundle.getString("address");
					break;
				default:
					address = "";
				}
			}
		});
	}
}