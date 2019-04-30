package com.lionel.gonews.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class BasePopupWindow extends PopupWindow {

    private final Context context;
    private boolean isDimBehind = false;

    public BasePopupWindow(Context context) {
        this.context = context;

        initLayoutParams();
    }

    private void initLayoutParams() {
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setElevation(4f);
        setFocusable(true);
        setOutsideTouchable(true);
    }

    public void setBackgroundStyle(Drawable backgroundDrawable) {
        setBackgroundDrawable(backgroundDrawable);
    }

    public void setIsDimBehind(boolean isDimBehind) {
        this.isDimBehind = isDimBehind;
    }

    public void setView(View view) {
        setContentView(view);
    }

    public void show(View anchor, int gravity) {
        getContentView().measure(makeDropDownMeasureSpec(getWidth()), makeDropDownMeasureSpec(getHeight()));
        int offsetX = -getContentView().getMeasuredWidth();
        showAsDropDown(anchor, offsetX, 20, gravity);

        if (isDimBehind) {
            dimBehind();
        }
    }

    // can't get measure spec unless do this process,
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
