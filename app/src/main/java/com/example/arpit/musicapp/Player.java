package com.example.arpit.musicapp;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;


public class Player extends MediaPlayer implements  MediaPlayer.OnCompletionListener {

    static int PLAYING = 1;
    static int PAUSED = 0;
    static int STOPPED = -1;
    static public int playing_pos = -1;
    public int state;
    public View mBarView=null;
    public ImageButton mBarPlay= null;
    public ImageButton mRowPlay = null;
    public Player.MyViewHolder holder = null;
    MainActivity main;
    SongsManager sm;
    MyAdapter mAdapter;
    SeekBar mSeekBar;
    Thread t;
    SeekBarUpdater mSeekBarupdater;
    BarController mBarController;

    public Player(MainActivity main, SongsManager sm){
        this.sm = sm;
        this.main = main;
        this.state = STOPPED;
        this.mBarView = main.findViewById(R.id.bar_view);
        this.mBarPlay = main.findViewById(R.id.bar_pp);
        this.mSeekBar = main.findViewById(R.id.seek);
        if(this.state==STOPPED){
            mBarView.setVisibility(View.GONE);
        }
        this.setOnCompletionListener(this);

        mBarController = new BarController(this);

        mSeekBarupdater = new SeekBarUpdater(mSeekBar,this);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarupdater);
        t = new Thread(mSeekBarupdater);
        t.start();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView artist;
        public ImageView album;
        public ImageButton playbutton;

        public MyViewHolder(View v) {
            super(v);
            this.title = (TextView) v.findViewById(R.id.title);
            this.playbutton = (ImageButton) v.findViewById(R.id.play_pause);
            this.album = (ImageView) v.findViewById(R.id.album);
            this.artist = (TextView)v.findViewById((R.id.artist));
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        int prev = playing_pos;
        playing_pos = (playing_pos+1)%(SongsManager.size);
        setBar(prev, playing_pos);
        this.reset();
        play(playing_pos);
    }

    public void setBarTitle(int pos){

        TextView t = (TextView)main.findViewById(R.id.bar_title);
        t.setText(sm.mTracks.get(pos).title);
        t.setSelected(true);
        t.setEnabled(true);
        t.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        t.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e("Focus Change", "........");
                TextView tv = (TextView)v;
                if(hasFocus==false){
                    Log.e("Focus Change", "retrievd");
                    tv.setEnabled(true);
                }
            }
        });
        // t.setTextColor(main.getResources().getColor(R.color.colorPrimaryDark));
    }
    public void setBarAlbumArt(int pos){
        ImageView iv = (ImageView) main.findViewById(R.id.bar_album);
        Bitmap bm = sm.mTracks.get(pos).albumArt;
        if(bm==null)
            iv.setImageResource(R.drawable.album);
        else
            iv.setImageBitmap(bm);
    }
    public void setBarPlayButton(){    // when a song is touched

        if(playing_pos==-1)
        {
            ImageButton bt = main.findViewById(R.id.bar_pp);
            bt.setBackgroundResource(R.drawable.play_sq);

            bt = main.findViewById(R.id.prev);
            bt.setBackgroundResource(R.drawable.previous);

            bt = main.findViewById(R.id.next);
            bt.setBackgroundResource(R.drawable.next);
        }
        else{
            ImageButton bt = main.findViewById(R.id.bar_pp);
            bt.setBackgroundResource(R.drawable.play_sq);

        }
    }

    public void setBar(int prev, int curr){

        if(prev!=-1)
            mAdapter.notifyItemChanged(prev);
        mAdapter.notifyItemChanged(curr);
        setBarAlbumArt(curr);  //Bar
        setBarTitle(curr);
        setBarPlayButton();
        playing_pos = curr;
    }

    public void updatePlayer(Player.MyViewHolder vh, MyAdapter adapter) {

        int prev = playing_pos;
        int pos = vh.getAdapterPosition();

        if (playing_pos ==-1) {
            state = PLAYING;
            setBar(-1, pos);
            play(pos);
            main.findViewById(R.id.bar_view).setVisibility(View.VISIBLE);
        }
        else{
            if (pos == playing_pos) {   //Restart Case
                if (state == PLAYING) {
                    this.seekTo(0);
                }
                else{
                    state = PLAYING;
                    setBarPlayButton();
                    this.seekTo(0);
                    start();
                }
            }
            else{
                state = PLAYING;
                setBar(prev, pos);
                this.play(pos);
            }
        }
        playing_pos = pos;
    }

    public void updateOnBind(Player.MyViewHolder vh, int pos){

        Bitmap bm = sm.mTracks.get(pos).albumArt;
        Log.e("Bind on", pos+"");
        if(bm==null)
            vh.album.setImageResource(R.drawable.album);
        else
            vh.album.setImageBitmap(bm);
        vh.title.setText(sm.mTracks.get(pos).title);
        //vh.title.setSelected(true);
        vh.title.setEllipsize(TextUtils.TruncateAt.END);
        vh.artist.setText(sm.mTracks.get(pos).artist);


        if(pos== Player.playing_pos){
            vh.title.setTextColor(main.getResources().getColor(R.color.colorAccent));
            //setHolder(vh);
        }
        else{
            vh.title.setTextColor(main.getResources().getColor(R.color.colorWhite));
        }

        //vh.title.setText(vh.getAdapterPosition()+", playing "+tracks.state+""+holder);
    }

    public MyViewHolder createViewHolder(ViewGroup parent, int viewType, final MyAdapter adapter){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_row, parent, false);
        final Player.MyViewHolder vh = new Player.MyViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePlayer(vh, adapter);
            }
        });
        return vh;
    }
    void play(final int position)   // for new song
    {
        if(playing_pos!=-1){
            stop();
            reset();
        }
        this.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            this.setDataSource(main.getApplicationContext(), Uri.parse(sm.mTracks.get(position).path));
            this.prepareAsync();
            this.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Player p = (Player)mp;
                    p.mSeekBar.setMax(mp.getDuration());
                    mp.start();
                    state = PLAYING;
                }
            });
        } catch (IOException e) {
            Log.e("Error in Playing: ", "" + e);
        }
    }
    public void pause(){
        //Toast.makeText(main.getApplicationContext(), "Pause", Toast.LENGTH_SHORT).show();
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
