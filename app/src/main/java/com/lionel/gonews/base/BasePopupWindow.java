package com.lionel.gonews.base;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.lionel.gonews.R;

public class BasePopupWindow extends PopupWindow {

    private final Context context;

    public BasePopupWindow(Context context) {
        this.context = context;

        initLayoutParams();
    }

    private void initLayoutParams() {
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
        getContentView().measure(makeDropDownMeasureSpec(getWidth()), makeDropDownMeasureSpec(getHeight()));
        int offsetX = -getContentView().getMeasuredWidth();
        showAsDropDown(anchor, offsetX, 20, Gravity.START);

        dimBehind();
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

    private void dimBehind() {
        View container = getContentView().getRootView();
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.5f;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.updateViewLayout(container, p);
    }
}
