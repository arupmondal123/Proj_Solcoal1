
package com.at.solcoal.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.at.solcoal.R;
import com.at.solcoal.adapter.GridViewAdapterProduct;
import com.at.solcoal.adapter.GridViewAdapterProduct2;
import com.at.solcoal.adapter.GridViewAdapterProductSearch;
import com.at.solcoal.animation.ANIM;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.data.GridViewAdapterProductData;
import com.at.solcoal.data.GridViewAdapterProductDataSearch;
import com.at.solcoal.data.ImageUri;
import com.at.solcoal.data.IsLocationChanged;
import com.at.solcoal.data.ProductConciseList;
import com.at.solcoal.data.ProductConciseListSearch;
import com.at.solcoal.data.UserLocation;
import com.at.solcoal.model.Product_Concise;
import com.at.solcoal.model.UserInfo;
import com.at.solcoal.sharedpref.SharedPref;
import com.at.solcoal.utility.IMAGE_UTIL;
import com.at.solcoal.utility.NI;
import com.at.solcoal.utility.Network;
import com.at.solcoal.utility.SharedPreferenceUtility;
import com.at.solcoal.utility.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;


import android.support.v7.app.ActionBarActivity;
import android.widget.SearchView;
import com.at.solcoal.utility.JSONParser;


public class SearchInputActivity extends AppCompatActivity {
    private Context context = null;

    private Activity activity = null;

    String responseCode;

    private String TAG;

    private LinearLayout profile = null;

    private TextView user_location = null;

    private GridView gridView = null;

    private int imageWidth;

    private ProgressDialog progressDialog = null;

    private GridViewAdapterProductSearch productGridViewAdapterSearch = null;

    // private ArrayList<Product_Concise> productConciseList = new
    // ArrayList<Product_Concise>();

    /* change */
    private SlidingDrawer slidingDrawer = null;

    private ImageView handle_image = null;

    private UserInfo userInfo;

    private Intent intentLogout = null;

    private SharedPreferences sharedpreferences = null;

    private String response_code;

    private String response_message;

    private boolean dataRetrieved;

    private String userIdStr;

    private SearchView search;

    private String url;

    private String query;

    private ImageView					geo_autocomplete_clear;

    EditText editText;

    private LinearLayout linearLayout;


