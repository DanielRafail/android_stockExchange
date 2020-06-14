/**
 * class to use and async task class to sell some stock, given on the stock you are clicking on from the recycler view
 *
 * @author george Ilias
 * @author Kajal Bordhon
 */
package com.dawson.highwaytohell.fehighwaytohell.StockDown;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.dawson.highwaytohell.fehighwaytohell.Constants.User;
import com.dawson.highwaytohell.fehighwaytohell.R;

public class StockSellActivity extends Activity {

    private TextView number;
    private TextView ticker;
    private String amountOwned;


    @Override
    public void onCreate(Bundle out) {
        super.onCreate(out);
        setContentView(R.layout.selldownstocks);
        ticker = findViewById(R.id.totalSellUnit);
        number = findViewById(R.id.stockNumber);
        intent();
    }

    /**
     * method to get the items from the intent, will be given by the recycler view when clicking
     * on the item in question
     *
     * @author George ilias
     */
    public void intent() {
        Intent it = getIntent();
        ticker.setText(it.getStringExtra("ticker"));
        amountOwned = it.getStringExtra("amount");
    }

    /**
     * Method to sell stocks
     *
     * @param view
     * @author George Ilias
     */
    public void sellStock(View view) {

        int numberToBuy= Integer.parseInt(number.getText().toString());
        if (numberToBuy > 0) {
            AsyncTaskSelling task = new AsyncTaskSelling(this);
            task.execute(ticker.getText().toString(), number.getText().toString(),User.getToken());
        } else {
            greatWall("Error, the number or stocks were invalid", false);
        }
    }

    /**
     * Porly named method, due in part to my exhaustion,however  the message will make a toast explaining if you have succesfully
     * sold your stocks or not
     *
     * @param message
     * @author George Ilias
     */
    public void greatWall(String message, boolean finish) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        if (finish == true) {
         //   finish();
        }
    }


}
