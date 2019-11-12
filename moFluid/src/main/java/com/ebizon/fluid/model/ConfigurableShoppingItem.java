package com.ebizon.fluid.model;

import com.ebizon.fluid.Utils.Validation;
import com.mofluid.magento2.fragment.SimpleProductFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by manish on 19/01/16.
 */
public class ConfigurableShoppingItem extends ShoppingItem{
    private final HashMap<String, ConfigurableAttribute> attributeMap = new HashMap<>();

    public static ConfigurableShoppingItem create(JSONArray jsonArray,String img) {
        ConfigurableShoppingItem configurableShoppingItem = null;
        if (jsonArray.length() > 0) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                configurableShoppingItem = ConfigurableShoppingItem.create(jsonObject,img,"2");

                if (!Validation.isNull(configurableShoppingItem)) {
                    addAttribute(jsonArray, configurableShoppingItem);
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        return configurableShoppingItem;
    }

    private static void addAttribute(JSONArray jsonArray, ConfigurableShoppingItem configurableShoppingItem) throws JSONException {
        JSONObject jsonObject;
        for (int i = 0; i < jsonArray.length(); ++i) {
            jsonObject = jsonArray.getJSONObject(i);
            jsonObject = jsonObject.getJSONObject("data");
            ConfigurableAttribute configurableAttribute = ConfigurableAttribute.create(jsonObject);
            if(!Validation.isNull(configurableAttribute)){
                configurableShoppingItem.addAttribute(configurableAttribute);
            }
        }
    }

    public static ConfigurableShoppingItem create(JSONObject jsonObject,String img, String temp){
        ConfigurableShoppingItem configurableShoppingItem = null;
        try {
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String sku = jsonObject.getString("sku");
            String price = jsonObject.getString("price");
            String specialPrice = jsonObject.getString("spclprice");
            String inStock = jsonObject.getString("is_in_stock");
            String stockQuantity = jsonObject.getString("stock_quantity");
            String type = jsonObject.getString("type");
            if(SimpleProductFragment.cartImage==null)
            SimpleProductFragment.cartImage="";
            if (!Validation.isNull(id, name, sku, price, SimpleProductFragment.cartImage, specialPrice, inStock, stockQuantity, type)){
                configurableShoppingItem = new ConfigurableShoppingItem(id, name, sku, SimpleProductFragment.cartImage, price, specialPrice, inStock, stockQuantity, type,img);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return configurableShoppingItem;
    }

    private ConfigurableShoppingItem(String id, String name, String sku, String image, String price, String specialPrice, String inStock, String stockQuantity, String type,String img){
        super(id, name, sku, image, price, specialPrice, inStock, stockQuantity, type,img,id);
    }

    private void addAttribute(ConfigurableAttribute attribute){
        assert(attribute != null);
        this.attributeMap.put(attribute.getId(), attribute);
    }

    public Collection<ConfigurableAttribute> getAllAttributes(){
        return this.attributeMap.values();
    }

}