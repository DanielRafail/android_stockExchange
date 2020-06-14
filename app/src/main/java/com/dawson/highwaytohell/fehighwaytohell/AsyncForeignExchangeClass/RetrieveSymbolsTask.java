package com.dawson.highwaytohell.fehighwaytohell.AsyncForeignExchangeClass;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class that handles an asynchronous task querying an API for ticker symbols
 *
 */
public class RetrieveSymbolsTask extends AsyncTask<Void, Void, ArrayList<String>> {
    private String baseURL = "http://data.fixer.io/api/";
    private String accessKey = "a186abaea10d174893931a2df2947c24";
    private ArrayList<String> listOfSymbols;

    public RetrieveSymbolsTask() {
        listOfSymbols = new ArrayList<>();
    }

    /**
     * Overridden doInBackground method that queries the Fixer API for all ticker symbols available
     *
     * @param voids
     * @return List representing all ticker symbols available
     */
    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(baseURL + "symbols?access_key=" + accessKey);

            // Attempt to make a connection to the API
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();

            // Check if connection is successful
            if (responseCode == HttpURLConnection.HTTP_OK) {

                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                // Construct the String in JSON format
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                // Close resources
                reader.close();
                connection.disconnect();

                Log.d("JSON to string", result.toString());
                JSONObject object = (JSONObject) new JSONTokener(result.toString()).nextValue();
                JSONObject jsonSymbols = object.getJSONObject("symbols");

                // Retrieve the 3 character symbols and add them to a list
                Iterator<String> symbolIterator = jsonSymbols.keys();
                while (symbolIterator.hasNext()) {
                    String key = symbolIterator.next();
                    Log.d("Adding symbol", key);
                    listOfSymbols.add(key + " - " + jsonSymbols.get(key));
                }
            }

        } catch (IOException | JSONException e) {
            Log.wtf(null, e);
        }
        return listOfSymbols;
    }
}
