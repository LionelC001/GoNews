package com.lionel.gonews.content;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lionel.gonews.BR;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.local.LocalNews;
import com.lionel.gonews.data.local.LocalNewsSource;

public class ContentViewModel extends AndroidViewModel {
    private ViewDataBinding binding;
    private final LocalNewsSource localNewsSource;
    private LocalNews localNews;

    public ContentViewModel(@NonNull Application application) {
        super(application);

        localNewsSource = new LocalNewsSource(application);
    }

    public void setBinding(ViewDataBinding binding) {
        this.binding = binding;
    }

    public void setNewsContent(News news) {
        binding.setVariable(BR.remoteNews, news);
    }

    public void initLocalNewsHistory(News news) {
        String content = news.content != null ? news.content : news.description;
        localNews = new LocalNews(news.source.name, news.title, news.url, null, news.publishedAt, content, true, false);
    }

    public void startStoreLocalNewsHistory(Drawable drawable) {
        localNewsSource.insert(localNews);
    }

    private String drawableToBase64(Drawable drawable) {


        return null;
    }

    public void updateFavorite(boolean isFavorite) {
        localNews.isFavorite = isFavorite;
        Log.d("<>", "favorite: " + localNews.isFavorite);
        localNewsSource.update(localNews);
    }
}
