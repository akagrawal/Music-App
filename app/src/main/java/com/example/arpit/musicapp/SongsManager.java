package com.example.arpit.musicapp;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class SongsManager {

    public ArrayList<AudioModel> mTracks= new ArrayList<AudioModel>();
    public ArrayList<HashMap<String, String>> mData = new ArrayList<>();
    public String current_song = "";
    private final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");

    static String external_add = Environment.getExternalStorageDirectory().getAbsolutePath();
    //static String internal_addr  = Environment.getRootDirectory().getAbsolutePath();
    //static String internal_add= getfil;
    static int size = 0;

    public SongsManager(MainActivity main){

        setAlbums(main);
        Log.e("====> Total : ", size+" tracks Added==========");
    }

    public void setAlbums(MainActivity context) {
        Log.e("setAlbums:", "start");
        //String where = null;

        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.e("Uri: ", uri+"");

        final String _id = MediaStore.Audio.Albums._ID;
        final String artist = MediaStore.Audio.Media.ARTIST;
        final String albumID = MediaStore.Audio.Media.ALBUM_ID;
        final String album = MediaStore.Audio.Media.ALBUM;
        final String path = MediaStore.Audio.Media.DATA;
        final String title = MediaStore.Audio.Media.TITLE;
        final String order = MediaStore.Audio.Media.TITLE_KEY;



        final String[] columns = {_id, title, artist, albumID, album, path};
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor = context.getContentResolver().query(uri, columns, selection,
                null, order);

        // add playlist to list
        if (cursor!=null && cursor.moveToFirst()) {
            do {
                AudioModel tracks = new AudioModel();
                tracks.setID(cursor.getLong(cursor.getColumnIndex(_id)));
                tracks.setTitle(cursor.getString(cursor.getColumnIndex(title)));
                tracks.setArtist(cursor.getString(cursor.getColumnIndex(artist)));
                tracks.setAlbum(cursor.getString(cursor.getColumnIndex(album)));
                tracks.setAlbumID(cursor.getLong(cursor.getColumnIndex(albumID)));
                tracks.setPath(cursor.getString(cursor.getColumnIndex(path)));
                tracks.setAlbumArtURI(ContentUris.withAppendedId(albumArtUri, tracks.albumID));
                try{
                    InputStream in = context.getContentResolver().openInputStream(tracks.albumArtURI);
                    Bitmap artwork = BitmapFactory.decodeStream(in);
                    tracks.setAlbumArt(artwork);
                }
                catch(FileNotFoundException e){
                    Log.e("Exception: ", e+"");
                }


                mTracks.add(tracks);
                this.size++;
                if(this.size==50)
                    break;
                //tracks.print();
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void setup(String rootPath){
        try {
            Log.e("path: ", rootPath+"");
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            for (File file : files) {
                if (file.isDirectory()) {
                    setup(file.getAbsolutePath());
                }
                else if (file.getName().endsWith(".mp3")) {
                    Log.e("file ", "found");
                    HashMap<String, String> song = new HashMap<>();
                    song.put("file_path", file.getAbsolutePath());
                    song.put("file_name", file.getName());
                    mData.add(song);
                    //temp.add(file.getName());
                    Log.e("filename", mData.get(size).get("file_name"));
                    this.size++;
                }
            }
        } catch (Exception e) {
            Log.e("Error in Setup: ", e+"");

        }
    }
}

