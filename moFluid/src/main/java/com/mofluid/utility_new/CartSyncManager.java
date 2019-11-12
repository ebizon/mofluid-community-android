package com.mofluid.utility_new;

import java.util.HashMap;
import java.util.Map;

public class CartSyncManager {
    public static void addItem(long customer_id, String item_data, final Callback callback) {
        String url = URLManager.getAddItemsToCartURL(customer_id, item_data);
        Map<String,String> header=new HashMap<>();
        ResponseManager manager = new ResponseManager(ResponseManager.REQUEST_TYPE_GET, url, header, null, new ResponseReceived() {
            @Override
            public void onResponseReceived(Object response, int response_code) {
                if (response == null) {
                    callback.callback(null, response_code);
                    return;
                }

            }
        });
        manager.get();
    }

    public static void removeItem(long customer_id, long item_id, final Callback callback) {
        String url = URLManager.getRemoveItemFromCartURL(customer_id, item_id);
        Map<String,String> header=new HashMap<>();
        ResponseManager manager = new ResponseManager(ResponseManager.REQUEST_TYPE_GET, url, header, null, new ResponseReceived() {
            @Override
            public void onResponseReceived(Object response, int response_code) {
                if (response == null) {
                    callback.callback(null, response_code);
                    return;
                }

            }
        });
        manager.get();
    }

    public static void getItems(long customer_id, final Callback callback) {
        String url = URLManager.getCartItemsURL(customer_id);
        Map<String,String> header=new HashMap<>();
        ResponseManager manager = new ResponseManager(ResponseManager.REQUEST_TYPE_GET, url, header, null, new ResponseReceived() {
            @Override
            public void onResponseReceived(Object response, int response_code) {
                if (response == null) {
                    callback.callback(null, response_code);
                    return;
                }
            }
        });
        manager.get();
    }

    public static void updateItemCount(long customer_id, long product_id, long count, final Callback callback) {
        String url = URLManager.getUpdateCartItemsCountURL(customer_id, product_id, count);
        Map<String,String> header=new HashMap<>();
        ResponseManager manager = new ResponseManager(ResponseManager.REQUEST_TYPE_GET, url, header, null, new ResponseReceived() {
            @Override
            public void onResponseReceived(Object response, int response_code) {
                if (response == null) {
                    callback.callback(null, response_code);
                    return;
                }
            }
        });
        manager.get();
    }
}
