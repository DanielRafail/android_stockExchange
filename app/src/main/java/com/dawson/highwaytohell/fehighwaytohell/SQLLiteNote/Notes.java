/**
 * Entity class to give the database a class to work on to make a Table
 * in this case its the note Table
 *
 * @author George Ilias
 */
package com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * this class will define the TableName
 * the column names and a constructor to create the object.
 *
 * Once entered an object can be inserted using NotesRepository and DAONotes
 *
 * @author George Ilias
 * With help from the code labs supplied by Campbell
 */
@Entity(tableName = "Notes")
public class Notes {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "subject")
    private String subject;

    @NonNull
    @ColumnInfo(name = "text")
    private String text;

    /**
     * constructor to create the object
     *
     * @param subject
     * @param text
     * @author George Ilias
     */
    public Notes(String subject, String text) {
        this.subject = subject;
        this.text = text;
    }

    /**
     * getters  and setters for the class below
     *
     * @author George Ilias
     */
    public int getId() {
        return this.id;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getText() {
        return this.text;
    }

    public void setId(int id) {
        this.id = id;
    }


}
