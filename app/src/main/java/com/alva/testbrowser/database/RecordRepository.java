package com.alva.testbrowser.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RecordRepository {

    private final LiveData<List<Bookmark>> allBookmarksLive;
    private final LiveData<List<History>> allHistoriesLive;
    private final BookmarkDao bookmarkDao;
    private final HistoryDao historyDao;
    private final List<Bookmark> allBookmarks;
    private final List<History> allHistories;

    public RecordRepository(Context context) {
        MyDatabase myDatabase = MyDatabase.getMyDatabase(context.getApplicationContext());
        bookmarkDao = myDatabase.bookmarkDao();
        historyDao = myDatabase.historyDao();
        allBookmarksLive = bookmarkDao.getAllBookmarks();
        allHistoriesLive = historyDao.getAllHistories();
        allBookmarks = bookmarkDao.getAll();
        allHistories = historyDao.getAll();
    }

    public LiveData<List<Bookmark>> getAllBookmarksLive() {
        return allBookmarksLive;
    }

    public LiveData<List<History>> getAllHistoriesLive() {
        return allHistoriesLive;
    }

    public List<Bookmark> getAllBookmarks() {
        return allBookmarks;
    }

    public List<History> getAllHistories() {
        return allHistories;
    }

    public void insertBookmarks(Bookmark bookmark) {
        MyDatabase.databaseWriteExecutor.execute(() -> {
            bookmarkDao.insertBookmark(bookmark);
        });
    }

    public void updateBookmarks(Bookmark bookmark) {
        MyDatabase.databaseWriteExecutor.execute(() -> {
            bookmarkDao.updateTitle(bookmark);
        });
    }

    public void deleteBookmarks(Bookmark bookmark) {
        MyDatabase.databaseWriteExecutor.execute(() -> {
            bookmarkDao.deleteBookmark(bookmark);
        });
        ;
    }

    public void deleteAllBookmarks() {
        MyDatabase.databaseWriteExecutor.execute(() -> {
            bookmarkDao.deleteAllBookmarks();
        });
    }

    public void deleteSameBookmark(String url) {
        MyDatabase.databaseWriteExecutor.execute(() -> {
            bookmarkDao.deleteSameBookmark(url);
        });
    }

    public void insertHistory(History history) {
        MyDatabase.databaseWriteExecutor.execute(() -> {
            historyDao.insertHistory(history);
        });
    }

    public void deleteHistory(History history) {
        MyDatabase.databaseWriteExecutor.execute(() -> {
            historyDao.deleteHistory(history);
        });
    }

    public void deleteAllHistories() {
        MyDatabase.databaseWriteExecutor.execute(() -> {
            historyDao.deleteAllHistories();
        });
    }


}
