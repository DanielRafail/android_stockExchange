/**
 * AsyncTask Class used for updating a Notes object into the database
 *
 * @author George Ilias
 */
package com.dawson.highwaytohell.fehighwaytohell.AsyncNotesClass;

import android.os.AsyncTask;

import com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote.DAONotes;
import com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote.Notes;

public class UpdateAsyncTask extends AsyncTask<Notes,Void,Void> {
    private DAONotes note;


    public UpdateAsyncTask(DAONotes notes) {
        note = notes;
    }


    /**
     * Updates a given Notes object in the DAONotes object, in the bg thread
     * @author George Ilias
     * @param notes
     * @return
     */

    @Override
    protected Void doInBackground(Notes... notes) {

        note.updateNote(notes[0]);
        return null;
        }

}
