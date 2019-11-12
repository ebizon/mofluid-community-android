package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.ebizon.fluid.Utils.MyOnClickListener;
import com.ebizon.fluid.model.BannerItem;
import com.mofluid.magento2.service.AppController;

import java.util.ArrayList;

/**
 * Adapter class to show banners on HomeFragment using ViewPager
 */
public class BannerAdapter extends PagerAdapter {

    private final ArrayList<BannerItem> bannerDataList;
    MyOnClickListener myOnClickListener;
    /* public ImageLoader imageLoader;*/
    private com.android.volley.toolbox.ImageLoader imageLoaderVolley;

    public BannerAdapter(ArrayList<BannerItem> bannerDataList, MyOnClickListener myOnClickListener) {

        this.bannerDataList = bannerDataList;
        /* imageLoader = new ImageLoader(activity.getApplicationContext());*/
        this.myOnClickListener = myOnClickListener;
        imageLoaderVolley = AppController.getInstance().getImageLoader();
    }

    public int getCount() {
        return bannerDataList.size();
    }

    public Object instantiateItem(final ViewGroup collection, final int position) {

        NetworkImageView thumbNail = new NetworkImageView(collection.getContext());
        thumbNail.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        thumbNail.setScaleType(ImageView.ScaleType.FIT_XY);
        String url = (bannerDataList.get(position).getMofluid_image_value());
        Log.d("Image Path= ", url);
        thumbNail.setImageUrl(url, imageLoaderVolley);
        thumbNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myOnClickListener.myOnClick(collection, "TAG", position);
            }
        });
        collection.addView(thumbNail, 0);
        return thumbNail;
    }

    @Override
    public void destroyItem(ViewGroup collection, int arg1, Object view) {
        collection.removeView((View) view);
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