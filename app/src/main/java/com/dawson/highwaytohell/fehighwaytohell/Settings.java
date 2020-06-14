package com.dawson.highwaytohell.fehighwaytohell;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class responsible for the settings. Contains Textviews and Editviews so that the user can enter his credentials and save them
 * @author 1633028
 * @version 1.0.0
 */
public class Settings extends Activity {

    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private int mSelectedIndex = 0;

    /**
     * Override of onCreate to add hints if sharedPreferences exist
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        sharedPrefs = getSharedPreferences("saves", MODE_PRIVATE);
        editor = sharedPrefs.edit();
        if(sharedPrefs.contains("date")) {
            TextView date = findViewById(R.id.dateInput);
            date.setText(sharedPrefs.getString("date", ""));
            setText();
        }
        setSpinnerCurrency();
        setSpinnerStock();
        editor.apply();
    }

    /**
     * Override of onInstanceState to save the data in case of minor interruptions
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        EditText firstName = findViewById(R.id.firstNameInput);
        EditText lastName = findViewById(R.id.lastNameInput);
        EditText email = findViewById(R.id.emailInput);
        EditText password = findViewById(R.id.passwordInput);
        Spinner currency = findViewById(R.id.currencyInput);
        Spinner stock = findViewById(R.id.stockInput);
        TextView date = findViewById(R.id.dateInput);
        savedInstanceState.putString("firstName", firstName.getText().toString());
        savedInstanceState.putString("lastName", lastName.getText().toString());
        savedInstanceState.putString("email", email.getText().toString());
        savedInstanceState.putString("password", password.getText().toString());
        if(currency.getSelectedItem() != null){
            savedInstanceState.putString("currency", currency.getSelectedItem().toString());
        }
        if(stock.getSelectedItem() != null) {
            savedInstanceState.putString("stock", stock.getSelectedItem().toString());
        }
        savedInstanceState.putString("date", date.getText().toString());
    }

    /**
     * Override of onRestoreInstanceState which allows to read the instance State if an interruption
     * happened so that the user can have a smooth experience with the app
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        EditText firstName = findViewById(R.id.firstNameInput);
        EditText lastName = findViewById(R.id.lastNameInput);
        EditText email = findViewById(R.id.emailInput);
        EditText password = findViewById(R.id.passwordInput);
        Spinner currency = findViewById(R.id.currencyInput);
        Spinner stock = findViewById(R.id.stockInput);
        TextView date = findViewById(R.id.dateInput);

        firstName.setText(savedInstanceState.getString("firstName"));
        lastName.setText(savedInstanceState.getString("lastName"));
        email.setText(savedInstanceState.getString("email"));
        password.setText(savedInstanceState.getString("password"));
        currency.setSelection(getSpinnerLocation(currency, (savedInstanceState.getString("currency"))));
        stock.setSelection(getSpinnerLocation(stock, (savedInstanceState.getString("stock"))));
        date.setText(savedInstanceState.getString("date"));
    }

    /**
     * Verifies the the index of a string inside of a spinner
     * @param spin the spinner we look inside of
     * @param lookingFor the string we are looking for
     * @return the index of the string, 0 if not found
     */
    private int getSpinnerLocation(Spinner spin, String lookingFor){
        for (int i=0;i<spin.getCount();i++){
            if (spin.getItemAtPosition(i).toString().equalsIgnoreCase(lookingFor)){
                return i;
            }
        }
        return 0;
    }

    /**
     * Set the hints if the shared preferences exist
     */
    private void setText(){
        EditText firstName = findViewById(R.id.firstNameInput);
        EditText lastName = findViewById(R.id.lastNameInput);
        EditText email = findViewById(R.id.emailInput);
        Spinner currency = findViewById(R.id.currencyInput);
        Spinner stock = findViewById(R.id.stockInput);
        firstName.setText(sharedPrefs.getString("firstname",""));
        lastName.setText(sharedPrefs.getString("lastname",""));
        email.setText(sharedPrefs.getString("email",""));
        currency.setSelection(getSpinnerLocation(currency,sharedPrefs.getString("currency","")));
        stock.setSelection(getSpinnerLocation(stock,sharedPrefs.getString("stock","")));


    }

    /**
     * Instantiate the currency spinner variables
     */
    private void setSpinnerCurrency(){
        Spinner dropdown = findViewById(R.id.currencyInput);
        String[] items = new String[]{getResources().getString(R.string.CAD),getResources().getString(R.string.USD),getResources().getString(R.string.BTC)};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items){
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
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        dropdown.setAdapter(adapter);
    }