    private CircleProgressBar progress1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_input);
        context = SearchInputActivity.this;
        activity = SearchInputActivity.this;

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Search Results");

        //mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);
        progress1 = (CircleProgressBar) findViewById(R.id.progressBar);
        progress1.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        linearLayout = (LinearLayout) findViewById(R.id.body);
        TAG = "SearchInputActivity";
        Log.e(TAG + "_", "onCreate()");

        Intent intent = getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }


        gridView = (GridView) findViewById(R.id.grid_view);
        InitializeGridLayout(); //

        url= "http://166.62.32.227:8983/solr/products/select?q=fullText%3A*";
        url = url+ query + "*&rows=100000&wt=json&indent=true";
        url = url.replace(" ", "%20");
        //Toast.showSmallToast(context, query);
        gridView.setVisibility(View.VISIBLE);
        new JSONParse().execute();
        hideKeyboard(gridView);

        
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG + "_", "onStart()");
    }

    /* */
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG + "_", "onResume()");
        try {
            revokeImageOnClickListenerOnProductGridView();
        } catch (Exception e) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG + "_", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG + "_", "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG + "_", "onRestart()");
        try {
            revokeImageOnClickListenerOnProductGridView();
        } catch (Exception e) {
        }
    }

    private void revokeImageOnClickListenerOnProductGridView() {
        for (int i = 0; i < GridViewAdapterProductDataSearch.imagelist.size(); i++) {
            GridViewAdapterProductData.imagelist.get(i)
                    .setOnClickListener(GridViewAdapterProductDataSearch.imageOnImageClickListenerList.get(i));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG + "_", "onDestroy()");
        SharedPref sharedPref = new SharedPref(context);
        sharedPref.saveUsersLastLatLong(UserLocation.getUserPreferredLatLng().latitude + "",
                UserLocation.getUserPreferredLatLng().longitude + "");
    }


    private void setAdaptertoGridView() {
        productGridViewAdapterSearch = new GridViewAdapterProductSearch(activity, imageWidth, this);
        gridView.setAdapter(productGridViewAdapterSearch);
    }


    private void clearProductConciseList() {
        try {
            ProductConciseListSearch.productConciseListSearch.clear();
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {

        /*Toast.showSmallToast(context, "onBacpressed");
        Intent intent1=new Intent();
        intent1.putExtra("MESSAGE","test");
        setResult(2, intent1);*/

/*
        if(IsLocationChanged.isChanged)
        {
			IsLocationChanged.isChanged = false;
*/
        SearchInputActivity.this.finish();
        Intent intent = new Intent(SearchInputActivity.this,ProductListActivity.class);
        intent.putExtra(AppConstant.START_APP, AppConstant.START_APP_FIRST_TIME);
        intent.putExtra("userInfo", userInfo);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


        ANIM.onBack(activity);
        //super.onBackPressed();
        // clearProductConciseList();
        // ProductListPage.this.finish();
    }

    public void onBack(View view)
    {
        onBackPressed();
    }

    private void InitializeGridLayout() {
        int NUM_OF_COLUMNS = 2; // Number of columns of Grid View
        int GRID_PADDING1 = 13; // Gridview image padding in dp
        int GRID_PADDING2 = 13; // Gridview image padding in dp
        float padding1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, GRID_PADDING1,
                getResources().getDisplayMetrics());
        float padding2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, GRID_PADDING2,
                getResources().getDisplayMetrics());
		/**/
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
		/**/
        // imageWidth = (int) ((Image.getScreenWidth(context) - ((NUM_OF_COLUMNS
        // + 1) * padding1)) / NUM_OF_COLUMNS);
        imageWidth = ((int) ((width - ((NUM_OF_COLUMNS + 1) * padding2)) / NUM_OF_COLUMNS));
        gridView.setNumColumns(NUM_OF_COLUMNS);
        gridView.setColumnWidth(imageWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding1, (int) padding1, (int) padding1, (int) padding1);
        gridView.setHorizontalSpacing((int) padding2);
        gridView.setVerticalSpacing((int) padding2);
    }

    /* */
    private static final int CAPTURE_IMAGE = 1;

    private Uri imageUri = null;


    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            clearProductConciseList();
            //mDilatingDotsProgressBar.showNow();

            progress1.setVisibility(View.VISIBLE);

            //progressDialog = new ProgressDialog(context, R.style.ProgressDialogThemeSearch);
            //progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);

            //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //progressDialog.setIndeterminate(true);

            //progressDialog.setCanceledOnTouchOutside(false);
            //progressDialog.show();
            Log.d("SearchInputActivity", "onPreExecute");
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();

            // Getting JSON from URL

            JSONObject json = jParser.getJSONFromUrl(url);

            Log.d("SearchInputActivity", "doInBackground");
            return json;

        }
        @Override
        protected void onPostExecute(JSONObject json) {
            //progressDialog.dismiss();
            //mDilatingDotsProgressBar.hideNow();
            progress1.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.VISIBLE);

            try {

                try {
                    JSONObject resHeader = json.getJSONObject("responseHeader");
                    responseCode = resHeader.getString("status");
                    Log.d("SearchInputActivity", "onPostExecute");
					/**/
                    Log.e("response_code", responseCode);
                } catch (Exception e) {

                    SearchInputActivity.this.finish();
                    Toast.showSmallToast(context, "Search is temporarily unavailable");
                    if(IsLocationChanged.isChanged)
                    {
                        IsLocationChanged.isChanged = false;

                        Intent intent = new Intent(SearchInputActivity.this,ProductListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    ANIM.onBack(activity);

                }



                if (responseCode.equals("0")) {

                    JSONObject resbody  = json.getJSONObject("response");

                    JSONArray pArray = resbody.getJSONArray("docs");
                    int l = pArray.length();
                    if (l > 0) {
                        JSONObject obj = null;
                        JSONObject obj1 = null;
                        String productId = "";
                        String title = "";
                        String price = "";
                        String link = "";
                        String distance = "";
                        for (int i = 0; i < l; i++) {
                            obj = pArray.getJSONObject(i);
                            try {
                                productId = obj.getString("product_id");
                            } catch (Exception e) {
                                productId = "";
                            }
                            try {
                                title = obj.getString("title");
                            } catch (Exception e) {
                                title = "";
                            }
                            try {
                                price = obj.getString("price");

                            } catch (Exception e) {
                                price = "";
                            }
                            try {
                                link = obj.getString("full_img_link");

                            } catch (Exception e) {
                                link = "";
                            }

                            ProductConciseListSearch.productConciseListSearch
                                    .add(new Product_Concise(productId, title, price, link,distance));
                        }
                        if (ProductConciseListSearch.productConciseListSearch.size() > 0) {
                            // setAdaptertoGridView(ProductConciseList.productConciseList);
                            setAdaptertoGridView();
                        } else {
                            Toast.showSmallToast(context, "No Data found.");
                        }
                    } else {
                        Toast.showSmallToast(context, "No Data found.");
                    }
                } else {

                    //mDilatingDotsProgressBar.hideNow();
                    SearchInputActivity.this.finish();
                    Toast.showSmallToast(context, "Search is temporarily unavailable");
                    if(IsLocationChanged.isChanged)
                    {
                        IsLocationChanged.isChanged = false;

                        Intent intent = new Intent(SearchInputActivity.this,ProductListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    ANIM.onBack(activity);

                    //progressDialog.dismiss();
                }
            } catch (JSONException e) {
                Log.e("errr", e.getMessage());

                //mDilatingDotsProgressBar.hideNow();

            } finally {
                progress1.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        };



    }


}

