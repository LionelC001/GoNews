package com.lionel.gonews.util;

import android.content.Context;

import com.lionel.gonews.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.lionel.gonews.util.Constants.DATE_ISO8601;
import static com.lionel.gonews.util.Constants.DATE_YY_MM_DD;

public class DateConvertManager {

    /**
     * from calendar picker, ints to "yy-MM-dd"
     */
    public static String turnIntsToYYMMDD(int year, int month, int day) {
        return year + "-" + (month + 1) + "-" + day;
    }

    /**
     * "yy-MM-dd" to int[]
     * 0=yy, 1=mm, 2=dd
     */
    public static int[] turnYYMMDDToIntArray(String time) {
        Date date = parseStringToDate(time, DATE_YY_MM_DD, TimeZone.getDefault());
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
        String today = formatDateToString(new Date(), DATE_YY_MM_DD, TimeZone.getDefault());
        return turnYYMMDDToIntArray(today);
    }

    /**
     * pattern "yy-MM-dd"
     */
    public static String getTodayYYMMDD() {
        int[] today = getTodayIntArray();
        return today[0] + "-" + today[1] + "-" + today[2];
    }

    /**
     * when query from remote source, the date range should end at (dateFrom+1d)
     * ex. 2019-4-23(local) should be 2019-4-23 16:00:00 (UTC)
     */
    public static String turnLocalToUTCAndPlus1Day(String oldTime) {
        String newTimezone = turnLocalToUTC(oldTime);
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
     * turn "yyyy-MM-dd"(local) to "yyyy-MM-dd'T'HH:mm:ss"(UTC)
     */
    public static String turnLocalToUTC(String oldTime) {
        Date date = parseStringToDate(oldTime, DATE_YY_MM_DD, TimeZone.getDefault());
        return formatDateToString(date, DATE_ISO8601, TimeZone.getTimeZone("UTC"));
    }

    /**
     * turn  "yyyy-MM-dd'T'HH:mm:ss"(UTC) to Specific Pattern(local)
     */
    public static String turnUTCToLocalSpecificPattern(String oldTime, String pattern) {
        Date date = parseStringToDate(oldTime, DATE_ISO8601, TimeZone.getTimeZone("UTC"));
        return formatDateToString(date, pattern, TimeZone.getDefault());
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
                return turnUTCToLocalSpecificPattern(oldTime, DATE_YY_MM_DD);
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

    public static String getCurrentLocalYYMMDD() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return formatDateToString(date, DATE_YY_MM_DD, TimeZone.getDefault());
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
