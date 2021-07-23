package com.alva.testbrowser.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Bookmark.class, History.class},version = 1,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract HistoryDao historyDao();
    public abstract BookmarkDao bookmarkDao();

    private static final int NUMBER_OF_THREAD = 5;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREAD);

    private static MyDatabase myDatabase;
    public static MyDatabase getMyDatabase(){
        return myDatabase;
    }
    public static MyDatabase getMyDatabase(Context context){
        if (myDatabase == null){
            myDatabase= Room.databaseBuilder(context, MyDatabase.class,"myDatabase")
                    .allowMainThreadQueries().build();
        }

        return myDatabase;
    }
}
