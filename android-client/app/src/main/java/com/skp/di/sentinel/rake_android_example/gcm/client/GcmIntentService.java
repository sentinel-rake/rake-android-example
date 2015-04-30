package com.skp.di.sentinel.rake_android_example.gcm.client;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.rake.android.rkmetrics.RakeAPI;
import com.skp.di.sentinel.rake_android_example.MainActivity;
import com.skp.di.sentinel.rake_android_example.RakeConfig;
import com.skplanet.pdp.sentinel.shuttle.AppSampleSentinelShuttle;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static final String TAG = "GcmIntentService";

    private RakeAPI rake = null;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent()");

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.d(TAG, "Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.d(TAG, "Deleted messages on server: " + extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message.
                Log.d(TAG, "Received: " + extras.toString());

                AppSampleSentinelShuttle shuttle = new AppSampleSentinelShuttle();

                // shuttle provides a logging method per action.
                // let action name is 'action4'
                shuttle.setBodyOfaction4("field1 value", "field3 value", "field4 value");

                // track a log. log will be saved into SQLite (local storage)
                Log.d("shuttle string", shuttle.toJSONString());
                sendNotification("Received: " + extras.toString());

                rake = RakeAPI.getInstance(this, RakeConfig.TOKEN, RakeConfig.IS_DEV_MODE);
                rake.track(shuttle.toJSONObject());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(com.google.android.gms.gcm.R.drawable.ic_plusone_medium_off_client)
                        .setContentTitle("GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}


