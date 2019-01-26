package com.example.arpit.musicapp;

import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarUpdater implements Runnable {

    Player player = null;
    SeekBar mSeekBar;

    public SeekBarUpdater(Player p, SeekBar seekBar){
        this.player = p;
        this.mSeekBar = seekBar;
    }

    @Override
    public void run() {

        Log.e("SeekBarUpdate", "Update");

        while(true){
            if(this.mSeekBar.getMax()==100){
                continue;
            }
        try {

            if(this.player.dragging ==false) {
                this.mSeekBar.setProgress(player.getCurrentPosition());
                int sec = player.getCurrentPosition()/1000;
                sec%=60;
                if(sec<10)
                    ((TextView) this.player.main.findViewById(R.id.time)).setText(player.getCurrentPosition() / 60000 + ":0" + sec);
                else{
                    ((TextView) this.player.main.findViewById(R.id.time)).setText(player.getCurrentPosition() / 60000 + ":" + sec);
                }
            }
            //Log.e("SeekBarUpdate", "max:"+this.mSeekBar.getMax()+" " +this.mSeekBar.getProgress()+" Update to "+ player.getCurrentPosition());

            Thread.sleep(1000);
        }
        catch (Exception e ){
            Log.e("Exception", e+"");
            }
        }
    }
}
