package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.model.SlideMenuListItem;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.AllProductListAdapter;
import com.mofluid.magento2.custome.widget.CustomePullListview;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ebizon on 9/11/15.
 */
public class AllProductFragment extends BaseFragment {
    private View rootView;
    private CustomePullListview listV_all_products;
    private List<SlideMenuListItem> childListData = new ArrayList<SlideMenuListItem>();
    String headerText;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;


    public AllProductFragment()
    {
    }

    @SuppressLint("ValidFragment")
    public AllProductFragment(String name) {
        headerText=name;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_all_product_list, null);

            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
            getControllView(rootView);
            setDataToAdapter();
        }

        listV_all_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String categoryId;

                if (position == 0) {
                     /*
                        zero position's  child is hardcoded and it has no any id
                 */
                    categoryId = "0";
                } else
                    categoryId = String.valueOf(childListData.get(position).getId());
                String name = String.valueOf(childListData.get(position).getName());

                ProductDetailListFragment fragment = new ProductDetailListFragment(name);
                Bundle subCategoryBundle = new Bundle();
                subCategoryBundle.putString(MainActivity.KEY_CATEGORY_ID, categoryId);
                fragment.setArguments(subCategoryBundle);
                callFragment(fragment);
            }
        });

        return  rootView;
    }

    private void setDataToAdapter() {
        /*
         Get Child List From Mainactivity

         */
        childListData= MainActivity.childListForAllProductListFragment;

        AllProductListAdapter allProductListAdapter=new AllProductListAdapter(getActivity(),childListData);
        listV_all_products.setAdapter(allProductListAdapter);

    }

    private void getControllView(View rootView) {
        listV_all_products=(CustomePullListview)  rootView.findViewById(R.id.listV_all_products);
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
