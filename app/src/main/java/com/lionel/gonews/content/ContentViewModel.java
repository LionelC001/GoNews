package com.lionel.gonews.content;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;

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

    public void setBinding(ViewDataBinding binding){
        this.binding = binding;
    }

    public void setNewsContent(News news){
        binding.setVariable(BR.remoteNews, news);
    }

    public void setLocalNewsHistory(News news){
        localNews = new LocalNews(news.source, news.author, news.title, news.description, news.url, news.urlToImage, news.publishedAt, news.content, true, false);
        localNewsSource.insert(localNews);
    }

    public void updateFavorite(boolean isFavorite){
        localNews.isFavorite = isFavorite;
        localNewsSource.update(localNews);
    }
}
