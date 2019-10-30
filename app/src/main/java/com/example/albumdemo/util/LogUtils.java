package com.example.albumdemo.util;

import android.os.Environment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class LogUtils {
    private LogUtils() {
    }

    public static void log2file(String log) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/macao.log", true);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());

            String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(cal.getTime());

            fos.write((timeStr + " " + log + "\n").getBytes("utf-8"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (fos != null) {
                try {

                    fos.close();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        }

    }

}
