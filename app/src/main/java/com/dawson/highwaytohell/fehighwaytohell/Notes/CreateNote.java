package com.dawson.highwaytohell.fehighwaytohell.Notes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dawson.highwaytohell.fehighwaytohell.R;
import com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote.Notes;
import com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote.NotesRepository;

public class CreateNote extends Activity {

    private EditText subject;
    private EditText text;

    @Override
    public void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.create_note_layout);
    }

    /**
     * method to create a note in an asynctack as opposed to the ui thread
     * simply checks if the code is
     *
     * @param view
     */
    public void createNote(View view) {

        subject = (EditText) findViewById(R.id.completeSubject);
        text = (EditText) findViewById(R.id.completeText);

        if (subject.getText() != null && !subject.getText().equals("")) {
                NotesRepository nr = new NotesRepository(this.getApplication());

                Notes note = new Notes(subject.getText().toString(), text.getText().toString());
                nr.insert(note);
                finish();
        } else {
            Toast.makeText(this, "Error the subject is empty", Toast.LENGTH_SHORT).show();
        }
    }
}
