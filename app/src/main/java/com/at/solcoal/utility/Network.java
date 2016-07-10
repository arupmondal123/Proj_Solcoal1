package com.at.solcoal.utility;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class Network
{
	private static final String LOG_TAG = "Network Class";

	public static boolean isConnected(Context context)
	{
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity == null)
		{
			return false;
		} else
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
			{
				for (int i = 0; i < info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
//						return hasActiveInternetConnection(context);
						return true;
					}
				}
			}
		}
		return false;
	}

	public static InputStream getInputStreamFromURLPath(String urlPath) throws IOException
	{
		return new BufferedInputStream(((HttpURLConnection) (new URL(urlPath)).openConnection()).getInputStream());
	}

	public static boolean hasActiveInternetConnection(Context context)
	{
		// if (isNetworkAvailable(context)) {
		try
		{
			return tryConnection();
			
		} catch (IOException e)
		{
			Log.e(LOG_TAG, "Error checking internet connection", e);
			return false;
		}
		// } else {
		// Log.d(LOG_TAG, "No network available!");
		// }
	}
	
	private static boolean tryConnection() throws IOException
	{
		HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
		urlc.setRequestProperty("User-Agent", "Test");
		urlc.setRequestProperty("Connection", "close");
		urlc.setConnectTimeout(1500);
		urlc.connect();
		return (urlc.getResponseCode() == 200);
	}

	class N extends AsyncTask<Void, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(Void... params)
		{
			try
			{
				return tryConnection();
			} catch (IOException e)
			{
				e.printStackTrace();
				
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result)
		{
			super.onPostExecute(result);
		}
		
	}
}