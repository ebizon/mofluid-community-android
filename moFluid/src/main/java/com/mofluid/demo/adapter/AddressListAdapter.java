package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ebizon.fluid.Utils.MySharedPreferences;
import com.ebizon.fluid.model.Address;
import com.mofluid.magento2.AddressList;
import com.mofluid.magento2.R;
import com.mofluid.magento2.fragment.ShippingMethodSameAddressFragment;

import java.util.List;

/**
 * Created by saddam on 21/3/18.
 */

public class AddressListAdapter extends BaseAdapter{
private final LayoutInflater inflater;
private Activity activity;
private List<Address> addresses;
private AddressList ob;
    public AddressListAdapter(Activity activity, List<Address> addresses,AddressList ob){

        this.activity=activity;
        this.addresses=addresses;
        this.ob=ob;
        this.inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        if(this.addresses==null)
            return 0;
        return this.addresses.size();
    }

    @Override
    public Object getItem(int position) {
        if(this.addresses==null || this.addresses.size()<=position)
        return null;
        return this.addresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      AddressListViewHolder viewHolder;
      if(convertView==null){
          viewHolder=new AddressListViewHolder();
          convertView=inflater.inflate(R.layout.show_billing_adress_details,null);
          viewHolder.name=(TextView) convertView.findViewById(R.id.name);
          viewHolder.address=(TextView) convertView.findViewById(R.id.address);
         // viewHolder.add=(Button) convertView.findViewById(R.id.add_new);
          viewHolder.edit=(Button)convertView.findViewById(R.id.edit);
          convertView.setTag(viewHolder);

      }
      else
          viewHolder=(AddressListViewHolder)convertView.getTag();
      Address cur_address=addresses.get(position);
      viewHolder.name.setText(cur_address.getFname()+" "+cur_address.getLname());
      viewHolder.address.setText(cur_address.getCompleteAddress());
      viewHolder.add.setText("Select");
      viewHolder.edit.setText("Edit");
      viewHolder.add.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(ob.getBillingOrShipping()==AddressList.BILLING_ADDRESS)
                  MySharedPreferences.getInstance().set(MySharedPreferences.BILLING_ADDRESS_ID,addresses.get(position).getAddress_id()+"");
              else
                  MySharedPreferences.getInstance().set(MySharedPreferences.SHIPPING_ADDRESS_ID,addresses.get(position).getAddress_id()+"");
              ob.callFragment();

          }
      });

      viewHolder.edit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
      });
        return convertView;
    }

}
class AddressListViewHolder{
TextView name,address;
Button add,edit;
}