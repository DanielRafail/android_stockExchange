package com.dawson.highwaytohell.fehighwaytohell.AsyncForeignExchangeClass;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dawson.highwaytohell.fehighwaytohell.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * Class that handles an asynchronous task querying an API for currency exchange rate conversion
 *
 */
public class ConversionTask extends AsyncTask<String, Integer, BigDecimal> {
    private String baseURL = "https://free.currencyconverterapi.com/api/v6/convert?q=";
    private WeakReference<TextView> tvConvertedAmountRef;
    private WeakReference<ProgressBar> progressBarRef;
    private WeakReference<LineChart> lineChartRef;
    private WeakReference<Context> activityContextRef;
    private List<String> dates;
    private List<BigDecimal> data;

    /**
     * Constructor for the ConversionTask
     *
     * @param textview
     * @param progBar
     * @param chart
     * @param context
     */
    public ConversionTask(TextView textview, ProgressBar progBar, LineChart chart, Context context) {
        tvConvertedAmountRef = new WeakReference<>(textview);
        progressBarRef = new WeakReference<>(progBar);
        lineChartRef = new WeakReference<>(chart);
        activityContextRef = new WeakReference<>(context);
        dates = new ArrayList<>();
        data = new ArrayList<>();
    }

    /**
     * Overridden doInBackground method that queries the currencyconverterapi API for historical
     * data regarding currency exchange rates
     *
     * @param params representing the base currency, the exchanging currency, and the amount of the
     *               base currency to convert
     * @return BigDecimal representing the result of the conversion
     */
    @Override
    protected BigDecimal doInBackground(String...params) {
        StringBuilder resultMessage = new StringBuilder();
        String today = getTodaysDate();
        String lastWeek = getLastWeekDate();

        try {
            URL url = new URL(baseURL+ params[0] + "_"+ params[1]+ "&compact=ultra&date=" + lastWeek + "&endDate=" + today);
            Log.d("URL", baseURL+ params[0] + "_"+ params[1]+ "&compact=ultra&date=" + lastWeek + "&endDate=" + today);

            // Attempt to make a connection to the API
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            // Check if a connection is successful
            if (responseCode == HttpURLConnection.HTTP_OK) {

                Log.d("MESSAGE", connection.getResponseMessage());
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                // Construct the String in JSON format
                String line;
                while ((line = reader.readLine()) != null) {
                    resultMessage.append(line);
                }

                // Close resources
                reader.close();
                connection.disconnect();
            }

            // Retrieve the result of the conversion
            Log.d("JSON to string", resultMessage.toString());
            JSONObject object = (JSONObject) new JSONTokener(resultMessage.toString()).nextValue();
            JSONObject historicalData = object.getJSONObject(params[0] + "_"+ params[1]);

            // Iterate through the dates and store both key and values in 2 separate lists
            Iterator<String> dataIterator = historicalData.keys();
            while (dataIterator.hasNext()) {
                // Tracking dates
                String key = dataIterator.next();
                Log.d("Adding date", key);
                dates.add(key);
                // Tracking currency exchange rates
                Double rate = historicalData.getDouble(key);
                Log.d("Adding exchange rate", Double.toString(historicalData.getDouble(key)));
                data.add(new BigDecimal(rate).setScale(6, BigDecimal.ROUND_DOWN));
            }

        } catch (IOException | JSONException e) {
            Log.wtf("ERROR", e);
        }
        // The last entry of the currency exchange rate list is the latest rate
        return data.get(data.size()-1).multiply(new BigDecimal(params[2]));
    }

    /**
     * Overridden onPostExecute that displays the result of the currency conversion
     *
     * @param result representing the result of the currency conversion
     */
    @Override
    protected void onPostExecute(BigDecimal result) {
        ProgressBar progressBar = progressBarRef.get();
        TextView tvConvertedAmount = tvConvertedAmountRef.get();

        // Check if references to the widgets in the activity are null, which in that case would cancel
        // the AsyncTask
        if (progressBar == null || tvConvertedAmount == null) {
            this.cancel(true);
        }

        progressBar.setVisibility(View.GONE);
        tvConvertedAmount.setText(result.setScale(3, BigDecimal.ROUND_DOWN).toPlainString());

        drawLineChart();
    }

    /**
     * Helper method that gets the system's current date and time
     *
     * @return String representing a date and time
     */
    private String getTodaysDate() {
        Date date = Calendar.getInstance().getTime();
        Log.d("DATE", date.toString());

        // Formatted to the currencyconverterapi API's requirement
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    /**
     * Helper method that gets the system's date and time 7 days prior to the current date
     *
     * @return String representing a date and time
     */
    private String getLastWeekDate() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -6);
        Date dateLastWeek = calendar.getTime();
        Log.d("DATE", dateLastWeek.toString());

        // Formatted to the currencyconverterapi API's requirement
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(dateLastWeek);

    }

    /**
     * Uses the MPAndroidChart library to draw a graph representing currency exchange rates for
     * the last 7 days
     *
     */
    private void drawLineChart() {
        LineChart lineChart = lineChartRef.get();
        Context activityContext = activityContextRef.get();

        // Check if references to the widgets in the activity are null, which in that case would cancel
        // the AsyncTask
        if (lineChart == null || activityContext == null) {
            this.cancel(true);
        }

        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            entries.add(new Entry(i, data.get(i).floatValue()));
        }

        // Uses a dataset to plot a line
        LineDataSet dataSet = new LineDataSet(entries, "Exchange rates per unit");
        lineChart.getDescription().setText("Exchange rates for the past 7 days per unit");
        lineChart.getDescription().setTextSize(16f);
        lineChart.getDescription().setPosition((float) (Resources.getSystem().getDisplayMetrics().widthPixels/ 1.5),20);

        // Styling the line
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        int color = activityContext.getResources().getColor(R.color.linechart_color);
        dataSet.setColor(color);
        dataSet.setDrawFilled(true);
        Drawable drawable = activityContext.getDrawable(R.drawable.gradient_color);
        dataSet.setFillDrawable(drawable);

        // Styling the dataset text
        lineChart.getAxisLeft().setTextColor(activityContext.getResources().getColor(R.color.White)); // left y-axis
        lineChart.getXAxis().setTextColor(activityContext.getResources().getColor(R.color.White));
        lineChart.getDescription().setTextColor(activityContext.getResources().getColor(R.color.White));
        Legend legend = lineChart.getLegend();
        legend.setTextColor(activityContext.getResources().getColor(R.color.White));

        // Controlling X axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        // Setting date values on the X axis
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dates.toArray(new String[dates.size()])[(int) value];
            }
        };
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);

        // Controlling Y axis'
        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();

        // Remove grid lines from Y axis
        yAxisLeft.setEnabled(false);
        yAxisRight.setEnabled(false);

        // Draw the line graph
        LineData data = new LineData(dataSet);
        lineChart.setData(data);
        lineChart.animateX(2000);

        LineData textData = lineChart.getData();
        textData.setValueTextColor(activityContext.getResources().getColor(R.color.White));

    }

}
