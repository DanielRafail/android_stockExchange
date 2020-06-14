/**
 * Adapter class for the recycler view
 *
 * @author George Ilias
 * used the code labs tutorial to learn how to do this with sqllite database
 */

package com.dawson.highwaytohell.fehighwaytohell.SQLLiteNote;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dawson.highwaytohell.fehighwaytohell.EditNotes;
import com.dawson.highwaytohell.fehighwaytohell.R;

import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NoteViewHolder> {

    private LayoutInflater inflater;
    private List<Notes> allNotes;
    private Context context;
    private Application app;

    /**
     * twin constructors in case a given amount of notes is needed in future vesions of the app
     *
     * @param context
     * @author George Ilias
     */
    public NotesListAdapter(Context context, Application app) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.app = app;
    }

    /**
     * Second constructor in the set of constructors
     *
     * @param context
     * @param notes
     * @author George Ilias
     */
    public NotesListAdapter(Context context, List<Notes> notes) {
        allNotes = notes;
        inflater = LayoutInflater.from(context);
    }

    /**
     * Will inflate the class created below adn return the viewHolder
     *
     * @param viewGroup
     * @param i
     * @return
     * @author George Ilias
     */
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View viewItem = inflater.inflate(R.layout.short_recycler_note, viewGroup, false);
        return new NoteViewHolder(viewItem);
    }

    /**
     * Method to bind all the notes in the database once the ViewHolder has been created
     * will also create a click event that will use extras and create an intent to properly display the
     * subject and text
     * a second long click event will be set so that once clicked the note will be deleted from the dataabse and the the observer will be notified
     *
     * @param noteViewHolder
     * @param i
     * @author George Ilias
     */
    @Override
    public void onBindViewHolder(@NonNull final NoteViewHolder noteViewHolder, int i) {
        if (allNotes != null) {
            final Notes current = allNotes.get(i);
            noteViewHolder.subject.setText(current.getSubject());
            noteViewHolder.setNote(current);
            noteViewHolder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(context, EditNotes.class);
                    it.putExtra("subject", current.getSubject());
                    it.putExtra("text", current.getText());
                    it.putExtra("id",current.getId());
                    context.startActivity(it);
                }
            });
            noteViewHolder.parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    try {
                        NotesRepository nr = new NotesRepository(app);
                        nr.deleteNote(noteViewHolder.getNote());
                        notifyDataSetChanged();
                        Toast.makeText(context, "Column deleted", Toast.LENGTH_LONG).show();
                        return true;

                    } catch (Exception e) {
                        Toast.makeText(context, "error when deleting column", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
            });
        }
    }

    /**
     * setNotes will set a given list of notes and be used by the NotesRepository and onObserve
     * on the ViewShortNotesFragment class
     *
     * @param note
     * @author George Ilias
     */
    public void setNotes(List<Notes> note) {
        this.allNotes = note;
        notifyDataSetChanged();
    }

    /**
     * getItemCount() simply returns the amount of notes in the list
     *
     * @return
     * @author George Ilias
     */
    @Override
    public int getItemCount() {
        if (allNotes != null)
            return allNotes.size();
        else
            return 0;
    }

    /**
     * class that will be used to create the backbone of the ViewHolder(will be used to
     * be inflated in the onCreateViewHolder method)
     *
     * @author George Ilias
     */
    class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView subject;
        
        private final RelativeLayout parent;

        private Notes note;

        private TextView id;

        private TextView company;

        private TextView ticker;




        private NoteViewHolder(View item) {
            super(item);
            subject = item.findViewById(R.id.subject);
            parent = item.findViewById(R.id.layoutItem);
        }

        public Notes getNote() {
            return this.note;
        }

        public void setNote(Notes note) {
            this.note = note;
        }

    }
}

