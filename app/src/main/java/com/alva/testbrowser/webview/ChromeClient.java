package com.alva.testbrowser.webview;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.alva.testbrowser.database.History;
import com.alva.testbrowser.database.MyDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        String url = view.copyBackForwardList().getCurrentItem().getUrl();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        History history = new History(title,url,simpleDateFormat.format(date));
        MyDatabase database = MyDatabase.getMyDatabase();
        database.historyDao().deletesameHistory(history.getUrl(),history.getTime());
        database.historyDao().insertHistory(history);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        progressView.setProgress(newProgress);
        if (newProgress == 100) {
            //加载完毕进度条消失
            progressView.setVisibility(View.GONE);
        } else {
            //更新进度
            progressView.setVisibility(View.VISIBLE);
        }
    }
}
