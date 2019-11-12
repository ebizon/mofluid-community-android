package com.mofluid.magento2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.Utils.AddressManager;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.MySharedPreferences;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.Utils.Validation;
import com.ebizon.fluid.Utils.ViewUtils;
import com.ebizon.fluid.model.Address;
import com.ebizon.fluid.model.AddressData;
import com.ebizon.fluid.model.LocaleManager;
import com.ebizon.fluid.model.MyCountryItem;
import com.ebizon.fluid.model.MyStateItem;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserProfileItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.EncodeString;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.MyCountryAdapter;
import com.mofluid.magento2.adapter.MyStateAdapter;
import com.mofluid.magento2.custome.widget.ShowAlertDialogBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ebizon on 16/11/15.
 */
public class BillingAndShippingAddressFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private RadioButton radio_btn_billingg_and_shipping_same;
    private RadioButton radio_btn_shipping_different;
    private Spinner spinner_shipping_state;
    private Spinner spinner_shipping_country;
    private Spinner spinner_state;
    private Spinner spinner_country;
    private MyCountryAdapter myCountryAdapter;
    private SharedPreferences preference;
    private String customer_id;
    private ArrayList<MyStateItem> myStateItems;
    private LinearLayout ll_spnr_state;
    private LinearLayout ll_shiping_spnr_state;
    private String headerText;
    private AddressData billingGuest, shippingGuest;
    private LinearLayout billingSpinnerLayout;
    private LinearLayout shippingSpinnerLayout;
    private TextView labelShippingState;
    private TextView labelBillingState;
    private Address guest_address;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        WelcomeFragment2.isOrdering = false;
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_billing_shipping_address, null);
            TAG = getClass().getSimpleName();
            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
            initialized();
            getViewControlls(rootView);
            setFontStyle();
            hitServiceForCountry();

            spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i > 0) {
                        MyCountryItem countryItem = LocaleManager.getInstance().getCountryAtIndex(i - 1);
                        String countryId = countryItem.getCountry_id();
                        setBillingCountry(countryId);
                        if (countryId != null){
                            hideShowStateSpinner(countryId, ll_spnr_state, R.id.edt_billing_state);
                        }

                    }else{
                        billingSpinnerLayout.setVisibility(View.GONE);
                        labelBillingState.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        spinner_shipping_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    MyCountryItem countryItem = LocaleManager.getInstance().getCountryAtIndex(i - 1);
                    String countryId = countryItem.getCountry_id();
                    setShippingCountry(countryId);
                    if (countryId != null){
                        // hideShowStateSpinner(countryId, ll_shiping_spnr_state, R.id.edt_shiping_state);
                    }

                }else{
                    shippingSpinnerLayout.setVisibility(View.GONE);
                    //labelShippingState.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return rootView;
    }

    private void hideShowStateSpinner(String countryId, LinearLayout llLayout, int viwId) {
        if (countryId.equalsIgnoreCase(LocaleManager.getInstance().getDefaultCountry())) {
            llLayout.setVisibility(View.VISIBLE);
            ViewUtils.setEditTextVisibility(rootView, viwId, View.VISIBLE);
        } else {
            llLayout.setVisibility(View.GONE);
            ViewUtils.setEditTextVisibility(rootView, viwId, View.VISIBLE);
        }
    }

    private void hitServiceGetUSRProfile() {
        if(UserManager.getInstance().getUser()!=null && UserManager.getInstance().getUser().getLogin_status()!="0") {
            String url = WebApiManager.getInstance().getUserProfileURL(getActivity());
            String finalUrl = String.format(url, customer_id);

            Log.d(TAG, "hitServiseGetUSRProfile() called with:finalUrl  " + finalUrl + "");
            final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.show();
            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(finalUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.cancel();
                    Log.d(TAG, response);

                    JSONObject strJSNobj = null;

                    try {
                        strJSNobj = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject jsonObject = strJSNobj.getJSONObject("BillingAddress");
                        AddressData addressData = AddressData.create(jsonObject);
                        UserProfileItem activeUser = UserManager.getInstance().getUser();
                        if (activeUser != null) {
                            activeUser.setBillingAddress(addressData);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject jsonObject = strJSNobj.getJSONObject("ShippingAddress");
                        AddressData addressData = AddressData.create(jsonObject);
                        UserProfileItem activeUser = UserManager.getInstance().getUser();
                        if (activeUser != null) {
                            activeUser.setShippingAddress(addressData);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setUserProfileAddresses();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                    pDialog.cancel();
                }
            });


        }
    }

    private void setUserProfileAddresses() {
        UserProfileItem activeUser = UserManager.getInstance().getUser();
        if (activeUser != null) {
            AddressData billingAddress = activeUser.getBillingAddress();
            if (billingAddress != null) {
                ViewUtils.setToTextView(rootView, R.id.edt_first_name, billingAddress.getFirstName());
                ViewUtils.setToTextView(rootView, R.id.edt_last_name, billingAddress.getLastName());
                ViewUtils.setToTextView(rootView, R.id.edt_contact_number, billingAddress.getContactNumber());
                ViewUtils.setToTextView(rootView, R.id.edt_email_address, preference.getString(ConstantDataMember.USER_INFO_USER_NAME, ""));
                ViewUtils.setToEditText(rootView, R.id.edt_billing_state, billingAddress.getState());
                ViewUtils.setToTextView(rootView, R.id.edt_city, billingAddress.getCity());
                ViewUtils.setToTextView(rootView, R.id.edt_address, billingAddress.getStreet());
                ViewUtils.setToTextView(rootView, R.id.edt_zip, billingAddress.getZipCode());
                ViewUtils.setTextViewEnabled(rootView, R.id.edt_email_address, false);

                setBillingCountry(billingAddress.getCountryId());
            }

            AddressData shippingAddress = activeUser.getShippingAddress();

            if (shippingAddress != null) {
                ViewUtils.setToTextView(rootView, R.id.edt_first_name_shipping, shippingAddress.getFirstName());
                ViewUtils.setToTextView(rootView, R.id.edt_last_name_shipping, shippingAddress.getLastName());
                ViewUtils.setToTextView(rootView, R.id.edt_contact_number_shipping, shippingAddress.getContactNumber());
                ViewUtils.setToTextView(rootView, R.id.edt_shipping_email_address, preference.getString(ConstantDataMember.USER_INFO_USER_NAME, ""));
                ViewUtils.setToEditText(rootView, R.id.edt_shiping_state, shippingAddress.getState());
                ViewUtils.setToTextView(rootView, R.id.edt_shipping_address, shippingAddress.getStreet());
                ViewUtils.setToTextView(rootView, R.id.edt_shipping_city, shippingAddress.getCity());
                ViewUtils.setToTextView(rootView, R.id.edt_shipping_zip, shippingAddress.getZipCode());
                ViewUtils.setTextViewEnabled(rootView, R.id.edt_shipping_email_address, false);
                setShippingCountry(shippingAddress.getCountryId());
            }





        }
    }

    private void setCountryAdapter(ArrayList<MyCountryItem> countryItems) {
        myCountryAdapter=new MyCountryAdapter(getActivity(), countryItems);

        spinner_country.setAdapter(myCountryAdapter);
        spinner_shipping_country.setAdapter(myCountryAdapter);
    }

    private void setStateAdapter(ArrayList<MyStateItem> stateItems, boolean isBilling) {
        if(isBilling) {
            MyStateAdapter billingStateAdapter = new MyStateAdapter(getActivity(), stateItems);
            spinner_state.setAdapter(billingStateAdapter);
        }else{
            MyStateAdapter shippingStateAdapter = new MyStateAdapter(getActivity(), stateItems);
            spinner_shipping_state.setAdapter(shippingStateAdapter);
        }
    }

    private void setFontStyle() {
        Typeface textFont = Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.REGULAR_FONT_STYLE);
        ViewUtils.setAllTextViewFont(rootView, textFont);
        radio_btn_billingg_and_shipping_same.setTypeface(textFont);
        radio_btn_shipping_different.setTypeface(textFont);
    }

    private void initialized() {

        preference=getActivity().getSharedPreferences(ConstantDataMember.USER_INFO_SESSION, Context.MODE_PRIVATE);
        customer_id=preference.getString(ConstantDataMember.USER_INFO_USER_ID, "");
    }

    private void getViewControlls(View rootView) {

        radio_btn_billingg_and_shipping_same=(RadioButton) rootView.findViewById(R.id.radio_btn_billingg_and_shipping_same);
        radio_btn_billingg_and_shipping_same.setOnClickListener(this);
        radio_btn_shipping_different=(RadioButton) rootView.findViewById(R.id.radio_btn_shipping_different);
        radio_btn_shipping_different.setOnClickListener(this);
        TextView txtV_submit=(TextView) rootView.findViewById(R.id.txtV_submit);
        txtV_submit.setOnClickListener(this);
        spinner_country=(Spinner) rootView.findViewById(R.id.spinner_country);
        spinner_state=(Spinner) rootView.findViewById(R.id.spinner_state);
        billingSpinnerLayout=(LinearLayout) rootView.findViewById(R.id.ll_spnr_state);
        shippingSpinnerLayout=(LinearLayout) rootView.findViewById(R.id.ll_shiping_spnr_state);
        spinner_shipping_country=(Spinner) rootView.findViewById(R.id.spinner_shipping_country);
        spinner_shipping_state=(Spinner) rootView.findViewById(R.id.spinner_shipping_state);
        ll_spnr_state=(LinearLayout) rootView.findViewById(R.id.ll_spnr_state);
        ll_shiping_spnr_state=(LinearLayout) rootView.findViewById(R.id.ll_shiping_spnr_state);
        labelShippingState=(TextView) rootView.findViewById(R.id.txtV_shipping_satate);
        labelBillingState=(TextView) rootView.findViewById(R.id.txtV_satate);

    }

    @Override
    public void onClick(View view) {
        boolean isBothAddressSame=radio_btn_billingg_and_shipping_same.isChecked();
        switch (view.getId()) {
            case R.id.radio_btn_billingg_and_shipping_same:
                if(isBothAddressSame) {
                    LinearLayout ll_shipping_address = (LinearLayout) rootView.findViewById(R.id.ll_shipping_address);
                    ll_shipping_address.setVisibility(View.GONE);
                    radio_btn_shipping_different.setChecked(false);
                }
                break;
            case R.id.radio_btn_shipping_different:
                if(radio_btn_shipping_different.isChecked()) {
                    LinearLayout ll_shipping_address = (LinearLayout) rootView.findViewById(R.id.ll_shipping_address);
                    ll_shipping_address.setVisibility(View.VISIBLE);
                    radio_btn_billingg_and_shipping_same.setChecked(false);
                }
                break;

            case R.id.txtV_submit:
                String validationMessage = isAllFieldFilled();
                if(validationMessage != null) {
                    String appName = Utils.getAppName(getActivity());
                    ShowAlertDialogBox showAlertDialogBoxObj=new ShowAlertDialogBox();
                    showAlertDialogBoxObj.showCustomeDialogBoxWithoutTitle(getActivity(),  "Please Enter "+validationMessage);
                } else {
                    storeUserAddresses();
                    boolean isGuest= MySharedPreferences.getInstance().get(MySharedPreferences.CUSTOMER_ID)==null;
                    if (isGuest != true) {
                        String billing_address = EncodeString.encodeStrBase64Bit(getBillingAddress());
                        String url = WebApiManager.getInstance().getUpdateProfileURL(getActivity());
                        String profile_shipp = EncodeString.encodeStrBase64Bit(getProfile());

                        String finalUrl = String.format(url, customer_id, billing_address, "", profile_shipp, "billingaddress");
                        hitServiceUpDateProfile(finalUrl);

                        if(!isBothAddressSame) {
                            String shipping_address= EncodeString.encodeStrBase64Bit(getShippingAddress());
                            finalUrl = String.format(url, customer_id, billing_address, shipping_address, profile_shipp, "shippingaddress");
                            hitServiceUpDateProfile(finalUrl);
                        }
                    }
                    else
                    {
                        boolean isBothAddressSameGuest = radio_btn_billingg_and_shipping_same.isChecked();
                        //ShippingMethodSameAddressFragment fragment = new ShippingMethodSameAddressFragment(billingGuest,shippingGuest,isBothAddressSameGuest, isGuest);
                        ShippingMethodSameAddressFragment fragment=new ShippingMethodSameAddressFragment();
                        //fragment.setBillingAddress();
                        callFragment(fragment);
                    }
                }
                break;
        }
    }

    private void storeUserAddresses() {
        String statecode = null;
        String firstName = ViewUtils.getTextViewText(rootView, R.id.edt_first_name);
        String lastName = ViewUtils.getTextViewText(rootView, R.id.edt_last_name);
        String contactNumber = ViewUtils.getTextViewText(rootView, R.id.edt_contact_number);
        String emailAddress;
        boolean isGuest= MySharedPreferences.getInstance().get(MySharedPreferences.CUSTOMER_ID)==null;
        if(isGuest!=true)
            emailAddress= ViewUtils.getTextViewText(rootView, R.id.edt_email_address);
        else
            emailAddress = HomeFragment.userEmail;
        String street = ViewUtils.getTextViewText(rootView, R.id.edt_address);
        String city = ViewUtils.getTextViewText(rootView, R.id.edt_city);
        String zipCode = ViewUtils.getTextViewText(rootView, R.id.edt_zip);
        String state = ViewUtils.getEditTextText(rootView, R.id.edt_billing_state);
        MyStateItem stateItem = (MyStateItem) spinner_state.getSelectedItem();
        if (stateItem != null && myStateItems.isEmpty() == false) {
            state = stateItem.getRegion_name();
            statecode = stateItem.getRegion_id();
        }
        String country=((MyCountryItem)spinner_country.getSelectedItem()).getCountry_id();

        if(statecode!=null)
            state = statecode;
        AddressData billingAddress = new AddressData(firstName, lastName, contactNumber, emailAddress, street, city, zipCode, state, country);
        AddressData shippingAddress = new AddressData(billingAddress);

        if(radio_btn_shipping_different.isChecked()) {
            firstName = ViewUtils.getTextViewText(rootView, R.id.edt_first_name_shipping);
            lastName = ViewUtils.getTextViewText(rootView, R.id.edt_last_name_shipping);
            contactNumber = ViewUtils.getTextViewText(rootView, R.id.edt_contact_number_shipping);
            emailAddress = ViewUtils.getTextViewText(rootView, R.id.edt_shipping_email_address);
            street = ViewUtils.getTextViewText(rootView, R.id.edt_shipping_address);
            city = ViewUtils.getTextViewText(rootView, R.id.edt_shipping_city);
            state = ViewUtils.getEditTextText(rootView, R.id.edt_shiping_state);
            MyStateItem stateItem2 = (MyStateItem) spinner_shipping_state.getSelectedItem();
            if (stateItem2 != null && myStateItems.isEmpty()==false) {
                state = stateItem2.getRegion_name();
            }
            country = ((MyCountryItem)spinner_shipping_country.getSelectedItem()).getCountry_id();
            zipCode = ViewUtils.getTextViewText(rootView, R.id.edt_shipping_zip);
            shippingAddress = new AddressData(firstName, lastName, contactNumber, emailAddress, street, city, zipCode, state, country);
        }

        UserProfileItem activeUser = UserManager.getInstance().getUser();
        if (activeUser != null) {
            activeUser.setBillingAddress(billingAddress);
            activeUser.setShippingAddress(shippingAddress);
        } else if (isGuest == true) {
            billingGuest = billingAddress;
            shippingGuest = shippingAddress;
        }
        guest_address=new Address(0,firstName,lastName,contactNumber,street,city,country,country,statecode,state,zipCode);
        AddressManager.getInstance().setGuest_address(guest_address);
    }
    private String isAllFieldFilled() {
        String firstName = ViewUtils.getTextViewText(rootView, R.id.edt_first_name);
        String lastName = ViewUtils.getTextViewText(rootView, R.id.edt_last_name);
        String contactNumber = ViewUtils.getTextViewText(rootView, R.id.edt_contact_number);

        String street = ViewUtils.getTextViewText(rootView, R.id.edt_address);
        String city = ViewUtils.getTextViewText(rootView, R.id.edt_city);
        String zipCode = ViewUtils.getTextViewText(rootView, R.id.edt_zip);
        String billingState = ViewUtils.getEditTextText(rootView, R.id.edt_billing_state);

        String validationMessage = null;

        if (!Validation.isStrNotEmpty(firstName)) {
            validationMessage = getActivity().getResources().getString(R.string.display_first_name);
        } else if (!Validation.isStrNotEmpty(lastName)) {
            validationMessage = getActivity().getResources().getString(R.string.display_last_name);
        } else if (!Validation.isStrNotEmpty(contactNumber)) {
            validationMessage = getActivity().getResources().getString(R.string.display_contact_no);
        } else if (!Validation.isStrNotEmpty(street)) {
            validationMessage = getActivity().getResources().getString(R.string.display_address);
        } /*else if (!Validation.isStrNotEmpty(city)) {
            validationMessage = "City";
        }*/ else if (spinner_country.getSelectedItemPosition() == 0) {
            validationMessage = getActivity().getResources().getString(R.string.display_country);
        } else if (!Validation.isStrNotEmpty(zipCode)) {
            validationMessage = getActivity().getResources().getString(R.string.display_zipcode);
        } else if (!Validation.isStrNotEmpty(billingState)) {
            MyStateItem stateItem = (MyStateItem) spinner_state.getSelectedItem();
            if (stateItem == null || stateItem.getRegion_name().equals("0")) {
                validationMessage = getActivity().getResources().getString(R.string.display_state);
            }
        } else if (radio_btn_shipping_different.isChecked()) {
            firstName = ViewUtils.getTextViewText(rootView, R.id.edt_first_name_shipping);
            lastName = ViewUtils.getTextViewText(rootView, R.id.edt_last_name_shipping);
            contactNumber = ViewUtils.getTextViewText(rootView, R.id.edt_contact_number_shipping);

            street = ViewUtils.getTextViewText(rootView, R.id.edt_shipping_address);
            city = ViewUtils.getTextViewText(rootView, R.id.edt_shipping_city);
            zipCode = ViewUtils.getTextViewText(rootView, R.id.edt_shipping_zip);

            if (!Validation.isStrNotEmpty(firstName)) {
                validationMessage = getActivity().getResources().getString(R.string.dispaly_shiping_firstName);
            }
            if (!Validation.isStrNotEmpty(lastName)) {
                validationMessage = getActivity().getResources().getString(R.string.dispaly_shiping_lastname);
            } else if (!Validation.isStrNotEmpty(contactNumber)) {
                validationMessage = getActivity().getResources().getString(R.string.dispaly_shiping_contactNumber);
            }  else if (!Validation.isStrNotEmpty(street)) {
                validationMessage = getActivity().getResources().getString(R.string.dispaly_shiping_address);
            } /*else if (!Validation.isStrNotEmpty(city)) {
                validationMessage = "Shipping City";
            }*/ else if (spinner_country.getSelectedItemPosition() == 0) {
                validationMessage = getActivity().getResources().getString(R.string.dispaly_shiping_country);
            } else if (!Validation.isStrNotEmpty(zipCode)) {
                validationMessage = getActivity().getResources().getString(R.string.dispaly_shiping_zipcode);
            }
            String shippingState = ViewUtils.getEditTextText(rootView, R.id.edt_shiping_state);
            if (!Validation.isStrNotEmpty(shippingState)) {
                MyStateItem stateItem = (MyStateItem)spinner_shipping_state.getSelectedItem();
                if(spinner_shipping_state.getCount()==0) return null;
                if(stateItem == null || stateItem.getRegion_name().equals("0")) {
                    validationMessage = getActivity().getResources().getString(R.string.dispaly_shiping_state);
                }
            }
        }
        return validationMessage;
    }


    private void hitServiceForCountry() {
        final ArrayList<MyCountryItem> countryItems = LocaleManager.getInstance().getCountryList();
        if (countryItems.isEmpty()) {
            String urlCountry = WebApiManager.getInstance().getAllCountryURL(getActivity());

            final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.show();
            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(urlCountry, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.cancel();
                    Log.d(TAG, response);
                    try {
                        JSONObject strJSNobj = new JSONObject(response);
                        JSONArray mofluid_countries = strJSNobj.getJSONArray("mofluid_countries");

                        for (int i = 0; i < mofluid_countries.length(); i++) {
                            JSONObject jsnObj = mofluid_countries.getJSONObject(i);
                            String country_name = jsnObj.getString("country_name");
                            String id = jsnObj.getString("country_id");
                            countryItems.add(new MyCountryItem(country_name, id));
                        }
                        JSONObject mofluidDefaultCountryJSNObj = strJSNobj.getJSONObject("mofluid_default_country");
                        String defaultCountryId = mofluidDefaultCountryJSNObj.getString("country_id");
                        LocaleManager.getInstance().setDefaultCountry(defaultCountryId);
                        populateCountryList(countryItems);
                        hitServiceGetUSRProfile();



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    myCountryAdapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                    pDialog.cancel();
                }
            });


        }else{
            populateCountryList(countryItems);
            hitServiceGetUSRProfile();
        }
    }

    private void populateCountryList(ArrayList<MyCountryItem> countryItems){
        ArrayList<MyCountryItem> myCountryItems = new ArrayList<>();
        String select = "";
        Activity activity= getActivity();
        if(isAdded()&&activity!=null)
            select = getActivity().getResources().getString(R.string.select_small);
        myCountryItems.add(new MyCountryItem(select, "0"));
        myCountryItems.addAll(countryItems);
        setCountryAdapter(myCountryItems);

        setBillingCountry(LocaleManager.getInstance().getDefaultCountry());
        setShippingCountry(LocaleManager.getInstance().getDefaultCountry());
    }

    private void populateStateList(String countryId, boolean isBilling){
        myStateItems = new ArrayList<>();
//        myStateItems.add(new MyStateItem("0", "Select"));
        final ArrayList<MyStateItem> stateItems = LocaleManager.getInstance().getStateList(countryId);
        if(stateItems != null) {
            myStateItems.addAll(stateItems);
            setStateAdapter(myStateItems, isBilling);
        }
        if(isBilling) {
            setBillingState(countryId,myStateItems);
        }else {
            setShippingState(countryId,myStateItems);
        }
    }

    private void hitServiceForState(final String countryId, final boolean isBilling) {
        final ArrayList<MyStateItem> stateItems = LocaleManager.getInstance().getStateList(countryId);
        String defaultcountryID= LocaleManager.getInstance().getDefaultCountry();
        if (stateItems == null) {
            String urlState = WebApiManager.getInstance().getAllStateURL(getActivity());
            if(countryId==null){
                urlState = String.format(urlState, defaultcountryID);
            }else {
                urlState = String.format(urlState, countryId);
            }

            final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.show();

            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(urlState, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.cancel();
                    Log.d(TAG, response);
                    try {
                        JSONObject strJSNobj = new JSONObject(response);
                        JSONArray jsonArray = strJSNobj.getJSONArray("mofluid_regions");
                        ArrayList<MyStateItem> stateItemArrayList = new ArrayList<>();
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsnObj = jsonArray.getJSONObject(i);
                                String region_name = jsnObj.getString("region_name");
                                String region_id = jsnObj.getString("region_id");
                                stateItemArrayList.add(new MyStateItem(region_id, region_name));
                            }

                            LocaleManager.getInstance().setStateList(countryId, stateItemArrayList);
                            populateStateList(countryId, isBilling);
                        }
                    } catch (JSONException e) {
                        populateStateList(countryId, isBilling);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                    pDialog.cancel();
                }
            });



        }else{
            populateStateList(countryId, isBilling);
        }
    }

    private void hitServiceUpDateProfile(String finalUrl) {
        Log.d(TAG, "hitServiceUpDateProfile() called with:finalUrl " + finalUrl + "");
        final ProgressDialog pDialog =new ProgressDialog(getActivity(),R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.show();
        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(finalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                Log.d(TAG, response);
                ShippingMethodSameAddressFragment fragment = new ShippingMethodSameAddressFragment();
                callFragment(fragment);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                pDialog.cancel();
            }
        });


    }

    private String getProfile() {
        HashMap<String,String>  kepAdd=new HashMap<>();
        kepAdd.put("email", preference.getString(ConstantDataMember.USER_INFO_USER_NAME, ""));

        JSONObject jsonObj=new JSONObject(kepAdd);

        return jsonObj.toString();
    }

    private String getShippingAddress() {
        JSONObject jsonObj = new JSONObject();
        UserProfileItem activeUser = UserManager.getInstance().getUser();

        if (activeUser != null){
            AddressData addressData = activeUser.getShippingAddress();
            if(addressData != null) {
                jsonObj = addressData.getAsShippingJSON();
            }
        }

        return jsonObj.toString();
    }

    private String getBillingAddress() {
        JSONObject jsonObj = new JSONObject();
        UserProfileItem activeUser = UserManager.getInstance().getUser();
        if (activeUser != null) {
            AddressData addressData = activeUser.getBillingAddress();
            if (addressData != null) {
                jsonObj = addressData.getAsBillingJSON();
            }
        }

        return jsonObj.toString();
    }

    private void setBillingCountry(String countryId) {
        Log.d(TAG, "setBillingCountry() called with: " + "countryId = [" + countryId + "]");
        int index = LocaleManager.getInstance().getCountryIndex(countryId);
        spinner_country.setSelection(index + 1);
        hitServiceForState(countryId, true);

    }

    private void setShippingCountry(String countryId) {
        int index = LocaleManager.getInstance().getCountryIndex(countryId);
        spinner_shipping_country.setSelection(index+1);
        hitServiceForState(countryId, false);
    }

    private void setBillingState(String countryId, ArrayList<MyStateItem> myStateItems) {
        labelBillingState.setVisibility(View.VISIBLE);
        Log.d("saddam1",myStateItems.size()+"");
        if (myStateItems != null && myStateItems.size() > 0) {
            billingSpinnerLayout.setVisibility(View.VISIBLE);
            ViewUtils.setEditTextVisibility(rootView, R.id.edt_billing_state, View.GONE);
            UserProfileItem activeUser = UserManager.getInstance().getUser();
            if(activeUser!=null) {
                AddressData addressData = activeUser.getBillingAddress();
                setState(countryId, addressData, R.id.edt_billing_state, spinner_state, billingSpinnerLayout, myStateItems);
            }
        }
        else{
            billingSpinnerLayout.setVisibility(View.GONE);
            ViewUtils.setEditTextVisibility(rootView, R.id.edt_billing_state, View.VISIBLE);

        }
    }

    private void setShippingState(String countryId, ArrayList<MyStateItem> myStateItems) {
        labelShippingState.setVisibility(View.VISIBLE);
        Log.d("saddam",myStateItems.size()+"");
        if (myStateItems != null && myStateItems.size() > 0) {
            shippingSpinnerLayout.setVisibility(View.VISIBLE);
            ViewUtils.setEditTextVisibility(rootView, R.id.edt_shiping_state, View.GONE);
            UserProfileItem activeUser = UserManager.getInstance().getUser();
            if(activeUser!=null) {
                AddressData addressData = activeUser.getShippingAddress();
                setState(countryId, addressData, R.id.edt_shiping_state, spinner_shipping_state, shippingSpinnerLayout, myStateItems);
            }
        }
        else{
            shippingSpinnerLayout.setVisibility(View.GONE);
            ViewUtils.setEditTextVisibility(rootView, R.id.edt_shiping_state, View.VISIBLE);

        }
    }

    private void setState(String countryId, AddressData addressData, int viewId, Spinner spinner, LinearLayout billingSpinnerLayout, ArrayList<MyStateItem> myStateItems) {
        if(addressData != null) {
            int index = LocaleManager.getInstance().getStateIndex(countryId, addressData.getState());
            if (index > -1) {
                spinner.setSelection(index);
                spinner.setVisibility(View.VISIBLE);
                ViewUtils.setEditTextVisibility(rootView, viewId, View.GONE);
            } else {
               /* spinner.setVisibility(View.GONE);
                ViewUtils.setToTextView(rootView, viewId, addressData.getState());
                ViewUtils.setEditTextVisibility(rootView, viewId, View.VISIBLE);*/
                if(myStateItems!=null||myStateItems.size()>0) {
                    billingSpinnerLayout.setVisibility(View.VISIBLE);
                    ViewUtils.setEditTextVisibility(rootView, viewId, View.GONE);
                }else{
                    billingSpinnerLayout.setVisibility(View.GONE);
                    ViewUtils.setEditTextVisibility(rootView, viewId, View.VISIBLE);
                }
            }
        } else{
            if(myStateItems!=null||myStateItems.size()>0) {
                billingSpinnerLayout.setVisibility(View.VISIBLE);
                ViewUtils.setEditTextVisibility(rootView, viewId, View.GONE);
            }else{
                billingSpinnerLayout.setVisibility(View.GONE);
                ViewUtils.setEditTextVisibility(rootView, viewId, View.VISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        Activity activity = getActivity();
        if(isAdded()&&activity!=null) {
            headerText = getActivity().getResources().getString(R.string.billing_and_shipping_header);
            //MainActivity.setHeaderText(headerText);
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
