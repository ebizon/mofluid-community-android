package com.ebizon.fluid.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by manish on 21/01/16.
 */
public class Config {
    private static final Config ourInstance = new Config();

    public static Config getInstance() {
        return ourInstance;
    }

    private static final String BaseURL = "BaseURL";
    private static final String StoreId = "StoreId";
    private static final String CurrencyCode = "CurrencyCode";
    private static final String Language = "AppLanguage";
    private static final String Currency_symbol = "CurrencySymbol";
    private static final String Currency_symbol_value = "$";
    private static final String MOFLUID_API_ACCESS_KEY = "MOFLUID_API_ACCESS_KEY";

    private final HashMap<String, String> configMap = new HashMap<>();

    public String getCurrency_symbol() {
        return this.configMap.get(Currency_symbol);
    }

    private Config() {
    }

    public void load(Context context){
        String TAG = "Configurable data load";
        MySharedPreferences prefs=MySharedPreferences.getInstance();
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            configMap.put(BaseURL, bundle.getString("base_url"));
            if(prefs.get(MySharedPreferences.CURRENCY_CODE)!=null)
                configMap.put(CurrencyCode,prefs.get(MySharedPreferences.CURRENCY_CODE));
            else
            configMap.put(CurrencyCode, bundle.getString("currency_code"));
            if(prefs.get(MySharedPreferences.STORE_ID)!=null)
                configMap.put(StoreId,prefs.get(MySharedPreferences.STORE_ID));
            else
            configMap.put(StoreId,bundle.getString("store_id"));
            if(prefs.get(MySharedPreferences.SYSTEM_LANG)!=null)
            configMap.put(Language, prefs.get(MySharedPreferences.SYSTEM_LANG));
            else
            configMap.put(Language, bundle.getString("system_language"));
            configMap.put(Currency_symbol, Currency_symbol_value);
            if(bundle.containsKey("mofluid_api_access_key")){
                configMap.put(MOFLUID_API_ACCESS_KEY, bundle.getString("mofluid_api_access_key"));
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
    }

    public String getBaseURL(){
        return this.configMap.get(BaseURL);
    }

    public String getMofluiApiAccessKey(){
        return this.configMap.get(MOFLUID_API_ACCESS_KEY);
    }
    public String getStoreValue(){
        return this.configMap.get(StoreId);
    }

    public void setStoreValue(String storevalue)
    {
        ourInstance.configMap.put(StoreId, storevalue);
    }

    public void setCurrency_symbol(String symbol)
    {
        ourInstance.configMap.put(Currency_symbol, symbol);
    }

    public String getLanguage() {
        return this.configMap.get(Language);
    }
    public void setLanguage(String language)
    {
        ourInstance.configMap.put(Language, language);
    }
    public void setCurrencyCode(String currency)
    {

        ourInstance.configMap.put(CurrencyCode,currency);
    }

    public String getCurrencyCode(){
        return this.configMap.get(CurrencyCode);
    }
}
