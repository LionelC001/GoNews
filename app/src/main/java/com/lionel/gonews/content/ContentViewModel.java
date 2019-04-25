package com.lionel.gonews.content;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.lionel.gonews.BR;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.local.FavoriteNews;
import com.lionel.gonews.data.local.LocalNewsSource;

public class ContentViewModel extends AndroidViewModel {
    private ViewDataBinding binding;
    private final LocalNewsSource localNewsSource;
    private FavoriteNews favoriteNews;

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

    public void storeLocalNewsHistory(News news) {
        localNewsSource.insertHistory(news);
    }

    public void updateFavorite(boolean isFavorite) {

    }

    private String drawableToBase64(Drawable drawable) {


        return null;
    }
}
