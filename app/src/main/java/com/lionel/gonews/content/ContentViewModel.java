package com.lionel.gonews.content;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;

import com.lionel.gonews.BR;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.local.LocalNewsSource;

import static com.lionel.gonews.util.Constants.DATE_ISO8601;
import static com.lionel.gonews.util.DateConvertManager.getCurrentDateSpecificPattern;

public class ContentViewModel extends AndroidViewModel {
    private final LocalNewsSource localNewsSource;
    private News news;
    private boolean isFavoriteExist = false;

    public ContentViewModel(@NonNull Application application) {
        super(application);

        localNewsSource = new LocalNewsSource(application);
    }

    public void setBindingNews(ViewDataBinding binding, News news) {
        this.news = news;
        binding.setVariable(BR.newsData, news);
    }

    public LiveData<Integer> checkIsFavoriteNews(News news) {
        return localNewsSource.checkIsFavorite(news);
    }

    public void setFavoriteClickedFromObserve() {
        this.isFavoriteExist = true;
    }

    public void storeNewsHistory() {
        news.isHistory = true;
        news.browseDate = getCurrentDateSpecificPattern(DATE_ISO8601);
        localNewsSource.insertOrUpdateHistory(news);
    }

    public void deleteFavorite() {
        localNewsSource.deleteFavorite(news);
        isFavoriteExist = false;
    }

    public void updateFavorite() {
        if (!isFavoriteExist) {
            news.isFavorite = true;
            news.favoriteDate =  getCurrentDateSpecificPattern(DATE_ISO8601);
            localNewsSource.updateIsFavorite(news);
        }
    }
}
