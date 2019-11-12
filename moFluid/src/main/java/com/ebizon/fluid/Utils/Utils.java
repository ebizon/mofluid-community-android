package com.ebizon.fluid.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.ebizon.fluid.model.ShoppingCartItem;
import com.mofluid.magento2.R;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;

/**
 * Created by manish on 22/01/16.
 */
public class Utils {

    public static void hideKeyboardwithoutPopulate(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
public static String getCurrentDate(String format){
    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat(format);
    String formattedDate= dateFormat.format(date);
    return formattedDate;
}
    public static String appendWithCurrencySymbol(double value){
        //Replace it with number format
        Currency currency = Currency.getInstance(Config.getInstance().getCurrencyCode());
        DecimalFormat decimalFormat = new DecimalFormat("##,##0.00");

//        return currency.getSymbol() + " "+ decimalFormat.format(value);
        return Config.getInstance().getCurrency_symbol() + " "+ decimalFormat.format(value);
    }

    public static String getAppName(Context context){

        return context.getResources().getString(R.string.app_name);
    }


    public static String appendWithCurrencySymbolstring(String value){
        //Replace it with number format
        Currency currency = Currency.getInstance(Config.getInstance().getCurrencyCode());
        // DecimalFormat decimalFormat = new DecimalFormat("#.00");

//        return currency.getSymbol() + " "+ decimalFormat.format(value);
        return Config.getInstance().getCurrency_symbol() + " "+ value;
    }

    public static String encodeToBase64(String input) {
        byte[] data = new byte[0];
        try {
            data = input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String base64Encoded = Base64.encodeToString(data, Base64.DEFAULT);

        return base64Encoded;

    }

    public static boolean checkCartItemforDownloadable(ArrayList<ShoppingCartItem> cartItemList) {
        for (int i = 0; i <cartItemList.size() ; i++) {
            if (cartItemList.get(i).getShoppingItem().getType().toString().equals("downloadable")) {
                return true;
            }
        }
        return false;
    }
    public static boolean checkIfIsLoggedIn(Activity activity)
    {
        SharedPreferences mySharedPreference = activity.getSharedPreferences(ConstantDataMember.USER_INFO_SESSION, Context.MODE_PRIVATE);
        Boolean isLoggedin=false;
        if (mySharedPreference.getString(ConstantDataMember.USER_INFO_USER_LOGIN_STATUS, "0").equals("1")) {
            isLoggedin=true;
        }
        else
        {
            isLoggedin=false;
        }
        return isLoggedin;
    }

    public static String fetchDeviceID(Context context) {

        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("PiyushK", "Device ID is" + android_id);
        return android_id;
    }
}
