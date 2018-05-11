package com.gift.sawatariyuki.amclient.Utils;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GenerateDynamicCode {
    /***
     * use ActivateCode + utctime(format"yyyyMMddHHmm") to generate a dynamic code
     * @param code  user's ActivateCode
     * @return dynamic code
     */
    public static String Generate(String code){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");

        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String timeStr = format.format(date);

        return MD5Encrypt.getMD5(code+timeStr);
    }

}
