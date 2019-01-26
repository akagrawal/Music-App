package com.example.arpit.musicapp;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SongsManager {

    public ArrayList<AudioModel>mTracks = new ArrayList<AudioModel>();
    MainActivity main = null;

    public SongsManager(MainActivity main){
        this.main = main;
        setup(main.getApplicationContext());
    }
    public void setup(Context context){
        Log.e("setup", "start");
        final String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID
        };

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Audio.Media.TITLE_KEY);

        Log.e("cursor ", cursor+"");
        if(cursor.moveToFirst())
        {
            //Log.e("cursor ", cursor+"");
            do{
                //Log.e("cursor ==============", cursor+"");
                AudioModel data = new AudioModel();
                data.setID(cursor.getLong(cursor.getColumnIndex(projection[0])));
                data.setArtist(cursor.getString(cursor.getColumnIndex(projection[1])));
                data.setTitle(cursor.getString(cursor.getColumnIndex(projection[2])));
                data.setAlbum(cursor.getString(cursor.getColumnIndex(projection[3])));
                data.setPath(cursor.getString(cursor.getColumnIndex(projection[4])));
                data.setDuration(cursor.getLong(cursor.getColumnIndex(projection[5])));
                data.setAlbumID(cursor.getLong(cursor.getColumnIndex(projection[6])));
                //data.print();
            try{
                final Uri AlbumArtUri = Uri.parse("content://media/external/audio/albumart");
                Uri album_uri = ContentUris.withAppendedId(AlbumArtUri, data.albumID);
                ParcelFileDescriptor pfd =cr.openFileDescriptor(album_uri, "r");

                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    data.setAlbumArt(BitmapFactory.decodeFileDescriptor(fd));
                }
            } catch (Exception e) {

                //Log.e("Error: ", e+"");
                data.setAlbumArt(null);

            }
            mTracks.add(data);
            }while(cursor.moveToNext());
        }
    }
}



