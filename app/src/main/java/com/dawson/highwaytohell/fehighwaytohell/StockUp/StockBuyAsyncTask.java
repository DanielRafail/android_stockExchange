package com.dawson.highwaytohell.fehighwaytohell.StockUp;

import android.os.AsyncTask;
import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StockBuyAsyncTask extends AsyncTask<String, String, String> {
    private static final String TAG = "StockBuyAsyncTask";
    private WeakReference<StockBuyActivity> activityWeakReference;

    public StockBuyAsyncTask(StockBuyActivity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
        String connectionString = "http://stockphpwebsite.herokuapp.com/api/api/buy";
        String token= "Bearer "+strings[2];
        URL url = new URL(connectionString);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("ticker", strings[0]);
        params.put("amount", strings[1]);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Authorization", token);
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);


        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;){
            sb.append((char)c);
        }

        String response = sb.toString();
        return response;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Something went wrong";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        StockBuyActivity activity = activityWeakReference.get();

        if (activity != null && !activity.isFinishing()){
            try {
                JSONObject obj = new JSONObject(s);
                String cash=obj.getString("cashleft");
                activity.showError("Transaction successful!\nYou have "+cash+"$ left in your wallet");

            } catch (JSONException e) {
                Log.d(TAG, "onPostExecute: Something went wrong");
                activity.showError("Invalid Ticker or not enough cash to complete this transaction");
                e.printStackTrace();
            }

        }
    }
}
