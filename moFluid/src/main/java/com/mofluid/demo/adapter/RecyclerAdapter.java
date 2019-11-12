package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.model.OrderedProduct;
import com.ebizon.fluid.model.UserOrders;
import com.mofluid.magento2.EncodeString;
import com.mofluid.magento2.MyReoder;
import com.mofluid.magento2.R;
import com.mofluid.magento2.fragment.MyOrderFragment;
import com.mofluid.magento2.fragment.ViewOrder;
import com.mofluid.magento2.service.AppController;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by ebizon on 10/3/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter {

    private  Typeface open_sans_semibold;
    private  Typeface open_sans_regular;
    Activity context;

    LayoutInflater inflater;
    ArrayList<UserOrders> complete_order;
    ArrayList<UserOrders> complete_order_complete;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public RecyclerAdapter(ArrayList<UserOrders> complete_order_small, Activity context, ArrayList<UserOrders> complete_order){
        this.context = context;
        this.complete_order = complete_order_small;
        this.complete_order_complete = complete_order;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        open_sans_semibold = Typeface.createFromAsset(context.getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
        open_sans_regular = Typeface.createFromAsset(context.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
   }


    public class MyviewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemsView;
        public TextView timeDate;
        public  Button moreordersbtn;
        public MyviewHolder(View itemView) {
            super(itemView);
            itemsView=(LinearLayout)itemView.findViewById(R.id.productsLayout);
            timeDate=(TextView)itemView.findViewById(R.id.date_time);
            moreordersbtn=(Button)itemView.findViewById(R.id.navigatetxt_order);
        }
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.demousing, parent, false);
        return new MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyviewHolder h = (MyviewHolder) holder;
        h.timeDate.setText(complete_order.get(position).getOrder_date() + "(" + complete_order.get(position).getOrder_id() + ")");
        if (position==complete_order.size()-1){
            h.moreordersbtn.setVisibility(View.VISIBLE);
        }
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ArrayList<OrderedProduct> orderedProduct=complete_order.get(position).getOrderedProducts();
        for(int i=0;i<orderedProduct.size();i++){
            View view=inflater.inflate(R.layout.productlist_myorder_item,null);
            TextView productname=(TextView) view.findViewById(R.id.my_order_product_name);
            TextView productPrice=(TextView) view.findViewById(R.id.my_order_product_price);
            TextView productStatus=(TextView)view.findViewById(R.id.status);
            TextView reorder=(TextView)view.findViewById(R.id.reorder);
            TextView viewOrder=(TextView)view.findViewById(R.id.vieworder);
            ImageView productImage=(ImageView) view.findViewById(R.id.my_order_image);
            View divider=(View)view.findViewById(R.id.divider);
            ImageView statusImage=(ImageView)view.findViewById(R.id.imgv_order_status);
            productname.setText(orderedProduct.get(i).getProduct_name());
            productPrice.setText(Utils.appendWithCurrencySymbolstring(orderedProduct.get(i).getProduct_unit_price()));
            productStatus.setText(complete_order.get(position).getOrder_status());
            String product_image=orderedProduct.get(i).getProduct_image();
            imageLoader.get(product_image, ImageLoader.getImageListener(productImage, R.drawable.default_mofluid, R.drawable.default_mofluid));
            if(complete_order.get(position).getOrder_status().equals("complete"))
               statusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.order_processed));
            else
             statusImage.setImageDrawable(context.getResources().getDrawable(R.drawable.processing));
            h.itemsView.addView(view);
            viewOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewOrder orderAcknowledgeFragment = new ViewOrder(complete_order.get(position), context);
                    callfragment(orderAcknowledgeFragment);
                }
            });
            h.moreordersbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyOrderFragment myOrderFragment = new MyOrderFragment(complete_order_complete);
                    callfragment(myOrderFragment);
                }
            });

            reorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<OrderedProduct> allProductOfOrder = complete_order.get(position).getOrderedProducts();
                    String orderId=complete_order.get(position).getOrder_id();
                    String idsOfAllProduct=getIdOfAllProducts(allProductOfOrder);
                    Log.d("DVS", "onClick() called with: " + "idsOfAllProduct = [" + idsOfAllProduct + "]");
                    new MyReoder().hitserviceforreorder(context,idsOfAllProduct,orderId);
                    //String product_id, String product_name,Activity context,int listsize
                    /*String productIDarray= createJsonforProductsIDs(complete_order.get(position).getOrderedProducts(),position);
                    new Reorder(productIDarray,complete_order.get(position).getOrderedProducts().get(position).getProduct_name(),context,complete_order.size());*/
                }
            });

        }
    }

    private String  createJsonforProductsIDs(ArrayList<OrderedProduct> orderedProducts, int position)  {
        JSONArray jsonArray=new JSONArray();
        String productID=orderedProducts.get(position).getProduct_id();
        try {
            jsonArray.put(productID);
            }catch (Exception e){
            }
        return jsonArray.toString();

    }

   /* private void addingitemstocart(OrderedProduct orderedProduct, int listsize) {
        String product_id = orderedProduct.getProduct_id();
        String product_name = orderedProduct.getProduct_name();
        String product_price = orderedProduct.getProduct_unit_price();
        Reorder reorder = new Reorder(product_id, product_name, context, listsize);
    }*/
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
    @Override
    public int getItemCount() {
        return complete_order.size();
    }
    private String getIdOfAllProducts(ArrayList<OrderedProduct> allProductOfOrder) {
        String ids[]=new String[allProductOfOrder.size()];
        for(int i=0;i<allProductOfOrder.size();i++) {
            OrderedProduct orderPro = allProductOfOrder.get(i);
            String prductsId=orderPro.getProduct_id();
            ids[i]=prductsId;
        }
        String idsStr= Arrays.toString(ids);
        return   EncodeString.encodeStrBase64Bit(idsStr);


    }
}
