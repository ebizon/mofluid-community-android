package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;

import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.model.GroupedProductDetailItem;
import com.ebizon.fluid.model.ShoppingItem;
import com.ebizon.fluid.model.SimpleShoppingItem;
import com.mofluid.magento2.R;
import com.mofluid.magento2.fragment.BaseFragment;
import com.mofluid.magento2.fragment.GroupedProductDetailFragment;
import com.mofluid.magento2.fragment.HomeFragment;
import com.mofluid.magento2.fragment.SimpleProductFragment2;
import com.mofluid.magento2.service.AppController;
import com.mofluid.magento2.service.FeedImageView;


import java.util.ArrayList;


/**
 * Created by ebizon on 19/11/15.
 */
public class GroupedProductDetailListAdapter  extends BaseAdapter {

    private  Typeface lat0_Font_ttf;
    private  String TAG = "";
    private final Activity activity;
    private final ArrayList<GroupedProductDetailItem> productListDetails;
    private com.android.volley.toolbox.ImageLoader imageLoaderVolley = AppController.getInstance().getImageLoader();
    private GroupedProductDetailFragment groupedProductDetailFragment;


    public GroupedProductDetailListAdapter(Activity activity, ArrayList<GroupedProductDetailItem> productListDetails,GroupedProductDetailFragment groupedProductDetailFragment) {
        this.activity=activity;
        this.productListDetails=productListDetails;
        imageLoaderVolley = AppController.getInstance().getImageLoader();
        TAG=getClass().getSimpleName();
        if(activity!=null)
            this.groupedProductDetailFragment=groupedProductDetailFragment;
        lat0_Font_ttf = Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.REGULAR_FONT_STYLE);

    }

    @Override
    public int getCount() {

        return productListDetails.size();
    }

    @Override
    public Object getItem(int arg0) {

        return productListDetails.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {

        return arg0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final RecordHolder holder;

        if (row == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            row = inflater.inflate(R.layout.row_group_product_list_adapter, parent, false);

            holder = new RecordHolder();
            //      holder.txtV_product_count = (TextView) row.findViewById(R.id.txtV_product_count);
            holder.txtTitle = (TextView) row.findViewById(R.id.txtV_product_name_grouped);
            holder.txtV_price=(TextView) row.findViewById(R.id.txtV_productnormal_price);
            holder.txtV_special_price=(TextView) row.findViewById(R.id.txtV_productspecial_price);
            holder.imageItem = (FeedImageView) row.findViewById(R.id.imgV_added_itm);
            holder.rl_increase_decrease_product=(RelativeLayout) row.findViewById(R.id.rlQuantity);
            holder.txtV_out_of_stock=(TextView) row.findViewById(R.id.out_of_stock);
            holder.minus_image=(ImageView)row.findViewById(R.id.imgMinus);
            holder.plus_image=(ImageView)row.findViewById(R.id.imgPlus);
            holder.txtV_product_count=(TextView)row.findViewById(R.id.txtvStockQun);
            holder.addToCart=(Button)row.findViewById(R.id.btnaddtocart);

            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        final GroupedProductDetailItem item = productListDetails.get(position);

        setPrice(holder.txtV_price, holder.txtV_special_price, item.getGrouped_price_regular(), item.getGrouped_price_final());

//        holder.txtV_product_count.setText(item.getNo_of_produc()+"");
        holder.txtTitle.setText(item.getGrouped_general_name());
        holder.txtTitle.setTypeface(lat0_Font_ttf);
        // holder.txtV_price.setText(item.getGrouped_price_regular());
        holder.txtV_price.setTypeface(lat0_Font_ttf);
        // holder.txtV_special_price.setText(item.getGrouped_price_final());
        holder.txtV_special_price.setTypeface(lat0_Font_ttf);
        holder.txtV_out_of_stock.setTypeface(lat0_Font_ttf);
        int quantity=item.getQuantity();
        holder.txtV_product_count.setText(String.valueOf(quantity));


        if(item.getGrouped_stock_is_in_stock().equalsIgnoreCase("1")) {
            holder.txtV_out_of_stock.setVisibility(View.GONE);
            holder.rl_increase_decrease_product.setVisibility(View.VISIBLE);
        }
        else {
            holder.txtV_out_of_stock.setVisibility(View.VISIBLE);
            holder.addToCart.setVisibility(View.GONE);
            holder.rl_increase_decrease_product.setVisibility(View.GONE);
        }

        if(imageLoaderVolley==null)
            imageLoaderVolley=AppController.getInstance().getImageLoader();

        if (item.getGrouped_image() != null) {
            Log.d(TAG, item.getGrouped_image());
            holder.imageItem.setDefaultImageResId(R.drawable.default_mofluid);
            holder.imageItem.setImageUrl(item.getGrouped_image(), imageLoaderVolley);
            holder.imageItem.setVisibility(View.VISIBLE);
            holder.imageItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog=new Dialog(activity, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
                    dialog.setContentView(R.layout.gridv_grouped_item_image_dialog);
                    FeedImageView grid_item_image=(FeedImageView) dialog.findViewById(R.id.grid_image_item);
                    Button crossButton=(Button)dialog.findViewById(R.id.btnIvClose2);
                    crossButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    grid_item_image.setImageUrl(item.getGrouped_image(),imageLoaderVolley);
                    grid_item_image.setVisibility(View.VISIBLE);
                    dialog.show();
                }
            });
            holder.imageItem
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {

                        }

                        @Override
                        public void onSuccess() {
                        }
                    });
        } else {
            //  feedImageView.setVisibility(View.GONE);
            holder.imageItem.setDefaultImageResId(R.drawable.default_mofluid);
        }
        holder.imageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleProductFragment2 fm=new SimpleProductFragment2();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantDataMember.PRO_ID, item.getGrouped_id());
                bundle.putString(ConstantDataMember.PRO_NAME, item.getGrouped_general_name());
                fm.setArguments(bundle);
                callFragment(fm,"SimpleProductFragment2");


            }
        });
