package com.mofluid.utility_new;

import com.ebizon.fluid.Utils.MySharedPreferences;

public class URLManager {
    private static final String BASE_URL = "https://store2.mofluid.com/mofluidapi2";
    private static final int STORE_ID = 1;

    private static String initURL(String service) {
        String pref_base_url = MySharedPreferences.getInstance().get(MySharedPreferences.BASE_URL);
        String pref_store_id = MySharedPreferences.getInstance().get(MySharedPreferences.STORE_ID);
        String base_url = pref_base_url != null ? pref_base_url : BASE_URL;
        base_url += "?";
        base_url += "store=";
        base_url += pref_store_id != null ? pref_store_id : STORE_ID;
        base_url += "&service=";
        base_url += service;
        return base_url;
    }

    public static final String getCartItemsURL(long customer_id) {
        String url = initURL("get_cart_items");
        url += "&customerid=" + customer_id;
        return url;
    }

    public static final String getAddItemsToCartURL(long customer_id,String item_data) {
        String url = initURL("add_cart_item");
        url += "&customerid=" + customer_id+"&item_data="+item_data;
        return url;
    }

    public static final String getRemoveItemFromCartURL(long customer_id,long item_id) {
        String url = initURL("remove_cart_item");
        url += "&customerid=" + customer_id+"&itemid="+item_id;
        return url;
    }
    public static final String getClearCartItemsURL(long customer_id) {
        String url = initURL("clearcart");
        url += "&customerid=" + customer_id;
        return url;
    }
    public static final String getUpdateCartItemsCountURL(long customer_id,long product_id,long count) {
        String url = initURL("update_cart_item");
        url += "&customerid=" + customer_id+"&product_id="+product_id+"&count="+count;
        return url;
    }

    public static final String getSearchProductURL(String search_data,String sort_type,String sort_order,long cur_page,long page_size){
        String url = initURL("search");
        url += "&sorttype=" + sort_type+"&sortorder="+sort_order+"&currentpage="+cur_page+"&pagesize="+page_size+"&search_data="+search_data;
        return url;
    }
    public static final String getRegisterDeiceforPushURL(String deviceID){
        String url=initURL("registerdevice");
        url+="&deviceid="+deviceID;
        return url;
    }
    public static String getDirectPayURL(){
        return "https://secure.3gdirectpay.com/API/v5/";
    }
    public static String getDirectPayWebViewURL(String trans_token){
             return "https://secure.3gdirectpay.com/pay.asp?ID="+trans_token;
    }
}
