package com.alva.testbrowser.webview;

import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;

/**
 * @author Alva
 * @since 2021/7/11 21:36
 */
public class ChromeClient extends WebChromeClient {

    private WebViewExt.Callback mCallback;
    private ProgressView progressView;

    public ChromeClient(WebViewExt.Callback callback, ProgressView progressView) {
        mCallback = callback;
        this.progressView = progressView;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        mCallback.onReceivedTitle(view, title);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        progressView.setProgress(newProgress);
        if (newProgress == 100) {
            //加载完毕进度条消失
            view.onResume();
            progressView.setVisibility(View.GONE);
        } else {
            //更新进度
            view.onPause();
            progressView.setVisibility(View.VISIBLE);
        }
    }
}
