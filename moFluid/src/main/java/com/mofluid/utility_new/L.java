package com.mofluid.utility_new;

import android.util.Log;

public class L {
    private static final boolean DEBUG = true;
    private static final String APP_NAME = "mofluid";

    public static void d(String TAG, String message) {
        if (DEBUG) Log.d(APP_NAME + " " + TAG, message);

    }

    public static void e(String TAG, String messsage) {
        if (DEBUG) Log.e(APP_NAME + " " + TAG, messsage);
    }
}