    /**
     * Instantiate the stock spinner variables
     */
    private void setSpinnerStock(){
        Spinner dropdown = findViewById(R.id.stockInput);
        String[] items = new String[]{getResources().getString(R.string.TSX),getResources().getString(R.string.NYSE),getResources().getString(R.string.NASDAQ)};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items){
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
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        dropdown.setAdapter(adapter);
    }

    /**
     * Set menubar utility
     * @param menu the menu bar
     * @return boolean if success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        String url = "https://www.dawsoncollege.qc.ca/computer-science-technology/";
        Intent dawsonWebsite = new Intent(Intent.ACTION_VIEW);
        dawsonWebsite.setData(Uri.parse(url));

        getMenuInflater().inflate(R.menu.mymenu, menu);
        menu.findItem(R.id.about
        ).setIntent(new Intent(this, About.class));
        menu.findItem(R.id.dawson
        ).setIntent(dawsonWebsite);
        return true;
    }

    /**
     * Get an the string value of a editview
     * @param id the id of the editview
     * @param type enum used to represent the type of information it contains so that the regex
     *             can be correctly set (EMAIL, TEXT, PASS, DATE)
     * @return the string received from the view
     */
    private String getElementEditText(int id, InputType type){
        if(type == InputType.DATE){
            throw new IllegalArgumentException("Element chosen is not an EditText");
        }
        EditText value = findViewById(id);
        String returnValue = value.getText().toString().trim();
        String regex = "";
        switch(type){
            case EMAIL:
                regex = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                    "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\" +
                    "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
                    "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                    "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:" +
                    "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\" +
                    "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$";
                break;
            case PASS:
                regex = "^(?=.*[a-z])(?=.*[0-9])[A-Za-z[0-9]@$!%*#?&]{6,}$";
                break;
            case TEXT:
                regex = "^[a-zA-Z]+$";
                break;
        }
        if(returnValue!= null && returnValue.matches(regex)){
            return returnValue;
        }else{
            throw new IllegalArgumentException();
        }
    }

    private String getElementTextView(int id, InputType type){
        if(type != InputType.DATE){
            throw new IllegalArgumentException("Element chosen is not a TextView");
        }
        TextView value = findViewById(id);
        String returnValue = value.getText().toString().trim();
        String regex = "^[0-9]{2}-[a-zA-Z]{3}-[0-9]{4}$";
        if(returnValue!= null && returnValue.matches(regex)){
            return returnValue;
        }else{
            throw new IllegalArgumentException();
        }
    }

    /**
     * Get the string value of the objected selected on a spinner. Else return the one at index 0
     * @param id the id of the spinner
     * @return
     */
    private String getElementSpinner(int id) {
        Spinner value = findViewById(id);
        if (value.getSelectedItem() != null) {
            return value.getSelectedItem().toString();
        }else{
            value.setSelection(0);
            return value.getSelectedItem().toString();
        }
    }

    /**
     * get the current date and put it to string
     * @return the date as a string
     */
    private String dateToString(){
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(calendar);

        return formattedDate;
    }

    /**
     * save the information to shared preferences
     * @param view the view of the button that got clicked
     */
    public void saveInformation(View view) {
        TextView text = findViewById(R.id.dateInput);
        text.setText(dateToString());
        try {
            editor.putString("firstname", getElementEditText(R.id.firstNameInput, InputType.TEXT));
            editor.putString("lastname", getElementEditText(R.id.lastNameInput, InputType.TEXT));
            editor.putString("email", getElementEditText(R.id.emailInput, InputType.EMAIL));
            editor.putString("password", getElementEditText(R.id.passwordInput, InputType.PASS));
            editor.putString("currency", getElementSpinner(R.id.currencyInput));
            editor.putString("stock", getElementSpinner(R.id.stockInput));
            editor.putString("date", getElementTextView(R.id.dateInput, InputType.DATE));
            editor.putString("username", getElementEditText(R.id.firstNameInput, InputType.TEXT) + " " + getElementEditText(R.id.lastNameInput, InputType.TEXT));
            editor.commit();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }catch(IllegalArgumentException ex){
            TextView error = findViewById(R.id.errorSettings);
            error.setText(getResources().getString(R.string.error));
            Log.e(this.getClass().toString(), "Invalid settings parameters : " + ex);
        }
    }
}
