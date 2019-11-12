package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ebizon.fluid.Utils.AddressManager;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.GuestStockCheck;
import com.ebizon.fluid.Utils.IResponseListener;
import com.ebizon.fluid.Utils.MySharedPreferences;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.model.Address;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.ShoppingCartItem;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserProfileItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.demo.CallBack;
import com.mofluid.magento2.AddressList;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.ShoppingCartAdapter;
import com.mofluid.magento2.database.MyDataBaseAdapter;
import com.mofluid.magento2.service.AppController;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ebizon on 6/11/15.
 */
public class MyCartFragment extends BaseFragment implements View.OnClickListener,GuestStockCheck {
    private static String TAG = "";
    private TextView tct_my_cart;
    private RelativeLayout rl_cart_item_available;
    private RelativeLayout rl_no_item_in_my_cart;
    private ListView listV_my_cart;
    private ShoppingCartAdapter adapter;
    private TextView tct_continue_shoping;
    private int counter = 0;
    private ArrayList<ShoppingCartItem> cartItemList;
    private TextView txtV_total_price;
    private Typeface lat0_Font_ttf, tf;
    private TextView txtV_total;
    private SharedPreferences mySharedPreference;
    private TextView txtV_check_out;
    private int FLAG=0;
    private MyDataBaseAdapter dataBaseAdapter=new MyDataBaseAdapter(AppController.getContext());;

    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    @SuppressLint("ValidFragment")
    public MyCartFragment() {
        //Empty

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootvView = inflater.inflate(R.layout.fragment_my_cart, null);

        initialized();
        getViewControll(rootvView);
        setFontStyle();
        cartItemList = dataBaseAdapter.getCartItems();
        //UpdateCartFromServer();
        setTotalPrice();
        setCounterItemAddedCart();
        setDataToAdapter();
        MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
        return rootvView;
    }

    private void setCounterItemAddedCart() {
        if (cartItemList.size() > 0) {
            MainActivity.txtV_item_counter.setText(cartItemList.size() + "");
            MainActivity.txtV_item_counter.setVisibility(View.VISIBLE);
            rl_cart_item_available.setVisibility(View.VISIBLE);
            rl_no_item_in_my_cart.setVisibility(View.GONE);
        } else {
            MainActivity.txtV_item_counter.setVisibility(View.INVISIBLE);
            rl_no_item_in_my_cart.setVisibility(View.VISIBLE);
            rl_cart_item_available.setVisibility(View.GONE);
        }
    }

    private void setFontStyle() {
        txtV_total_price.setTypeface(lat0_Font_ttf);
        tct_continue_shoping.setTypeface(lat0_Font_ttf);
        txtV_check_out.setTypeface(lat0_Font_ttf);
        txtV_total.setTypeface(tf);
    }

    private void removeItemFromCart(ShoppingCartItem cartItem, int position) {
        ShoppingCart.getInstance().deleteItem(cartItem,this);
        setDataToAdapter();

        setTotalPrice();
    }

    private void setTotalPrice() {
        double totalPrice = ShoppingCart.getInstance().getSubTotal();
        txtV_total_price.setText(Utils.appendWithCurrencySymbol(totalPrice));
    }

