package com.lionel.gonews.favorite_history;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lionel.gonews.R;
import com.lionel.gonews.base.BaseDisplayNewsListView;
import com.lionel.gonews.base.IDisplayNewsList;
import com.lionel.gonews.data.News;


public class FavoriteHistoryAct extends AppCompatActivity implements IDisplayNewsList.IDisplayNewsListCallback {

    private String typeData;
    private FavoriteHistoryViewModel viewModel;
    private BaseDisplayNewsListView displayNewsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_favorite_history);

        viewModel = ViewModelProviders.of(this).get(FavoriteHistoryViewModel.class);

        initDisplayNews();
    }

    private void initDisplayNews() {
        displayNewsListView = findViewById(R.id.newsListView);
        displayNewsListView.setCallback(this);
    }

    @Override
    public void onRefreshNews() {

    }

    @Override
    public void onLoadMoreNews() {

    }

    @Override
    public void onIntentToNewsContent(News news) {

    }
}
