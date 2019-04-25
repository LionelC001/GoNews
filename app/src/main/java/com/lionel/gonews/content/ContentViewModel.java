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
import com.lionel.gonews.data.local.LocalNewsSource;

import java.io.ByteArrayOutputStream;
import java.net.URL;

;

public class ContentViewModel extends AndroidViewModel {
    private ViewDataBinding binding;
    private final LocalNewsSource localNewsSource;
    private News news;
    private boolean isFavoriteNewsReady = false;
    private boolean isFavoriteFromObserve;

    public ContentViewModel(@NonNull Application application) {
        super(application);

        localNewsSource = new LocalNewsSource(application);
    }

    public void setBindingNews(ViewDataBinding binding, News news) {
        this.binding = binding;
        this.news = news;
        binding.setVariable(BR.newsData, news);
    }

    public LiveData<Integer> checkIsFavoriteNews(String title) {
        return localNewsSource.checkIsFavorite(title);
    }

    // avoid the exist favorite news trigger imageToBase64FromUrl(), to waste data flow
    public void setFavoriteClickedFromObserve() {
        this.isFavoriteFromObserve = true;
    }

    public void storeNewsHistory() {
        news.isHistory = true;
        localNewsSource.insertOrUpdateHistory(news);
    }

    public void deleteFavorite() {
        localNewsSource.deleteFavorite(news.title);
    }

    public void updateFavorite() {
        if (news.base64ToImage == null && !isFavoriteFromObserve) {
            initFavoriteNews();
        }
        if (isFavoriteNewsReady) {  // store favorite news have to wait until the one is ready
            localNewsSource.updateIsFavorite(news);
        }
        isFavoriteFromObserve = false;
    }

    private void initFavoriteNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                news.base64ToImage = imageToBase64FromUrl();
                news.isFavorite = true;

                ContentViewModel.this.isFavoriteNewsReady = true;
                updateFavorite();
            }
        }).start();
    }

    private String imageToBase64FromUrl() {
        String base64Image = "default is no image";
        Log.d("<>", base64Image);
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
        Log.d("<>", "base64Image is DONE");
        return base64Image;
    }
}
