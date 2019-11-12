package com.mofluid.magento2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.mofluid.magento2.R;

import java.util.ArrayList;

/**
 * Created by prashant-ios on 5/5/16.
 */
public class DownloadlistAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<DownloadProducts> products;
    ArrayList<DownloadProducts> products_complete;
    LayoutInflater layoutInflater;
    public DownloadlistAdapter(Activity activity, ArrayList<DownloadProducts> listDownloadProducts) {
        this.activity=activity;
        this.products=listDownloadProducts;
        layoutInflater=(LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }
    public DownloadlistAdapter(Activity activity, ArrayList<DownloadProducts> listDownloadProducts_small,ArrayList<DownloadProducts> listDownloadProducts) {
        this.activity=activity;
        this.products=listDownloadProducts_small;
        this.products_complete=listDownloadProducts;
        layoutInflater=(LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         convertView=layoutInflater.inflate(R.layout.my_downloads_list,null);
        TextView product_name=(TextView)convertView.findViewById(R.id.mydownloads_product_name);
        TextView order_date=(TextView)convertView.findViewById(R.id.mydownloads_orderdate);
        TextView order_remain=(TextView)convertView.findViewById(R.id.mydownloads_orderremain);
        TextView order_status=(TextView)convertView.findViewById(R.id.mydownloads_orderstatus);
        TextView navigat_txt=(TextView)convertView.findViewById(R.id.navigatetxt_downloads);
        navigat_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDownloadsFragment myDownloadsFragment=new MyDownloadsFragment(products_complete);
                callfragment(myDownloadsFragment);
            }
        });
        if(products.size()>=6)
        {
            if(position==products.size()-1){
                navigat_txt.setVisibility(View.VISIBLE);
            }
        }
        product_name.setText(products.get(position).getProduct_name());
        order_date.setText(products.get(position).getOrder_date());
        order_remain.setText(products.get(position).getOrder_remain());
        order_status.setText(products.get(position).getOrder_status());
        final String product_url=products.get(position).getDownload_url();
        TextView download_btn=(TextView)convertView.findViewById(R.id.download_links);
        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goWebPage=new Intent(Intent.ACTION_VIEW);
                goWebPage.setData(Uri.parse(product_url));
                activity.startActivity(goWebPage);
            }
        });


        return convertView;
    }
    public void callfragment(Fragment orderAcknowledgeFragment) {
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(activity.getApplicationContext(), fragmentTransaction, new ViewOrder(), orderAcknowledgeFragment, R.id.content_frame);
        /*
        index 7 is used to slide fragment Left to right and vise versa
         */
        String fragmentName = orderAcknowledgeFragment.getClass().getSimpleName();
        fragmentTransactionExtended.addTransition(7);
        if (fragmentName != null) {
            fragmentTransaction.addToBackStack(fragmentName);
        }
        fragmentTransactionExtended.commit();
    }


}
