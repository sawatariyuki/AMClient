package com.gift.sawatariyuki.amclient.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneChanger {
    /***
     * @param timeStr local time (String) : "1970-01-01 08:00" GMT+8
     * @return utc time (String) : "1970-01-01 00:00" GMT+0
     */
    public static String StringLocalToStringUTC(String timeStr){
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

    /***
     * @param timeStr utc time (String)
     * @return locate time (Date)
     */
    public static Date StringUTCToDateLocal(String timeStr){
        Date time = new Date();
        try {
            time =  SimpleDateFormat.getDateTimeInstance().parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /***
     * @param timeDate locate time (Date)
     * @return locate time (String) "1970年01月01日 00:00:00"
     */
    public static String DateLocalTOStringLocalCN(Date timeDate){
        String time = SimpleDateFormat.getDateTimeInstance().format(timeDate);
        return time;
    }

    /***
     * @param timeDate locate time (Date)
     * @return locate time (String) "1970-01-01 00:00"
     */
    public static String DateLocalTOStringLocalEN(Date timeDate){
        String time = (String) android.text.format.DateFormat.format("yyyy-MM-dd HH:mm", timeDate);
        return time;
    }
}
