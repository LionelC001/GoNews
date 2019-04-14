package com.lionel.gonews.headlines;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lionel.gonews.R;
import com.lionel.gonews.data.News;

import java.util.List;

public class HeadlinesFragment extends Fragment {

    private static final String CATEGORY = "category";

    private HeadlinesFragmentViewModel viewModel;
    private HeadlinesRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    public HeadlinesFragment() {

    }

    public static HeadlinesFragment newInstance(String category) {
        HeadlinesFragment fragment = new HeadlinesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(HeadlinesFragmentViewModel.class);
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
//        viewModel.getStatus().
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
        getNews();
    }

    private void initRecyclerView() {
        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void getNews() {
        String category = getArguments().getString(CATEGORY);
        viewModel.getNews(category, 1);
    }

    private void showNews(List<News> data) {
        adapter.setData(data);
    }
}
