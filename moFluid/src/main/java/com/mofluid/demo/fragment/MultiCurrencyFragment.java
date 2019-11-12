package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.ebizon.fluid.Utils.Config;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.model.MultiCurrencyModel;
import com.ebizon.fluid.model.MultiStoreView;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.MultipleCurrencyAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;


/**
 * Created by avnish on 11/21/17.
 */

public class MultiCurrencyFragment extends BaseFragment {

    private ArrayList<MultiCurrencyModel> currencyList = new ArrayList<>();
    private View rootView;
    private Context context;
    private ProgressDialog pDialog;
    String headerText;
    private RequestQueue requestQueue;
    private ArrayList<MultiStoreView> view_list;
    private ListView currency_list;
    private TextView empty_views;
    private BaseFragment fragment;
    public static final String STORE_ID ="StoreID";
    public static final String MULTI_STORE = "MultiStore";
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;


    public MultiCurrencyFragment()
    {

    }
    @SuppressLint("ValidFragment")
    public MultiCurrencyFragment(ArrayList<MultiCurrencyModel> currencyList)
    {
        this.currencyList= currencyList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_multi_currency, container, false);
            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
            context= getActivity();
            pDialog= new ProgressDialog(context, R.style.MyTheme);
            initialize(rootView);
            populateCurrencyList();

        }
        return rootView;
    }


    private void populateCurrencyList() {

        if(currencyList.isEmpty()) {
            empty_views.setVisibility(View.VISIBLE);
            currency_list.setVisibility(View.GONE);
        }
        else {
            empty_views.setVisibility(View.GONE);
            currency_list.setVisibility(View.VISIBLE);
            MultipleCurrencyAdapter multipleCurrencyAdapter = new MultipleCurrencyAdapter(currencyList, getActivity());
            currency_list.setAdapter(multipleCurrencyAdapter);
            currency_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   // TODO
                    MultiCurrencyModel selectedCurrency = currencyList.get(position);
                    Intent mStartActivity = new Intent(MultiCurrencyFragment.this.getActivity(), MainActivity.class);
                    mStartActivity.putExtra("StoreID", Config.getInstance().getStoreValue());
                    mStartActivity.putExtra("Currency", selectedCurrency.getCurrency_code());
                    mStartActivity.putExtra("Language", Config.getInstance().getLanguage());
                    mStartActivity.putExtra("CurrSymbol", selectedCurrency.getCurrency_symbol());
                    mStartActivity.putExtra("isFromcurrency", true);
                    startActivity(mStartActivity);
                    getActivity().finish();

//                    Config.getInstance().setCurrencyCode(selectedCurrency.getCurrency_code());
//                    if(selectedCurrency.getCurrency_symbol() ==null || selectedCurrency.getCurrency_symbol().equalsIgnoreCase("null"))
//                    {
//                        Config.getInstance().setCurrency_symbol(selectedCurrency.getCurrency_code());
//                    }
//                    else
//                        Config.getInstance().setCurrency_symbol(selectedCurrency.getCurrency_symbol());
//
//                    callFragment(new HomeFragment(),HomeFragment.class.getSimpleName());
                }
            });
        }
    }

    private void initialize(View rootView)
    {
        Activity activity = getActivity();
        if(isAdded()&&activity!=null)
            headerText= getActivity().getResources().getString(R.string.choose_currency);
        currency_list = (ListView) rootView.findViewById(R.id.multi_currency_list);
        empty_views = (TextView)rootView.findViewById(R.id.empty_currency);
    }

    public void onResume() {
//        MainActivity.setHeaderText(headerText);
        mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
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
