package com.lionel.gonews.search;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.lionel.gonews.R;
import com.lionel.gonews.base.BasePopupWindow;

import static com.lionel.gonews.util.Constants.POPULARITY;
import static com.lionel.gonews.util.Constants.PUBLISHEDAT;
import static com.lionel.gonews.util.Constants.RELEVANCY;

public class SearchSortByPopupWindow extends BasePopupWindow {
    private final Context context;
    private RadioGroup radGrp;
    private final ISortByCallback callback;
    private View viewSortBy;
    private String value = RELEVANCY;
    private String oldValue = RELEVANCY;

    public interface ISortByCallback {
        void onSortBySelected(String sortBy);
    }

    public SearchSortByPopupWindow(Context context, ISortByCallback callback) {
        super(context);
        this.context = context;
        this.callback = callback;

        initDimBehind();
        initBackgroundStyle();
        initView();
        initRadioGroup();
    }

    private void initDimBehind() {
        super.setIsDimBehind(true);
    }

    private void initBackgroundStyle() {
        super.setBackgroundStyle(context.getDrawable(R.drawable.frame_gray_round_corner_white));
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        viewSortBy = inflater.inflate(R.layout.layout_popup_sortby, null, false);
        super.setView(viewSortBy);
    }

    private void initRadioGroup() {
        radGrp = viewSortBy.findViewById(R.id.radioGrp);
        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radRelevancy) {
                    value = RELEVANCY;
                } else if (checkedId == R.id.radPopularity) {
                    value = POPULARITY;
                } else if (checkedId == R.id.radPublishAt) {
                    value = PUBLISHEDAT;
                }
                SearchSortByPopupWindow.this.dismiss();
            }
        });
    }

    public void show(View anchor) {
        super.show(anchor, Gravity.START);
    }

    @Override
    public void dismiss() {
        if (checkIsValueChanged()) {
            callback.onSortBySelected(value);
        }
        super.dismiss();
    }

    public int getState() {
        return radGrp.getCheckedRadioButtonId();
    }

    public void setState(int state) {
        radGrp.check(state);
    }

    private boolean checkIsValueChanged() {
        if (!oldValue.equals(value)) {
            oldValue = value;
            return true;
        }
        return false;
    }
}
