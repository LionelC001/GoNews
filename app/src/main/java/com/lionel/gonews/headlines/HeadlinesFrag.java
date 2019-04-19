package com.lionel.gonews.headlines;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lionel.gonews.R;
import com.lionel.gonews.base.IDisplayNewsList;
import com.lionel.gonews.content.ContentAct;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.remote.ErrorInfo;
import com.lionel.gonews.util.DialogManager;

import java.util.List;

import static com.lionel.gonews.util.Constants.NEWS_CONTENT;

/**
 * <p>
 * note: in method {@link #initNewsListView()},
 * we have to call getView() not getActivity()!!!
 * if call getActivity(), the view objects will be the same one, then the others' (views) callback will be null.
 */
public class HeadlinesFrag extends Fragment implements IDisplayNewsList.IDisplayNewsListCallback {

    private static final String CATEGORY = "category";

    private HeadlinesFragViewModel viewModel;
    private IDisplayNewsList newsListView;

    public HeadlinesFrag() {

    }

    public static HeadlinesFrag newInstance(String category) {
        HeadlinesFrag fragment = new HeadlinesFrag();
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(HeadlinesFragViewModel.class);

        initObserve();
    }

    private void initObserve() {
        viewModel.getNewsDataLiveData().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> newsList) {
                newsListView.showNews(newsList);
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
                newsListView.showOrCloseRefreshing(isLoading);
            }
        });
        viewModel.getErrorInfoLiveData().observe(this, new Observer<ErrorInfo>() {
            @Override
            public void onChanged(@Nullable ErrorInfo errorInfo) {
                if (errorInfo.isError) {
                    DialogManager.showErrorDialog(getActivity(), errorInfo.msg);
                }
                newsListView.setIsError(errorInfo.isError);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_headlines, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initNewsListView();
        initNews();
    }

    private void initNewsListView() {
        newsListView = getView().findViewById(R.id.newsListView);  // don't use getActivity().
        newsListView.setCallback(HeadlinesFrag.this);
    }

    private void initNews() {
        String category = getArguments().getString(CATEGORY);
        viewModel.setQueryCategory(category);
        viewModel.initNews();
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
        intent.setClass(getActivity(), ContentAct.class);
        intent.putExtra(NEWS_CONTENT, news);
        startActivity(intent);
    }
}
