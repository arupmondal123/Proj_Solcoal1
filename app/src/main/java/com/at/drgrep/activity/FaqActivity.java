package com.at.drgrep.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.at.drgrep.R;

public class FaqActivity extends AppCompatActivity {
    private boolean ret = false;
    private WebView wv1;
    private Toolbar mToolbar;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

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
        getSupportActionBar().setTitle("FAQ");

        wv1=(WebView)findViewById(R.id.webview);
        pd = new ProgressDialog(FaqActivity.this);
        pd.setMessage("Loading...");
        pd.show();
        wv1.setWebViewClient(new MyWebViewClient());

        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.loadUrl("https://medium.com/@m.kousik/drgrep-faq-598b7cf6093c#.l9806cnnv");
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            if (!pd.isShowing()) {
                pd.show();
            }

            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            //System.out.println("on finish");
            if (pd.isShowing()) {
                pd.dismiss();
            }

        }
    }

}
