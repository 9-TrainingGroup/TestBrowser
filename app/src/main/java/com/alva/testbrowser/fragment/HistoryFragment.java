package com.alva.testbrowser.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alva.testbrowser.Adapter.HistoryAdapter;
import com.alva.testbrowser.R;
import com.alva.testbrowser.database.History;
import com.alva.testbrowser.database.MyDatabase;
import com.alva.testbrowser.database.RecordViewModel;
/*历史记录列表展示及操作*/

public class HistoryFragment extends Fragment{
    MyDatabase dataBase;
    RecordViewModel recordViewModel;
    HistoryAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_layout, container, false);
        ListView listView = view.findViewById(R.id.history_view);


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


        return view;
    }

    public void confirm(int position,ListView listView){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("DELETE");
        dialog.setMessage("确认删除吗");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
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
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }




}
