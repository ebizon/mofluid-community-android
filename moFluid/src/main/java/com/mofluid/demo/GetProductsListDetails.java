package com.mofluid.magento2;

import com.ebizon.fluid.model.WebApiManager;
import com.ebizon.fluid.model.productDetailListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ebizon on 30/10/15.
 */
public class GetProductsListDetails {
    private String id;
    private String name;
    private String imageurl;
    private String sku;
    private String type;
    private String spclprice;
    private String currencysymbol;
    private String price;
    private String created_date;
    private String is_in_stock;
    private String hasoptions;
    private String stock_quantity;

    private static final String KEY_id = "id";
    private static final String KEY_name = "name";
    private static final String KEY_imageurl = "imageurl";
    private static final String KEY_sku = "sku";
    private static final String KEY_type = "type_id";
    private static final String KEY_spclprice = "special_price";
    private static final String KEY_currencysymbol = "currencysymbol";
    private static final String KEY_price = "price";
    private static final String KEY_created_date = "created_date";
    private static final String KEY_is_in_stock = "is_in_stock";
    private static final String KEY_hasoptions = "hasoptions";
    private static final String KEY_stock_quantity = "stock_quantity";
    private static final String KEY_custom_attributes = "custom_attributes";
    private static final String KEY_attribute_code = "attribute_code";
    private static final String KEY_value = "value";
    private final String image_path="";

    public ArrayList<productDetailListItem> getProductListDetails(JSONArray imageJSNArray) {

        ArrayList<productDetailListItem> productListDetails = new ArrayList<productDetailListItem>();

        for (int i = 0; i < imageJSNArray.length(); i++) {
            JSONObject imageJSNObj = null;
            try {
                imageJSNObj = imageJSNArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                id = imageJSNObj.getString(KEY_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                name = imageJSNObj.getString(KEY_name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*try {
                imageurl = imageJSNObj.getString(KEY_imageurl);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            try {
                sku = imageJSNObj.getString(KEY_sku);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                type = imageJSNObj.getString(KEY_type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            currencysymbol="$";
            /*try {
                currencysymbol = imageJSNObj.getString(KEY_currencysymbol);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            try {
                price = imageJSNObj.getString(KEY_price);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                created_date = imageJSNObj.getString(KEY_created_date);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //try {
                is_in_stock ="true";
            /*} catch (JSONException e) {
                e.printStackTrace();
            }*/
            /*try {
                hasoptions = imageJSNObj.getString(KEY_hasoptions);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            //try {
                stock_quantity = "10";
            /*} catch (JSONException e) {
                e.printStackTrace();
            }*/

       spclprice=price;
         try{
             JSONArray custom_attributes=imageJSNObj.getJSONArray(KEY_custom_attributes);
             for(int j=0;j<custom_attributes.length();j++){
                 JSONObject custom_attributes_obj=custom_attributes.getJSONObject(j);
                 String code=custom_attributes_obj.getString(KEY_attribute_code);
                 String value=custom_attributes_obj.getString(KEY_value);
                 if(code!=null && code.equals("image"))
                     imageurl= WebApiManager.getInstance().getCatalogProductImagePath()+value;
                 if(code!=null && code.equalsIgnoreCase("special_price"))
                    spclprice=value;

             }
         }catch (JSONException e){}
            productListDetails.add(i,new productDetailListItem(id, name, imageurl, sku, type, spclprice, currencysymbol, price, created_date, is_in_stock, hasoptions, stock_quantity));


        }

        return productListDetails;
    }
}
