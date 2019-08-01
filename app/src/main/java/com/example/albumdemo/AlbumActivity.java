package com.example.albumdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albumdemo.util.MediaFile;
import com.example.albumdemo.util.MediaLoader2;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class AlbumActivity extends AppCompatActivity {

    private static final String TAG = AlbumActivity.class.getSimpleName();

    private RecyclerView rvAlbum;

    private AlbumAdapter adapter;
    private MediaLoader2 loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        rvAlbum = (RecyclerView) findViewById(R.id.rv_album);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this){

            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {

                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                //return super.generateDefaultLayoutParams();
            }
        };
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvAlbum.setLayoutManager(layoutManager);

        adapter = new AlbumAdapter();

        rvAlbum.setAdapter(adapter);


        startLoading();


    }

    private void startLoading() {
        final long start = System.currentTimeMillis();

        loader = new MediaLoader2(new MediaLoader2.MediaLoaderListener() {
            @Override
            public void onLoadComplete(LinkedHashMap<String, ArrayList<MediaFile>> mediaFilesMap) {
                long end = System.currentTimeMillis();
                Log.i(TAG, "Loading completed in " + (end - start) + "ms");
                adapter.setMediaFilesMap(mediaFilesMap);
            }
        }, getApplication());

        loader.startLoading();
    }


    private void stopLoading() {
        if (loader != null) {
            loader.stopLoading();
        }
        //ImageLoader.getInstance().clear();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLoading();
    }
}
