package com.example.priyanka.noteit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by priya on 13-02-2018.
 */

public class MyViewHolder extends RecyclerView.ViewHolder{
    public TextView title;
    public TextView note;
    public TextView update;

    public MyViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.title);
        note = (TextView) view.findViewById(R.id.noteText);
        update = (TextView) view.findViewById(R.id.update);
    }
}
