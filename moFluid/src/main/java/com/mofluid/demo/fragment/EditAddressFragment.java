package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.Utils.AddressManager;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
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
 * Created by ebizon on 16/12/15.
 */
@SuppressLint("ValidFragment")
public class EditAddressFragment extends BaseFragment implements View.OnClickListener {
    MyCountryAdapter myCountryAdapter;
    View rootView;
    private EditText edt_zip;
    private Spinner spinner_state;
    private Spinner spinner_country;
    private EditText edt_city;
    private EditText edt_address;
    private EditText edt_email_address;
    private EditText edt_contact_number;
    private EditText edt_last_name;
    private EditText edt_first_name;
    private Typeface lat0_Font_ttf;
    private String TAG;
    private String validationMesssage;
    private int country_selected_position;
    private MyStateAdapter myStateAdapter;
    private ArrayList<MyStateItem> my_state_list;
    private String billing_state_value;
    private EditText edt_billing_state;
    private LinearLayout ll_spnr_state;
    private String headerText;
    private boolean isBilling= false;
    private int default_country;
    private String select;
    private ProgressDialog pDialog;
    private ArrayList<MyCountryItem> countryItems= new ArrayList<>();
    private LinearLayout spinnerLayout;
    private int indexSpinnerCountry;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;


    public EditAddressFragment(boolean isBilling) {
        this.isBilling = isBilling;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragmentt_edit_billing_address, null);
        initialized();
        getViewControll(rootView);
        setFontStyle(rootView);
        pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        hitServiceForCountry();



        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {

                    MyCountryItem countryItem = LocaleManager.getInstance().getCountryAtIndex(i - 1);
                    String countryId = countryItem.getCountry_id();
                    if (countryId != null)
                        my_state_list.clear();
                    my_state_list.add(new MyStateItem("0", select));
                    setBillingCountry(countryId);
                    // hideShowStateSpinner(countryId, ll_spnr_state, R.id.edt_billing_state);
                    // country_selected_position = i;
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
            Log.d("PiyushKatyal1", "1");
            ViewUtils.setTextViewVisibility(rootView, viwId, View.GONE);
        } else {
            Log.d("PiyushKatyal1", "2");
            llLayout.setVisibility(View.GONE);
            ViewUtils.setTextViewVisibility(rootView, viwId, View.VISIBLE);
//            ViewUtils.setToTextView(rootView, viwId, "Enter State");
        }
    }

    private void setBillPrflDefaultValue() {
        AddressData address = getAddressData();
        if (address != null) {
            edt_first_name.setText(address.getFirstName());
            edt_last_name.setText(address.getLastName());
            edt_contact_number.setText(address.getContactNumber());
            edt_email_address.setText(address.getEmailAddress());
            edt_billing_state.setText(address.getState());
            edt_city.setText(address.getCity());
            edt_address.setText(address.getStreet());
            edt_zip.setText(address.getZipCode());
            setBillingCountry(address.getCountryId());
        }
    }

    private void setCountryAdapter(ArrayList<MyCountryItem> my_country_lista) {

        myCountryAdapter = new MyCountryAdapter(getActivity(), my_country_lista);
        spinner_country.setAdapter(myCountryAdapter);
    }

    private void setStateAdapter(ArrayList<MyStateItem> stateItems, boolean isBilling) {

        MyStateAdapter billingStateAdapter = new MyStateAdapter(getActivity(), stateItems);
        spinner_country.setSelection(country_selected_position);
        spinner_state.setAdapter(billingStateAdapter);

    }

    private void setFontStyle(View rootView) {
        ViewUtils.setAllTextViewFont(rootView, lat0_Font_ttf);
    }

    private void initialized() {

        lat0_Font_ttf = Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.REGULAR_FONT_STYLE);
        TAG = getClass().getSimpleName();
        my_state_list = new ArrayList<>();
        Activity activity= getActivity();
        if(isAdded()&&activity!=null)
            select =getActivity().getResources().getString(R.string.select_small);
    }

    private void getViewControll(View rootView) {


        TextView txtV_update = (TextView) rootView.findViewById(R.id.txtV_update);
        txtV_update.setOnClickListener(this);
        edt_first_name = (EditText) rootView.findViewById(R.id.edt_first_name);
        edt_last_name = (EditText) rootView.findViewById(R.id.edt_last_name);
        edt_contact_number = (EditText) rootView.findViewById(R.id.edt_contact_number);
        edt_email_address = (EditText) rootView.findViewById(R.id.edt_email_address);
        edt_email_address.setText(HomeFragment.userEmail);
        edt_email_address.setEnabled(false);
        edt_address = (EditText) rootView.findViewById(R.id.edt_address);
        edt_city = (EditText) rootView.findViewById(R.id.edt_city);
        spinner_country = (Spinner) rootView.findViewById(R.id.spinner_country);
        spinner_state = (Spinner) rootView.findViewById(R.id.spinner_state);
        spinnerLayout=(LinearLayout)rootView.findViewById(R.id.ll_spnr_state);
        edt_zip = (EditText) rootView.findViewById(R.id.edt_zip);
        ll_spnr_state = (LinearLayout) rootView.findViewById(R.id.ll_spnr_state);
        edt_billing_state = (EditText) rootView.findViewById(R.id.edt_billing_state);
        TextView title = (TextView) rootView.findViewById(R.id.txtV_please_fill);
        if (isBilling==false) {
            title.setText(R.string.shiping_address);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtV_update:
                boolean isValid = checkAndSetAddress();
                if (!isValid) {
                    new ShowAlertDialogBox().showCustomeDialogBoxWithoutTitle(getActivity(), "Please Enter " + validationMesssage);
                } else {
                    hitServiceUpDateProfile();
                }
                break;
        }
    }

    private boolean checkAndSetAddress() {

        String firstName = edt_first_name.getText().toString();
        String lstName = edt_last_name.getText().toString();
        String contactNumber = edt_contact_number.getText().toString();
        String emailAddress = edt_email_address.getText().toString();
        String street = edt_address.getText().toString();
        String city = edt_city.getText().toString();
        String sttatet=null;
        String country = ((MyCountryItem) spinner_country.getSelectedItem()).getCountry_id();
        String state = edt_billing_state.getText().toString();
        MyStateItem stateItem = (MyStateItem) spinner_state.getSelectedItem();
        Log.d("PKatyal", "isShown of state spinner"+ spinner_state.isShown()+"");
        if (stateItem != null && my_state_list.isEmpty()==false&&spinner_state.isShown()==true) {
            state = stateItem.getRegion_name();
            sttatet = stateItem.getRegion_id();
        }
        String zipCode = edt_zip.getText().toString();

        if (!Validation.isStrNotEmpty(firstName)) {
            validationMesssage = getActivity().getResources().getString(R.string.display_first_name);
            return false;
        } else if (!Validation.isStrNotEmpty(lstName)) {
            validationMesssage = getActivity().getResources().getString(R.string.display_last_name);
            return false;
        } else if (!Validation.isStrNotEmpty(contactNumber)) {
            validationMesssage = getActivity().getResources().getString(R.string.display_contact_no);
            return false;
        } else if (!Validation.isStrNotEmpty(street)) {
            validationMesssage = getActivity().getResources().getString(R.string.display_address);
            return false;
        }/* else if (!Validation.isStrNotEmpty(city)) {
            validationMesssage = "City";
            return false;
        }*/ else if (spinner_state.getSelectedItemPosition() == 0 && my_state_list.isEmpty() == false && spinner_state.isShown()==true) {
            validationMesssage = getActivity().getResources().getString(R.string.display_state);
            return false;
        } else if (!Validation.isStrNotEmpty(zipCode)) {
            validationMesssage = getActivity().getResources().getString(R.string.display_zipcode);

            return false;
        }

        if (!my_state_list.isEmpty()) {
            billing_state_value = my_state_list.get(spinner_state.getSelectedItemPosition()).getRegion_name();
            if (spinner_state.getSelectedItemPosition() == 0) {
                validationMesssage = getActivity().getResources().getString(R.string.display_state);
                return false;
            }
        } else {
            billing_state_value = edt_billing_state.getText().toString();
            if (!Validation.isStrNotEmpty(billing_state_value)) {
                validationMesssage = getActivity().getResources().getString(R.string.display_state);
                return false;
            }
        }
        if(sttatet!=null)
            state = sttatet;
        AddressData address = new AddressData(firstName, lstName, contactNumber, emailAddress, street, city, zipCode, state, country);
        if (isBilling==true) {
            UserManager.getInstance().getUser().setBillingAddress(address);
        } else {
            UserManager.getInstance().getUser().setShippingAddress(address);
        }

        return true;
    }
    private void populateStateList(String countryId, boolean isBilling) {
        ArrayList<MyStateItem> myStateItems = new ArrayList<>();
        myStateItems.add(new MyStateItem("0", select));
        final ArrayList<MyStateItem> stateItems = LocaleManager.getInstance().getStateList(countryId);
        if (stateItems != null) {
            myStateItems.addAll(stateItems);
            my_state_list = myStateItems;
            setStateAdapter((ArrayList<MyStateItem>) myStateItems.clone(), isBilling);
            if(stateItems==null || stateItems.size()<=0)
            {
                Log.d("PiyushKatyal1", "3");
                spinnerLayout.setVisibility(View.GONE);
                spinner_state.setVisibility(View.GONE);
                ViewUtils.setToTextView(rootView, R.id.edt_billing_state, UserManager.getInstance().getUser().getShippingAddress().getState()); // both case biling
                Log.d("PiyushKatyal1", "4");
                ViewUtils.setTextViewVisibility(rootView, R.id.edt_billing_state, View.VISIBLE);
            }
            else
            {
                spinnerLayout.setVisibility(View.VISIBLE);
                int index = checkStatePresentList(stateItems, countryId);
                spinner_state.setSelection(index);
                spinner_state.setVisibility(View.VISIBLE);
                Log.d("PiyushKatyal1", "5");
                ViewUtils.setTextViewVisibility(rootView, R.id.edt_billing_state, View.GONE);
            }
        }
        if(isBilling==true) {
            setBillingState(countryId);
        }else {
            setShippingState(countryId);
        }
    }
    private int checkStatePresentList(ArrayList<MyStateItem> stateItems, String countryId) {
        int index=-1;
        UserProfileItem activeUser = UserManager.getInstance().getUser();
        AddressData addressData = activeUser.getBillingAddress();
        if(addressData!=null)
            index = LocaleManager.getInstance().getStateIndex(countryId, addressData.getState());
        if(index>-1)
        {
            return (index+1);
        }
        else
            return 0;
    }

    private void setShippingState(String countryId) {
        UserProfileItem activeUser = UserManager.getInstance().getUser();
        if (activeUser != null) {
            AddressData addressData = activeUser.getShippingAddress();
            //setState(countryId, addressData, R.id.edt_billing_state, spinner_state);
        }
    }

    private void setBillingState(String countryId) {
        UserProfileItem activeUser = UserManager.getInstance().getUser();
        if (activeUser != null) {
            AddressData addressData = activeUser.getBillingAddress();
            //setState(countryId, addressData, R.id.edt_billing_state, spinner_state);
        }

    }

    private void setState(String countryId, AddressData addressData, int viewId, Spinner spinner) {
        if (addressData != null) {
            int index = LocaleManager.getInstance().getStateIndex(countryId, addressData.getState());
            if (index > -1) {
                spinner.setSelection(index + 1);
                spinner.setVisibility(View.VISIBLE);
                ViewUtils.setTextViewVisibility(rootView, viewId, View.GONE);
            } else {
                spinner.setVisibility(View.GONE);
//                if(ViewUtils.getTextViewText(rootView,viewId).toString().equals(addressData.getState()))
//                    ViewUtils.setToTextView(rootView, viewId,"Enter State");
//                else
                ViewUtils.setToTextView(rootView, viewId, addressData.getState()); // both case biling

                ViewUtils.setTextViewVisibility(rootView, viewId, View.VISIBLE);
            }
        } else {
            spinner.setVisibility(View.GONE);
            ViewUtils.setTextViewVisibility(rootView, viewId, View.VISIBLE);
        }
    }

    private void hitServiceForState(final String countryId, final boolean isBilling) {
        final ArrayList<MyStateItem> stateItems = LocaleManager.getInstance().getStateList(countryId);

        if (stateItems == null) {

            String urlState = WebApiManager.getInstance().getAllStateURL(getActivity());
            if(countryId==null) {
                String defaultCountry = LocaleManager.getInstance().getDefaultCountry();
                urlState = String.format(urlState, defaultCountry);
            }else{
                urlState = String.format(urlState, countryId);
            }
            Log.d(TAG,"@vnish- state service url : "+urlState);
            StateService stateService = new StateService();
            stateService.execute(urlState,countryId);

//            if(pDialog!=null)
            pDialog.show();


        } else {
            Log.d("PiyushKatyal1", "Pop 3");
            populateStateList(countryId, isBilling);
        }
    }


    private void hitServiceForCountry() {

        countryItems = LocaleManager.getInstance().getCountryList();
        if (countryItems.isEmpty()) {
            String urlCountry = WebApiManager.getInstance().getAllCountryURL(getActivity());
            Log.d(TAG,"@vnish- country service url : "+urlCountry);
            CountryService countryService = new CountryService();
            countryService.execute(urlCountry);

            pDialog.show();
        } else {

            populateCountryList(countryItems);
            setBillPrflDefaultValue();
        }
    }
    private void populateCountryList(ArrayList<MyCountryItem> countryItems) {

        ArrayList<MyCountryItem> myCountryItems = new ArrayList<>();
        myCountryItems.add(new MyCountryItem(select, "0"));
        myCountryItems.addAll(countryItems);
        setCountryAdapter(myCountryItems);
        AddressData address = getAddressData();
        if(address==null)
            setBillingCountry(LocaleManager.getInstance().getDefaultCountry());

    }


    private void setBillingCountry(String countryId) {
        Log.d(TAG, "setBillingCountry() called with: " + "countryId = [" + countryId + "]");
        int index = LocaleManager.getInstance().getCountryIndex(countryId);
        country_selected_position = index+1;
        hitServiceForState(countryId, isBilling);
    }


    private String getJSON() {
        HashMap<String, String> kepAdd = new HashMap<>();
        AddressData address = getAddressData();

        if (address != null) {
            if (isBilling==true) {
                kepAdd.put("billfname", address.getFirstName());
                kepAdd.put("billlname", address.getLastName());
                kepAdd.put("billstreet1", address.getStreet());
                kepAdd.put("billphone", address.getContactNumber());

                kepAdd.put("billcity", address.getCity());
                kepAdd.put("billpostcode", address.getZipCode());
                kepAdd.put("billstate", address.getState());
                kepAdd.put("billcountry", address.getCountryId());
            } else {
                kepAdd.put("shippfname", address.getFirstName());
                kepAdd.put("shipplname", address.getLastName());
                kepAdd.put("shippstreet1", address.getStreet());
                kepAdd.put("shippphone", address.getContactNumber());

                kepAdd.put("shippcity", address.getCity());
                kepAdd.put("shipppostcode", address.getZipCode());
                kepAdd.put("shippstate", address.getState());
                kepAdd.put("shippcountry", address.getCountryId());
            }
        }

        JSONObject jsonObj = new JSONObject(kepAdd);

        return jsonObj.toString();
    }

    private AddressData getAddressData() {
        AddressData address = UserManager.getInstance().getUser().getBillingAddress();
        if (isBilling==false) {
            address = UserManager.getInstance().getUser().getShippingAddress();
        }
        return address;
    }

    private void hitServiceUpDateProfile() {
        String profile_email = EncodeString.encodeStrBase64Bit(getProfile());
        String address = EncodeString.encodeStrBase64Bit(this.getJSON());
        String url = WebApiManager.getInstance().getUpdateProfileURL(getActivity());
        String customerId = UserManager.getInstance().getUser().getId();
        String finalUrl = String.format(url, customerId, address, "", profile_email, "billingaddress");
        if (isBilling==false) {
            finalUrl = String.format(url, customerId, "", address, profile_email, "shippingaddress");
        }

        Log.d(TAG, "hitServiceUpDateProfile() called with:finalUrl " + finalUrl + "");
        final ProgressDialog pDialog2 = new ProgressDialog(getActivity(), R.style.MyTheme);
        pDialog2.setCancelable(false);
        pDialog2.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog2.show();

        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(finalUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog2.cancel();
                callFragment(new WelcomeFragment2());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                pDialog2.cancel();
            }
        });
    }

    private String getProfile() {
        HashMap<String, String> kepAdd = new HashMap<>();
        String email = UserManager.getInstance().getUser().getUsername();

        kepAdd.put("email", email);

        JSONObject jsonObj = new JSONObject(kepAdd);

        return jsonObj.toString();
    }

    @Override
    public void onResume() {
        Activity act = getActivity();
        if(isAdded() && act!=null) {
            headerText = getActivity().getResources().getString(R.string.edit_address_header);
//            MainActivity.setHeaderText(headerText);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
        }
        super.onResume();
    }

    private class CountryService extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(final String... params) {
            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(params[0], new Response.Listener<String>() {
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
                        setBillPrflDefaultValue();
                        populateCountryList(countryItems);

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


            return null;
        }
    }

    private class StateService extends AsyncTask<String,String,String>
    {

        @Override
        protected String doInBackground(String... params) {
            final String countryId = params[1];
            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(params[0], new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pDialog.cancel();
                    try {
                        JSONObject strJSNobj = new JSONObject(response);
                        JSONArray jsonArray = strJSNobj.getJSONArray("mofluid_regions");
                        Log.d(TAG, "@vnish - StateService Response : "+response);
                        ArrayList<MyStateItem> stateItemArrayList = new ArrayList<>();
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsnObj = jsonArray.getJSONObject(i);
                                String region_name = jsnObj.getString("region_name");
                                String region_id = jsnObj.getString("region_id");
                                stateItemArrayList.add(new MyStateItem(region_id, region_name));
                            }

                            LocaleManager.getInstance().setStateList(countryId, stateItemArrayList);
                            Log.d("PiyushKatyal1", "Pop 1");
                            Log.d("PiyushKatyal1", "country is [" + countryId +" ]");
                            populateStateList(countryId, isBilling);
                        }
                    } catch (JSONException e) {
                        Log.d("PiyushK", "inside exception");
                        Log.d("PiyushKatyal1", "Pop 2");
                        e.printStackTrace();
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


            return null;
        }
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
