package com.example.arpit.musicapp;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class BarController implements View.OnClickListener {

    Player mp;
    ImageButton m_play;
    ImageButton m_next;
    ImageButton m_prev;
    TextView mBarTitle;

    public BarController(Player p){
        this.mp = p;
        m_play = mp.main.findViewById(R.id.bar_pp);
        m_prev = mp.main.findViewById(R.id.prev);
        m_next= mp.main.findViewById(R.id.next);
        mBarTitle = mp.main.findViewById(R.id.bar_title);

        m_play.setOnClickListener(this);
        m_prev.setOnClickListener(this);
        m_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        ImageButton bt = (ImageButton) v;
        if(v.getId()==m_play.getId()){
            if (mp.state != Player.PLAYING) {
                mBarTitle.setActivated(true);
                bt.setBackgroundResource(R.drawable.play_sq);
                mp.setstate(Player.PLAYING);
                mp.start();
            } else {
                mBarTitle.setActivated(false);
                bt.setBackgroundResource(R.drawable.pause_sq);
                mp.setstate(Player.PAUSED);
                mp.pause();
            }
        }
        else{

            int pos = 0;
            if (v.getId() == m_next.getId()) {
                pos = (Player.playing_pos + 1) % (SongsManager.size);
            }
            else if (v.getId() == m_prev.getId()) {
                pos =((Player.playing_pos - 1 + SongsManager.size) % (SongsManager.size));
            }
            m_play.setBackgroundResource(R.drawable.play_sq);
            mp.setBar(Player.playing_pos,  pos);
            mp.play(pos);
            mBarTitle.setActivated(true);
            mBarTitle.setEnabled(true);
        }
    }
}
