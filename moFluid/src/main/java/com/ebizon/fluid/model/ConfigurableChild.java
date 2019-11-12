package com.ebizon.fluid.model;

import android.util.Log;

import com.mofluid.magento2.fragment.SimpleProductFragment2;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * Created by piyush-ios on 24/1/17.
 */
public class ConfigurableChild extends ShoppingItem {

    private final JSONObject data;
    private String keyOFChild=null;

    public String getKeyOFChild() {
        return keyOFChild;
    }

    HashMap<String,ConfigurableChildOptions> attributes = new HashMap<>();
    SimpleProductFragment2 context;
    private LinkedHashSet<String> currentButtons;

    public HashMap<String, ConfigurableChildOptions> getAttributes() {
        return attributes;
    }

    public ConfigurableChild(String id, String name, String sku, String image, String price, String specialPrice, String inStock, String stockQuantity, String type, String thumbnail, JSONObject data, SimpleProductFragment2 simpleProductFragment2,String parentID) {
        super(id, name, sku, image, price, specialPrice, inStock, stockQuantity, type, thumbnail,parentID);
        this.context = simpleProductFragment2;
        currentButtons = new LinkedHashSet<>();
        this.attributes = parseAttributesOfChild(data);
        this.data = data;


    }

    private HashMap<String, ConfigurableChildOptions> parseAttributesOfChild(JSONObject attributes) {

        HashMap<String,ConfigurableChildOptions> childoptions = new HashMap<>();

        Iterator<String> iter = attributes.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                String option_type = key;
                JSONObject option_value = attributes.getJSONObject(option_type);
                String label = option_value.getString("label");

                if(keyOFChild==null)
                    keyOFChild = label;
                else
                keyOFChild = keyOFChild+","+label;
                savetoDisplayList(option_type,label);
//                currentButtons.add(Math.abs(label.hashCode()));
                currentButtons.add(label);
                String code = option_value.getString("value_index");  //  attribute_code");
                String attribute_id = option_value.getString("attribute_id");
                ConfigurableChildOptions option = new ConfigurableChildOptions(label,code,attribute_id);
                childoptions.put(option_type,option);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        savetoButtonsList();
        return childoptions;
    }



    private void savetoDisplayList(String option_type, String label) {
        if(context.configDisplayListMap.containsKey(option_type))
        {
            // key already exists
            LinkedHashSet<String> options = context.configDisplayListMap.get(option_type);
            options.add(label);
            context.configDisplayListMap.put(option_type,options);
            LinkedHashSet<Integer> optionsHashed = context.configDisplayHashMap.get(option_type);
            int code = Math.abs(label.hashCode());
            optionsHashed.add(code);
            context.configDisplayHashMap.put(option_type,optionsHashed);

        }
        else
        {
            // first time adding key
            LinkedHashSet<String> options = new LinkedHashSet<>();
            options.add(label);
            LinkedHashSet<Integer> optionsHashed = new LinkedHashSet<>();
            int code = Math.abs(label.hashCode());
            optionsHashed.add(code);
            context.configDisplayListMap.put(option_type,options);
            context.configDisplayHashMap.put(option_type,optionsHashed);
        }

    }

    private void savetoButtonsList() {

        currentButtons.size();
        Iterator<String> it = currentButtons.iterator();
        while(it.hasNext())
        {
            String key = it.next();
            Integer code = Math.abs(key.hashCode());
            if(context.configButtonsListMap.containsKey(code))
            {
                // Already key exists, fetch Set and add to Set
                LinkedHashSet<String> tempSet = new LinkedHashSet<>(context.configButtonsListMap.get(code));
                tempSet.addAll(currentButtons);
                context.configButtonsListMap.put(code,tempSet);
            }
            else
            {
                // Not added before, add key and create new set and all value in there
                context.configButtonsListMap.put(code,currentButtons);
            }
        }

        Log.d("aa",context.configButtonsListMap.toString());
    }




}
