package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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
public class MyOrderFragment extends BaseFragment {
    private String headerText="MY ORDERS";
    private TextView txt_no_order;
    private ListView order_list;
    private View rootView;
    private String myorder_URL;
    private RequestQueue requestQueue;
    OredrlistAdapter orderlistAdapter;
    private Context context;
    private String customerId;
    private ArrayList<UserOrders> complete_order;
    private ArrayList<OrderedProduct> order_products;
    private ProgressDialog progressDialog;
    private TextView orderReview;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    public MyOrderFragment()
    {

    }

    public MyOrderFragment(ArrayList<UserOrders> complete_order) {
        this.complete_order= new ArrayList<>();
        this.complete_order=complete_order;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_my_order, null);
            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
            context = getActivity();
            initialise(rootView);
            if(complete_order.size()>=0)
            {
                setdatatoList();
            }
            else{
                showNoOrder();
            }
        }
        return rootView;
    }
    private void setdatatoList() {
        txt_no_order.setVisibility(View.GONE);
        order_list.setVisibility(View.VISIBLE);
        setListView_of_order();
    }

    private void showNoOrder() {
        txt_no_order.setVisibility(View.VISIBLE);
        order_list.setVisibility(View.GONE);
    }

    private void setListView_of_order() {
        Activity context=getActivity();
        orderlistAdapter=new OredrlistAdapter(complete_order,context);
        order_list.setAdapter(orderlistAdapter);

    }

    private void initialise(View rootView){
//        complete_order=new ArrayList<>();
        txt_no_order = (TextView) rootView.findViewById(R.id.txt_no_orders);
        order_list=(ListView)rootView.findViewById(R.id.order_list);
    }

    @Override
    public void onResume() {
        Activity activity = getActivity();
        if(isAdded()&&activity!=null) {
            headerText = getActivity().getResources().getString(R.string.my_order_header);
            //MainActivity.setHeaderText(headerText);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
        }
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
