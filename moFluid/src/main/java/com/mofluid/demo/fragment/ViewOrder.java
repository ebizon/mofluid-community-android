package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.model.OrderedProduct;
import com.ebizon.fluid.model.UserOrders;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.service.AppController;

import java.lang.reflect.Field;

// created by prashant chauhan
// TODO: 03/08/18 Change the name from ViewOrder to ViewFragmnet
@SuppressLint("ValidFragment")
public class ViewOrder extends BaseFragment{

    private UserOrders orderReview;
    private TextView billing_firstname;
    private TextView billing_lastname;
    private TextView billing_contactno;
    private TextView billing_pincode;
    private TextView billing_city;
    private TextView billing_state;
    private TextView billing_address;
    private TextView shipping_firstname;
    private TextView shipping_lastname;
    private TextView shipping_contactno;
    private TextView shipping_pincode;
    private TextView shipping_city;
    private TextView shipping_state;
    private TextView shipping_address;
    private TextView txt_grand_total_detail;
    private TextView txt_status_detail;
    private TextView txt_order_id_detail;
    private TextView txt_payment_method_detail;
    private TextView txt_shipping_method_detail;
    private TextView txt_order_date_detail;
    private TextView txt_sub_total_detail;
    private LinearLayout view_orders;
    private TextView billing_country;
    private TextView shipping_country;
    private TextView shipping_amount;
    private TextView tax_amount;
    private  TextView grd_total;
    private TextView shipping_method;
    private TextView viewall;
    private LayoutInflater inflater;
    private View rootview;
    ImageLoader imageLoader;
    Context context;
    private float subtotal=0;
    private String headerText;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;


