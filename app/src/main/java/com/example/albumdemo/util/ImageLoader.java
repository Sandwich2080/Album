package com.example.albumdemo.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.widget.ImageView;


import java.util.Collection;
import java.util.WeakHashMap;

public class ImageLoader {
    private static final ImageLoader ourInstance = new ImageLoader();

    private WeakHashMap<String, Bitmap> memoryCache = new WeakHashMap<>();

    private SparseArray<AsyncTask> imageLoadingTasks = new SparseArray<>();

    //private AsyncTask<Void, Void, Bitmap> loadTask;

    public static ImageLoader getInstance() {
        return ourInstance;
    }

    private ImageLoader() {
    }

    public void loadImage(final String filePath, final ImageView imageView) {
        if (memoryCache.containsKey(filePath)) {
            Bitmap bm = memoryCache.get(filePath);
            if (!bm.isRecycled()) {
                imageView.setImageBitmap(bm);
                return;
            }
        }

        AsyncTask<Void, Void, Bitmap> loadTask = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                return BitmapUtil.decode(filePath, imageView);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                memoryCache.put(filePath, bitmap);
                imageView.setImageBitmap(bitmap);
            }
        };

        imageLoadingTasks.put(loadTask.hashCode(), loadTask);
        loadTask.execute();

    }

    public void loadThumbnail(final long videoId, final String filePath, final ImageView imageView) {
        if (memoryCache.containsKey(filePath)) {
            Bitmap bm = memoryCache.get(filePath);
            if (!bm.isRecycled()) {
                imageView.setImageBitmap(bm);
                return;
            }
        }

        AsyncTask<Void, Void, Bitmap> loadTask = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                return BitmapUtil.getVideoThumbnail(imageView.getContext(), videoId);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                memoryCache.put(filePath, bitmap);
                imageView.setImageBitmap(bitmap);
            }
        };

        imageLoadingTasks.put(loadTask.hashCode(), loadTask);

        loadTask.execute();
    }

    public void clear() {
        cancelAllTasks();
        clearMemory();
    }

    private void clearMemory() {
        if (memoryCache == null || memoryCache.isEmpty()) {
            return;
        }
        Collection<Bitmap> bitmaps = memoryCache.values();
        for (Bitmap bm : bitmaps) {
            if (bm != null && !bm.isRecycled()) {
                bm.recycle();
            }
        }
        memoryCache.clear();
    }

    private void cancelAllTasks() {
        if (imageLoadingTasks == null || imageLoadingTasks.size() <= 0) {
            return;
        }

        int size = imageLoadingTasks.size();
        for (int i = 0; i < size; i++) {
            AsyncTask task = imageLoadingTasks.get(i);
            if (task != null) {
                task.cancel(true);
            }
        }
        imageLoadingTasks.clear();

    }

    /*void testLRU(){
        LruCache<String,Bitmap> lruCache = new LruCache<>(20);
        lruCache.put(null,null);
    }*/

}
