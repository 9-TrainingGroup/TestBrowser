package com.alva.testbrowser.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alva.testbrowser.R;

class AlbumItem {

    private final Context context;
    private final AlbumController albumController;
    public int index;
    private ImageView albumClose;
    private View albumView;
    private TextView albumTitle;
    private BrowserController browserController;

    AlbumItem(Context context, AlbumController albumController, int index) {
        this.context = context;
        this.albumController = albumController;
        this.index = index;
        initUI();
    }

    View getAlbumView() {
        return albumView;
    }

    void setAlbumTitle(String title) {
        albumTitle.setText(title);
    }

    void setBrowserController(BrowserController browserController) {
        this.browserController = browserController;
    }

    @SuppressLint("InflateParams")
    private void initUI() {
        albumView = LayoutInflater.from(context).inflate(R.layout.item_icon_right, null, false);

        albumView.setOnLongClickListener(v -> {
            return true;
        });
        albumClose = albumView.findViewById(R.id.whitelist_item_cancel);
        albumClose.setVisibility(View.VISIBLE);
        albumClose.setOnClickListener(view -> browserController.removeAlbum(albumController));
        albumTitle = albumView.findViewById(R.id.whitelist_item_domain);
    }

    public void activate() {
        albumTitle.setTextColor(ContextCompat.getColor(context, R.color.active));
        albumClose.setImageResource(R.drawable.icon_close_light);
    }

    public void deactivate() {
        albumTitle.setTextColor(ContextCompat.getColor(context, R.color.black));
        albumClose.setImageResource(R.drawable.icon_close_light);
        albumView.setOnClickListener(view -> browserController.showAlbum(albumController));
    }
}