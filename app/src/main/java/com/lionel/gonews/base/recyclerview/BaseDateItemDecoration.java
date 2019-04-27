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
import com.lionel.gonews.util.DateConvertManager;

import java.util.List;
import java.util.TimeZone;

import static com.lionel.gonews.util.Constants.DATE_EEEE_MMMM_DD_YY;
import static com.lionel.gonews.util.Constants.DATE_YYYY_MM_DD;

public class BaseDateItemDecoration extends RecyclerView.ItemDecoration {

    private int paddingTop;
    private List<News> data;
    private Paint paint;

    public BaseDateItemDecoration(Context context, List<News> data) {
        this.data = data;

        initPaintAndParams(context);
    }

    private void initPaintAndParams(Context context) {
        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.colorDeepGray));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        float textSize = context.getResources().getDimension(R.dimen.x28sp);
        paint.setTextSize(textSize);
        paddingTop = (int) (1.6 * textSize);

        paint.setStrokeWidth(5);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        if (isDrawingDateGroup(position)) {
            outRect.set(0, paddingTop, 0, 0);
        } else {
            outRect.set(0, 0, 0, 0);
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final int current = parent.getChildCount();
        for (int i = 0; i < current; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildLayoutPosition(child);
            if (isDrawingDateGroup(position)) {
                drawGroupDate(c, child, position);
            }
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView
            parent, @NonNull RecyclerView.State state) {


    }


    private boolean isDrawingDateGroup(int position) {
        if (data != null && position == 0) {
            return true;
        }
        return data != null && !data.get(position).historyDate.equals(data.get(position - 1).historyDate);
    }

    private void drawGroupDate(Canvas canvas, View child, int position) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        int left = child.getLeft();
        int right = child.getRight();
        int bottom = child.getTop() - params.topMargin;
        int baseline = child.getTop() - params.topMargin - paddingTop / 4;
        canvas.drawText(getDateText(position), left, baseline, paint);
        canvas.drawLine(left, bottom, right, bottom, paint);
    }

    private String getDateText(int position) {
        String time = data.get(position).historyDate;
        return DateConvertManager.turnToSpecificPatternAndTimeZone(time, DATE_YYYY_MM_DD, TimeZone.getDefault(), DATE_EEEE_MMMM_DD_YY, TimeZone.getDefault());
    }
}
