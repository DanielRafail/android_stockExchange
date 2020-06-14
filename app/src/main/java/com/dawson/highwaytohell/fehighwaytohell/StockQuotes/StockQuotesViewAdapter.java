package com.dawson.highwaytohell.fehighwaytohell.StockQuotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dawson.highwaytohell.fehighwaytohell.R;

/**
 * This adapter displays all the tickers from the user.
 * When the user clicks on any ticker, it start an AsyncTask to load the stock information.
 */
public class StockQuotesViewAdapter extends RecyclerView.Adapter<StockQuotesViewAdapter.ViewHolder> {
    private static final String TAG = "StockQuotesViewAdapter";
    private String[] tickers;
    private Context mContext;

    public StockQuotesViewAdapter(String[] tickers, Context mContext) {
        Log.i(TAG, "StockQuotesViewAdapter: Created");
        this.tickers = tickers;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.i(TAG, "onCreateViewHolder: Inflating view");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_info_recycler_pod, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tickerSymbol.setText(tickers[i]);
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onBindViewHolder onClick: Loading info for " + tickers[i]);
                StockQuotesAsyncTask task = new StockQuotesAsyncTask((StockQuotesActivity) mContext);
                task.execute(tickers[i]);
            }
        });


    }

    @Override
    public int getItemCount() {
        return tickers.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout parentLayout;
        TextView tickerSymbol;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tickerSymbol = itemView.findViewById(R.id.tickerSymbol);
            parentLayout = itemView.findViewById(R.id.StockPodParentLayout);
        }
    }


}
