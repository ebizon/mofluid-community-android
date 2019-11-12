package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.ShoppingCartItem;
import com.ebizon.fluid.model.ShoppingItem;
import com.mofluid.magento2.R;
import com.mofluid.magento2.fragment.MyCartFragment;
import com.mofluid.magento2.service.AppController;

import java.util.ArrayList;

/**
 * Created by ebizon on 6/11/15.
 */
public class ShoppingCartAdapter extends BaseAdapter {
    private  Typeface lat0_Font_ttf, open_sans_regular, open_sans_semibold;
    private LayoutInflater inflater;
    private final ArrayList<ShoppingCartItem> cartItemList;
    private final ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private MyCartFragment cartFragment;
    private Activity activity;

    public ShoppingCartAdapter(Activity activity, ArrayList<ShoppingCartItem> cartItemList, MyCartFragment cartFragment) {
        this.cartFragment = cartFragment;
        this.activity = activity;
        this.cartItemList = cartItemList;
        if (activity != null)
        {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lat0_Font_ttf = Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.REGULAR_FONT_STYLE);
        open_sans_regular = Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
        open_sans_semibold = Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
    }
    }

    @Override
    public int getCount() {
        return cartItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return cartItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder hodler;
        View row=convertView;
//        if(convertView==null) {
            row=inflater.inflate(R.layout.row_product_added_my_cart_adapter,null);
            hodler=new ViewHolder();
            hodler.txtV_product_count=(TextView) row.findViewById(R.id.txtV_product_count);
            hodler.txtV_product_price=(TextView) row.findViewById(R.id.txtV_product_price);
            hodler.txtV_product_name_mycart=(TextView) row.findViewById(R.id.txtV_product_name_mycart);
            hodler.ll_delete_added_itm = (LinearLayout)row.findViewById(R.id.ll_delete_added_itm);
            hodler.imgV_minus = (ImageView)row.findViewById(R.id.imgV_minus);
            hodler.imgV_plus  = (ImageView)row.findViewById(R.id.imgV_plus);
            hodler.txtV_cartitem_error_message = (TextView)row.findViewById(R.id.txtV_cartitem_error_message);

            row.setTag(hodler);
//        } else {
//            hodler = (ViewHolder) row.getTag();
//        }

        ShoppingCartItem item =cartItemList.get(i);
        hodler.txtV_product_name_mycart.setText(item.getShoppingItem().getName());
        hodler.txtV_product_name_mycart.setTypeface(open_sans_regular);
        double subTotal = ShoppingCart.getInstance().getSubTotal(item.getShoppingItem());
        hodler.txtV_product_price.setText(Utils.appendWithCurrencySymbol(subTotal));
        hodler.txtV_product_price.setTypeface(open_sans_semibold);

        hodler.txtV_product_count.setText(String.valueOf(item.getCount()));
        hodler.txtV_product_count.setTypeface(open_sans_regular);
        if(item.getCount()>item.getShoppingItem().getStockQuantity() || !item.getShoppingItem().isInStock() )
        {
            // show the message
            String errorMessgae = "";
            if(item.getShoppingItem().getStockQuantity()==0 || !item.getShoppingItem().isInStock() )
                errorMessgae= activity.getResources().getString(R.string.default_error_out_of_stock);
            else
                errorMessgae= activity.getResources().getString(R.string.default_error_stock);

            hodler.txtV_cartitem_error_message.setVisibility(View.VISIBLE);
            hodler.txtV_cartitem_error_message.setText(errorMessgae);
            item.setQualifies_checkout(false);
        }
        else
        {
            hodler.txtV_cartitem_error_message.setVisibility(View.INVISIBLE);
            item.setQualifies_checkout(true);
        }

        ImageView imageView = (ImageView)row.findViewById(R.id.imgV_added_itm);
        if(imageView != null) {
            ShoppingItem shoppingItem = item.getShoppingItem();
            String imageURL = shoppingItem.getThumbnail();//change for thumbnail
            Log.d("ImageCArt", shoppingItem.getThumbnail());

            if (imageURL != null) {
                imageLoader.get(imageURL, ImageLoader.getImageListener(imageView, R.drawable.default_mofluid, R.drawable.default_mofluid));
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCartItem items =cartItemList.get(i);
                    cartFragment.callF(items.getShoppingItem().getId(),items.getShoppingItem().getName(), (ImageView) v);

                }
            });
        }

        hodler.ll_delete_added_itm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartFragment.deleteItemFromCartList(i);
                hodler.ll_delete_added_itm.setClickable(false);
            }
        });

        hodler.imgV_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartItemList.get(i).getShoppingItem().isInStock())
                    if(cartItemList.get(i).isQualifies_checkout()) {
                        cartFragment.lessItemCountToCart(cartItemList.get(i), i);
                        notifyDataSetChanged();
                    }
                    else // quantity is more than available
                    {
                        cartItemList.get(i).setCount(cartItemList.get(i).getShoppingItem().getStockQuantity());
                        cartFragment.RefreshItemValueToCart(cartItemList.get(i), i); // set to maximum value available of product.
                        cartItemList.get(i).setQualifies_checkout(true);
                    }
                else // out of stock status true
                {
                    if(activity!=null)
                        Toast.makeText(activity, activity.getResources().getString(R.string.remove_from_cart_text), Toast.LENGTH_SHORT).show();
                }
            }
        });

        hodler.imgV_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cartItemList.get(i).getShoppingItem().getType().toString().equals("downloadable")) {
                    if(cartItemList.get(i).getShoppingItem().isInStock())
                        if(cartItemList.get(i).isQualifies_checkout())
                            cartFragment.addItemValueToCart(cartItemList.get(i), i);
                        else
                        {
                            cartItemList.get(i).setCount(cartItemList.get(i).getShoppingItem().getStockQuantity()); // set to max quantity
                            cartFragment.RefreshItemValueToCart(cartItemList.get(i), i);
                            cartItemList.get(i).setQualifies_checkout(true);
                        }
                    else
                    {
                        if(activity!=null)
                            Toast.makeText(activity, activity.getResources().getString(R.string.default_error_stock), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(activity,"Single quantity for downloadable Product",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return row;
    }
private void update(){

}
    class ViewHolder {
        TextView txtV_product_count;
        TextView txtV_product_price;
        TextView txtV_product_name_mycart;
        LinearLayout ll_delete_added_itm;
        ImageView imgV_minus, imgV_plus;
        TextView txtV_cartitem_error_message;
    }
}
