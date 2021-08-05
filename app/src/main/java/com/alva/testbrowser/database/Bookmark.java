package com.alva.testbrowser.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmark")
public class Bookmark {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    private int bookmarkid;
    @ColumnInfo
    private String title;
    @ColumnInfo
    private String url;

    public Bookmark(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getBookmarkid() {
        return bookmarkid;
    }

    public void setBookmarkid(int bookmarkid) {
        this.bookmarkid = bookmarkid;
    }
}
