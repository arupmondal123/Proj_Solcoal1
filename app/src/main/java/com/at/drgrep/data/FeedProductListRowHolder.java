package com.at.drgrep.data;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.at.drgrep.R;

public class FeedProductListRowHolder extends RecyclerView.ViewHolder  {
    public ImageView image;
    public TextView name;
    public TextView price;
    public CheckBox checkBox;


    public FeedProductListRowHolder(View view) {
        super(view);
        this.image = (ImageView) view.findViewById(R.id.image);
        this.name = (TextView) view.findViewById(R.id.p_name);
        this.price = (TextView) view.findViewById(R.id.p_price);
        this.checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
    }


}