package com.lionel.gonews.data.local;


import android.arch.persistence.room.Entity;

import com.lionel.gonews.data.News;

import java.io.Serializable;

import static com.lionel.gonews.util.Constants.TABLE_FAVORITE_NEWS;

@Entity(tableName = TABLE_FAVORITE_NEWS, inheritSuperIndices = true)
public class FavoriteNews extends News implements Serializable {

    public String sourceName;
    public String base64Image;
    public String localContent;

    public FavoriteNews(int id, String sourceName, String title, String url, String base64Image, String publishedAt, String localContent) {
        super(id, null, null, title, null, url, null, publishedAt, null);
        this.sourceName = sourceName;
        this.base64Image = base64Image;
        this.localContent = localContent;
    }
}
