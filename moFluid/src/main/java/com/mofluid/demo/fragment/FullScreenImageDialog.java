package com.mofluid.magento2.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.ShoppingItem;
import com.ebizon.fluid.model.ShoppingItemManager;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.ProductDetailBannerAdapter;
import com.mofluid.magento2.service.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by piyush on 15/3/16.
 */
public class FullScreenImageDialog {

    static public int FLAG = 0;
    private final Activity activity;
    Button btnClose;
    int value;
    private String PRODUCT_ID;
    private ViewPager viewPager_banner2;
    private ProductDetailBannerAdapter productDetailBannerAdapter;
    private ProgressBar progressBar_imgLoading2;
    private ArrayList<String> imgListStrArry;


    public FullScreenImageDialog(Activity act) {
        activity = act;
    }

    private void initialize(Dialog nagDialog) {
        viewPager_banner2 = (ViewPager) nagDialog.findViewById(R.id.viewPager_banner2);
        btnClose = (Button) nagDialog.findViewById(R.id.btnIvClose);
        progressBar_imgLoading2 = (ProgressBar) nagDialog.findViewById(R.id.progressBar_imgLoading2);
    }


    public void displayDialog(int position, String productID) {
        // set dialogbox properties.
        final Dialog nagDialog = new Dialog(activity,R.style.PauseDialog);
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setCancelable(false);
        nagDialog.setContentView(R.layout.dialog_image);
        value = position;
        initialize(nagDialog);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FLAG = 1;
                nagDialog.getWindow().setWindowAnimations(
                        R.style.dialog_animation_fade);
                nagDialog.dismiss();
            }
        });

        hitServiceForImages(productID);
        setViewPager(productID);
        nagDialog.getWindow().setWindowAnimations(
                R.style.dialog_animation_fade);
        nagDialog.show();
    }

    private void setViewPager(String proDuctID ) {
        productDetailBannerAdapter = new ProductDetailBannerAdapter(activity, imgListStrArry,proDuctID);
        FLAG = 2;
        if (imgListStrArry != null)
            viewPager_banner2.setAdapter(productDetailBannerAdapter);
        viewPager_banner2.setCurrentItem(value);
    }

    private void hitServiceForImages(String PRODUCT_ID) {
        PRODUCT_ID = SimpleProductFragment2.getProductID();
        String urlImages = WebApiManager.getInstance().getProductImagesURL(activity);
        urlImages = String.format(urlImages, PRODUCT_ID.trim());

        final String finalPRODUCT_ID = PRODUCT_ID;
        NetworkAPIManager.getInstance(activity).sendGetRequest(urlImages, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject strJSNobj = new JSONObject(response);
                    JSONArray imageJSNArray = strJSNobj.getJSONArray("image");
                    imgListStrArry = new ArrayList<>();

                    for (int i = 0; i < imageJSNArray.length(); i++) {
                        imgListStrArry.add(imageJSNArray.getString(i));
                    }
                    ShoppingItem item = ShoppingItemManager.getInstance().getShoppingItem(finalPRODUCT_ID);

                    if (item != null) {
                        item.setImageList(imgListStrArry);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressBar_imgLoading2.setVisibility(View.GONE);
                viewPager_banner2.setVisibility(View.VISIBLE);
                setViewPager(finalPRODUCT_ID);
                productDetailBannerAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
            }
        });
    }
}
