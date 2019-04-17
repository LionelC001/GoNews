package com.lionel.gonews.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * model class for news
 */
public class News implements Parcelable {
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

    public static final Creator CREATOR = new Creator() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(
                    (Source) in.readParcelable(Source.class.getClassLoader()),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString());
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(source, flags);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(urlToImage);
        dest.writeString(publishedAt);
        dest.writeString(content);
    }


    public static class Source implements Parcelable {
        public String id;
        public String name;

        public Source(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public static final Creator CREATOR = new Creator() {
            @Override
            public Source createFromParcel(Parcel in) {
                return new Source(in.readString(), in.readString());
            }

            @Override
            public Source[] newArray(int size) {
                return new Source[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
        }
    }
}
