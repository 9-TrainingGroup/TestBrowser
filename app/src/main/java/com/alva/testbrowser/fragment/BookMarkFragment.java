package com.alva.testbrowser.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alva.testbrowser.adapter.BookmarkAdapter;
import com.alva.testbrowser.R;
import com.alva.testbrowser.database.Bookmark;
import com.alva.testbrowser.database.MyDatabase;
import com.alva.testbrowser.util.RecordViewModel;

import java.util.ArrayList;
import java.util.List;

public class BookMarkFragment extends Fragment {
    MyDatabase dataBase;
    RecordViewModel recordViewModel;
    BookmarkAdapter adapter;
    private List<Bookmark> bookmarkList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookmark_layout, container, false);
        ListView listView = view.findViewById(R.id.bookmark_view);
        ImageButton button = view.findViewById(R.id.bookmarkDelete);

        this.recordViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(RecordViewModel.class);
        this.recordViewModel.getAllBookmarks();
        //BookmarkAdapter1 adapter = new BookmarkAdapter1(getActivity(), R.layout.simple_adapter_item, this.recordViewModel.bookmarkList);
        adapter = new BookmarkAdapter(getActivity(), R.layout.simple_adapter_item, recordViewModel.bookmarkList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bookmark bookmark = recordViewModel.bookmarkList.get(position);
                getActivity().setResult(Activity.RESULT_OK, new Intent().putExtra("open_url", bookmark.getUrl()));
                getActivity().finish();
            }
        });

        //长按监听
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Bookmark bookmark = recordViewModel.bookmarkList.get(position);
                Log.d("longClick", bookmark.getTitle());
                //弹出菜单
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_bookmark, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.editItem:
                                optionEdit(position, listView);
                                break;
                            case R.id.deleteItem:
                                optionDelete(position, listView);
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
                        }
                        return true;
                    }
                });
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(getActivity());
                dialogbuilder.setTitle("DELETE");
                dialogbuilder.setMessage("确认删除所有书签吗");
                dialogbuilder.setCancelable(false);
                dialogbuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    //按了确定后删除数据，并且刷新列表
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recordViewModel.deleteAllBookmarks();
                        adapter.deleteAllBookmark();
                        adapter.notifyDataSetChanged();
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

    public void optionDelete(int position, ListView listView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("DELETE");
        builder.setMessage("是否删除此书签？");
        builder.setCancelable(false);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            //按了确定后删除数据，并且刷新列表
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bookmark bookmark = recordViewModel.bookmarkList.get(position);
//                Log.d("output_title",bookmark.getTitle());
                recordViewModel.deleteBookmark(bookmark);
//                Log.d("delete_url",bookmark.getUrl());
                recordViewModel.getAllBookmarks();
                //从列表中删除
                adapter.deleBookmarkItem(position);
//                Log.d("dele_length", String.valueOf(recordViewModel.bookmarkList.size()));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLUE);
    }

    public void optionEdit(int position, ListView listView) {
        final EditText editText = new EditText(getActivity());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), 3);
        builder.setTitle("请输入新标题");
        builder.setView(editText);
        editText.setText(recordViewModel.bookmarkList.get(position).getTitle());
        //单点全选
        editText.setSelectAllOnFocus(true);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = editText.getText().toString();
                adapter.updateBookmarkItem(position, title);
                Bookmark bookmark = recordViewModel.bookmarkList.get(position);
                bookmark.setTitle(title);
                recordViewModel.updateBookmark(bookmark);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

}
