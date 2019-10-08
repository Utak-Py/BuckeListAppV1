package com.example.numbingnote.buckelistappv1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

//    to store our notes
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    static Set<String> set;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView)findViewById(R.id.listView);

//           saving our data permanently after the intent transition and edidnote updatae on CharSequence

        SharedPreferences sharedPreferences= this.getSharedPreferences("com.example.numbingnote.buckelistappv1",Context.MODE_PRIVATE);

        set =  sharedPreferences.getStringSet("notes",null);

        notes.clear();

        if (set != null){ // if set is not null then we add all the notes in set(set is our database basically through shPref) to the local notes
             notes.addAll(set);
        }else{

            notes.add("Example note");
            set = new HashSet<String>();
            set.addAll(notes);
//            now we remove anything that was stored in the shared pref in this case as there should be nothing there
            sharedPreferences.edit().remove("notes").apply();

            sharedPreferences.edit().putStringSet("notes",set).apply();
        }




            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,notes);

            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                  To transition into the editNoteView on click
                    Intent intent = new Intent(getApplicationContext(), EditNote.class);
                    intent.putExtra("noteId",position);
                    startActivity(intent);
                }
            });

//            the delete functionality

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Are you sure?")
                            .setMessage("Do you want to delete this item?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    notes.remove(position);
                                    SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("com.example.numbingnote.buckelistappv1",Context.MODE_PRIVATE);
                                    if (set == null){
                                        set = new HashSet<String>();
                                    }else{
                                        set.clear();
                                    }

                                    set.addAll(notes);

//                controlling the app so that it does not duplicate notes
                                    sharedPreferences.edit().remove("notes").apply();
                                    sharedPreferences.edit().putStringSet("notes",set).apply();
                                    arrayAdapter.notifyDataSetChanged();


                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

                }
            });

////saving data through Shared Preferences look iot up
//        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.numbingnote.buckelistappv1", Context.MODE_PRIVATE);
//        sharedPreferences.edit().putString("name", "Taku").apply();
//
//        String name = sharedPreferences.getString("name", "");
//
////checking if its actuially working using the log
//        Log.i("name",name);


//        switching from activity to activity through the floating "+" button
//        to do this we need to use intent
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notes.add("");
                SharedPreferences sharedPreferences= getApplicationContext().getSharedPreferences("com.example.numbingnote.buckelistappv1",Context.MODE_PRIVATE);
                if (set == null){
                    set = new HashSet<String>();
                }else{
                    set.clear();
                }

                set.addAll(notes);

//                controlling the app so that it does not duplicate notes
                sharedPreferences.edit().remove("notes").apply();

                sharedPreferences.edit().putStringSet("notes",set).apply();
                arrayAdapter.notifyDataSetChanged();

//                directing where we get the axctivity from through intent and getAppContext
                Intent intent = new Intent(getApplicationContext(), EditNote.class);

                intent.putExtra("noteId",notes.size()-1);

                startActivity(intent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
