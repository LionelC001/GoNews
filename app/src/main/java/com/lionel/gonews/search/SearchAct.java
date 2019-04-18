package com.lionel.gonews.search;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lionel.gonews.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAct extends AppCompatActivity implements SearchBox.ISearchBoxCallback {

    private SearchBox searchBox;
    private View layoutResult;
    private TextView txtResultCount;
    private ImageButton btnFilter;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search);


        initSearchBox();
        initLayoutResult();
        initDrawerLayout();

        testSearchBox();
    }

    private void initDrawerLayout() {
        drawerLayout = findViewById(R.id.drawerLayout);
    }


    private void initSearchBox() {
        searchBox = findViewById(R.id.searchBox);
        searchBox.setCallback(this);
    }

    private void initLayoutResult() {
        layoutResult = findViewById(R.id.layoutResult);
        txtResultCount = findViewById(R.id.txtResultCount);
        btnFilter = findViewById(R.id.imbBtnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
    }

    private void testSearchBox() {


        List<String> data = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            data.add("" + (i + 1));
        }
        searchBox.setSearchHistory(data);
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
