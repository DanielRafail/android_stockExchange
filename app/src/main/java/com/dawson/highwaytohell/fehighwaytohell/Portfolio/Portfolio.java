package com.dawson.highwaytohell.fehighwaytohell.Portfolio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.dawson.highwaytohell.fehighwaytohell.R;
import com.dawson.highwaytohell.fehighwaytohell.RealPortfolio.StockItemRecyclerView;
import com.dawson.highwaytohell.fehighwaytohell.RealPortfolio.StockObject;

import java.util.ArrayList;
import java.util.List;

import android.view.View;

import com.dawson.highwaytohell.fehighwaytohell.RealPortfolio.StockObjectAsyncTask;
import com.dawson.highwaytohell.fehighwaytohell.StockDown.StockSellActivity;
import com.dawson.highwaytohell.fehighwaytohell.StockUp.StockBuyActivity;


public class Portfolio extends Activity {

    /**
     * override of onCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stockmain);
        getAllStocks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllStocks();
    }

    public void buy(View view) {
        Intent intent = new Intent(this, StockBuyActivity.class);
        startActivity(intent);
    }

    public void sell(View view) {
        Intent intent = new Intent(this, StockSellActivity.class);
        startActivity(intent);
    }

    public void getAllStocks(){
        StockObjectAsyncTask task= new StockObjectAsyncTask(this);
        task.execute();
    }

    public void createRecyclerView(List<StockObject> stocks){
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recyclerView);
        StockItemRecyclerView adapter = new StockItemRecyclerView(this, stocks);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
    }
}
