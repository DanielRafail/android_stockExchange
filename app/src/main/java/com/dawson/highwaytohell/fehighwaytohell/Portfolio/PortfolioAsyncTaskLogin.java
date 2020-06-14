package com.dawson.highwaytohell.fehighwaytohell.Portfolio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.dawson.highwaytohell.fehighwaytohell.MainActivity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.dawson.highwaytohell.fehighwaytohell.Constants.User;
import com.dawson.highwaytohell.fehighwaytohell.RealPortfolio.StockObjectAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import static android.content.Context.MODE_PRIVATE;

public class PortfolioAsyncTaskLogin extends AsyncTask<Void, Void, String>{

    private static final String TAG = "PortfolioAsyncTaskLogin";
    private Context mContext;
    private SharedPreferences sharedPrefs;

    public PortfolioAsyncTaskLogin(MainActivity activity) {
        mContext = activity;
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "Start onPreExecute progress");
        super.onPreExecute();
        sharedPrefs = mContext.getSharedPreferences("saves", MODE_PRIVATE);
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.d(TAG, "Start background progress");
        try {
            String connectionString = "http://stockphpwebsite.herokuapp.com/api/auth/login";
            URL url = new URL(connectionString);
            //Open connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            Log.d(TAG, "Connection created");
            //send the data to the post
            try {
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.writeBytes(URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(sharedPrefs.getString("email",""), "UTF-8") + "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(sharedPrefs.getString("password",""),"UTF-8"));
                wr.flush();
                wr.close();
                Log.d(TAG, "Connection started");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Load data
            StringBuilder result = new StringBuilder();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            //Read data
            String line;
            Log.d(TAG, "Reading from response");
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
            JSONObject json = new JSONObject(result.toString());
            return json.getString("access_token");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Everything went to hell";
    }


    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, "Start onPostExecute progress");
        super.onPostExecute(s);
            User.setLogged(true);
            User.setToken(s);
    }
}