package com.dawson.highwaytohell.fehighwaytohell.Notes;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dawson.highwaytohell.fehighwaytohell.R;
import com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote.Notes;
import com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote.NotesListAdapter;
import com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote.NotesViewModel;

import android.widget.Toast;


import java.util.List;

public class ViewShortNotesFragment extends AppCompatActivity {

    private NotesViewModel nvm;

    /**
     * Overriden onCreate method it will create a Recycler view and load it with all the items, as well as
     * hold the button for creating a new note
     *
     * @param outState
     * @author George ilias
     */
    @Override
    public void onCreate(Bundle outState) {

        super.onCreate(outState);
        setContentView(R.layout.short_recycler_view);

        RecyclerView rcv = findViewById(R.id.recyclerView);

        final NotesListAdapter nla = new NotesListAdapter(this, this.getApplication());

        rcv.setAdapter(nla);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        nvm = ViewModelProviders.of(this).get(NotesViewModel.class);
        nvm.getAllNotes().observe((LifecycleOwner) this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(@Nullable List<Notes> notes) {
                nla.setNotes(notes);
            }
        });

    }

    @Override
    public void onResume()
    {
        super.onResume();
        Toast.makeText(this,"To delete a note long click on the given note",Toast.LENGTH_LONG).show();

    }

    public void clickCreateButton(View view) {
        Intent it = new Intent(this, CreateNote.class);
        startActivity(it);
    }


}
