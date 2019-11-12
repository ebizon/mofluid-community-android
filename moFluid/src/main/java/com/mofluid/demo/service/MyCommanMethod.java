package com.mofluid.magento2.service;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by ebizon on 24/11/15.
 */
public class MyCommanMethod {

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}
