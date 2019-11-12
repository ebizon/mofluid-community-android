package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ebizon.fluid.Utils.Config;
import com.ebizon.fluid.model.MultiCurrencyModel;
import com.mofluid.magento2.R;

import java.util.ArrayList;


/**
 * Created by avnish on 11/21/17.
 */

public class MultipleCurrencyAdapter extends BaseAdapter {


    private final Activity context;
    private final ArrayList<MultiCurrencyModel> currencyList;
    private final LayoutInflater inflater;
    private TextView currencyName;
//    private TextView img_currency_symbol;
    private LinearLayout layout_currency;
    private ImageView currency_selected;

    public MultipleCurrencyAdapter(ArrayList<MultiCurrencyModel> currencyList, Activity context) {
        this.context=context;
        this.currencyList= currencyList;
        inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return currencyList.size();
    }

    @Override
    public Object getItem(int position) {
        return currencyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView=inflater.inflate(R.layout.multi_currency_list_layout, null);
        initialise(rootView);
        setDataToViews(position);
        return rootView;
    }

    private void setDataToViews(int position) {

        currencyName.setText(currencyList.get(position).getCurrency_code());
        //img_currency_symbol.setText(currencyList.get(position).getCurrency_symbol());

        if(Config.getInstance().getCurrencyCode().equals(currencyList.get(position).getCurrency_code()))
            currency_selected.setVisibility(View.VISIBLE);
        else
            currency_selected.setVisibility(View.INVISIBLE);
    }

    private void initialise(View rootView) {

        currencyName= (TextView) rootView.findViewById(R.id.txtV_multistore_list_item);
        //img_currency_symbol = (TextView) rootView.findViewById(R.id.img_currency_symbol);
        currency_selected= (ImageView)rootView.findViewById(R.id.imgV_selected_store);
        layout_currency = (LinearLayout) rootView.findViewById(R.id.ll_row_multistore_item);

    }
}
