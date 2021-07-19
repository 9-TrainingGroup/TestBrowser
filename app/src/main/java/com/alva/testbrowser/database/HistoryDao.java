package com.alva.testbrowser.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {
    /*添加历史记录*/
    @Insert
    void insertHistory(History... histories);

    /*删除历史记录*/
    @Delete
    void deleteHistory(History ... histories);

    /*删除所有历史记录*/
    @Query("DELETE FROM history")
    void deleteAllHistories();

    /*返回所有历史记录*/
    @Query("SELECT * FROM history")
    LiveData<List<History>> getAllHistories();

    @Query("SELECT * FROM history order by id desc")
    List<History> getAll();
}