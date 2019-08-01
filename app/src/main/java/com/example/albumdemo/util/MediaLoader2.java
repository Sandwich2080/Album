package com.example.albumdemo.util;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Using #{@link androidx.loader.content.CursorLoader} to load media files.
 */
public class MediaLoader2 {

    private static final String TAG = MediaLoader2.class.getSimpleName();

    private List<MediaFile> imageList;
    private List<MediaFile> videoList;
    private AsyncTask<Void, Void, LinkedHashMap<String, ArrayList<MediaFile>>> resortTask;

    public interface MediaLoaderListener {
        void onLoadComplete(LinkedHashMap<String, ArrayList<MediaFile>> mediaFilesMap);
    }

    private CursorLoader imageLoader;
    private CursorLoader videoLoader;

    private boolean isImagesLoadFinished = false;

    private boolean isVideosLoadFinished = false;

    private MediaLoaderListener loaderListener;

    private Context ctx;

    public MediaLoader2(MediaLoaderListener loaderListener, Context ctx) {
        this.loaderListener = loaderListener;
        this.ctx = ctx;
    }

    public void registerLoaderListener(MediaLoaderListener listener) {
        this.loaderListener = listener;
    }

    public void startLoading() {
        if (ctx == null) {
            return;
        }
        readImages(ctx);
        readVideos(ctx);
    }

    public void stopLoading() {
        isImagesLoadFinished = false;
        isVideosLoadFinished = false;
        if (imageLoader != null) {
            imageLoader.stopLoading();
        }
        if (videoLoader != null) {
            videoLoader.stopLoading();
        }
        if (resortTask != null) {
            resortTask.cancel(true);
        }

    }

    private void readImages(Context c) {
        imageLoader = new CursorLoader(c,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc"
        );

        imageLoader.registerListener(imageLoader.getId(), new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(@NonNull Loader<Cursor> loader, @Nullable Cursor cursor) {
                if (cursor != null && cursor.moveToFirst()) {

                    imageList = new ArrayList<>();

                    do {
                        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                        long dateModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED));

                        Log.i(TAG, "id:" + id + ",path:" + path + ",dateModified:" + dateModified);

                        MediaFile imageFile = new MediaFile();
                        imageFile.setId(id);
                        imageFile.setType(MediaFile.TYPE_IMAGE);
                        imageFile.setPath(path);
                        imageFile.setDateModified(dateModified);
                        imageList.add(imageFile);

                    } while (cursor.moveToNext());

                    cursor.close();

                    isImagesLoadFinished = true;

                    if (isImagesLoadFinished && isVideosLoadFinished) {
                        resortMediaData();
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }

            }
        });

        imageLoader.registerOnLoadCanceledListener(new Loader.OnLoadCanceledListener<Cursor>() {
            @Override
            public void onLoadCanceled(@NonNull Loader<Cursor> loader) {

            }
        });

        imageLoader.startLoading();


    }

    private void readVideos(Context c) {
        videoLoader = new CursorLoader(c,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Video.Media.MIME_TYPE + "=? ",
                new String[]{"video/mp4"},
                MediaStore.Video.Media.DATE_MODIFIED + " desc");
        videoLoader.registerListener(videoLoader.getId(), new Loader.OnLoadCompleteListener<Cursor>() {
            @Override
            public void onLoadComplete(@NonNull Loader<Cursor> loader, @Nullable Cursor cursor) {

                if (cursor != null && cursor.moveToFirst()) {

                    videoList = new ArrayList<>();

                    do {

                        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                        long dateModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_MODIFIED));
                        //String thumbnailPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.MINI_THUMB_MAGIC));

                        Log.i(TAG, "id:" + id + ",path:" + path + ",dateModified:" + dateModified);

                        MediaFile videoFile = new MediaFile();
                        videoFile.setId(id);
                        videoFile.setType(MediaFile.TYPE_VIDEO);
                        videoFile.setPath(path);
                        videoFile.setDateModified(dateModified);
                        videoList.add(videoFile);

                    } while (cursor.moveToNext());

                    cursor.close();

                    isVideosLoadFinished = true;

                    if (isImagesLoadFinished && isVideosLoadFinished) {
                        resortMediaData();
                    }

                }
                if (cursor != null) {
                    cursor.close();
                }

            }
        });
        videoLoader.registerOnLoadCanceledListener(new Loader.OnLoadCanceledListener<Cursor>() {
            @Override
            public void onLoadCanceled(@NonNull Loader<Cursor> loader) {
                // TODO:
            }
        });

        videoLoader.startLoading();


    }


    /**
     *
     */
    private void resortMediaData() {
        resortTask = new AsyncTask<Void, Void, LinkedHashMap<String, ArrayList<MediaFile>>>() {
            @Override
            protected LinkedHashMap<String, ArrayList<MediaFile>> doInBackground(Void... voids) {
                return resortFinalData();
            }

            @Override
            protected void onPostExecute(LinkedHashMap<String, ArrayList<MediaFile>> finalResult) {
                super.onPostExecute(finalResult);
                if (loaderListener != null) {
                    loaderListener.onLoadComplete(finalResult);
                }
            }
        };
        resortTask.execute();
    }

    private LinkedHashMap<String, ArrayList<MediaFile>> resortFinalData() {
        LinkedHashMap<String, ArrayList<MediaFile>> tmpResult = new LinkedHashMap<>();

        if (imageList != null && !imageList.isEmpty()) {

            for (MediaFile mf : imageList) {

                String date = DateUtil.formatYYYYMMDD(mf.getDateModified());
                if (tmpResult.containsKey(date)) {
                    tmpResult.get(date).add(mf);
                } else {
                    ArrayList<MediaFile> fileList = new ArrayList<>();
                    fileList.add(mf);
                    tmpResult.put(date, fileList);
                }


            }

        }

        if (videoList != null && !videoList.isEmpty()) {

            for (MediaFile mf : videoList) {
                String date = DateUtil.formatYYYYMMDD(mf.getDateModified());
                if (tmpResult.containsKey(date)) {
                    tmpResult.get(date).add(mf);
                } else {
                    ArrayList<MediaFile> fileList = new ArrayList<>();
                    fileList.add(mf);
                    tmpResult.put(date, fileList);
                }
            }

        }

        // resort all data by date.
        String[] dateList = tmpResult.keySet().toArray(new String[0]);
        Arrays.sort(dateList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });

        LinkedHashMap<String, ArrayList<MediaFile>> finalResult = new LinkedHashMap<>();
        for (String dateKey :
                dateList) {

            ArrayList<MediaFile> tmpList = tmpResult.get(dateKey);
            Collections.sort(tmpList, new Comparator<MediaFile>() {
                @Override
                public int compare(MediaFile o1, MediaFile o2) {
                    return (int) (-o1.getDateModified() + o2.getDateModified());
                }
            });
            finalResult.put(dateKey, tmpList);
        }

        tmpResult.clear();
        return finalResult;
    }


}
