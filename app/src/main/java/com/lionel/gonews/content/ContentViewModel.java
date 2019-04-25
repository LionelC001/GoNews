package com.lionel.gonews.content;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.lionel.gonews.BR;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.local.FavoriteNews;
import com.lionel.gonews.data.local.LocalNewsSource;

import java.io.ByteArrayOutputStream;
import java.net.URL;

public class ContentViewModel extends AndroidViewModel {
    private ViewDataBinding binding;
    private final LocalNewsSource localNewsSource;
    private News news;
    private FavoriteNews favoriteNews;
    private boolean isFavoriteNewsReady = false;
    private boolean isFavoriteClicked = false;

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

    public void updateFavorite(boolean isFavorite) {
        this.isFavoriteClicked = isFavorite;
        if (favoriteNews == null) {
            initFavoriteNews();
        }

        if (isFavorite && isFavoriteNewsReady) {  // store favorite news have to wait until the one is ready
            localNewsSource.insertFavorite(favoriteNews);
        } else if (!isFavorite) {
            localNewsSource.deleteFavorite(favoriteNews);
        }
    }

    private void initFavoriteNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int id = news.title.hashCode();
                String content = news.content != null ? news.content : news.description;
                String base64Image = imageToBase64FromUrl();
                Log.d("<>", "base64 DONE");
                favoriteNews = new FavoriteNews(id, news.source.name, news.title, news.url, base64Image, news.publishedAt, content);

                ContentViewModel.this.isFavoriteNewsReady = true;
                updateFavorite(isFavoriteClicked);
            }
        }).start();
    }

    private String imageToBase64FromUrl() {
        Log.d("<>", "default is no image");
        String base64Image = "default is no image";
        try {
            URL imageUrl = new URL(news.urlToImage);
            Bitmap bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64Image;
    }
}
