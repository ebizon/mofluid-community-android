package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ebizon.fluid.model.MyCountryItem;
import com.mofluid.magento2.R;

import java.util.ArrayList;

/**
 * Created by ebizon on 16/11/15.
 */
public class MyCountryAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final ArrayList<MyCountryItem> arrayList;

    public MyCountryAdapter(Activity activity, ArrayList<MyCountryItem> arrayList) {
        inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList=arrayList;

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        View view;
        if(convertView==null)
        {
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.simple_list,null);
            holder.countryNam=(TextView)convertView.findViewById(R.id.txtV_country_name);
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();
        holder.countryNam.setText(arrayList.get(i).getCountry_name());
        view=convertView;
        return view;
    }

     class ViewHolder
    {
        public  TextView countryNam;
    }
}
