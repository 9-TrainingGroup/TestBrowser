package com.alva.testbrowser.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BookMarkDaoImp extends SQLiteOpenHelper implements  BookMarkDao{

    private static final String DEG_TAG = "webBrowser_SQLManager";

    public BookMarkDaoImp(Context context, String name, CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL(SQLStr.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public boolean addBookMark(SQLiteDatabase sqLiteDatabase, String name, String url) throws SQLException{
        ContentValues bookmark = new ContentValues();
        bookmark.put("name", name);
        bookmark.put("url", url);
        long id = sqLiteDatabase.insert("bookmark", null, bookmark);
        if(id!=-1){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean deleteBookMark(SQLiteDatabase sqLiteDatabase, String id) {
        Log.d(DEG_TAG, "deleteId:"+id);
        int number = sqLiteDatabase.delete("bookmark", "id=?", new String[]{id});
        Log.d(DEG_TAG, "delete_result:"+number);
        if(number!=0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean modifyBookMark(SQLiteDatabase sqLiteDatabase, String id, String name, String url) {
        ContentValues bookmark = new ContentValues();
        bookmark.put("name", name);
        bookmark.put("url", url);
        Log.d(DEG_TAG, "id:"+id+",name:"+name+",url:"+url);
        int number = sqLiteDatabase.update("bookmark", bookmark, "id=?", new String[]{id});
        Log.d(DEG_TAG, "number:"+number);
        if(number!=0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Cursor getAllBookMarks(SQLiteDatabase sqLiteDatabase) {
        String[] returnColmuns = new String[]{
                "id as _id",
                "name",
                "url"
        };
        Cursor result = sqLiteDatabase.query("bookmark", returnColmuns, null, null, null, null, "id");
        while(result.moveToNext()){
            String id = String.valueOf(result.getInt(result.getColumnIndex("_id")));
            String name = result.getString(result.getColumnIndex("name"));
            String url = result.getString(result.getColumnIndex("url"));
            Log.d(DEG_TAG, "id:"+id+",name:"+name+",url:"+url);
        }
        return result;
    }

    @Override
    public boolean multiplyBookMark(SQLiteDatabase sqLiteDatabase, String url) {
        Cursor result = sqLiteDatabase.query("bookmark", null, "url=?", new String[]{url}, null, null, null);
        while(result.moveToNext()){
            Log.d(DEG_TAG, "multiply:[id:"+String.valueOf(result.getInt(result.getColumnIndex("id"))
                    +",name:"+result.getString(result.getColumnIndex("name"))
                    +",url:"+result.getString(result.getColumnIndex("url"))));
        }
        if(result.getCount()>0){
            result.close();
            return true;
        }else{
            result.close();
            return false;
        }
    }

    @Override
    public void transactionAround(boolean readOnly, CallBack callback) {
        SQLiteDatabase sqLiteDatabase = null;
        if(readOnly){
            sqLiteDatabase = this.getReadableDatabase();
        }else{
            sqLiteDatabase = this.getWritableDatabase();
        }
        sqLiteDatabase.beginTransaction();
        callback.doSomething(sqLiteDatabase);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }
}
