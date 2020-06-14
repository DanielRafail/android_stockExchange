package com.dawson.highwaytohell.fehighwaytohell;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dawson.highwaytohell.fehighwaytohell.ForeignExchange.ForeignExchangeActivity;
import com.dawson.highwaytohell.fehighwaytohell.AsyncForeignExchangeClass.RetrieveSymbolsTask;
import com.dawson.highwaytohell.fehighwaytohell.ForeignExchange.NoConnectionDialogFragment;
import com.dawson.highwaytohell.fehighwaytohell.Hints.HintActivity;
import com.dawson.highwaytohell.fehighwaytohell.LoanCalculator.CalculatorActivity;
import com.dawson.highwaytohell.fehighwaytohell.Notes.ViewShortNotesFragment;
import com.dawson.highwaytohell.fehighwaytohell.RealPortfolio.StockObject;
import com.dawson.highwaytohell.fehighwaytohell.RealPortfolio.StockObjectAsyncTask;
import com.dawson.highwaytohell.fehighwaytohell.StockQuotes.StockQuotesActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.dawson.highwaytohell.fehighwaytohell.Constants.User;
import com.dawson.highwaytohell.fehighwaytohell.Portfolio.Portfolio;
import com.dawson.highwaytohell.fehighwaytohell.Portfolio.PortfolioAsyncTaskLogin;

/**
 * Class responsible for the mainActivity (first that boots). Shows layouts and allows for access to different activities
 *
 * @author 1633028
 * @version 1.0.0
 */
public class MainActivity extends AppCompatActivity{

    private ArrayList<String> symbols = new ArrayList<>();
    private List<StockObject> stocksList=new ArrayList<>();
    private RetrieveSymbolsTask symbolsTask = new RetrieveSymbolsTask();
    private DrawerLayout mDrawerLayout;



