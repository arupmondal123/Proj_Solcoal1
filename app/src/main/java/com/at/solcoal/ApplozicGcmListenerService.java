package com.at.solcoal;

/**
 * Created by Arup on 6/4/2016.
 */
import android.os.Bundle;
import android.util.Log;

import com.applozic.mobicomkit.api.notification.MobiComPushReceiver;
import com.google.android.gms.gcm.GcmListenerService;

public class ApplozicGcmListenerService extends GcmListenerService {

    private static final String TAG = "ApplozicGcmListener";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        if (MobiComPushReceiver.isMobiComPushNotification(data)) {
            Log.i(TAG, "Applozic notification processing...");
            MobiComPushReceiver.processMessageAsync(this, data);
            return;
        }
    }

}
