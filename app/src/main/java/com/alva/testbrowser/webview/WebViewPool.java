package com.alva.testbrowser.webview;

import android.content.Context;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.alva.testbrowser.JavascriptInterface;
import com.alva.testbrowser.R;

import java.util.ArrayList;
import java.util.List;

/**
 * webview 复用池
 */
public class WebViewPool {
    private static final String DEMO_URL = "https://www.baidu.com";
    private static final String APP_CACAHE_DIRNAME = "webCache";
    private static List<WebViewExt> available = new ArrayList<>();
    private static List<WebViewExt> inUse = new ArrayList<>();
    private static final byte[] lock = new byte[]{};
    private static int maxSize = 2;
    private int currentSize = 0;
    private static long startTimes = 0;
    private static volatile WebViewPool instance = null;
 
    public static WebViewPool getInstance() {
        if (instance == null) {
            synchronized (WebViewPool.class) {
                if (instance == null) {
                    instance = new WebViewPool();
                }
            }
        }
        return instance;
    }
 
    /**
     * Webview 初始化
     * 最好放在application oncreate里
     */
    public static void init(Context context) {
        for (int i = 0; i < maxSize; i++) {
            WebViewExt webView = new WebViewExt(context);
            webView.loadUrl(DEMO_URL);
            webView.addJavascriptInterface(new JavascriptInterface(context), "imageListener");
            available.add(webView);
        }
    }
 
    /**
     * 获取webview
     */
    public WebViewExt getWebView(Context context) {
        synchronized (lock) {
            WebViewExt webView;
            if (available.size() > 0) {
                webView = available.get(0);
                available.remove(0);
                currentSize++;
                inUse.add(webView);
            } else {
                webView = new WebViewExt(context);
                inUse.add(webView);
                currentSize++;
            }
            return webView;
        }
    }

 
    /**
     * 回收webview ,不解绑
     *
     * @param webView 需要被回收的webview
     */
    public void removeWebView(WebViewExt webView) {
        webView.loadUrl("");
        webView.stopLoading();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.clearCache(true);
        webView.clearHistory();
        synchronized (lock) {
            inUse.remove(webView);
            if (available.size() < maxSize) {
                available.add(webView);
            } else {
                webView = null;
            }
            currentSize--;
        }
    }
 
    /**
     * 回收webview ,解绑
     *
     * @param webView 需要被回收的webview
     */
    public void removeWebView(ViewGroup viewGroup, WebViewExt webView) {
        viewGroup.removeView(webView);
        webView.loadUrl("");
        webView.stopLoading();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.clearCache(true);
        webView.clearHistory();
        synchronized (lock) {
            inUse.remove(webView);
            if (available.size() < maxSize) {
                available.add(webView);
            } else {
                webView = null;
            }
            currentSize--;
        }
    }
 
    /**
     * 设置webview池个数
     *
     * @param size webview池个数
     */
    public void setMaxPoolSize(int size) {
        synchronized (lock) {
            maxSize = size;
        }
    }
}