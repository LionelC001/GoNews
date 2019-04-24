package com.lionel.gonews.data.local;


import com.lionel.gonews.data.News;

public class LocalNews extends News {
    public Source source;
    public String author;
    public String title;
    public String description;
    public String url;
    public String localToImage;
    public String publishedAt;
    public String content;
    public boolean isFavorite;
    public boolean isHistory;


    public LocalNews(Source source, String author, String title, String description, String url, String urlToImage, String publishedAt, String content) {
        super(source, author, title, description, url, urlToImage, publishedAt, content);
    }
}
