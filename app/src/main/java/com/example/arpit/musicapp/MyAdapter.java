package com.example.arpit.musicapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter {

    static int size = 26;
    static public boolean stop = true;
    public View playbar = null;
    public RecyclerView rv = null;
    static int playing = -1;
    static String arr[] = new String[size];
    static ImageButton mPlaying = null;
    public MyAdapter(RecyclerView rv, View v){

        this.rv = rv;
        this.playbar = v;
        for(int i=0;i<size;i++){
            int temp = (int)'a';
            temp+=i;
            arr[i] = ""+(char)temp;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mTextView;
        public ImageButton mPlay;
        public  MyViewHolder(View v){
            super(v);
            this.mTextView = v.findViewById(R.id.title);
            this.mPlay = (ImageButton) v.findViewById(R.id.play_pause);
            this.mPlay.setOnClickListener(this);
           // ((View)mPlay).setOnClickListener(MyAdapter.this);
        }

        @Override
            public void onClick(View v) {

                if(stop==true){
                    stop = false;

                    //ViewGroup.LayoutParams params_playbar = playbar.getLayoutParams();
                    //ViewGroup.LayoutParams params_rv = rv.getLayoutParams();
                    //params_playbar.height = rv.getHeight()/10;
                    //playbar.requestLayout();
                    //ImageButton pp = playbar.findViewById(R.id.bar_album);
                    //rv.requestLayout();
                    /// ImageButton bar_pp = playbar.findViewById(R.id.bar_pp);
                    //bar_pp.setBackgroundResource(R.drawable.play);
                    playbar.setVisibility(View.VISIBLE);
                }

                int position = this.getAdapterPosition();   //position of this view holder which is clicked
                ImageButton bt = (ImageButton)v;
                if(position==playing){
                    playing = -1;
                    bt.setBackgroundResource(R.drawable.pause);
                }
                else{
                    playing = position;
                    if(mPlaying != null)
                        MyAdapter.mPlaying.setBackgroundResource(R.drawable.pause);
                    mPlaying = bt;
                    bt.setBackgroundResource(R.drawable.play);
                }
            }

        }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        MyViewHolder vh = (MyViewHolder) holder;
        if(position == playing ){
            mPlaying = vh.mPlay;
            vh.mPlay.setBackgroundResource(R.drawable.play);
        }
        else
            vh.mPlay.setBackgroundResource(R.drawable.pause);

        vh.mTextView.setText(arr[position]);
       // final View view = this.playbar;



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
