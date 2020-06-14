package com.dawson.highwaytohell.fehighwaytohell.LoanCalculator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.dawson.highwaytohell.fehighwaytohell.About;
import com.dawson.highwaytohell.fehighwaytohell.R;
import com.dawson.highwaytohell.fehighwaytohell.Settings;


public class CalculatorActivity extends AppCompatActivity {

    EditText balance;
    EditText rate;
    EditText minMonth;
    EditText monthly;
    EditText minMonthB;
    EditText additional;
    EditText fixed;

    double balanceValue;
    double rateValue;
    double minMonthValue;
    double monthlyValue;
    double minMonthBValue;
    double additionalValue;
    double fixedValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_inputs);
        setEditTexts();
    }

    private void setDoubles(){
        balanceValue = Double.parseDouble(balance.getText().toString());
        rateValue = Double.parseDouble(rate.getText().toString());
        minMonthValue = Double.parseDouble(minMonth.getText().toString());
        monthlyValue = Double.parseDouble(monthly.getText().toString());
        minMonthBValue = Double.parseDouble(minMonthB.getText().toString());
        additionalValue = Double.parseDouble(additional.getText().toString());
        fixedValue = Double.parseDouble(fixed.getText().toString());
    }

    private void setEditTexts(){
        balance = (EditText) findViewById(R.id.balance);
        rate = (EditText)findViewById(R.id.rate);
        minMonth = (EditText)findViewById(R.id.minMonth);
        monthly = (EditText)findViewById(R.id.monthPercentage);
        minMonthB = (EditText)findViewById(R.id.minMonthOptionB);
        additional = (EditText)findViewById(R.id.additional);
        fixed = (EditText)findViewById(R.id.fixedAmount);
    }

    /**
     * Override of onInstanceState to save the data in case of minor interruptions
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("balance", balance.getText().toString());
        savedInstanceState.putString("rate", rate.getText().toString());
        savedInstanceState.putString("minMonth", minMonth.getText().toString());
        savedInstanceState.putString("minMonthB", minMonthB.getText().toString());
        savedInstanceState.putString("monthly", monthly.getText().toString());
        savedInstanceState.putString("additional", additional.getText().toString());
        savedInstanceState.putString("fixed", fixed.getText().toString());
    }

    /**
     * Override of onRestoreInstanceState which allows to read the instance State if an interruption
     * happened so that the user can have a smooth experience with the app
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        balance.setText(savedInstanceState.getString("balance"));
        rate.setText(savedInstanceState.getString("rate"));
        minMonth.setText(savedInstanceState.getString("minMonth"));
        minMonthB.setText(savedInstanceState.getString("minMonthB"));
        monthly.setText(savedInstanceState.getString("monthly"));
        additional.setText(savedInstanceState.getString("additional"));
        fixed.setText(savedInstanceState.getString("fixed"));

    }

    public void onSubmitClick(View view) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow( getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        setDoubles();
        if(balanceValue > 0 && rateValue > 0 && rateValue < 100 && minMonthValue > 0 && monthlyValue > 0 && monthlyValue < 100 && minMonthBValue > 0 && fixedValue > 0) {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("balance", balanceValue);
            intent.putExtra("rate", rateValue);
            intent.putExtra("minMonth", minMonthValue);
            intent.putExtra("monthly", monthlyValue);
            intent.putExtra("minMonthB", minMonthBValue);
            intent.putExtra("additional", additionalValue);
            intent.putExtra("fixed", fixedValue);
            startActivity(intent);
        }else{
            Toast toast = Toast.makeText(this, getResources().getString(R.string.invalid), Toast.LENGTH_SHORT);
            toast.show();
        }
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
        menu.findItem(R.id.settings
        ).setIntent(new Intent(this, Settings.class));
        return true;
    }
}
