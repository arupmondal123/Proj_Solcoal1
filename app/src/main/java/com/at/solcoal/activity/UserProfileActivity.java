package com.at.solcoal.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.at.solcoal.utility.Toast;
import com.at.solcoal.ProductsFragment;
import com.at.solcoal.R;
import com.at.solcoal.StoresFragment;
import com.at.solcoal.adapter.GridViewAdapterProductForUser;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.data.ProductConciseList;
import com.at.solcoal.model.Product_Concise;
import com.at.solcoal.model.UserInfo;
import com.at.solcoal.utility.NI;
import com.at.solcoal.utility.SharedPreferenceUtility;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private UserInfo userInfo				= null;
    private String							ownerId					= null;
    private int							pageSelected				= 0;
    private String fragmentOpen 				;
    private ViewPager viewPager;
    private UserInfo						ownerInfo				= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Intent intent = getIntent();


        fragmentOpen = intent.getStringExtra("fragmentToOpen");
        if (fragmentOpen == null) {
            fragmentOpen = "Inventory";
        }

        //final String cheeseName = intent.getStringExtra(EXTRA_NAME);
        //Toast.showLongToast(UserProfileActivity.this,"fragmentOpen="+fragmentOpen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("");

        userInfo = SharedPreferenceUtility.getUserInfo(UserProfileActivity.this);
/*
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(userInfo.getName());
*/

        ownerId = getIntent().getStringExtra("owner_id");
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        if (viewPager != null) {
            setupViewPager(viewPager);
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageSelected = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //Window window = UserProfileActivity.this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:

        /*
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(UserProfileActivity.this.getResources().getColor(R.color.profile_systembar_color));
        //loadBackdrop();
        */




        Log.e("UserProfileActivity", "onerid="+ownerId);
        if (ownerId != null)
        {

            Log.e("UserProfileActivity", "enteredonerid=" + ownerId);
            fetchOwnerAndProductListDetails();

            ((FloatingActionButton) findViewById(R.id.fab)).setVisibility(View.GONE);
        }
        else {

            ((TextView) findViewById(R.id.user_profile_name)).setText(toCamelCase(userInfo.getName()));
            ((FloatingActionButton) findViewById(R.id.fab)).setVisibility(View.VISIBLE);
        }


        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (pageSelected == 0) {
                    startProductAddPictureActivity();
                } else {
                    startAddStoreActivity();
                }
            }

        });

    }

    private void fetchOwnerAndProductListDetails()
    {
        com.at.solcoal.web.AsyncWebClient asyncWebClient = new com.at.solcoal.web.AsyncWebClient();
        asyncWebClient.SetUrl(AppConstant.URL);
        RequestParams reqParam = new RequestParams();
        reqParam.add("action", "user_get_by_id");
        reqParam.add("user_id", ownerId);
        Log.e("UserProfileActivity", "fetchOwnerAndProductListDetails");
        asyncWebClient.post(reqParam, new JsonHttpResponseHandler() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject jsr) {
                        try {
                            Log.e("UserProfileActivity", "onSuccess");
                            String responseCode = jsr.getString("response_code");
                            if (responseCode.equals("1")) {
                                JSONObject obj = jsr.getJSONObject("data");
                                ownerInfo = new UserInfo();
                                ownerInfo.setId(obj.getString("user_id"));
                                ownerInfo.setName(obj.getString("user_name"));
                                ownerInfo.setEmail(obj.getString("user_email_id"));
                                ownerInfo.setGender(obj.getString("user_sex"));
                                ((TextView) findViewById(R.id.user_profile_name)).setText(toCamelCase(ownerInfo.getName()));
                                Log.e("UserProfileActivity", "onerino=" + obj.getString("user_name"));
                                // ownerInfo.setImageUrl(obj.getString("user_image_link"));
                                //fetchOwnerAndProductListDetails(ownerInfo);
                            } else {
                                //Toast.showSmallToast(context, "Error!!!");
                                Log.e("UserProfileActivity", "onError");
                            }
                        } catch (JSONException e) {
                            Log.e("errr", e.getMessage());
                        }
                    }

                    ;

                    @Override
                    public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                                          Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.e("UserProfileActivity", "onFailure");
                        Toast.showNetworkErrorMsgToast(UserProfileActivity.this);
                    }

                    ;
                });

    }

    private void landOnUserDetails()
    {

        Intent intentLocal = new Intent(UserProfileActivity.this, UserLoginInfo.class);
        intentLocal.putExtra("userInfo", userInfo);
        intentLocal.putExtra("edit_view_profile", "Edit Profile");
        intentLocal.putExtra(AppConstant.USER_INFO_ACTION, AppConstant.USER_INFO_USER);
        startActivity(intentLocal);
    }

    public void onBack(View view)
    {
        onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            //Intent intent = new Intent(ProductListActivity.this, SearchInputActivity.class);
            //startActivityForResult(intent, 2);
            landOnUserDetails();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void startProductAddPictureActivity()
    {
        Intent intent = new Intent(UserProfileActivity.this, ProductAddPictureActivity.class);


        // TODO

        intent.putExtra("from_", "ProductListActivity");
        startActivity(intent);
    }

    protected void startAddStoreActivity()
    {

        Intent intent = new Intent(UserProfileActivity.this, AddStoreActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        userInfo = SharedPreferenceUtility.getUserInfo(UserProfileActivity.this);
        if (ownerId != null)
        {
        //    ((TextView) findViewById(R.id.user_profile_name)).setText(toCamelCase(ownerInfo.getName()));
        }
        else {
            ((TextView) findViewById(R.id.user_profile_name)).setText(toCamelCase(userInfo.getName()));
        }
        if (fragmentOpen.equals("Inventory")) {
            viewPager.setCurrentItem(0);
            pageSelected =0;
        }
        else{
            viewPager.setCurrentItem(1);
            pageSelected = 1;
        }

    }


    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        if (ownerId != null)
        {

        }
        else {
            adapter.addFragment(new ProductsFragment(), "Inventory");
        }

        adapter.addFragment(new StoresFragment(), "Shops");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (ownerId != null)
        {
            //do not populate menu for third party profile
        }
        else {
            getMenuInflater().inflate(R.menu.menu_profile, menu);
        }

        return true;
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    public static String toCamelCase(String s){
        if(s.length() == 0){
            return s;
        }
        String[] parts = s.split(" ");
        String camelCaseString = "";
        for (String part : parts){
            camelCaseString = camelCaseString + toProperCase(part) + " ";
        }
        return camelCaseString;
    }

    public static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }


}
