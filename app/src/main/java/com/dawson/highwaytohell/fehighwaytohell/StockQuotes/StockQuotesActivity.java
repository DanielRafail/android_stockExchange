package com.dawson.highwaytohell.fehighwaytohell.StockQuotes;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dawson.highwaytohell.fehighwaytohell.R;

/**
 * StockQuotesActivity is pretty self explanatory.
 * It's the base class for Stock Quotes.
 * It simply gets the ticker input from the user.
 */
public class StockQuotesActivity extends Activity {
    private static final String TAG = "StockQuotesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_quotes);
    }


    public void loadTickers(View view) {
        Log.i(TAG, "loadTickers: Parsing the ticker string");
        EditText inputText = findViewById(R.id.tickers);
        String text = inputText.getText().toString();
        if (!text.isEmpty()) {
            String[] tickers = text.split(",");
            //Might be a good idea to check tickers are valid somehow. Regex or an api?
            if (tickers.length > 5) {
                Log.d(TAG, "loadTickers: Wrong input" + text);
                Toast.makeText(this, "Maximum of 5 tickers", Toast.LENGTH_SHORT).show();
            } else {
                loadView(tickers);
            }
        }
    }

    private void loadView(String[] tickers) {
        Log.i(TAG, "loadView: Creating RecylerView");
        RecyclerView recyclerView = findViewById(R.id.ticker_recycler_view);
        StockQuotesViewAdapter adapter = new StockQuotesViewAdapter(tickers, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


}
