package com.example.arpit.musicapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MyAdapter extends RecyclerView.Adapter  implements View.OnClickListener {

    static int size = 26;
    static String arr[] = new String[size];
    public MyAdapter(){

        for(int i=0;i<size;i++){
            int temp = (int)'a';
            temp+=i;
            arr[i] = ""+(char)temp;
        }
    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick ");
        ImageButton bt = (ImageButton)v;
        bt.setBackgroundResource(R.drawable.pause);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public ImageButton mPlay;
        public  MyViewHolder(View v){
            super(v);
            this.mTextView = v.findViewById(R.id.title);
            this.mPlay = (ImageButton) v.findViewById(R.id.play);
            ((View)mPlay).setOnClickListener(MyAdapter.this);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyViewHolder vh = (MyViewHolder) holder;
        vh.mPlay.setBackgroundResource(R.drawable.play);
        vh.mTextView.setText(arr[position]);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.songrow, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public int getItemCount() {
        return size;
    }
}
