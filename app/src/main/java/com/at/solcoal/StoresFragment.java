package com.at.solcoal;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.at.solcoal.adapter.MyRecyclerAdapter;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.data.FeedItem;
import com.at.solcoal.data.ProductConciseList;
import com.at.solcoal.model.Product_Concise;
import com.at.solcoal.model.UserInfo;
import com.at.solcoal.utility.SharedPreferenceUtility;
import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class StoresFragment extends Fragment {

    private List<FeedItem> feedItemList = new ArrayList<FeedItem>();

    private RecyclerView mRecyclerView;

    private MyRecyclerAdapter adapter;
    private UserInfo userInfo				= null;
    private CircleProgressBar progress1;
    private String							ownerId					= null;
    private List<FeedItem> feedsList;
    private RecyclerView rv;
    private Boolean ownprofile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = (RecyclerView) inflater.inflate(
                R.layout.fragment_stores, container, false);
        setupRecyclerView(rv);
        //rv.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        //rv.getItemAnimator().setMoveDuration(1000);
        userInfo = SharedPreferenceUtility.getUserInfo(getActivity());
        ownerId = getActivity().getIntent().getStringExtra("owner_id");
        //fetchShopList(userInfo.getId());
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        /*recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                getRandomSublist(Cheeses.sCheeseStrings, 30)));*/
    }

    @Override
    public void onResume() {
        super.onResume();
        //adapter.notifyDataSetChanged();
        fetchShopList(userInfo.getId());
    }

    private void fetchShopList(String userIdStr)
    {
        /*ProductConciseList.clearForUser();
        gridView = (GridView) findViewById(R.id.grid_view_upl);
        initializeGridLayout();
        progressDialog = new ProgressDialog(getContext(), R.style.ProgressDialogTheme);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();*/
        com.at.solcoal.web.AsyncWebClient asyncWebClient = new com.at.solcoal.web.AsyncWebClient();
        asyncWebClient.SetUrl(AppConstant.URL);
        RequestParams reqParam = new RequestParams();
        reqParam.add("action", "shop_get_by_user");
        if (ownerId != null)
        {
            reqParam.add("user_id", ownerId);
            ownprofile = false;
        }
        else
        {
            reqParam.add("user_id", userIdStr);
            ownprofile = true;
        }


        asyncWebClient.post(reqParam, new JsonHttpResponseHandler() {
            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject jsr) {
                try {
                    String responseCode = jsr.getString("response_code");
                    if (responseCode.equals("1")) {
                        JSONArray pArray = jsr.getJSONArray("data");
                        int l = pArray.length();
                        feedsList = new ArrayList<>();
                        //Log.e("MyRecyclerAdapter", "array count="+l);
                        if (l > 0) {
                            JSONObject obj = null;
                            String shop_name = "";
                            String shop_id = "";
                            String shop_desc = "";
                            String shop_contact_email = "";
                            String shop_phone_no = "";
                            String shop_weblink = "";
                            String shop_active_ind = "";

                            for (int i = 0; i < l; i++) {
                                obj = pArray.getJSONObject(i);
                                try {
                                    shop_name = obj.getString("shop_name");
                                    shop_id = obj.getString("shop_id");
                                    shop_desc = obj.getString("shop_desc");
                                    shop_contact_email = obj.getString("shop_contact_email");
                                    shop_phone_no = obj.getString("shop_phone_no");
                                    shop_weblink = obj.getString("shop_online_weblink");
                                    shop_active_ind = obj.getString("shop_active_ind");
                                    //com.at.solcoal.utility.Toast.showSmallToast(getContext(), shop_name);
                                    FeedItem item = new FeedItem();
                                    item.setTitle(shop_name);
                                    item.setShop_id(shop_id);
                                    item.setShop_desc(shop_desc);
                                    item.setShop_contact_email(shop_contact_email);
                                    item.setShop_phone_no(shop_phone_no);
                                    item.setShop_weblink(shop_weblink);
                                    item.setShop_active_ind(shop_active_ind);


                                    feedsList.add(item);
                                } catch (Exception e) {
                                    shop_name = "";
                                }

                            }
                            if (feedsList.size() > 0) {
                                adapter = new MyRecyclerAdapter(getActivity(), feedsList,ownprofile);
                                rv.setAdapter(adapter);

                            } else {
                                com.at.solcoal.utility.Toast.showSmallToast(getContext(), "No Data found.");
                            }
                        } else {
                            com.at.solcoal.utility.Toast.showSmallToast(getContext(), "No Data found.");
                        }
                    } else {
                        com.at.solcoal.utility.Toast.showSmallToast(getContext(), "Error!!!");
                        //progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e("errr", e.getMessage());
                } finally {
                    //progressDialog.dismiss();
                }
            }



            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                //progressDialog.dismiss();
                com.at.solcoal.utility.Toast.showNetworkErrorMsgToast(getContext());
            }

            ;
        });
    }

}
