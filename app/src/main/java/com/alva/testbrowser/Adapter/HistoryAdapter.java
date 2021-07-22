package com.alva.testbrowser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alva.testbrowser.R;
import com.alva.testbrowser.database.History;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<History> {
    private int resourceID;
    private List<History> historyList;

    public HistoryAdapter(@NonNull Context context, int resource, @NonNull List<History> objects) {
        super(context, resource, objects);
        resourceID = resource;
        historyList = objects;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        History history = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
        TextView title = view.findViewById(R.id.ItemText1);
        TextView url = view.findViewById(R.id.ItemText2);
        title.setText(history.getTitle());
        url.setText(history.getUrl());
        return view;
    }

    //删除指定项并刷新listview
    public void deleteItem(int position){
        historyList.remove(position);
        notifyDataSetChanged();
    }

    public void clear(){
        historyList.clear();
        notifyDataSetChanged();
    }
}


