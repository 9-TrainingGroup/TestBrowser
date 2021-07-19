package com.alva.testbrowser.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Bookmark.class, History.class},version = 1,exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {
    public abstract HistoryDao historyDao();
    public abstract BookmarkDao bookmarkDao();

    private static MyDataBase myDataBase;
    public static MyDataBase getMyDataBase(Context context){
        if (myDataBase == null){
            myDataBase= Room.databaseBuilder(context,MyDataBase.class,"myDatabase")
                    .allowMainThreadQueries().build();
        }

        return myDataBase;
    }
}
