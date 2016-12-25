package com.at.drgrep.activity;

import java.util.Locale;

import com.at.drgrep.R;
import com.at.drgrep.animation.ANIM;
import com.at.drgrep.constants.AppConstant;
import com.at.drgrep.data.IsLocationChanged;
import com.at.drgrep.data.ProductLocation;
import com.at.drgrep.data.UserLocation;
import com.at.drgrep.intentservice.AddressResultReceiver;
import com.at.drgrep.m.DelayAutoCompleteTextView;
import com.at.drgrep.m.GeoAutoCompleteAdapter;
import com.at.drgrep.utility.ADDRESS;
import com.at.drgrep.utility.KEYBOARD;
import com.at.drgrep.utility.STR;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class GoogleMapActivity extends Activity implements OnMapReadyCallback
// , GoogleApiClient.ConnectionCallbacks,
// GoogleApiClient.OnConnectionFailedListener
{
	private String						TAG;
	private Context						context			= null;
	private Activity					activity		= null;
	// private EditText mapSearchBox = null;
	private GoogleMap					googleMap		= null;
														// private
														// LinearLayout
														// search = null;
														// private
														// LinearLayout
														// search_ll = null;

	/* Footer Address */
	private LinearLayout				layout_btn		= null;
	private TextView					address_txt		= null;
														/* /Footer Address */

	// private String properAddress = null;
	// private String properAddress_used = null;
	/**/
	private Location					mLastLocation	= null;
	private AddressResultReceiver		mResultReceiver;
	Double								Latitude;
	Double								Longitude;
	protected GoogleApiClient			mGoogleApiClient;

	private Geocoder					geoCoder		= null;

	private Integer						THRESHOLD		= 2;
	private DelayAutoCompleteTextView	geo_autocomplete;
	private ImageView					geo_autocomplete_clear;

	private String						mapJobType		= "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_google_map);

		context = GoogleMapActivity.this;
		activity = GoogleMapActivity.this;

		TAG = "GoogleMapActivity";

		layout_btn = (LinearLayout) findViewById(R.id.layout_btn);
		address_txt = (TextView) findViewById(R.id.address_txt);

		setMapJobType();

		initializeGeoCoder();

		startMapFragment();

	}

	private void initializeGeoCoder()
	{
		// gCoder = new Geocoder(context,
		// context.getResources().getConfiguration().locale);
		geoCoder = new Geocoder(this, Locale.getDefault());
	}

	private void startMapFragment()
	{
		MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.google_map_fragment);
		mapFragment.getMapAsync(this);
	}

	private void setAddressTextOnMap(final LatLng lat_long, String pAddress)
	{
		address_txt.setText(getResources().getString(R.string.use) + ": " + STR.getSliceStr(pAddress, 30));

		layout_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				if (mapJobType.equals(AppConstant.CHANGE_USER_LOCATION))
				{
					/**/Log.e(TAG + "_setAddressTextOnMap()", "(mapJobType.equals(AppConstant.CHANGE_USER_LOCATION))");
					UserLocation.setUserPreferredLatLng(lat_long);
					IsLocationChanged.isChanged = true;
					// UserLocation.setUserAddress(properAddress);
				} else if (mapJobType.equals(AppConstant.PRODUCT_ADD))
				{
					/**/Log.e(TAG + "_setAddressTextOnMap()", "(mapJobType.equals(AppConstant.PRODUCT_ADD))");
					ProductLocation.setLatLng(lat_long);
				} else if (mapJobType.equals(AppConstant.PRODUCT_EDIT))
				{
					/**/Log.e(TAG + "_setAddressTextOnMap()", "(mapJobType.equals(AppConstant.PRODUCT_EDIT))");

					ProductLocation.setLatLng(lat_long);
				}

				onBack(v);
			}
		});
	}

	private void hideSoftKeyBoard()
	{
		KEYBOARD.hideSoftKeyboard(activity);
	}

	private void setAddress(double latitude, double longitude, String properAddress)
	{
		LatLng lat_long = new LatLng(latitude, longitude);
		setAddressTextOnMap(lat_long, properAddress);
	}

	private void setAddressAndPin(double latitude, double longitude)
	{
		hideSoftKeyBoard();

		Address address = null;
		String properAddress = getResources().getString(R.string.location_unknown);

		try
		{
			address = geoCoder.getFromLocation(latitude, longitude, 1).get(0);

			properAddress = ADDRESS.getAddressLines(address);

			if (mapJobType.equals(AppConstant.CHANGE_USER_LOCATION))
			{
				UserLocation.setUserAddress(ADDRESS.getLastAddressLine(address));
			}

		} catch (Exception e)
		{

		}
		setAddress(latitude, longitude,properAddress);
		setPin(new LatLng(latitude, longitude), properAddress);
	}

	// private String getAddressString(Address address)
	// {
	// ArrayList<String> addressFragments = new ArrayList<String>();
	// for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
	// {
	// addressFragments.add(address.getAddressLine(i));
	// }
	//
	// // return String.format("%s, %s",
	// // address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0)
	// // : "", address.getCountryName());
	//
	// return TextUtils.join(System.getProperty("line.separator"),
	// addressFragments);
	// }

	@Override
	public void onMapReady(GoogleMap googleMap)
	{
		this.googleMap = googleMap;

		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setZoomControlsEnabled(false);
		googleMap.getUiSettings().setZoomGesturesEnabled(true);
		googleMap.getUiSettings().setCompassEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.getUiSettings().setRotateGesturesEnabled(false);
		googleMap.getUiSettings().setMapToolbarEnabled(true);
		// googleMap.getUiSettings().setIndoorLevelPickerEnabled(true);

		if (!mapJobType.equals(""))
		{
			if (mapJobType.equals(AppConstant.PRODUCT_ADD))
			{
				/**/Log.e(TAG + "_onMapReady()", "(mapJobType.equals(AppConstant.PRODUCT_ADD))");
				setAddressAndPin(UserLocation.getUserPreferredLatLng().latitude,
						UserLocation.getUserPreferredLatLng().longitude);

			} else if (mapJobType.equals(AppConstant.CHANGE_USER_LOCATION))
			{
				/**/Log.e(TAG + "_onMapReady()", "(mapJobType.equals(AppConstant.CHANGE_USER_LOCATION))");
				setAddressAndPin(UserLocation.getUserPreferredLatLng().latitude,
						UserLocation.getUserPreferredLatLng().longitude);
			} else if (mapJobType.equals(AppConstant.PRODUCT_EDIT))
			{
				/**/Log.e(TAG + "_onMapReady()", "(mapJobType.equals(AppConstant.PRODUCT_EDIT))");
				setAddressAndPin(ProductLocation.getLatLng().latitude, ProductLocation.getLatLng().longitude);
			}
		}

		googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

			@Override
			public void onMapClick(LatLng latLng)
			{
				setAddressAndPin(latLng.latitude, latLng.longitude);
			}
		});

		googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {

			@Override
			public boolean onMyLocationButtonClick()
			{
				/**/LatLng latlong = UserLocation.getUserORGLatLong();

				setAddressAndPin(latlong.latitude, latlong.longitude);
				return false;
			}
		});

		// googleMap.setOnMyLocationChangeListener(listener);

		/**/
		geoCoder();
		/**/
	}

	// protected void startIntentService()
	// {
	// Intent intent = new Intent(this, FetchAddressIntentService.class);
	// intent.putExtra(AppConstant.RECEIVER, mResultReceiver);
	// intent.putExtra(AppConstant.LOCATION_DATA_EXTRA, mLastLocation);
	// startService(intent);
	// }

	/**/

	public void geoCoder()
	{
		geo_autocomplete_clear = (ImageView) findViewById(R.id.geo_autocomplete_clear);
		
		geo_autocomplete = (DelayAutoCompleteTextView) findViewById(R.id.geo_autocomplete);
		geo_autocomplete.setThreshold(THRESHOLD);
		geo_autocomplete.setAdapter(new GeoAutoCompleteAdapter(this));

		geo_autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
			{
				Address result = (Address) adapterView.getItemAtPosition(position);
				geo_autocomplete.setText(ADDRESS.getAddressLines(result));
				setAddressAndPin(result.getLatitude(), result.getLongitude());
			}
		});

		geo_autocomplete.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(Editable s)
			{
				if (s.length() > 0)
				{
					geo_autocomplete_clear.setVisibility(View.VISIBLE);
				} else
				{
					geo_autocomplete_clear.setVisibility(View.GONE);
				}
			}
		});

		geo_autocomplete_clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				geo_autocomplete.setText("");
			}
		});
	}

	private void setMapJobType()
	{
		try
		{
			mapJobType = getIntent().getExtras().getString(AppConstant.MAP_JOB_TYPE);
		} catch (Exception e)
		{
		}
	}

	private void setPin(LatLng lat_long, String title)
	{
		try
		{
			googleMap.clear();
		} catch (Exception e)
		{

		}

		CameraPosition cPosition = new CameraPosition.Builder().target(lat_long).zoom(14).build();
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cPosition));

		MarkerOptions marker = new MarkerOptions().position(lat_long);
		if (!title.equals(""))
		{
			marker.title(title);
		}
		marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		googleMap.addMarker(marker);
	}

	@Override
	public void onBackPressed()
	{
		GoogleMapActivity.this.finish();
		
		if(IsLocationChanged.isChanged)
		{
//			IsLocationChanged.isChanged = false;

			Intent intent = new Intent(GoogleMapActivity.this,ProductListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		
		ANIM.onBack(activity);
	}

	public void onBack(View view)
	{
		onBackPressed();
	}
}
