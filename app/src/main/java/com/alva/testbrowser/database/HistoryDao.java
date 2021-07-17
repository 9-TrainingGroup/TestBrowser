package com.alva.testbrowser.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {

    @Query("SELECT * FROM history")
    List<History> getAll();

    @Insert
    void insertAll(History... histories);

    @Delete
    void delete(History history);
}