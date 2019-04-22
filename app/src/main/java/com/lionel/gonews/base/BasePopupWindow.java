package com.lionel.gonews.base;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.PopupWindow;

public class BasePopupWindow extends PopupWindow {

    public BasePopupWindow(View view) {
        setContentView(view);

        setWidth(ConstraintLayout.LayoutParams.WRAP_CONTENT);
        setHeight(ConstraintLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
       //TODO ANIMATION
    }
}
