package com.lionel.gonews.base;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.lionel.gonews.R;

public class BasePopupWindow extends PopupWindow {

    public BasePopupWindow(Context context) {
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(context.getDrawable(R.drawable.frame_round_corner_white));
        setElevation(4f);
        setFocusable(true);
        setOutsideTouchable(true);
    }

    public void setView(View view) {
        setContentView(view);
    }

    public void show(View anchor) {
        getContentView().measure(makeDropDownMeasureSpec(getWidth()),
                makeDropDownMeasureSpec(getHeight()));

        int offsetX = -getContentView().getMeasuredWidth();
        showAsDropDown(anchor, offsetX, 20, Gravity.START);
    }

    private int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;

        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }
}
