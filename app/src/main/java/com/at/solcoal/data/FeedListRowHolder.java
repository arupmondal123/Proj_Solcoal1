package com.at.solcoal.data;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.at.solcoal.R;

public class FeedListRowHolder extends RecyclerView.ViewHolder  {
    public ImageView thumbnail;
    public ImageView overflow;
    public TextView title;


    public FeedListRowHolder(View view) {
        super(view);
        this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        this.overflow = (ImageView) view.findViewById(R.id.overflow);
        this.title = (TextView) view.findViewById(R.id.title);
    }


}