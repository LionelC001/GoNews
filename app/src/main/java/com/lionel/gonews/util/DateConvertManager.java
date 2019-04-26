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
     * ints to "yy-MM-dd"
     */
    public static String turnIntsToYYMMDD(int year, int month, int day) {
        return year + "-" + month + "-" + day;
    }

    /**
     * "yy-MM-dd" to int[]
     * 0=yy, 1=mm, 2=dd
     */
    public static int[] turnYYMMDDToIntArray(String yymmdd) {
        String[] dateStrArray = yymmdd.split("-");
        int[] dateIntArray = new int[3];
        dateIntArray[0] = Integer.parseInt(dateStrArray[0]);
        dateIntArray[1] = Integer.parseInt(dateStrArray[1]);
        dateIntArray[2] = Integer.parseInt(dateStrArray[2]);
        return dateIntArray;
    }

    /**
     * 0=yy, 1=mm, 2=dd
     */
    public static int[] getTodayIntArray() {
        int[] today = new int[3];
        Calendar calendar = Calendar.getInstance();
        today[0] = calendar.get(Calendar.YEAR);
        today[1] = calendar.get(Calendar.MONTH) + 1;
        today[2] = calendar.get(Calendar.DAY_OF_MONTH);
        return today;
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
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_YY_MM_DD);
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            Date date = sdf.parse(oldTime);
            sdf.applyPattern(DATE_ISO8601);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * turn  "yyyy-MM-dd'T'HH:mm:ss"(UTC) to Specific Pattern(local)
     */
    public static String turnUTCToLocalSpecificPattern(String oldTime, String datePattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_ISO8601);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date oldDate = sdf.parse(oldTime);
            sdf.setTimeZone(TimeZone.getDefault());
            sdf.applyPattern(datePattern);

            String formattedDate = sdf.format(oldDate);
            return formattedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * turn "yyyy-MM-dd'T'HH:mm:ss"(UTC) to ""yy-MM-dd" or "H ago" / "mm ago"(local)(within 1 day)
     */
    public static String turnUTCToLocalYYMMDDOrPastTime(Context context, String oldTime) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_ISO8601);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date oldDate = sdf.parse(oldTime);
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
}
