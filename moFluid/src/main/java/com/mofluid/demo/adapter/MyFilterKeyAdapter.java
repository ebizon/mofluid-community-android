package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.FilterMyProducts;
import com.ebizon.fluid.Utils.GetFilter;
import com.ebizon.fluid.model.FilterKeyItem;
import com.mofluid.magento2.R;


import java.util.ArrayList;

/**
 * Created by ebizon on 1/7/16.
 */
public class MyFilterKeyAdapter  extends BaseAdapter {
    private final LayoutInflater inflater;
    private final ArrayList<FilterKeyItem> arrayList;
    private final Activity activity;
    private final Typeface open_sans_semibold;
    private final Typeface open_sans_semiregular;
    private View previousTextView;
    boolean isFirsTime=true;

    public MyFilterKeyAdapter(Activity activity, ArrayList<FilterKeyItem> arrayList, FilterMyProducts filterKeyinstance) {
        inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList=arrayList;
        this.activity=activity;
        open_sans_semibold= Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
        open_sans_semiregular= Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);

    }

    public MyFilterKeyAdapter(Activity activity, ArrayList<FilterKeyItem> arrayList) {
        inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList=arrayList;
        this.activity=activity;
        open_sans_semibold= Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
        open_sans_semiregular= Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);

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
        final ViewHolder holder;
        View view;
        if(convertView==null)
        {
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.simple_list_filter_key,null);
            holder.countryNam=(TextView)convertView.findViewById(R.id.txtV_country_name);
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();

        final FilterKeyItem categoryItem = arrayList.get(i);
         final String name=categoryItem.getName();
        holder.countryNam.setText(name);
        holder.countryNam.setId(i);
        holder.countryNam.setTypeface(open_sans_semibold);

        if( isFirsTime && i==0) {
            holder.countryNam.setBackgroundColor(ContextCompat.getColor(activity, R.color.bg_filter_color));
            previousTextView=holder.countryNam;
            isFirsTime=false;
        }
        holder.countryNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.countryNam.setBackgroundColor(ContextCompat.getColor(activity, R.color.bg_filter_color));

                GetFilter.getInstance(activity).changeFilterOption(categoryItem);

                Log.d("MyFilterKeyAdapter", "onClick() called with: " + "v = [" + v.getId() + "]  prev "+previousTextView.getId());
                if(previousTextView!=null && previousTextView.getId()!= v.getId())
                    previousTextView.setBackgroundColor(ContextCompat.getColor(activity, R.color.bg_white));

                previousTextView= v;
            }
        });

        view=convertView;
        return view;
    }

    class ViewHolder
    {
        public  TextView countryNam;
    }
}
