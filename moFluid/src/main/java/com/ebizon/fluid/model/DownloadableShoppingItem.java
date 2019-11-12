package com.ebizon.fluid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by piyush on 5/5/16.
 */
public class DownloadableShoppingItem extends ShoppingItem {

    private final HashSet<String> downloadable_links;

    public DownloadableShoppingItem(String id, String name, String sku, String image, String price, String specialPrice, String inStock, String stockQuantity, String type, HashSet<String> checkbox_links, String thumbnail) {
        super(id, name, sku, image, price, specialPrice, inStock, stockQuantity, type, thumbnail,id);
        this.downloadable_links = checkbox_links;
    }

    public JSONObject createJSON(int quantity) {
        HashMap<String, String> productMap = this.createJSONMAP(quantity);
        JSONObject jsnObj = new JSONObject(productMap);
        try {
            jsnObj.put("down_link_options",createDownloadLink());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsnObj;
    }

    private JSONArray createDownloadLink() {

        JSONArray ar = new JSONArray();

        for (String link_id : downloadable_links) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("link_id", link_id.toString());
                obj.put("link_name","tale");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ar.put(obj);
        }
        return ar;
    }
}
