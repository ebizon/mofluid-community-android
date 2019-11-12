package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.OrderedProduct;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserOrders;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by ebizon on 16/12/15.
 */
@TargetApi(Build.VERSION_CODES.M)
@SuppressLint("ValidFragment")
public class MyDownloadsFragment extends BaseFragment {

    private String headerText="MY ORDERS";
    private TextView txt_no_order;
    private ListView order_list;
    private View rootView;
    private String mydownloads_URL;
    private Context context;
    private String customerId;
    ArrayList<DownloadProducts> listDownloadProducts;
    private ProgressDialog progressDialog;
    ListView mydownloadlist;
    private TextView no_order;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    public MyDownloadsFragment(ArrayList<DownloadProducts> listDownloadProducts) {
        this.listDownloadProducts=listDownloadProducts;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragmenty_my_downloads, null);
            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
            context = getActivity();
            initialise(rootView);
            if(listDownloadProducts.size()>=0)
            {
                setAdapterDownloadList();
            }else {
                noOrderPlace();
            }
        }
        return rootView;
    }
    private void noOrderPlace() {

        no_order.setVisibility(View.VISIBLE);
    }

    private void setAdapterDownloadList() {
        mydownloadlist.setAdapter(new DownloadlistAdapter(getActivity(),listDownloadProducts));
    }

    private void initialise(View rootView){
        mydownloadlist=(ListView)rootView.findViewById(R.id.my_downloads_list);
        no_order=(TextView)rootView.findViewById(R.id.no_downloads);
    }
    @Override
    public void onResume() {
        Activity activity = getActivity();
        if(isAdded()&&activity!=null) {
            headerText = getActivity().getResources().getString(R.string.my_downloads_header);
//            MainActivity.setHeaderText(headerText);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
        }
        super.onResume();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        try {
            Field childFragmentManager = android.app.Fragment.class.getDeclaredField("mChildFragmentManager");
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
