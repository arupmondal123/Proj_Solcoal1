package com.at.solcoal.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.account.user.UserClientService;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.at.solcoal.R;
import com.at.solcoal.adapter.GridViewAdapterProduct;
import com.at.solcoal.animation.ANIM;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.data.GridViewAdapterProductData;
import com.at.solcoal.data.ImageUri;
import com.at.solcoal.data.IsLocationChanged;
import com.at.solcoal.data.ProductConciseList;
import com.at.solcoal.data.UserLocation;
import com.at.solcoal.model.Product_Concise;
import com.at.solcoal.model.UserInfo;
import com.at.solcoal.sharedpref.SharedPref;
import com.at.solcoal.utility.IMAGE_UTIL;
import com.at.solcoal.utility.NI;
import com.at.solcoal.utility.Network;
import com.at.solcoal.utility.ScrollableSwipeRefreshLayout;
import com.at.solcoal.utility.SessionManager;
import com.at.solcoal.utility.SharedPreferenceUtility;
import com.at.solcoal.utility.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import static com.at.solcoal.activity.UserProfileActivity.toCamelCase;

public class ProductListActivity extends AppCompatActivity {

    private Context context = null;

    private Activity activity = null;

    private String TAG;

    private LinearLayout profile = null;

    private TextView user_location = null;

    private GridView gridView = null;

    private int imageWidth;

    private ProgressDialog progressDialog = null;

    private GridViewAdapterProduct productGridViewAdapter = null;
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
    private int backButtonCount = 0;

    SearchView search;

    ImageButton imageButton;

    //private SwipeRefreshLayout mSwipeRefreshLayout;

    private ScrollableSwipeRefreshLayout mSwipeRefreshLayout;

    private DilatingDotsProgressBar mDilatingDotsProgressBar = null;
    private CircleProgressBar progress1;
    private Boolean layoutRefresh;


