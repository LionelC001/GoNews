package com.lionel.gonews.favorite_history;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lionel.gonews.R;
import com.lionel.gonews.base.IDisplayNewsList;
import com.lionel.gonews.content.ContentAct;
import com.lionel.gonews.data.News;

import java.util.List;

import static com.lionel.gonews.util.Constants.NEWS_CONTENT;
import static com.lionel.gonews.util.Constants.TYPE_ACT;
import static com.lionel.gonews.util.Constants.TYPE_FAVORITE;
import static com.lionel.gonews.util.Constants.TYPE_HISTORY;


public class FavoriteHistoryAct extends AppCompatActivity implements IDisplayNewsList.IDisplayNewsListCallback {

    private FavoriteHistoryViewModel viewModel;
    private IDisplayNewsList newsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_favorite_history);

        viewModel = ViewModelProviders.of(this).get(FavoriteHistoryViewModel.class);

        initDisplayNews();
        initObserve();
        initContent();
    }

    private void initDisplayNews() {
        newsListView = findViewById(R.id.newsListView);
        newsListView.setCallback(this);
    }

    private void initObserve() {
        viewModel.getNewsData().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> news) {
                Log.d("<>", "new: " + news.size());
                newsListView.showNews(news);
            }
        });
    }

    private void initContent() {
        String type = getIntent().getStringExtra(TYPE_ACT);
        if (type.equals(TYPE_FAVORITE)) {
            initFavoriteContent();
        } else if (type.equals(TYPE_HISTORY)) {
            initHistoryContent();
        }
    }

    private void initFavoriteContent() {

        viewModel.getFavoriteNews().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> news) {
                Log.d("<>", "new: " + news.size());
                newsListView.showNews(news);
            }
        });
    }

    private void initHistoryContent() {

        viewModel.getHistoryNews().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> news) {
                Log.d("<>", "new: " + news.size());
                newsListView.showNews(news);
            }
        });
    }

    @Override
    public void onRefreshNews() {

    }

    @Override
    public void onLoadMoreNews() {

    }

    @Override
    public void onIntentToNewsContent(News news) {
        Intent intent = new Intent();
        intent.setClass(this, ContentAct.class);
        intent.putExtra(NEWS_CONTENT, news);
        startActivity(intent);
    }
}