    /**
     * override of onCreate. If shared preferences does not exist, create intent to settings
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPrefs = getSharedPreferences("saves", MODE_PRIVATE);
        if (!sharedPrefs.contains("username")) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }
        if (sharedPrefs.contains("symbols")) {
            Log.d("Retrieve", "Symbols is not empty");
            String tempString = sharedPrefs.getString("symbols", null);
            String[] tempArray = tempString.split(",");
            symbols.addAll(Arrays.asList(tempArray));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragments(savedInstanceState);
        setNavigation();
        setToolbar();

        if (isConnected() && symbols.isEmpty()) {
            Log.d("Retrieve", "Symbols is empty");
            symbolsTask.execute();
        }
        if(!User.isLogged()){
            PortfolioAsyncTaskLogin task = new PortfolioAsyncTaskLogin(this);
            task.execute();
        }
    }

    /**
     * Override onResume to start an intent to settings if shared preferences is empty
     */
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = getSharedPreferences("saves", MODE_PRIVATE);
        if (!sharedPrefs.contains("username")) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }
    }

    /**
     * https://developer.android.com/training/basics/fragments/fragment-ui
     * sets the fragments
     */
    private void setFragments(Bundle savedInstanceState){

        if (findViewById(R.id.content_frame) != null) {
            int orientation = getResources().getConfiguration().orientation;
            SharedPreferences sharedPrefs = getSharedPreferences("saves", MODE_PRIVATE);

            Bundle data = new Bundle();//Use bundle to pass data
            data.putString("data",sharedPrefs.getString("username", "" ));//put string, int, etc in bundle with a key value

            MainActivityVertical vertical = new MainActivityVertical();
            MainActivityHorizontal horizontal = new MainActivityHorizontal();

            horizontal.setArguments(data);//Finally set argument bundle to fragment
            vertical.setArguments(data);//Finally set argument bundle to fragment


            if (savedInstanceState != null) {
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame, vertical).commit();
                }else{
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame, horizontal).commit();
                }
            }else{
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.content_frame, vertical).commit();
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.content_frame, horizontal).commit();
                }
            }

        }
    }




    /**
     * create intent to go to about
     *
     * @param view the view the button was clicked on
     */
    public void gotoAbout(View view) {
        Intent intent = new Intent(this, About.class);
        startActivity(intent);
    }

    /**
     * create intent to go to foreign exchange
     *
     * @param view the view the button was clicked on
     */
    public void gotoForeign(View view) {
        if (symbolsTask.getStatus() == AsyncTask.Status.FINISHED) {
            try {
                symbols = symbolsTask.get();

            } catch (ExecutionException e) {
                Log.wtf("Execution", e);
                Toast.makeText(this, getResources().getString(R.string.internetoast), Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                Log.wtf("Interrupted", e);
                Toast.makeText(this, getResources().getString(R.string.internetoast), Toast.LENGTH_SHORT).show();
            }

        } else if (symbols.isEmpty()) {
            RetrieveSymbolsTask symbolsTaskAgain = new RetrieveSymbolsTask();
            symbolsTaskAgain.execute();
        }

        if (!isConnected()) {
            NoConnectionDialogFragment noConnectionDialog = new NoConnectionDialogFragment();
            noConnectionDialog.show(getSupportFragmentManager(), "Tag");

        } else {

            // Prepare a String with all symbols concatenated, separated by commas
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < symbols.size(); i++) {
                sb.append(symbols.get(i)).append(",");
            }

            // Save symbols in SharedPreferences
            SharedPreferences prefs = getSharedPreferences("saves", MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = prefs.edit();
            prefsEditor.putString("symbols", sb.toString());
            prefsEditor.apply();

            // Fire Foreign Exchange activity, passing it the list of symbols
            Intent intent = new Intent(this, ForeignExchangeActivity.class);
            intent.putStringArrayListExtra("symbols", symbols);
            startActivity(intent);
        }
    }


    /**
     * create intent to go to hints
     *
     * @param view the view the button was clicked on
     */
    public void gotoHints(View view) {
        if (!isConnected()){
            NoConnectionDialogFragment noConnectionDialog = new NoConnectionDialogFragment();
            noConnectionDialog.show(getSupportFragmentManager(), "Tag");

        } else {
            Intent intent = new Intent(this, HintActivity.class);
            startActivity(intent);
        }
    }

    /**
     * create intent to go to stock quote
     *
     * @param view the view the button was clicked on
     */
    public void gotoQuote(View view) {
        if (!isConnected()){
            NoConnectionDialogFragment noConnectionDialog = new NoConnectionDialogFragment();
            noConnectionDialog.show(getSupportFragmentManager(), "Tag");

        } else {
            Intent intent = new Intent(this, StockQuotesActivity.class);
            startActivity(intent);
        }
    }

    /**
     * create intent to go to notes
     *
     * @param view the view the button was clicked on
     */
    public void gotoNotes(View view) {
        Intent intent = new Intent(this, ViewShortNotesFragment.class);
        startActivity(intent);
    }

    /**
     * create intent to go to a calculator_inputs
     *
     * @param view the view the button was clicked on
     */
    public void gotoCalculator(View view) {
        Intent intent = new Intent(this, CalculatorActivity.class);
        startActivity(intent);
    }

    /**
     * create intent to go to the portfolio simulator
     *
     * @param view the view the button was clicked on
     */
    public void gotoPortfolio(View view) {
        if (!isConnected()){
            NoConnectionDialogFragment noConnectionDialog = new NoConnectionDialogFragment();
            noConnectionDialog.show(getSupportFragmentManager(), "Tag");

        } else {
            if(User.isLogged()){
                Intent intent = new Intent(this, Portfolio.class);
                startActivity(intent);
            }
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();

    }


    /**
     * Sets the toolbar
     */
    private void setToolbar(){
        Toolbar toolbar = findViewById(R.id.includeid);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.drawerlogo);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets the navigation
     */
    private void setNavigation(){

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nvView);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        String url = "https://www.dawsoncollege.qc.ca/computer-science-technology/";
                        Intent dawsonWebsite = new Intent(Intent.ACTION_VIEW);
                        dawsonWebsite.setData(Uri.parse(url));

                        switch(menuItem.getItemId()){
                            case(R.id.about):
                                Intent aboutIntent = new Intent(MainActivity.this, About.class);
                                startActivity(aboutIntent);
                                break;
                            case(R.id.dawson):
                                Intent dawsonIntent = new Intent(dawsonWebsite);
                                startActivity(dawsonIntent);
                                break;
                            case(R.id.settings):
                                Intent settingsIntent = new Intent(MainActivity.this, Settings.class);
                                startActivity(settingsIntent);
                                break;

                            default:
                                break;
                        }
                        return true;
                    }
                });
    }
}
