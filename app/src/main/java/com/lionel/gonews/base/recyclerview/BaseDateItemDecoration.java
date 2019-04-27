package com.lionel.gonews.base.recyclerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class BaseDateItemDecoration extends RecyclerView.ItemDecoration {

    private static final int PADDING_TOP = 100;

    public BaseDateItemDecoration() {

    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        outRect.top = PADDING_TOP;
    }


    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {


        final int childCount = parent.getChildCount();
        Log.d("<>", "childCount: " + childCount);
        Paint paint = new Paint();
//        paint.setColor(Color.GREEN);
        paint.setTextSize(100);
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            paint.setColor(Color.GREEN);
            int left = child.getLeft();
            int right = child.getRight();
            int down = child.getTop();
            int top = child.getTop() - params.topMargin;

//            c.drawText("THIS IS TEST", left, top, paint);
            c.drawRect(left, top, right, down, paint);


            paint.setColor(Color.BLUE);
             down = child.getTop() - params.topMargin;
            top = child.getTop() - params.topMargin -PADDING_TOP;
            c.drawRect(left, top, right, down, paint);

            paint.setColor(Color.RED);
            down = child.getTop() - params.topMargin -PADDING_TOP;
            top = child.getTop() - params.topMargin -PADDING_TOP - params.bottomMargin;
            c.drawRect(left, top, right, down, paint);

            Log.d("<>", "" + params.bottomMargin);
        }

    }
}
