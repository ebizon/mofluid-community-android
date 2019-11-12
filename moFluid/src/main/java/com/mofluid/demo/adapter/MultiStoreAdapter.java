package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebizon.fluid.Utils.Config;
import com.ebizon.fluid.model.MultiStoreData;
import com.ebizon.fluid.model.MultiStoreView;
import com.mofluid.magento2.R;

import java.util.ArrayList;

/**
 * Created by piyush on 4/5/16.
 */
public class MultiStoreAdapter extends BaseAdapter
{
    Activity context;
    ArrayList<MultiStoreData> storeList;
    LayoutInflater inflater;
    private TextView multistore_item ;
    private ImageView multistore_selected;
    private LinearLayout layout_multistore_layout;

    public MultiStoreAdapter(ArrayList<MultiStoreData> storeList, Activity context) {
        this.context=context;
        this.storeList= storeList;
        inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return storeList.size();
    }

    @Override
    public Object getItem(int position) {
        return storeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView=inflater.inflate(R.layout.multi_store_list_layout, null);
        initialise(rootView);
        setDataToViews(position);
        return rootView;
    }



    private void setDataToViews(int position) {
        multistore_item.setText(storeList.get(position).getStore_Name());
//        if(Config.getInstance().getStoreValue().equals(storeList.get(position).getViews_Store().get()))
//            multistore_selected.setVisibility(View.VISIBLE);
//        else
//            multistore_selected.setVisibility(View.INVISIBLE);
    }

    private void initialise(View rootView) {
        multistore_item= (TextView) rootView.findViewById(R.id.txtV_multistore_list_item);
//        multistore_selected= (ImageView)rootView.findViewById(R.id.imgV_selected_store);
        layout_multistore_layout = (LinearLayout) rootView.findViewById(R.id.ll_row_multistore_item);

    }
}
