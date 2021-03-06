package com.alva.testbrowser.webview;

import android.view.View;

public interface AlbumController {
    View getAlbumView();

    void activate();

    void deactivate();

    int getIndex();

    void setIndex(int Index);
}
