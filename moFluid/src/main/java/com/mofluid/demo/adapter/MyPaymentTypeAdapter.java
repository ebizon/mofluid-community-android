package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.ebizon.fluid.model.PaymentMethod;
import com.mofluid.magento2.R;

import java.util.ArrayList;

/**
 * Created by ebizon on 22/12/15.
 */
public class MyPaymentTypeAdapter extends BaseAdapter {

    private final ArrayList<PaymentMethod> paymentTypeList;
    private LayoutInflater inflater;
    public MyPaymentTypeAdapter(Activity activity, ArrayList<PaymentMethod> paymentTypeList) {
        this.paymentTypeList=paymentTypeList;
        if(activity!=null)
        inflater= activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return paymentTypeList.size();
    }

    @Override
    public Object getItem(int i) {
        return paymentTypeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view=inflater.inflate(R.layout.row_my_shipp_method_adapter,null);
        TextView txt=(TextView) view.findViewById(R.id.txtV_method_title);
        txt.setText(paymentTypeList.get(i).getTitle()+"");

        return view;
    }
}
