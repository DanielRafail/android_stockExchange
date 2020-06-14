/**
 * DAO class, holds the dao and the queries that will be used in the app
 *
 * @author George Ilias
 */
package com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DAONotes {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Notes note);


    @Query("select * from Notes order by id asc")
    LiveData<List<Notes>> getAllNotes();


    @Delete
    void deleteNote(Notes note);

    @Update
    void updateNote(Notes note);


}
