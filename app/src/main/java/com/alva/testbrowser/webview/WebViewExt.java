package com.alva.testbrowser.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alva.testbrowser.R;

import java.util.Map;

/**
 * @author Alva
 * @since 2021/7/11 17:21
 */
public class WebViewExt extends WebView implements AlbumController{

    private ProgressView progressView;
    private AlbumItem album;
    private BrowserController browserController = null;
    private boolean foreground;

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
    public void init(Callback callback,int index) {
        progressView = new ProgressView(getContext());
        progressView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(getContext(), 4)));
        progressView.setColor(R.color.cyan);
        progressView.setProgress(10);
        addView(progressView);

        this.foreground = false;
        this.album = new AlbumItem(getContext(), this,index);

        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);

        setWebViewClient(new WebClient());
        setWebChromeClient(new ChromeClient(callback, progressView));
    }

    public void setBrowserController(BrowserController browserController) {
        this.browserController = browserController;
        this.album.setBrowserController(browserController);
    }

    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void setAlbumTitle(String title) {
        album.setAlbumTitle(title);
    }

    @Override
    public View getAlbumView() {
        return album.getAlbumView();
    }

    @Override
    public void activate() {
        requestFocus();
        foreground = true;
        album.activate();
    }

    @Override
    public void deactivate() {
        clearFocus();
        foreground = false;
        album.deactivate();
    }

    public interface Callback {
        void onReceivedTitle(WebView view, String title);
    }

    public void setIndex(int index){
        this.album.index = index;
    }

    public int getIndex(){return this.album.index;}



    @Override
    public void loadUrl(@NonNull String url, @NonNull Map<String, String> additionalHttpHeaders) {
        super.loadUrl(url, additionalHttpHeaders);
    }

}
