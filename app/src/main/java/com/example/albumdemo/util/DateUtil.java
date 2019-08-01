package com.example.albumdemo.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {

    private static final SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy年MM月dd日");

    private DateUtil() {
    }

    public static String formatYYYYMMDD(long seconds) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(seconds * 1000);

        Date date = calendar.getTime();

        return YYYY_MM_DD.format(date);
    }

}
