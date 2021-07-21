package com.alva.testbrowser.ui;

import android.webkit.WebView;
import android.widget.EditText;

import com.alva.testbrowser.webview.WebViewExt;

/**
 * @author Alva
 * @since 2021/7/11 21:41
 */
public class UrlBarController implements WebViewExt.Callback {

   private EditText mEdit;

    public UrlBarController(EditText editText){
        mEdit = editText;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        mEdit.setText(view.getUrl());
    }
}
