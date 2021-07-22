package com.alva.testbrowser.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alva.testbrowser.R;
import com.alva.testbrowser.fragment.BookMarkFragment;
import com.alva.testbrowser.fragment.HistoryFragment;

/*历史记录和书签总界面,从历史记录按钮点进去，优先显示历史记录*/
public class RecordActivity extends AppCompatActivity implements View.OnClickListener {

    TextView bookmarkButton;
    TextView historybutton;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        bookmarkButton = findViewById(R.id.bookmark_button);
        historybutton = findViewById(R.id.history_button);
        back = findViewById(R.id.history_back);


        historybutton.setBackgroundColor(0xFFCCCCCC);
        bookmarkButton.setOnClickListener(this);
        historybutton.setOnClickListener(this);
        back.setOnClickListener(this);

        replaceFragment(new HistoryFragment());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.history_button:
                replaceFragment(new HistoryFragment());
                historybutton.setBackgroundColor(0xFFCCCCCC);
                bookmarkButton.setBackgroundColor(0xFFFFFFFF);
                break;
            case R.id.bookmark_button:
                replaceFragment(new BookMarkFragment());
                bookmarkButton.setBackgroundColor(0xFFCCCCCC);
                historybutton.setBackgroundColor(0xFFFFFFFF);
                break;
            case R.id.history_back:
                finish();
            default:
                break;
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.record_view,fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }
}