package com.mofluid.magento2.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.MySharedPreferences;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.Utils.ViewUtils;
import com.mofluid.utility_new.WishListManager;
import com.ebizon.fluid.model.AddressData;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserProfileItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.UserSession;
import com.mofluid.magento2.custome.widget.ShowAlertDialogBox;
import com.mofluid.magento2.database.MyDataBaseAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by ebizon on 15/12/15.
 */
public class WelcomeFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private LinearLayout ll_edit_billing;
    private LinearLayout ll_edit_shipping;
    private TextView txtV_change_password;
    private TextView txtV_logout;
    private TextView txtV_my_order;
    private String TAG;
    private LinearLayout ll_billing_address;
    private LinearLayout ll_shipping_address;
    private TextView txtV_shipping_default_address;
    private TextView txtV_billing_default_address;
    private TextView mydownloads;
    static boolean isOrdering = false;
    public static boolean isSocialLogin= false;
    private ShowAlertDialogBox showAlertDialogBoxObj;
    ProgressDialog pDialog;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    public WelcomeFragment() {
    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(isOrdering)
        {
            callFragment(new BillingAndShippingAddressFragment(), "BillingAndShippingAddressFragment");
        }

        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_welcome, null);

            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
            initialized();

            hitServiseGetUSRProfile();

            getViewControlls(rootView);
            setemailtoprefrences();

            if(isSocialLogin==true)
                txtV_change_password.setVisibility(View.GONE);
            else
                txtV_change_password.setVisibility(View.VISIBLE);

            ll_edit_billing.setOnClickListener(this);
            ll_edit_shipping.setOnClickListener(this);
            txtV_change_password.setOnClickListener(this);
            txtV_logout.setOnClickListener(this);
            txtV_my_order.setOnClickListener(this);
        }

        return  rootView;
    }
    private void setemailtoprefrences() {
        final SharedPreferences sharedPreferences=getActivity().getSharedPreferences("lockapprating", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("emailID",HomeFragment.userEmail);
        editor.commit();
    }

    private void initialized() {
        pDialog=HomeFragment.pDialog;
        TAG=getClass().getSimpleName();
        showAlertDialogBoxObj=new ShowAlertDialogBox();
        HomeFragment.userEmail=UserManager.getInstance().getUser().getUsername();
    }

    private void getViewControlls(View rootView) {
        ll_edit_billing=(LinearLayout) rootView.findViewById(R.id.ll_edit_billing);
        ll_edit_shipping=(LinearLayout) rootView.findViewById(R.id.ll_edit_shipping);

        txtV_change_password=(TextView) rootView.findViewById(R.id.txtV_change_passwordt);
        mydownloads=(TextView)rootView.findViewById(R.id.txtV_my_downloads);
        mydownloads.setOnClickListener(this);
        txtV_logout=(TextView) rootView.findViewById(R.id.txtV_logout);
        txtV_my_order=(TextView) rootView.findViewById(R.id.txtV_my_order);

        ll_billing_address=(LinearLayout) rootView.findViewById(R.id.ll_billing_address);
        ll_shipping_address=(LinearLayout) rootView.findViewById(R.id.ll_shipping_address);

        txtV_billing_default_address=(TextView) rootView.findViewById(R.id.txtV_billing_default_address);
        txtV_shipping_default_address=(TextView) rootView.findViewById(R.id.txtV_shipping_default_address);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_edit_shipping:
                BaseFragment fragment = new EditAddressFragment(false);
                callFragment(fragment);
                break;
            case R.id.ll_edit_billing:
                fragment =new EditAddressFragment(true);
                callFragment(fragment);
                break;
            case R.id.txtV_change_passwordt:
                callFragment(new ChangePasswordFragment());
                break;
            case R.id.txtV_logout:
                SharedPreferences preferences = getActivity().getSharedPreferences("lockapprating", 0);
                preferences.edit().remove("emailID").commit();
                MySharedPreferences.getInstance().clear();
                if(SignInSignUpFragment.Fb_tracker==1) {
                    SignInSignUpFragment.Fb_tracker = 2;
//                    FragmentManager fm = getActivity().getFragmentManager();
//                    fm.popBackStackImmediate();
                }
                else
                    SignInSignUpFragment.Fb_tracker = 0;

                UserProfileItem activeUser = UserManager.getInstance().getUser();
                activeUser.setLogin_status("0");
                MyDataBaseAdapter dbAdapter = new MyDataBaseAdapter(getActivity());
                dbAdapter.updateOnlyUserProfileStatus(activeUser);
                UserSession.setSession(getActivity());
                clearCart();
                showAlertDialogBoxObj. showCustomeDialogBoxWithoutTitle(getActivity(), "Logged out Successfully");
                FragmentManager fm = getActivity().getFragmentManager();
                fm.popBackStackImmediate();
                fm.popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                callFragment(new SignInSignUpFragment());
                break;
          /*  case R.id.txtV_my_order:
                callFragment(new MyOrderFragment());
                break;
            case R.id.txtV_my_downloads:
                callFragment(new MyDownloadsFragment());*/
        }
    }

    private void clearCart() {
        ShoppingCart.getInstance().clearCart();
        setCounterItemAddedCart();
    }
    private void setCounterItemAddedCart() {
        MainActivity.txtV_item_counter.setVisibility(View.INVISIBLE);
    }
    private void hitServiseGetUSRProfile() {
        String url = WebApiManager.getInstance().getUserProfileURL(getActivity());
        UserProfileItem user = UserManager.getInstance().getUser();
        String finalUrl=String.format(url,user.getId());

        Log.d(TAG, "hitServiseGetUSRProfile() called with:finalUrl  " + finalUrl + "");

        pDialog.show();
        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(finalUrl, new Response.Listener<String>() {
            JSONObject strJSNobj = null;

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                Log.d(TAG, response);
                try {
                    strJSNobj=new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject billingAddressJSn = strJSNobj.getJSONObject("BillingAddress");
                    JSONObject userinfo=strJSNobj.getJSONObject("CustomerInfo");
                    if(userinfo!=null)
                    {
                      /*  userEmail=userinfo.getString("email");*/
                    }
                    if(billingAddressJSn !=null) {
                        AddressData address = AddressData.create(billingAddressJSn);
                        UserProfileItem activeUser = UserManager.getInstance().getUser();
                        activeUser.setBillingAddress(address);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject shippingAddressJSn = strJSNobj.getJSONObject("ShippingAddress");
                    if(shippingAddressJSn!=null) {

                        AddressData address =  AddressData.create(shippingAddressJSn);

                        UserProfileItem activeUser = UserManager.getInstance().getUser();
                        activeUser.setShippingAddress(address);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                UserProfileItem activeUser = UserManager.getInstance().getUser();
                if (activeUser != null) {
                    if (activeUser.getBillingAddress() != null) {
                        ll_billing_address.setVisibility(View.VISIBLE);
                        txtV_billing_default_address.setVisibility(View.GONE);
                        setBillPrflDefaultValue();
                    }else{
                        ll_billing_address.setVisibility(View.GONE);
                        ll_shipping_address.setVisibility(View.GONE);
                        txtV_billing_default_address.setVisibility(View.VISIBLE);
                        txtV_shipping_default_address.setVisibility(View.VISIBLE);
                    }

                    if (activeUser.getShippingAddress() != null) {
                        ll_billing_address.setVisibility(View.VISIBLE);
                        txtV_billing_default_address.setVisibility(View.GONE);
                        ll_shipping_address.setVisibility(View.VISIBLE);
                        txtV_shipping_default_address.setVisibility(View.GONE);
                        setPrflDefaultValue();
                    }else {
                        txtV_shipping_default_address.setVisibility(View.VISIBLE);
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                pDialog.cancel();


            }
        });
    }

    private void setBillPrflDefaultValue() {
        /*UserProfileItem activeUser = UserManager.getInstance().getUser();
        AddressData address = activeUser.getBillingAddress();
        if (address != null) {
            ViewUtils.setToTextView(rootView, R.id.txtV_billing_fname, address.getFirstName());
            ViewUtils.setToTextView(rootView, R.id.txtV_billing_lname, address.getLastName());
            ViewUtils.setToTextView(rootView, R.id.txtV_billing_fnumber, address.getContactNumber());
            ViewUtils.setToTextView(rootView, R.id.txtV_billing_mail, HomeFragment.userEmail);

            ViewUtils.setToTextView(rootView, R.id.txtV_billing_state, address.getState());
            ViewUtils.setToTextView(rootView, R.id.txtV_billing_city, address.getCity());
            ViewUtils.setToTextView(rootView, R.id.txtV_billing_address, address.getStreet());
            ViewUtils.setToTextView(rootView, R.id.txtV_billing_zip, address.getZipCode());

            TextView countryText = (TextView) rootView.findViewById(R.id.txtV_billing_country);

            setCountryName(countryText, address.getCountryId());*/
        //}
    }

    private void setPrflDefaultValue() {
        UserProfileItem activeUser = UserManager.getInstance().getUser();
        AddressData address = activeUser.getShippingAddress();

        if(address != null) {
            ViewUtils.setToTextView(rootView, R.id.txtV_shipping_fname, address.getFirstName());
            ViewUtils.setToTextView(rootView, R.id.txtV_shipping_lname, address.getLastName());
            ViewUtils.setToTextView(rootView, R.id.txtV_shipping_fnumber, address.getContactNumber());
            ViewUtils.setToTextView(rootView, R.id.txtV_shipping_mail, HomeFragment.userEmail);

            ViewUtils.setToTextView(rootView, R.id.txtV_shipping_state, address.getState());
            ViewUtils.setToTextView(rootView, R.id.txtV_shipping_city, address.getCity());
            ViewUtils.setToTextView(rootView, R.id.txtV_shipping_address, address.getStreet());
            ViewUtils.setToTextView(rootView, R.id.txtV_shipping_zip, address.getZipCode());

            TextView countryText = (TextView) rootView.findViewById(R.id.txtV_shipping_country);

            setCountryName(countryText, address.getCountryId());
        }
    }
    @Override
    public void onResume() {
        Activity activity = getActivity();
        if(isAdded()&&activity!=null) {
            String headerText = getActivity().getResources().getString(R.string.welcome_header);
//            MainActivity.setHeaderText(headerText);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
        }
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Unregister the listener
        mListener = null;
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
