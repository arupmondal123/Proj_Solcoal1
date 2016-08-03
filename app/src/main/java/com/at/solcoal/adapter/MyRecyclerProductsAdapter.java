package com.at.solcoal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.at.solcoal.R;
import com.at.solcoal.activity.ShopDetail;
import com.at.solcoal.activity.ShopSettingsActivity;
import com.at.solcoal.constants.AppConstant;
import com.at.solcoal.data.FeedItem;
import com.at.solcoal.data.FeedListRowHolder;
import com.at.solcoal.data.FeedProductListRowHolder;
import com.at.solcoal.model.Product_Concise_Shop;
import com.at.solcoal.utility.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.at.solcoal.data.ProductConciseList;
import com.at.solcoal.model.Product_Concise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Arup on 7/31/2016.
 */
public class MyRecyclerProductsAdapter extends RecyclerView.Adapter<FeedProductListRowHolder>{


    CheckBox product_in_shop;
    private List<Product_Concise_Shop> feedProductItemList;
    private Context mContext;
    private View v;
    public MyRecyclerProductsAdapter(Context context, List<Product_Concise_Shop> feedProductItemList) {
        this.feedProductItemList = feedProductItemList;
        this.mContext = context;
    }

    @Override
    public FeedProductListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_product_to_shop_item, null);
        FeedProductListRowHolder mh = new FeedProductListRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(final FeedProductListRowHolder feedProductListRowHolder, int i) {
        final Product_Concise_Shop product_Concise_Shop = feedProductItemList.get(i);
        Boolean inShop;
        Boolean inShop1;
        final String shopid = product_Concise_Shop.getShop_id();
        final String productid = product_Concise_Shop.getProduct_id();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = 400; //height recycleviewer
        feedProductListRowHolder.itemView.setLayoutParams(params);

        //Log.e("MyRecyclerProductAdapter", "count" + i);
        Picasso.with(mContext).load(product_Concise_Shop.getProd_img_link())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.ic_store_black_48dp)
                .into(feedProductListRowHolder.image);

        feedProductListRowHolder.name.setText(product_Concise_Shop.getTitle());
        feedProductListRowHolder.price.setText(product_Concise_Shop.getPrice());

        if (product_Concise_Shop.getIsprodinstore() == "Y"){
            inShop = true;
        }
        else {
            inShop = false;
        }
        feedProductListRowHolder.checkBox.setChecked(inShop);

        //Log.e("MyRecyclerAdapter", feedProductItem.getTitle());


        feedProductListRowHolder.checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    setProductinShopStatus(((CheckBox) v).isChecked(),shopid,productid);
            }

        });

        feedProductListRowHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                Context context = v.getContext();
                Intent intent = new Intent(context, ShopDetail.class);

                intent.putExtra("shop_id_extra", feedItem.getShop_id());
                intent.putExtra("shop_name_extra", feedItem.getTitle());
                intent.putExtra("shop_desc_extra", feedItem.getShop_desc());
                intent.putExtra("shop_email_extra", feedItem.getShop_contact_email());
                intent.putExtra("shop_phone_extra", feedItem.getShop_phone_no());
                intent.putExtra("shop_weblink_extra", feedItem.getShop_weblink());
                intent.putExtra("shop_shopactive_ind", feedItem.getShop_active_ind());

                context.startActivity(intent);
            */
            }
        });

        /*
        feedProductListRowHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ShopSettingsActivity.class);
                intent.putExtra("shop_id_extra", feedItem.getShop_id());
                intent.putExtra("shop_name_extra", feedItem.getTitle());
                intent.putExtra("shop_desc_extra", feedItem.getShop_desc());
                intent.putExtra("shop_email_extra", feedItem.getShop_contact_email());
                intent.putExtra("shop_phone_extra", feedItem.getShop_phone_no());

                context.startActivity(intent);

            }
        });
        */
/*
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedListRowHolder holder = (FeedListRowHolder) view.getTag();
                int position = holder.getPosition();

                FeedItem feedItem = feedItemList.get(position);
                Intent intent = new Intent(view.getContext(), ShopDetail.class);
                intent.putExtra("shop_name_extra", feedItem.getTitle());

                // TODO
                view.getContext().startActivity(intent);

            }
        };*/
    }

    @Override
    public int getItemCount() {
        return feedProductItemList.size();
    }


    public void setProductinShopStatus(Boolean b,String shopid,String productid) {

        com.at.solcoal.web.AsyncWebClient asyncWebClient = new com.at.solcoal.web.AsyncWebClient();
        asyncWebClient.SetUrl(AppConstant.URL);

        RequestParams reqParam = new RequestParams();

        reqParam.add("prod_id", productid);
        reqParam.add("shop_id", shopid);
        if (b){
            reqParam.add("is_prod_in_store", "Y");
        }
        else {
            reqParam.add("is_prod_in_store", "");
        }
        reqParam.add("action", "shop_prod_entries_add_update");

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
                        Log.e("MyRecyclerProduct", "success");
                    }
                    else {
                        Log.e("MyRecyclerProduct", "failure");
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

            }

            ;
        });
    }


}

