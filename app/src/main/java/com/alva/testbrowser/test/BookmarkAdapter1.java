package com.alva.testbrowser.test;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alva.testbrowser.R;
import com.alva.testbrowser.database.Bookmark;

import java.util.List;

public class BookmarkAdapter1 extends ArrayAdapter<Bookmark> {
    private int resourceID;
    private List<Bookmark> bookmarkList;

    public BookmarkAdapter1(@NonNull Context context, int resource, @NonNull List<Bookmark> objects) {
        super(context, resource, objects);
        resourceID = resource;
        bookmarkList=objects;
    }
    public void setBookmarkList(List<Bookmark> bookmarks){
        bookmarkList = bookmarks;
        Log.d("Adapter", String.valueOf(bookmarkList.size()));
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        bookmarkList.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        Bookmark bookmark = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
        TextView title = view.findViewById(R.id.ItemText1);
        TextView url = view.findViewById(R.id.ItemText2);
        title.setText(bookmark.getTitle());
        url.setText(bookmark.getUrl());
        return view;
    }
    public void clear(){
        notifyDataSetChanged();
    }
}
