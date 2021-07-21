package com.alva.testbrowser.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface BookmarkDao {

    /*添加书签
    * url不可重复*/
    @Insert
    void insertBookmark(Bookmark ... bookmarks);

    /*更改书签title
    * 参数：title*/
    @Update
    void updateTitle(Bookmark ... bookmarks);

    /*删除此书签*/
    @Delete
    void deleteBookmark(Bookmark ... bookmarks);

    /*删除全部书签*/
    @Query("delete from bookmark")
    void deleteAllBookmarks();

    /*返回所有书签*/
    @Query("select * from bookmark")
    LiveData<List<Bookmark>> getAllBookmarks();

    /*返回所有书签*/
    @Query("select * from bookmark order by bookmarkid desc")
    List<Bookmark> getAll();

    /*删除该url的书签记录*/
    @Query("delete from bookmark where url==:url")
    void deleteSameBookmark(String url);
}
