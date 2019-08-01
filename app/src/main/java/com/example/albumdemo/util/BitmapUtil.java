package com.example.albumdemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

public class BitmapUtil {
    private static final String TAG = BitmapUtil.class.getSimpleName();

    private BitmapUtil() {
    }

    public static Bitmap decode(String filePath, ImageView imageView) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);

        int orgHeight = opts.outHeight;
        int orgWidth = opts.outWidth;

        double hRatio = (double) imageView.getMeasuredHeight() / (double) orgHeight;
        double wRatio = (double) imageView.getMeasuredWidth() / (double) orgWidth;

        Log.d(TAG, filePath+",hRatio:" + hRatio + ",wRatio:" + wRatio);

        double ratio = Math.min(hRatio, wRatio);

        /*opts.outHeight = (int)(ratio * orgHeight);
        opts.outWidth = (int)(ratio*orgWidth);*/

        int inSampleSize = (int) (1 / ratio) < 1 ? 1 : (int) (1 / ratio);

        Log.d(TAG,"inSampleSize : "+inSampleSize);

        opts.inSampleSize = inSampleSize;
        opts.inJustDecodeBounds = false;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;

        return BitmapFactory.decodeFile(filePath, opts);

    }

    public static Bitmap getVideoThumbnail(Context c, long id) {
        return MediaStore.Video.Thumbnails.getThumbnail(c.getContentResolver(), id, MediaStore.Video.Thumbnails.MICRO_KIND, null);
    }
}
