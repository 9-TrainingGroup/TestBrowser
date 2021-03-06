package com.alva.testbrowser.util;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import androidx.lifecycle.LiveData;

import com.alva.testbrowser.database.Bookmark;
import com.alva.testbrowser.database.History;
import com.alva.testbrowser.database.RecordRepository;

import java.util.ArrayList;
import java.util.List;

//存储相关数据和数据操作
public class RecordViewModel extends AndroidViewModel {
    public List<History> historyList = new ArrayList<>();
    public List<Bookmark> bookmarkList = new ArrayList<>();
    private RecordRepository recordRepository;

    public RecordViewModel(@NonNull Application application) {
        super(application);
        recordRepository = new RecordRepository(application);
    }

    LiveData<List<Bookmark>> getAllBookmarksLive() {
        return recordRepository.getAllBookmarksLive();
    }

    LiveData<List<History>> getAllHistoriesLive() {
        return recordRepository.getAllHistoriesLive();
    }

    /*书签管理*/
    public void insertBookmark(Bookmark bookmark) {
        recordRepository.insertBookmarks(bookmark);
    }

    public void updateBookmark(Bookmark bookmark) {
        recordRepository.updateBookmarks(bookmark);
    }

    public void deleteBookmark(Bookmark bookmark) {
        recordRepository.deleteBookmarks(bookmark);
    }

    public void deleteAllBookmarks() {
        recordRepository.deleteAllBookmarks();
    }

    public void getAllBookmarks() {
        bookmarkList = recordRepository.getAllBookmarks();
    }

    public void deleteSameBookmark(String url) {
        recordRepository.deleteSameBookmark(url);
    }

    /*历史管理*/
    public void insertHistory(History history) {
        recordRepository.insertHistory(history);
    }

    public void deleteHistory(History history) {
        recordRepository.deleteHistory(history);
    }

    public void deleteAllHistories() {
        recordRepository.deleteAllHistories();
    }

    public void getAllHistory() {
        historyList = recordRepository.getAllHistories();
    }

}
