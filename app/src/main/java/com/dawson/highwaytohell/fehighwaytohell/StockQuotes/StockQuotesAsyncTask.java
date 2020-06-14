package com.dawson.highwaytohell.fehighwaytohell.StockQuotes;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dawson.highwaytohell.fehighwaytohell.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * It gets the stock information from the World Trading data api and
 * display them using an dialog.
 * It uses a WeakReference to StockQuotesActivity, since WeakReference will
 * prevent memory leaks in case user decides to close the activity.
 */
public class StockQuotesAsyncTask extends AsyncTask<String, String, String> {
    private static final String TAG = "StockQuotesAsyncTask";
    private WeakReference<StockQuotesActivity> activityWeakReference;

    public StockQuotesAsyncTask(StockQuotesActivity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.i(TAG, "doInBackground: Loading information for " + strings[0]);
        try {
            String connectionString = "https://www.worldtradingdata.com/api/v1/stock?symbol=" + strings[0] + "&api_token=wwqiutajs8Ta48tW5Qydx8FFlO55wApIQCf5txnEaE2eOzpIwUU9pHPeja8s";
            URL url = new URL(connectionString);

            //Open connection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Load data
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            //Read data
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }


            return stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Everything went to hell";
    }


    @Override
    protected void onPostExecute(String s) {
        Log.i(TAG, "onPostExecute: Started");
        super.onPostExecute(s);
        StockQuotesActivity activity = activityWeakReference.get();

        if (activity != null && !activity.isFinishing()) {
            Log.i(TAG, "onPostExecute: Decoding Json");
            /* It would be much cleaner to put them in data object but i am only using it once so it doesn't really make sense.*/
            String ticker = "Check your internet connection and Tickers";
            String name = "Check your internet connection and Tickers";
            String price = "Check your internet connection and Tickers";
            String type = "Check your internet connection and Tickers";
            String exchange = "Check your internet connection and Tickers";
            try {
                Log.i(TAG, "onPostExecute: Parsing Json");
                // I am not manually parsing Json
                JSONObject obj = new JSONObject(s);
                JSONArray arr = obj.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject stock = arr.getJSONObject(i);

                    ticker = stock.getString("symbol");
                    name = stock.getString("name");
                    price = stock.getString("price");
                    type = stock.getString("currency");
                    exchange = stock.getString("stock_exchange_short");
                }


                Log.i(TAG, "onPostExecute: Creating dialog");
                //I am sure there is a better way than dialog but this kind of inception stuff is amazing.
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.stock_quotes_pop_up);
                TextView x = (TextView) dialog.findViewById(R.id.x);
                TextView tickerSymbol = (TextView) dialog.findViewById(R.id.tickerSymbol);
                TextView tickerName = (TextView) dialog.findViewById(R.id.tickerName);
                TextView tickerPrice = (TextView) dialog.findViewById(R.id.tickerPrice);
                TextView tickerCurrency = (TextView) dialog.findViewById(R.id.tickerCurrency);
                TextView tickerExchange = (TextView) dialog.findViewById(R.id.tickerExchange);

                x.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                //Concatenation is bad according to Android studio but i am not writing another 6 lines.
                tickerSymbol.setText(tickerSymbol.getText() + ticker);
                tickerName.setText(tickerName.getText() + name);
                tickerPrice.setText(tickerPrice.getText() + price);
                tickerCurrency.setText(tickerCurrency.getText() + type);
                tickerExchange.setText(tickerExchange.getText() + exchange);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            } catch (JSONException e) {
                Log.d(TAG, "onPostExecute: Something went wrong");
                e.printStackTrace();
            }
        }
    }


}
