package com.example.priyanka.noteit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private ArrayList<notes> notesList = new ArrayList<>();  // Main content is here
    private RecyclerView recyclerView; // Layout's recyclerview
    private NotesAdapter nAdapter; // Data to recyclerview adapter
    ArrayList<notes> userdata = new ArrayList<notes>();
    private static final int B_REQUEST_CODE = 1;
    private static final int N_REQUEST_CODE = 2;
    private static final String TAG = "MainActivity";
    private final String RECYLER_STATE = "recyler_state";
    private TextView userTitle;
    private TextView userNote;
    private static Bundle myBundleRecylerState;
    private static int pos;
    String title, noteText, update;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
            case R.id.newnote:
                startActivityForResult(new Intent(this, EditActivity.class), N_REQUEST_CODE);
                return true;
            case R.id.about:
                startActivity(new Intent(this, AboutAct.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        nAdapter = new NotesAdapter(notesList,this);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        LinearLayoutManager myLayoutMgr = new LinearLayoutManager(this);
//        myLayoutMgr.setReverseLayout(true);
//        myLayoutMgr.setStackFromEnd(true);
//        recyclerView.setLayoutManager(myLayoutMgr);

//        notesList.add(new notes("","",""));
//        notes n = notesList.get(0);
//        n.setTitle("Welcome");
//        n.setNote("Hello. Welcome to Note It!");
//        n.setUpdate(DateFormat.getDateTimeInstance().format(new Date()));

        loadFile();
    }



    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        Log.d(TAG, "onClick when note is clicked");
        pos = recyclerView.getChildLayoutPosition(v);
        Intent intent = new Intent(v.getContext(), EditActivity.class);
        intent.putExtra("title", userdata.get(pos).getTitle().toString());
        intent.putExtra("noteText", userdata.get(pos).getNote().toString());
        intent.putExtra("update", userdata.get(pos).getUpdate().toString());
        Log.d(TAG, "In onClick" );
        Log.d(TAG, "Title: " + title );
        Log.d(TAG, "Note: "+ noteText);
        Log.d(TAG, "notes points to "+pos);
        startActivityForResult(intent, B_REQUEST_CODE);
    }

    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        final int pos = recyclerView.getChildLayoutPosition(v);
        notes n = notesList.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteWithIndex(pos);
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
//                Toast.makeText(MainActivity.this, "You cancelled your action of deleting the note", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setMessage("Are you sure you want to delete?");
        builder.setTitle("Delete");
        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    @Override
    protected void onPause(){
        super.onPause();
        saveNotes();
    }

    public void saveNotes(){
        //implementation same as in assignment 2
        Log.d(TAG, "saveNotes");
        try{
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginObject();
            Iterator<notes> iterator = userdata.iterator();
            notes temp;
            while(iterator.hasNext()){
                temp = iterator.next();
                writer.name("title").value(temp.getTitle());
                writer.name("noteText").value(temp.getNote());
                writer.name("update").value(temp.getUpdate());
            }
            writer.endObject();
//            writer.name("title").value(); -- what will be here? -- check example for saving multiple fields
            // should I save title, note, update OR save one note at a time
            // for i<pos, notes n = .get(pos); n.setText().....
            writer.close();
        }
        catch(Exception e){
            e.getStackTrace();
        }
    }

    public void deleteWithIndex(int i){
        if(!notesList.isEmpty()) {
            notesList.remove(i);
            userdata.remove(i);
            nAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        notes tempNote;
        if (requestCode == N_REQUEST_CODE){
            if (resultCode == RESULT_OK) {
                title = data.getStringExtra("title");
                noteText = data.getStringExtra("noteText");
                update = data.getStringExtra("update");

                notes n = new notes(title, noteText, update);
                userdata.add(0, n);
                notesList.add(0, new notes(title, noteText, update));
                Log.d(TAG, "Title: " + title );
                Log.d(TAG, "Note: "+ noteText);
                Log.d(TAG, "notes points to "+pos);
                nAdapter.notifyDataSetChanged();
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                LinearLayoutManager myLayoutMgr = new LinearLayoutManager(this);
                myLayoutMgr.setReverseLayout(true);
                myLayoutMgr.setStackFromEnd(true);
                recyclerView.setLayoutManager(myLayoutMgr);
            }
        }
        else if (requestCode == B_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                notes n2;
                title = data.getStringExtra("title");
                noteText = data.getStringExtra("noteText");
                update = data.getStringExtra("update");
                n2 = new notes(title, noteText, update);
                userdata.remove(pos);
                notesList.remove(pos);
                nAdapter.notifyDataSetChanged();
                userdata.add(0, n2);
                notesList.add(0, new notes(title, noteText, update));
                Log.d(TAG, "Title: " + title );
                Log.d(TAG, "Note: "+ noteText);
                Log.d(TAG, "notes points to "+pos);
                nAdapter.notifyDataSetChanged();
            }
        } else {

        }
    }

    public void loadFile() {
        //this implementation is the same as in assignment #2
        userdata.clear();
        notesList.clear();
        int counter = -1;
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(openFileInput("noteit.json"), "UTF-8"));
            reader.beginObject();
            int fieldcount = 0;
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("title")) {
                    fieldcount++;
                    title = reader.nextString();
                } else if (name.equals("update")) {
                    fieldcount++;
                    update = reader.nextString();
                } else if (name.equals("noteText")) {
                    fieldcount++;
                    noteText = reader.nextString();
                } else {
                    reader.skipValue();
                }
                if (fieldcount == 3) {
                    fieldcount = 0; // add the note to the list only once you get all 3 fields
                    counter++;
                    notes n = new notes(title, noteText, update);
                    userdata.add(n);
                    notesList.add(counter, new notes(title, noteText, update));
                    nAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class MyAsyncTask extends AsyncTask<Context, Integer, String> { //  <Parameter, Progress, Result>
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Context... contexts) {
            loadFile();
            return userdata.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}