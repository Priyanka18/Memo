package com.example.priyanka.noteit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

public class notes_list_row extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list_row);
    }
}
