package com.at.solcoal.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.at.solcoal.R;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.model.UserInfo;
import com.at.solcoal.utility.SharedPreferenceUtility;
import com.at.solcoal.utility.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class AddStoreActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private UserInfo userInfo				= null;
    EditText input_shopname = null;
    EditText input_shopdescription = null;
    EditText input_shoplocation = null;
    EditText input_shopcontactemail = null;
    EditText input_shopcontactphone = null;
    EditText input_shopcontactweblink = null;
    String shop_id = null;
    TextInputLayout layout_shop_name;
    private ProgressDialog pd;
    TextInputLayout layout_shop_desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Add a Shop");

        input_shopname = (EditText) findViewById(R.id.input_shop_name);
        input_shopdescription = (EditText) findViewById(R.id.input_shop_description);
        input_shoplocation = ((EditText) findViewById(R.id.input_shop_location));
        input_shopcontactemail = ((EditText) findViewById(R.id.input_shop_contact_email));
        input_shopcontactphone = ((EditText) findViewById(R.id.input_shop_contact_phone));
        input_shopcontactweblink = ((EditText) findViewById(R.id.input_shop_contact_weblink));

        layout_shop_name = (TextInputLayout) findViewById(R.id.input_layout_shop_name);
        layout_shop_desc = (TextInputLayout) findViewById(R.id.input_layout_shop_description);


        input_shopname.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                //Toast.showSmallToast(AddStoreActivity.this, "entered");
                if( input_shopname.getText().length()>0)
                {
                    //Toast.showSmallToast(AddStoreActivity.this, "entered1");
                    input_shopname.setError(null);
                    layout_shop_name.setErrorEnabled(false);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    // call method
                }
            }
        });


        input_shopdescription.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                if( input_shopdescription.getText().length()>0)
                {
                    input_shopdescription.setError(null);
                    layout_shop_desc.setErrorEnabled(false);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    // call method
                }
            }
        });



        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
                                                                               @Override
                                                                               public void onClick(View v) {

                                                                                   if (ValidateInputClick()) {
                                                                                       pd = new ProgressDialog(AddStoreActivity.this);
                                                                                       pd.setMessage("Adding Shop...");
                                                                                       pd.show();
                                                                                       addShop();
                                                                                   }
                                                                               }

                                                                           }
        );


        ((LinearLayout) findViewById(R.id.llshoppicture)).setOnClickListener(new View.OnClickListener() {
                                                                               @Override
                                                                               public void onClick (View v){

                                                                                   //Intent intent = new Intent(AddStoreActivity.this, ProductAddPictureActivity.class);
                                                                                   //intent.putExtra("from_", "AddStoreActivity");
                                                                                   //startActivity(intent);
                                                                               }

                                                                           }
        );


        }

    public Boolean ValidateInputClick() {

        Boolean valid =true;

        String strshopName = layout_shop_name.getEditText().getText().toString();

        if(!TextUtils.isEmpty(strshopName)) {
            Snackbar.make(layout_shop_name, strshopName, Snackbar.LENGTH_SHORT).show();
            layout_shop_name.setErrorEnabled(false);

        } else {
            layout_shop_name.setError("*Shop name required");
            layout_shop_name.setErrorEnabled(true);
            valid =false;
        }


        String strshopDesc = layout_shop_desc.getEditText().getText().toString();

        if(!TextUtils.isEmpty(strshopDesc)) {
            Snackbar.make(layout_shop_desc, strshopDesc, Snackbar.LENGTH_SHORT).show();
            layout_shop_desc.setErrorEnabled(false);

        } else {
            layout_shop_desc.setError("*Shop Description required");
            layout_shop_desc.setErrorEnabled(true);
            valid =false;
        }



        if(!TextUtils.isEmpty(input_shopcontactphone.getText().toString())) {
            if (!isValidPhoneNumber(input_shopcontactphone.getText().toString())) {
                input_shopcontactphone.setError("Enter valid Phone Number");
                //input_shopcontactphone.set(true);
                valid = false;
            }
        }
        if(!TextUtils.isEmpty(input_shopcontactemail.getText().toString())) {
           if(!isValidEmail(input_shopcontactemail.getText().toString())) {
               input_shopcontactemail.setError("Enter valid Email Address");
               //input_shopcontactphone.set(true);
               valid = false;
           }
        }

        return valid;
    }

    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }
    private void addShop() {

        com.at.solcoal.web.AsyncWebClient asyncWebClient = new com.at.solcoal.web.AsyncWebClient();
        asyncWebClient.SetUrl(AppConstant.URL);
        RequestParams reqParam = new RequestParams();
        userInfo = SharedPreferenceUtility.getUserInfo(AddStoreActivity.this);
        reqParam.add("action", "shop_add_or_update");
        reqParam.add("shop_id", shop_id);
        reqParam.add("seller_id", userInfo.getId());
        reqParam.add("shop_name", input_shopname.getText().toString());
        reqParam.add("shop_desc", input_shopdescription.getText().toString());
        reqParam.add("shop_contact_email",input_shopcontactemail.getText().toString());
        reqParam.add("shop_phone_no", input_shopcontactphone.getText().toString());
        reqParam.add("shop_online_weblink",input_shopcontactweblink.getText().toString());
        reqParam.add("shop_active_ind", "Y");
        asyncWebClient.post(reqParam, new JsonHttpResponseHandler() {
            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject jsr) {
                try {
                    String responseCode = jsr.getString("response_code");
                    if (responseCode.equals("1")) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                pd.dismiss();
                                Toast.showLongToast(AddStoreActivity.this, "Shop Added Successfully");
                                finish();
                            }
                        }, 1000);



                    } else {

                        pd.dismiss();
                        Toast.showSmallToast(AddStoreActivity.this, "Error!!!");
                    }
                } catch (JSONException e) {
                    pd.dismiss();
                    Toast.showSmallToast(AddStoreActivity.this, "Error!!!");
                    Log.e("errr", e.getMessage());
                }
            }


            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                pd.dismiss();
                Toast.showNetworkErrorMsgToast(AddStoreActivity.this);
            }
        });

    }

}
