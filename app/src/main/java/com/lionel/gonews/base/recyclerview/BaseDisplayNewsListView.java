package com.lionel.gonews.base.recyclerview;

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
 * A RecyclerView can show news items, with loadingAnim and loadingNextPageAnim.
 * go with {@link BaseRecyclerViewAdapter} and {@link IDisplayNewsList}
 * will call callback when these actions: refresh, need load more data, intent to another place.
 */
public class BaseDisplayNewsListView extends FrameLayout implements IDisplayNewsList, BaseRecyclerViewAdapter.IRecyclerViewAdapterCallback {
    private final BaseRecyclerViewAdapter adapter;
    private final Context context;
    private SwipeRefreshLayout refreshLayout;
    private IDisplayNewsList.IDisplayNewsListCallback callback;
    private boolean isShowLoadingAnim = true;
    private boolean isLoadingState = false;
    private boolean isErrorState = false;
    private RecyclerView recyclerView;

    public BaseDisplayNewsListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_display_result_news, this);

        this.context = context;
        adapter = new BaseRecyclerViewAdapter();
        adapter.setCallback(this);

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
                if (isRecyclerViewHasItems() && !isErrorState && !isLoadingState && !recyclerView.canScrollVertically(1)) {  // 1 means down, btw -1 means up
                    callback.onLoadMoreNews();
                }
            }
        });
    }

    private boolean isRecyclerViewHasItems() {
        return adapter.getItemCount() > 1;  // 1 means background view
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
                isShowLoadingAnim = true;
                callback.onRefreshNews();
            }
        });
    }

    @Override
    public void setCallback(IDisplayNewsList.IDisplayNewsListCallback callback) {
        this.callback = callback;
    }

    @Override
    public void showNews(List<News> data) {
        adapter.setData(data);
    }

    @Override
    public void showNewsWithDateGroup(List<News> data) {
        showNews(data);
        recyclerView.addItemDecoration(new BaseDateItemDecoration(context, data));
    }

    @Override
    public void setIsShowLoadingAnimAtBeginning(boolean isShow) {
        if (isShowLoadingAnim && isShow) {  //show refresh loading only once at new result
            refreshLayout.setRefreshing(true);
        } else {
            refreshLayout.setRefreshing(false);
            isShowLoadingAnim = false;
        }
    }

    @Override
    public void showLoadingAnimAgain() {
        isShowLoadingAnim = true;
    }

    @Override
    public void setIsShowLoadingNextPageAnim(boolean isShow) {
        adapter.setIsShowLoadingNextPageAnim(isShow);
    }

    @Override
    public void setIsLoadingState(boolean isLoadingState) {
        this.isLoadingState = isLoadingState;
    }

    @Override
    public void setIsErrorState(boolean isErrorState) {
        this.isErrorState = isErrorState;
    }

    @Override
    public void onItemClick(News news) {
        callback.onIntentToNewsContent(news);
    }
}
