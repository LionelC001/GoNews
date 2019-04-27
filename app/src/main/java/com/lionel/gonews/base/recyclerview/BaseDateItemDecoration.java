package com.lionel.gonews.base.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lionel.gonews.R;
import com.lionel.gonews.data.News;

import java.util.List;

public class BaseDateItemDecoration extends RecyclerView.ItemDecoration {

    private int paddingTop;
    private List<News> data;
    private Paint paint;

    public BaseDateItemDecoration(Context context, List<News> data, int txtSizeRes) {
        this.data = data;

        initPaintAndParams(context, txtSizeRes);
    }

    private void initPaintAndParams(Context context, int txtSizeRes) {
        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorDeepGray));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        float textSize = context.getResources().getDimension(txtSizeRes);
        paint.setTextSize(textSize);
        paddingTop = (int) (1.2 * textSize);

        paint.setStrokeWidth(5);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {


        outRect.top = paddingTop;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int left = child.getLeft();
            int right = child.getRight();
            int bottom = child.getTop() - params.topMargin;
            int baseline = child.getTop() - params.topMargin - paddingTop / 4;
            c.drawText("THIS IS TEST", left, baseline, paint);
            c.drawLine(left, bottom, right, bottom, paint);

        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView
            parent, @NonNull RecyclerView.State state) {


    }


    private boolean isDrawingDateGroup(int index) {
        if (data.get(index).historyDate.equals(data.get(index - 1).historyDate)) {
            return true;
        }
        return false;
    }
}
