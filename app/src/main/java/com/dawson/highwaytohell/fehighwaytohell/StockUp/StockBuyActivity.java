package com.dawson.highwaytohell.fehighwaytohell.StockUp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dawson.highwaytohell.fehighwaytohell.Constants.User;
import com.dawson.highwaytohell.fehighwaytohell.R;

public class StockBuyActivity extends Activity {

   private static final String TAG = "StockBuyActivity";
   private EditText ticker;
   private EditText numberOfStock;
   private String token = User.getToken();
   private TextView error;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_buy);
        ticker=findViewById(R.id.ticker);
        numberOfStock= findViewById(R.id.amount);
        error=findViewById(R.id.error);
    }


    public void buyStock(View view) {
        if (!ticker.getText().toString().isEmpty() && !numberOfStock.getText().toString().isEmpty()){

            String asyTicker=ticker.getText().toString();
            int asyStock=Integer.parseInt(numberOfStock.getText().toString());

            if(asyStock>0){
                showError("Buying stocks ... ");
                StockBuyAsyncTask task= new StockBuyAsyncTask(this);
                task.execute(asyTicker,numberOfStock.getText().toString(),token);
            }
            else{
                showError("Invalid # of stock");
            }

        }
        else {
            showError("Invalid data");
        }
    }

    public void showError(String message){
        error.setText(message);
    }




}
