package com.lionel.gonews.data.local;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.lionel.gonews.data.News;

import java.io.Serializable;

import static com.lionel.gonews.util.Constants.COLUMN_FAVORITE;
import static com.lionel.gonews.util.Constants.COLUMN_HISTORY;
import static com.lionel.gonews.util.Constants.COLUMN_TITLE;
import static com.lionel.gonews.util.Constants.TABLE_LOCAL_NEWS;

@Entity(tableName = TABLE_LOCAL_NEWS)
public class LocalNews extends News implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @Embedded
    public Source source;

    public String author;

    @ColumnInfo(name = COLUMN_TITLE)
    public String title;

    public String description;
    public String url;
    public String localToImage;
    public String publishedAt;
    public String content;

    @ColumnInfo(name = COLUMN_HISTORY)
    public boolean isHistory;

    @ColumnInfo(name = COLUMN_FAVORITE)
    public boolean isFavorite;


    public LocalNews(Source source, String author, String title, String description, String url, String urlToImage, String publishedAt, String content, boolean isHistory, boolean isFavorite) {
        super(source, author, title, description, url, urlToImage, publishedAt, content);

        this.isHistory = isHistory;
        this.isFavorite = isFavorite;
    }
}
