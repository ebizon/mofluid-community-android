package com.mofluid.magento2;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.ConvertValues;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.ReoredrItem;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.ShoppingCartItem;
import com.ebizon.fluid.model.ShoppingItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.adapter.OutOfStockAdapter;
import com.mofluid.magento2.fragment.HomeFragment;
import com.mofluid.magento2.fragment.MyCartFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ebizon201 on 1/11/16.
 */
public class MyReoder {

    public void hitserviceforreorder(final Activity context, String productIds, String ordrId) {
        String urlFeatureProducts = WebApiManager.getInstance().getReoredURL(context);
        urlFeatureProducts = String.format(urlFeatureProducts, productIds,ordrId.trim());
        Log.d("hitserviceforreorder", urlFeatureProducts);
        final ProgressDialog pDialog = new ProgressDialog(context, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.show();


        NetworkAPIManager.getInstance(context).sendGetRequest(urlFeatureProducts, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Reorder response", response);
                int count=0;
                String out_of_stock="";
                if(pDialog.isShowing())
                    pDialog.cancel();
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    ArrayList<ReoredrItem> outOfStockProducts=new ArrayList<ReoredrItem>();
                    if(jsonArray!=null && jsonArray.length()>0) {
                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObj=jsonArray.getJSONObject(i);
                            String id=jsonObj.getString("id");
                          /*  if(stock_status!=null && stock_status.equalsIgnoreCase("0"))*/ {
                                String name=jsonObj.getString("name");
                               // String attributevalue=jsonObj.getString("attributevalue");
                                String sku=jsonObj.getString("sku");
                                String price=jsonObj.getString("price");
                                String image=jsonObj.getString("image");
                                String sprice=jsonObj.getString("sprice");
                                String type=jsonObj.getString("type");
                                JSONObject quantity=jsonObj.getJSONObject("quantity");
                                String order=quantity.getString("order");
                                String available=quantity.getString("available");
                                String pro_type=jsonObj.getString("type");
                                boolean is_in_stock=jsonObj.getBoolean("is_in_stock");
                                String stockQuantity=available;
                               // outOfStockProducts.add(new ReoredrItem(name,sprice,price,image));
                                if(is_in_stock) {
                                    ShoppingItem simpleItem = new ShoppingItem(id, name, sku, image, price, sprice, is_in_stock+"", stockQuantity, type, image, null, null);
                                    ShoppingCartItem shopItem = new ShoppingCartItem(simpleItem, ConvertValues.getInstance().convertStrToInt(order));
                                    ShoppingCart.getInstance().addItem(shopItem);
                                    ++count;
                                }
                                else{
                                    out_of_stock=out_of_stock+" "+name;
                                }
                            }

                        }

                       /* if(outOfStockProducts!=null && outOfStockProducts.size()>0)
                            showOutOfStockProducts(context,outOfStockProducts);
                        else {
                            callFragment(context);
                        }*/
                    }
                    if(count>0)
                    callFragment(context);
                    else
                        Toast.makeText(context,R.string.all_item_out_of_stock,Toast.LENGTH_SHORT).show();
                    if(count<jsonArray.length())
                        Toast.makeText(context,out_of_stock+" items are out of stock.",Toast.LENGTH_SHORT).show();


                }
                catch (Exception ex) {
                    Toast.makeText(context,context.getResources().getString(R.string.something_wen_wrong), Toast.LENGTH_SHORT).show();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: Feature Pro " + error.getMessage());
                if(pDialog.isShowing())
                    pDialog.cancel();

                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }



    private void showOutOfStockProducts(final Activity context, ArrayList<ReoredrItem> outOfStockProducts) {
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.CENTER;

        dialog.setContentView(R.layout.dialogbox_show_out_of_stock_products);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        Button btnBialog = (Button) dialog.findViewById(R.id.btnBialog);
        Typeface open_sans_semibold = Typeface.createFromAsset(context.getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);

        TextView txtVTitle = (TextView) dialog.findViewById(R.id.txtVTitle);
        txtVTitle.setTypeface(open_sans_semibold);
        btnBialog.setTypeface(open_sans_semibold);
        ListView listVOutOfStock = (ListView) dialog.findViewById(R.id.listVOutOfStock);
        OutOfStockAdapter myAdapter = new OutOfStockAdapter(context, outOfStockProducts);
        listVOutOfStock.setAdapter(myAdapter);

        dialog.setCancelable(false);

        btnBialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                callFragment(context);
            }
        });
        dialog.show();


    }
    private void callFragment(Activity context) {
        MyCartFragment frmnt = new MyCartFragment();
        FragmentManager fm = context.getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(context, fragmentTransaction, new HomeFragment(), frmnt, R.id.content_frame);
        /*
        index 7 is used to slide fragment Left to right and vise versa
         */
        fragmentTransactionExtended.addTransition(7);
//        if(fragmentName != null) {
//            fragmentTransaction.addToBackStack(fragmentName);
//        }
        fragmentTransactionExtended.commit();
    }


}