    public void addItemValueToCart(ShoppingCartItem cartItem, int position) {
        int currentCount = cartItem.getCount();


        if (currentCount < cartItem.getShoppingItem().getStockQuantity()) {
            ++currentCount;
            cartItem = new ShoppingCartItem(cartItem.getShoppingItem(), currentCount);
            ShoppingCart.getInstance().addItem(cartItem, this);
            setDataToAdapter();
            adapter.notifyDataSetChanged();
            setTotalPrice();
        } else {
            Activity activity = getActivity();
            if(isAdded()&&activity!=null)
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.you_are_exceeded_number_products), Toast.LENGTH_SHORT).show();
        }
    }


    public void RefreshItemValueToCart(ShoppingCartItem cartItem, int position) {
        int currentCount = cartItem.getCount();


        if (currentCount == cartItem.getShoppingItem().getStockQuantity()) {
            cartItem = new ShoppingCartItem(cartItem.getShoppingItem(), currentCount);
            ShoppingCart.getInstance().addItem(cartItem, this);
            setDataToAdapter();
            adapter.notifyDataSetChanged();
            setTotalPrice();
        } else {
            Activity activity = getActivity();
            if(isAdded()&&activity!=null)
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.you_are_exceeded_number_products), Toast.LENGTH_SHORT).show();
        }
    }


    public void lessItemCountToCart(ShoppingCartItem cartItem, int position) {
        int currentCount = cartItem.getCount();

        if (currentCount > 1) {
            --currentCount;
            cartItem = new ShoppingCartItem(cartItem.getShoppingItem(), currentCount);
            ShoppingCart.getInstance().addItem(cartItem, this);
            setDataToAdapter();
            setTotalPrice();
        } else {
            Activity activity = getActivity();
            if(isAdded()&&activity!=null)
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.least_quantity_selected), Toast.LENGTH_SHORT).show();
        }
    }

    private View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private void setDataToAdapter() {
        cartItemList=dataBaseAdapter.getCartItems();
        adapter = new ShoppingCartAdapter(getActivity(), cartItemList, MyCartFragment.this);
        listV_my_cart.setAdapter(adapter);
    }

    private void getViewControll(View rootvView) {

        rl_cart_item_available = (RelativeLayout) rootvView.findViewById(R.id.rl_cart_item_available);
        rl_no_item_in_my_cart = (RelativeLayout) rootvView.findViewById(R.id.rl_no_item_in_my_cart);

        listV_my_cart = (ListView) rootvView.findViewById(R.id.listV_my_cart);

        tct_continue_shoping = (TextView) rootvView.findViewById(R.id.tct_continue_shoping);
        tct_continue_shoping.setOnClickListener(this);

        txtV_check_out = (TextView) rootvView.findViewById(R.id.txtV_check_out);
        txtV_check_out.setOnClickListener(this);

        txtV_total_price = (TextView) rootvView.findViewById(R.id.txtV_total_price);
        txtV_total = (TextView) rootvView.findViewById(R.id.txtV_total);

        txtV_total.setTypeface(tf);

        Button btn_shop_to_cotinue = (Button) rootvView.findViewById(R.id.btn_shop_to_cotinue);
        btn_shop_to_cotinue.setOnClickListener(this);
    }

    private void initialized() {
        TAG = getClass().getSimpleName();
        lat0_Font_ttf = Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
        mySharedPreference = getActivity().getSharedPreferences(ConstantDataMember.USER_INFO_SESSION, Context.MODE_PRIVATE);
        tf = Typeface.createFromAsset(getActivity().getAssets(),ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tct_my_cart:
                counter++;
                if (counter % 2 == 0) {
                    rl_no_item_in_my_cart.setVisibility(View.VISIBLE);
                    rl_cart_item_available.setVisibility(View.GONE);
                } else {
                    rl_cart_item_available.setVisibility(View.VISIBLE);
                    rl_no_item_in_my_cart.setVisibility(View.GONE);
                }
                break;
            case R.id.tct_continue_shoping:
            case R.id.btn_shop_to_cotinue:
                mListener.onFragmentMessage(ConstantDataMember.POP_ALL_FRAGMENT_FROM_STACK,null);
                //callFragment(new HomeFragment(), "HomeFragment");
                break;
            case R.id.txtV_check_out:
               final MySharedPreferences pref=MySharedPreferences.getInstance();
                if(pref.get(pref.CUSTOMER_ID)!=null) {
                    final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
                         pDialog.setCancelable(false);
                         pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                         pDialog.show();
                        AddressManager.getInstance().getAddressList(Integer.parseInt(pref.get(pref.CUSTOMER_ID)), new CallBack() {
                            @Override
                            public void callback(Object o) {
                                pDialog.dismiss();
                                if (pref.get(pref.SHIPPING_ADDRESS_ID) != null || pref.get(pref.BILLING_ADDRESS_ID) != null) {
                                    ShippingMethodSameAddressFragment ob = new ShippingMethodSameAddressFragment();
                                    callFragment(ob, "ShippingMethodSameAddressFragment");
                                }
                                else callFragment(new BillingAndShippingAddressFragment());
                            }
                        });

                }
                else{
                    callFragment(new MyCheckOutFragment(), "MyCheckOutFragment");
                }
               //checkForCartProductsQuantity();
                break;
        }
    }
    private boolean checkForCartProductsQuantity() {
        final String productsList = ShoppingCart.getInstance().getCartItemsId();
        final boolean[] result = new boolean[1];
        final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.show();
        // close progress dialogbox after 1 second
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //result[0] = CartSync.getInstance().getAnonymousCartQuantity(productsList,MyCartFragment.this);
                setCounterItemAddedCart();
                pDialog.dismiss();
            }
        }, 3000);

        return result[0];
    }

    private void checkforCartItemsStatus() {
        UpdateCartFromServer();

    }

    @Override
    public void onResume() {
        Activity activity = getActivity();
        if(isAdded()&&activity!=null) {
            String headerText = getActivity().getResources().getString(R.string.cart_header);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
        }
        super.onResume();
    }

    public  void deleteItemFromCartList(final int position) {
        if (cartItemList.size() > position) {
            final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.show();
            // close progress dialogbox after 1 second
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    removeItemFromCart(MyCartFragment.this.cartItemList.get(position), position);
                    setCounterItemAddedCart();
                    pDialog.dismiss();
                }
            }, 1000);
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

    public void UpdateCartFromServer() {
      /*  if(UserManager.getInstance().getUser()!=null && UserManager.getInstance().getUser().getLogin_status()!="0")
        {
            final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.show();
            CartSync.getInstance().UpdateAppCart(UserManager.getInstance().getUser().getId(), new IResponseListener() {
                @Override
                public void onResponse(boolean result, ArrayList<ShoppingCartItem> data) {
                    pDialog.dismiss();
                    if(result==true)
                    {
                        cartItemList= data;
                        setCounterItemAddedCart();
                        setDataToAdapter();
                    }
                    else {
                        cartItemList = new ArrayList<ShoppingCartItem>();
                        setCounterItemAddedCart();
                    }
                }
            });
            if(pDialog.isShowing())
                pDialog.dismiss();
*/
      //  }
    }

    public void callF(String pro_id, String pro_name,ImageView view)
    {
        BaseFragment frmnt = new SimpleProductFragment2();
        Bundle bundle = new Bundle();
        bundle.putString(ConstantDataMember.PRO_ID, pro_id);
        bundle.putString(ConstantDataMember.PRO_NAME, pro_name);
        ImageView img = view;
        SimpleProductFragment.imageDrawable=img.getDrawable();
        frmnt.setArguments(bundle);
        callFragment(frmnt, frmnt.getClass().getSimpleName());
    }

    @Override
    public void processFinish(HashMap<String, String> output) {
        checkCartWithGuestProductStock(output);
        if(FLAG == 0) {
            UserProfileItem currentuser = UserManager.getInstance().getUser();
            if (currentuser != null && currentuser.getLogin_status() != "0") {
                if(!Utils.checkCartItemforDownloadable(cartItemList))
                        callFragment(new BillingAndShippingAddressFragment(), "BillingAndShippingAddressFragment");
                    else
                       callFragment(new BillingAddressDownloadableFragment(), "BillingAddressDownloadableFragment");

            } else {
                callFragment(new MyCheckOutFragment(), "MyCheckOutFragment");
            }
        }
    }

    private void checkCartWithGuestProductStock(HashMap<String, String> output) {
        // loop on items
        FLAG=0;
        Iterator it = output.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            ShoppingCartItem item = ShoppingCart.getInstance().getSingleCartItemfromID((String)pair.getKey());
            if(item.getShoppingItem().getStockQuantity() != (int)Float.parseFloat((String)pair.getValue()))
            FLAG = 1;
            item.getShoppingItem().setStockQuantity((String)pair.getValue());
            ShoppingCart.getInstance().addItem(item,this);
            cartItemList = new ArrayList<>(ShoppingCart.getInstance().getCartItems());
            setDataToAdapter();
            }
        adapter.notifyDataSetChanged();

    }
}
