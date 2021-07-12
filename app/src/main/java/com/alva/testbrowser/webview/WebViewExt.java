package com.alva.testbrowser.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Alva
 * @since 2021/7/11 17:21
 */
public class WebViewExt extends WebView {


    public WebViewExt(@NonNull Context context) {
        super(context);
    }

    public WebViewExt(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WebViewExt(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WebViewExt(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void init(Callback callback){
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        setWebViewClient(new WebClient());
        setWebChromeClient(new ChromeClient(callback));
    }

    public interface Callback{
        void onReceivedTitle(WebView view, String title);
    }
}
