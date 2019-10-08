package com.example.numbingnote.buckelistappv1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.HashSet;

import static com.example.numbingnote.buckelistappv1.MainActivity.notes;
import static com.example.numbingnote.buckelistappv1.MainActivity.set;

public class EditNote extends AppCompatActivity implements TextWatcher {

//    global variable for position
    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        making sure that after transitioning from mainactivity the edit text keeps the same note in editNoteView
        EditText editText =(EditText)findViewById(R.id.editText);

//        getting the intent that started the view after onItemClick from ManiActivity
        Intent i = getIntent();
        noteId = i.getIntExtra("noteId",-1);//defVAlue is negative since there is always something in the list so it will help detect erros

        if (noteId != -1){
        String fillerText = notes.get(noteId);
        editText.setText(fillerText);
        }

        editText.addTextChangedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainIntent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//       set value of note to the updated version in real time...update it
//       char sequence is the sequence of chars currents being put into the editText
        notes.set(noteId,String.valueOf(charSequence));

        //then update the array adapter so that it knpws that the data has been changed
        MainActivity.arrayAdapter.notifyDataSetChanged();

        SharedPreferences sharedPreferences= this.getSharedPreferences("com.example.numbingnote.buckelistappv1",Context.MODE_PRIVATE);
        if (set == null){
            set = new HashSet<String>();
        }else{
            set.clear();
        }
        set.clear();
        set.addAll(notes);//        now we remove anything that was stored in the shared pref in this case as there should be nothing there
        sharedPreferences.edit().remove("notes").apply();
        sharedPreferences.edit().putStringSet("notes",set).apply();

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
