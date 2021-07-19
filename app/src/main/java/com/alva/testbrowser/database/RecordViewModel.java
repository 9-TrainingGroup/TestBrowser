package com.alva.testbrowser.database;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

//存储相关数据和数据操作
public class RecordViewModel extends AndroidViewModel {
    private MyDataBase dataBase;
    public List<History> historyList = new ArrayList<>();
    public List<Bookmark> bookmarkList = new ArrayList<>();
    public BookmarkDao bookmarkDao;
    public HistoryDao historyDao;

    public RecordViewModel(@NonNull @NotNull Application application) {
        super(application);
        dataBase = MyDataBase.getMyDataBase(application);
        bookmarkDao = dataBase.bookmarkDao();
        historyDao = dataBase.historyDao();
    }

    public void insertBookmark(Bookmark bookmark){
        bookmarkDao.insertAll(bookmark);
    }

    public void insertHistory(History history){
        historyDao.insert(history);
    }

    public void deleBookmark(Bookmark bookmark){
        bookmarkDao.delete(bookmark);
    }

    public void deleteHistory(History history){
        historyDao.delete(history);
    }

    public void getAllHistory(){
        historyList = historyDao.getAll();
    }

    //获取数据库所有书签
    public void getALLBookmark(){
        //未添加dao方法
    }

    public void deleteAllHistory(){
        historyDao.deleteAll();
    }

    public void deleteAllBookmark(){

    }

}
