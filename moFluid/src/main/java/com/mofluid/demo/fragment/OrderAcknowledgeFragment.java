package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebizon.fluid.Utils.AddressManager;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.MySharedPreferences;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.Utils.ViewUtils;
import com.ebizon.fluid.model.Address;
import com.ebizon.fluid.model.AddressData;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.ShoppingCartItem;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserOrders;
import com.ebizon.fluid.model.UserProfileItem;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.database.MyDataBaseAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by ebizon on 23/12/15.
 */
@SuppressLint("ValidFragment")
public class OrderAcknowledgeFragment extends BaseFragment implements View.OnClickListener {
    private  String orderid;
    private  boolean isPayemnetDone;
    View rootView;
    private String headerText;
    private LinearLayout ll_ship_address_order;
    private UserOrders userOrders;
    private AddressData billingGuest, shippingGuest;
    private boolean isGuest;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;


    public OrderAcknowledgeFragment(String orderid, boolean isPayemnetDone) {
        this.orderid=orderid;
        this.isPayemnetDone = isPayemnetDone;

    }
    public OrderAcknowledgeFragment(String orderid, boolean isPayemnetDone, AddressData billingGuest, AddressData shippingGuest, boolean isGuest) {
        this.orderid=orderid;
        this.isPayemnetDone = isPayemnetDone;
        this.isGuest=isGuest;
        this.billingGuest=billingGuest;
        this.shippingGuest=shippingGuest;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_acknowledge, null);
        ll_ship_address_order = (LinearLayout)rootView.findViewById(R.id.ll_ship_address_order);
        setDataToUI();
        setContinueButton();
        return rootView;
    }

    private void setDataToUI() {
        ViewUtils.setToTextView(rootView, R.id.txtV_order_id_value, orderid);
        MyDataBaseAdapter dbAdapter = new MyDataBaseAdapter(getActivity());
        final double totalPrice = dbAdapter.getTotalPrice();
        final String priceWithCurrency = Utils.appendWithCurrencySymbol(totalPrice);

        if(!this.isPayemnetDone) {
            ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_payable_value, priceWithCurrency);
            ViewUtils.setTextViewVisibility(rootView, R.id.txtV_lshipp_payable_value, View.VISIBLE);
        }
        UserProfileItem activeUser = UserManager.getInstance().getUser();
        MySharedPreferences ob=MySharedPreferences.getInstance();
        Address address=null;
        if (activeUser != null) {
            if(!Utils.checkCartItemforDownloadable(new ArrayList<ShoppingCartItem>(ShoppingCart.getInstance().getCartItems()))) {
                if(ob.get(ob.SHIPPING_ADDRESS_ID)!=null) {
                    int ship_id = Integer.parseInt(ob.get(ob.SHIPPING_ADDRESS_ID));
                    address = AddressManager.getInstance().getAddress(ship_id);
                    ll_ship_address_order.setVisibility(View.VISIBLE);
                }
            }
            else {if(ob.get(ob.BILLING_ADDRESS_ID)!=null) {
                int bill_id = Integer.parseInt(ob.get(ob.BILLING_ADDRESS_ID));

                address = AddressManager.getInstance().getAddress(bill_id);
                ll_ship_address_order.setVisibility(View.VISIBLE);
            }
            }
            rootView.findViewById(R.id.email_address).setVisibility(View.VISIBLE);
            if (address != null) {
                ViewUtils.setToTextView(rootView, R.id.ttxtV_label__name_value, address.getFname()+" "+address.getLname());
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_add_value, address.getStreet());
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_city_value, address.getCity());
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_state_value, address.getRegion());
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_country_value, address.getCountry());
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_zipcode_value, address.getPincode());
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_payable_value, priceWithCurrency);
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_contact_value, address.getContact_no());
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_email_add_value, activeUser.getUsername());
            }
        }
        else if(isGuest==true)
        {AddressData addressData;
            if(!Utils.checkCartItemforDownloadable(new ArrayList<ShoppingCartItem>(ShoppingCart.getInstance().getCartItems()))) {
                addressData = shippingGuest;
                ll_ship_address_order.setVisibility(View.VISIBLE);
            }
            else {
                addressData = billingGuest;
                ll_ship_address_order.setVisibility(View.GONE);
            }
            rootView.findViewById(R.id.email_address).setVisibility(View.VISIBLE);
            if (addressData != null) {
                ViewUtils.setToTextView(rootView, R.id.ttxtV_label__name_value, addressData.getFullName());
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_add_value, addressData.getStreet());
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_city_value, addressData.getCity());
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_state_value, addressData.getState());
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_country_value, addressData.getCountryId());
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_zipcode_value, addressData.getZipCode());
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_payable_value, priceWithCurrency);
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_contact_value, addressData.getContactNumber());
                ViewUtils.setToTextView(rootView, R.id.txtV_lshipp_email_add_value, HomeFragment.userEmail);
            }
        }
        ShoppingCart.getInstance().clearCart();
        dbAdapter.clearCart();
        setCounterItemAddedCart();
    }

    private void setCounterItemAddedCart() {
        MainActivity.txtV_item_counter.setVisibility(View.INVISIBLE);
    }
    private void setContinueButton() {
        TextView txt_continue_to_shopp=(TextView) rootView.findViewById(R.id.txt_continue_to_shopp);
        txt_continue_to_shopp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

       // callFragmentPop(new HomeFragment(),"HomeFragment");
        mListener.onFragmentMessage(ConstantDataMember.POP_ALL_FRAGMENT_FROM_STACK,null);
        //callFragment(new HomeFragment(),"HomeFragment");
    }

    @Override
    public void onResume() {
        Activity activity = getActivity();
        if(isAdded()&&activity!=null) {
            headerText = getActivity().getResources().getString(R.string.order_review_header);
//            MainActivity.setHeaderText(headerText);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
        }
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        HomeFragment.userEmail=null;
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
