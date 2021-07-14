package com.alva.testbrowser.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alva.testbrowser.R;

/**
 * @author Alva
 * @since 2021/7/11 17:21
 */
public class WebViewExt extends WebView {

    private ProgressView progressView;

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
        progressView = new ProgressView(getContext());
        progressView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(getContext(), 4)));
        progressView.setColor(R.color.cyan);
        progressView.setProgress(10);
        addView(progressView);

        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        setWebViewClient(new WebClient());
        setWebChromeClient(new ChromeClient(callback,progressView));
    }

    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public interface Callback{
        void onReceivedTitle(WebView view, String title);
    }
}
