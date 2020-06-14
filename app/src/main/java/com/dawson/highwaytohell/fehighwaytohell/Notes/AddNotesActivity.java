package com.dawson.highwaytohell.fehighwaytohell.Notes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.dawson.highwaytohell.fehighwaytohell.R;
import com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote.Notes;
import com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote.NotesRepository;

public class AddNotesActivity extends Activity {


    @Override
    public void onCreate(Bundle outState) {
        super.onCreate(outState);

        setContentView(R.layout.new_note_layout);
    }


    public void onClickAddButton(View view) {
        EditText subject = (EditText) findViewById(R.id.subject);
        EditText text = (EditText) findViewById(R.id.text);
        Notes note = new Notes(subject.getText().toString(), text.getText().toString());

        NotesRepository nR = new NotesRepository(this.getApplication());
        nR.insert(note);

        System.out.println("finished");
        finish();

    }
}
