package com.mofluid.magento2.fragment;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.model.AdditionalInfoItem;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.AdditionalInfoAdapter;
import com.mofluid.magento2.custome.widget.CustomePullListview;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by ebizon on 20/11/15.
 */
public class MyAditionalFragmenrt extends BaseFragment {
    private CustomePullListview listV_additional_info;
    private static ArrayList<AdditionalInfoItem> additionalInfoList;
    public static String PRODUCT_NAME;
    public static String PRODUCT_SHORT_DESC;
    public static String PRODUCT_PRICE;
    private TextView txtV_pro_price;
    private TextView txtV_pro_additional_info_value;
    private TextView txtV_pro_name;
    private LinearLayout ll_show_;
    Typeface open_sans_regular, open_sans_semibold;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_additional, null);
        MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
        MainActivity.ivAppLogo.setVisibility(View.INVISIBLE);

        gteValueFromArgument();
        initialized();

        getViewControlls(rootView);


        txtV_pro_additional_info_value.setText(Html.fromHtml(PRODUCT_SHORT_DESC));
        txtV_pro_name.setText(PRODUCT_NAME);

        txtV_pro_price.setText(PRODUCT_PRICE);

// if additionalInfoList is empety then show product name ,price and additonal information
        if(additionalInfoList.size()<1)
        {
            ll_show_.setVisibility(View.VISIBLE);
        }
        else {
            ll_show_.setVisibility(View.VISIBLE);
            AdditionalInfoAdapter additionalInfoAdapter = new AdditionalInfoAdapter(getActivity(), additionalInfoList);
            listV_additional_info.setAdapter(additionalInfoAdapter);
        }



        return rootView;
    }

    private void gteValueFromArgument() {
        try {
            PRODUCT_SHORT_DESC=getArguments().getString(ConstantDataMember.PRO_SHORT_DESCRI);
        }catch (Exception ex)
        {

        }
        try {
            PRODUCT_NAME=getArguments().getString(ConstantDataMember.PRO_NAME);
        }catch (Exception ex)
        {

        }
        try {
            PRODUCT_PRICE=getArguments().getString(ConstantDataMember.PRO_FINAL_PRICE);
        }catch (Exception ex)
        {

        }
    }


    private void initialized() {
        additionalInfoList=SimpleProductFragment2.additionalInfoList;
        open_sans_regular = Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
        open_sans_semibold = Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
    }

    private void getViewControlls(View rootView) {

        listV_additional_info=(CustomePullListview) rootView.findViewById(R.id.listV_additional_info);
        txtV_pro_price=(TextView) rootView.findViewById(R.id.txtV_pro_price);
        txtV_pro_additional_info_value=(TextView) rootView.findViewById(R.id.txtV_pro_additional_info_value);
        ll_show_=(LinearLayout) rootView.findViewById(R.id.ll_show_);
        txtV_pro_name=(TextView) rootView.findViewById(R.id.txtV_pro_name);
        txtV_pro_price.setTypeface(open_sans_semibold);
        txtV_pro_additional_info_value.setTypeface(open_sans_regular);
        txtV_pro_name.setTypeface(open_sans_regular);

    }

    @Override
    public void onDetach() {
        super.onDetach();
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
}
