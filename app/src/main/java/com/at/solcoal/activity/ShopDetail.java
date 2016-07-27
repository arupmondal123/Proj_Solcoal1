package com.at.solcoal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.at.solcoal.R;
import com.bumptech.glide.Glide;

public class ShopDetail extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        Intent intent = getIntent();
        final String shopname = intent.getStringExtra("shop_name_extra");
        final String shopdesc = intent.getStringExtra("shop_desc_extra");
        final String shopemail = intent.getStringExtra("shop_email_extra");
        final String shopphone= intent.getStringExtra("shop_phone_extra");

        //final String shopdescription = intent.getStringExtra("shop_description");


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(shopname);

        TextView textview= (TextView)findViewById(R.id.shoptitle);
        TextView shopdescription= (TextView)findViewById(R.id.shopdescription);
        TextView phonenotext= (TextView)findViewById(R.id.phonetext);
        TextView emailaddrtext= (TextView)findViewById(R.id.emailtext);



        textview.setText(shopname);
        shopdescription.setText(shopdesc);
        phonenotext.setText(shopphone);
        emailaddrtext.setText(shopemail);
        loadBackdrop();
    }
    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(R.drawable.placeholder).centerCrop().into(imageView);
    }

}
