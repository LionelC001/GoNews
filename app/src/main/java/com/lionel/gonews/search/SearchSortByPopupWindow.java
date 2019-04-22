package com.lionel.gonews.search;

import android.content.Context;
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
    private final ISortByCallback callback;
    private View viewSortBy;
    private String selectedOption = RELEVANCY;

    public interface ISortByCallback {
        void onSortBySelected(String sortBy);
    }

    public SearchSortByPopupWindow(Context context, ISortByCallback callback) {
        super(context);
        this.context = context;
        this.callback = callback;

        initView();
        initRadioGroup();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        viewSortBy = inflater.inflate(R.layout.layout_popup_sortby, null, false);
        super.setView(viewSortBy);
    }

    private void initRadioGroup() {
        RadioGroup radGrp = viewSortBy.findViewById(R.id.radioGrp);
        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radRelevancy) {
                    selectedOption = RELEVANCY;
                } else if (checkedId == R.id.radPopularity) {
                    selectedOption = POPULARITY;
                } else if (checkedId == R.id.radPublishAt) {
                    selectedOption = PUBLISHEDAT;
                }
            }
        });
    }

    @Override
    public void dismiss() {
        callback.onSortBySelected(selectedOption);
        super.dismiss();
    }
}
