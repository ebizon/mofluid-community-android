package com.mofluid.magento2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.mofluid.adapter.SearchAdapter;
import com.mofluid.magento2.fragment.GroupedProductDetailFragment;
import com.mofluid.magento2.fragment.SimpleProductFragment2;
import com.mofluid.model_new.Product;
import com.mofluid.utility_new.Callback;
import com.mofluid.utility_new.ProductManager;

import java.util.List;

public class SearchActivity extends com.mofluid.magento2.fragment.BaseFragment{

    private AutoCompleteTextView searchView;
    private TextView txtV_item_counter;
    private SearchAdapter  searchAdapter;
    private ListView list;
    private boolean search_service_status=false;
    private ImageView cross;
    private List<Product> p_list;
    private View rootView;
    private OnFragmentInteractionListener mListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.activity_search, null, false);
            searchView =rootView.findViewById(R.id.search_view);
            list=rootView.findViewById(R.id.s_list);
            cross=rootView.findViewById(R.id.cross);
            cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchView.setText("");
                    searchView.setHint("search product");
                    list.setAdapter(null);
                }
            });
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Product cur_product=p_list.get(i);
                    com.mofluid.magento2.fragment.BaseFragment fragment;
                    if (cur_product.getType_id().equals("simple"))
                        fragment = new SimpleProductFragment2();
                    else if (cur_product.getType_id().equalsIgnoreCase("grouped"))
                        fragment = new GroupedProductDetailFragment();
                    else
                        fragment = new SimpleProductFragment2();

                    Bundle bundle = new Bundle();
                    bundle.putString(ConstantDataMember.PRO_ID, cur_product.getId()+"");
                    bundle.putString(ConstantDataMember.PRO_NAME, cur_product.getName());
                    fragment.setArguments(bundle);
                    try {
                        callFragment(fragment);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            });
            searchView.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    //if(search_service_status)
                    //  return;
                    showSuggestion(charSequence.toString());
                }

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            String str = getActivity().getResources().getString(R.string.search);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,str.toUpperCase());
            //MainActivity.setHeaderText();
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

    private void showSuggestion(String text){
        search_service_status=true;
        text=com.mofluid.magento2.EncodeString.encodeStrBase64Bit(text);
    ProductManager.getInstance().getSearchedProductList(text, "name", "ASC", 0, 200, new Callback() {
        @Override
        public void callback(Object o, int response_code) {
                            if(o==null){
                                return;
                            }
            List<Product> products=(List<Product>)o;
            p_list=products;
            searchAdapter=new SearchAdapter(getActivity(),products);
            searchAdapter.setProducts(products);
           list.setAdapter(searchAdapter);
            search_service_status=false;
        }
    });
}


}
