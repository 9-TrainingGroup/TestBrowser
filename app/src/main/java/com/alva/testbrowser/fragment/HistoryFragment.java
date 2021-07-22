package com.alva.testbrowser.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alva.testbrowser.Adapter.HistoryAdapter;
import com.alva.testbrowser.R;
import com.alva.testbrowser.database.History;
import com.alva.testbrowser.database.MyDatabase;
import com.alva.testbrowser.database.RecordViewModel;

import org.jetbrains.annotations.NotNull;
/*历史记录列表展示及操作*/

public class HistoryFragment extends Fragment{
    MyDatabase dataBase;
    RecordViewModel recordViewModel;
    HistoryAdapter adapter;
    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_layout, container, false);
        listView = view.findViewById(R.id.history_view);
        ImageButton button = view.findViewById(R.id.historyDelete);


        recordViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(RecordViewModel.class);
        recordViewModel.getAllHistory();

        adapter = new HistoryAdapter(getActivity(), R.layout.simple_adapter_item,recordViewModel.historyList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                History history = recordViewModel.historyList.get(position);
                getActivity().setResult(Activity.RESULT_OK,new Intent().putExtra("open_url",history.getUrl()));
                getActivity().finish();
            }
        });

/*
        //长按监听
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                History history = recordViewModel.historyList.get(position);
                //弹出对话框
                confirm(position,listView);
                return true;
            }
        });
*/

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                getActivity().getMenuInflater().inflate(R.menu.menu_history,menu);
                HistoryFragment.super.onCreateContextMenu(menu,v,menuInfo);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity());
                dialogbuilder.setTitle("DELETE");
                dialogbuilder.setMessage("确认删除所有历史记录吗");
                dialogbuilder.setCancelable(false);
                dialogbuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    //按了确定后删除数据，并且刷新列表
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recordViewModel.deleteAllHistories();
                        adapter.clear();
                    }
                });
                dialogbuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = dialogbuilder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });


        return view;
    }

    public void confirm(int position,ListView listView){
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity());
        dialogbuilder.setTitle("DELETE");
        dialogbuilder.setMessage("确认删除吗");
        dialogbuilder.setCancelable(false);
        dialogbuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            //按了确定后删除数据，并且刷新列表
            @Override
            public void onClick(DialogInterface dialog, int which) {
                History history = recordViewModel.historyList.get(position);
                Log.d("output_title",history.getTitle());
                recordViewModel.deleteHistory(history);
                Log.d("delete_url",history.getUrl());
                recordViewModel.getAllHistory();
                //从列表中删除
                adapter.deleteItem(position);
                Log.d("dele_length", String.valueOf(recordViewModel.historyList.size()));
                listView.setAdapter(adapter);
            }
        });
        dialogbuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = dialogbuilder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }


    @Override
    public boolean onContextItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getMenuInfo() instanceof AdapterView.AdapterContextMenuInfo){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            switch (item.getItemId()){
                case R.id.deleteItem:
                    History history = recordViewModel.historyList.get(info.position);
                    confirm(info.position,listView);
            }
        }
        return super.onContextItemSelected(item);
    }



}
