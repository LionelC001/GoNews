package com.lionel.gonews.base;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.lionel.gonews.R;

public class BasePopupWindow extends PopupWindow {

    public BasePopupWindow(Context context) {
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(context.getDrawable(R.drawable.round_corner_white));
        setElevation(4f);
        setFocusable(true);
        setOutsideTouchable(true);
    }

    public void setView(View view){
        setContentView(view);
    }
}
