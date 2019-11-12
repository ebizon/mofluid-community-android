package com.mofluid.magento2;

import com.ebizon.fluid.model.LogoHeaderItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ebizon on 27/10/15.
 */
class GetHeaderLogoDetails {

    private static final String KEY_mofluid_theeme_id = "mofluid_theme_id";
    private static final String KEY_mofluid_store_id="mofluid_store_id";
    private static final String KEY_mofluid_image_id = "mofluid_image_id";
    private static final String KEY_mofluid_image_type="mofluid_image_type";
    private static final String KEY_mofluid_image_label="mofluid_image_label";
    private static final String KEY_mofluid_image_value = "mofluid_image_value";
    private static final String KEY_mofluid_image_helptext="mofluid_image_helptext";
    private static final String KEY_mofluid_image_helplink = "mofluid_image_helplink";
    private static final String KEY_mofluid_image_required="mofluid_image_isrequired";
    private static final String KEY_mofluid_image_sort_order = "mofluid_image_sort_order";
    private static final String KEY_mofluid_image_isdefault="mofluid_image_isdefault";
    private static final String KEY_mofluid_image_action = "mofluid_image_action";
    private static final String KEY_mofluid_image_data="mofluid_image_data";

    private  String mofluid_theeme_id ;
    private  String mofluid_store_id;
    private  String mofluid_image_id ;
    private  String mofluid_image_type;
    private  String mofluid_image_label;
    private  String mofluid_image_value ;
    private  String mofluid_image_helptext;
    private  String mofluid_image_helplink ;
    private  String mofluid_image_required;
    private  String mofluid_image_sort_order;
    private  String mofluid_image_isdefault;
    private  String mofluid_image_action ;
    private  String mofluid_image_data;

    public ArrayList<LogoHeaderItem> getLogoDetails(JSONArray imageJSNArray)
    {

        ArrayList<LogoHeaderItem> logoListData = new ArrayList<LogoHeaderItem>();

        for(int i=0;i<imageJSNArray.length();i++)
        {
            JSONObject imageJSNObj = null;
            try {
                imageJSNObj = imageJSNArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                assert imageJSNObj != null;
                mofluid_theeme_id =imageJSNObj.getString(KEY_mofluid_theeme_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mofluid_store_id =imageJSNObj.getString(KEY_mofluid_store_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mofluid_image_id =imageJSNObj.getString(KEY_mofluid_image_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mofluid_image_type =imageJSNObj.getString(KEY_mofluid_image_type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mofluid_image_label =imageJSNObj.getString(KEY_mofluid_image_label);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mofluid_image_value  =imageJSNObj.getString(KEY_mofluid_image_value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mofluid_image_helptext =imageJSNObj.getString(KEY_mofluid_image_helptext);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mofluid_image_helplink =imageJSNObj.getString(KEY_mofluid_image_helplink);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mofluid_image_required=imageJSNObj.getString(KEY_mofluid_image_required);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mofluid_image_sort_order=imageJSNObj.getString(KEY_mofluid_image_sort_order);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mofluid_image_isdefault=imageJSNObj.getString(KEY_mofluid_image_isdefault);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mofluid_image_action =imageJSNObj.getString(KEY_mofluid_image_action);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mofluid_image_data=imageJSNObj.getString(KEY_mofluid_image_data);
            } catch (JSONException e) {
                e.printStackTrace();
            }



            logoListData.add(new LogoHeaderItem(mofluid_theeme_id,mofluid_store_id,mofluid_image_id,mofluid_image_type,mofluid_image_label,mofluid_image_value,mofluid_image_helptext,mofluid_image_helplink,mofluid_image_required,mofluid_image_sort_order,mofluid_image_isdefault,mofluid_image_action,mofluid_image_data));



        }

        return logoListData;
    }
}
