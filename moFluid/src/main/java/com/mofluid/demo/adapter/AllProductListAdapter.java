package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ebizon.fluid.model.SlideMenuListItem;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;

import java.util.List;

/**
 * Created by ebizon on 9/11/15.
 */
public class AllProductListAdapter extends BaseAdapter {

    private final List<SlideMenuListItem> childListData;
    private final LayoutInflater inflater;
    private final Activity activity;
    private final String STR_ALL;

    public AllProductListAdapter(Activity activity, List<SlideMenuListItem> childListData) {
        this.childListData=childListData;
        inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity=activity;
        STR_ALL = activity.getResources().getString(R.string.all);
    }

    @Override
    public int getCount() {
        return childListData.size();
    }

    @Override
    public Object getItem(int i) {
        return childListData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        View row =inflater.inflate(R.layout.row_all_product_list,null);
        holder.productName=(TextView) row.findViewById(R.id.txt_all_product_name);
        holder.txtV_banner_img=(TextView) row.findViewById(R.id.txtV_banner_img);
        row.setTag(holder);
        if(i==0) {
            String Nmae[]=childListData.get(i).getName().split(STR_ALL,2);
            holder.txtV_banner_img.setVisibility(View.VISIBLE);
            holder.txtV_banner_img.setText(Nmae[1]);

        }

        holder.productName.setText(childListData.get(i).getName());

        return row;
    }
    class  ViewHolder
    {
        TextView productName;
        TextView txtV_banner_img;
    }
}
