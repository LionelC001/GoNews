package com.lionel.gonews.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static com.lionel.gonews.util.Constants.TABLE_HISTORY_NEWS;

/**
 * represent model class for news
 * and entity for historyNews
 */

@Entity(tableName = TABLE_HISTORY_NEWS, indices = @Index(value = {"title"}, unique = true))
public class News implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @Embedded(prefix = "source_")
    public Source source;
    public String author;
    public String title;
    public String description;
    public String url;
    public String urlToImage;
    public String publishedAt;
    public String content;


    public News(int id, Source source, String author, String title, String description, String url, String urlToImage, String publishedAt, String content) {
        this.id = id;
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public static class Source implements Serializable {
        public String id;
        public String name;

        public Source(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
