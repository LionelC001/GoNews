package com.lionel.gonews.content;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.lionel.gonews.BR;
import com.lionel.gonews.data.News;
import com.lionel.gonews.data.local.LocalNewsSource;

import java.io.ByteArrayOutputStream;
import java.net.URL;

public class ContentViewModel extends AndroidViewModel {
    private ViewDataBinding binding;
    private final LocalNewsSource localNewsSource;
    private News news;
    private boolean isFavoriteExist = false;

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
        this.isFavoriteExist = true;
    }

    public void storeNewsHistory() {
        news.isHistory = true;
        localNewsSource.insertOrUpdateHistory(news);
    }

    public void deleteFavorite() {
        localNewsSource.deleteFavorite(news.title);
        isFavoriteExist = false;
    }

    public void updateFavorite() {
        if (news.base64ToImage == null && !isFavoriteExist) {
            initImageBase64();
        }
        if (!isFavoriteExist) {
            news.isFavorite = true;
            localNewsSource.updateIsFavorite(news);
        }
    }

    private void initImageBase64() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                news.base64ToImage = imageToBase64FromUrl();
                localNewsSource.updateBase64(news);
            }
        }).start();
    }

    private String imageToBase64FromUrl() {
        String base64Image = "default is no image";
        try {
            URL imageUrl = new URL(news.urlToImage);
            Bitmap bitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 20, baos);
            byte[] bytes = baos.toByteArray();
            base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64Image;
    }
}
