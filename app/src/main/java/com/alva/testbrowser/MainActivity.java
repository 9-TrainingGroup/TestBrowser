package com.alva.testbrowser;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alva.testbrowser.Activity.WebListActivity;
import com.alva.testbrowser.Adapter.CompleteAdapter;
import com.alva.testbrowser.ui.UrlBarController;
import com.alva.testbrowser.util.UiUtils;
import com.alva.testbrowser.webview.WebViewExt;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    public static final String FILE = "file://";

    private AutoCompleteTextView urlEdit;
    private WebViewExt webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        webView.init(new UrlBarController(urlEdit));
        webView.loadUrl("https://baidu.com");
        webView.addJavascriptInterface(new JavascriptInterface(this), "imageListener");

        // TODO: 2021/7/16：添加书签及历史记录到数据库
        //webView.getTitle();
        //webView.getUrl();
        //webView.copyBackForwardList().getCurrentItem().getTitle();
        //webView.copyBackForwardList().getCurrentItem().getUrl();
        // TODO: 2021/7/16：点击书签或历史记录打开网页
        webView.loadUrl(getIntent().getStringExtra("webUrl"));
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 初始化ui,并绑定相关view
     */
    private void init() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(android.R.color.darker_gray));
        window.setNavigationBarColor(getResources().getColor(android.R.color.white));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

        FrameLayout webViewContainer = findViewById(R.id.webViewContainer);
        urlEdit = findViewById(R.id.urlEdit);
        ImageButton goBack = findViewById(R.id.goBack);
        ImageButton goForward = findViewById(R.id.goForward);
        ImageButton refresh = findViewById(R.id.refresh);
        ImageButton history = findViewById(R.id.history);
        goBack.setOnClickListener(this);
        goForward.setOnClickListener(this);
        refresh.setOnClickListener(this);
        history.setOnClickListener(this);
        webView = new WebViewExt(this);
        webViewContainer.addView(webView);

        initUrlEdit();
    }

    private void initUrlEdit() {

        List<String> list = new ArrayList<>();
        CompleteAdapter adapter = new CompleteAdapter(this, R.layout.item_icon_left, list);
        urlEdit.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        urlEdit.setOnItemClickListener((parent, view, position, id) -> {
            String url = ((TextView) view.findViewById(R.id.record_item_time)).getText().toString();
            webView.loadUrl(coverKeywordLoadOrSearch(url));
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

    /**
     * 主页clickListener
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goBack:
                webView.goBack();
                break;
            case R.id.goForward:
                webView.goForward();
                break;
            case R.id.refresh:
                webView.reload();
                break;
            case R.id.history:
                Intent intent = new Intent(MainActivity.this, WebListActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}