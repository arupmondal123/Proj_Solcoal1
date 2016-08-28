package com.at.solcoal;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.at.solcoal.adapter.MyRecyclerProductsAdapter;
import com.at.solcoal.adapter.MyRecyclerProductsAdapterShopView;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.data.ProductConciseList;
import com.at.solcoal.model.Product_Concise;
import com.at.solcoal.model.Product_Concise_Shop;
import com.at.solcoal.model.UserInfo;
import com.at.solcoal.utility.SharedPreferenceUtility;
import com.at.solcoal.utility.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShopDetailProductViewFragment extends android.support.v4.app.Fragment {

    private List<Product_Concise> feedProductItemList = new ArrayList<Product_Concise>();
    SwitchCompat switchCompat;
    private RecyclerView mRecyclerView;

    private MyRecyclerProductsAdapterShopView adapter;
    private UserInfo userInfo				= null;
    private CircleProgressBar progress1;
    private String							ownerId					= null;
    private List<Product_Concise> feedsProductList;
    private RecyclerView rv;
    private GridLayoutManager gridLayoutManager;
    private String							shopid					= null;
    private RelativeLayout rlprogress1;
    private RelativeLayout rltextnoproduct;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop_detail_product_view, container, false);
        rv = (RecyclerView) rootView.findViewById(R.id.rv_recycler_view);
        //rv = (RecyclerView) inflater.inflate(
        //        R.layout.fragment_prouduct_add_to_shop, container, false);

        setupRecyclerView(rv);
        //rv.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        //rv.getItemAnimator().setMoveDuration(1000);
        userInfo = SharedPreferenceUtility.getUserInfo(getActivity());
        shopid = getActivity().getIntent().getStringExtra("shop_id_extra");
        ownerId = getActivity().getIntent().getStringExtra("owner_id");
        //rv.setHasFixedSize(true);
        //fetchShopList(userInfo.getId());
        progress1 = (CircleProgressBar) rootView.findViewById(R.id.progressBar);
        rlprogress1 = (RelativeLayout) rootView.findViewById(R.id.rl_progressBar);
        progress1 = (CircleProgressBar) rootView.findViewById(R.id.progressBar);
        progress1.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        //fetchShopList(userInfo.getId());
        progress1.setVisibility(View.VISIBLE);
        rlprogress1.setVisibility(View.VISIBLE);
        //fetchProductConciseList();
        rltextnoproduct = (RelativeLayout) rootView.findViewById(R.id.noproduct);
        return rootView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {


        //recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.card_margin2);
        recyclerView.addItemDecoration(itemDecoration);
        //recyclerView.setNestedScrollingEnabled(false);
        /*recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                getRandomSublist(Cheeses.sCheeseStrings, 30)));*/
    }




    @Override
    public void onResume() {
        super.onResume();
        fetchProductConciseList();
        //adapter.notifyDataSetChanged();

        //fetchShopList(userInfo.getId());
    }


    private void clearProductConciseList() {
        try {
            ProductConciseList.productConciseListForUserShop.clear();
        } catch (Exception e) {
        }
    }
    private void fetchProductConciseList() {

        clearProductConciseList();
        com.at.solcoal.web.AsyncWebClient asyncWebClient = new com.at.solcoal.web.AsyncWebClient();
        asyncWebClient.SetUrl(AppConstant.URL);

        RequestParams reqParam = new RequestParams();

        reqParam.add("shop_id", shopid);
        //reqParam.add("user_id", userInfo.getId());
        reqParam.add("action", "shop_prod_listing");


        //Toast.showSmallToast(getActivity(), "shop_id="+shopid);
        //reqParam.add("lat", latitude.trim());
        //reqParam.add("long", longitude.trim());
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
                        Boolean noproduct = true;
                        if (l > 0) {
                            JSONObject obj = null;
                            String productId = "";
                            String title = "";
                            String price = "";
                            String link = "";
                            String distance = "";
                            String isprodinstore = "";
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

                                try {
                                    isprodinstore = obj.getString("is_prod_in_store");
                                } catch (Exception e) {
                                    isprodinstore = "";
                                }
                                if (isprodinstore.equals("Y")) {
                                    noproduct = false;
                                    ProductConciseList.productConciseListForUserShop
                                            .add(new Product_Concise_Shop(productId, title, price, link, distance, isprodinstore, shopid));
                                }

                            }
                            //if (ProductConciseList.productConciseListForUserShop.size() > 0) {
                            // setAdaptertoGridView(ProductConciseList.productConciseList);
                            //  adapter = new MyRecyclerProductsAdapterShopView(getActivity(), ProductConciseList.productConciseListForUserShop);
                            //  rv.setAdapter(adapter);
                            //} else {
                            if (noproduct) {
                                rltextnoproduct.setVisibility(View.VISIBLE);
                            } else {
                                rltextnoproduct.setVisibility(View.INVISIBLE);
                            }
                            progress1.setVisibility(View.INVISIBLE);
                            rlprogress1.setVisibility(View.INVISIBLE);
                            adapter = new MyRecyclerProductsAdapterShopView(getActivity(), ProductConciseList.productConciseListForUserShop);
                            rv.setAdapter(adapter);
                            //Toast.showSmallToast(getActivity(), "No Data found.");
                            //}


                        } else {

                            progress1.setVisibility(View.INVISIBLE);
                            rlprogress1.setVisibility(View.INVISIBLE);
                            rltextnoproduct.setVisibility(View.VISIBLE);
                            //Toast.showSmallToast(getActivity(), "No Data found.");


                        }
                    } else {
                        progress1.setVisibility(View.INVISIBLE);
                        rlprogress1.setVisibility(View.INVISIBLE);
                        rltextnoproduct.setVisibility(View.VISIBLE);
                        //Toast.showSmallToast(getActivity(), "Error!!!");
                        //mDilatingDotsProgressBar.hideNow();
                        //progress1.setVisibility(View.INVISIBLE);
                        //linearLayout.setVisibility(View.VISIBLE);

                        //progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e("errr", e.getMessage());
                } finally {
                }
            }

            ;

            @Override
            public void onFailure(int statusCode, org.apache.http.Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                //progressDialog.dismiss();
                //mDilatingDotsProgressBar.hideNow();
                //progress1.setVisibility(View.INVISIBLE);
                //linearLayout.setVisibility(View.VISIBLE);
                //Toast.showNetworkErrorMsgToast(context);
            }

            ;
        });
    }

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }


}
