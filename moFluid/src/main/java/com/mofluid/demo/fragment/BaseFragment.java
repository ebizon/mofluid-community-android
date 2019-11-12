package com.mofluid.magento2.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.ebizon.fluid.model.LocaleManager;
import com.ebizon.fluid.model.MyCountryItem;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by manish on 18/01/16.
 */
public class BaseFragment extends Fragment {
    protected String TAG;
    protected void callFragment(BaseFragment fragment) {
        this.callFragment(fragment, null);
    }

    void callFragment(BaseFragment fragment, String fragmentName) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(getActivity(), fragmentTransaction, new HomeFragment(), fragment, R.id.content_frame);
        /*
        index 7 is used to slide fragment Left to right and vise versa
         */
        fragmentTransactionExtended.addTransition(7);
//        if(fragmentName != null) {
//            fragmentTransaction.addToBackStack(fragmentName);
//        }
        fragmentTransactionExtended.commit();
    }


    void callFragmentPop(BaseFragment fragment, String fragmentName) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(getActivity(), fragmentTransaction, new HomeFragment(), fragment, R.id.content_frame);
        /*
        index 7 is used to slide fragment Left to right and vise versa
         */
        fragmentTransactionExtended.addTransition(7);
        fm.popBackStackImmediate();
//        if(fragmentName != null) {
//            fragmentTransaction.addToBackStack(fragmentName);
//        }
        fragmentTransactionExtended.commit();
    }

    protected void showGenericError(){
        Toast.makeText(getActivity(), "Something went wrong, please try later", Toast.LENGTH_SHORT).show();
    }

    protected void setCountryName(final TextView textView, final String countryId) {
        MyCountryItem countryItem = LocaleManager.getInstance().getCountry(countryId);
        if (countryItem == null) {
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
                        LocaleManager.createCountry(strJSNobj);

                        MyCountryItem country = LocaleManager.getInstance().getCountry(countryId);

                        if(country != null) {
                            textView.setText(country.getCountry_name());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                    pDialog.cancel();
                }
            });



        }else{
            textView.setText(countryItem.getCountry_name());
        }
    }
}
