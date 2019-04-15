package com.lionel.gonews.util;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lionel.gonews.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.lionel.gonews.util.Constants.DATE_ISO8601;
import static com.lionel.gonews.util.Constants.DATE_YY_MM_DD;

public class NewsDataParsingHelper {

    @BindingAdapter("imageUrl")
    public static void setImageFromUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).diskCacheStrategy(DiskCacheStrategy.DATA).into(imageView);
    }

    // cut off the tail of titles after "-"
    @BindingAdapter("cleanTitle")
    public static void cutOffTitleTail(TextView view, String oldTitle) {
        String cleanTitle;
        int endDash = oldTitle.lastIndexOf("-");
        if (endDash != -1) {
            cleanTitle = oldTitle.substring(0, endDash);
        } else {
            cleanTitle = oldTitle;
        }
        view.setText(cleanTitle);
    }


    @BindingAdapter("passedTime")
    public static void setPassedTimeOrShortDate(TextView view, String oldTime) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_ISO8601);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date oldDate = sdf.parse(oldTime);
            sdf.setTimeZone(TimeZone.getDefault());  // locale timezone

            long oldDateInMs = sdf.parse(sdf.format(oldDate)).getTime();
            long currentDateInMs = sdf.parse(sdf.format(new Date())).getTime();

            long dayInMs = 24 * 60 * 60 * 1000;
            long passedTimeInMs = currentDateInMs - oldDateInMs;
            if (passedTimeInMs < dayInMs) {  // show passed time within 24hrs
                String passedTime = parsePassedTime(view.getContext(), passedTimeInMs);
                view.setText(passedTime);
            } else {  // otherwise, show "yyyy-MM-dd"
                setShortDate(view, oldTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static String parsePassedTime(Context context, long passedTimeInMs) {
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

    @BindingAdapter("shortDate")
    public static void setShortDate(TextView view, String oldTime) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_ISO8601);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date oldDate = sdf.parse(oldTime);
            sdf.setTimeZone(TimeZone.getDefault());
            sdf.applyPattern(DATE_YY_MM_DD);

            String shortDate = sdf.format(oldDate);
            view.setText(shortDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
