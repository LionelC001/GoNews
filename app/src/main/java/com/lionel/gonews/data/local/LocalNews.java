package com.lionel.gonews.data.local;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

import static com.lionel.gonews.util.Constants.TABLE_LOCAL_NEWS;

@Entity(tableName = TABLE_LOCAL_NEWS)
public class LocalNews implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "_id")
    public int id;

    public String sourceName;
    public String title;
    public String url;
    public String localToImage;
    public String publishedAt;
    public String content;
    public boolean isHistory;
    public boolean isFavorite;

    public LocalNews(String sourceName, String title, String url, String localToImage, String publishedAt, String content, boolean isHistory, boolean isFavorite) {
        this.sourceName = sourceName;
        this.title = title;
        this.url = url;
        this.localToImage = localToImage;
        this.publishedAt = publishedAt;
        this.content = content;
        this.isHistory = isHistory;
        this.isFavorite = isFavorite;
    }
}
