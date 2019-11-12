package com.mofluid.magento2;

import android.util.Log;

import com.ebizon.fluid.model.AdditionalInfoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ebizon on 23/11/15.
 */
public class GetDataForAdditionalInfo {
    private String attr_code;
    private String attr_label;
    private String attr_value;

    private JSONObject JSONObj;

    public  ArrayList<AdditionalInfoItem> getListOfAdditionalInfo(JSONObject obj)
    {

        String TAG = getClass().getSimpleName();

        ArrayList<AdditionalInfoItem> list = new ArrayList<>();
        int n=0;
        try{n=obj.getInt("size");} catch(Exception e){}
        for(int i=0;i<n;i++)
        {
            try {
                JSONObj=obj.getJSONObject(i+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                attr_code=JSONObj.getString("attr_code");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                attr_label=JSONObj.getString("attr_label");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                attr_value=JSONObj.getString("attr_value");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(attr_label.equalsIgnoreCase("featured") || attr_value.equalsIgnoreCase("No"))
                // nothing add
                ;
            else
            list.add(new AdditionalInfoItem( attr_code,  attr_label,  attr_value));

        }




        return list;
    }

}
