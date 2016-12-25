package com.at.drgrep.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.at.drgrep.R;
import com.at.drgrep.activity.ProductDetailsActivity;
import com.at.drgrep.constants.AppConstant;
import com.at.drgrep.data.FeedProductListRowHolder;
import com.at.drgrep.model.Product_Concise_Shop;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Arup on 7/31/2016.
 */
public class MyRecyclerProductsAdapterShopView extends RecyclerView.Adapter<FeedProductListRowHolder>{


    private List<Product_Concise_Shop> feedProductItemList;
    private Context mContext;
    SwitchCompat switchCompat;
    private String	_product_id;
    public MyRecyclerProductsAdapterShopView(Context context, List<Product_Concise_Shop> feedProductItemList) {
        this.feedProductItemList = feedProductItemList;
        this.mContext = context;
    }

    @Override
    public FeedProductListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout_nocard, null);
        FeedProductListRowHolder mh = new FeedProductListRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(final FeedProductListRowHolder feedProductListRowHolder, int i) {
        final Product_Concise_Shop product_Concise_Shop = feedProductItemList.get(i);

        //Log.e("MyRecyclerProductAdapter", "count" + i);
        /*
        Picasso.with(mContext).load(product_Concise_Shop.getProd_img_link())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.ic_store_black_48dp)
                .into(feedProductListRowHolder.image);
       */

        Glide.with(mContext).load(product_Concise_Shop.getProd_img_link()).into(feedProductListRowHolder.image);

        feedProductListRowHolder.name.setText(product_Concise_Shop.getTitle());
        feedProductListRowHolder.price.setText(product_Concise_Shop.getPrice());
        //feedProductListRowHolder.switchCompat.setChecked(false);
        //Log.e("MyRecyclerAdapter", feedProductItem.getTitle());

       /*
        feedProductListRowHolder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    switchCompat.setChecked(true);
                    Log.e("MyRecyclerAdapter", product_Concise.getProduct_id());

                } else {
                    switchCompat.setChecked(false);
                    Log.e("MyRecyclerAdapter", product_Concise.getProduct_id());

                }
            }
        });
*/

        feedProductListRowHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra(AppConstant.DETAILS_PAGE_TYPE, AppConstant.SHOW_DETAILS_FROM_LANDING_PAGE);
                intent.putExtra(AppConstant.P_ID, product_Concise_Shop.getProduct_id());
                context.startActivity(intent);

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
}
