package com.ebizon.fluid.Utils;

import android.os.AsyncTask;
import android.util.Log;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.ShoppingCartItem;
import com.ebizon.fluid.model.ShoppingItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.database.MyDataBaseAdapter;
import com.mofluid.magento2.service.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by saddam on 19/3/18.
 */

public class CartSynckManager {
    private final String TAG="CartSynckManager";
    private static CartSynckManager instance;
    private int flag;
    private MyDataBaseAdapter myDataBaseAdapter;
    private CartSynckManager(){
this.myDataBaseAdapter=new MyDataBaseAdapter(AppController.getContext());
    }
    public static CartSynckManager getInstance(){
        if(instance==null)
            instance=new CartSynckManager();
        return instance;

    }
public void clearCart(int customer_id){
          String url= WebApiManager.getInstance().clearCartURL();
          url=String.format(url,customer_id);
          this.flag=0;
          new CartAsynckTask().execute(url);
    }
public void updateCartItem(int customer_id,int product_id,int count){
String url=WebApiManager.getInstance().getcartURL();
url=String.format(url,customer_id,product_id,count);
this.flag=1;
new CartAsynckTask().execute(url);

}
public void getCartItems(int customer_id){
    String url=WebApiManager.getInstance().getcartURL();
    url=String.format(url,customer_id);
    this.flag=2;
    new CartAsynckTask().execute(url);

}
public void getCartItemsUtil(String response){
try{
    JSONArray array=new JSONArray(response);
    int n=array.length();
    for(int i=0;i<n;i++){
        JSONObject object=array.getJSONObject(i);
        int prod_id=Integer.parseInt(object.getString("item_id"));
        String sku=object.getString("sku");
        String name=object.getString("name");
        float price=Float.parseFloat(object.getString("price"));
        float spclprice=price;
        int is_in_stock=1;
        String img=null;
        String type=object.getString("product_type");
        int count=Integer.parseInt(object.getString("qty"));
        JSONArray medias=object.getJSONArray("media");
        for(int j=0;j<medias.length();j++){
         JSONObject media=medias.getJSONObject(j);
         JSONArray types=media.getJSONArray("types");
         for(int k=0;k<types.length();k++){
             String media_type=types.getString(k);
             if(media_type.equalsIgnoreCase("thumbnail"))
                 img=WebApiManager.getInstance().getBaseURL()+media.getString("file");

         }
        }
        ShoppingItem item=new ShoppingItem(prod_id+"",name,sku,img,price+"",spclprice+"",is_in_stock+"",count+"",type);
       ShoppingCartItem cartItem=new ShoppingCartItem(item,count);
       this.myDataBaseAdapter.insertItemtoCart(cartItem);

    }
}catch (JSONException e){}
}
public void insertItemtoCart(int customer_id,String item_data){
String url=WebApiManager.getInstance().getInsertCartItemURL();
url=String.format(url,customer_id,item_data);
this.flag=3;
new  CartAsynckTask().execute(url);

}
public void removeItemFromCart(int customer_id,int item_id){
String url=WebApiManager.getInstance().getRemoveCartItemURL();
url=String.format(url,customer_id,item_id);
this.flag=4;
new CartAsynckTask().execute(url);
}
private class CartAsynckTask extends AsyncTask<String,String,String>{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(final String... strings) {
        NetworkAPIManager.getInstance(AppController.getContext()).sendGetRequest(strings[0], new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"Url="+strings[0]);
                        Log.d(TAG,"Response="+response);
                             if(flag==2){
                                 getCartItemsUtil(response);
                             }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
}
