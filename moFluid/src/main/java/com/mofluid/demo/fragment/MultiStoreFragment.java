package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.model.MultiStoreData;
import com.ebizon.fluid.model.MultiStoreView;
import com.ebizon.fluid.model.SlideMenuListItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.MultiStoreAdapter;
import com.mofluid.magento2.adapter.MultiStoreViewAdapter;
import com.mofluid.magento2.adapter.SlideMenuAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by piyush on 4/5/16.
 */
public class
MultiStoreFragment extends BaseFragment {

    private ArrayList<MultiStoreData> storelist = new ArrayList<>();
    private View rootView;
    private Context context;
    private ProgressDialog pDialog;
    String headerText;
    private RequestQueue requestQueue;
    private ArrayList<MultiStoreView> view_list;
    private ListView store_list;
    private TextView empty_views;
    private BaseFragment fragment;
    public static final String STORE_ID ="StoreID";
    public static final String MULTI_STORE = "MultiStore";
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    public MultiStoreFragment()
    {

    }
    @SuppressLint("ValidFragment")
    public MultiStoreFragment(ArrayList<MultiStoreData> storeList)
    {
        this.storelist= storeList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_multistore_options, container, false);
            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
            context= getActivity();
            pDialog= new ProgressDialog(context, R.style.MyTheme);
            initialize(rootView);
            populateStoreViewList();

        }
        return rootView;
    }


    private void populateStoreViewList() {
       if(storelist.isEmpty()) {
            empty_views.setVisibility(View.VISIBLE);
            store_list.setVisibility(View.GONE);
        }
        else {
            empty_views.setVisibility(View.GONE);
            store_list.setVisibility(View.VISIBLE);
            MultiStoreAdapter multistoreadapter = new MultiStoreAdapter(storelist, getActivity());
            store_list.setAdapter(multistoreadapter);
            store_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = storelist.get(position).getStore_Name();
                    fragment = new MultiStoreViewListFragment(name);
                    Bundle storeBundle = new Bundle();
                    String s = String.valueOf(position);
                    Log.d("PiyushK", "value of clicked being sent is : "+ s);
                    storeBundle.putString(STORE_ID,s );
                    storeBundle.putParcelableArrayList(MULTI_STORE, storelist);
                    fragment.setArguments(storeBundle);
                    callFragment(fragment, "MultiStoreViewListFragment");
                }
            });
        }
    }

    private void initialize(View rootView)
    {
        Activity activity = getActivity();
        if(isAdded()&&activity!=null)
        headerText= getActivity().getResources().getString(R.string.choose_store);
        store_list= (ListView) rootView.findViewById(R.id.multi_store_list_options);
        empty_views = (TextView)rootView.findViewById(R.id.empty_store_view_options);
    }

    public void onResume() {
        //MainActivity.setHeaderText(headerText);
        mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
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
