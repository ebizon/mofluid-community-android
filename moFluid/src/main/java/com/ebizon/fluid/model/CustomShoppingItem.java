package com.ebizon.fluid.model;

import com.ebizon.fluid.Utils.Utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This is designed to have only one custom option for that id
 * Created by manish on 04/02/16.
 */
public class CustomShoppingItem extends ShoppingItem {
    private final ArrayList<CustomOptionSet> customOptionSets;
    private HashMap<String,String> messages;

    public CustomShoppingItem(String id, String name, String sku, String image, String price, String specialPrice, String inStock, String stockQuantity, String type, ArrayList<CustomOptionSet> customOptionSets,String thumbnail,HashMap<String,String> messages) {
        super(id, name, sku, image, price, specialPrice, inStock, stockQuantity, type,thumbnail,id);
        this.customOptionSets = customOptionSets;
        this.messages=messages;
    }

    public String getPriceExpandedStr() {

        String price = Utils.appendWithCurrencySymbol(super.getFinalPrice());

        Boolean haveCustomOption = false;

        for (CustomOptionSet optionSet : customOptionSets) {
            Iterator<CustomOption> optionIterator = optionSet.getOptions();
            while (optionIterator.hasNext()) {
                CustomOption option = optionIterator.next();
                if (option.getSelected()) {
                    haveCustomOption = true;
                    price += " + " + option.getPriceWithCurrency();
                }
            }
        }

        if (haveCustomOption) {
            price += " = " + this.getFinalPriceWithCurrency();
        }

        return price;
    }

    public double getFinalPrice() {
        double price = super.getFinalPrice();

        if (customOptionSets==null) {
            throw new NullPointerException();
        }

        for (CustomOptionSet optionSet : customOptionSets) {
            price += optionSet.getCustomCost();
        }

        return price;
    }

    public JSONObject createJSON(int quantity) {
        HashMap<String, String> productMap = this.createJSONMAP(quantity);
        JSONObject optionJSON = this.createOptionMap();
        //productMap.put("options", optionJSON.toString());
        JSONObject jsnObj = new JSONObject(productMap);
        try {
            jsnObj.put("options",optionJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsnObj;
    }

    private JSONObject createMessagesMap() {
        JSONObject messageObject=null;
        if(messages.size()>0) {
            messageObject = new JSONObject(messages);
        }
        return messageObject;

    }

    private JSONObject createOptionMap() {
        HashMap<String, String> optionMap = new HashMap<>();

        for(CustomOptionSet optionSet : this.customOptionSets){
            int size = optionSet.getSelectedIds().size();
            String selectedIds="";
            for (int i = 0; i < size; i++) {
                if(selectedIds.equalsIgnoreCase("")){
                    selectedIds=optionSet.getSelectedIds().get(i);
                }else{
                    selectedIds=selectedIds+","+optionSet.getSelectedIds().get(i);
                }
            }
            optionMap.put(String.valueOf(optionSet.getId()),selectedIds);
        }

        JSONObject jsnObj = new JSONObject(optionMap);
        JSONObject messageobject= this.createMessagesMap();
        try {
            jsnObj.put("text_messages",messageobject);
        }catch (Exception e){

        }

        return jsnObj;
    }
}
