package com.at.drgrep;


import com.google.android.gms.iid.InstanceIDListenerService;

public class GcmInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        GCMRegistrationUtils gcmRegistrationUtils = new GCMRegistrationUtils(this);
        gcmRegistrationUtils.setUpGcmNotification();
    }
}