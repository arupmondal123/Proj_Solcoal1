
package com.at.solcoal.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.api.account.user.UserLoginTask;
import com.applozic.mobicomkit.uiwidgets.ApplozicSetting;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.at.solcoal.GCMRegistrationUtils;
import com.at.solcoal.R;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.data.ProdCategory;
import com.at.solcoal.model.UserInfo;
import com.at.solcoal.utility.KEYBOARD;
import com.at.solcoal.utility.NI;
import com.at.solcoal.utility.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class UserLoginInfo extends Activity
{

	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API. See
	 * https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient	client;

	private String			TAG;

	private UserInfo		userInfo;

	SharedPreferences		sharedpreferences;

	String					response_code;

	String					response_message;

	boolean					dataRetrieved;

	Intent					intent;

	private String			afterLoginAction;

	private TextView		header_textview	= null;

	/**/
	private String			from___			= "";

	private UserLoginTask mAuthTask = null;
	/**/

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		// setContentView(R.layout.act_user_login_info);
		setContentView(R.layout.a___edit_profile_page);
		userInfo = (UserInfo) getIntent().getExtras().get("userInfo");
		/* change */
		try
		{
			from___ = getIntent().getExtras().getString("from___");
		}
		catch (Exception e)
		{
			from___ = "";
		}
		/* /change */
		String userInfoAction = getIntent().getStringExtra(AppConstant.USER_INFO_ACTION);
		afterLoginAction = getIntent().getStringExtra(AppConstant.AFTER_LOGIN_ACTION);

		/* change */ header_textview = (TextView) findViewById(R.id.header_textview);

		((TextView) findViewById(R.id.signed_in_name)).setText(userInfo.getName());
		/* change */ ((TextView) findViewById(R.id.name_initial)).setText(NI.getInitial(userInfo.getName()));
		EditText mEdit = (EditText) findViewById(R.id.signed_in_email);
		//mEdit.setEnabled(false);
		mEdit.setText(userInfo.getEmail());
		//((TextView) findViewById(R.id.signed_in_email)).setText(userInfo.getEmail());
		((TextView) findViewById(R.id.dob_et)).setText(userInfo.getDob());
		enableEditTexts(true);


		// ((ImageView)
		// findViewById(R.id.after_sign_dp)).setImageURI(userInfo.getImageUrl());
		// Spinner spinnerGender = (Spinner) findViewById(R.id.gender);
		// ArrayAdapter<CharSequence> adapter =
		// ArrayAdapter.createFromResource(this, R.array.genders,
		// android.R.layout.simple_spinner_item);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// spinnerGender.setAdapter(adapter);

		((EditText) findViewById(R.id.gender)).setText(userInfo.getGender().toUpperCase());

		// Button proceedBtn = ((Button) findViewById(R.id.proceed_sign_in));
		LinearLayout proceedBtn = ((LinearLayout) findViewById(R.id.proceed_sign_in));

		if (userInfoAction != null)
		{

			if (userInfoAction.equals(AppConstant.USER_INFO_USER))
			{
				((TextView) findViewById(R.id.tv_chat_seller_id121212)).setText("SAVE");
				enableEditTexts(true);
			}
			else if (userInfoAction.equals(AppConstant.USER_INFO_OTHER_USER))
			{
				((LinearLayout) findViewById(R.id.divider_)).setVisibility(View.GONE);
				proceedBtn.setVisibility(View.GONE);
				vp();
			}
		}
		if (!(userInfoAction != null && userInfoAction.equals(AppConstant.USER_INFO_OTHER_USER)))
		{
			proceedBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v)
				{
					userInfo.setName((String) ((TextView) findViewById(R.id.signed_in_name)).getText().toString());
					// Spinner spinnerGenderTmp = (Spinner)
					// findViewById(R.id.gender);
					// userInfo.setGender(spinnerGenderTmp.getSelectedItem().toString());
					userInfo.setGender(((TextView) findViewById(R.id.gender)).getText().toString().trim());
					signIn();
					appLozicSignin();
				}
			});
		}

		/* change */

		((LinearLayout) findViewById(R.id.back_sign_in)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				onBackPressed();
			}
		});
		/* /change */

		try
		{
			if (!getIntent().getExtras().getString("edit_view_profile").equals("Edit Profile"))
			{
				vp();
			}
			else
			{
				ep();
			}

		}
		catch (Exception e)
		{}

		try
		{
			if (from___.equals("Login"))
			{
				enableEditTexts(true);
			}
		}
		catch (Exception e)
		{

		}

	}

	@Override
	public void onBackPressed()
	{

		if (null != this.from___ && this.from___.equals("Login"))
		{
			Intent intent = new Intent(UserLoginInfo.this, SigninActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		else
		{
			super.onBackPressed();
		}
	}

	private ListPopupWindow listPopupWindow = null;

	protected void showGenderList()
	{
		final ArrayList<String> products = new ArrayList<String>(Arrays.asList((new String[] { "FEMALE", "MALE" })));
		Collections.sort(products);
		listPopupWindow = new ListPopupWindow(UserLoginInfo.this);
		listPopupWindow.setAdapter(new ArrayAdapter(UserLoginInfo.this, R.layout.list_item, products));

		listPopupWindow.setAnchorView(((EditText) findViewById(R.id.gender)));

		listPopupWindow.setWidth(302);
		listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

		listPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_menu));
		listPopupWindow.setModal(true);
		listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				((EditText) findViewById(R.id.gender)).setText(products.get(arg2));
				listPopupWindow.dismiss();
			}
		});

		listPopupWindow.show();

	}

	private void vp()
	{
		header_textview.setText("View Profile");
		enableEditTexts(false);
	}

	private void ep()
	{
		header_textview.setText("Edit Profile");
		enableEditTexts(true);
	}

	private void enableEditTexts(boolean b)
	{
		((EditText) findViewById(R.id.signed_in_name)).setEnabled(b);
		//((EditText) findViewById(R.id.signed_in_email)).setEnabled(b);
		((EditText) findViewById(R.id.dob_et)).setEnabled(false);
		((EditText) findViewById(R.id.gender)).setEnabled(b);
		((EditText) findViewById(R.id.gender)).setClickable(b);
		if (b)
		{
			OnTouchListener onT = new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					KEYBOARD.hideSoftKeyboard(UserLoginInfo.this);

					if (event.getAction() == MotionEvent.ACTION_UP)
					{
						showGenderList();

						v.setPressed(false);
					}
					else if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						v.setPressed(true);
					}
					else if (event.getAction() == MotionEvent.ACTION_CANCEL)
					{
						v.setPressed(false);
					}
					return true;
				}
			};
			((EditText) findViewById(R.id.gender)).setOnTouchListener(onT);
			((RelativeLayout) findViewById(R.id.gender_relative_layout)).setOnTouchListener(onT);
		}
		else
		{
			((EditText) findViewById(R.id.gender)).setOnTouchListener(null);
		}
	}


	private void appLozicSignin() {

		UserLoginTask.TaskListener listener = new UserLoginTask.TaskListener() {

			@Override
			public void onSuccess(RegistrationResponse registrationResponse, final Context context) {

				ApplozicSetting.getInstance(context).showStartNewButton();//To show contact list.
				ApplozicSetting.getInstance(context).enableRegisteredUsersContactCall();

				GCMRegistrationUtils gcmRegistrationUtils = new GCMRegistrationUtils(UserLoginInfo.this);
				gcmRegistrationUtils.setUpGcmNotification();

				//Toast.showSmallToast(context, "Login successful");
				/*
				//starting main MainActivity
				Intent mainActvity = new Intent(context, MainActivity.class);
				startActivity(mainActvity);
				Intent intent = new Intent(context, ConversationActivity.class);
				if(ApplozicClient.getInstance(UserLoginInfo.this).isContextBasedChat()){
					intent.putExtra(ConversationUIService.CONTEXT_BASED_CHAT,true);
				}
				startActivity(intent);
				finish();
				*/
			}

			@Override
			public void onFailure(RegistrationResponse registrationResponse, Exception exception) {

				Toast.showSmallToast(UserLoginInfo.this, "DrTalk is temporarily unavaiable");

				AlertDialog alertDialog = new AlertDialog.Builder(UserLoginInfo.this).create();
				alertDialog.setTitle("Login Unsuccessful");
				alertDialog.setMessage(exception.toString());
				alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(android.R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
				if (!isFinishing()) {
					alertDialog.show();
				}
			}
		};
		User user = new User();
		user.setUserId(userInfo.getEmail());
		user.setEmail(userInfo.getEmail());
		user.setDisplayName(userInfo.getName());


		mAuthTask = new UserLoginTask(user, listener, this);
		mAuthTask.execute((Void) null);
	}



	private void signIn()
	{
		// if (afterLoginAction != null)
		// {
		//
		// intent = new Intent(UserLoginInfo.this, HoldingPageActivity.class);
		// }
		// else
		// {
		// intent = new Intent(UserLoginInfo.this, MainActivity.class);
		// }

		if (header_textview.getText().toString().trim().equals("Edit Profile"))
		{
			intent = new Intent(UserLoginInfo.this, UserProductList.class);
			intent.putExtra("owner_id", getIntent().getStringExtra("userInfo___"));
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		else
		{
			intent = new Intent(UserLoginInfo.this, MainActivity.class);
		}
		com.at.solcoal.web.AsyncWebClient asyncWebClientAdd;

		asyncWebClientAdd = new com.at.solcoal.web.AsyncWebClient();
		asyncWebClientAdd.SetUrl(AppConstant.URL);

		RequestParams reqParam = new RequestParams();
		reqParam.add("action", AppConstant.ADD_USER_ACTION);
		reqParam.add("user_name", userInfo.getName());
		reqParam.add("user_email_id", userInfo.getEmail());
		reqParam.add("user_ext_id", userInfo.getExtId());
		reqParam.add("user_login_source", userInfo.getExtSource());
		reqParam.add("user_dob", null);
		reqParam.add("user_sex", userInfo.getGender());
		reqParam.add("user_is_active", "Y");
		sharedpreferences = getSharedPreferences(AppConstant.LOGIN_PREFERENCE, Context.MODE_PRIVATE);

		/*Sign in to AppLogic*/



		asyncWebClientAdd.post(reqParam, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject jsr)
			{
				try
				{
					response_code = jsr.getString("response_code");
					response_message = jsr.getString("response_message");
					if (response_code.equals(AppConstant.DB_OPERATION_SUCCESS))
					{
						userInfo.setId(jsr.getString("user_id"));
						SharedPreferences.Editor editor = sharedpreferences.edit();
						editor.putString(AppConstant.LOGIN_PREFERENCE_EMAIL, userInfo.getEmail());
						editor.putString(AppConstant.LOGIN_PREFERENCE_ID, userInfo.getId());
						editor.putString(AppConstant.LOGIN_PREFERENCE_SOURCE, userInfo.getExtSource());
						editor.putString(AppConstant.LOGIN_PREFERENCE_EXT_ID, userInfo.getExtId());
						editor.putString(AppConstant.LOGIN_PREFERENCE_NAME, userInfo.getName());
						editor.putString(AppConstant.LOGIN_PREFERENCE_GENDER, userInfo.getGender());
						editor.putString(AppConstant.LOGIN_PREFERENCE_SOURCE, userInfo.getExtSource());
						editor.commit();
					}
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}
				finally
				{
					intent.putExtra("userInfo", userInfo);
					if (afterLoginAction != null)
					{
						intent.putExtra(AppConstant.AFTER_LOGIN_ACTION, afterLoginAction);
					}

					startActivity(intent);
				}
			}

			@Override
			public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
					Throwable throwable)
			{
				super.onFailure(statusCode, headers, responseString, throwable);
				// dialog.dismiss();
				// Toast.showNetworkErrorMsgToast(context);

			}

			;
		});
	}
}
