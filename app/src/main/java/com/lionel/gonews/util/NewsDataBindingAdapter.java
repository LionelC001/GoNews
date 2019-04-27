package com.lionel.gonews.util;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v4.widget.CircularProgressDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.lionel.gonews.R;

import java.util.TimeZone;

import static com.lionel.gonews.util.Constants.DATE_ISO8601;
import static com.lionel.gonews.util.Constants.DATE_YY_MM_DD_HH_MM_SS;
import static com.lionel.gonews.util.DateConvertManager.turnToSpecificPatternAndTimeZone;
import static com.lionel.gonews.util.DateConvertManager.turnUTCToLocalYYMMDDOrPastTime;

public class NewsDataBindingAdapter {

    @BindingAdapter(value = {"srcFromUrl"})
    public static void setImageFromUrl(final ImageView view, final String srcFromUrl) {
        Context context = view.getContext();

        CircularProgressDrawable loadingDrawable = new CircularProgressDrawable(view.getContext());
        loadingDrawable.setColorSchemeColors(context.getResources().getColor(R.color.colorLightGray));
        loadingDrawable.setStrokeWidth(8f);
        loadingDrawable.setCenterRadius(45f);
        loadingDrawable.start();

        Glide.with(context)
                .load(srcFromUrl)
                .placeholder(loadingDrawable)
                .error(R.drawable.ic_error_cat_yellow_lean)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(view);
    }

    // cut off the tail of titles after "-"
    @BindingAdapter("titleWithoutTail")
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
        view.setText(turnUTCToLocalYYMMDDOrPastTime(view.getContext(), oldTime));
    }

    @BindingAdapter("dateAndTime")
    public static void setDateAndTime(TextView view, String oldTime) {
        String date = turnToSpecificPatternAndTimeZone(oldTime, DATE_ISO8601, TimeZone.getTimeZone("UTC"), DATE_YY_MM_DD_HH_MM_SS, TimeZone.getDefault());
        if (date != null) {
            view.setText(date);
        }
    }
}
