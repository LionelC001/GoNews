package com.lionel.gonews.util;

import android.content.Context;

import com.lionel.gonews.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.lionel.gonews.util.Constants.DATE_ISO8601;
import static com.lionel.gonews.util.Constants.DATE_YYYY_MM_DD;

public class DateConvertManager {

    /**
     * from calendar picker, ints to "yy-MM-dd"
     */
    public static String turnIntsToYYMMDD(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Date date = calendar.getTime();
        return formatDateToString(date, DATE_YYYY_MM_DD, TimeZone.getDefault());
    }

    /**
     * "yy-MM-dd" to int[]
     * 0=yy, 1=mm, 2=dd
     */
    public static int[] turnYYMMDDToIntArray(String time) {
        Date date = parseStringToDate(time, DATE_YYYY_MM_DD, TimeZone.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int[] dateIntArray = new int[3];
        dateIntArray[0] = calendar.get(Calendar.YEAR);
        dateIntArray[1] = calendar.get(Calendar.MONTH) + 1;
        dateIntArray[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return dateIntArray;
    }

    /**
     * 0=yy, 1=mm, 2=dd
     */
    public static int[] getTodayIntArray() {
        String today = formatDateToString(new Date(), DATE_YYYY_MM_DD, TimeZone.getDefault());
        return turnYYMMDDToIntArray(today);
    }

    public static String getCurrentDateSpecificPattern(String pattern) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return formatDateToString(date, pattern, TimeZone.getDefault());
    }

    public static String turnToSpecificPatternAndTimeZone(String time, String fromPattern, TimeZone fromTimeZone, String toPattern, TimeZone toTimeZone) {
        Date date = parseStringToDate(time, fromPattern, fromTimeZone);
        return formatDateToString(date, toPattern, toTimeZone);
    }

    public static String turnToSpecificPatternAndTimeZoneAndPlusDays(String time, String fromPattern, TimeZone fromTimeZone, String toPattern, TimeZone toTimeZone, int addDays) {
        String strDate = turnToSpecificPatternAndTimeZone(time, fromPattern, fromTimeZone, toPattern, toTimeZone);
        Date date = parseStringToDate(strDate, toPattern, toTimeZone);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, addDays);
        Date newDate = calendar.getTime();
        return formatDateToString(newDate, toPattern, toTimeZone);
    }

    /**
     * turn "yyyy-MM-dd'T'HH:mm:ss"(UTC) to ""yy-MM-dd" or "H ago" / "mm ago"(local)(within 1 day)
     */
    public static String turnUTCToLocalYYMMDDOrPastTime(Context context, String oldTime) {
        try {
            Date oldDate = parseStringToDate(oldTime, DATE_ISO8601, TimeZone.getTimeZone("UTC"));
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_ISO8601);
            sdf.setTimeZone(TimeZone.getDefault());  // locale timezone

            long oldDateInMs = sdf.parse(sdf.format(oldDate)).getTime();
            long currentDateInMs = sdf.parse(sdf.format(new Date())).getTime();
            long passedTimeInMs = currentDateInMs - oldDateInMs;

            long dayInMs = 24 * 60 * 60 * 1000;
            if (passedTimeInMs < dayInMs) {  // show passed time within 1 day
                return formatPassedTime(context, passedTimeInMs);
            } else {  // otherwise, show "yyyy-MM-dd"
                return turnToSpecificPatternAndTimeZone(oldTime, DATE_ISO8601, TimeZone.getTimeZone("UTC"), DATE_YYYY_MM_DD, TimeZone.getDefault());
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @return "H ago" / "mm ago" (local)
     */
    private static String formatPassedTime(Context context, long passedTimeInMs) {
        int hour = (int) (passedTimeInMs / 1000 / 60 / 60 % 24);
        int minute = (int) (passedTimeInMs / 1000 / 60 % 60);

        String passedTime;
        long hourInMs = 60 * 60 * 1000;
        if (passedTimeInMs > hourInMs) {
            passedTime = hour + context.getString(R.string.hour)
                    + " " + context.getString(R.string.ago);

            return passedTime;
        } else {
            passedTime = minute + context.getString(R.string.minute)
                    + " " + context.getString(R.string.ago);

            return passedTime;
        }
    }

    private static Date parseStringToDate(String time, String pattern, TimeZone timeZone) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern(pattern);
            sdf.setTimeZone(timeZone);
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String formatDateToString(Date date, String pattern, TimeZone timeZone) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat();
            sdf.applyPattern(pattern);
            sdf.setTimeZone(timeZone);
            return sdf.format(date);
        } else {
            return "";
        }
    }
}
