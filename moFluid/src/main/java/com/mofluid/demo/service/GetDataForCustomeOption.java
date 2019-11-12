package com.mofluid.magento2.service;

import com.ebizon.fluid.model.CustomOption;
import com.ebizon.fluid.model.CustomOptionSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ebizon on 26/11/15.
 */
public class GetDataForCustomeOption {
    public ArrayList<CustomOptionSet> getCustomeOptionAttribute(JSONArray jsnArray) {
        ArrayList<CustomOptionSet> customOptionSets = new ArrayList<>();

        for (int i = 0; i < jsnArray.length(); i++) {
            try {
                JSONObject jsnObj = jsnArray.getJSONObject(i);

                String id = jsnObj.getString("custom_option_id");
                String name = jsnObj.getString("custom_option_name");
                String isRequired = jsnObj.getString("custom_option_is_required");
                String type = jsnObj.getString("custom_option_type");
                JSONArray customOptionsValueArray=null;
                ArrayList<CustomOption> customOptions=null;
                if(jsnObj.has("custom_option_value_array")) {
                    customOptionsValueArray = jsnObj.getJSONArray("custom_option_value_array");
                }
                if(customOptionsValueArray!=null) {
                    customOptions = this.getCustomeOptions(customOptionsValueArray);
                }
                CustomOptionSet customOptionSet = new CustomOptionSet(id, name, type, isRequired, customOptions);
                customOptionSets.add(customOptionSet);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return customOptionSets;
    }

    private ArrayList<CustomOption> getCustomeOptions(JSONArray customOptionsValueArray) {
        ArrayList<CustomOption> customOptions = new ArrayList<>();

        for(int i=0;i<customOptionsValueArray.length();i++){
            try {
                JSONObject jsonObj = customOptionsValueArray.getJSONObject(i);
                String id = jsonObj.getString("id");
                String price = jsonObj.getString("price");
                String priceType = jsonObj.getString("price_type");
                String sku = jsonObj.getString("sku");
                String sortOrder = jsonObj.getString("sort_order");
                String title = jsonObj.getString("title");

                customOptions.add(new CustomOption(id,  price,  priceType,  sku,  sortOrder,  title));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return customOptions;
    }
}
