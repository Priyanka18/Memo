package com.example.priyanka.noteit;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by priya on 13-02-2018.
 */

public class NotesAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private static final String TAG = "NotesAdapter";
    private List<notes> notesList;
    private MainActivity mainAct;

    public NotesAdapter(List<notes> nList, MainActivity ma) {
        this.notesList = nList;
        mainAct = ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_notes_list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        notes notes = notesList.get(position);
        holder.title.setText(notes.getTitle());
        holder.note.setText(notes.getNote());
        holder.update.setText(notes.getUpdate());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
