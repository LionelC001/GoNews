package com.lionel.gonews.headlines;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lionel.gonews.R;
import com.lionel.gonews.data.News;

import java.util.List;

public class HeadlinesFragment extends Fragment {

    private static final String CATEGORY = "category";

    private HeadlinesFragmentViewModel viewModel;

    public HeadlinesFragment() {

    }

    public static HeadlinesFragment newInstance(String category) {
        HeadlinesFragment fragment = new HeadlinesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY, category);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.frag_headlines, container, false);
        viewModel = new HeadlinesFragmentViewModel(getContext(), dataBinding);

        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String category = getArguments().getString(CATEGORY);
        if (category !=null){
            //start loading
            viewModel.start(category);
        }
    }

    @BindingAdapter("setData")
    public static void setData(RecyclerView view, List<News> data) {
        if (data != null) {
            HeadlinesRecyclerViewAdapter adapter = new HeadlinesRecyclerViewAdapter(data);
            view.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL, false));
            view.setAdapter(adapter);

            //hide loading
        }
    }
}
