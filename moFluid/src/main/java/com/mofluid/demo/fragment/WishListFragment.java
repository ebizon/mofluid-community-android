package com.mofluid.magento2.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
//import com.ebizon.fluid.Utils.WishListManager;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.WishListItem;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.WishListAdapter;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class WishListFragment extends BaseFragment {


    private View rootView;
    private  Context context;

    public static ListView wishlist_listView;
    public static WishListAdapter adapter;
    private ProgressDialog pDialog;
    private ArrayList<WishListItem> myWishList;
    public static TextView noproductin_wishlist;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    public WishListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
           MainActivity.iswishlist=false;

            rootView = inflater.inflate(R.layout.fragment_wish_list, container, false);
            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
            initialize();
            getviewControlls(rootView);
            setFontStyle();
            //showDialog();
            setAdapter();
        return rootView;
    }

    private void showDialog() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pDialog.dismiss();
            }
        }, 2000);
    }

    private void initialize() {
        context = getActivity();
    }


    private void setAdapter() {
        myWishList = null;//WishListManager.getInstance().getuserWishList();
        if(myWishList.size()==0)
        {
            //WishListManager.getInstance().hitServiceforGettingWishList(getActivity());
        }
        if(myWishList.size()==0)
        {
            noproductin_wishlist.setVisibility(View.VISIBLE);
            wishlist_listView.setVisibility(View.GONE);
        }
        else {
            noproductin_wishlist.setVisibility(View.GONE);
            wishlist_listView.setVisibility(View.VISIBLE);
        }
        adapter=new WishListAdapter(myWishList,getActivity());
        final ArrayList<WishListItem> myWishList = null;//WishListManager.getInstance().getuserWishList();
        wishlist_listView.setAdapter(adapter);
        wishlist_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaseFragment frmnt = new SimpleProductFragment2();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantDataMember.PRO_ID, myWishList.get(position).getId());
                bundle.putString(ConstantDataMember.PRO_NAME,myWishList.get(position).getName());
                frmnt.setArguments(bundle);
                callFragment(frmnt, frmnt.getClass().getSimpleName());
            }
        });
    }


    private void setFontStyle() {
    }

    private void getviewControlls(View rootView) {
        wishlist_listView=(ListView)rootView.findViewById(R.id.wish_list);
        noproductin_wishlist=(TextView)rootView.findViewById(R.id.no_wishlist);
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if(isAdded()&&activity!=null)
//        MainActivity.setHeaderText(getResources().getString(R.string.wishlist_header));
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,getResources().getString(R.string.wishlist_header));
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