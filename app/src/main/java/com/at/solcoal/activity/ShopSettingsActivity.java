package com.at.solcoal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.at.solcoal.R;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.data.ProductConciseList;
import com.at.solcoal.model.Product_Concise;
import com.at.solcoal.model.UserInfo;
import com.at.solcoal.utility.SharedPreferenceUtility;
import com.at.solcoal.utility.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ShopSettingsActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    LinearLayout l_contact_email ;
    LinearLayout l_contact_website ;
    LinearLayout l_contact_phoneno ;
    LinearLayout body ;
    TextView contact_email ;
    TextView contact_website ;
    TextView contact_phoneno ;
    String shop_active_ind;
    String shop_id;

    private CircleProgressBar progress1;
    private UserInfo userInfo				= null;
    TextView input_contact_email = null;
    TextView input_contact_website = null;
    TextView input_contact_phoneno = null;
    SwitchCompat switchCompat;

    String input_shopcontactemail = "";
    String input_shopcontactphone = "";
    String input_shopcontactweblink = "";
    String input_shopactive_ind = "";
    String returned_shop_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_settings);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //mToolbar.setNavigationIcon(R.drawable.ic_action_back);

        progress1 = (CircleProgressBar) findViewById(R.id.progressBar);
        progress1.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Shop Settings");

        Intent intent =getIntent();
        shop_id = intent.getStringExtra("shop_id_extra");
        input_shopcontactemail = intent.getStringExtra("shop_email_extra");;
        input_shopcontactphone = intent.getStringExtra("shop_phone_extra");;
        input_shopcontactweblink = intent.getStringExtra("shop_weblink_extra");;
        input_shopactive_ind = intent.getStringExtra("shop_shopactive_ind");;


        body = (LinearLayout) findViewById(R.id.body);
        l_contact_email = (LinearLayout) findViewById(R.id.linear_contact_email);
        l_contact_website = (LinearLayout) findViewById(R.id.linear_contact_website);
        l_contact_phoneno = (LinearLayout) findViewById(R.id.linear_contact_phoneno);


        contact_email = (TextView) findViewById(R.id.contact_email);
        contact_website = (TextView) findViewById(R.id.contact_website);
        contact_phoneno = (TextView) findViewById(R.id.contact_phoneno);

        input_contact_email = (TextView) findViewById(R.id.input_contact_email);
        input_contact_website = (TextView) findViewById(R.id.input_contact_website);
        input_contact_phoneno = (TextView) findViewById(R.id.input_contact_phoneno);

        switchCompat = (SwitchCompat) findViewById(R.id
                .switch_item2);



        //fetchShopSettings();
        userInfo = SharedPreferenceUtility.getUserInfo(ShopSettingsActivity.this);

        input_contact_email.setText(input_shopcontactemail);
        input_contact_phoneno.setText(input_shopcontactphone);
        input_contact_website.setText(input_shopcontactweblink);
        input_contact_phoneno.setText(input_shopcontactphone);


        if (input_shopactive_ind.equals("Y")) {
            switchCompat.setChecked(true);
            shop_active_ind ="Y";
        } else {
            switchCompat.setChecked(false);
            shop_active_ind ="N";
        }
        body.setVisibility(View.VISIBLE);

        l_contact_email.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showInputDialog(contact_email.getText().toString(),input_contact_email );
            }
        });

        l_contact_website.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showInputDialog(contact_website.getText().toString(),input_contact_website);
            }
        });


        l_contact_phoneno.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showInputDialog(contact_phoneno.getText().toString(), input_contact_phoneno);
            }
        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    switchCompat.setChecked(true);
                    shop_active_ind = "Y";

                } else {
                    switchCompat.setChecked(false);
                    shop_active_ind = "N";

                }
            }
        });


    }



    @Override
    protected void onPause() {
        super.onPause();
        saveShopSettings();
        finish();

    }

    @Override
    protected void onResume() {

        super.onResume();
        progress1.setVisibility(View.VISIBLE);
        body.setVisibility(View.INVISIBLE);
        fetchShopSettings();

    }
    @Override
    protected void onRestart() {
        super.onRestart();


    }

    protected void showInputDialog(String text,final TextView textView2) {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(ShopSettingsActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShopSettingsActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        TextView textView = (TextView) promptView.findViewById(R.id.textView);
        textView.setText(text);

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        textView2.setText(editText.getText());
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void fetchShopSettings() {

        progress1.setVisibility(View.VISIBLE);
        com.at.solcoal.web.AsyncWebClient asyncWebClient = new com.at.solcoal.web.AsyncWebClient();
        asyncWebClient.SetUrl(AppConstant.URL);
        RequestParams reqParam = new RequestParams();

        reqParam.add("action", "shop_get_by_user");
        reqParam.add("user_id", userInfo.getId());
        reqParam.add("shop_id", shop_id);
        //reqParam.add("user_id", "99");

        asyncWebClient.post(reqParam, new JsonHttpResponseHandler() {
            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject jsr) {
                try {
                    String responseCode = jsr.getString("response_code");
                    Log.e("response_code", responseCode);
                    if (responseCode.equals("1")) {
                        JSONArray pArray = jsr.getJSONArray("data");
                        int l = pArray.length();
                        if (l > 0) {
                            JSONObject obj = null;
                            String input_shopcontactemail = "";
                            String input_shopcontactphone = "";
                            String input_shopcontactweblink = "";
                            String input_shopactive_ind = "";
                            String returned_shop_id = "";

                            for (int i = 0; i < l; i++) {
                                obj = pArray.getJSONObject(i);

                                try {
                                    returned_shop_id = obj.getString("shop_id");
                                } catch (Exception e) {
                                    input_shopcontactemail = "";
                                }

                                if (shop_id.equals(returned_shop_id)) {

                                    try {
                                        input_shopcontactemail = obj.getString("shop_contact_email");
                                        input_contact_email.setText(input_shopcontactemail);
                                    } catch (Exception e) {
                                        input_shopcontactemail = "";
                                    }
                                    try {
                                        input_shopcontactphone = obj.getString("shop_phone_no");
                                        input_contact_phoneno.setText(input_shopcontactphone);
                                    } catch (Exception e) {
                                        input_shopcontactphone = "";
                                    }
                                    try {
                                        input_shopcontactweblink = obj.getString("shop_online_weblink");
                                        input_contact_website.setText(input_shopcontactweblink);
                                    } catch (Exception e) {
                                        input_shopcontactweblink = "";
                                    }
                                    try {
                                        input_shopactive_ind = obj.getString("shop_active_ind");
                                        if (input_shopactive_ind.equals("Y")) {
                                            switchCompat.setChecked(true);
                                            shop_active_ind ="Y";
                                        } else {
                                            switchCompat.setChecked(false);
                                            shop_active_ind ="N";
                                        }
                                    } catch (Exception e) {
                                        input_shopactive_ind = "";
                                    }

                                }
                                else {
                                    //Toast.showSmallToast(ShopSettingsActivity.this, "No Data found.");
                                }
                            }
                        } else {
                            Toast.showSmallToast(ShopSettingsActivity.this, "No Data found.");
                        }
                    } else {
                        Toast.showSmallToast(ShopSettingsActivity.this, "Error!!!");
                        //mDilatingDotsProgressBar.hideNow();
                        progress1.setVisibility(View.INVISIBLE);
                        body.setVisibility(View.VISIBLE);

                        //progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e("errr", e.getMessage());
                } finally {

                    progress1.setVisibility(View.INVISIBLE);
                    body.setVisibility(View.VISIBLE);


                }
            }

            ;

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                progress1.setVisibility(View.INVISIBLE);
                body.setVisibility(View.VISIBLE);
                Toast.showNetworkErrorMsgToast(ShopSettingsActivity.this);
            }

            ;
        });

    }



    private void saveShopSettings() {

        progress1.setVisibility(View.VISIBLE);
        com.at.solcoal.web.AsyncWebClient asyncWebClient = new com.at.solcoal.web.AsyncWebClient();
        asyncWebClient.SetUrl(AppConstant.URL);
        RequestParams reqParam = new RequestParams();
        //userInfo = SharedPreferenceUtility.getUserInfo(ShopSettingsActivity.this);
        reqParam.add("action", "shop_add_or_update");
        reqParam.add("seller_id", userInfo.getId());
        reqParam.add("shop_id", shop_id);
        reqParam.add("shop_name", "null");
        reqParam.add("shop_desc", "null");
        reqParam.add("shop_contact_email", input_contact_email.getText().toString());
        reqParam.add("shop_phone_no", input_contact_phoneno.getText().toString());
        reqParam.add("shop_online_weblink", input_contact_website.getText().toString());
        reqParam.add("shop_active_ind", shop_active_ind);

        Log.e("ShopSettingsActivity", "seller_id" + userInfo.getId());
        Log.e("ShopSettingsActivity", "shop_id" + shop_id);
        Log.e("ShopSettingsActivity", "shop_contact_email"+input_contact_email.getText().toString());
        Log.e("ShopSettingsActivity", "shop_phone_no"+input_contact_phoneno.getText().toString());
        Log.e("ShopSettingsActivity", "shop_online_weblink"+input_contact_website.getText().toString());
        Log.e("ShopSettingsActivity", "shop_active_ind"+shop_active_ind);


        //reqParam.add("user_id", "99");

        asyncWebClient.post(reqParam, new JsonHttpResponseHandler() {
            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject jsr) {
                try {
                    String responseCode = jsr.getString("response_code");
                    if (responseCode.equals("1")) {
                        Log.e("ShopSettingsActivity", "Updated successfully");

                    } else {
                        Log.e("ShopSettingsActivity", jsr.getString("response_message"));
                    }
                } catch (JSONException e) {

                    //Toast.showSmallToast(ShopSettingsActivity.this, "Error!!!");
                    Log.e("errr", e.getMessage());
                }
            }


            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                //Toast.showNetworkErrorMsgToast(ShopSettingsActivity.this);
            }
        });

    }



}
