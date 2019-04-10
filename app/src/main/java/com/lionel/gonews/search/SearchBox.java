package com.lionel.gonews.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lionel.gonews.R;

import java.util.List;

public class SearchBox extends FrameLayout {

    private final Context context;
    private AppCompatAutoCompleteTextView edtSearchBox;
    private ISearchBoxCallback callback;

    public interface ISearchBoxCallback {
        void startQuery(String queryWord);

        void onBackBtnPressed();
    }

    public SearchBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_search_box, this);

        this.context = context;

        initEdtSearchBox();
        initBackBtn();
        initCancelBtn();
    }

    public void setCallback(ISearchBoxCallback callback) {
        this.callback = callback;
    }

    public void setData(List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                R.layout.item_search_box_history, R.id.txt_item_search_box_history,
                data);

        if (edtSearchBox != null) {
            edtSearchBox.setAdapter(adapter);
        }
    }

    private void initEdtSearchBox() {
        edtSearchBox = findViewById(R.id.edt_search_box);

        // show history as edt box is touched
        edtSearchBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edtSearchBox.showDropDown();
                return false;
            }
        });

        // events of history list is pressed
        edtSearchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                doSearch();
            }
        });

        //as search button on keyboard is pressed
        edtSearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();

                    //hide keyboard
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    private void initBackBtn() {
        ImageButton btnBack = findViewById(R.id.img_btn_back);
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onBackBtnPressed();
                }
            }
        });
    }

    private void initCancelBtn() {
        ImageButton btnCancel = findViewById(R.id.img_btn_cancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearchBox.setText("");
            }
        });
    }

    private void doSearch() {
        String queryWord = edtSearchBox.getText().toString();
        if (callback != null) {
            edtSearchBox.clearFocus();
            callback.startQuery(queryWord);
        }
    }
}
