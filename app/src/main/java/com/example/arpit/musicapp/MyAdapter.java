package com.example.arpit.musicapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter {

    static int size = 26;
    static String arr[] = new String[size];
    public MyAdapter(){

        for(int i=0;i<size;i++){

            int temp = (int)'a';
            temp+=i;
            arr[i] = ""+(char)temp;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        public  MyViewHolder(View v){
            super(v);
            this.mTextView = v.findViewById(R.id.title);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MyViewHolder vh = (MyViewHolder) holder;
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
