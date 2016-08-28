package com.at.solcoal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.at.solcoal.adapter.GridViewAdapterProductForUser;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.data.ProductConciseList;
import com.at.solcoal.model.Product_Concise;
import com.at.solcoal.model.UserInfo;
import com.at.solcoal.utility.NI;
import com.at.solcoal.utility.SharedPreferenceUtility;
import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.at.solcoal.adapter.GridViewAdapterProductForUser;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class ProductsFragment extends android.support.v4.app.Fragment {


    private GridViewAdapterProductForUser	productGridViewAdapter	= null;
    private GridView gridView = null;
    private UserInfo						userInfo				= null;
    private String							ownerId					= null;
    private UserInfo						ownerInfo				= null;
    private ProgressDialog					progressDialog			= null;
    private int								imageWidth;
    private CircleProgressBar progress1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gridView = (GridView) inflater.inflate(
                R.layout.fragment_products, container, false);


        userInfo = SharedPreferenceUtility.getUserInfo(getActivity());

        ownerId = getActivity().getIntent().getStringExtra("owner_id");

        fetchProductConciseList(userInfo.getId());

        return gridView;
    }






    private void fetchProductConciseList(String userIdStr)
    {
        ProductConciseList.clearForUser();
        //gridView = (GridView) findViewById(R.id.grid_view_upl);
        initializeGridLayout();
        progressDialog = new ProgressDialog(getContext(), R.style.ProgressDialogTheme);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        com.at.solcoal.web.AsyncWebClient asyncWebClient = new com.at.solcoal.web.AsyncWebClient();
        asyncWebClient.SetUrl(AppConstant.URL);
        RequestParams reqParam = new RequestParams();
        reqParam.add("action", "product_get_by_user_id");
        if (ownerId != null)
        {
            reqParam.add("user_id", ownerId);
        }
        else
        {
            reqParam.add("user_id", userIdStr);
        }

        asyncWebClient.post(reqParam, new JsonHttpResponseHandler() {
            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(int statusCode, org.apache.http.Header[] headers, JSONObject jsr)
            {
                try
                {
                    String responseCode = jsr.getString("response_code");
                    if (responseCode.equals("1"))
                    {
                        JSONArray pArray = jsr.getJSONArray("data");
                        int l = pArray.length();
                        if (l > 0)
                        {
                            JSONObject obj = null;
                            String productId = "";
                            String title = "";
                            String price = "";
                            String link = "";
                            String distance = "";
                            String userId = "";
                            for (int i = 0; i < l; i++)
                            {
                                obj = pArray.getJSONObject(i);
                                try
                                {
                                    productId = obj.getString("product_id");
                                }
                                catch (Exception e)
                                {
                                    productId = "";
                                }
                                try
                                {
                                    title = obj.getString("title");
                                }
                                catch (Exception e)
                                {
                                    title = "";
                                }
                                try
                                {
                                    price = obj.getString("price");
                                }
                                catch (Exception e)
                                {
                                    price = "";
                                }
                                try
                                {
                                    link = obj.getJSONArray("images").getJSONObject(0).getString("prod_img_link");
                                }
                                catch (Exception e)
                                {
                                    link = "";
                                }
                                try
                                {
                                    distance = obj.getString("distance");
                                }
                                catch (Exception e)
                                {
                                    distance = "";
                                }
                                try
                                {
                                    userId = obj.getString("user_id");
                                }
                                catch (Exception e)
                                {
                                    userId = "";
                                }
                                ProductConciseList.productConciseListForUser
                                        .add(new Product_Concise(productId, title, price, link, distance, userId));
                            }
                            if (ProductConciseList.productConciseListForUser.size() > 0)
                            {
                                setAdaptertoGridView();
                            }
                            else
                            {
                                com.at.solcoal.utility.Toast.showSmallToast(getContext(), "No Data found.");
                            }
                        }
                        else
                        {
                            com.at.solcoal.utility.Toast.showSmallToast(getContext(), "No Data found.");
                        }
                    }
                    else
                    {
                        com.at.solcoal.utility.Toast.showSmallToast(getContext(), "Error!!!");
                        progressDialog.dismiss();
                    }
                }
                catch (JSONException e)
                {
                    Log.e("errr", e.getMessage());
                }
                finally
                {
                    progressDialog.dismiss();
                }
            }

            ;

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                                  Throwable throwable)
            {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressDialog.dismiss();
                com.at.solcoal.utility.Toast.showNetworkErrorMsgToast(getContext());
            }

            ;
        });
    }

    private void setAdaptertoGridView()
    {
        productGridViewAdapter = new GridViewAdapterProductForUser(getActivity(), imageWidth);
        gridView.setAdapter(productGridViewAdapter);
    }

    private void initializeGridLayout()
    {
        int NUM_OF_COLUMNS = 2; // Number of columns of Grid View
        int GRID_PADDING1 = 8; // Gridview image padding in dp
        int GRID_PADDING2 = 8; // Gridview image padding in dp
        float padding1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, GRID_PADDING1,
                getResources().getDisplayMetrics());
        float padding2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, GRID_PADDING2,
                getResources().getDisplayMetrics());
		/**/
        Display display = getActivity().getWindowManager().getDefaultDisplay();
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



}
