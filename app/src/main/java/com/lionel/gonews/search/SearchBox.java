package com.lionel.gonews.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.lionel.gonews.R;

public class SearchBox extends FrameLayout {
    private ImageButton btnMenu, btnCancel;
    private AppCompatAutoCompleteTextView edtText;

    public SearchBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_query_box, this);

        initView();
        initCallback();
    }

    private void initView() {
        btnMenu = findViewById(R.id.img_btn_menu);
        btnCancel = findViewById(R.id.img_btn_cancel);
        edtText = findViewById(R.id.edt_text);
    }

    private void initCallback() {

    }


    public void CancelQueryBoxFocus() {

        edtText.clearFocus();
    }
}
