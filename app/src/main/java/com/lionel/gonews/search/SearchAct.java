package com.lionel.gonews.search;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lionel.gonews.R;
import com.lionel.gonews.base.IDisplayNewsList;
import com.lionel.gonews.content.ContentAct;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.remote.ErrorInfo;
import com.lionel.gonews.util.DialogManager;

import java.util.List;

import static com.lionel.gonews.util.Constants.NEWS_CONTENT;

public class SearchAct extends AppCompatActivity implements SearchBox.ISearchBoxCallback, IDisplayNewsList.IDisplayNewsListCallback, SearchDateRangePopupWindow.IDateRangeCallback {

    private SearchViewModel viewModel;
    private SearchBox searchBox;
    private View layoutResult;
    private TextView txtResultCount;
    private ImageButton btnDateFilter;
    private IDisplayNewsList newsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search);

        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

        initObserve();
        initLayoutResult();
        initSearchBox();
    }


    private void initObserve() {
        viewModel.getNewsDataLiveData().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> newsList) {
                if (layoutResult.getVisibility() != View.VISIBLE) {
                    layoutResult.setVisibility(View.VISIBLE);
                }
                newsListView.showNews(newsList);
            }
        });

        viewModel.getTotalCountLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer count) {
                setResultCount(count);
            }
        });

        viewModel.getIsLastPageLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLastPage) {
                newsListView.setIsLastPage(isLastPage);
            }
        });

        viewModel.getIsLoadingLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                newsListView.setIsLoading(isLoading);
                newsListView.showRefreshingAtBeginning(isLoading);
            }
        });
        viewModel.getErrorInfoLiveData().observe(this, new Observer<ErrorInfo>() {
            @Override
            public void onChanged(@Nullable ErrorInfo errorInfo) {
                if (errorInfo.isError) {
                    DialogManager.showErrorDialog(SearchAct.this, errorInfo.msg);
                }
                newsListView.setIsError(errorInfo.isError);
            }
        });
    }

    private void initLayoutResult() {

        layoutResult = findViewById(R.id.layoutResult);

        txtResultCount = findViewById(R.id.txtResultCount);

        LayoutInflater inflater = LayoutInflater.from(this);
        btnDateFilter = findViewById(R.id.imbBtnFilter);
        View viewPopupDate = inflater.inflate(R.layout.layout_popup_date,null, false);
        final SearchDateRangePopupWindow datePopupWindow = new SearchDateRangePopupWindow(this, viewPopupDate, this);
        btnDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePopupWindow.showAsDropDown(btnDateFilter);
            }
        });

        newsListView = findViewById(R.id.newsListView);
        newsListView.setCallback(this);
    }

    private void setResultCount(int count) {
        txtResultCount.setText(getString(R.string.search_result_count, count));
    }

    private void initSearchBox() {
        searchBox = findViewById(R.id.searchBox);
        searchBox.setCallback(this);
        // searchBox.setSearchHistory(data);
    }

    @Override
    public void startQuery(String queryWord) {
        newsListView.showRefreshingAgain();
        viewModel.setQueryWord(queryWord);
        viewModel.initNewsWithoutCache();
    }

    @Override
    public void onBackBtnPressed() {
        finish();
    }

    @Override
    public void onRefreshNews() {
        viewModel.initNewsWithoutCache();
    }

    @Override
    public void onLoadMoreNews() {
        viewModel.loadMoreNews();
    }

    @Override
    public void onIntentToNewsContent(News news) {
        Intent intent = new Intent();
        intent.setClass(this, ContentAct.class);
        intent.putExtra(NEWS_CONTENT, news);
        startActivity(intent);
    }

    @Override
    public void onDateRangeSelected(String from, String to) {

    }
}
