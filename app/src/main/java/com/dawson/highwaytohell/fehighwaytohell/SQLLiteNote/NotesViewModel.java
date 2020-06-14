package com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {

    private NotesRepository nR;

    private LiveData<List<Notes>> allNotes;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        nR = new NotesRepository(application);
        allNotes = nR.getAllNotes();
    }

    public LiveData<List<Notes>> getAllNotes() {
        return allNotes;
    }

    public void insertNewNote(Notes note) {
        nR.insert(note);
    }
}
