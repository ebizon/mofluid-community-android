package com.mofluid.magento2.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.model.ReoredrItem;
import com.mofluid.magento2.LoadImageWithPicasso;
import com.mofluid.magento2.R;

import java.util.ArrayList;

/**
 * Created by ebizon201 on 1/11/16.
 */
public class OutOfStockAdapter extends BaseAdapter {
    private final Typeface open_sans_semibold;
    Context context;
    ArrayList<ReoredrItem> outOfStockProducts;
    LayoutInflater inflater;

    public OutOfStockAdapter(Context context, ArrayList<ReoredrItem> outOfStockProducts) {
        this.context = context;
        this.outOfStockProducts = outOfStockProducts;
        inflater=LayoutInflater.from(context);
         open_sans_semibold = Typeface.createFromAsset(context.getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
    }

    @Override
    public int getCount() {
        return outOfStockProducts.size();
    }

    @Override
    public Object getItem(int i) {
        return outOfStockProducts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row=view;
        ViewHolder holder=null;
        if(row==null) {
            row=inflater.inflate(R.layout.adapter_out_of_stock,null);
            holder=new ViewHolder();
            holder.imgOutOfStockId=(ImageView) row.findViewById(R.id.imgOutOfStockId);
            holder.txtvProductName=(TextView) row.findViewById(R.id.txtvProductName);
            row.setTag(holder);
        }
        else
        holder= (ViewHolder) row.getTag();

        ReoredrItem item = outOfStockProducts.get(i);
        holder.txtvProductName.setText(Html.fromHtml(item.getName()));
        holder.txtvProductName.setTypeface(open_sans_semibold);
        LoadImageWithPicasso.getInstance().loadImage(holder.imgOutOfStockId,item.getImg());

        return row;
    }
    class  ViewHolder
    {
        TextView txtvProductName;
        ImageView imgOutOfStockId;
    }

}
