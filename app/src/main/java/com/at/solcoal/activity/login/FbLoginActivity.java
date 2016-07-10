
package com.at.solcoal.activity.login;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import com.at.solcoal.activity.UserLoginInfo;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.model.UserInfo;
import com.at.solcoal.utility.KeyHash;
import com.at.solcoal.utility.Toast;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class FbLoginActivity extends Activity
{
	private String	fbname;

	private String	fbfname;

	private String	fblnm;

	private String	fbid;

	private String	fbbday;

	private String	fbusername;

	private String	fbgender;

	private String	fbemail;

	private String	message;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		KeyHash.printKeyHash(FbLoginActivity.this);

		openFacebookSession();
	}

	private void openFacebookSession()
	{
		Session.openActiveSession(this, true, Arrays.asList("email", "public_profile", "user_birthday"),
				new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state, Exception exception)
					{
						if (exception != null)
						{
							Log.d("Facebook", exception.getMessage());
						}
						if (session.isOpened())
						{
							Bundle parameters = new Bundle();
							parameters.putString("fields", "id,name,birthday,first_name,last_name,email,gender");
							Request req = Request.newGraphPathRequest(session, "me", new Callback() {
								@Override
								public void onCompleted(Response response)
								{
									Log.d("FbResponse---", response.toString());
									JSONObject Json = response.getGraphObject().getInnerJSONObject();
									try
									{
										fbname = Json.getString("name");
									}
									catch (JSONException e)
									{
										fbname = "";
									}
									try
									{
										fbbday = Json.getString("birthday");
									}
									catch (JSONException e1)
									{
										fbbday = "";
									}
									try
									{
										fbgender = Json.getString("gender");
									}
									catch (JSONException e)
									{
										fbgender = "";
									}
									try
									{
										fbid = Json.getString("id");
									}
									catch (JSONException e)
									{
										fbid = "";
									}
									try
									{
										fbemail = Json.getString("email");
									}
									catch (JSONException e)
									{
										if (!fbid.equalsIgnoreCase(""))
										{
											fbemail = fbid + "@facebook.com";
										}
										else
										{
											fbemail = "";
										}
									}
									if (!fbid.equalsIgnoreCase("") && !fbemail.equalsIgnoreCase(""))
									{
										FacebookLoginInSuccessful();
									}
									else
									{
										FacebookLoginInUnsuccessful();
									}
									Log.d("FbResponse", response.toString());
									Log.d("FbResponse", response.getGraphObject().getInnerJSONObject().toString());
								}
							});
							req.setParameters(parameters);
							req.executeAsync();
						}
						else
						{
							// TODO
						}
					}
				});
	}

	private void FacebookLoginInSuccessful()
	{
		// TODO

		/* do stuffs after login is successful */

		startAfterLoginActivity();
	}

	private String afterLoginAction;

	private void startAfterLoginActivity()
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(FbLoginActivity.this, UserLoginInfo.class);

		UserInfo userInfo = new UserInfo();
		userInfo.setExtId(fbid);
		userInfo.setName(fbname);
		userInfo.setEmail(fbemail);
		userInfo.setDob(fbbday);
		userInfo.setGender(fbgender);
		userInfo.setExtSource("facebook");
		// userInfo.setImageUrl(acct.getPhotoUrl());
		intent.putExtra("userInfo", userInfo);
		intent.putExtra("is_edit_profile", "");
		intent.putExtra("from___", "Login");
		if (afterLoginAction != null)
		{
			intent.putExtra(AppConstant.AFTER_LOGIN_ACTION, afterLoginAction);
		}
		startActivity(intent);

	}

	private void FacebookLoginInUnsuccessful()
	{
		// TODO
		Toast.showSmallToast(FbLoginActivity.this, "Could not get information from your Facebook account.");

	}

	void restart_fb()
	{
		Session session = Session.getActiveSession();
		session.close();
		session.closeAndClearTokenInformation();
		Log.d("restart_fb", session.isOpened() + "");
		openFacebookSession();
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle savedState)
	{
		super.onSaveInstanceState(savedState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
}
