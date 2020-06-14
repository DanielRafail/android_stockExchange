/**
 * This class will build a database named Notes Database based off the @Entity class NotesDatabase
 *
 * @author George Ilias
 * Created with the help of the code labs Campbell gave
 */
package com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

/**
 * abstract class, therefor no Constructor
 * the class will build the database and create an abstract DAONotes class
 * as well as a NotesDatabase instance that will be used to put the built database into
 * IDEALLY
 *
 * @author george Ilias & the code labs
 */
@Database(entities = {Notes.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {

    public abstract DAONotes daoNotes();

    private static volatile NotesDatabase INSTANCE;

    /**
     * the method that will actually have the Database being built
     *
     * @param context
     * @return author George Ilias
     */
    static NotesDatabase getDatabase(Application context) {
        if (INSTANCE == null) {
            synchronized (NotesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NotesDatabase.class, "Notes Database").build();
                }
            }
        }
        return INSTANCE;
    }


}
