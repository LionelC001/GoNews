package com.lionel.gonews.favorite_history;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lionel.gonews.R;
import com.lionel.gonews.base.recyclerview.IDisplayNewsList;
import com.lionel.gonews.content.ContentAct;
import com.lionel.gonews.data.News;

import java.util.List;

import static com.lionel.gonews.util.Constants.NEWS_CONTENT;
import static com.lionel.gonews.util.Constants.TYPE_ACT;
import static com.lionel.gonews.util.Constants.TYPE_FAVORITE;

public class FavoriteHistoryAct extends AppCompatActivity implements IDisplayNewsList.IDisplayNewsListCallback {

    private FavoriteHistoryViewModel viewModel;
    private IDisplayNewsList newsListView;
    private View imgBackground;
    private String actType;
    private TextView txtTitle, txtClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_favorite_history);

        viewModel = ViewModelProviders.of(this).get(FavoriteHistoryViewModel.class);
        actType = getIntent().getStringExtra(TYPE_ACT);

        initToolbar();
        initNewsListView();
        initBackground();
        initContent();
    }

    private void initToolbar() {
        txtTitle = findViewById(R.id.txtTitle);
        txtClear = findViewById(R.id.txtClear);
        txtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteAllHistoryNews();
            }
        });

        ImageButton btnBack = findViewById(R.id.imgBtnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initNewsListView() {
        newsListView = findViewById(R.id.newsListView);
        newsListView.setCallback(this);
        newsListView.setIsShowLoadingNextPageAnim(false);
        newsListView.setIsEnableRefreshLayout(false);
    }

    private void initBackground() {
        imgBackground = findViewById(R.id.imgBackground);
    }

    private void setIsShowBackground(boolean isShow) {
        imgBackground.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void initContent() {
        if (checkIsActTypeFavorite()) {
            imgBackground.setBackgroundResource(R.drawable.ic_favorite_full_gray);
            txtTitle.setText(R.string.favorite);
            viewModel.getFavoriteNews().observe(this, new NewsDataObserver());
        } else {
            imgBackground.setBackgroundResource(R.drawable.ic_history);
            txtTitle.setText(R.string.history);
            txtClear.setVisibility(View.VISIBLE);
            viewModel.getHistoryNews().observe(this, new NewsDataObserver());
        }
    }

    private boolean checkIsActTypeFavorite() {
        if (actType.equals(TYPE_FAVORITE)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onIntentToNewsContent(News news) {
        Intent intent = new Intent();
        intent.setClass(this, ContentAct.class);
        intent.putExtra(NEWS_CONTENT, news);
        startActivity(intent);
    }

    @Override
    public void onRefreshNews() {
        // nothing to do.
    }

    @Override
    public void onLoadMoreNews() {
        // nothing to do.
    }

    private class NewsDataObserver implements Observer<List<News>> {
        @Override
        public void onChanged(@Nullable List<News> newsList) {
            setIsShowBackground(newsList.size() <= 0);
            if (checkIsActTypeFavorite()) {
                newsListView.showNews(newsList);
            } else {
                newsListView.showNewsWithDateGroup(newsList);
            }
        }
    }
}
