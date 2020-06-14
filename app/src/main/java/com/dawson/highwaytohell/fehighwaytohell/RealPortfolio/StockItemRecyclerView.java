package com.dawson.highwaytohell.fehighwaytohell.RealPortfolio;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dawson.highwaytohell.fehighwaytohell.R;
import com.dawson.highwaytohell.fehighwaytohell.StockDown.StockSellActivity;
import com.dawson.highwaytohell.fehighwaytohell.StockUp.StockBuyActivity;
import android.content.Intent;
import java.util.List;

public class StockItemRecyclerView extends RecyclerView.Adapter<StockItemRecyclerView.ViewHolder> {

    private List<StockObject> jArray;
    private static final String TAG = "StockItemRecyclerView";
    private Context mContext;

    public StockItemRecyclerView(Context act, List<StockObject> jArray)
    {
        this.jArray = jArray;
        mContext=act;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stocksrecyleritems, viewGroup, false);
        return new StockItemRecyclerView.ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.company.setText(jArray.get(i).getCompany());
        viewHolder.company.setTextColor(Color.parseColor("#FFFFFF"));
        viewHolder.ticker.setText(jArray.get(i).getTicker());
        viewHolder.ticker.setTextColor(Color.parseColor("#FFFFFF"));
        viewHolder.amount.setText(String.valueOf(jArray.get(i).getAmount()));
        viewHolder.amount.setTextColor(Color.parseColor("#FFFFFF"));
//        viewHolder.purchase.setText(jArray.get(i).getPurchaseDate().toString());
        //viewHolder.purchase.setTextColor(Color.parseColor("#FFFFFF"));
        viewHolder.closeYesterday.setText(String.valueOf(jArray.get(i).getCloseYesterday()));
        viewHolder.closeYesterday.setTextColor(Color.parseColor("#FFFFFF"));
        viewHolder.currentPrice.setText(String.valueOf(jArray.get(i).getCurrent_price()));
        viewHolder.currentPrice.setTextColor(Color.parseColor("#FFFFFF"));
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StockSellActivity.class);
                intent.putExtra("ticker",jArray.get(i).getTicker());
                intent.putExtra("amount",jArray.get(i).getAmount());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.jArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public StockObject obj;
        public RelativeLayout parentLayout;
        public TextView company;
        public TextView ticker;
        public TextView amount;
        public TextView purchase;
        public TextView closeYesterday;
        public TextView currentPrice;

        public ViewHolder(View v)
        {
            super(v);
            parentLayout = v.findViewById(R.id.recyclerItem);
            ticker = v.findViewById(R.id.ticker);
            company = v.findViewById(R.id.company);
            amount = v.findViewById(R.id.amount);
            purchase = v.findViewById(R.id.purchase);
            currentPrice = v.findViewById(R.id.currentPrice);
            closeYesterday =v.findViewById(R.id.closePrice);

        }

        public void setObj(StockObject obj)
        {
            this.obj = obj;
        }

        public StockObject getObj() {
            return this.obj;
        }
    }


}
