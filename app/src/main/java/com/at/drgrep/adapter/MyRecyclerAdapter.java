package com.at.drgrep.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.at.drgrep.R;
import com.at.drgrep.activity.ShopDetail;
import com.at.drgrep.activity.ShopSettingsActivity;
import com.at.drgrep.data.FeedItem;
import com.at.drgrep.data.FeedListRowHolder;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<FeedListRowHolder> {


    private List<FeedItem> feedItemList;
    private Context mContext;
    private Boolean ownprofile;

    public MyRecyclerAdapter(Context context, List<FeedItem> feedItemList, Boolean ownprofile) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        this.ownprofile =ownprofile;
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_item, null);
        FeedListRowHolder mh = new FeedListRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(final FeedListRowHolder feedListRowHolder, int i) {
        final FeedItem feedItem = feedItemList.get(i);
        Log.e("MyRecyclerAdapter", "count" + i);
        Picasso.with(mContext).load(feedItem.getThumbnail())
                .error(R.drawable.placeholder1)
                .placeholder(R.drawable.placeholder1)
                .into(feedListRowHolder.thumbnail);

        feedListRowHolder.title.setText(feedItem.getTitle());
        Log.e("MyRecyclerAdapter", feedItem.getTitle());



        feedListRowHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ShopDetail.class);

                intent.putExtra("shop_id_extra", feedItem.getShop_id());
                intent.putExtra("shop_name_extra", feedItem.getTitle());
                intent.putExtra("shop_desc_extra", feedItem.getShop_desc());
                intent.putExtra("shop_email_extra", feedItem.getShop_contact_email());
                intent.putExtra("shop_phone_extra", feedItem.getShop_phone_no());
                intent.putExtra("shop_weblink_extra", feedItem.getShop_weblink());
                intent.putExtra("shop_shopactive_ind", feedItem.getShop_active_ind());
                if (ownprofile){
                    intent.putExtra("shop_ownprofile","Y" );
                }
                else {
                    intent.putExtra("shop_ownprofile","N" );
                }
                context.startActivity(intent);
            }
        });

        feedListRowHolder.overflow.setOnClickListener(new View.OnClickListener() {
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
        return feedItemList.size();
    }
}
