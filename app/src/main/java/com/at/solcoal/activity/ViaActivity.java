package com.at.solcoal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.at.solcoal.R;
import com.sinch.android.rtc.SinchError;

public class ViaActivity extends BaseActivity  implements SinchService.StartFailedListener {

    private Button viaBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_via);
        viaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viaButtonClicked();
            }
        });
    }

    @Override
    protected void onServiceConnected() {
       // mLoginButton.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);
    }

    private void viaButtonClicked() {
        Bundle bundle = getIntent().getExtras();
        String userEmail = bundle.getString("useremail");
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userEmail);
            //showSpinner();
        } else {
            openMessagingActivity();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        viaBtn = (Button) findViewById(R.id.via_btn);
        viaBtn.performClick();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStarted() {
        openMessagingActivity();
    }

    @Override
    public void onStart(){
        super.onStart();


    }
    private void openMessagingActivity() {
        Intent messagingActivity = new Intent(this, MessagingActivity.class);
        startActivity(messagingActivity);
    }

}
