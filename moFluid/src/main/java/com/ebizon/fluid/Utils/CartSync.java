package com.ebizon.fluid.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.ShoppingCartItem;
import com.ebizon.fluid.model.ShoppingItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.fragment.BaseFragment;
import com.mofluid.magento2.fragment.MyCartFragment;
import com.mofluid.magento2.fragment.PaymentTypeFragment;
import com.mofluid.magento2.fragment.SignInSignUpFragment;
import com.mofluid.magento2.fragment.SimpleProductFragment2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


/**
 * Created by piyush-ios on 18/8/16.
 */
class CartSync1 {

   // private static CartSync ourInstance = new CartSync();
    private final String TAG = "Piyush";
    ShoppingCart appCart;
    Context context;
    Collection<ShoppingCartItem> appCartItems;
    private BaseFragment fragment=null;

    /*private CartSync() {
        appCart = ShoppingCart.getInstance();
        appCartItems = ShoppingCart.getInstance().getCartItems();
    }*/

   /* public static CartSync getInstance() {
        ourInstance.refreshAppCartList();
        return ourInstance;
    }*/

    private void refreshAppCartList() {
        appCart = ShoppingCart.getInstance();
        appCartItems = ShoppingCart.getInstance().getCartItems();
    }

    public void UpdateAppCart(String customerId, final IResponseListener mylistener) {

        final ArrayList<ShoppingCartItem> updatedList = new ArrayList<>();
        String url = WebApiManager.getInstance().getUserCartFromServer(context);
        url = String.format(url, customerId);
        Log.d("Piyush", "Service to sync server cart to app called with following URL : " + url);
        NetworkAPIManager.getInstance(context).sendGetRequest(url, new Response.Listener<String>() {
            String result = "";


            @Override
            public void onResponse(String response) {
                Log.d("Piyush", response);
                Log.d("Volley= ", response);
                JSONArray jsnArray = null;
                ShoppingCartItem cartItem=null;
//
                try {
                    JSONObject strJSNobj = new JSONObject(response);
                    jsnArray = strJSNobj.getJSONArray("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        JSONArray strar = new JSONArray(response);
                    }catch (Exception e1){}
                }
                // logic to check for empty cart and also to add items into local cart
                if (jsnArray == null || jsnArray.length() == 0) {
                    //empty cart from server
                    mylistener.onResponse(false, updatedList);
                } else {
                    // updaet cart
                    for (int i = 0; i < jsnArray.length(); i++) {
                        JSONObject singleItem = null;
                        try {
                            singleItem = jsnArray.getJSONObject(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (singleItem != null) {
                            try {
                                String id = singleItem.getString("id");
                                String name = singleItem.getString("name");
                                String sku = singleItem.getString("sku");
                                String type = singleItem.getString("type");
                                String price = singleItem.getString("price");
                                String spclprice = singleItem.getString("spclprice");
                                String is_in_stock = singleItem.getString("is_in_stock");
                                String cartQuantity = singleItem.getString("quantity");
                                String imageUrl = singleItem.getString("imageurl");
                                String stockQuantity = singleItem.getString("stock_quantity");
                                String thumbnail = singleItem.getString("img");
                                String parentID = singleItem.getString("parentId");

                                ShoppingItem newItem = new ShoppingItem(id,name,sku,imageUrl,price,spclprice, is_in_stock, stockQuantity ,type,thumbnail,parentID);
                                int qty = 1;
                                try {
                                    qty = Integer.parseInt(cartQuantity);

                                }catch (Exception e)
                                {
                                    qty = 1;
                                }
                                cartItem = new ShoppingCartItem(newItem,qty);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(cartItem!=null) {
                            ShoppingCart.getInstance().addItemFromServer(cartItem);
                            updatedList.add(cartItem);
                        }
                    }
                    mylistener.onResponse(true, updatedList);
                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                mylistener.onResponse(false, updatedList);
            }
        });


    }

    public void updateServerCartAdd(String customerId, String itemID, int quantity, BaseFragment simpleProductFragment2) {
        this.fragment = simpleProductFragment2;
        String url = WebApiManager.getInstance().addToCartServer(context);
        url = String.format(url, customerId, itemID, quantity);
        SyncToServer updateServerCart = new SyncToServer();
        updateServerCart.execute(url);
    }

    public void updateServerCartAdd2(String customerId, String itemID, int quantity, SignInSignUpFragment simpleProductFragment2) {
        this.fragment = simpleProductFragment2;
        String url = WebApiManager.getInstance().addToCartServer(context);
        url = String.format(url, customerId, itemID, quantity);
        SyncToServer updateServerCart = new SyncToServer();
        updateServerCart.delegate = simpleProductFragment2;
        updateServerCart.execute(url);
    }

    public void updateServerCartDelete(String customerId, String itemID) {
        String url = WebApiManager.getInstance().deleteFromCartServer(context);
        url = String.format(url, customerId, itemID);
        DeleteFromServer deleteServerCart = new DeleteFromServer();
        deleteServerCart.execute(url);
    }

    public boolean getAnonymousCartQuantity(String listofProducts, MyCartFragment myCartFragment) {
        this.fragment = myCartFragment;
        String url = WebApiManager.getInstance().getAnonymousCartQuantity(context);
        url = String.format(url, listofProducts);
        GetAnonymousProductQuantity getquantity = new GetAnonymousProductQuantity();
        getquantity.delegate = myCartFragment;
        getquantity.execute(url);
        return true;
    }

    public boolean getAnonymousCartQuantity(String listofProducts, PaymentTypeFragment myCartFragment) {
        this.fragment = myCartFragment;
        String url = WebApiManager.getInstance().getAnonymousCartQuantity(context);
        url = String.format(url, listofProducts);
        GetAnonymousProductQuantity getquantity = new GetAnonymousProductQuantity();
        getquantity.delegate = myCartFragment;
        getquantity.execute(url);
        return true;
    }

    public void setContext(Context context) {
        this.context = context;
    }




    private class SyncToServer extends AsyncTask<String, String, String> {
        public AddCartResponse delegate = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // hit service to add item from cart to server
            Log.d("Piyush", "Service to add cart item to server called with following URL : " + params[0]);
            NetworkAPIManager.getInstance(context).sendGetRequest(params[0], new Response.Listener<String>() {
                String result = "";

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    Log.d("Volley= ", response);
//
                    try {
                        JSONObject strJSNobj = new JSONObject(response);
                        result = strJSNobj.getString("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (result.equals("success")) {
                        Log.d("Piyush", "Successfully added to cart on server");
                        publishProgress("success");
                        // now open cart page
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if(values[0].equals("success")) {
                if(delegate!=null)
                delegate.processFinish(true);
                if (fragment != null)
                    if (fragment.getClass().getSimpleName().equals("SimpleProductFragment2")) {
                        SimpleProductFragment2 f = (SimpleProductFragment2) fragment;
                        f.callF();
                        Log.d("PiyushCarySync", "called fragmet name");
                    }
            }

        }

        @Override
        protected void onPostExecute(String s) {
//            Toast.makeText(context, "Updated From App to Server with total " + appCart.getNumDifferentItems() + " items", Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
        }


    }

    private class DeleteFromServer extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
// hit service to add item from cart to server
            Log.d("Piyush", "Service to delete cart item from server called with following URL : " + params[0]);
            NetworkAPIManager.getInstance(context).sendGetRequest(params[0], new Response.Listener<String>() {
                String result = "";

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    Log.d("Volley= ", response);
//
                    try {
                        JSONObject strJSNobj = new JSONObject(response);
                        result = strJSNobj.getString("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (result.equals("success")) {
                        Log.d("Piyush", "Successfully deleted from cart on server");
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
//            Toast.makeText(context, "Deleted From App to Server with total " + appCart.getNumDifferentItems() + " items", Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
        }


    }

    private class GetAnonymousProductQuantity extends AsyncTask<String, String, String> {
        public GuestStockCheck delegate = null;
        public HashMap<String,String> cartproductstock = new HashMap<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
// hit service to add item from cart to server
            Log.d("Piyush", "Service to get ananymous cart quantity : " + params[0]);
            NetworkAPIManager.getInstance(context).sendGetRequest(params[0], new Response.Listener<String>() {
                String result = "";

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    Log.d("Volley= ", response);
//
                    try {
                        JSONArray strJSNarr = new JSONArray(response);
                        for (int i = 0; i <strJSNarr.length() ; i++) {
                            JSONObject currentObj = strJSNarr.getJSONObject(i);
                            cartproductstock.put(currentObj.getString("Product id"),currentObj.getString("Quantity"));
                        }
                        publishProgress("success");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
//            Toast.makeText(context, "Deleted From App to Server with total " + appCart.getNumDifferentItems() + " items", Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
//            if(delegate!=null)
//                delegate.processFinish(cartproductstock);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if(delegate!=null&& values[0].equals("success"))
                delegate.processFinish(cartproductstock);
        }
    }



}

