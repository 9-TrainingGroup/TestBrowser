package com.alva.testbrowser.adapter;

import android.content.Context;
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
import com.alva.testbrowser.database.Bookmark;
import com.alva.testbrowser.database.History;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alva
 * @since 2021/7/15 22:35
 */
public class CompleteAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private final int layoutResId;
    private final List<CompleteItem> originalList;
    private final List<CompleteItem> resultList;
    private final CompleteFilter filter = new CompleteFilter();
    public CompleteAdapter(Context context, int layoutResId, List<History> historyList, List<Bookmark> bookmarkList) {
        this.context = context;
        this.layoutResId = layoutResId;
        this.originalList = new ArrayList<>();
        this.resultList = new ArrayList<>();
        getRecordList(historyList, bookmarkList);
    }

    private void getRecordList(List<History> historyList, List<Bookmark> bookmarkList) {
        for (History a : historyList) {
            CompleteItem completeItem = new CompleteItem(a.getTitle(), a.getUrl(), a.getTime());
            originalList.add(completeItem);
        }
        for (Bookmark a : bookmarkList) {
            CompleteItem completeItem = new CompleteItem(a.getTitle(), a.getUrl(), "BookMark");
            originalList.add(completeItem);
        }
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
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder = new Holder();
            holder.titleView = view.findViewById(R.id.record_item_title);
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

        if (item.getTime().equals("BookMark")) {
            holder.iconView.setImageResource(R.drawable.button_menu);
        } else {
            holder.iconView.setImageResource(R.drawable.button_history);
        }
        holder.iconView.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private static class CompleteItem {
        private final String title;
        private final String time;
        private final String url;
        private int index = Integer.MAX_VALUE;

        private CompleteItem(String title, String url, String time) {
            this.title = title;
            this.url = url;
            this.time = time;
        }

        String getTitle() {
            return title;
        }

        String getURL() {
            return url;
        }

        int getIndex() {
            return index;
        }

        void setIndex(int index) {
            this.index = index;
        }

        String getTime() {
            return time;
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
}
