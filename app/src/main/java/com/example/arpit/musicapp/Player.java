package com.example.arpit.musicapp;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.logging.Handler;


public class Player extends MediaPlayer implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    static int PLAYING = 1;
    static int PAUSED = 0;
    static int STOPPED = -1;
    public boolean dragging = false;
    static public int playing_pos = -1;
    public int state;
    public View mBarView=null;
    public ImageButton mBarPlay= null;
    public SeekBar mSeekbar=null;
    public ImageButton mRowPlay = null;
    public Player.MyViewHolder holder = null;
    MainActivity main;
    SongsManager sm;
    SeekBarUpdater mSeekBarUpdater = null;
    Thread t;



    public Player(MainActivity main, SongsManager sm){
        this.sm = sm;
        this.main = main;
        this.state = STOPPED;
        this.mBarView = main.findViewById(R.id.bar_view);
        this.mBarPlay = main.findViewById(R.id.bar_pp);
        this.mSeekbar = main.findViewById(R.id.seekBar);

        mSeekbar.setOnSeekBarChangeListener(this);
        mBarPlay.setOnClickListener(this);
        mSeekBarUpdater = new SeekBarUpdater(this, mSeekbar);
        t = new Thread(mSeekBarUpdater);
        if(this.state==STOPPED){
            mSeekbar.setVisibility(View.GONE);
            mBarView.setVisibility(View.GONE);
        }

        //mSeekbar.setOnSeekBarChangeListener(this);

    }
    public Player(View barView, ImageButton barplay){
        this.state = STOPPED;
        this.mBarView = barView;
        this.mBarPlay = barplay;
        if(this.state==STOPPED)
            mBarView.setVisibility(View.GONE);
        else
            mBarView.setVisibility(View.VISIBLE);
        mBarPlay.setOnClickListener(this);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.e("onprogress", "start"+ fromUser);
        //Toast.makeText(main.getApplicationContext(),"Progess Changed to "+progress, Toast.LENGTH_SHORT).show();
        //mSeekbar.setProgress(getCurrentPosition());
        if(fromUser) {
            int sec = progress/1000;
            sec%=60;
            if(sec<10)
                ((TextView) main.findViewById(R.id.time)).setText(progress / 60000 + ":0" + sec);
            else{
                ((TextView) main.findViewById(R.id.time)).setText(progress / 60000 + ":" + sec);
            }
            seekBar.setProgress(progress);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.e("OnStartTracking", "start");
       // Toast.makeText(main.getApplicationContext(),"start tracking", Toast.LENGTH_SHORT).show();
        dragging = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.e("onstoptracking", "stop");
        dragging = false;
        this.seekTo((seekBar.getProgress()));
        //Toast.makeText(main.getApplicationContext(),"stop tracking", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bar_pp) {
            ImageButton bt = (ImageButton) v;
            if (this.state != Player.PLAYING) {
                bt.setBackgroundResource(R.drawable.play);
                this.setstate(Player.PLAYING);
                this.start();
                //this.play(playing_pos);
            } else {
                bt.setBackgroundResource(R.drawable.pause);
                //  holder.playbutton.setBackgroundResource(R.drawable.pause);
                this.setstate(Player.PAUSED);
                this.pause();
                //this.stop();
            }
        } else if (v.getId()==R.id.seekBar){

        }
    }
    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView album;
        public ImageButton playbutton;

        public MyViewHolder(View v) {
            super(v);
            this.title = (TextView) v.findViewById(R.id.title);
            this.playbutton = (ImageButton) v.findViewById(R.id.play_pause);
            this.album = (ImageView) v.findViewById(R.id.album);
        }
    }


    public void setBarTitle(int pos){

        TextView t = (TextView)main.findViewById(R.id.bar_title);
        t.setText(sm.mTracks.get(pos).title);
        t.setTextColor(main.getResources().getColor(R.color.colorPrimaryDark));
    }
    public void setBarAlbumArt(int pos){
        ImageView iv = (ImageView) main.findViewById(R.id.bar_album);
        Bitmap bm = sm.mTracks.get(pos).albumArt;
        if(bm==null)
            iv.setBackgroundResource(R.drawable.album);
        else
            iv.setImageBitmap(bm);
    }
    public void setBarPlayButton(int state){
        ImageButton bt = main.findViewById(R.id.bar_pp);
        if(state==PLAYING){
            bt.setBackgroundResource(R.drawable.play);
        }
        else{
            bt.setBackgroundResource(R.drawable.pause);
        }
    }
    public  void resetHolder(){
        if(holder!=null){
            holder.title.setTextColor(main.getResources().getColor(R.color.colorBlack));
            if(holder.isRecyclable()==false)
                holder.setIsRecyclable(true);
        }

    }
    public  void setHolder(MyViewHolder vh){

        holder = vh;
        holder.setIsRecyclable(false);
        holder.title.setTextColor(main.getResources().getColor(R.color.colorAccent));
    }

    public void updatePlayer(Player.MyViewHolder vh) {

        int pos = vh.getAdapterPosition();

        if (holder == null) {
            Toast.makeText(main.getApplicationContext(), "FIrst time call...", Toast.LENGTH_SHORT).show();
            state = PLAYING;
            setHolder(vh);         //Row

            setBarAlbumArt(pos);  //Bar
            setBarTitle(pos);
            setBarPlayButton(state);

            play(pos);        //play
            playing_pos = pos;

            main.findViewById(R.id.bar_view).setVisibility(View.VISIBLE);
            main.findViewById(R.id.seekBar).setVisibility(View.VISIBLE);
            //return;
        }
        else{
            if (pos == playing_pos) {   //Restart Case
                boolean test = true;
                if (state == PLAYING) {
                    Toast.makeText(main.getApplicationContext(), "---> 1 "+"Restart", Toast.LENGTH_SHORT).show();
                    this.seekTo(0);
                }
                else{
                    state = PLAYING;
                    Toast.makeText(main.getApplicationContext(), "---> 2 "+"Restart", Toast.LENGTH_SHORT).show();
                    setBarPlayButton(state);
                    this.seekTo(0);
                    start();
                }
            }
            else{
                Toast.makeText(main.getApplicationContext(), "---> 3 different track", Toast.LENGTH_SHORT).show();
                state = PLAYING;
                resetHolder(); //Row
                setHolder(vh);

                setBarAlbumArt(pos);  //Bar
                setBarTitle(pos);
                setBarPlayButton(state);

                this.reset();
                this.play(pos);
            }
            playing_pos = pos;
        }
    }

    public void updateOnBind(Player.MyViewHolder vh, int pos){

        Bitmap bm = sm.mTracks.get(pos).albumArt;
        if(bm==null){
            Log.e("Null at", pos+"");
            vh.album.setBackgroundResource(R.drawable.album);
        }
        else
            vh.album.setImageBitmap(bm);
        vh.title.setText(sm.mTracks.get(pos).title);

        if(pos== Player.playing_pos){
            resetHolder();
            setHolder(vh);
        }

        //vh.title.setText(vh.getAdapterPosition()+", playing "+tracks.state+""+holder);
    }

    public MyViewHolder createViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_row, parent, false);
        final Player.MyViewHolder vh = new Player.MyViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePlayer(vh);
            }
        });
        return vh;
    }
    void play(int position)
    {
        if(isPlaying()){
            this.reset();
            //this.stop();
        }
        this.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if(playing_pos==-1)
            t.start();
        try {
            this.setDataSource(main.getApplicationContext(), Uri.parse(sm.mTracks.get(position).path));
            this.prepareAsync();
            this.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mSeekbar.setMax(getDuration());
                    if (isPlaying()) {
                        mp.pause();
                        mp.seekTo(0);
                        mp.start();
                    }
                    else{
                        mp.start();
                    }
                }
            });
        } catch (IOException e) {
            Log.e("Error in Playing: ", "" + e);
        }
    }
    public void pause(){
        Toast.makeText(main.getApplicationContext(), "Pause", Toast.LENGTH_SHORT).show();
        if(isPlaying()==true)
            super.pause();
    }



    public int getState(){
        return this.state;
    }

    public void setstate(int state){
        this.state = state;
    }




}