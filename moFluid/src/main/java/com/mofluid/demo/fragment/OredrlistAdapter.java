package com.mofluid.magento2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.model.OrderedProduct;
import com.ebizon.fluid.model.ShoppingItem;
import com.ebizon.fluid.model.UserOrders;
import com.mofluid.magento2.R;
import com.mofluid.magento2.Reorder;
import com.mofluid.magento2.service.AppController;

import java.util.ArrayList;

/**
 * Created by prashant Chauhan on 29/3/16.
 */
public class OredrlistAdapter extends BaseAdapter {

    private final Typeface open_sans_semibold;
    private final Typeface open_sans_regular;
    Activity context;
    LayoutInflater inflater;
    ArrayList<UserOrders> complete_order;
    ArrayList<UserOrders> complete_order_complete;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    OredrlistAdapter(ArrayList<UserOrders> complete_order, Activity context) {
        this.context = context;
        this.complete_order = complete_order;
        this.complete_order_complete= new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        open_sans_semibold = Typeface.createFromAsset(context.getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
        open_sans_regular = Typeface.createFromAsset(context.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);

    }

    OredrlistAdapter(ArrayList<UserOrders> complete_order_small, Activity context, ArrayList<UserOrders> complete_order) {
        this.context = context;
        this.complete_order = complete_order_small;
        this.complete_order_complete = complete_order;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        open_sans_semibold = Typeface.createFromAsset(context.getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
        open_sans_regular = Typeface.createFromAsset(context.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);

    }

    @Override
    public int getCount() {
        return complete_order.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Log.d("TIMEPIY", "START GETVIEW :["+ position+" ]");

        View rowView = convertView;
        ViewHolder viewHolder;
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(params);

        for (int i = 0; i < complete_order.get(position).getOrderedProducts().size(); i++) {
            viewHolder = new ViewHolder();

                LayoutInflater inflater = context.getLayoutInflater();
                rowView = inflater.inflate(R.layout.productlist_myorder_item, null);
                viewHolder.setDate_time((TextView) rowView.findViewById(R.id.date_time));
                viewHolder.setMy_order_product_name((TextView) rowView.findViewById(R.id.my_order_product_name));
                viewHolder.setMy_order_product_price((TextView) rowView.findViewById(R.id.my_order_product_price));
                viewHolder.setStatus((TextView) rowView.findViewById(R.id.status));
                viewHolder.setReorder((TextView) rowView.findViewById(R.id.reorder));
                viewHolder.setViewOrder((TextView) rowView.findViewById(R.id.vieworder));
                viewHolder.setMy_order_image((ImageView)rowView.findViewById(R.id.my_order_image));
                viewHolder.setNavigatetxt_order((TextView) rowView.findViewById(R.id.navigatetxt_order));
                viewHolder.setDivider((View)rowView.findViewById(R.id.divider));
                viewHolder.setImgv_order_status((ImageView)rowView.findViewById(R.id.imgv_order_status));
                rowView.setTag(viewHolder);

            OrderedProduct orderedProduct = complete_order.get(position).getOrderedProducts().get(i);

            // START SETTING DATA
            if(i==0) // if first item
            {
                viewHolder.getDate_time().setText(complete_order.get(position).getOrder_date() + "(" + complete_order.get(position).getOrder_id() + ")");
                viewHolder.getDate_time().setVisibility(View.VISIBLE);
            }
            else
            {
                viewHolder.getDate_time().setVisibility(View.GONE);
            }

            if(i==complete_order.get(position).getOrderedProducts().size()-1) // if last item
            {
                viewHolder.getReorder().setVisibility(View.VISIBLE);
                viewHolder.getDivider().setVisibility(View.GONE);
            }

            viewHolder.getMy_order_product_name().setText(orderedProduct.getProduct_name());
            viewHolder.getMy_order_product_price().setText(complete_order.get(i).getOrderCurrency() + orderedProduct.getProduct_unit_price());
            String imageurl = orderedProduct.getProduct_image();
            imageLoader.get(imageurl, ImageLoader.getImageListener(viewHolder.getMy_order_image(), R.drawable.default_mofluid, R.drawable.default_mofluid));
            viewHolder.getStatus().setText(complete_order.get(position).getOrder_status());
            if(complete_order.get(position).getOrder_status().equals("complete"))
                viewHolder.getImgv_order_status().setImageDrawable(context.getResources().getDrawable(R.drawable.order_processed));
            else
                viewHolder.getImgv_order_status().setImageDrawable(context.getResources().getDrawable(R.drawable.processing));

        if (complete_order.size() >= 3&& complete_order_complete.size()!=0) {
            if (position == complete_order.size() - 1 &&i==complete_order.get(position).getOrderedProducts().size()-1 ) {
                viewHolder.getNavigatetxt_order().setVisibility(View.VISIBLE);
            }
        }
            viewHolder.getNavigatetxt_order().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyOrderFragment myOrderFragment = new MyOrderFragment(complete_order_complete);
                callfragment(myOrderFragment);
            }
        });

        viewHolder.getViewOrder().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewOrder orderAcknowledgeFragment = new ViewOrder(complete_order.get(position), context);
                callfragment(orderAcknowledgeFragment);
            }
        });
        viewHolder.getReorder().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < complete_order.get(position).getOrderedProducts().size(); i++) {
                    addingitemstocart(complete_order.get(position).getOrderedProducts().get(i), complete_order.get(position).getOrderedProducts().size());
                }
            }
        });
            layout.addView(rowView);
        }
        Log.d("TIMEPIY", "END GETVIEW :["+ position+" ]");
        return layout;
    }

    public void callfragment(Fragment orderAcknowledgeFragment) {
        FragmentManager fm = context.getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(context.getApplicationContext(), fragmentTransaction, new ViewOrder(), orderAcknowledgeFragment, R.id.content_frame);
        /*
        index 7 is used to slide fragment Left to right and vise versa
         */
        String fragmentName = orderAcknowledgeFragment.getClass().getSimpleName();
        fragmentTransactionExtended.addTransition(7);
        if (fragmentName != null) {
            fragmentTransaction.addToBackStack(fragmentName);
        }
        fragmentTransactionExtended.commit();
    }

    private void addingitemstocart(OrderedProduct orderedProduct, int listsize) {
        String product_id = orderedProduct.getProduct_id();
        String product_name = orderedProduct.getProduct_name();
        String product_price = orderedProduct.getProduct_unit_price();
        Reorder reorder = new Reorder(product_id, product_name, context, listsize);
    }

    class ViewHolder {
        private TextView date_time, my_order_product_name, status, my_order_product_price, reorder, viewOrder, navigatetxt_order;
        private View divider;
        private ImageView my_order_image, imgv_order_status;

        public View getDivider() {
            return divider;
        }

        public void setDivider(View divider) {
            this.divider = divider;
        }

        public TextView getNavigatetxt_order() {

            return navigatetxt_order;
        }

        public void setNavigatetxt_order(TextView navigatetxt_order) {
            this.navigatetxt_order = navigatetxt_order;
            this.navigatetxt_order.setTypeface(open_sans_semibold);
        }

        public TextView getDate_time() {

            return date_time;
        }

        public void setDate_time(TextView date_time) {
            this.date_time = date_time;
            this.date_time.setTypeface(open_sans_semibold);
        }

        public TextView getMy_order_product_name() {
            return my_order_product_name;
        }

        public void setMy_order_product_name(TextView my_order_product_name) {
            this.my_order_product_name = my_order_product_name;
            this.my_order_product_name.setTypeface(open_sans_semibold);
        }

        public TextView getStatus() {
            return status;
        }

        public void setStatus(TextView status) {

            this.status = status;
            this.status.setTypeface(open_sans_regular);
        }

        public TextView getMy_order_product_price() {
            return my_order_product_price;
        }

        public void setMy_order_product_price(TextView my_order_product_price) {

            this.my_order_product_price = my_order_product_price;
            this.my_order_product_price.setTypeface(open_sans_semibold);
        }

        public TextView getReorder() {
            return reorder;
        }

        public void setReorder(TextView reorder) {

            this.reorder = reorder;
            this.reorder.setTypeface(open_sans_semibold);
        }

        public TextView getViewOrder() {
            return viewOrder;
        }

        public void setViewOrder(TextView viewOrder) {

            this.viewOrder = viewOrder;
            this.viewOrder.setTypeface(open_sans_semibold);
        }

        public ImageView getMy_order_image() {
            return my_order_image;
        }

        public void setMy_order_image(ImageView my_order_image) {
            this.my_order_image = my_order_image;
        }

        public ImageView getImgv_order_status() {
            return imgv_order_status;
        }

        public void setImgv_order_status(ImageView imgv_order_status) {
            this.imgv_order_status = imgv_order_status;
        }
    }


}
