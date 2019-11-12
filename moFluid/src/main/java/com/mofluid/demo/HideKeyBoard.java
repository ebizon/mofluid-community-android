package com.mofluid.magento2;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ebizon on 5/11/15.
 */
public class HideKeyBoard {


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
