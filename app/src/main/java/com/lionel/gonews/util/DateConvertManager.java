package com.lionel.gonews.util;

import java.util.Calendar;

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
}
