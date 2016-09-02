
package com.at.solcoal.activity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.at.solcoal.R;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.data.UserLocation;
import com.at.solcoal.intentservice.AddressResultReceiver;
import com.at.solcoal.intentservice.FetchAddressIntentService;
import com.at.solcoal.model.UserInfo;
import com.at.solcoal.sharedpref.SharedPref;
import com.at.solcoal.utility.ADDRESS;
import com.at.solcoal.utility.GPS;
import com.at.solcoal.utility.Network;
import com.at.solcoal.utility.SessionManager;
import com.at.solcoal.utility.SharedPreferenceUtility;
import com.at.solcoal.utility.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import com.google.android.gms.location.LocationListener;

import android.location.Location;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity
		implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
{
	private Context					context							= null;

	private static String			TAG								= "MainActivity";

	private Location mLastLocation					= null;

	protected GoogleApiClient		mGoogleApiClient				= null;

	private AddressResultReceiver	addressResultReceiver			= null;

	private double					latitude						= 0.00d;

	private double					longitude						= 0.00d;

	private UserInfo				userInfo						= null;

	/**/
	private boolean					isMessageDialogShowing			= false;

	private boolean					isGoOnlineMessageDialogShowing	= false;

	/**/

	//private ProgressDialog			dialog							= null;

	private FindingLocationDialog dialog = null;

	private boolean					isGPSSettingsOpened				= false;

	private MessageEnableGPSDialog	messageEnableGPSDialog			= null;

	private  DilatingDotsProgressBar mDilatingDotsProgressBar = null;

	private LocationRequest mLocationRequest1=null;

	private int locationInterval, fastedInterval;

