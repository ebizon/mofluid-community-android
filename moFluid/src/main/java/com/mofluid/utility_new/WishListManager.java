package com.mofluid.utility_new;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserProfileItem;
import com.ebizon.fluid.model.WebApiManager;
import com.ebizon.fluid.model.WishListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Modified by saddam on 2nd aug.
 */
public class WishListManager{
    private static WishListManager instance;

    public static WishListManager getInstance(){
        if(instance==null)
            instance=new WishListManager();
        return instance;
    }

    private WishListManager() {
    }
public void getWishList(final Callback callback){
     String url=WebApiManager.getInstance().getWishList();
     UserProfileItem user = UserManager.getInstance().getUser();
     if(user==null){
         callback.callback(null,0);
         return;
     }
     final String finalUrl = String.format(url, user.getId());
        HashMap<String,String> header=new HashMap<>();
        ResponseManager manager=new ResponseManager(ResponseManager.REQUEST_TYPE_GET, finalUrl, header, null, new ResponseReceived() {
            @Override
            public void onResponseReceived(Object response, int response_code) {
                             if(response==null){
                                 callback.callback(null,response_code);
                                 return;
                             }
                try{ ArrayList<WishListItem> myWishList=new ArrayList<>();
                    JSONArray list=new JSONArray((String) response);
                    int n=list.length();
                    for(int i=0;i<n;i++){
                        JSONObject obj=list.getJSONObject(i);
                        JSONObject product=obj.getJSONObject("product");
                        String item_id=obj.getString("wishlist_item_id");
                        String name=product.getString("name");
                        String sku=product.getString("sku");
                        String id=product.getString("entity_id");
                        String price=product.getString("price");
                        String sprice=product.getString("minimal_price");
                        String image=product.getString("thumbnail");
                        image=WebApiManager.getInstance().getCatalogProductImagePath()+image;
                        WishListItem listitem = new WishListItem(id, name, price, sprice, image, sku,item_id);
                        myWishList.add(listitem);
                    }
                    callback.callback(myWishList,response_code);
                }catch (JSONException e){
                   callback.callback(null,response_code);
                }

            }
        });
        manager.get();
    }
public void deleteWishListItem(final String product_id, final Callback callback){
    this.getWishList(new Callback() {
        @Override
        public void callback(Object o, int response_code) {
                             if(o==null){
                                 callback.callback(null,response_code);
                                 return;
                             }
            ArrayList<WishListItem> list=(ArrayList<WishListItem>)o;
            for(int i=0;i<list.size();i++){
                if(list.get(i).getId().equalsIgnoreCase(product_id))
                {
                 deleteWishListItemUtil(list.get(i).getItem_id(),callback);
                }
            }
        }
    });
}
public void deleteWishListItemUtil(String item_id,final Callback callback){
    String url = WebApiManager.getInstance().removeFromWishList();
    UserProfileItem user = UserManager.getInstance().getUser();
    if(user==null){
        callback.callback(null,0);
        return;
    }
    String finalUrl = String.format(url, user.getId(),item_id);
    HashMap<String,String> header=new HashMap<>();
   ResponseManager manager=new ResponseManager(ResponseManager.REQUEST_TYPE_GET, finalUrl, header, null, new ResponseReceived() {
       @Override
       public void onResponseReceived(Object response, int response_code) {
           if(response==null){
               callback.callback(null,response_code);
               return;
           }
         callback.callback("success",response_code);
       }
   }) ;
   manager.get();
}
public void addItemToWishList(String product_id,final Callback callback){
    UserProfileItem user = UserManager.getInstance().getUser();
    if(user==null){
        callback.callback(null,0);
        return;
    }
    String userid = user.getId();
    String url = WebApiManager.getInstance().addToWishList();
    url = String.format(url, userid, product_id);
    HashMap<String,String> header=new HashMap<>();
    ResponseManager manager=new ResponseManager(ResponseManager.REQUEST_TYPE_GET, url, header, null, new ResponseReceived() {
        @Override
        public void onResponseReceived(Object response, int response_code) {
            if(response==null){
                callback.callback(null,response_code);
                return;
            }
            callback.callback("success",response_code);
        }
    }) ;
    manager.get();

}
}
