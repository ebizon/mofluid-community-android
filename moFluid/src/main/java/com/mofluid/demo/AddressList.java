package com.mofluid.magento2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.ebizon.fluid.Utils.AddressManager;
import com.ebizon.fluid.model.Address;
import com.mofluid.magento2.adapter.AddressListAdapter;
import com.mofluid.magento2.fragment.BaseFragment;
import com.mofluid.magento2.fragment.HomeFragment;
import com.mofluid.magento2.fragment.ShippingMethodSameAddressFragment;

import java.util.List;

public class AddressList extends BaseFragment {
private ListView list;
private AddressListAdapter adapter;
public final static int BILLING_ADDRESS=0;
public final static int SHIPPING_ADDRESS=1;
private int billingOrShipping;
public void setBillingOrShipping(int billingOrShipping){
    this.billingOrShipping=billingOrShipping;
}
public int getBillingOrShipping(){
    return this.billingOrShipping;
}
public void callFragment(){
    FragmentManager fm = getFragmentManager();
    FragmentTransaction fragmentTransaction = fm.beginTransaction();
    FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(getActivity(), fragmentTransaction, new HomeFragment(), new ShippingMethodSameAddressFragment(), R.id.content_frame);
    fragmentTransactionExtended.addTransition(7);
    fragmentTransaction.remove(this);
    fragmentTransactionExtended.commit();
}
public AddressList(){}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.address_list_view,null);
        list=(ListView)rootView.findViewById(R.id.address_list);
        List<Address> addresses= AddressManager.getInstance().getAddressList(580,null);
        adapter=new AddressListAdapter(getActivity(),addresses,this);
        list.setAdapter(adapter);
        return rootView;
    }

}
