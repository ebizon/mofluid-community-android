package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.model.ShippingMethodItem;
import com.mofluid.magento2.R;

import java.util.ArrayList;

/**
 * Created by ebizon on 22/12/15.
 */
public class myShippingMethodAdapter extends BaseAdapter {
    private final ArrayList<ShippingMethodItem> listOfMethod;
    private final LayoutInflater inflater;
    public myShippingMethodAdapter(Activity activity, ArrayList<ShippingMethodItem> listOfMethod) {
        this.listOfMethod=listOfMethod;
        inflater= activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return listOfMethod.size();
    }

    @Override
    public Object getItem(int i) {
        return listOfMethod.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null)
        {
            view=inflater.inflate(R.layout.row_my_shipp_method_adapter,null);
        }
        TextView txtV_method_title=(TextView) view.findViewById(R.id.txtV_method_title);
        String textToDisplay = listOfMethod.get(i).getCarrier_title();
        if(i!=0)
        {
            String price = Utils.appendWithCurrencySymbol(listOfMethod.get(i).getPrice());
            textToDisplay += " (<b>" + price + " </b>)";
        }
        txtV_method_title.setText(Html.fromHtml(textToDisplay));


        return view;
    }
}
