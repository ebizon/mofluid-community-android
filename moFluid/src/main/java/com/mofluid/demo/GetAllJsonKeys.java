package com.mofluid.magento2;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ebizon on 17/12/15.
 */
public class GetAllJsonKeys {
    public static ArrayList<String> getAllJsonKeyList(JSONObject json_obj)
    {
        ArrayList<String> keysList;
        String TAG;
        TAG="GetAllJsonKeys";

        keysList =new ArrayList<>();
        //keysList=json_obj.keys();

        Iterator iterator = json_obj.keys();
        while(iterator.hasNext()){
            String key = (String)iterator.next();
            keysList.add(key);
            Log.d(TAG, "getAllJsonKeyList() called with: " + "JSON key = [" + key + "]");
        }
        return keysList;
    }
}
