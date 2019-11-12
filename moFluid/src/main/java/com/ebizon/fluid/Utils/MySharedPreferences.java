package com.ebizon.fluid.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mofluid.magento2.service.AppController;
import com.mofluid.utility_new.L;

/**
 * Created by saddam on 13/3/18.
 */

public class MySharedPreferences {
    private final String TAG="MySharedPreferences";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public static final String BASE_URL="base_url";
    public static final String isLogedin="isLogedin";
    public static final String BILLING_ADDRESS_ID="BILLING_ADDRESS_ID";
    public static final String SHIPPING_ADDRESS_ID="SHIPPING_ADDRESS_ID";
    public static final String CUSTOMER_ID="CUSTOMER_ID";
    private  final String PREFS_NAME="mofluid_prefs";
    public static final String CURRENCY_CODE="currency_code";
    public static  final String STORE_ID="store_id";
    public static  final String SYSTEM_LANG="system_lang";


    public static MySharedPreferences getInstance() {
        if(instance==null) instance=new MySharedPreferences();
        return instance;
    }

    private static MySharedPreferences instance;
    private MySharedPreferences(){
        this.pref= AppController.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.editor=this.pref.edit();
    }
    public String get(String KEY){
      return this.pref.getString(KEY,null);
    }
    public void set(String KEY,String value){
        this.editor.putString(KEY,value);
        this.editor.apply();
        L.d(TAG,"key="+KEY+" value="+value +" added.");

    }
    public void remove(String KEY){
        this.editor.remove(KEY);
        this.editor.apply();
        L.d(TAG,"key="+KEY+" removed.");
    }
    public void clear(){
        this.editor.clear();
        this.editor.commit();
        this.editor.apply();
        L.d(TAG,"SharedPreferences cleared");
    }
}
