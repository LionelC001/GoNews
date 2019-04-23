package com.lionel.gonews.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.lionel.gonews.util.Constants.DATE_ISO8601;
import static com.lionel.gonews.util.Constants.DATE_YY_MM_DD;

public class DateConvertManager {

    public static String intsToYYMMDD(int year, int month, int day) {
        return year + "/" + month + "/" + day;
    }

    /**
     * 0=yy, 1=mm, 2=dd
     */
    public static int[] yyMMDDToIntArray(String yymmdd) {
        String[] dateStrArray = yymmdd.split("/");
        int[] dateIntArray = new int[3];
        dateIntArray[0] = Integer.parseInt(dateStrArray[0]);
        dateIntArray[1] = Integer.parseInt(dateStrArray[1]);
        dateIntArray[2] = Integer.parseInt(dateStrArray[2]);
        return dateIntArray;
    }

    /**
     * 0=yy, 1=mm, 2=dd
     */
    public static int[] getTodayInArray() {
        int[] today = new int[3];
        Calendar calendar = Calendar.getInstance();
        today[0] = calendar.get(Calendar.YEAR);
        today[1] = calendar.get(Calendar.MONTH) + 1;
        today[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return today;
    }

    /**
     * pattern "yy/MM/dd"
     */
    public static String getTodayInStr() {
        int[] today = getTodayInArray();
        return today[0] + "/" + today[1] + "/" + today[2];
    }

    /**
     * the date range should end at (dateFrom+1d)
     * ex. 2019-4-23 should be 2019-4-24 00:00:00
     */
    public static String turnLocalToUTCAndPlus1Day(String oldTimezone) {
        String newTimezone = turnLocalToUTC(oldTimezone);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_ISO8601);
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(newTimezone));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date newDate = calendar.getTime();
            return sdf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * from "yyyy/MM/dd"(local) to "yyyy-MM-dd'T'HH:mm:ss"(UTC)
     */
    public static String turnLocalToUTC(String oldTimezone) {
        String slashDate = oldTimezone.replace("/", "-");

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_YY_MM_DD);
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            Date date = sdf.parse(slashDate);
            sdf.applyPattern(DATE_ISO8601);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
