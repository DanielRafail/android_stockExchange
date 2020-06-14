package com.dawson.highwaytohell.fehighwaytohell;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Class responsible for the About. Shows information on the authors along with their picture
 * @author 1633028
 * @version 1.0.0
 */
public class About extends Activity{

    /**
     * Shows a toast when user clicks on an image
     * @param view the view it belongs to
     */
    public void showBlurb(View view) {
        Context context = getApplicationContext();
        CharSequence text = getResources().getString(R.string.blurb);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * Default onCreate
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }
}
