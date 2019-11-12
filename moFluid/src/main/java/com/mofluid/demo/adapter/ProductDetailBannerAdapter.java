package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.mofluid.magento2.R;
import com.mofluid.magento2.fragment.FullScreenImageDialog;
import com.mofluid.magento2.fragment.SimpleProductFragment2;
import com.mofluid.magento2.service.AppController;

import java.util.ArrayList;

/**
 * Created by ebizon on 19/11/15.
 */
public class ProductDetailBannerAdapter extends PagerAdapter {
    private final Activity activity;
    private final ArrayList<String> bannerDataList;
    private final String TAG;
    private String  productID;
    private ImageView img;

    private com.android.volley.toolbox.ImageLoader imageLoaderVolley = AppController.getInstance().getImageLoader();

    public ProductDetailBannerAdapter(Activity act, ArrayList<String> bannerDataList,String productID) {
        activity = act;
        this.bannerDataList = bannerDataList;
        imageLoaderVolley = AppController.getInstance().getImageLoader();
        TAG = getClass().getSimpleName();
        this.productID=productID;


    }
    public ProductDetailBannerAdapter(Activity act, ArrayList<String> bannerDataList,ImageView img) {
        activity = act;
        this.bannerDataList = bannerDataList;
        TAG = getClass().getSimpleName();
        this.img = img;
    }



    public int getCount() {
        return bannerDataList.size();
    }

    public Object instantiateItem(View collection, final int position) {
        final ImageView view = new ImageView(activity);
        view.setImageResource(R.drawable.default_mofluid);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);

        final String url = bannerDataList.get(position);
        if(position==0)
            SimpleProductFragment2.cartImage = url;

        Log.d(TAG, url);

        if (url != null)
            imageLoaderVolley.get(url, ImageLoader.getImageListener(
                    view, R.drawable.default_mofluid, R.drawable.default_mofluid));
        else
            view.setBackgroundResource(R.drawable.default_mofluid);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FullScreenImageDialog.FLAG == 1) {
                    FullScreenImageDialog fs = new FullScreenImageDialog(activity);
                    fs.displayDialog(position,productID);
                }
            }
        });

        ((ViewPager) collection).addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}