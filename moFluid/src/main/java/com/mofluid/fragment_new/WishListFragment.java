package com.mofluid.fragment_new;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.mofluid.magento2.fragment.SimpleProductFragment2;
import com.mofluid.magento2.fragment.BaseFragment;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.mofluid.utility_new.WishListManager;
import com.ebizon.fluid.model.WishListItem;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.utility_new.Callback;
import java.util.ArrayList;

public class WishListFragment extends BaseFragment {
    private View rootView;
    private ListView wishlist_listView;
    private com.mofluid.magento2.adapter.WishListAdapter adapter;
    private WishListManager wmanager;
    private TextView empty_message;
    private ProgressDialog pDialog;
    private ArrayList<WishListItem> wlist;
    public WishListFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_wish_list, container, false);
        wmanager=WishListManager.getInstance();
        MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
        wishlist_listView=(ListView)rootView.findViewById(R.id.wish_list);
        empty_message=(TextView)rootView.findViewById(R.id.no_wishlist);
        pDialog=new ProgressDialog(getActivity(), R.style.MyTheme);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        setItems();
        wishlist_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BaseFragment frmnt = new SimpleProductFragment2();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantDataMember.PRO_ID, wlist.get(i).getId());
                bundle.putString(ConstantDataMember.PRO_NAME,wlist.get(i).getName());
                frmnt.setArguments(bundle);
                callFragment(frmnt);

            }
        });
        return rootView;
    }
    private void errorMessage(){
        Toast.makeText(getActivity(),"Some error occured.",Toast.LENGTH_SHORT).show();
    }
    private void setItems(){
        pDialog.show();
        wmanager.getWishList(new Callback() {
            @Override
            public void callback(Object o, int response_code) {
                pDialog.hide();
                if(o==null){
                    errorMessage();
                    return;
                }
                wlist=(ArrayList<WishListItem>)o;
                if(wlist.size()<=0){
                    empty_message.setVisibility(View.VISIBLE);
                    return;
                }
                adapter= new com.mofluid.magento2.adapter.WishListAdapter(wlist,getActivity());
                wishlist_listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }
    @Override
public void onResume() {
        super.onResume();
    }

    @Override
public void onDetach() {
        super.onDetach();
    }
}
