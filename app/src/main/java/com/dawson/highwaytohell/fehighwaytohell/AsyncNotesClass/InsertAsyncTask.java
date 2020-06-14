/**
 * Class to insert an enrty in the background thread to keep the ui thread clean and responsive
 *
 * @author George Ilias
 * with the code labs provided by  Campbell
 */
package com.dawson.highwaytohell.fehighwaytohell.AsyncNotesClass;

import android.os.AsyncTask;

import com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote.DAONotes;
import com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote.Notes;

public class InsertAsyncTask extends AsyncTask<Notes, Void, Void> {
    private DAONotes note;

    /**
     * constructor that is given a DAONotes object
     *
     * @param notes
     * @author George Ilias
     */
    public InsertAsyncTask(DAONotes notes) {
        note = notes;
    }

    /**
     * will call the DAONotes object's insert method to insert an item into the database
     *
     * @param param
     * @return
     * @author George Ilias
     */
    @Override
    protected Void doInBackground(final Notes... param) {
        note.insert(param[0]);
        return null;
    }

}
