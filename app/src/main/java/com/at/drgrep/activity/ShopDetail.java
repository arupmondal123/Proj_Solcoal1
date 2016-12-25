package com.at.drgrep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.at.drgrep.AddProductsToShop;
import com.at.drgrep.R;
import com.at.drgrep.ShopDetailProductViewFragment;
import com.at.drgrep.constants.AppConstant;
import com.at.drgrep.model.UserInfo;
import com.at.drgrep.utility.SharedPreferenceUtility;
import com.at.drgrep.utility.Toast;
import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopDetail extends AppCompatActivity {

    String shopid;
    String shopname;
    String shopdesc;
    String shopemail;
    String shopphone;
    String shopweblink;
    String shopownprofile;
    String shopactiveind;
    private UserInfo userInfo = null;
    TextView phonenotext;
    TextView emailaddrtext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        Intent intent = getIntent();

        shopid = intent.getStringExtra("shop_id_extra");
        shopname = intent.getStringExtra("shop_name_extra");
        shopdesc = intent.getStringExtra("shop_desc_extra");
        shopemail = intent.getStringExtra("shop_email_extra");
        shopphone = intent.getStringExtra("shop_phone_extra");
        shopweblink = intent.getStringExtra("shop_weblink_extra");
        shopownprofile = intent.getStringExtra("shop_ownprofile");

        shopactiveind = intent.getStringExtra("shop_shopactive_ind");

        //final String shopdescription = intent.getStringExtra("shop_description");


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(shopname);

        TextView textview = (TextView) findViewById(R.id.shoptitle);
        TextView shopdescription = (TextView) findViewById(R.id.shopdescription);
        phonenotext = (TextView) findViewById(R.id.phonetext);
        emailaddrtext = (TextView) findViewById(R.id.emailtext);
        //fetchShopSettings();
        userInfo = SharedPreferenceUtility.getUserInfo(ShopDetail.this);


        textview.setText(shopname);
        shopdescription.setText(shopdesc);
        Linkify.addLinks(phonenotext, Linkify.PHONE_NUMBERS);
        phonenotext.setText(shopphone);
        Linkify.addLinks(emailaddrtext, Linkify.EMAIL_ADDRESSES);
        emailaddrtext.setText(shopemail);

        loadBackdrop();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ShopDetailProductViewFragment())
                    .commit();
        }

        //Toast.showSmallToast(ShopDetail.this, "shopownprofile="+shopownprofile);
        if  (shopownprofile.equals("Y")) {
            ((FloatingActionButton) findViewById(R.id.addbutton)).setVisibility(View.VISIBLE);
            ((FloatingActionButton) findViewById(R.id.addbutton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ShopDetail.this, AddProductsToShop.class);
                    intent.putExtra("shop_id_extra", shopid);
                    startActivity(intent);
                }
            });
        }
        else {
            ((FloatingActionButton) findViewById(R.id.addbutton)).setVisibility(View.GONE);
        }
    }




    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(R.drawable.placeholder1).centerCrop().into(imageView);
    }

    @Override
    protected void onResume() {

        super.onResume();
        fetchShopSettings();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_store_settings) {
            Intent intent = new Intent(ShopDetail.this, ShopSettingsActivity.class);

            intent.putExtra("shop_id_extra", shopid);
            intent.putExtra("shop_email_extra", shopemail);
            intent.putExtra("shop_phone_extra", shopphone);
            intent.putExtra("shop_weblink_extra", shopweblink);
            intent.putExtra("shop_shopactive_ind", shopactiveind);

            startActivity(intent);
            return true;
        }

/*
        if (id == R.id.menu_edit) {
            /*
            Intent intent = new Intent(ShopDetail.this, SearchInputActivity.class);
            startActivityForResult(intent, 2);
            return true;

            return true;

        }
*/


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if  (shopownprofile.equals("Y")) {
            getMenuInflater().inflate(R.menu.menu_store, menu);
        }
        return true;
    }


    private void fetchShopSettings() {

        //progress1.setVisibility(View.VISIBLE);
        com.at.drgrep.web.AsyncWebClient asyncWebClient = new com.at.drgrep.web.AsyncWebClient();
        asyncWebClient.SetUrl(AppConstant.URL);
        RequestParams reqParam = new RequestParams();

        //reqParam.add("action", "shop_get_by_user");
        reqParam.add("action", "shop_get");
        /*
        if (userInfo != null) {
            reqParam.add("user_id", userInfo.getId());
        } else {
            reqParam.add("user_id", userInfo.getId());
        }
*/
        reqParam.add("shop_id", shopid);
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

                                if (shopid.equals(returned_shop_id)) {

                                    try {
                                        input_shopcontactemail = obj.getString("shop_contact_email");
                                        emailaddrtext.setText(input_shopcontactemail);
                                    } catch (Exception e) {
                                        input_shopcontactemail = "";
                                    }
                                    try {
                                        input_shopcontactphone = obj.getString("shop_phone_no");
                                        phonenotext.setText(input_shopcontactphone);
                                    } catch (Exception e) {
                                        input_shopcontactphone = "";
                                    }
                                    /*
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
                                    }*/

                                } else {
                                    //Toast.showSmallToast(ShopSettingsActivity.this, "No Data found.");
                                }
                            }
                        } else {
                            Toast.showSmallToast(ShopDetail.this, "No Data found.");
                        }
                    } else {
                        Toast.showSmallToast(ShopDetail.this, "Error!!!");
                        //mDilatingDotsProgressBar.hideNow();
                        //progress1.setVisibility(View.INVISIBLE);
                        //body.setVisibility(View.VISIBLE);

                        //progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e("errr", e.getMessage());
                } finally {

                    //progress1.setVisibility(View.INVISIBLE);
                    //body.setVisibility(View.VISIBLE);


                }
            }


            ;

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                //progress1.setVisibility(View.INVISIBLE);
                //body.setVisibility(View.VISIBLE);
                Toast.showNetworkErrorMsgToast(ShopDetail.this);
            }

            ;
        });

    }
}