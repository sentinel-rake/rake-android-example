package com.skp.di.sentinel.rake_android_example.gcm.server;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skp.di.sentinel.rake_android_example.gcm.GcmConfig;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GcmServer {

    private static final String TAG = "RAKE GCM SERVER";

    public static void postMessageToGCM(final GcmContent content) {

        final String apiKey = GcmConfig.SERVER_API_KEY;

        if (null == apiKey || apiKey.isEmpty()) {
            Log.d(TAG, "apiKey is Empty in postMessageToGCM()");
            return;
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL(GcmConfig.GCM_SERVER_URL);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Authorization", "key=" + apiKey);

                    conn.setDoOutput(true);
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());


                    Log.d(TAG, "JSON value: \n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(content));
                    mapper.writeValue(wr, content);

                    // send push message
                    wr.flush();
                    wr.close();

                    int responseCode = conn.getResponseCode();
                    Log.d(TAG, "responseCode: " + responseCode);

                    if (200 != responseCode)
                        throw new IOException("postMessageToGCM(): responseCode is not 200");

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String input;
                    StringBuffer response = new StringBuffer();

                    while(null != (input = br.readLine())) {
                        response.append(input);
                    }

                    br.close();
                    Log.d(TAG, "response: " + response.toString());

                } catch (MalformedURLException e) {
                    Log.e(TAG, "MalformedURLException: " + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, "IOException: " + e.getMessage());
                }

                return null;
            }
        }.execute(null, null, null);

    }


}
