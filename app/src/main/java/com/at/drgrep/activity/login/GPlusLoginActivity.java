
package com.at.drgrep.activity.login;

import com.at.drgrep.activity.UserLoginInfo;
import com.at.drgrep.constants.AppConstant;
import com.at.drgrep.model.UserInfo;
import com.at.drgrep.utility.KeyHash;
import com.at.drgrep.utility.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;


public class GPlusLoginActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener
{
	private static final int	RC_SIGN_IN			= 0;

	private static final String	TAG					= "MainActivity";

	private static final int	PROFILE_PIC_SIZE	= 400;

	private GoogleApiClient		mGoogleApiClient;

	private boolean				mIntentInProgress;

	private boolean				mSignInClicked;

	private ConnectionResult	mConnectionResult;

	private String				personName;

	private String				gpemail;

	private String				gpludid;

	private String				gplusbday;

	private String				gplusgender;

	private String				personGooglePlusProfile;

	private String /**/			personId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.placeholder_layout);
		mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();
		KeyHash.printKeyHash(GPlusLoginActivity.this);
		signInWithGplus();

	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent intent)
	{
		if (requestCode == RC_SIGN_IN)
		{
			if (responseCode != RESULT_OK)
			{
				mSignInClicked = false;
			}
			mIntentInProgress = false;
			if (!mGoogleApiClient.isConnecting())
			{
				mGoogleApiClient.connect();
			}
		}
	}

	private void resolveSignInError()
	{
		if (mConnectionResult != null)
		{
			if (mConnectionResult.hasResolution())
			{
				try
				{
					mIntentInProgress = true;
					mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
				}
				catch (SendIntentException e)
				{
					mIntentInProgress = false;
					mGoogleApiClient.connect();
				}
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result)
	{
		if (!result.hasResolution())
		{
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
			return;
		}
		if (!mIntentInProgress)
		{
			mConnectionResult = result;
			if (mSignInClicked)
			{
				resolveSignInError();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0)
	{
		mSignInClicked = false;
		getProfileInformation();
	}

	private void getProfileInformation()
	{
		try
		{
			Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
			Log.d("GPlusLoginActivity", currentPerson.toString());
			if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null)
			{
				personId = currentPerson.getId();
				personName = currentPerson.getDisplayName();
				personGooglePlusProfile = currentPerson.getUrl();
				gpemail = Plus.AccountApi.getAccountName(mGoogleApiClient);
				gpludid = currentPerson.getId();
				gplusbday = currentPerson.getBirthday();

				int gplusgndr = currentPerson.getGender();
				if (gplusgndr == 0)
				{
					gplusgender = "MALE";
				}
				else if (gplusgndr == 1)
				{
					gplusgender = "FEMALE";
				}
				else
				{
					gplusgender = "OTHER";
				}
				Log.e(TAG, "Name: " + personName + ", plusProfile: " + personGooglePlusProfile + ", email: " + gpemail);
				GoogleLoginSuccessful();
			}
			else
			{
				Toast.showSmallToast(getApplicationContext(), "Person information is null");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	 private String afterLoginAction;
	private void GoogleLoginSuccessful()
	{
		// TODO
		Intent intent = new Intent(GPlusLoginActivity.this, UserLoginInfo.class);
		
		UserInfo userInfo = new UserInfo();
		userInfo.setExtId(personId);
		userInfo.setName(personName);
		userInfo.setEmail(gpemail);
		userInfo.setDob(gplusbday);
		userInfo.setGender(gplusgender);
		userInfo.setExtSource("google");
		// userInfo.setImageUrl(acct.getPhotoUrl());
		intent.putExtra("is_edit_profile", "");
		intent.putExtra("userInfo", userInfo);
		intent.putExtra("from___", "Login");
		if (afterLoginAction != null)
		{
			intent.putExtra(AppConstant.AFTER_LOGIN_ACTION, afterLoginAction);
		}
		startActivity(intent);
	}

	@Override
	public void onConnectionSuspended(int arg0)
	{
		mGoogleApiClient.connect();
	}

	private void signInWithGplus()
	{
		if (!mGoogleApiClient.isConnecting())
		{
			mSignInClicked = true;
			resolveSignInError();
		}
	}

	protected void onStart()
	{
		super.onStart();
		mGoogleApiClient.connect();
	}

	protected void onStop()
	{
		super.onStop();
		if (mGoogleApiClient.isConnected())
		{
			mGoogleApiClient.disconnect();
		}
	}

}
