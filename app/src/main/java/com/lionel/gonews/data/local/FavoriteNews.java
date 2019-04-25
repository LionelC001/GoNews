package com.lionel.gonews.data.local;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static com.lionel.gonews.util.Constants.TABLE_FAVORITE_NEWS;

@Entity(tableName = TABLE_FAVORITE_NEWS, indices = @Index(value = {"id", "title"}, unique = true))
public class FavoriteNews implements Serializable {

    @PrimaryKey
    public int id;

    public String sourceName;
    public String title;
    public String url;
    public String base64Image;
    public String publishedAt;
    public String content;

    public FavoriteNews(int id,String sourceName, String title, String url, String base64Image, String publishedAt, String content) {
        this.id = id;
        this.sourceName = sourceName;
        this.title = title;
        this.url = url;
        this.base64Image = base64Image;
        this.publishedAt = publishedAt;
        this.content = content;
    }
}
