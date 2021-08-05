package com.alva.testbrowser.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alva.testbrowser.R;
import com.alva.testbrowser.fragment.BookMarkFragment;
import com.alva.testbrowser.fragment.HistoryFragment;

/*历史记录和书签总界面,从历史记录按钮点进去，优先显示历史记录*/
public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    Button bookmarkButton;
    Button historybutton;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        bookmarkButton = findViewById(R.id.bookmark_button);
        historybutton = findViewById(R.id.history_button);
        back = findViewById(R.id.history_back);


        historybutton.setBackgroundResource(R.drawable.btn_backgroundgray);
        bookmarkButton.setOnClickListener(this);
        historybutton.setOnClickListener(this);
        back.setOnClickListener(this);

        replaceFragment(new HistoryFragment());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.history_button:
                replaceFragment(new HistoryFragment());
                bookmarkButton.setBackgroundResource(R.drawable.btn_background);
                historybutton.setBackgroundResource(R.drawable.btn_backgroundgray);
                break;
            case R.id.bookmark_button:
                replaceFragment(new BookMarkFragment());
                historybutton.setBackgroundResource(R.drawable.btn_background);
                bookmarkButton.setBackgroundResource(R.drawable.btn_backgroundgray);
                break;
            case R.id.history_back:
                finish();
            default:
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.record_view, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }
}