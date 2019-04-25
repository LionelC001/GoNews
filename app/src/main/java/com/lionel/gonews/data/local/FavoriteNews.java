package com.lionel.gonews.data.local;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static com.lionel.gonews.util.Constants.TABLE_FAVORITE_NEWS;

@Entity(tableName = TABLE_FAVORITE_NEWS, indices = @Index(value = {"title"}, unique = true))
public class FavoriteNews implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int id;

    public String sourceName;
    public String title;
    public String url;
    public String base64Image;
    public String publishedAt;
    public String content;

    public FavoriteNews(String sourceName, String title, String url, String base64Image, String publishedAt, String content) {
        this.sourceName = sourceName;
        this.title = title;
        this.url = url;
        this.base64Image = base64Image;
        this.publishedAt = publishedAt;
        this.content = content;
    }
}