//        holder.imageItem.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View v) {
//                FullScreenImageDialog obj=new FullScreenImageDialog(activity);
//                obj.displayDialog(position);
//            }
//        });
        holder.minus_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupedProductDetailFragment.setQuantityMinus(position,holder.txtV_product_count);
            }
        });
        holder.plus_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupedProductDetailFragment.setQuantityPlus(position,holder.txtV_product_count);
            }
        });

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupId =item.getGrouped_id();
                String prosuctName=item.getGrouped_general_name();
                String productSku=item.getGrouped_sku();
                String cartImage=item.getGrouped_image();
                String price=item.getGrouped_price_regular();
                String sprice=item.getGrouped_price_final();
                String  quanity=item.getProduct_Stock_max_quantity();
                String type=item.getType();
                String image=item.getGrouped_image();
                ShoppingItem currentItem = new SimpleShoppingItem(groupId, prosuctName, productSku, cartImage, price, sprice, "1", quanity, type, image);
                groupedProductDetailFragment.addItemToCart(currentItem,item.getQuantity());
            }
        });
        return row;

    }
    void callFragment(BaseFragment fragment, String fragmentName) {
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(activity, fragmentTransaction, new HomeFragment(), fragment, R.id.content_frame);
        /*
        index 7 is used to slide fragment Left to right and vise versa
         */
        fragmentTransactionExtended.addTransition(7);
//        if(fragmentName != null) {
//            fragmentTransaction.addToBackStack(fragmentName);
//        }
        fragmentTransactionExtended.commit();
    }

    private void setPrice(TextView txtV_regular_price, TextView txtV_final_price, String grouped_price_regular, String grouped_price_final) {
        double final_price = Double.valueOf(grouped_price_final);
        double regular_price = Double.valueOf(grouped_price_regular);

        if(final_price<regular_price && final_price>0)
        {
            txtV_final_price.setText(Utils.appendWithCurrencySymbol(final_price));
            setTextThrougLine(Utils.appendWithCurrencySymbol(regular_price), txtV_regular_price);
        }
        else
        {
            txtV_final_price.setText(Utils.appendWithCurrencySymbol(regular_price));
            txtV_regular_price.setVisibility(View.GONE);
        }

    }

    private void setTextThrougLine(String off_money_discounted, TextView txtTitle) {

		/*String off_money_discounted = "50€";*/
        SpannableString discounted = new SpannableString(off_money_discounted);
        discounted.setSpan(new StrikethroughSpan(), 0, off_money_discounted.length(), 0);
        txtTitle.setText(discounted);

        String text = "dvs" + " calls";
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), 0);


    }

    static class RecordHolder {
        TextView txtTitle,txtV_special_price,txtV_price,txtV_out_of_stock,txtV_product_count;
        Button addToCart;
        FeedImageView imageItem;
        RelativeLayout rl_increase_decrease_product;
        ImageView plus_image,minus_image;

    }
}