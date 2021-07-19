package com.alva.testbrowser.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alva.testbrowser.Adapter.HistoryAdapter;
import com.alva.testbrowser.R;
import com.alva.testbrowser.database.MyDatabase;
import com.alva.testbrowser.database.RecordViewModel;
/*历史记录列表展示及操作*/

public class HistoryFragment extends Fragment implements AdapterView.OnItemClickListener {
    MyDatabase dataBase;
    RecordViewModel recordViewModel;
    HistoryAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_layout, container, false);
        ListView listView = view.findViewById(R.id.history_view);

//        RecordViewModel recordViewModel = new ViewModelProvider(requireActivity()).get(RecordViewModel.class);

        recordViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(RecordViewModel.class);
        recordViewModel.getAllHistory();
        adapter = new HistoryAdapter(getActivity(), R.layout.simple_adapter_item,recordViewModel.historyList);
        listView.setAdapter(adapter);

/*      未创建ViewModel前的操作
        dataBase = MyDataBase.getMyDataBase(getActivity());
        HistoryDao historyDao = dataBase.historyDao();
        historyList = historyDao.getAll();
        HistoryAdapter adapter = new HistoryAdapter(getActivity(), R.layout.simple_adapter_item, historyList);
        listView.setAdapter(adapter);*/

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
        }
    }

//    recordViewModel.getAllHistoriesLive().observe(this, new Observer<List<History>>() {
//        @Override
//        public void onChanged(List<History> historyList) {
//            int temp = myAdapter1.getItemCount();
//            adapter.setAllWords(words);
//        }
//    });


}
