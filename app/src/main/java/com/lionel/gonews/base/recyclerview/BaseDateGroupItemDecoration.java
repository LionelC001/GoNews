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
import android.view.View;

import com.lionel.gonews.R;
import com.lionel.gonews.data.News;
import com.lionel.gonews.util.DateConvertManager;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static com.lionel.gonews.util.Constants.DATE_EEEE_MMMM_DD_YY;
import static com.lionel.gonews.util.Constants.DATE_ISO8601;
import static com.lionel.gonews.util.Constants.DATE_YYYY_MM_DD;

public class BaseDateGroupItemDecoration extends RecyclerView.ItemDecoration {

    private final Context context;
    private List<News> data;
    private List<Integer> listLastItemInGroupsPosition;
    private List<Integer> listHeaderPosition;
    private Paint paint;
    private int headerHeight;
    private float headerTextSize;
    private int headerPadding;
    private int headerCornerRadius;
    private int headerTextMarginBottom;

    public BaseDateGroupItemDecoration(Context context, List<News> data) {
        this.context = context;
        this.data = data;

        initPosition();
        initParams();
        initPaint();
    }

    private void initPosition() {
        listLastItemInGroupsPosition = new ArrayList<>();
        listHeaderPosition = new ArrayList<>();

        if (data != null && data.size() > 0) {
            // get the item positions in the list, and they are the last one in their group
            TimeZone localTimeZone = TimeZone.getDefault();
            for (int i = 0; i < data.size() - 1; i++) {
                String dateCurrent = DateConvertManager.turnToSpecificPatternAndTimeZone(data.get(i).browseDate, DATE_ISO8601, localTimeZone, DATE_YYYY_MM_DD, localTimeZone);
                String dateNext = DateConvertManager.turnToSpecificPatternAndTimeZone(data.get(i + 1).browseDate, DATE_ISO8601, localTimeZone, DATE_YYYY_MM_DD, localTimeZone);
                if (!dateCurrent.equals(dateNext)) {
                    listLastItemInGroupsPosition.add(i);
                }
            }

            // get group titles' position
            listHeaderPosition.add(0);  // at the first
            for (Integer previousPos : listLastItemInGroupsPosition) {
                listHeaderPosition.add(previousPos + 1);    // the next one of the last item in the groups, is the first one in the next group
            }
        }
    }

    private void initParams() {
        headerTextSize = context.getResources().getDimension(R.dimen.x26sp);
        headerHeight = (int) (2.0 * headerTextSize);
        headerTextMarginBottom = headerHeight / 3;
        headerPadding = headerHeight / 8;
        headerCornerRadius = 5;
    }

    private void initPaint() {
        paint = new Paint();
        //about color block
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        //about group title's  font
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(headerTextSize);
        //about underline
        paint.setStrokeWidth(6);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);

        if (listHeaderPosition.contains(position)) {
            outRect.set(0, headerHeight, 0, 0);
        } else {
            outRect.set(0, 0, 0, 0);
        }
    }

    // draw normal header here
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final int currentCount = parent.getChildCount();  // child count of one screen range

        for (int i = 0; i < currentCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildLayoutPosition(child);

            if (listHeaderPosition.contains(position)) {
                String title = getTitleText(data.get(position).browseDate);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int left = child.getLeft();
                int right = child.getRight();
                int bottom = child.getTop() - params.topMargin;
                int top = bottom - headerHeight;
                int baseLine = bottom - headerTextMarginBottom;

                drawGroupTitle(c, title, left, right, bottom, top, baseLine);
            }
        }
    }

    // draw sticky header here
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final LinearLayoutManager manager = (LinearLayoutManager) parent.getLayoutManager();
        final int firstPosition = manager.findFirstVisibleItemPosition();
        final View child = parent.findViewHolderForAdapterPosition(firstPosition).itemView;

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        int left = child.getLeft();
        int right = child.getRight();
        int top = parent.getPaddingBottom();
        int bottom;
        int baseline;

        int childHeight = child.getBottom() + params.bottomMargin;
        int groupTitleHeight = headerHeight;

        if (listLastItemInGroupsPosition.contains(firstPosition) && childHeight < groupTitleHeight) {
            bottom = childHeight;
            baseline = bottom - headerTextMarginBottom;
        } else {
            bottom = groupTitleHeight;
            baseline = groupTitleHeight - headerTextMarginBottom;
        }

        String title = getTitleText(data.get(firstPosition).browseDate);
        drawGroupTitle(c, title, left, right, bottom, top, baseline);
    }

    private String getTitleText(String time) {
        return DateConvertManager.turnToSpecificPatternAndTimeZone(time, DATE_YYYY_MM_DD, TimeZone.getDefault(), DATE_EEEE_MMMM_DD_YY, TimeZone.getDefault());
    }

    private void drawGroupTitle(Canvas canvas, String title, int rectLeft, int rectRight, int rectBottom, int rectTop, int textBaseline) {
        // draw color block
        paint.setColor(Color.WHITE);
        RectF rect = new RectF(rectLeft, rectTop, rectRight, rectBottom);
        canvas.drawRoundRect(rect, headerCornerRadius, headerCornerRadius, paint);

        // draw title
        paint.setColor(context.getResources().getColor(R.color.colorDeepGray));
        canvas.drawText(title, rectLeft + headerPadding, textBaseline, paint);

        // draw underline
        canvas.drawLine(rectLeft + headerPadding, rectBottom - headerPadding, rectRight - headerPadding, rectBottom - headerPadding, paint);
    }
}
