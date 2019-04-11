package com.lionel.gonews.headlines;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lionel.gonews.R;
import com.lionel.gonews.search.SearchActivity;

public class HeadlinesActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headlines);

        initView();
        setupBtnMenu();
        setupSearchBox();

    }


    private void initView() {
        drawerLayout = findViewById(R.id.drawerLayout);


    }

    private void setupBtnMenu() {
        ImageButton btnMenu = findViewById(R.id.imgBtnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }


    private void setupSearchBox() {
        TextView searchBox = findViewById(R.id.txtSearchBox);
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HeadlinesActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initCallback() {

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
