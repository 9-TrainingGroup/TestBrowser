package com.alva.testbrowser.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alva.testbrowser.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alva
 * @since 2021/7/15 22:35
 */
public class CompleteAdapter extends BaseAdapter implements Filterable {

    private class CompleteFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            if (prefix == null) {
                return new FilterResults();
            }

            resultList.clear();
            for (CompleteItem item : originalList) {
                if (item.getTitle().contains(prefix) || item.getTitle().toLowerCase().contains(prefix) || item.getURL().contains(prefix)) {
                    if (item.getTitle().contains(prefix) || item.getTitle().toLowerCase().contains(prefix)) {
                        item.setIndex(item.getTitle().indexOf(prefix.toString()));
                    } else if (item.getURL().contains(prefix)) {
                        item.setIndex(item.getURL().indexOf(prefix.toString()));
                    }
                    resultList.add(item);
                }
            }

            Collections.sort(resultList, (first, second) -> Integer.compare(first.getIndex(), second.getIndex()));

            FilterResults results = new FilterResults();
            results.values = resultList;
            results.count = resultList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    }

    private static class CompleteItem {
        private final String title;
        private final Long time;

        String getTitle() {
            return title;
        }

        private final String url;

        String getURL() {
            return url;
        }

        private int index = Integer.MAX_VALUE;

        int getIndex() {
            return index;
        }

        long getTime() {
            return time;
        }

        void setIndex(int index) {
            this.index = index;
        }

        private CompleteItem(String title, String url, Long time) {
            this.title = title;
            this.url = url;
            this.time = time;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof CompleteItem)) {
                return false;
            }

            CompleteItem item = (CompleteItem) object;
            return item.getTitle().equals(title) && item.getURL().equals(url);
        }

        @Override
        public int hashCode() {
            if (title == null || url == null) {
                return 0;
            }

            return title.hashCode() & url.hashCode();
        }
    }

    private static class Holder {
        private ImageView iconView;
        private TextView titleView;
        private TextView urlView;
    }

    private final Context context;
    private final int layoutResId;
    private final List<CompleteItem> originalList;
    private final List<CompleteItem> resultList;
    private final CompleteFilter filter = new CompleteFilter();

    /**
     *
     * @param recordList String为占位符,实际需要包含title,url,time的自定义数据类型
     */
    public CompleteAdapter(Context context, int layoutResId, List<String> recordList) {
        this.context = context;
        this.layoutResId = layoutResId;
        this.originalList = new ArrayList<>();
        this.resultList = new ArrayList<>();
        getRecordList(recordList);
    }

    private void getRecordList(List<String> recordList) {
        //TODO 将recordList中的数据导入originalList中
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int i) {
        return resultList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        Holder holder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(layoutResId, null, false);
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.primaryDarkColor));
            holder = new Holder();
            holder.titleView = view.findViewById(R.id.record_item_title);
            holder.titleView.setTextColor(Color.WHITE);
            holder.urlView = view.findViewById(R.id.record_item_time);
            holder.iconView = view.findViewById(R.id.record_item_icon);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        CompleteItem item = resultList.get(i);
        holder.titleView.setText(item.title);
        holder.urlView.setVisibility(View.GONE);
        holder.urlView.setText(item.url);

        //TODO 通过time设置iconView中的图标

        holder.iconView.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
}
