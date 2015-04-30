package com.skp.di.sentinel.rake_android_example.gcm.client;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.skp.di.sentinel.rake_android_example.gcm.GcmConfig;

import java.io.IOException;

public class GcmClient {

    private static final String TAG = "RAKE GCM CLIENT";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    private String registrationID;
    private Context context;
    private Activity activity;
    private GoogleCloudMessaging gcm;

    public GcmClient(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
    }

    public String getRegistrationID() {
        return this.registrationID;
    }

    public void registerDevice() {
        // check device for Play Service APK
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(context);

            if (null == registrationID) {
                Log.d(TAG, "not registered. run registerInBackground()");
                registerInBackground();
            } else {
                Log.d(TAG, "already registered. regID: " + registrationID);
            }
        } else Log.d(TAG, "No Valid Google Play Service APK found.");
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d(TAG, "This device is not supported.");
            }
            return false;
        }

        return true;
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";

                try {
                    if (null == gcm) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }

                    registrationID = gcm.register(GcmConfig.SENDER_ID);

                    Log.d(TAG, "registered. regID: " + registrationID);
                    // sendRegistrationIdToBackend();
                    // persistRegistrationId();

                } catch(IOException e) {
                    msg = "Error: " + e.getMessage();
                    Log.d(TAG, "can't get regID due to: \n" + msg);
                }

                return msg;
            }
        }.execute(null, null, null);
    }

}
