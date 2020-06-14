package com.dawson.highwaytohell.fehighwaytohell.Hints;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dawson.highwaytohell.fehighwaytohell.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

/**
 * Load random hints from the firebase database and displays them.
 * If you click on the source, it will open the source in a browser.
 */
public class HintActivity extends Activity {
    private static final String TAG = "HintActivity";
    private ArrayList<HintBean> hintArray;
    private TextView hint;
    private TextView source;
    private boolean informationReceived = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);
        hintArray = new ArrayList<>();
        hint = findViewById(R.id.hint);
        source = findViewById(R.id.source);
        loadAllHints(savedInstanceState);
    }




    /**
     * Override of onInstanceState to save the data in case of minor interruptions
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("source", source.getText().toString());
        savedInstanceState.putString("hint", hint.getText().toString());
    }

    /**
     * Override of onRestoreInstanceState which allows to read the instance State if an interruption
     * happened so that the user can have a smooth experience with the app
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        hint.setText(savedInstanceState.getString("hint"));
        source.setText(savedInstanceState.getString("source"));
    }



    private void loadAllHints(final Bundle savedInstanceState) {
        DatabaseReference fBase = FirebaseDatabase.getInstance().getReference();
        fBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot c : dataSnapshot.getChildren()) {
                    HintBean hb = c.getValue(HintBean.class);
                    hintArray.add(hb);
                }
                Log.d(TAG, "hints loaded");
                if(savedInstanceState == null) {
                    showHint(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Loading database failed " + databaseError.getMessage());
            }
        });
    }

    public void showHint(View view) {
            Log.i(TAG, "showHint: Showing hint");
            Random random = new Random();
            int randomNum = random.nextInt(hintArray.size());
            HintBean hb = hintArray.get(randomNum);
            hint.setText(hb.getText());
            source.setText(hb.getUrl());
    }

    public void goToSource(View view) {
            Log.i(TAG, "goToSource: Opening the source");
            Uri uri = Uri.parse(source.getText().toString());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
    }

}
