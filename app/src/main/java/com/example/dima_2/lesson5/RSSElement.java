package com.example.dima_2.lesson5;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dima_2 on 08.01.2015.
 */
public class RSSElement implements Parcelable {
    public String title;
    public String description;
    public String link;
    public String pubDate;

    public RSSElement() {
        //nothing to be done
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemsTable.COLUMN_TITLE, title);
        contentValues.put(ItemsTable.COLUMN_DESCRIPTION, description);
        contentValues.put(ItemsTable.COLUMN_LINK, link);
        contentValues.put(ItemsTable.COLUMN_PUB_DATE, pubDate);
        return contentValues;
    }

    public RSSElement(String title, String description, String link, String pubDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
    }

    private RSSElement(Parcel in) {
        title = in.readString();
        description = in.readString();
        link = in.readString();
        pubDate = in.readString();
    }

    public static final Parcelable.Creator<RSSElement> CREATOR = new Parcelable.Creator<RSSElement>() {
        @Override
        public RSSElement createFromParcel(Parcel source) {
            return new RSSElement(source);
        }

        @Override
        public RSSElement[] newArray(int size) {
            return new RSSElement[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(link);
        parcel.writeString(pubDate);
    }
}