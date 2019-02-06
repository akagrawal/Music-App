package com.example.arpit.musicapp;
import android.util.Log;
import android.widget.SeekBar;

public class SeekBarUpdater implements Runnable, SeekBar.OnSeekBarChangeListener {
    public Player mp;
    SeekBar mSeekBar;
    boolean drag = false;
    public SeekBarUpdater(SeekBar seekbar, Player p){
        this.mSeekBar = seekbar;
        this.mp = p;
    }
    @Override
    public void run() {
        while(true){
            if(drag==false && mp.isPlaying()){
                int t = mp.getCurrentPosition();
                mSeekBar.setProgress(t);
            }
            try
            {
                Thread.sleep(1000);
            }
            catch(Exception e) {
                Log.e("Exception: ", e+"");
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.e("Drag: ", "start");
        drag = true;

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //seekBar.setProgress(progress);
        if(fromUser)
            mp.seekTo(progress);
        Log.e("Progress : ", "------->");

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.e("Drag: ", "Stop");
        mp.seekTo(seekBar.getProgress());
        drag = false;
    }
}
