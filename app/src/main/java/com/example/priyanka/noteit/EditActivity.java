package com.example.priyanka.noteit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditActivity extends AppCompatActivity {
    private static final String TAG = "EditActivity";
    private TextView datetimeTextView;
    private notes notes;
    private EditText title;
    private EditText noteText;
    private EditText update;

    private String datetime;
    private String datetimestring;
    private SimpleDateFormat df;
    private Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        title = findViewById(R.id.title);
        noteText = findViewById(R.id.notes);
        noteText.setMovementMethod(new ScrollingMovementMethod());
        noteText.setTextIsSelectable(true);

        datetimeTextView = (TextView) findViewById(R.id.update);
        c = Calendar.getInstance();
        df = new SimpleDateFormat("dd-MMM-yyyy 'at' HH:mm:ss a");
        datetimestring = df.format(c.getTime());

        Intent intent = getIntent();
        if (intent.hasExtra("title")) {
//            notes mynotes = (notes) intent.getSerializableExtra("Notes");
            title.setText(intent.getStringExtra("title"));
        }
        if(intent.hasExtra("noteText")){
            noteText.setText(intent.getStringExtra("noteText"));
        }
            Log.d(TAG, "Title: " + title );
            Log.d(TAG, "Note: "+ noteText);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to save  your note before exiting?");
        builder.setTitle("Save");
        final String datetime = DateFormat.getDateTimeInstance().format(new Date());
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent data = new Intent();
                data.putExtra("title", title.getText().toString());
                data.putExtra("noteText", noteText.getText().toString());
                data.putExtra("update", datetime);
                setResult(RESULT_OK, data);
                EditActivity.super.onBackPressed();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            //Do not save the note, just go back to MainActivity
                EditActivity.super.onBackPressed();
            }
        });

        AlertDialog dialog = builder.create();
        title = findViewById(R.id.title);
        if (!title.getText().toString().isEmpty()){
            dialog.show();
        }
        else{
            EditActivity.super.onBackPressed();
            Toast.makeText(this, "Your note could not be saved! ", Toast.LENGTH_LONG).show();
        }
    }
}