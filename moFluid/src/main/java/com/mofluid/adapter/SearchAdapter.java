package com.mofluid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mofluid.magento2.LoadImageWithPicasso;
import com.mofluid.magento2.R;
import com.mofluid.model_new.Product;

import java.util.List;

public class SearchAdapter extends BaseAdapter{
private List<Product> products;
private Context context;
private LayoutInflater inflater;

public SearchAdapter(Context context,List<Product> products){
this.context=context;
this.products=products;
this.inflater=LayoutInflater.from(this.context);
 }
public void setProducts(List<Product> products){
     this.products=products;
}
    @Override
    public int getCount() {
        return this.products.size();
    }

    @Override
    public Product getItem(int i) {
        return this.products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.search_suggestion_view, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else holder = (ViewHolder) view.getTag();

        Product item = getItem(i);
        if(item!=null) {
            holder.name.setText(item.getName());
            holder.price.setText(item.getPrice() + "");
            LoadImageWithPicasso.getInstance().loadImage(holder.imageView,item.getImage());
        }
        return view;
    }
    private class ViewHolder{
    ImageView imageView;
    TextView name,price;
    public ViewHolder(View view){
        this.imageView=(ImageView) view.findViewById(R.id.p_image);
        this.name=(TextView) view.findViewById(R.id.p_name);
        this.price=(TextView) view.findViewById(R.id.p_price);
    }
    }
}
