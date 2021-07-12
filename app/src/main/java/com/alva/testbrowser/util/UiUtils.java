package com.alva.testbrowser.util;

import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.RequiresApi;

/**
 * @author Alva
 * @since 2021/7/11 21:31
 */
public final class UiUtils {
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void hideKeyboard(View view) {
        InputMethodManager imm = view.getContext().getSystemService(InputMethodManager.class);
        if (imm != null){
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

    }
}
