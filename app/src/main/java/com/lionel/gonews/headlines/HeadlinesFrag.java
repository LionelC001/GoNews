package com.lionel.gonews.headlines;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lionel.gonews.R;
import com.lionel.gonews.content.ContentAct;
import com.lionel.gonews.data.News;
import com.lionel.gonews.util.DialogManager;

import java.util.List;

import static com.lionel.gonews.util.Constants.DISTANCE_TO_SYNC;
import static com.lionel.gonews.util.Constants.END_POSITION;
import static com.lionel.gonews.util.Constants.NEWS_CONTENT;

public class HeadlinesFrag extends Fragment implements HeadlinesRecyclerViewAdapter.IItemNewsCallback {

    private static final String CATEGORY = "category";

    private HeadlinesFragViewModel viewModel;
    private HeadlinesRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private boolean isShowRefreshing = true;
    private boolean isLoading = false;
    private boolean isErroring = false;

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
        adapter = new HeadlinesRecyclerViewAdapter(this);

        initObserve();
    }

    private void initObserve() {
        viewModel.getNewsData().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> newsList) {
                showNews(newsList);
            }
        });
        viewModel.getIsLoadingLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                HeadlinesFrag.this.isLoading = isLoading;
                showOrCloseRefreshing(isLoading);  //show loading anim at beginning
            }
        });
        viewModel.getIsLastPageLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLastPage) {
                adapter.setIsLastPage(isLastPage);
            }
        });
        viewModel.getIsErrorLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isError) {
                if (isError) {
                    DialogManager.showErrorDialog(getActivity());
                }
                isErroring = isError;
            }
        });
    }

    private void showNews(List<News> data) {
        adapter.setData(data);
    }

    private void showOrCloseRefreshing(boolean isLoading) {
        if (isShowRefreshing && isLoading) {  //show refresh loading only once at new result
            refreshLayout.setRefreshing(true);
        } else {
            refreshLayout.setRefreshing(false);
            isShowRefreshing = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_headlines, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        initRefreshLayout();
        initNews();
    }

    private void initRecyclerView() {
        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isErroring && !isLoading && !recyclerView.canScrollVertically(1)) {  // 1 means down, btw -1 means up
                    viewModel.loadMoreNews();
                }
            }
        });
    }

    private void initRefreshLayout() {
        refreshLayout = getView().findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorDeepGray);
        refreshLayout.setDistanceToTriggerSync(DISTANCE_TO_SYNC);
        refreshLayout.setProgressViewEndTarget(false, END_POSITION);
        refreshLayout.setSize(SwipeRefreshLayout.LARGE);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isShowRefreshing = true;
                viewModel.initNewsWithoutCache();
            }
        });
    }

    private void initNews() {
        String category = getArguments().getString(CATEGORY);
        viewModel.setQueryCondition(category);
        viewModel.initNews();
    }

    @Override
    public void onIntentToNewsContent(News news) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ContentAct.class);
        intent.putExtra(NEWS_CONTENT, news);
        startActivity(intent);
    }
}
