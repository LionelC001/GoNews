package com.lionel.gonews.search;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lionel.gonews.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchBox.ISearchBoxCallback {

    private SearchBox searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();

        testSearchBox();
    }

    private void initViews() {
        searchBox = findViewById(R.id.searchBox);
        searchBox.setCallback(this);
    }


    private void testSearchBox() {


        List<String> data = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            data.add("" + (i + 1));
        }
        searchBox.setData(data);
    }

    @Override
    public void startQuery(String queryWord) {
        Log.d("<>", queryWord);
    }

    @Override
    public void onBackBtnPressed() {
        finish();
    }
}
