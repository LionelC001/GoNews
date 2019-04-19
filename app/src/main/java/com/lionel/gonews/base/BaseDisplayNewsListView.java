package com.lionel.gonews.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.lionel.gonews.R;
import com.lionel.gonews.data.News;

import java.util.List;

import static com.lionel.gonews.util.Constants.DISTANCE_TO_SYNC;
import static com.lionel.gonews.util.Constants.END_POSITION;

/**
 *
 */
public class BaseDisplayNewsListView extends FrameLayout implements IDisplayNewsList {
    private final BaseRecyclerViewAdapter adapter;
    private final Context context;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private IDisplayNewsList.IDisplayNewsListCallback callback;
    private boolean isShowRefreshing = true;
    private boolean isLoading = false;
    private boolean isError = false;

    public BaseDisplayNewsListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_display_result_news, this);

        this.context = context;
        adapter = new BaseRecyclerViewAdapter();

        initRecyclerView();
        initRefreshLayout();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isError && !isLoading && !recyclerView.canScrollVertically(1)) {  // 1 means down, btw -1 means up
                    callback.onLoadMoreNews();
                }
            }
        });
    }

    private void initRefreshLayout() {
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorDeepGray);
        refreshLayout.setDistanceToTriggerSync(DISTANCE_TO_SYNC);
        refreshLayout.setProgressViewEndTarget(false, END_POSITION);
        refreshLayout.setSize(SwipeRefreshLayout.LARGE);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isShowRefreshing = true;
                callback.onRefreshNews();
            }
        });
    }

    @Override
    public void setCallback(IDisplayNewsList.IDisplayNewsListCallback callback) {
        this.callback = callback;
        adapter.setItemNewsClickCallback(callback);
    }

    @Override
    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    @Override
    public void setIsLastPage(boolean isLastPage) {
        adapter.setIsLastPage(isLastPage);
    }

    @Override
    public void setIsError(boolean isError) {
        this.isError = isError;
    }

    @Override
    public void showNews(List<News> data) {
        adapter.setData(data);
    }

    @Override
    public void showRefreshingAtBeginning(boolean isShow) {
        if (isShowRefreshing && isShow) {  //show refresh loading only once at new result
            refreshLayout.setRefreshing(true);
        } else {
            refreshLayout.setRefreshing(false);
            isShowRefreshing = false;
        }
    }

    @Override
    public void showRefreshingAgain() {
        isShowRefreshing = true;
    }
}
