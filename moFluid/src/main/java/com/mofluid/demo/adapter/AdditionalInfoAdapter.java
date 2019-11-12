package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.model.AdditionalInfoItem;
import com.mofluid.magento2.R;
import com.mofluid.magento2.fragment.MyAditionalFragmenrt;

import java.util.ArrayList;

/**
 * Created by ebizon on 23/11/15.
 */
public class AdditionalInfoAdapter extends BaseAdapter {
    private final Typeface lat0_Font_ttf;
    private final LayoutInflater inflater;
    private final ArrayList<AdditionalInfoItem> list;

    public AdditionalInfoAdapter(Activity activity, ArrayList<AdditionalInfoItem> list) {
        this.list=list;
        inflater= activity.getLayoutInflater();

        lat0_Font_ttf = Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.REGULAR_FONT_STYLE);
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

        AdditionalInfoItem itemObj=list.get(i);

        Holder holder;
       {
            view=inflater.inflate(R.layout.row_additional_info,null);
            holder=new Holder();
            holder.ll_headerid=(LinearLayout) view.findViewById(R.id.ll_headerid);
            holder.txtV_label=(TextView) view.findViewById(R.id.txtV_label);
            holder.txtV_label__value=(TextView) view.findViewById(R.id.txtV_label__value);

            holder.txtV_pro_price=(TextView) view.findViewById(R.id.txtV_pro_price);
            holder.txtV_pro_additional_info_value=(TextView) view.findViewById(R.id.txtV_pro_additional_info_value);
            holder.txtV_pro_name=(TextView) view.findViewById(R.id.txtV_pro_name);

            view.setTag(holder);
        }


        if(i==0) {
            holder.ll_headerid.setVisibility(View.VISIBLE);
        }

        holder.txtV_label.setText(itemObj.getAttr_label());
        holder.txtV_label.setTypeface(lat0_Font_ttf);

        holder.txtV_label__value.setText(itemObj.getAttr_value());
        holder.txtV_label__value.setTypeface(lat0_Font_ttf);

        holder.txtV_pro_additional_info_value.setText(Html.fromHtml(MyAditionalFragmenrt.PRODUCT_SHORT_DESC));
        holder.txtV_pro_additional_info_value.setTypeface(lat0_Font_ttf);

        holder.txtV_pro_name.setText(MyAditionalFragmenrt.PRODUCT_NAME);
        holder.txtV_pro_name.setTypeface(lat0_Font_ttf);

        holder.txtV_pro_price.setText(MyAditionalFragmenrt.PRODUCT_PRICE);
        holder.txtV_pro_price.setTypeface(lat0_Font_ttf);

        return view;
    }

    static class Holder {
        TextView txtV_label,txtV_label__value,txtV_pro_price,txtV_pro_additional_info_value,txtV_pro_name;
        LinearLayout ll_headerid;
    }
}
