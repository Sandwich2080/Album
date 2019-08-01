package com.example.albumdemo.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Load media files from phone storage.
 */
public class MediaLoader {


    /**
     * CursorLoader also can be used #{@link MediaLoader2}.
     *
     * @param c
     * @return
     */
    public List<MediaFile> readImages(Context c) {

        if (c == null) {
            throw new NullPointerException("c == null");
        }

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return null;
        }

        ContentResolver cr = c.getContentResolver();
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = cr.query(imageUri,
                null,
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (cursor != null && cursor.moveToFirst()) {

            List<MediaFile> imageList = new ArrayList<>();

            do {

                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                long dateModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED));

                MediaFile imageFile = new MediaFile();
                imageFile.setId(id);
                imageFile.setType(MediaFile.TYPE_IMAGE);
                imageFile.setPath(path);
                imageFile.setDateModified(dateModified);
                imageList.add(imageFile);

            } while (cursor.moveToNext());

            cursor.close();

            return imageList;

        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public List<MediaFile> readVideos(Context c) {
        if (c == null) {
            throw new NullPointerException("c == null");
        }

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return null;
        }

        ContentResolver cr = c.getContentResolver();
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = cr.query(videoUri,
                null,
                MediaStore.Video.Media.MIME_TYPE + "=? ",
                new String[]{"video/mp4"},
                MediaStore.Video.Media.DATE_MODIFIED + " desc");
        if (cursor != null && cursor.moveToFirst()) {

            List<MediaFile> videoList = new ArrayList<>();

            do {

                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                long dateModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_MODIFIED));
                //String thumbnailPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.MINI_THUMB_MAGIC));

                MediaFile videoFile = new MediaFile();
                videoFile.setId(id);
                videoFile.setType(MediaFile.TYPE_VIDEO);
                videoFile.setPath(path);
                videoFile.setDateModified(dateModified);
                videoList.add(videoFile);

            } while (cursor.moveToNext());

            cursor.close();

            return videoList;

        }
        if (cursor != null) {
            cursor.close();
        }


        return null;
    }

    public Bitmap getVideoThumbnail(Context c, int id) {
        return MediaStore.Video.Thumbnails.getThumbnail(c.getContentResolver(), id, MediaStore.Video.Thumbnails.MICRO_KIND, null);
    }


}
