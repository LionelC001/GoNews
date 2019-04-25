package com.lionel.gonews.content;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.lionel.gonews.BR;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.local.FavoriteNews;
import com.lionel.gonews.data.local.LocalNewsSource;

import java.io.ByteArrayOutputStream;

public class ContentViewModel extends AndroidViewModel {
    private ViewDataBinding binding;
    private final LocalNewsSource localNewsSource;
    private News news;
    private FavoriteNews favoriteNews;
    private boolean isDrawableReady = false;
    private boolean isFavoriteClicked = false;
    private String base64Drawable;

    public ContentViewModel(@NonNull Application application) {
        super(application);

        localNewsSource = new LocalNewsSource(application);
    }

    public void setBindingNews(ViewDataBinding binding, News news) {
        this.binding = binding;
        this.news = news;
        binding.setVariable(BR.remoteNews, news);
    }

    public LiveData<Integer> checkIsFavoriteNews(String title) {
        return localNewsSource.getFavoriteNewsCount(title);
    }

    public void storeLocalNewsHistory() {
        localNewsSource.insertHistory(news);
    }

    public void onDrawableIsReady(Drawable drawable) {
        drawableToBase64(drawable);
    }

    private void drawableToBase64(final Drawable drawable) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes = baos.toByteArray();
                ContentViewModel.this.base64Drawable = Base64.encodeToString(bytes, Base64.DEFAULT);
                ContentViewModel.this.isDrawableReady = true;


                if (isFavoriteClicked) { // maybe the favorite checkbox is clicked early
                    storeLocalFavoriteNews();
                }
            }
        }).start();
    }

    public void updateFavorite(boolean isFavorite) {
        this.isFavoriteClicked = isFavorite;
        if (favoriteNews == null) {
            String content = news.content != null ? news.content : news.description;
            favoriteNews = new FavoriteNews(news.source.name, news.title, news.url, null, news.publishedAt, content);
        }

        if (isDrawableReady && isFavorite) {  // store favorite news have to wait until the imageview drawable is ready
            storeLocalFavoriteNews();
        } else if (!isFavorite) {
            localNewsSource.deleteFavorite(favoriteNews);
        }
    }

    private void storeLocalFavoriteNews() {
        favoriteNews.base64Image = base64Drawable;
        localNewsSource.insertFavorite(favoriteNews);
    }
}
