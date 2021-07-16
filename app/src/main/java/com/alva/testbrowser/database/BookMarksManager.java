package com.alva.testbrowser.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class BookMarksManager {
    private static final String DEG_TAG = "testbrowser_BMsManager";

    private BookMarkDao database;
    private boolean flag = false;
    private Cursor resultMap;

    public BookMarksManager(Context context){
        this.database = new BookMarkDaoImp(context, "favorite", null, 1);
    }

    /**
     * 增加书签
     * @param	name	书签名
     * @param	url		书签地址
     * */
    public boolean addBookMark(final String name, final String url) {
        flag = false;
        this.database.transactionAround(false, new CallBack() {

            @Override
            public void doSomething(SQLiteDatabase sqLiteDatabase) {
                boolean ifmultiply = database.multiplyBookMark(sqLiteDatabase, url);
                if(!ifmultiply){
                    Log.d(DEG_TAG, "reason:未存在相同书签");
                    flag = database.addBookMark(sqLiteDatabase, name, url);
                }else{
                    Log.d(DEG_TAG, "reason:已经存在相同书签");
                    flag = false;
                }
            }
        });
        Log.d(DEG_TAG, "result:"+flag);
        return flag;
    }

    /**
     * 删除书签
     * @param	id		书签ID
     * */
    public boolean deleteBookMark(final String id) {
        flag = false;
        this.database.transactionAround(false, new CallBack() {

            @Override
            public void doSomething(SQLiteDatabase sqLiteDatabase) {
                flag = database.deleteBookMark(sqLiteDatabase, id);
            }
        });
        return flag;
    }

    /**
     * 修改书签
     * @param	id		修改的书签ID
     * @param	name	修改后的书签名
     * @param	url		修改后的书签地址
     * */
    public boolean modifyFavorite(final String id, final String name, final String url) {
        flag = false;
        this.database.transactionAround(false, new CallBack() {

            @Override
            public void doSomething(SQLiteDatabase sqLiteDatabase) {
                flag = database.modifyBookMark(sqLiteDatabase, id, name, url);
            }
        });
        return flag;
    }

    /**
     * 获取所有书签
     * @return	HashMap<String, String>
     * */
    public Cursor getAllBookMarks() {
        this.database.transactionAround(true, new CallBack() {

            @Override
            public void doSomething(SQLiteDatabase sqLiteDatabase) {
                resultMap = database.getAllBookMarks(sqLiteDatabase);
            }
        });
        return resultMap;
    }
}
