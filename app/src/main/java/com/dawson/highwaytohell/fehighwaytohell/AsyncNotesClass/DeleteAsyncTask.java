/**
 * Delete class to delete the given note from the database
 *
 * @author George Ilias
 * Based off of the InsertAsyncTask class that was base doff the Code labs given by Campbell
 */
package com.dawson.highwaytohell.fehighwaytohell.AsyncNotesClass;

import android.os.AsyncTask;

import com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote.DAONotes;
import com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote.Notes;

public class DeleteAsyncTask extends AsyncTask<Notes, Void, Void> {
    private DAONotes daNotes;

    /**
     * Constructor that will be given a DAONotes object that will be the one to actually be talking to and changing the database
     * in the bg thread
     *
     * @param note
     * @author George Ilias
     */
    public DeleteAsyncTask(DAONotes note) {
        this.daNotes = note;
    }

    /**
     * doInBackground will call the deleteNote method from the DAONotes object
     *
     * @param params
     * @return
     */
    @Override
    public Void doInBackground(final Notes... params) {
        daNotes.deleteNote(params[0]);
        return null;
    }
}
