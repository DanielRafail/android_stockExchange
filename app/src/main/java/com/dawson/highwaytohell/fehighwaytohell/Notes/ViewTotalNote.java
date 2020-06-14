/**
 * Class that will show to the user the complete view of the given note
 *
 * @author George Ilias
 */

package com.dawson.highwaytohell.fehighwaytohell.Notes;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.dawson.highwaytohell.fehighwaytohell.R;

import org.w3c.dom.Text;

public class ViewTotalNote extends Activity {

    private TextView subject;
    private TextView text;

    @Override
    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.full_note_view);

        text = (TextView) findViewById(R.id.textIdView);
        subject = (TextView) findViewById(R.id.subjectIdView);

        loadIntentsExtra();

    }


    /**
     * Private method to load the intents into the ui
     *
     * @author George Ilias
     */
    private void loadIntentsExtra() {
        text.setText(getIntent().getStringExtra("text"));
        subject.setText(getIntent().getStringExtra("subject"));

    }


}
