package com.alva.testbrowser.database;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {History.class},version = 1,exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {
    public abstract HistoryDao historyDao();
}
