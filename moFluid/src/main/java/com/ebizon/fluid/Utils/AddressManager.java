package com.ebizon.fluid.Utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ebizon.fluid.model.Address;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.demo.CallBack;
import com.mofluid.magento2.service.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by saddam on 19/3/18.
 */

public class AddressManager {
    private static AddressManager instance;
    private Map<Integer,Address> m_map;
    private final String TAG="AddressManager";
    private boolean is_address_saved;
    private Address  guest_address;
    private AddressManager(){
        m_map=new HashMap<>();
        is_address_saved=false;
    }
    public boolean isAddress_saved(){
       return is_address_saved;
    }
    public static AddressManager getInstance(){
        if(instance==null)
            instance=new AddressManager();
        return instance;
    }

    public Address getGuest_address() {
        return guest_address;
    }

    public void setGuest_address(Address guest_address) {
        this.guest_address = guest_address;
    }

    public Address getAddress(int id){
        MySharedPreferences ob=MySharedPreferences.getInstance();
       int customer_id=Integer.parseInt(ob.get(ob.CUSTOMER_ID));
       return this.getAddress(customer_id,id);

    }
    public Address getAddress(int customerid,int id){
        if(m_map.get(id)!=null)
            return m_map.get(id);
        this.getAddressList(customerid,null);
        return m_map.get(id);
    }
    public List<Address> getAddressList(int customerid, CallBack callBack){
        List<Address> addresses=new ArrayList<>();
        String url1= WebApiManager.getInstance().getAddressListURL();
        url1=String.format(url1,customerid);
        //if(MySharedPreferences.getInstance().get(url1)!=null)
           // createAddresses(MySharedPreferences.getInstance().get(url1));
        final String url=url1;
        AddressManagerTask ob=new AddressManagerTask();
        ob.setCallBack(callBack);
        ob.execute(url);
        for (Map.Entry<Integer, Address> entry : m_map.entrySet()) {
            addresses.add(entry.getValue());
        }
return addresses;
    }
    public void insertAddress(Address address){
        MySharedPreferences pref=MySharedPreferences.getInstance();
        if(pref.get(pref.CUSTOMER_ID)==null)
            return;
        int customer_id=Integer.parseInt(pref.get(pref.CUSTOMER_ID));
        String url=WebApiManager.getInstance().getAddAddressURL();
        url=String.format(url,customer_id,address.getAddressEncodeString());
        new AddressManagerTask().execute(url);
    }
    public void updateAddress(final Address address){
        MySharedPreferences pref=MySharedPreferences.getInstance();
        if(pref.get(pref.CUSTOMER_ID)==null)
            return;
        int customer_id=Integer.parseInt(pref.get(pref.CUSTOMER_ID));
   String url=WebApiManager.getInstance().getUpdateAddressURL();
   url=String.format(url,customer_id,address.getAddress_id(),address.getAddressEncodeString());
   new AddressManagerTask().execute(url);
   Handler handler=new Handler();
   handler.postDelayed(new Runnable() {
       @Override
       public void run() {
           getAddressList(address.getCustomer_id(),null);
       }
   },1000);
    }
    private void createAddresses(String response){
        Log.d(TAG,"Response to create address="+response);
        MySharedPreferences pref=MySharedPreferences.getInstance();
        try {
            JSONArray jsonArray = new JSONArray(response);
            int n=jsonArray.length();
            for(int i=0;i<n;i++){
                JSONObject object=jsonArray.getJSONObject(i);
                int id=Integer.parseInt(object.getString("id"));
                String firstname=object.getString("firstname");
                String lastname=object.getString("lastname");
                String contactno=object.getString("telephone");
                String fstreet="";
                JSONArray street=object.getJSONArray("street");
                for(int j=0;j<street.length();j++){
                    fstreet+=street.get(j)+" ";
                }
                String city=object.getString("city");
                String country_code=object.getString("country_id");
                String country=country_code;//=object.getString("country");
                String region_id=object.getString("region_id");
                JSONObject regions=object.getJSONObject("region");
                String region=regions.getString("region");
                String pincode=object.getString("postcode");
                Address address=new Address(id,firstname,lastname,contactno,fstreet,city,country_code,country,region_id,region,pincode);
                Log.d(TAG,"address="+address.getCompleteAddress());
                m_map.put(id,address);
                if(pref.get(pref.BILLING_ADDRESS_ID)==null)
                    pref.set(pref.BILLING_ADDRESS_ID,id+"");
                if(pref.get(pref.SHIPPING_ADDRESS_ID)==null)
                    pref.set(pref.SHIPPING_ADDRESS_ID,id+"");

            }
        }catch (JSONException e){
            Log.d(TAG,e.toString());
        }
        this.is_address_saved=true;
    }
private class AddressManagerTask extends AsyncTask<String,String,String>{
private CallBack callBack;
public void setCallBack(CallBack callBack){this.callBack=callBack;}
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(final String... strings) {
        NetworkAPIManager.getInstance(AppController.getContext()).sendGetRequest(strings[0],
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"Url="+strings[0]);
                        Log.d(TAG,"response="+response);
                      if(strings[0].contains("address_list")) {
                          createAddresses(response);
                          if(callBack!=null)
                              callBack.callback(null);
                         // MySharedPreferences.getInstance().set(strings[0],response);
                      }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }
}
}
