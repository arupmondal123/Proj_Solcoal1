package com.at.drgrep.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.at.drgrep.R;
import com.at.drgrep.animation.ANIM;
import com.at.drgrep.constants.AppConstant;
import com.at.drgrep.data.ImageUri;
import com.at.drgrep.model.UserInfo;
import com.at.drgrep.utility.IMAGE_UTIL;
import com.at.drgrep.utility.SharedPreferenceUtility;
import com.at.drgrep.utility.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;

public class HoldingPageActivity extends Activity {

    UserInfo userInfo;
    boolean dataRetrieved;
    SharedPreferences sharedpreferences;
    String response_code;
    String response_message;
    Intent intentLogout;
    private String userIdStr;
    private String afterLoginAction;

    TextView tvTextView;
    TextView tvTextView1;
    TextView tvTextView2;
    TextView tvTextView3;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_holding_page);
        context = HoldingPageActivity.this;
        activity = HoldingPageActivity.this;

        //Set Fonts
        tvTextView1 = (TextView) findViewById(R.id.tv2);
        typeface = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");
        tvTextView1.setTypeface(typeface);

        tvTextView2 = (TextView) findViewById(R.id.tv3);
        typeface = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");
        tvTextView2.setTypeface(typeface);

        tvTextView3 = (TextView) findViewById(R.id.tv4);
        typeface = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");
        tvTextView3.setTypeface(typeface);

        tvTextView = (TextView) findViewById(R.id.tv);
        typeface = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");
        tvTextView.setTypeface(typeface);
        //End Of Set

        afterLoginAction = getIntent().getStringExtra(AppConstant.AFTER_LOGIN_ACTION);
        userInfo = null;
        if (getIntent() != null && getIntent().getExtras() != null) {
            userInfo = (UserInfo) getIntent().getExtras().getSerializable("userInfo");
        } else {
            userInfo = SharedPreferenceUtility.getUserInfo(context);
        }
        ((RelativeLayout) findViewById(R.id.my_chats)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openLoginActivity();
            }
        });
        if(AppConstant.AFTER_LOGIN_LIST_PRODUCT.equals(afterLoginAction)) {
            takePhoto();
        } else {
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_left);
            handle();

            ((RelativeLayout) findViewById(R.id.blank_area_holding)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startLandingPage();
                }
            });

            if (userInfo != null && userInfo.getId() != null) {
                ((RelativeLayout) findViewById(R.id.logout)).setVisibility(View.VISIBLE);
                ((RelativeLayout) findViewById(R.id.sign_in_register)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        browseUserProduct();
                    }
                });
                ((RelativeLayout) findViewById(R.id.logout)).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        signOut();
                    }
                });
                //set user image
                RelativeLayout rlView = (RelativeLayout) findViewById(R.id.sign_in_register);
                String first = userInfo.getEmail().substring(0, 1);
                int id = getResources().getIdentifier("drawable/" + first, null, getPackageName());
                rlView.setBackgroundResource(id);
                //end of set user image
            } else {
                ((RelativeLayout) findViewById(R.id.logout)).setVisibility(View.GONE);
                ((RelativeLayout) findViewById(R.id.sign_in_register)).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HoldingPageActivity.this, SigninActivity.class);
                        intent.putExtra(AppConstant.ADD_EDIT, AppConstant.ADD);
                        startActivity(intent);
                    }
                });

                ((RelativeLayout) findViewById(R.id.sign_in_register)).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HoldingPageActivity.this, SigninActivity.class);
                        intent.putExtra(AppConstant.ADD_EDIT, AppConstant.ADD);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private void startLandingPage() {
        super.onBackPressed();
    }

    private void signOut() {
        intentLogout = new Intent(HoldingPageActivity.this, MainActivity.class);

        com.at.drgrep.web.AsyncWebClient asyncWebClient;

        asyncWebClient = new com.at.drgrep.web.AsyncWebClient();
        asyncWebClient.SetUrl(AppConstant.URL);

        RequestParams reqParam = new RequestParams();
        reqParam.add("action", AppConstant.UPDATE_ACTIVE_USER_ACTION);
        reqParam.add("user_email_id", userInfo.getEmail());
        reqParam.add("user_ext_id", userInfo.getExtId());
        reqParam.add("user_login_source", userInfo.getExtSource());
        sharedpreferences = getSharedPreferences(AppConstant.LOGIN_PREFERENCE, Context.MODE_PRIVATE);

        asyncWebClient.post(reqParam, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, org.json.JSONObject jsr) {
                try {
                    response_code = jsr.getString("response_code");
                    response_message = jsr.getString("response_message");
                    if (response_code.equals(AppConstant.DB_OPERATION_SUCCESS)) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();
                        editor.commit();
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                } finally {
                    intentLogout.removeExtra("userInfo");
                    startActivity(intentLogout);
                }
            };

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                                  Throwable throwable) {
                dataRetrieved = true;
                super.onFailure(statusCode, headers, responseString, throwable);
                //dialog.dismiss();
                //Toast.showNetworkErrorMsgToast(context);

            };
        });
    }

    private void handle()
    {
        View.OnClickListener onClick = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                afterLoginActivity(AppConstant.AFTER_LOGIN_LIST_PRODUCT);
            }
        };
        ((RelativeLayout) findViewById(R.id.list_product)).setOnClickListener(onClick);

        View.OnClickListener onClickBrowse = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startLandingPage();
            }
        };
        ((RelativeLayout) findViewById(R.id.browse_product)).setOnClickListener(onClickBrowse);
    }

    private static final int	CAPTURE_IMAGE	= 1;
    private Uri imageUri		= null;
    private Context context			= null;
    private Activity				activity		= null;

    private void takePhoto()
    {
        String fileName = "random1234567.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		/**/ values.clear();
        startActivityForResult(intent, CAPTURE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE)
        {
            String str = IMAGE_UTIL.getActualFilePathFromTempUri(activity, imageUri);
            if (resultCode == RESULT_OK)
            {
                ImageUri.images.put(str, AppConstant.Camera);
                startAddproduct();
            } else if (resultCode == RESULT_CANCELED)
            {
                clearImageUrisAndDeleteBlankFile(str);
                Toast.showSmallToast(context, getResources().getString(R.string.photo_was_not_taken));
            } else
            {
                clearImageUrisAndDeleteBlankFile(str);
                Toast.showSmallToast(context, getResources().getString(R.string.photo_was_not_taken));
            }
        }
    }

    public void afterLoginActivity(String afterLoginAction) {
        SharedPreferences sharedpreferences = getSharedPreferences(AppConstant.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
        if(sharedpreferences!= null) {
            userIdStr = sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_ID,null);
            if(userIdStr == null) {
                Intent intent = new Intent(HoldingPageActivity.this, SigninActivity.class);
                intent.putExtra(AppConstant.AFTER_LOGIN_ACTION, afterLoginAction);
                startActivity(intent);
            } else {
                if(AppConstant.AFTER_LOGIN_LIST_PRODUCT.equals(afterLoginAction)) {
                    takePhoto();
                }
            }
        }
    }

    private void browseUserProduct() {
        Intent intent = new Intent(HoldingPageActivity.this, UserProductList.class);
        intent.putExtra("userInfo", userInfo);
        startActivity(intent);
    }

    private void startAddproduct()
    {
        Intent intent = new Intent(HoldingPageActivity.this, ProductAddProductActivity.class);
        intent.putExtra(AppConstant.ADD_EDIT, AppConstant.ADD);
//		ProductListPage.this.finish();
        startActivity(intent);
        ANIM.onStartActivity(HoldingPageActivity.this);
    }

    private void clearImageUrisAndDeleteBlankFile(String str)
    {
        try
        {
            ImageUri.images.clear();
        } catch (Exception e)
        {}
        try
        {
            (new File(str)).delete();
        } catch (Exception e)
        {
            Log.e("(new File(str)).delete();", e.getMessage());
        }
    }

    private void openLoginActivity() {
        String user_id;
        SharedPreferences sharedpreferences = getSharedPreferences(AppConstant.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
        if(sharedpreferences!= null && (sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_ID,null) != null)) {
            user_id = sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_ID,null);
            if(user_id == null) {
                Intent intent = new Intent(HoldingPageActivity.this, SigninActivity.class);
                intent.putExtra(AppConstant.AFTER_LOGIN_ACTION, "after_login_action");
                startActivity(intent);
            }
            else {
                Intent chatHoldingActivity = new Intent(this, UserChatEntryHoldingPage.class);
                chatHoldingActivity.putExtra("userId", user_id);
                startActivity(chatHoldingActivity);
            }
        }

    }

}
