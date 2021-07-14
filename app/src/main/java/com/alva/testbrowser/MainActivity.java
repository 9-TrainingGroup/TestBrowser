package com.alva.testbrowser;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alva.testbrowser.ui.UrlBarController;
import com.alva.testbrowser.util.UiUtils;
import com.alva.testbrowser.webview.WebViewExt;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    public static final String FILE = "file://";

    private EditText urlEdit;
    private WebViewExt webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        webView.init(new UrlBarController(urlEdit));
        webView.loadUrl("https://baidu.com");
        webView.addJavascriptInterface(new JavascriptInterface(this), "imageListener");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void init() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(android.R.color.darker_gray));
        window.setNavigationBarColor(getResources().getColor(android.R.color.white));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

        FrameLayout webViewContainer = findViewById(R.id.webViewContainer);
        urlEdit = findViewById(R.id.urlEdit);
        webView = new WebViewExt(this);
        webViewContainer.addView(webView);

        urlEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO
                        || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    webView.loadUrl(coverKeywordLoadOrSearch(textView.getText().toString()));
                    UiUtils.hideKeyboard(textView);
                    urlEdit.clearFocus();
                    return true;
                }
                return false;
            }
        });
    }


    private static String coverKeywordLoadOrSearch(String keyword) {
        keyword = keyword.trim();

        if (keyword.startsWith("www.")) {
            keyword = HTTP + keyword;
        } else if (keyword.startsWith("ftp.")) {
            keyword = "ftp://" + keyword;
        }

        boolean containsPeriod = keyword.contains(".");
        boolean isIPAddress = (TextUtils.isDigitsOnly(keyword.replace(".", ""))
                && (keyword.replace(".", "").length() >= 4) && keyword.contains("."));
        boolean aboutScheme = keyword.contains("about:");
        boolean validURL = (keyword.startsWith("ftp://") || keyword.startsWith(HTTP)
                || keyword.startsWith(FILE) || keyword.startsWith(HTTPS))
                || isIPAddress;
        boolean isSearch = ((keyword.contains(" ") || !containsPeriod) && !aboutScheme);

        if (isIPAddress
                && (!keyword.startsWith(HTTP) || !keyword.startsWith(HTTPS))) {
            keyword = HTTP + keyword;
        }

        String coverUrl;
        if (isSearch) {
            try {
                keyword = URLEncoder.encode(keyword, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            coverUrl = "http://www.baidu.com/s?wd=" + keyword + "&ie=UTF-8";
        } else if (!validURL) {
            coverUrl = HTTP + keyword;
        } else {
            coverUrl = keyword;
        }
        return coverUrl;
    }

}