package com.alva.testbrowser.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.alva.testbrowser.R;
import com.alva.testbrowser.adapter.CompleteAdapter;
import com.alva.testbrowser.database.Bookmark;
import com.alva.testbrowser.ui.UrlBarController;
import com.alva.testbrowser.util.RecordViewModel;
import com.alva.testbrowser.util.UiUtils;
import com.alva.testbrowser.webview.AlbumController;
import com.alva.testbrowser.webview.BrowserController;
import com.alva.testbrowser.webview.WebViewExt;
import com.alva.testbrowser.webview.WebViewPool;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BrowserController {

    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    public static final String FILE = "file://";

    //dialog_tab
    private AlertDialog dialog_tabPreview;
    private LinearLayout tab_container;
    private ImageButton tab_openOverView;
    private ImageButton goBack;
    private ImageButton goForward;

    private FrameLayout webViewContainer;
    private WebViewPool webViewPool;
    private AutoCompleteTextView urlEdit;
    private WebViewExt webView;
    private long exitTime;
    private Context context;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webViewPool = WebViewPool.getInstance();
        WebViewPool.init(this);

        init();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.stopLoading();
            webView.goBack();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 1000) {
                Toast.makeText(this, "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finishAndRemoveTask();
                System.exit(0);
            }
        }
    }

    /**
     * 初始化ui,并绑定相关view
     */
    private void init() {
        context = MainActivity.this;

        initDialogTab();
        initView();
        initUrlEdit();
    }

    private void initView() {
        webViewContainer = findViewById(R.id.webViewContainer);
        urlEdit = findViewById(R.id.urlEdit);
        goBack = findViewById(R.id.goBack);
        goForward = findViewById(R.id.goForward);
        ImageButton refresh = findViewById(R.id.refresh);
        ImageButton history = findViewById(R.id.history);
        ImageButton drawerDialog = findViewById(R.id.drawerDialog);
        ImageButton dialogTab = findViewById(R.id.tab);
        goBack.setOnClickListener(this);
        goForward.setOnClickListener(this);
        refresh.setOnClickListener(this);
        history.setOnClickListener(this);
        drawerDialog.setOnClickListener(this);
        dialogTab.setOnClickListener(this);
        addAlbum("https://www.baidu.com");
    }

    /**
     * 初始化窗口切换Dialog
     */
    private void initDialogTab() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
        View dialogView = View.inflate(context, R.layout.dialog_tabs, null);

        tab_container = dialogView.findViewById(R.id.tab_container);
        tab_openOverView = dialogView.findViewById(R.id.tab_openOverView);
        tab_openOverView.setOnClickListener(view -> {
            addAlbum("https://www.baidu.com");
            dialog_tabPreview.cancel();
        });

        builder.setView(dialogView);
        dialog_tabPreview = builder.create();
        Objects.requireNonNull(dialog_tabPreview.getWindow()).setGravity(Gravity.BOTTOM);
        dialog_tabPreview.setOnCancelListener(dialog ->
                dialog_tabPreview.hide());
    }

    /**
     * 初始化webView
     */
    private void initUrlEdit() {

        RecordViewModel record = new RecordViewModel(getApplication());
        record.getAllHistory();
        record.getAllBookmarks();
        CompleteAdapter adapter = new CompleteAdapter(context, R.layout.item_icon_left, record.historyList, record.bookmarkList);
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

    private synchronized void addAlbum(String url) {
        if (webView != null) {
            webView.deactivate();
        }
        webView = webViewPool.getWebView(context);
        webView.init(new UrlBarController(urlEdit, webView), webViewPool.getSize(), goBack, goForward);
        webView.setBrowserController(this);

        if (!url.isEmpty()) {
            webView.loadUrl(url);
        } else {
            webView.loadUrl("https://github.com/9-TrainingGroup/TestBrowser");
        }

        webView.activate();
        webViewContainer.removeAllViews();
        webViewContainer.addView(webView);

        View albumView = webView.getAlbumView();
        tab_container.addView(albumView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        webView.setIndex(webViewPool.getSize() - 1);
    }

    /**
     * 主页clickListener
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goBack:
                if (webView.canGoBack()) {
                    onBackPressed();
                } else {
                    startActivity(new Intent(MainActivity.this, NewsActivity.class));
                }
                break;
            case R.id.goForward:
                if (webView.canGoForward()) {
                    webView.goForward();
                } else {
                    webView.loadUrl("https://www.baidu.com");
                    webView.clearHistory();
                }
                break;
            case R.id.refresh:
                webView.reload();
                break;
            case R.id.drawerDialog:
//                new MaterialDialog.Builder(MainActivity.this)
//                        .title("标题")\
//                        .content("内容")
//                        .positiveText("确认")
//                        .negativeText("取消")
//                        .show();
                Bookmark bookmark = new Bookmark(webView.getTitle(), webView.getUrl());
                RecordViewModel recordViewModel = new ViewModelProvider(this).get(RecordViewModel.class);
                recordViewModel.deleteSameBookmark(webView.getUrl());
                recordViewModel.insertBookmark(bookmark);
                Toast.makeText(this, "已收藏", Toast.LENGTH_SHORT).show();
                break;

            case R.id.history:
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.tab:
                dialog_tabPreview.show();
            default:

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
        webView.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String url = data.getStringExtra("open_url");
                    Log.d("open-url", url);
                    webView.loadUrl(url);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showAlbum(AlbumController albumController) {
        synchronized (this) {
            webView.deactivate();
            int index = albumController.getIndex();
            WebViewExt webViewExt = webViewPool.getWebViewByIndex(context, index);
            webView = webViewExt;
            webViewContainer.removeAllViews();
            webViewContainer.addView(webView);
            urlEdit.setText(webView.getUrl());
            webView.activate();
        }
    }

    @Override
    public void removeAlbum(AlbumController albumController) {
        if (webViewPool.getSize() == 1) {
            webView.loadUrl("https://www.baidu.com");
            return;
        }
        int index = albumController.getIndex();
        tab_container.removeViewAt(index);
        WebViewExt webViewExt = webViewPool.getWebViewByIndex(context, index);
        if (this.webView.getIndex() == webViewExt.getIndex()) {
            index = index == 0 ? 1 : index;
            this.webView = webViewPool.getWebViewByIndex(context, index);
            webViewContainer.removeAllViews();
            webViewContainer.addView(webView);
            webView.activate();
        }
        webViewPool.removeWebView(webViewExt);
        dialog_tabPreview.cancel();
    }

}