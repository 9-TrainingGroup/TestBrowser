package com.alva.testbrowser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alva.testbrowser.R;
import com.alva.testbrowser.database.Bookmark;

import java.util.List;

public class BookmarkAdapter extends ArrayAdapter<Bookmark> {
    private int resourceID;

    public BookmarkAdapter(Context context, int resource, List<Bookmark> objects) {
        super(context, resource, objects);
        resourceID = resource;
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
}
