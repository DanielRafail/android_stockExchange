package com.dawson.highwaytohell.fehighwaytohell.RealPortfolio;

import android.os.AsyncTask;
import android.util.Log;

import com.dawson.highwaytohell.fehighwaytohell.Constants.User;
import com.dawson.highwaytohell.fehighwaytohell.Portfolio.Portfolio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StockObjectAsyncTask extends AsyncTask<String,String,String> {

    private WeakReference<Portfolio> activityWeakReference;
    private List<StockObject> stockData;

    public StockObjectAsyncTask(Portfolio activity)
    {
        this.activityWeakReference = new WeakReference<>(activity);
        stockData = new ArrayList<>();
    }


    @Override
    protected String doInBackground(String... strings) {
        String token= User.getToken();
        String baseURL = "http://stockphpwebsite.herokuapp.com/api/api/allstocks?token=" + token ;
        StringBuilder resultMessage = new StringBuilder();

        try {
            URL url = new URL(baseURL);
            Log.d("URL", baseURL);

            // Attempt to make a connection to the API
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            // Check if a connection is successful
            if (responseCode == HttpURLConnection.HTTP_OK) {

                Log.d("MESSAGE", connection.getResponseMessage());
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                // Construct the String in JSON format
                String line;
                while ((line = reader.readLine()) != null) {
                    resultMessage.append(line);
                }

                // Close resources
                reader.close();
                connection.disconnect();
            }

            // Retrieve the result of the conversion
            Log.d("JSON to string", resultMessage.toString());
            JSONArray stocksArray = (JSONArray) new JSONTokener(resultMessage.toString()).nextValue();
            for (int i = 0; i < stocksArray.length(); i++) {
                try {
                    StockObject stockObject = new StockObject();
                    JSONObject stock = stocksArray.getJSONObject(i);
                    // Pulling items from the array
                    stockObject.setCompany(stock.getString("company"));
                    stockObject.setId(stock.getInt("id"));
                    stockObject.setAmount(stock.getInt("amount"));
                    stockObject.setChange(stock.getInt("change"));
                    stockObject.setCurrent_price(stock.getInt("current_price"));
                    stockObject.setCloseYesterday(stock.getInt("close_yesterday"));
                    stockObject.setEmail(stock.getString("email"));
                    stockObject.setTicker(stock.getString("ticker"));
//                    Long timestamp = stock.getLong("purchase_date");
                    Log.i("purchase", ""+stockObject.getPurchaseDate());
//                    Timestamp ts = new Timestamp(timestamp);
//                    stockObject.setPurchaseDate(ts);
//                    Log.i("date", ""+stockObject.getPurchaseDate());
                    stockData.add(stockObject);

                } catch (JSONException e) {
                    // Oops
                }
            }

        } catch (IOException | JSONException e) {
            Log.wtf("ERROR", e);
        }
        return "Finish";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Portfolio activity = activityWeakReference.get();
        if (activity != null && !activity.isFinishing()){
            activity.createRecyclerView(stockData);
        }
    }
}
