package com.lionel.gonews.content;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.lionel.gonews.R;
import com.lionel.gonews.base.BasePopupWindow;

public class ContentFavoritePopupWindow extends BasePopupWindow {

    private final Context context;

    public ContentFavoritePopupWindow(Context context) {
        super(context);
        this.context = context;

        initDimBehind();
        initBackgroundStyle();
        initView();
    }

    private void initDimBehind() {
        super.setIsDimBehind(false);
    }

    private void initBackgroundStyle() {
        super.setBackgroundStyle(context.getDrawable(R.drawable.frame_round_gray));
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_popup_favorite, null, false);
        super.setView(view);
    }

    public void show(View anchor) {
        super.show(anchor, Gravity.END);
        setAutoDismiss();
    }

    private void setAutoDismiss() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 1500);
    }
}
