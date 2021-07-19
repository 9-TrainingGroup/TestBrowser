package com.alva.testbrowser.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.alva.testbrowser.Adapter.HistoryAdapter;
import com.alva.testbrowser.R;
import com.alva.testbrowser.database.History;
import com.alva.testbrowser.database.HistoryDao;
import com.alva.testbrowser.database.MyDataBase;

import java.util.ArrayList;
import java.util.List;
/*历史记录列表展示及操作*/

public class HistoryFragment extends Fragment implements AdapterView.OnItemClickListener {
    private List<History> historyList = new ArrayList<>();
    MyDataBase dataBase;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_layout, container, false);
        ListView listView = view.findViewById(R.id.history_view);

        dataBase = MyDataBase.getMyDataBase(getActivity());
        HistoryDao historyDao = dataBase.historyDao();
        historyList = historyDao.getAll();
        HistoryAdapter adapter = new HistoryAdapter(getActivity(), R.layout.simple_adapter_item, historyList);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
        }
    }

}
