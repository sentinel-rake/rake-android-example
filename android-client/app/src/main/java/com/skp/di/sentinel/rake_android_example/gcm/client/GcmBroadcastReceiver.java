package com.skp.di.sentinel.rake_android_example.gcm.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.rake.android.rkmetrics.RakeAPI;
import com.skp.di.sentinel.rake_android_example.gcm.client.GcmIntentService;
import com.skplanet.pdp.sentinel.shuttle.AppSampleSentinelShuttle;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = "GcmBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        // Explicitly specify that GcmMessageHandler will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
