package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.FilterMyProducts;
import com.ebizon.fluid.model.FilterOption;
import com.ebizon.fluid.model.FilterOptionItem;
import com.mofluid.magento2.R;
import com.mofluid.magento2.manager.FilterManager;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ebizon on 1/7/16.
 */
public class MyFilterOptionAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final ArrayList<FilterOptionItem> arrayList;
    private final Typeface open_sans_semiregular;
    private final FilterMyProducts filterInstance;
    private final Activity activity;
    String keyid;

    public MyFilterOptionAdapter(Activity activity, ArrayList<FilterOptionItem> arrayList, FilterMyProducts filterInstance, String id) {
        inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList=arrayList;
        this.filterInstance=filterInstance;
        open_sans_semiregular= Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
        this.activity=activity;
        this.keyid = id;
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
            convertView=inflater.inflate(R.layout.simple_list_filter_option,null);
            holder.checkboxId=(CheckBox)convertView.findViewById(R.id.checkboxId);
            holder.txtVIdCounter=(TextView) convertView.findViewById(R.id.txtVIdCounter);
            convertView.setTag(holder);
        }
        else
            holder=(ViewHolder)convertView.getTag();
        if(i==0)
            updateSelectedFilterList();
        final FilterOptionItem optionItem = arrayList.get(i);
        holder.checkboxId.setText(optionItem.getName());
        if(optionItem.getId().equals(FilterManager.getInstance().selectedFilterOptionID))
        holder.checkboxId.setChecked(true);
        else
        holder.checkboxId.setChecked(optionItem.isChecked());

        String counter=activity.getResources().getString(R.string.filter_counter);
        counter=String.format(counter,optionItem.getCounter());
        if(counter!=null && counter.trim().length()>0 && !counter.equalsIgnoreCase("()")) {
            holder.txtVIdCounter.setText(counter);
            holder.txtVIdCounter.setVisibility(View.GONE);
        }
        else {
            holder.txtVIdCounter.setVisibility(View.GONE);
        }
        holder.checkboxId.setTypeface(open_sans_semiregular);
        holder.txtVIdCounter.setTypeface(open_sans_semiregular);

        holder.checkboxId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox ch=(CheckBox)v;
                //resetAllChecked(MyFilterOptionAdapter.this.arrayList);
                if(ch.isChecked())
                {
                    optionItem.setChecked(true);
                    updateSelectedFilterList();
                }
                else
                {
                    optionItem.setChecked(false);
                    removeSelectedFilterList(optionItem.getId());
                }
                filterInstance.setUpdateList(MyFilterOptionAdapter.this.arrayList);
                MyFilterOptionAdapter.this.notifyDataSetChanged();

            }
        });
        view=convertView;
        return view;
    }

    private void removeSelectedFilterList(String v) {
        HashMap<String,ArrayList<FilterOptionItem>> list = filterInstance.getSelectedList();
        if(list!=null) {
            ArrayList<FilterOptionItem> mylist = list.get(keyid);
            if (mylist != null) {
                for (int i = 0; i < mylist.size(); i++) {
                    FilterOptionItem item = mylist.get(i);
                    if (item.getId().equals(v))
                        item.setChecked(false);
                }
                ArrayList<FilterOptionItem> finall = new ArrayList<>(mylist);
                list.get(keyid).clear();
                list.get(keyid).addAll(finall);
            }
        }
    }

    private void updateSelectedFilterList() {
       /* HashMap<String,ArrayList<FilterOptionItem>> list = filterInstance.getSelectedList();
        if(list.containsKey(keyid))
        {
            ArrayList<FilterOptionItem> options= list.get(keyid);
            options= this.arrayList;
            filterInstance.setSelectedList(keyid,options);
        }
        else
        {
            ArrayList<FilterOptionItem> selectlist = new ArrayList<>();
            for (int i = 0; i < this.arrayList.size(); i++) {
                if(this.arrayList.get(i).isChecked()==true)
                    selectlist.add(this.arrayList.get(i));
            }
            filterInstance.setSelectedList(keyid,this.arrayList);
        }*/
    }

    public static  void resetAllChecked(ArrayList<FilterOptionItem> arrayList) {
        if (arrayList != null) {
            for (int i = 0; i < arrayList.size(); i++) {
                FilterOptionItem item = arrayList.get(i);
                item.setChecked(false);
            }
        }
        FilterManager.getInstance().selectedFilterOption = null;
        FilterManager.getInstance().selectedFilterOptionID = null;
    }

    class ViewHolder
    {
        public  CheckBox checkboxId;
        TextView txtVIdCounter;
    }
}