	private String showShopgroup = null;
	public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.e(TAG + "_", "onCreate()");
		context = MainActivity.this;
		TAG = "MainActivity";
		userInfo = SharedPreferenceUtility.getUserInfo(context);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			setupWindowAnimations();

		}
		//buildGoogleApiClient();
		/* New Code */ checkIfGPSEnabled();
	}

	/* New Code Starts */
	private void checkIfGPSEnabled()
	{
		if (GPS.isGPSEnabled(context))
		{
			/* debug */ Log.d(TAG + "_checkIfGPSEnabled()_", "checkIfGPSEnabled() == true");
			isGPSSettingsOpened = false;
			checkIFWIFIEnabled();
		}
		else
		{
			/* debug */ Log.d(TAG + "_checkIfGPSEnabled()_", "checkIfGPSEnabled() == false");
			isGPSSettingsOpened = true;
			messageEnableGPSDialog = new MessageEnableGPSDialog(context);
			messageEnableGPSDialog.show();
		}
	}

	private class MessageEnableGPSDialog extends Dialog
	{
		private Context ctx = null;

		public MessageEnableGPSDialog(Context context)
		{
			super(context);
			this.ctx = context;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_enable_gps);
			getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
			setCancelable(false);
			setCanceledOnTouchOutside(false);
			((LinearLayout) findViewById(R.id.dialog_allow)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					dismiss();
					openSettings();
				}
			});
			((LinearLayout) findViewById(R.id.dialog_cancel)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{

					dismiss();
					isMessageDialogShowing = false;
					closeApp();
				}
			});
		}
	}

	private class GoOnlineMessageDialog extends Dialog
	{
		private Context ctx = null;

		public GoOnlineMessageDialog(Context context)
		{
			super(context);
			this.ctx = context;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_go_online_);
			isGoOnlineMessageDialogShowing = true;
			getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
			setCancelable(false);
			setCanceledOnTouchOutside(false);
			((LinearLayout) findViewById(R.id.dialog_goonline)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					isGoOnlineMessageDialogShowing = false;
					dismiss();
					closeApp();
				}
			});
		}
	}

	private class FindingLocationDialog extends Dialog
	{
		private Context ctx = null;

		public FindingLocationDialog(Context context)
		{
			super(context,R.style.MyDialogTheme);
			this.ctx = context;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_finding_location);

			//mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);
			//isFindingLocationDialog = true;
			//getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
			setCancelable(false);
		}
	}


	private void checkIFWIFIEnabled()
	{
		if (Network.isConnected(context))
		{
			// buildGoogleApiClient();
			N n = new N();
			n.execute();
		}
		else
		{
			(new GoOnlineMessageDialog(context)).show();
			// showMessageDialog(R.string.network_error);
		}
	}

	@Override
	protected void onRestart()
	{
		super.onRestart();

		if (isGPSSettingsOpened)
		{
			/* debug */ Log.d(TAG + "_onRestart()_", "isGPSSettingsOpened == true");
			checkIfGPSEnabled();
		}
		else
		{
			/* debug */ Log.d(TAG + "_onRestart()_", "isGPSSettingsOpened == false");
		}

		isGPSSettingsOpened = false;
	}

	/**/
	private static final int SETTINGS_ACTIVITY = 12345;

	protected void openSettings()
	{
		// Intent dialogIntent = new
		// Intent(android.provider.Settings.ACTION_SETTINGS);
		Intent dialogIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivityForResult(dialogIntent, SETTINGS_ACTIVITY);
	}
	/**/
	@Override
	public void onBackPressed()
	{
		if (isGoOnlineMessageDialogShowing)
		{
			/* debug */ Log.d(TAG + "_onBackPressed()_", "isGoOnlineMessageDialogShowing=true");
			isGoOnlineMessageDialogShowing = false;
			// try
			// {
			// messageEnableGPSDialog.dismiss();
			// }
			// catch (Exception e)
			// {}
			closeApp();
		}
		else
		{
			super.onBackPressed();
		}

	}
	/**/

	/* New Code Ends */

	class N extends AsyncTask<Void, Void, Boolean>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			Log.e(TAG + "_AsyncTask", "onPreExecute()");

			dialog = new FindingLocationDialog(context);
			dialog.show();
			//mDilatingDotsProgressBar.showNow();
			/*
			dialog = new ProgressDialog(context, R.style.ProgressDialogTheme);
			dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setMessage(getResources().getString(R.string.checking_network_connectivity));
			dialog.setIndeterminate(true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			*/
		}

		@Override
		protected Boolean doInBackground(Void... params)
		{
			try
			{

				HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
				urlc.setRequestProperty("User-Agent", "Test");
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(2000);
				urlc.connect();
				return (urlc.getResponseCode() == 200);




			}
			catch (MalformedURLException e)
			{
				Log.e(TAG, "Error checking internet connection", e);
				return false;
			}
			catch (IOException e)
			{
				Log.e(TAG, "Error checking internet connection", e);
				return false;
			}
			catch (Exception e)
			{
				Log.e(TAG, "Error checking internet connection", e);
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);


			//dialog.dismiss();

			if (result)
			{
				buildGoogleApiClient();

			}
			else
			{
				// showMessageDialog(R.string.network_error);

				(new GoOnlineMessageDialog(context)).show();

			}
		}
	}

	private void showMessageDialog(int string_resource_id)
	{
		(new MessageDialog(context, context.getResources().getString(string_resource_id))).show();
	}
	/*
	 *
	 *
	 *
	 *
	 * */

	/*
	 *
	 *
	 *
	 *
	 * */
	private class MessageDialog extends Dialog
	{
		private Context	ctx	= null;

		private String	message;

		public MessageDialog(Context context, String msg)
		{
			super(context);
			this.ctx = context;
			this.message = msg;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.dialog_warning);
			getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
			setCancelable(false);
			setCanceledOnTouchOutside(false);
			((TextView) findViewById(R.id.message)).setText(this.message);
			((LinearLayout) findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					/**/
					isMessageDialogShowing = false;
					dismiss();
					closeApp();
					/**/
				}
			});
			isMessageDialogShowing = true;
		}
	}

	private void closeApp()
	{
		try
		{
			/* debug */ Log.d(TAG + "_closeApp()_", "closeApp()");
			MainActivity.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
		catch (Exception e)
		{
			Log.e(TAG + "_closeApp()_error_", e.getMessage() + "...");
			e.printStackTrace();
		}
	}

	@Override
	public void onStart()
	{
		super.onStart();
		connectGoogleApiClient();

		Log.e(TAG + "_", "onStart()");

	}

	private void connectGoogleApiClient()
	{
		try
		{
			if (mGoogleApiClient!= null)
			{
				mGoogleApiClient.connect();
			}

		}
		catch (Exception e)
		{}
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		Log.e(TAG + "_", "onStop()");
		try
		{
			if (mGoogleApiClient.isConnected())
			{
				mGoogleApiClient.disconnect();
			}
		}
		catch (Exception e)
		{

		}
	}

	@Override
	public void onPause() {
		super.onPause();  // Always call the superclass method first

	}

	@Override
	public void onResume() {
		super.onResume();  // Always call the superclass method first
	}

	/* Initialize and Connect mGoogleApiClient */
	protected synchronized void buildGoogleApiClient()
	{
		Log.e(TAG + "_buildGoogleApiClient()", "buildGoogleApiClient()");
		mLocationRequest1 = new LocationRequest();
		mLocationRequest1.setInterval(30000);
		mLocationRequest1.setFastestInterval(45000);
		mLocationRequest1.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();

		connectGoogleApiClient();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result)
	{
		Log.e(TAG + "_onConnectionFailed()",
				"ConnectionResult.getErrorCode() = " + result.getErrorCode() + "" + result.toString());

		//trySecondaryWayToGetLocation();
	}

	@Override
	public void onConnectionSuspended(int arg0)
	{
		Log.i("TAG", "Connection suspended");
		mGoogleApiClient.connect();
		Log.i("TAG", "Reconnecting Google_Api_Client");
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d("Permission","Permission Granted!! "+ requestCode);
					getLocation();

				}
				else {

					// add the response if permission is denied. The app should be closed forcefully.
					Log.d("Permission","Permission Denied!! " + requestCode);
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}

	@Override
	public void onConnected(Bundle arg0){

		// adding new code to fix the permission crash

		//check if the location permission exists; if not exist then ask for it.
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {

			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
					MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
		}
		else {
			//if permission already exists then go to next step
			getLocation();
		}

	}

	private void getLocation(){
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest1,this);

		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

		if (mLastLocation != null)
		{

			//Toast.showSmallToast(context, "Getting location returned");
			try
			{
				latitude = mLastLocation.getLatitude();
			}
			catch (Exception e)
			{
				Log.e("error", ""+e.getMessage());
				latitude = 0.00d;
			}

			try
			{
				longitude = mLastLocation.getLongitude();
			}
			catch (Exception e)
			{
				Log.e("error", ""+e.getMessage());
				longitude = 0.00d;
			}

			try
			{
				addressResultReceiver = new AddressResultReceiver(context, new Handler());
				startIntentService();
			}
			catch (Exception e)
			{
				Log.e("error", ""+e.getMessage());
			}

			setUserLocationAndStartLandingPage(latitude, longitude);

		}
		else
		{
			Toast.showSmallToast(context, getResources().getString(R.string.no_location_detected));
			checkNetworkConnectionAndStartWithLastLocation();
		}
	}

	private void checkNetworkConnectionAndStartWithLastLocation()
	{
		if (Network.isConnected(context))
		{
			Toast.showSmallToast(context, getResources().getString(R.string.failed_to_get_location_));
			setUsersLastLocation();
		}
		else
		{
			Toast.showNetworkErrorMsgToast(context);
		}
	}

	private void setUsersLastLocation()
	{
		SharedPref sharedPref = new SharedPref(context);
		double lat = Double.parseDouble(sharedPref.getUsersLastLatitude());
		double longi = Double.parseDouble(sharedPref.getUsersLastLongitude());

		setUserLocationAndStartLandingPage(lat, longi);
	}

	private void setUserLocationAndStartLandingPage(double latitude, double longitude)
	{
		setLocation(latitude, longitude);
		startLandingPage();
	}

	private void setLocation(double latitude2, double longitude2)
	{
		LatLng lat_long = new LatLng(latitude2, longitude2);

		UserLocation.setUserORGLatLong(lat_long);
		UserLocation.setUserPreferredLatLng(lat_long);
		UserLocation.setUserAddress(ADDRESS.getLastAddressLine(context, latitude2, longitude2));
	}

	private void startLandingPage() {

		Intent intent = new Intent(MainActivity.this, ProductListActivity.class);
		intent.putExtra(AppConstant.START_APP, AppConstant.START_APP_FIRST_TIME);
		intent.putExtra("userInfo", userInfo);
		intent.putExtra("Shopgroup", showShopgroup );
		dialog.dismiss();
		//MainActivity.this.finish();
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
			startActivity(intent, options.toBundle());
		} else {
			startActivity(intent);
		}

	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void setupWindowAnimations() {
		/*Slide slide = null;

			slide = new Slide();
			slide.setSlideEdge(Gravity.TOP);
			slide.setDuration(getResources().getInteger(R.integer.anim_duration_medium));
			*/
		Explode explode = null;

		explode = new Explode();
		explode.setStartDelay(1000);
		explode.setDuration(getResources().getInteger(R.integer.anim_duration_medium));

		getWindow().setExitTransition(explode);
		getWindow().setReenterTransition(explode);
	}

	protected void startIntentService()
	{
		Log.e(TAG + "_startIntentService()", "startIntentService()");

		Intent intent = new Intent(getApplicationContext(), FetchAddressIntentService.class);
		intent.putExtra(AppConstant.RECEIVER, addressResultReceiver);
		intent.putExtra(AppConstant.LOCATION_DATA_EXTRA, mLastLocation);

		// Start the service. If the service isn't already running, it is
		// instantiated and started
		// (creating a process for it if needed); if it is running then it
		// remains running. The
		// service kills itself automatically once all intents are processed.
		startService(intent);
	}

	/* LocationListener */
	/* Secondary way to get user's location */

	private LocationManager locationManager;


	private String			provider;

	@Override
	public void onLocationChanged(Location location)
	{
		//setUserLocationAndStartLandingPage(location.getLatitude(), location.getLongitude());
		Log.e(TAG + "_onLocationChanged()", "onLocationChanged()");
	}
/*
	@Override
	public void onProviderDisabled(String provider)
	{
		Log.e(TAG + "_LocationListener_onProviderDisabled()", "onProviderDisabled()");
	}

	@Override
	public void onProviderEnabled(String provider)
	{
		Log.e(TAG + "_LocationListener_onProviderEnabled()", "onProviderEnabled()");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		Log.e(TAG + "_LocationListener_onStatusChanged()", "onStatusChanged()");
	}

	private void trySecondaryWayToGetLocation()
	{
		long minTime = 3600000L;
		float minDistance = 5.0f;
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
				Log.e(TAG, "Location cannot be retrieved");
			}
		}
		else
		{
			Log.e(TAG, "No GPS Provider Found");
		}
	}
*/



}
