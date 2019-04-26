package com.lionel.gonews.headlines;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lionel.gonews.R;
import com.lionel.gonews.favorite_history.FavoriteHistoryAct;
import com.lionel.gonews.search.SearchAct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.lionel.gonews.util.Constants.CATEGORYS;
import static com.lionel.gonews.util.Constants.TYPE_ACT;
import static com.lionel.gonews.util.Constants.TYPE_FAVORITE;
import static com.lionel.gonews.util.Constants.TYPE_HISTORY;

public class HeadlinesAct extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_headlines);

        initDrawerLayout();
        initBtnMenu();
        initSearchBox();
        initViewPager();
    }

    private void initDrawerLayout() {
        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_favorite:
                        intentToActivity(TYPE_FAVORITE);
                        break;
                    case R.id.menu_history:
                        intentToActivity(TYPE_HISTORY);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void intentToActivity(String type) {
        Intent intent = new Intent();
        intent.setClass(this, FavoriteHistoryAct.class);
        intent.putExtra(TYPE_ACT, type);
        startActivity(intent);
    }

    private void initBtnMenu() {
        ImageButton btnMenu = findViewById(R.id.imgBtnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void initSearchBox() {
        TextView searchBox = findViewById(R.id.txtSearchBox);
        searchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HeadlinesAct.this, SearchAct.class);
                startActivity(intent);
            }
        });
    }

    private void initViewPager() {
        HeadlinesViewPagerAdapter adapter = new HeadlinesViewPagerAdapter(getSupportFragmentManager(), initFragments(), getTopics());

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private List<Fragment> initFragments() {
        List<Fragment> fragments = new ArrayList<>();
        for (String category : CATEGORYS) {
            fragments.add(HeadlinesFrag.newInstance(category));
        }
        return fragments;
    }

    private List<String> getTopics() {
        return Arrays.asList(getResources().getStringArray(R.array.topics));
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
