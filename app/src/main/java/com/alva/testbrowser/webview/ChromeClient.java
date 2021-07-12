package com.alva.testbrowser.webview;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;

/**
 * @author Alva
 * @since 2021/7/11 21:36
 */
public class ChromeClient extends WebChromeClient {

    private WebViewExt.Callback mCallback;

    public ChromeClient(WebViewExt.Callback callback){
        mCallback = callback;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        mCallback.onReceivedTitle(view, title);
    }
}
