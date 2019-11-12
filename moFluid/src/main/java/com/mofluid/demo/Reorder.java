package com.mofluid.magento2;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.ebizon.fluid.Utils.Validation;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.ShoppingItem;
import com.ebizon.fluid.model.ShoppingItemManager;
import com.ebizon.fluid.model.SimpleShoppingItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.fragment.MyCartFragment;
import com.mofluid.magento2.fragment.SimpleProductFragment;
import com.mofluid.magento2.fragment.SimpleProductFragment2;
import com.mofluid.magento2.fragment.ViewOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


// Created by prashant on 6/4/16.
public class Reorder {
    private String product_id;
    private String product_name;
    private Activity context;
    private ShoppingItem currentItem;
    private String type;
    private String cartImage;
    int listsize;
    private  static int counter=0;

    public Reorder(String product_id, String product_name,Activity context,int listsize)
    {
        this.product_id=product_id;
        this.product_name=product_name;
        this.context=context;
        this.listsize=listsize;
        hitServiceForImages();
        hitserviceforreorder();
    }

    private void hitserviceforreorder() {
        String urlFeatureProducts = WebApiManager.getInstance().getProductDetailURL(context);
        urlFeatureProducts = String.format(urlFeatureProducts, product_id.trim());
        Log.d("productdetails", urlFeatureProducts);

        final ProgressDialog pDialog = new ProgressDialog(context, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.show();


        NetworkAPIManager.getInstance(context).sendGetRequest(urlFeatureProducts, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d("Reorder response", response);
                Log.d("Volley= ", response);

                try {

                    JSONObject strJSNobj = new JSONObject(response);
                    String name = strJSNobj.getString("name");
                    String quantity = strJSNobj.getString("quantity");
                    String price = strJSNobj.getString("price");
                    String sprice = strJSNobj.getString("sprice");
                    String sku = strJSNobj.getString("sku");
                    type = strJSNobj.getString("type");
                    String img = strJSNobj.getString("img");
                    if(cartImage==null)
                    {
                        cartImage="";
                    }
                    if (!Validation.isNull(product_id, product_name, sku, cartImage, price, sprice, quantity)) {

                        currentItem = new SimpleShoppingItem(product_id, product_name, sku, cartImage, price, sprice, "1", quantity, type,img);// edited by prashant
                        ShoppingItemManager.getInstance().addShoppingItem(currentItem);
                        new SimpleProductFragment2(currentItem,context);
                    }
                    counter++;
                    pDialog.hide();
                    if(counter==listsize)
                    {
                        MyCartFragment myCartFragment=new MyCartFragment();
                        FragmentManager fm = context.getFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(context.getApplicationContext(), fragmentTransaction, new ViewOrder(), myCartFragment, R.id.content_frame);
        /*
        index 7 is used to slide fragment Left to right and vise versa
         */
                        String fragmentName = myCartFragment.getClass().getSimpleName();
                        fragmentTransactionExtended.addTransition(7);
                        if (fragmentName != null) {
                            fragmentTransaction.addToBackStack(fragmentName);
                        }
                        fragmentTransactionExtended.commit();
                        counter=0;
                    }

                } catch (JSONException e) {
                    pDialog.hide();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: Feature Pro " + error.getMessage());


            }
        });
    }
    private void hitServiceForImages() {

        String urlImages = WebApiManager.getInstance().getProductImagesURL(context);
        urlImages = String.format(urlImages, product_id.trim());

        NetworkAPIManager.getInstance(context).sendGetRequest(urlImages, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Image Response", response);
                try {
                    JSONObject strJSNobj = new JSONObject(response);
                    JSONArray imageJSNArray = strJSNobj.getJSONArray("image");
                    for (int i = 0; i < imageJSNArray.length(); i++) {
                        if (i == 0) {
                            cartImage = imageJSNArray.getString(i);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Something Went wrong in loading image", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error Response", "Error: Feature Pro " + error.getMessage());

            }
        });
    }



}