    private DrawerLayout mDrawer;
    private LinearLayout linearLayout;
    private NavigationView nvDrawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private MenuItem searchMenuItem;
    private boolean showShopgroup = false;
    private LinearLayout name_initial_layout;
    private TextView sign_in_register_tv;
    private TextView username;
    private String user_has_shops = "no";
    private View headerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_product_list);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        // Setup drawer view

        setupDrawerContent(nvDrawer);

        //drawerToggle = setupDrawerToggle();

        drawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                //getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.setDrawerListener(drawerToggle);


        mSwipeRefreshLayout = (ScrollableSwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        //mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        progress1 = (CircleProgressBar) findViewById(R.id.progressBar);
        progress1.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        linearLayout = (LinearLayout) findViewById(R.id.body);
        context = ProductListActivity.this;
        activity = ProductListActivity.this;
        TAG = "ProductListActivity";
        Log.e(TAG + "_", "onCreate()");
        layoutRefresh = false;
        setupWindowAnimations();
        backButtonCount = 0;


        //mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);


		/**/

        try {
            if (getIntent().getExtras().getString(AppConstant.START_APP).equals(AppConstant.START_APP_FIRST_TIME)) {
                fetchProductConciseListAndSetHeaderAddress();


            }
        } catch (Exception e) {
            Log.e(TAG + "", "Not First Time.");
            // if (ProductConciseList.productConciseList.size() > 0)
            // {
            // setAdaptertoGridView(ProductConciseList.productConciseList);
            // } else
            // {
            // Toast.showSmallToast(context, "No Data found.");
            // }
        }
        if (IsLocationChanged.isChanged) {
            IsLocationChanged.isChanged = false;
            fetchProductConciseListAndSetHeaderAddress();
        }

        gridView = (GridView) findViewById(R.id.grid_view);
        InitializeGridLayout();


        imageButton = (ImageButton) findViewById(R.id.locationimageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGoogleMapActivity();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new ScrollableSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                layoutRefresh = true;

                fetchProductConciseListAndSetHeaderAddress();

            }
        });

        /*Code to setup Header*/
        headerView = nvDrawer.getHeaderView(0);
        sign_in_register_tv = (TextView) headerView.findViewById(R.id.sign_in_register_tv);

        //RelativeLayout logout = (RelativeLayout) findViewById(R.id.logout);
        name_initial_layout = (LinearLayout) headerView.findViewById(R.id.name_initial_layout);
        setupNavHeader();
        CheckifShopMenutoShow();


    }

    private void setupNavHeader() {
        if (isUserLoggedIn()) {
            String userName = getUserName();
            //Toast.showSmallToast(context, "User Name"+userName);
            ((TextView) headerView.findViewById(R.id.username_tv)).setText(toCamelCase(userName));
            ((TextView) headerView.findViewById(R.id.name_initial)).setText(NI.getInitial(userName));
            sign_in_register_tv.setVisibility(View.GONE);

            View.OnClickListener onCLick = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    browseUserProduct();
                }
            };

            name_initial_layout.setOnClickListener(onCLick);
            name_initial_layout.setVisibility(View.VISIBLE);
			/* change */
            ((RelativeLayout) headerView.findViewById(R.id.relativelayoutheader)).setOnClickListener(onCLick);
            //logout.setVisibility(View.VISIBLE);
        } else {
            name_initial_layout.setVisibility(View.GONE);
            username = (TextView) headerView.findViewById(R.id.username_tv);
            username.setVisibility(View.GONE);
            View.OnClickListener clickL = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startSignInActivity();
                }
            };
            sign_in_register_tv.setOnClickListener(clickL);
			/* change */
            ((RelativeLayout) headerView.findViewById(R.id.relativelayoutheader)).setOnClickListener(clickL);
            sign_in_register_tv.setVisibility(View.VISIBLE);
            //logout.setVisibility(View.GONE);


        }


    }


    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Menu m = nvDrawer.getMenu();
                        /*
                        for (int i = 0; i < m.size(); i++) {
                            MenuItem mi = m.getItem(i);
                            if (!(mi.getItemId() == menuItem.getItemId())) {
                                mi.setChecked(false);
                            }
                        }*/


                        menuItem.setChecked(true);
                        selectDrawerItem(menuItem);
                        return false;
                    }
                });


    }

    public void selectDrawerItem(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.browse_product:
                linearLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.list_product:
                startProductAddProductActivity();
                break;
            case R.id.my_chats:
                if (SessionManager.isUserLoggedIn(ProductListActivity.this)) {
                    Intent intent = new Intent(ProductListActivity.this, ConversationActivity.class);
                    if (ApplozicClient.getInstance(ProductListActivity.this).isContextBasedChat()) {
                        intent.putExtra(ConversationUIService.CONTEXT_BASED_CHAT, true);
                    }
                    startActivity(intent);
                    return;

                } else {
                    Intent intent = new Intent(ProductListActivity.this, SigninActivity.class);
                    // TODO
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;
            case R.id.share:
                menuItem.setChecked(true);
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "DrGrep");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=Orion.Soft \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
                break;
            case R.id.info:
                Intent intent = new Intent(ProductListActivity.this, InfoActivity.class);
                startActivity(intent);
                break;
            case R.id.myShop:
                Intent intent2 = new Intent(ProductListActivity.this, UserProfileActivity.class);
                intent2.putExtra("userInfo", userInfo);
                intent2.putExtra("fragmentToOpen", "Shop");
                startActivity(intent2);
                break;
            //case R.id.shop_settings:
                /*
                Intent intent3 = new Intent(ProductListActivity.this, ShopSettingsActivity.class);
                intent3.putExtra("userInfo", userInfo);
                startActivity(intent3);
                break;*/
            default:
                break;
        }

        try {
            //fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle("Products");
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    public MenuItem getSearchMenuItem() {
        return searchMenuItem;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.e(TAG + "_", "onCreateOptionsMenu()");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView search = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchInputActivity.class)));
        search.setQueryHint(getResources().getString(R.string.search_hint));

        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.INVISIBLE);
            }
        });

        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                linearLayout.setVisibility(View.VISIBLE);
                return false;
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MenuItem searchMenuItem = getSearchMenuItem();
                if (searchMenuItem != null) {
                    searchMenuItem.collapseActionView();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // ...
                return true;
            }
        });



        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_Logout) {
            signOut();
            new UserClientService(ProductListActivity.this).logout();
            return true;
        }
        if (id == R.id.menu_search) {

            Intent intent = new Intent(ProductListActivity.this, SearchInputActivity.class);
            startActivityForResult(intent, 2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.e(TAG + "_", "onPrepareOptionsMenu()");
        MenuItem logoutmenu = menu.findItem(R.id.action_Logout);
        if (isUserLoggedIn()) {
            logoutmenu.setVisible(true);
        } else {
            logoutmenu.setVisible(false);
        }

        SharedPreferences sharedpreferences = getSharedPreferences(AppConstant.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedpreferences != null && (sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_ID, null) != null)) {
            user_has_shops = sharedpreferences.getString(AppConstant.USER_HAS_SHOPS, "no");
            //Toast.showLongToast(ProductListActivity.this,"inside sharedpreferences");
        }
        if (user_has_shops.equals("yes")){
            //Toast.showLongToast(ProductListActivity.this,"user_has_shops"+sharedpreferences.getString(AppConstant.USER_HAS_SHOPS, null));

            Menu drawermenu = nvDrawer.getMenu();
            //nvDrawer.getMenu().setGroupCheckable(R.id.mnu_shop_group, true, true);
            //nvDrawer.getMenu().setGroupVisible(R.id.mnu_shop_group, true);
            //menu.add(R.id.mnu_shop_group, 6, 600, "My shops");
            //menu.add(R.id.mnu_shop_group, 7, 700, "Shop Settings");
            //drawermenu.setGroupCheckable(R.id.mnu_shop_group, true, true);
            drawermenu.setGroupVisible(R.id.mnu_shop_group, true);
        }
        else {
            nvDrawer.getMenu().setGroupVisible(R.id.mnu_shop_group, false);
            //Toast.showSmallToast(ProductListActivity.this, "outside showShopgroup ");
        }

        return true;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


    private void setupWindowAnimations() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = null;
            Explode explode = null;

            explode = new Explode();
            //explode.setStartDelay(1000);
            explode.setDuration(getResources().getInteger(R.integer.anim_duration_medium));


            Fade fade = new Fade();
            fade.setDuration(2000);

            getWindow().setEnterTransition(explode);
            getWindow().setReenterTransition(explode);

            getWindow().setExitTransition(fade);
            /*
            slide = new Slide();
            slide.setDuration(1500);
            slide.setSlideEdge(Gravity.LEFT);
            getWindow().setEnterTransition(slide);*/
            //getWindow().setExitTransition(slide);
            //getWindow().setExitTransition(slide);
            //getWindow().setReenterTransition(slide);
        }

    }

    private void fetchProductConciseListAndSetHeaderAddress() {
        fetchProductConciseList();
        //setHeaderAddress();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG + "_", "onStart()");
        // fetchProductConciseList();
        // setHeaderAddress();
    }

    /* */
    @Override
    protected void onResume() {
        super.onResume();
        //setupNavHeader();
        //invalidateOptionsMenu();
        backButtonCount = 0;

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
        //invalidateOptionsMenu();
        Log.e(TAG + "_", "onRestart()");
        try {
            revokeImageOnClickListenerOnProductGridView();
        } catch (Exception e) {
        }
    }

    private void revokeImageOnClickListenerOnProductGridView() {
        for (int i = 0; i < GridViewAdapterProductData.imagelist.size(); i++) {
            GridViewAdapterProductData.imagelist.get(i)
                    .setOnClickListener(GridViewAdapterProductData.imageOnImageClickListenerList.get(i));
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

    /*
        private void setHeaderAddress()
        {
            final int selected = R.drawable.style__2_selected;
            profile = (LinearLayout) findViewById(R.id.profile);
            user_location = (TextView) findViewById(R.id.user_location);
            View.OnClickListener onclick1 = new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    startGoogleMapActivity();
                }
            };
            profile.setOnClickListener(onclick1);
            // user_location.setOnClickListener(onclick1);
            setUserLocationText();
        }
    */
    /* /change */
	/*
	 *
	 *
	 *
	 *
	 * */
    public void startGoogleMapActivity() {
        Intent intent = new Intent(ProductListActivity.this, GoogleMapActivity.class);
        intent.putExtra(AppConstant.MAP_JOB_TYPE, AppConstant.CHANGE_USER_LOCATION);
        startActivity(intent);
        // gridView.setOnDragListener(l);

    }

    private void setUserLocationText() {
        try {
            String str = UserLocation.getUserAddress();
            user_location.setText(str);
            if (str.contains(",")) {
                str = str.substring(0, str.indexOf(","));
                user_location.setText(str);
            }
        } catch (Exception e) {
        }
    }

    private void fetchProductConciseList() {
        if (Network.isConnected(context)) {
            fetchProductConciseList(UserLocation.getUserPreferredLatLng().latitude + "",
                    UserLocation.getUserPreferredLatLng().longitude + "");
        } else {
            Toast.showNetworkErrorMsgToast(context);
        }
    }

    private void setAdaptertoGridView(ArrayList<Product_Concise> productConciseList2) {
        // adapter = new GridViewAdapterProduct(activity, productConciseList2,
        // imageWidth);
        // gridView.setAdapter(adapter);
    }

    private void setAdaptertoGridView() {
//		productGridViewAdapter = new GridViewAdapterProduct(activity, imageWidth);
        productGridViewAdapter = new GridViewAdapterProduct(activity, imageWidth, this);

        gridView.setAdapter(productGridViewAdapter);
    }


    private void clearProductConciseList() {
        try {
            ProductConciseList.productConciseList.clear();
        } catch (Exception e) {
        }
    }

    private void fetchProductConciseList(String latitude, String longitude) {
        clearProductConciseList();

        if (layoutRefresh == false) {
            progress1.setVisibility(View.VISIBLE);

        }
	/*
		progressDialog = new ProgressDialog(context, R.style.ProgressDialogTheme);
		progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
		// progressDialog.getWindow().setBackgroundDrawable(new
		// ColorDrawable(android.graphics.Color.TRANSPARENT));
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


		// dialog.setMessage(getResources().getString(R.string.please_wait___));
		progressDialog.setIndeterminate(true);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	*/


        com.at.solcoal.web.AsyncWebClient asyncWebClient = new com.at.solcoal.web.AsyncWebClient();
        asyncWebClient.SetUrl(AppConstant.URL);
        RequestParams reqParam = new RequestParams();
        reqParam.add("action", "product_get");
        reqParam.add("lat", latitude.trim());
        reqParam.add("long", longitude.trim());
		/**/
        // Log.e(TAG + "+fetchProductConciseList_reqParam",
        // reqParam.toString());
        asyncWebClient.post(reqParam, new JsonHttpResponseHandler() {
            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject jsr) {
                try {
                    String responseCode = jsr.getString("response_code");
					/**/
                    Log.e("response_code", responseCode);
                    if (responseCode.equals("1")) {
                        JSONArray pArray = jsr.getJSONArray("data");
                        int l = pArray.length();
                        if (l > 0) {
                            JSONObject obj = null;
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
                                    link = obj.getJSONArray("images").getJSONObject(0).getString("prod_img_link");
                                } catch (Exception e) {
                                    link = "";
                                }
                                try {
                                    distance = obj.getString("distance");
                                } catch (Exception e) {
                                    distance = "";
                                }
                                ProductConciseList.productConciseList
                                        .add(new Product_Concise(productId, title, price, link, distance));
                            }
                            if (ProductConciseList.productConciseList.size() > 0) {
                                // setAdaptertoGridView(ProductConciseList.productConciseList);
                                setAdaptertoGridView();
                            } else {
                                Toast.showSmallToast(context, "No Data found.");
                            }
                        } else {
                            Toast.showSmallToast(context, "No Data found.");
                        }
                    } else {
                        Toast.showSmallToast(context, "Error!!!");
                        //mDilatingDotsProgressBar.hideNow();
                        progress1.setVisibility(View.INVISIBLE);
                        linearLayout.setVisibility(View.VISIBLE);

                        //progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e("errr", e.getMessage());
                } finally {
                    //progressDialog.dismiss();
                    progress1.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    //mDilatingDotsProgressBar.hideNow();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            ;

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                //progressDialog.dismiss();
                //mDilatingDotsProgressBar.hideNow();
                progress1.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                Toast.showNetworkErrorMsgToast(context);
            }

            ;
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // clearProductConciseList();
        //ProductListActivity.this.finish();

        //android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(0);
        if (backButtonCount >= 1) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.showSmallToast(context, "Press the back button once again to close DrGrep");
            //Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
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

    private void takePhoto() {
        String fileName = "random1234567.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		/**/
        values.clear();
        startActivityForResult(intent, CAPTURE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.showSmallToast(context, "Entered onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE) {
            String str = IMAGE_UTIL.getActualFilePathFromTempUri(activity, imageUri);
            if (resultCode == RESULT_OK) {
                ImageUri.images.put(str, AppConstant.Camera);
                startAddproduct();
            } else if (resultCode == RESULT_CANCELED) {
                clearImageUrisAndDeleteBlankFile(str);
                Toast.showSmallToast(context, getResources().getString(R.string.photo_was_not_taken));
            } else {
                clearImageUrisAndDeleteBlankFile(str);
                Toast.showSmallToast(context, getResources().getString(R.string.photo_was_not_taken));
            }
        }
        if (requestCode == 2) {
            //Toast.showSmallToast(context, "Entered requestcode");
            linearLayout.setVisibility(View.VISIBLE);
        }

    }

    private void startAddproduct() {
        Intent intent = new Intent(ProductListActivity.this, ProductAddProductActivity.class);
        intent.putExtra(AppConstant.ADD_EDIT, AppConstant.ADD);
        // ProductListActivity.this.finish();
        startActivity(intent);
        ANIM.onStartActivity(ProductListActivity.this);
    }

    private void clearImageUrisAndDeleteBlankFile(String str) {
        try {
            ImageUri.images.clear();
        } catch (Exception e) {
        }
        try {
            (new File(str)).delete();
        } catch (Exception e) {
            Log.e("(new File(str)).delete();", e.getMessage());
        }
    }

	/*
	 *
	 *
	 *
	 * */

    /* change */
    @SuppressWarnings("deprecation")


    private void browseUserProduct() {
        //Intent intent = new Intent(ProductListActivity.this, UserProductList.class);
        Intent intent = new Intent(ProductListActivity.this, UserProfileActivity.class);
        intent.putExtra("userInfo", userInfo);
        intent.putExtra("fragmentToOpen", "Inventory");
        startActivity(intent);
    }

    protected void startProductAddProductActivity() {

        if (SessionManager.isUserLoggedIn(ProductListActivity.this)) {
            Intent intent = new Intent(ProductListActivity.this, ProductAddPictureActivity.class);
            intent.putExtra("from_", "ProductListActivity");
            startActivity(intent);

        } else {
            Intent intent = new Intent(ProductListActivity.this, SigninActivity.class);
            // TODO
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        // TODO Auto-generated method stub

    }

    public void closeDrawer() {
        try {
            slidingDrawer.close();
        } catch (Exception e) {
        }
    }

    public boolean isDrawerOpen() {
        return mDrawer.isDrawerOpen(GravityCompat.START);
    }

    private void openLoginActivity() {
        String user_id;
        SharedPreferences sharedpreferences = getSharedPreferences(AppConstant.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedpreferences != null && (sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_ID, null) != null)) {
            user_id = sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_ID, null);
            if (user_id == null) {
                Intent intent = new Intent(ProductListActivity.this, SigninActivity.class);
                intent.putExtra(AppConstant.AFTER_LOGIN_ACTION, "after_login_action");
                startActivity(intent);
            } else {
                Intent chatHoldingActivity = new Intent(this, UserChatEntryHoldingPage.class);
                chatHoldingActivity.putExtra("userId", user_id);
                startActivity(chatHoldingActivity);
            }
        }

    }

    // private void browseUserProduct()
    // {
    // Intent intent = new Intent(ProductListActivity.this, UserProductList.class);
    //// intent.putExtra("userInfo",
    // SharedPreferenceUtility.getUserInfo(ProductListActivity___.this));
    // startActivity(intent);
    // }

    protected void startSignInActivity() {

        Intent intent = new Intent(ProductListActivity.this, SigninActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private String getUserName() {
        // TODO returns userName
        String userName = "Name";
        try {
            userName = SharedPreferenceUtility.getUserInfo(ProductListActivity.this).getName();

        } catch (Exception e) {

        }
        return userName;
    }

    public void afterLoginActivity(String afterLoginAction) {
        SharedPreferences sharedpreferences = getSharedPreferences(AppConstant.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedpreferences != null) {
            userIdStr = sharedpreferences.getString(AppConstant.LOGIN_PREFERENCE_ID, null);
            if (userIdStr == null) {
                Intent intent = new Intent(ProductListActivity.this, SigninActivity.class);
                intent.putExtra(AppConstant.AFTER_LOGIN_ACTION, afterLoginAction);
                startActivity(intent);
            } else {
                if (AppConstant.AFTER_LOGIN_LIST_PRODUCT.equals(afterLoginAction)) {
                    takePhoto();
                }
            }
        }
    }

    private boolean isUserLoggedIn() {
        return (null != getSharedPreferences(AppConstant.LOGIN_PREFERENCE, Context.MODE_PRIVATE)
                .getString(AppConstant.LOGIN_PREFERENCE_ID, null));
    }

    private void signOut() {
        intentLogout = new Intent(ProductListActivity.this, MainActivity.class);

        com.at.solcoal.web.AsyncWebClient asyncWebClient;

        asyncWebClient = new com.at.solcoal.web.AsyncWebClient();
        asyncWebClient.SetUrl(AppConstant.URL);

        userInfo = SharedPreferenceUtility.getUserInfo(ProductListActivity.this);

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
                    //hide login
                    //CheckifShopMenutoShow();
                    //name_initial_layout.setVisibility(View.GONE);
                    //username.setVisibility(View.GONE);
                    //sign_in_register_tv.setVisibility(View.VISIBLE);
                    //Toast.showLongToast(context, "Logged Out Successfully");

                }
            }

            ;

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                                  Throwable throwable) {
                dataRetrieved = true;
                super.onFailure(statusCode, headers, responseString, throwable);
                // dialog.dismiss();
                // Toast.showNetworkErrorMsgToast(context);

            }

            ;
        });
    }


    private void CheckifShopMenutoShow() {

        if (SessionManager.isUserLoggedIn(ProductListActivity.this)) {

            com.at.solcoal.web.AsyncWebClient asyncWebClient = new com.at.solcoal.web.AsyncWebClient();
            asyncWebClient.SetUrl(AppConstant.URL);
            RequestParams reqParam = new RequestParams();
            userInfo = SharedPreferenceUtility.getUserInfo(ProductListActivity.this);
            reqParam.add("action", "user_shop_entries_get");
            reqParam.add("seller_id", userInfo.getId());

            asyncWebClient.post(reqParam, new JsonHttpResponseHandler() {
                @SuppressWarnings("unchecked")
                @Override
                public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject jsr) {
                    try {
                        String responseCode = jsr.getString("response_code");
                        if (responseCode.equals("1")) {
                            SharedPreferences sharedpreferences = getSharedPreferences(AppConstant.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(AppConstant.USER_HAS_SHOPS, "yes");
                            editor.commit();
                            //invalidateOptionsMenu();
                            //Toast.showLongToast(ProductListActivity.this, "user_has_shops yes" + sharedpreferences.getString(AppConstant.USER_HAS_SHOPS, null));
                        } else {
                            SharedPreferences sharedpreferences = getSharedPreferences(AppConstant.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(AppConstant.USER_HAS_SHOPS, "no");
                            editor.commit();
                            //invalidateOptionsMenu ();
                        }
                    } catch (JSONException e) {
                        //Toast.showSmallToast(ProductListActivity.this, "Error!!!");
                        Log.e("errr", e.getMessage());
                    }
                }


                @Override
                public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                                      Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    //Toast.showNetworkErrorMsgToast(ProductListActivity.this);
                }
            });
        } else {

            SharedPreferences sharedpreferences = getSharedPreferences(AppConstant.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(AppConstant.USER_HAS_SHOPS, "no");
            editor.commit();
        }



    }
}


