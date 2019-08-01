package com.example.albumdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.albumdemo.util.ImageLoader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    //private MediaLoader2 loader;
    private Button btnAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*final long start = System.curre
        ntTimeMillis();

        loader = new MediaLoader2(new MediaLoader2.MediaLoaderListener() {
            @Override
            public void onLoadComplete(LinkedHashMap<String, ArrayList<MediaFile>> mediaFilesMap) {
                long end = System.currentTimeMillis();
                Log.i(TAG, "Loading completed in " + (end - start) + "ms");
            }
        }, getApplication());

        loader.startLoading();*/

        btnAlbum = (Button) findViewById(R.id.btn_album);
        btnAlbum.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_album) {
            startActivity(new Intent(this, AlbumActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*if (loader != null) {
            loader.stopLoading();
        }*/

        ImageLoader.getInstance().clear();
    }
}
