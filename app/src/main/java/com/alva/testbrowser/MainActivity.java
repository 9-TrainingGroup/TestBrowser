package com.alva.testbrowser;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alva.testbrowser.ui.UrlBarController;
import com.alva.testbrowser.util.UiUtils;
import com.alva.testbrowser.webview.WebViewExt;

public class MainActivity extends AppCompatActivity {

    private FrameLayout webViewContainer;
    private EditText urlEdit;
    private WebViewExt webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webViewContainer = findViewById(R.id.webViewContainer);
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
                    webView.loadUrl(textView.getText().toString());
                    UiUtils.hideKeyboard(textView);
                    return true;
                }
                return false;
            }
        });

        webView.init(new UrlBarController(urlEdit));
        webView.loadUrl("https://baidu.com");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}