package com.gift.sawatariyuki.amclient.Utils;

import android.icu.text.TimeZoneFormat;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneChanger {
    public static String StringLocalToStringUTC(String timeStr){
        Log.d("DEBUG", "BEFORE: "+timeStr);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        format.setTimeZone(TimeZone.getDefault());
        String time = null;
        try {
            Date timeDate = format.parse(timeStr);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            time = format.format(timeDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static Date StringUTCToDateLocal(String timeStr){
        Date time = new Date();
        try {
            time =  SimpleDateFormat.getDateTimeInstance().parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
