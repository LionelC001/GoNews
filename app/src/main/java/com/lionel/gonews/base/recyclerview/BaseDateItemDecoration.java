package com.lionel.gonews.base.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.lionel.gonews.R;
import com.lionel.gonews.data.News;
import com.lionel.gonews.util.DateConvertManager;

import java.util.List;
import java.util.TimeZone;

import static com.lionel.gonews.util.Constants.DATE_EEEE_MMMM_DD_YY;
import static com.lionel.gonews.util.Constants.DATE_ISO8601;
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
                drawGroupDateText(c, child, position);
            }
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        LinearLayoutManager manager = (LinearLayoutManager) parent.getLayoutManager();
        int firstPosition = manager.findFirstVisibleItemPosition();
        Log.d("<>", "firstPosition: " + firstPosition);
        View child = parent.findViewHolderForAdapterPosition(firstPosition).itemView;

//        Log.d("<>", "c")
//        drawGroupDateText(c, child, firstPosition);

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        int left = child.getLeft();
        int right = child.getRight();
        int bottom = paddingTop;
        int baseline = paddingTop - paddingTop / 4;

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        RectF rect = new RectF(left, 0, right, bottom + params.topMargin);
        c.drawRoundRect(rect,15,15,paint);


        paint.setColor(Color.BLACK);
        c.drawText(getDateText(firstPosition), left, baseline, paint);
        c.drawLine(left, bottom, right, bottom, paint);




    }


    private boolean isDrawingDateGroup(int position) {
        if (data == null) {
            return false;
        }

        if (position == 0) {
            return true;
        }

        TimeZone localTimeZone = TimeZone.getDefault();
        String dateCurrentItem = DateConvertManager.turnToSpecificPatternAndTimeZone(data.get(position).browseDate, DATE_ISO8601, localTimeZone, DATE_YYYY_MM_DD, localTimeZone);
        String dateLastItem = DateConvertManager.turnToSpecificPatternAndTimeZone(data.get(position - 1).browseDate, DATE_ISO8601, localTimeZone, DATE_YYYY_MM_DD, localTimeZone);
        return !dateCurrentItem.equals(dateLastItem);
    }

    private void drawGroupDateText(Canvas canvas, View child, int position) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        int left = child.getLeft();
        int right = child.getRight();
        int bottom = child.getTop() - params.topMargin;
        int baseline = child.getTop() - params.topMargin - paddingTop / 4;
        canvas.drawText(getDateText(position), left, baseline, paint);
        canvas.drawLine(left, bottom, right, bottom, paint);
    }

    private String getDateText(int position) {
        String time = data.get(position).browseDate;
        return DateConvertManager.turnToSpecificPatternAndTimeZone(time, DATE_YYYY_MM_DD, TimeZone.getDefault(), DATE_EEEE_MMMM_DD_YY, TimeZone.getDefault());
    }
}
