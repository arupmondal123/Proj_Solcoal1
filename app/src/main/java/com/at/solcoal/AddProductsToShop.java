package com.at.solcoal;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AddProductsToShop extends AppCompatActivity {
    private Toolbar mToolbar;
    private ProuductAddToShopFragment prouductAddToShopFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products_to_shop);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Add or Delete Products");

        if (savedInstanceState == null) {
            prouductAddToShopFragment =  new ProuductAddToShopFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, prouductAddToShopFragment)
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //getSupportFragmentManager().beginTransaction().detach(prouductAddToShopFragment).attach(prouductAddToShopFragment).commit();
        //adapter.notifyDataSetChanged();

        //fetchShopList(userInfo.getId());
    }



}
