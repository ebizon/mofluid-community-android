package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.Utils;
import com.mofluid.utility_new.Callback;
import com.mofluid.utility_new.WishListManager;
import com.ebizon.fluid.model.WishListItem;
import com.mofluid.magento2.R;
import com.mofluid.magento2.Reorder;
import com.mofluid.magento2.service.AppController;

import java.util.ArrayList;

/**
 * Created by piyush-ios on 25/5/16.
 */
public class WishListAdapter extends BaseAdapter {

    private ArrayList<WishListItem> mywishList;
    Activity act;
    LayoutInflater inflater;
    com.android.volley.toolbox.ImageLoader imageLoader;
    private String PRODUCT_PRICE;
    private String OLD_PRICE;

    public WishListAdapter(ArrayList<WishListItem> mywishList,Activity act) {
        this.mywishList = mywishList;
        this.act=act;
        inflater=(LayoutInflater)act.getSystemService(act.LAYOUT_INFLATER_SERVICE);
        imageLoader= AppController.getInstance().getImageLoader();
    }

    @Override

    public int getCount() {
        return mywishList.size();
    }

    @Override
    public Object getItem(int position) {
        return mywishList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.wishlist_row_layout,null);
        TextView product_name=(TextView)view.findViewById(R.id.wish_list_product_name);
        TextView product_secial_price=(TextView)view.findViewById(R.id.wish_list_product_offer_price);
        TextView product_old_price=(TextView)view.findViewById(R.id.wish_list_product_old_price);
        ImageView product_image=(ImageView)view.findViewById(R.id.wishlist_product_image);
        ImageView imgv_delete_wishlist=(ImageView)view.findViewById(R.id.delete_item_wishlist);
        imgv_delete_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WishListManager.getInstance().deleteWishListItemUtil(mywishList.get(position).getItem_id(), new Callback() {
                    @Override
                    public void callback(Object o, int response_code) {
                                         if(o==null){
                                             Toast.makeText(act,"Some error occured.",Toast.LENGTH_SHORT);
                                             return;
                                         }
                                         mywishList.remove(position);
                                         notifyDataSetChanged();
                    }
                });
            }
        });
        ImageView addtocart_image=(ImageView)view.findViewById(R.id.addtocart_wishlist);
        addtocart_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              ordertheproduct(position);
            }
        });
        setTypeface(product_name,product_old_price,product_secial_price);
        product_name.setText(mywishList.get(position).getName());
        if(mywishList.get(position).getSprice()!=0.00) {
            PRODUCT_PRICE = Utils.appendWithCurrencySymbol(mywishList.get(position).getSprice());
            product_secial_price.setText(PRODUCT_PRICE);
        }
        else
            product_secial_price.setVisibility(View.GONE);
        OLD_PRICE=Utils.appendWithCurrencySymbol(mywishList.get(position).getPrice());
        product_old_price.setText(OLD_PRICE);
        imageLoader.get(mywishList.get(position).getImage(), com.android.volley.toolbox.ImageLoader.getImageListener(product_image, R.drawable.default_mofluid, R.drawable.default_mofluid));
        return view;
    }

    private void ordertheproduct(int position) {
        String product_id=mywishList.get(position).getId();
        String product_name=mywishList.get(position).getName();
        Reorder reorder=new Reorder(product_id,product_name,act,1);
    }

    private void setTypeface(TextView product_name, TextView product_old_price, TextView product_secial_price) {
        Typeface font_type = Typeface.createFromAsset(act.getAssets(), ConstantDataMember.HELVETICA_FONT_STYLE);
        product_name.setTypeface(font_type);
        product_old_price.setTypeface(font_type);
        product_secial_price.setTypeface(font_type);
    }


}
