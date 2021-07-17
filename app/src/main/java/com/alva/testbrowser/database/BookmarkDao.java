package com.alva.testbrowser.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface BookmarkDao {

    /*添加网址记录
    * url不可重复*/
    @Insert
    void insertAll(Bookmark bookmark);

    /*更新网址记录
    * 参数：title*/
    @Update
    void updateTitle(Bookmark bookmark);

    /*删除某个网址记录*/
    @Delete
    void delete(Bookmark bookmarks);

//    /*删除全部网址记录*/
//    @Delete
//    void deleteAll();
}
