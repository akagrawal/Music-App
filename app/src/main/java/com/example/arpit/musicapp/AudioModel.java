package com.example.arpit.musicapp;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class AudioModel {

    long id;
    long albumID ;
    String title;
    String artist;
    String album;
    String path;
    Uri albumArtURI;
    Bitmap albumArt;
    long count = 0;
    long duration;
    public void print(){

        Log.e("albumData:", "\nID: +"+id+
                ", title-->"+title+
                ", Album: "+artist+
                ", Artist:"+album+
                ", Path:"+path);
    }

    public void setID(long id){this.id = id;}
    public void setTitle(String title){this.title= title;}
    public void setArtist(String artist){this.artist = artist;}
    public void setAlbum(String album){this.album = album;}
    public void setAlbumID(long albumID){this.albumID = albumID;}
    public void setPath(String path){this.path= path;}
    public void setDuration(long duraiton){this.duration = duration;}

    public void setAlbumArtURI(Uri albumArtURI){
        this.albumArtURI= albumArtURI;
    }

    public void setAlbumArt(Bitmap albumArt){
        this.albumArt = albumArt;
    }


    public void setCount(long count){this.count= count;}


}

