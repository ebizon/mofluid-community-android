package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.content.Intent;;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.model.MultiStoreData;
import com.ebizon.fluid.model.MultiStoreView;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.MultiStoreViewAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by piyush on 21/4/16.
 */
public class MultiStoreViewListFragment extends BaseFragment {

    ArrayList<MultiStoreView> multiStoreViewList = new ArrayList<>();
    private View rootView;
    private Context context;
    private ListView store_list;
    private TextView empty_views;
    private String headerText;
    private String store_ID;
    private ArrayList<MultiStoreData> storelist ;
    ArrayList<MultiStoreView> storeview_list;
    ProgressDialog pDialog;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    public MultiStoreViewListFragment()
    {

    }
    @SuppressLint("ValidFragment")
    public MultiStoreViewListFragment(String name)
    {
        this.headerText= name;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_multistore, container, false);
            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
            context= getActivity();
            pDialog= new ProgressDialog(context, R.style.MyTheme);
            initialize(rootView);
            getIntentData();
            populateStoreViewList();
        }

        return rootView;
    }

    private void getIntentData() {

        storelist=getArguments().getParcelableArrayList(MainActivity.MULTI_STORE);
        store_ID=getArguments().getString(MainActivity.STORE_ID);
    }

    private void populateStoreViewList() {
        int selected_pos = Integer.parseInt(store_ID);
        storeview_list = storelist.get(selected_pos).getViews_Store();
        if(storeview_list.isEmpty()) {
            empty_views.setVisibility(View.VISIBLE);
            store_list.setVisibility(View.GONE);
        }
        else {
            empty_views.setVisibility(View.GONE);
            store_list.setVisibility(View.VISIBLE);
            MultiStoreViewAdapter multistoreadapter = new MultiStoreViewAdapter(storeview_list, getActivity());
            store_list.setAdapter(multistoreadapter);
            store_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String ID = storeview_list.get(position).getId();
                    String name = storeview_list.get(position).getName();
                    String currency = storeview_list.get(position).getCurrency();
                    String language = storeview_list.get(position).getLanguage_code();
                    Toast.makeText(context, "Selected Store View : " + name, Toast.LENGTH_SHORT).show();
                    restartApp(ID, name, currency, language);
                }
            });
        }
    }

    private void restartApp(String ID, String name, String currency, String language) {

        Intent mStartActivity = new Intent(this.getActivity(), MainActivity.class);
        mStartActivity.putExtra("StoreID", ID);
        mStartActivity.putExtra("Currency", currency);
        mStartActivity.putExtra("Language", language);
        startActivity(mStartActivity);
        getActivity().finish();
    }

    private void initialize(View rootView) {
        multiStoreViewList = new ArrayList<>();
        store_list= (ListView) rootView.findViewById(R.id.multi_store_list);
        storelist = new ArrayList<>();
        empty_views = (TextView)rootView.findViewById(R.id.empty_store_view);

    }

    @Override
    public void onResume() {
//        MainActivity.setHeaderText(headerText);
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
