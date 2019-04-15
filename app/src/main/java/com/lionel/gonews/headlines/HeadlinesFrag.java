package com.lionel.gonews.headlines;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
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
import com.lionel.gonews.data.News;

import java.util.List;

import static com.lionel.gonews.util.Constants.DISTANCE_TO_SYNC;
import static com.lionel.gonews.util.Constants.END_POSITION;

public class HeadlinesFrag extends Fragment {

    private static final String CATEGORY = "category";

    private HeadlinesFragViewModel viewModel;
    private HeadlinesRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

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

        ViewModelProvider.Factory factory = new HeadlinesFragViewModelFactory(getActivity().getApplication(), getArguments().getString(CATEGORY));
        viewModel = ViewModelProviders.of(this, factory).get(HeadlinesFragViewModel.class);
        adapter = new HeadlinesRecyclerViewAdapter();

        initObserve();
    }

    private void initObserve() {
        viewModel.newsData.observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> newsList) {
                showNews(newsList);
            }
        });
        viewModel.isInitLoading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isInitLoading) {
                if (isInitLoading != null && isInitLoading) {
                    refreshLayout.setRefreshing(true);  //show loading anim
                } else {
                    refreshLayout.setRefreshing(false); //stop loading anim
                }
            }
        });
        viewModel.isLastPage.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLastPage) {
                adapter.setIsLastPage(isLastPage);
            }
        });
    }

    private void showNews(List<News> data) {
        adapter.setData(data);
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
                if (!viewModel.getLoadingState() && !recyclerView.canScrollVertically(1)) {  // 1 means down, btw -1 means up
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
                viewModel.reloadNews();
            }
        });
    }

    private void initNews() {
        viewModel.initNews();
    }
}