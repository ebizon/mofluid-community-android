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
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.Utils.Validation;
import com.ebizon.fluid.Utils.ViewUtils;
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
public class BillingAddressDownloadableFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private Spinner spinner_state;
    private Spinner spinner_country;
    private MyCountryAdapter myCountryAdapter;
    private SharedPreferences preference;
    private String customer_id;
    private ArrayList<MyStateItem> myStateItems;
    private LinearLayout ll_spnr_state;
    private String headerText;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        WelcomeFragment2.isOrdering = false;
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_billing_downloadable, null);
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
                        if (countryId != null)
                            hideShowStateSpinner(countryId, ll_spnr_state, R.id.edt_billing_state_d);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        return rootView;
    }

    private void hideShowStateSpinner(String countryId, LinearLayout llLayout, int viwId) {
        if (countryId.equalsIgnoreCase(LocaleManager.getInstance().getDefaultCountry())) {
            llLayout.setVisibility(View.VISIBLE);
            ViewUtils.setTextViewVisibility(rootView, viwId, View.GONE);
        } else {
            llLayout.setVisibility(View.GONE);
            ViewUtils.setTextViewVisibility(rootView, viwId, View.VISIBLE);
        }
    }

    private void hitServiceGetUSRProfile() {
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

    private void setUserProfileAddresses() {
        UserProfileItem activeUser = UserManager.getInstance().getUser();
        if (activeUser != null) {
            AddressData billingAddress = activeUser.getBillingAddress();
            if (billingAddress != null) {
                ViewUtils.setToTextView(rootView, R.id.edt_first_name_d, billingAddress.getFirstName());
                ViewUtils.setToTextView(rootView, R.id.edt_last_name_d, billingAddress.getLastName());
                ViewUtils.setToTextView(rootView, R.id.edt_contact_number_d, billingAddress.getContactNumber());
                ViewUtils.setToTextView(rootView, R.id.edt_email_address_d, preference.getString(ConstantDataMember.USER_INFO_USER_NAME, ""));
                ViewUtils.setToTextView(rootView, R.id.edt_billing_state_d, billingAddress.getState());
                ViewUtils.setToTextView(rootView, R.id.edt_city_d, billingAddress.getCity());
                ViewUtils.setToTextView(rootView, R.id.edt_address_d, billingAddress.getStreet());
                ViewUtils.setToTextView(rootView, R.id.edt_zip_d, billingAddress.getZipCode());
                ViewUtils.setTextViewEnabled(rootView, R.id.edt_email_address_d, false);

                setBillingCountry(billingAddress.getCountryId());
            }

        }
    }

    private void setCountryAdapter(ArrayList<MyCountryItem> countryItems) {
        myCountryAdapter = new MyCountryAdapter(getActivity(), countryItems);
        spinner_country.setAdapter(myCountryAdapter);
    }

    private void setStateAdapter(ArrayList<MyStateItem> stateItems, boolean isBilling) {
        if (isBilling) {
            MyStateAdapter billingStateAdapter = new MyStateAdapter(getActivity(), stateItems);
            spinner_state.setAdapter(billingStateAdapter);
        }
    }

    private void setFontStyle() {
        Typeface textFont = Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.REGULAR_FONT_STYLE);
        ViewUtils.setAllTextViewFont(rootView, textFont);
    }

    private void initialized() {

        preference = getActivity().getSharedPreferences(ConstantDataMember.USER_INFO_SESSION, Context.MODE_PRIVATE);
        customer_id = preference.getString(ConstantDataMember.USER_INFO_USER_ID, "");
    }

    private void getViewControlls(View rootView) {

        TextView txtV_submit = (TextView) rootView.findViewById(R.id.txtV_submit_d);
        txtV_submit.setOnClickListener(this);
        spinner_country = (Spinner) rootView.findViewById(R.id.spinner_country_d);
        spinner_state = (Spinner) rootView.findViewById(R.id.spinner_state_d);
        ll_spnr_state = (LinearLayout) rootView.findViewById(R.id.ll_spnr_state_d);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtV_submit_d:
                String validationMessage = isAllFieldFilled();
                if (validationMessage != null) {
                    String appName = Utils.getAppName(getActivity());
                    ShowAlertDialogBox showAlertDialogBoxObj = new ShowAlertDialogBox();
                    showAlertDialogBoxObj.showCustomeDialogBoxWithoutTitle(getActivity(), "Please Enter " + validationMessage);
                } else {
                    storeUserAddresses();
                    String billing_address = EncodeString.encodeStrBase64Bit(getBillingAddress());
                    String url = WebApiManager.getInstance().getUpdateProfileURL(getActivity());
                    String profile_shipp = EncodeString.encodeStrBase64Bit(getProfile());
                    String finalUrl = String.format(url, customer_id, billing_address, "", profile_shipp, "billingaddress");
                    hitServiceUpDateProfile(finalUrl);

                }
                break;
        }
    }

    private void storeUserAddresses() {
        String firstName = ViewUtils.getTextViewText(rootView, R.id.edt_first_name_d);
        String lastName = ViewUtils.getTextViewText(rootView, R.id.edt_last_name_d);
        String contactNumber = ViewUtils.getTextViewText(rootView, R.id.edt_contact_number_d);
        String emailAddress = ViewUtils.getTextViewText(rootView, R.id.edt_email_address_d);
        String street = ViewUtils.getTextViewText(rootView, R.id.edt_address_d);
        String city = ViewUtils.getTextViewText(rootView, R.id.edt_city_d);
        String zipCode = ViewUtils.getTextViewText(rootView, R.id.edt_zip_d);
        String state = ViewUtils.getTextViewText(rootView, R.id.edt_billing_state_d);
        MyStateItem stateItem = (MyStateItem) spinner_state.getSelectedItem();
        if (stateItem != null && myStateItems.isEmpty() == false) {
            state = stateItem.getRegion_name();
        }
        String country = ((MyCountryItem) spinner_country.getSelectedItem()).getCountry_id();

        AddressData billingAddress = new AddressData(firstName, lastName, contactNumber, emailAddress, street, city, zipCode, state, country);

        UserProfileItem activeUser = UserManager.getInstance().getUser();
        if (activeUser != null) {
            activeUser.setBillingAddress(billingAddress);
        }
    }

    private String isAllFieldFilled() {
        String firstName = ViewUtils.getTextViewText(rootView, R.id.edt_first_name_d);
        String lastName = ViewUtils.getTextViewText(rootView, R.id.edt_last_name_d);
        String contactNumber = ViewUtils.getTextViewText(rootView, R.id.edt_contact_number_d);

        String street = ViewUtils.getTextViewText(rootView, R.id.edt_address_d);
        String city = ViewUtils.getTextViewText(rootView, R.id.edt_city_d);
        String zipCode = ViewUtils.getTextViewText(rootView, R.id.edt_zip_d);
        String billingState = ViewUtils.getTextViewText(rootView, R.id.edt_billing_state_d);

        String validationMessage = null;

        if (!Validation.isStrNotEmpty(firstName)) {
            validationMessage = "First Name";
        } else if (!Validation.isStrNotEmpty(lastName)) {
            validationMessage = "Last Name";
        } else if (!Validation.isStrNotEmpty(contactNumber)) {
            validationMessage = "Contact Number";
        } else if (!Validation.isStrNotEmpty(street)) {
            validationMessage = "Address";
        } else if (!Validation.isStrNotEmpty(city)) {
            validationMessage = "City";
        } else if (spinner_country.getSelectedItemPosition() == 0) {
            validationMessage = "Country";
        } else if (!Validation.isStrNotEmpty(zipCode)) {
            validationMessage = "ZipCode";
        } else if (!Validation.isStrNotEmpty(billingState)) {
            MyStateItem stateItem = (MyStateItem) spinner_state.getSelectedItem();
            if (stateItem == null || stateItem.getRegion_name().equals("0")) {
                validationMessage = "State";
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
                        populateCountryList(countryItems);
                        hitServiceGetUSRProfile();

                        JSONObject mofluidDefaultCountryJSNObj = strJSNobj.getJSONObject("mofluid_default_country");
                        String defaultCountryId = mofluidDefaultCountryJSNObj.getString("country_id");
                        LocaleManager.getInstance().setDefaultCountry(defaultCountryId);

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


        } else {
            populateCountryList(countryItems);
            hitServiceGetUSRProfile();
        }
    }

    private void populateCountryList(ArrayList<MyCountryItem> countryItems) {
        ArrayList<MyCountryItem> myCountryItems = new ArrayList<>();
        String select = "";
        Activity activity = getActivity();
        if (isAdded() && activity != null)
            select = getActivity().getResources().getString(R.string.select_small);
        myCountryItems.add(new MyCountryItem(select, "0"));
        myCountryItems.addAll(countryItems);
        setCountryAdapter(myCountryItems);

        setBillingCountry(LocaleManager.getInstance().getDefaultCountry());
    }

    private void populateStateList(String countryId, boolean isBilling) {
        myStateItems = new ArrayList<>();
//        myStateItems.add(new MyStateItem("0", "Select"));
        final ArrayList<MyStateItem> stateItems = LocaleManager.getInstance().getStateList(countryId);
        if (stateItems != null) {
            myStateItems.addAll(stateItems);
            setStateAdapter(myStateItems, isBilling);
        }
        if (isBilling) {
            setBillingState(countryId);
        }
    }

    private void hitServiceForState(final String countryId, final boolean isBilling) {
        final ArrayList<MyStateItem> stateItems = LocaleManager.getInstance().getStateList(countryId);
        if (stateItems == null) {

            String urlState = WebApiManager.getInstance().getAllStateURL(getActivity());
            urlState = String.format(urlState, countryId);

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


        } else {
            populateStateList(countryId, isBilling);
        }
    }

    private void hitServiceUpDateProfile(String finalUrl) {
        Log.d(TAG, "hitServiceUpDateProfile() called with:finalUrl " + finalUrl + "");
        final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.show();
        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(finalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                Log.d(TAG, response);
                boolean isBothAddressSame = true;
               // ShippingMethodSameAddressFragment fragment = new ShippingMethodSameAddressFragment(isBothAddressSame);
                //callFragment(fragment);
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
        HashMap<String, String> kepAdd = new HashMap<>();
        kepAdd.put("email", preference.getString(ConstantDataMember.USER_INFO_USER_NAME, ""));

        JSONObject jsonObj = new JSONObject(kepAdd);

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

    private void setBillingState(String countryId) {
        UserProfileItem activeUser = UserManager.getInstance().getUser();
        if (activeUser != null) {
            AddressData addressData = activeUser.getBillingAddress();
            setState(countryId, addressData, R.id.edt_billing_state_d, spinner_state);
        }

    }


    private void setState(String countryId, AddressData addressData, int viewId, Spinner spinner) {
        if (addressData != null) {
            int index = LocaleManager.getInstance().getStateIndex(countryId, addressData.getState());
            if (index > -1) {
                spinner.setSelection(index);
                spinner.setVisibility(View.VISIBLE);
                ViewUtils.setTextViewVisibility(rootView, viewId, View.GONE);
            } else {
                spinner.setVisibility(View.GONE);
                ViewUtils.setToTextView(rootView, viewId, addressData.getState());
                ViewUtils.setTextViewVisibility(rootView, viewId, View.VISIBLE);
            }
        } else {
            spinner.setVisibility(View.GONE);
            ViewUtils.setTextViewVisibility(rootView, viewId, View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            headerText = getActivity().getResources().getString(R.string.billing_header);
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
