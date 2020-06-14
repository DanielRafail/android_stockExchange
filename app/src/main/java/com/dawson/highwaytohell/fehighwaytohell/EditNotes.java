package com.dawson.highwaytohell.fehighwaytohell;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote.Notes;
import com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote.NotesRepository;

public class EditNotes extends Activity{


    private EditText subject;
    private EditText text;
    private int id;
    public boolean isSaved;

    @Override
    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.edit_note_activity);

        text = (EditText) findViewById(R.id.textIdView);
        subject = (EditText) findViewById(R.id.subjectIdView);


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
        id= getIntent().getIntExtra("id",0);

    }

    /**
     * Method to update a given note when it is clicked on in the list
     *
     * @author George Ilias
     * @return none
     */
    public void updateNote(View view){

        if (subject.getText() != null && !subject.getText().equals("")) {
            NotesRepository nr = new NotesRepository(this.getApplication());

            Notes note = new Notes(subject.getText().toString(), text.getText().toString());
            note.setId(id);
            nr.updateNote(note);
            isSaved = true;

            finish();
        } else {
            Toast.makeText(this, "Error something went wrong here", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStop()
    {
        super.onStop();
        if(isSaved == false) {
            updateNote(this.getCurrentFocus().getRootView());
        }
    }

}
