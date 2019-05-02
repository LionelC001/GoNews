package com.lionel.gonews.search;

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
import com.lionel.gonews.data.local.query_word.QueryWord;
import com.lionel.gonews.data.remote.ErrorInfo;
import com.lionel.gonews.util.DialogManager;

import java.util.ArrayList;
import java.util.List;

import static com.lionel.gonews.util.Constants.NEWS_CONTENT;
import static com.lionel.gonews.util.Constants.POPUP_WINDOW_DATE_RANGE_STATE;
import static com.lionel.gonews.util.Constants.POPUP_WINDOW_SORT_BY_STATE;
import static com.lionel.gonews.util.Constants.SEARCH_BOX_DROP_DOWN_STATE;

public class SearchAct extends AppCompatActivity implements SearchBox.ISearchBoxCallback, IDisplayNewsList.IDisplayNewsListCallback, SearchDateRangePopupWindow.IDateRangeCallback, SearchSortByPopupWindow.ISortByCallback {

    private SearchViewModel viewModel;
    private SearchBox searchBox;
    private TextView txtResultCount;
    private ImageButton btnSortByFilter, btnDateFilter;
    private IDisplayNewsList newsListView;
    private SearchDateRangePopupWindow datePopupWindow;
    private SearchSortByPopupWindow sortByPopupWindow;
    private View imgBackground;
    private boolean isShowSearchBoxDropDown = true;  //at the beginning, show dropDown. if screen orientation has changed, we want the showing state is same like before.

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
                showFilter();
                setIsShowBackground(false);
                newsListView.showNews(newsList);
            }
        });

        viewModel.getTotalCountLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer count) {
                setResultCount(count);
                if (count == 0) {
                    setIsShowBackground(true);
                }
            }
        });

        viewModel.getIsLastPageLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLastPage) {
                newsListView.setIsShowLoadingNextPageAnim(!isLastPage);
            }
        });

        viewModel.getIsLoadingLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                newsListView.setIsLoadingState(isLoading);
                newsListView.setIsShowLoadingAnimAtBeginning(isLoading);
            }
        });
        viewModel.getErrorInfoLiveData().observe(this, new Observer<ErrorInfo>() {
            @Override
            public void onChanged(@Nullable ErrorInfo errorInfo) {
                if (errorInfo.isError) {
                    DialogManager.showError(SearchAct.this, errorInfo.msg);
                }
                newsListView.setIsErrorState(errorInfo.isError);
            }
        });
    }

    private void setIsShowBackground(boolean isShowing) {
        if (isShowing) {
            imgBackground.setVisibility(View.VISIBLE);
        } else {
            imgBackground.setVisibility(View.GONE);
        }
    }

    private void showFilter() {
        if (btnSortByFilter.getVisibility() != View.VISIBLE ||
                btnDateFilter.getVisibility() != View.VISIBLE) {
            btnSortByFilter.setVisibility(View.VISIBLE);
            btnDateFilter.setVisibility(View.VISIBLE);
        }
    }

    private void initLayoutResult() {
        imgBackground = findViewById(R.id.imgBackground);

        txtResultCount = findViewById(R.id.txtResultCount);

        btnSortByFilter = findViewById(R.id.imgBtnSortByFilter);
        sortByPopupWindow = new SearchSortByPopupWindow(this, this);
        btnSortByFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByPopupWindow.show(btnSortByFilter);
            }
        });

        btnDateFilter = findViewById(R.id.imgBtnDateRangeFilter);
        datePopupWindow = new SearchDateRangePopupWindow(this, this);
        btnDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePopupWindow.show(btnDateFilter);
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

        viewModel.getAllQueryWord().observe(this, new Observer<List<QueryWord>>() {
            @Override
            public void onChanged(@Nullable List<QueryWord> queryWords) {
                searchBox.setSearchHistory(viewModel.getCleanQueryWords(queryWords));
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        isShowSearchBoxDropDown = savedInstanceState.getBoolean(SEARCH_BOX_DROP_DOWN_STATE);
        restoreDatePopupWindow(savedInstanceState.getStringArrayList(POPUP_WINDOW_DATE_RANGE_STATE));
        restoreSortByPopupWindow(savedInstanceState.getInt(POPUP_WINDOW_SORT_BY_STATE));

        super.onRestoreInstanceState(savedInstanceState);
    }

    private void restoreDatePopupWindow(ArrayList<String> state) {
        datePopupWindow.setState(state);
    }

    private void restoreSortByPopupWindow(int state) {
        sortByPopupWindow.setState(state);
    }

    @Override
    protected void onResume() {
        if (searchBox != null && isShowSearchBoxDropDown) {
            searchBox.showDropDown();
        }
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SEARCH_BOX_DROP_DOWN_STATE, searchBox.getIsDropDownShowing());
        outState.putStringArrayList(POPUP_WINDOW_DATE_RANGE_STATE, datePopupWindow.getState());
        outState.putInt(POPUP_WINDOW_SORT_BY_STATE, sortByPopupWindow.getState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        dismissAllPopupWindow();   // avoid memory leak
        super.onDestroy();
    }

    private void dismissAllPopupWindow() {
        if (datePopupWindow != null && datePopupWindow.isShowing()) {
            datePopupWindow.dismiss();
            datePopupWindow = null;
        }

        if (sortByPopupWindow != null && sortByPopupWindow.isShowing()) {
            sortByPopupWindow.dismiss();
            sortByPopupWindow = null;
        }
    }

    @Override
    public void startQuery(String queryWord) {
        newsListView.showLoadingAnimAgain();
        viewModel.storeQueryWord(queryWord);
        viewModel.setQueryWord(queryWord);
        viewModel.initNewsWithoutCache();
    }

    @Override
    public void onBackBtnPressed() {
        finish();
    }

    @Override
    public void deleteAllQueryWord() {
        viewModel.deleteAllQueryWord();
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
        newsListView.showLoadingAnimAgain();
        viewModel.setDateRange(from, to);
        viewModel.initNewsWithoutCache();
    }

    @Override
    public void onSortBySelected(String sortBy) {
        newsListView.showLoadingAnimAgain();
        viewModel.setSortBy(sortBy);
        viewModel.initNewsWithoutCache();
    }
}
