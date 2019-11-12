package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ebizon.fluid.model.CustomOption;
import com.mofluid.magento2.R;

import java.util.ArrayList;

/**
 * Created by ebizon on 27/11/15.
 */
public class CustomValueDrpDwnAdapter extends BaseAdapter{
    private final LayoutInflater layoutInflater;
    private final ArrayList<CustomOption> list;

    public CustomValueDrpDwnAdapter(Activity activity,  ArrayList<CustomOption> list) {
        this.list=list;
        layoutInflater= activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        VieHolder holder;
        if(view==null) {
            view=layoutInflater.inflate(R.layout.custome_option_simple_list,null);
            holder=new VieHolder();
            holder.txtV_name=(TextView)view.findViewById(R.id.txtV_name);

            view.setTag(holder);

        } else {
            holder = (VieHolder) view.getTag();
        }

        CustomOption option = list.get(i);
        String price=option.getTitleWithPrice();
        holder.txtV_name.setText(price);

        return view;
    }

    public void select(int index){
        for(CustomOption option : list){
            option.setSelected(false);
        }
        if (index > 0){
            list.get(index).setSelected(true);
        }
    }
    class VieHolder {
        TextView txtV_name;
    }
}
