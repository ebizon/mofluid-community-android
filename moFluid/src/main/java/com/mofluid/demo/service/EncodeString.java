package com.mofluid.magento2.service;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * Created by ebizon on 17/12/15.
 */
class EncodeString {

    private static final String TAG="EncodeString";

    public static String encodeStrBase64Bit(String password) {



        // Encode Password
        byte[] data = new byte[0];
        try {
            data = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.NO_WRAP);
        Log.d(TAG, " Encoded Password  " + base64);

// Decode password
       /* byte[] data1 = Base64.decode(base64, Base64.DEFAULT);
        try {
           text1 = new String(data1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d(TAG," Decoded Password  "+text1);

*/

        return base64;

    }
}
