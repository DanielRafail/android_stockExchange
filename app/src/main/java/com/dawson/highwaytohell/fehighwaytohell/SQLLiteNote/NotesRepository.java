/**
 * class to make the methods to receive and insert columns better able to be used
 *
 * @author George Ilias
 * With the code labs that was provided by Campbell
 */
package com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.dawson.highwaytohell.fehighwaytohell.AsyncNotesClass.DeleteAsyncTask;
import com.dawson.highwaytohell.fehighwaytohell.AsyncNotesClass.InsertAsyncTask;
import com.dawson.highwaytohell.fehighwaytohell.AsyncNotesClass.UpdateAsyncTask;

import java.util.List;

public class NotesRepository {
    private DAONotes notesDAO;
    private LiveData<List<Notes>> listNotes;
    NotesDatabase nd;

    /**
     * Constructor to create the list of all notes in the database and
     * the DAONotes object;
     *
     * @param application
     * @uahtor George Ilias
     */
    public NotesRepository(Application application) {
        nd = NotesDatabase.getDatabase(application);
        notesDAO = nd.daoNotes();
        listNotes = notesDAO.getAllNotes();
    }

    /**
     * Single Notes object insert
     *
     * @param note
     * @author George Ilias
     */
    public void insert(Notes note) {
        new InsertAsyncTask(notesDAO).execute(note);
    }

    /**
     * Multiple Notes Object inserts
     *
     * @param notes
     * @author George Ilias
     */
    public void insert(List<Notes> notes) {
        for (Notes in : notes) {
            new InsertAsyncTask(notesDAO).execute(in);
        }
    }

    /**
     * method to receive all note sin the database(received in the
     * constructor and returned here)
     *
     * @return
     * @author George Ilias
     */
    public LiveData<List<Notes>> getAllNotes() {
        return this.listNotes;
    }

    /**
     * delete method to be used when testing(will delete a given note based on subbject and text)
     *
     * @author George Ilias
     */
    public void deleteNote(Notes note) {
        new DeleteAsyncTask(notesDAO).execute(note);
    }
    /**
     * Update method that will create a new AsyncTask
     * @author George Ilias
     * @param note
     * @return none
     */
    public void updateNote(Notes note)
    {
        new UpdateAsyncTask(notesDAO).execute(note);
    }


}
