package com.dawson.highwaytohell.fehighwaytohell.ForeignExchange;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dawson.highwaytohell.fehighwaytohell.AsyncForeignExchangeClass.ConversionTask;
import com.dawson.highwaytohell.fehighwaytohell.R;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

public class ForeignExchangeActivity extends Activity {

    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    String defaultCurrency;
    private List<String> symbols = new ArrayList<>();
    private int mSelectedIndex = 0;

    /**
     * Overridden onCreate method
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forex);

        // Retrieve data from intent
        Intent intent = getIntent();
        symbols = intent.getStringArrayListExtra("symbols");

        // Retrieve default currency from Shared Preferences, set in Settings Activity
        SharedPreferences prefs = getSharedPreferences("saves", MODE_PRIVATE);
        defaultCurrency = prefs.getString("currency","");
        addItemsOnSpinners();

    }

    /**
     * Populate spinner widgets with data
     *
     */
    private void addItemsOnSpinners() {
        spinnerFrom = (Spinner) findViewById(R.id.spinnerFrom);
        spinnerTo = (Spinner) findViewById(R.id.spinnerTo);

        // Set adapters to both spinners
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, symbols){
            public View getView(int position, View convertView, ViewGroup parent) {
                // Cast the spinner collapsed item (non-popup item) as a text view
                TextView tv = (TextView) super.getView(position, convertView, parent);

                // Set the text color of spinner item
                tv.setTextColor(Color.WHITE);

                // Return the view
                return tv;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                // Cast the drop down items (popup items) as text view
                TextView tv = (TextView) super.getDropDownView(position,convertView,parent);

                // Set the text color of drop down items
                tv.setTextColor(Color.WHITE);

                // If this item is selected item
                if(position == mSelectedIndex){
                    // Set spinner selected popup item's text color
                    tv.setTextColor(Color.WHITE);
                }

                // Return the modified view
                return tv;
            }
        };

        // Set an item selection listener for spinner widget
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Set the value for selected index variable
                mSelectedIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // Set an item selection listener for spinner widget
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Set the value for selected index variable
                mSelectedIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);
        setDefaultCurrency();
    }


    /**
     * Asynchronously converts currencies
     *
     * @param view
     */
    public void convertCurrencies(View view) {
        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();

        EditText etAmountToConvert = (EditText) findViewById(R.id.amount_to_convert);
        TextView tvConvertedAmount = (TextView) findViewById(R.id.converted_amount);
        LineChart chart = (LineChart) findViewById(R.id.chart);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_loader);

        // Check if user inputted anything
        String amountToConvert = etAmountToConvert.getText().toString();
        if (amountToConvert.equals("")) {
            Toast.makeText(this, "Please enter an amount to convert", Toast.LENGTH_SHORT).show();
        } else {

            // CLoses the virtual keyboard
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            progressBar.setVisibility(View.VISIBLE);
            // Check if user is connected to a network
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.d("Converting", etAmountToConvert.getText().toString());

                String to = spinnerTo.getSelectedItem().toString();
                String from = spinnerFrom.getSelectedItem().toString();

                // Remove the full name from the list of symbols as required by the currencyconverterapi API
                to = to.substring(0, to.indexOf(' '));
                from = from.substring(0, from.indexOf(' '));
                ConversionTask convertTask = new ConversionTask(tvConvertedAmount, progressBar, chart, this);
                convertTask.execute(from, to, amountToConvert);
            }
        }
    }

    /**
     * Set the default item on the spinner representing the base currency
     *
     */
    private void setDefaultCurrency() {
        for (int i = 0; i < spinnerFrom.getCount(); i++) {
            String item = spinnerFrom.getItemAtPosition(i).toString();
            if (item.contains(defaultCurrency)) {
                spinnerFrom.setSelection(i);
            }
        }
    }
}