    public ViewOrder(UserOrders userOrders, Activity context) {
        this.orderReview=userOrders;
        this.context=context;
        imageLoader= AppController.getInstance().getImageLoader();
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }
    public ViewOrder()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            if(rootview==null) {
                rootview = inflater.inflate(R.layout.fragment_blank, container, false);
                getViewControl(rootview);
                addorderproduct(rootview);
                setDataToViews();

            }
            viewall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                MyOrderFragment myOrderFragment=new MyO
// rderFragment();
//                callFragment(myOrderFragment);
                    getActivity().onBackPressed();
                }
            });


        return rootview;

    }

    private void addorderproduct(View rootview) {
        for(int i=0;i<orderReview.getOrderedProducts().size();i++)
        {
            setData(orderReview.getOrderedProducts().get(i),rootview);
        }

    }

    private void setData(OrderedProduct orderedProduct, View rootview) {
        view_orders=(LinearLayout)rootview.findViewById(R.id.view_items);
        View attributeview=inflater.inflate(R.layout.view_order, null);
        ImageView product_image=(ImageView)attributeview.findViewById(R.id.vieworder_imageview);
        String imageurl=orderedProduct.getProduct_image();
        imageLoader.get(imageurl, ImageLoader.getImageListener(product_image, R.drawable.default_mofluid, R.drawable.default_mofluid));
        TextView  product_name=(TextView)attributeview.findViewById(R.id.vieworder_productname);
        product_name.setText(orderedProduct.getProduct_name());
        TextView  product_unitprice=(TextView)attributeview.findViewById(R.id.vieworder_unitprice);
        product_unitprice.setText(orderReview.getOrderCurrency()+" "+orderedProduct.getProduct_unit_price());
        TextView  product_quantity=(TextView)attributeview.findViewById(R.id.vieworder_quantity);
        product_quantity.setText("*"+orderedProduct.getProduct_quantity());
        float  total_price=calculatingprice(orderedProduct.getProduct_quantity(),orderedProduct.getProduct_unit_price());
        TextView  total_price_value=(TextView)attributeview.findViewById(R.id.View_order_total_price_value);
        total_price_value.setText(orderReview.getOrderCurrency()+" "+String.valueOf(total_price));
        view_orders.addView(attributeview);
    }

    private float calculatingprice(String product_quantity, String product_unit_price) {

        float unit_price=Float.parseFloat(product_unit_price);
        float quantity=Float.parseFloat(product_quantity);
        float total=(unit_price*quantity);
        subtotal=subtotal+total;


        return total;

    }

    private void setDataToViews() {
        //set data to billing Address
        billing_firstname.setText(orderReview.getFirstname_billing());
        billing_lastname.setText(orderReview.getLastname_billing());
        billing_contactno.setText(orderReview.getContactnumber_billing());
        billing_country.setText(orderReview.getCountry_billing());
        billing_pincode.setText(orderReview.getZipcode_billing());
        billing_city.setText(orderReview.getCity_billing());
        billing_state.setText(orderReview.getState_billing());
        billing_address.setText(orderReview.getShipping_billing());

        //set data to shipping Address
        shipping_firstname.setText(orderReview.getFirstname_billing());
        shipping_lastname.setText(orderReview.getLastname_billing());
        shipping_contactno.setText(orderReview.getContactnumber_billing());
        shipping_country.setText(orderReview.getCountry_billing());
        shipping_pincode.setText(orderReview.getZipcode_billing());
        shipping_city.setText(orderReview.getCity_billing());
        shipping_state.setText(orderReview.getState_billing());
        shipping_address.setText(orderReview.getShipping_billing());

        txt_grand_total_detail.setText(orderReview.getOrderCurrency()+" "+orderReview.getAmount_Payble());
        txt_status_detail.setText(orderReview.getOrder_status());
        txt_order_id_detail.setText(orderReview.getOrder_id());
        txt_payment_method_detail.setText(orderReview.getPayment_method());
        txt_order_date_detail.setText(orderReview.getOrder_date());
        txt_sub_total_detail.setText(orderReview.getOrderCurrency()+" "+String.valueOf(subtotal));
        shipping_amount.setText(orderReview.getOrderCurrency()+ " "+orderReview.getShippingAmount());
        tax_amount.setText(orderReview.getOrderCurrency()+" "+orderReview.getTaxAmount());
        grd_total.setText(orderReview.getOrderCurrency()+" "+orderReview.getAmount_Payble());
        shipping_method.setText(orderReview.getShipping_method());


    }

    private void getViewControl(View rootview) {
        //billing Address
        billing_firstname=(TextView)rootview.findViewById(R.id.txt_firstname);
        billing_lastname=(TextView)rootview.findViewById(R.id.txt_last_name);
        billing_contactno=(TextView)rootview.findViewById(R.id.txt_contactno);
        billing_country=(TextView)rootview.findViewById(R.id.txt_country);
        billing_pincode=(TextView)rootview.findViewById(R.id.txt_pincode);
        billing_city=(TextView)rootview.findViewById(R.id.txt_city);
        billing_state=(TextView)rootview.findViewById(R.id.txt_state);
        billing_address=(TextView)rootview.findViewById(R.id.txt_billing_address);
        //shipping Address
        shipping_firstname=(TextView)rootview.findViewById(R.id.firstname);
        shipping_lastname=(TextView)rootview.findViewById(R.id.lastname);
        shipping_contactno=(TextView)rootview.findViewById(R.id.contactno);
        shipping_country=(TextView)rootview.findViewById(R.id.country);
        shipping_pincode=(TextView)rootview.findViewById(R.id.pincode);
        shipping_city=(TextView)rootview.findViewById(R.id.city);
        shipping_state=(TextView)rootview.findViewById(R.id.state);
        shipping_address=(TextView)rootview.findViewById(R.id.shipping);

        txt_grand_total_detail=(TextView)rootview.findViewById(R.id.txt_grand_Total_details);
        txt_status_detail=(TextView)rootview.findViewById(R.id.txt_status_details);
        txt_order_id_detail=(TextView)rootview.findViewById(R.id.txt_order_id_details);
        txt_payment_method_detail=(TextView)rootview.findViewById(R.id.txt_payment_method_details);
        txt_shipping_method_detail=(TextView)rootview.findViewById(R.id.txt_shipping_method_details);
        txt_order_date_detail=(TextView)rootview.findViewById(R.id.txt_order_date_details);
        txt_sub_total_detail=(TextView)rootview.findViewById(R.id.txt_sub_Total_details);
        tax_amount=(TextView)rootview.findViewById(R.id.txt_tax_details);
        shipping_amount=(TextView)rootview.findViewById(R.id.txt_shipping_details);
        grd_total=(TextView)rootview.findViewById(R.id.txt_grd_Total_details);
        shipping_method=(TextView)rootview.findViewById(R.id.txt_shipping_method_details);
        viewall=(TextView)rootview.findViewById(R.id.viewall);


    }

    @Override
    public void onResume() {
        Activity activity = getActivity();
        if(isAdded()&&activity!=null) {
            headerText = getActivity().getResources().getString(R.string.review_order_header);
//            MainActivity.setHeaderText(headerText);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
        }
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

}
