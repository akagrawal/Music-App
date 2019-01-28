package com.example.arpit.musicapp;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static int mypermission = 1;
    final private String[] perm = {Manifest.permission.READ_EXTERNAL_STORAGE};

    public void setRecyclerView(MyAdapter ada){

        RecyclerView rv = findViewById(R.id.songlist);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(ada);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this, perm, mypermission);
        //start from here..
        //tracks_list tl = new tracks_list((View)findViewById(R.id.playbar), (ImageButton) findViewById(R.id.bar_pp));
        SongsManager sm =  new SongsManager(this);
        Player player = new Player(this, sm);
        MyAdapter ada = new MyAdapter(player, sm);
        setRecyclerView(ada);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Resume:", "start=============>ss");
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