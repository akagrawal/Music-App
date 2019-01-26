package com.example.arpit.musicapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter {

    static int x = 1;
    static int count = 0;
    Player player = null;
    SongsManager manager = null;

    public MyAdapter(Player player, SongsManager sm){
        this.player = player;
        this.manager = sm;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        Player.MyViewHolder vh = (Player.MyViewHolder) holder;
        player.updateOnBind(vh, position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("Created:",MyAdapter.count+"");
        count++;
        Player.MyViewHolder vh = player.createViewHolder(parent, viewType);
        return vh;
    }
    @Override
    public int getItemCount() {
        //Log.e("Size: ", manager.mTracks.size()+"");
        return manager.mTracks.size();
    }
}