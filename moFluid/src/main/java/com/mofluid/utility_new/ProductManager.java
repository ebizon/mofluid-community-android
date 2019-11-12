package com.mofluid.utility_new;

import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.model_new.Product;
import com.mofluid.model_new.RespoonseCodeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductManager {
   private final String TAG="ProductManager";
   private static ProductManager instance;
   private ProductManager(){

   }
   public static ProductManager getInstance(){
       if(instance==null)
           instance=new ProductManager();
       return instance;
   }
   public void getSearchedProductList(String needle,String sort_type,String sort_order,long cur_page,long page_size,final Callback callback){
       if(sort_type==null || sort_type.length()<=0)
           sort_type="name";
       if(sort_order==null || sort_order.length()<=0)
           sort_order="ASC";
       String url=URLManager.getSearchProductURL(needle,sort_type,sort_order,cur_page,page_size);
       Map<String,String> header=new HashMap<>();
       ResponseManager manager=new ResponseManager(ResponseManager.REQUEST_TYPE_GET, url, header, null, new ResponseReceived() {
           @Override
           public void onResponseReceived(Object response, int response_code) {
               if(response==null)
               {
                   callback.callback(null,response_code);
                   return;
               }
               try{
                   JSONObject obj = new JSONObject((String) response);
                   JSONArray items=obj.getJSONArray("data");
                   List<Product> list=new ArrayList<>();
                   for(int i=0;i<items.length();i++){
                       JSONObject cur_item=items.getJSONObject(i);
                       Product p=new Product();
                       p.setId(cur_item.getInt("id"));
                       p.setName(cur_item.getString("name"));
                       p.setImage(cur_item.getString("imageurl"));
                       p.setSku(cur_item.getString("sku"));
                       p.setType_id(cur_item.getString("type"));
                       p.setPrice(cur_item.getDouble("spclprice"));
                       p.setIs_in_stock(cur_item.getBoolean("is_in_stock"));
                       p.addcustom_attributes("has_options",cur_item.getString("hasoptions"));
                       p.setQty(cur_item.getInt("stock_quantity"));
                       list.add(p);
                   }
                callback.callback(list,response_code);
               }catch (JSONException ex){
                L.d(TAG,"error="+ex.getMessage());
                callback.callback(null,response_code);
               }
           }
       });
       manager.get();
   }
  public void getProductList(int categoryid,String sort_type,String sort_order,int cur_page,int page_size,final Callback callback){
                    if(sort_type==null || sort_type.length()<=0)
                        sort_type="name";
                    if(sort_order==null || sort_order.length()<=0)
                        sort_order="ASC";
    String url= WebApiManager.getInstance().getCategoryProductsURL();
     url=String.format(url,categoryid,sort_order,sort_type,cur_page,page_size);
      Map<String,String> header=new HashMap<>();
    ResponseManager manager=new ResponseManager(ResponseManager.REQUEST_TYPE_GET, url, header, null, new ResponseReceived() {
        @Override
        public void onResponseReceived(Object response, int response_code) {
                                  if(response==null)
                                  {
                                      callback.callback(null,response_code);
                                      return;
                                  }
            try {
                JSONObject obj = new JSONObject((String) response);
                JSONArray items=obj.getJSONArray("items");
                List<Product> list=new ArrayList<>();
                for(int i=0;i<items.length();i++)
                { Product p=new Product();
                  JSONObject cur=items.getJSONObject(i);
                  int id=cur.getInt("id");
                  String sku=cur.getString("sku");
                  String name=cur.getString("name");
                  int attribute_set_id=cur.getInt("attribute_set_id");
                  int price=cur.getInt("price");
                  int status=cur.getInt("status");
                  int visiblity=cur.getInt("visiblity");
                  String type_id=cur.getString("type_id");
                  p.setId(id);
                  p.setSku(sku);
                  p.setName(name);
                  p.setAttribute_set_id(attribute_set_id);
                  p.setPrice(price);
                  p.setStatus(status);
                  p.setVisibility(visiblity);
                  p.setType_id(type_id);
                  JSONArray custom_attributes=cur.getJSONArray("custom_attributes");
                  for(int j=0;j<custom_attributes.length();j++){
                   JSONObject cObj=custom_attributes.getJSONObject(j);
                   String key=cObj.getString("attribute_code");
                   String value=cObj.getString("value");
                   p.addcustom_attributes(key,value);
                  }
                  list.add(p);
                  callback.callback(list,response_code);
                }
            }catch (JSONException ex){
                L.e(TAG, "Error=" + ex.toString());
                callback.callback(null, RespoonseCodeMap.WRONG_RESPONSE);
            }
        }
    });
    manager.get();
  }
}
