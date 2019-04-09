package com.lionel.gonews.data;

/**
 * model class for news
 */
public class News {
    public Source source;
    public String author;
    public String title;
    public String description;
    public String url;
    public String urlToImage;
    public String publishedAt;
    public String content;

    public News(Source source, String author, String title, String description, String url, String urlToImage, String publishedAt, String content) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public class Source {
        public String id;
        public String name;

        public Source(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
