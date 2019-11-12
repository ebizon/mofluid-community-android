package com.mofluid.magento2.fragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.ebizon.fluid.Utils.AddressManager;
import com.ebizon.fluid.Utils.Config;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.MySharedPreferences;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.model.AddressData;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.ShippingMethodItem;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.ShoppingCartItem;
import com.ebizon.fluid.model.ShoppingItem;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserProfileItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.AddressList;
import com.mofluid.magento2.EncodeString;
import com.mofluid.magento2.GetAllJsonKeys;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.myShippingMethodAdapter;
import com.mofluid.magento2.custome.widget.ShowAlertDialogBox;
import com.mofluid.magento2.database.MyDataBaseAdapter;
import com.mofluid.magento2.service.AppController;
import com.ebizon.fluid.model.Address;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ebizon on 18/12/15.
 */
public class ShippingMethodSameAddressFragment extends BaseFragment implements View.OnClickListener {
    private Address shipping_address,billing_address;
    private Button shipping_add,shipping_change,billing_add,billing_change;
    private View rootView;
    private LinearLayout ll_list_of_order_item;
    private LayoutInflater inflaterLayout;
    private TextView txt_proceed;
    private MyDataBaseAdapter dbAdapter;
    private TextView txtV_total_price;
    private String TAG;
    private JSONObject jsonOBJ;
    private JSONArray pro_list_place_ordr_jsonArray;
    private String customer_id;
    private ArrayList<String> listOfJsnKeys;
    private final ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private String coupon_discount;
    private String address_id,carrier,carrier_title,shipp_carrier_code, shipp_method_code,created_at,description,error_message,id,method_title,updated_at;
    private int price;
    private JSONArray shipping_methodJsonArray;
    private ArrayList<String> listOfJsonKeysForShippMethd;
    private ArrayList<ShippingMethodItem> listOfMethod;
    private Spinner spinner_shipping_method;
    private LinearLayout ll_show_item_details, ll_spinner_shipping_method;
    private TextView txtV_ship_charge;
    private TextView txtV_grnd_total;
    private EditText edt_coupan_code;
    private TextView txtV_apply_coupan;
    private TextView txtV_shipping_type;
    private String code;
    private String headerText;
    private Typeface open_sans_regular, open_Sans_semibold, tf_h;
    private AddressData billingGuest, shippingGuest;
    private String tax_amount=null,total_amount=null;
    private TextView txtV_tax_charge;
    private RelativeLayout rl_tax;
    private boolean isGuest;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

public ShippingMethodSameAddressFragment(){}
    public boolean isGuest() {
        return isGuest;
    }
    public void setGuest(boolean guest) {
        isGuest = guest;
    }
   private void setAddressLayout(View rootView){
      // shipping_add=(Button)rootView.findViewById(R.id.shipping_add_new);
       shipping_change=(Button)rootView.findViewById(R.id.shipping_edit);
      // billing_add=(Button)rootView.findViewById(R.id.add_new);
       billing_change=(Button)rootView.findViewById(R.id.edit);
      // shipping_add.setOnClickListener(this);
       shipping_change.setOnClickListener(this);
       //billing_add.setOnClickListener(this);
       billing_change.setOnClickListener(this);
       MySharedPreferences ob=MySharedPreferences.getInstance();
       if(ob.get(ob.SHIPPING_ADDRESS_ID)!=null) {
           int ship_id = Integer.parseInt(ob.get(ob.SHIPPING_ADDRESS_ID));
           shipping_address= AddressManager.getInstance().getAddress(ship_id);
       }
       if(ob.get(ob.BILLING_ADDRESS_ID)!=null)
       {
           int bill_id=Integer.parseInt(ob.get(ob.BILLING_ADDRESS_ID));
           billing_address=AddressManager.getInstance().getAddress(bill_id);
       }
       if(shipping_address==null && billing_address==null){
           shipping_address=billing_address=AddressManager.getInstance().getGuest_address();
       }
       LinearLayout frameLayout1=(LinearLayout) rootView.findViewById(R.id.show_shiping_address_detail_include);
       LinearLayout frameLayout2=(LinearLayout) rootView.findViewById(R.id.show_billing_adress_details_include);
    TextView billing_name=(TextView)rootView.findViewById(R.id.name);
    TextView billing_addressv=(TextView)rootView.findViewById(R.id.address);
    TextView billing=(TextView)rootView.findViewById(R.id.billing);
    TextView shipping_name=(TextView)rootView.findViewById(R.id.shipping_name);
    TextView shipping_addressv=(TextView)rootView.findViewById(R.id.shipping_address);
    TextView shipping=(TextView)rootView.findViewById(R.id.shipping);
        billing.setText(AppController.getContext().getText(R.string.billing_header));
        billing_name.setText(billing_address.getFname()+" "+billing_address.getLname());
        billing_addressv.setText(billing_address.getCompleteAddress());
        shipping.setText(AppController.getContext().getText(R.string.shipping_header));
        shipping_name.setText(shipping_address.getFname()+" "+shipping_address.getLname());
        shipping_addressv.setText(shipping_address.getCompleteAddress());
        frameLayout1.setVisibility(View.VISIBLE);
        frameLayout2.setVisibility(View.VISIBLE);
   }
    private void hitServiceForCoupon(final String coupon) {
        String product=getProducts();
        product=EncodeString.encodeStrBase64Bit(product);
        String couponURL = WebApiManager.getInstance().getCouponURL(getActivity());
        String url=String.format(couponURL, customer_id, Config.getInstance().getCurrencyCode(),product,coupon);
        Log.d(TAG, "hitServiceForCoupon() called with:final " + url + "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity(),R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.show();

        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                Log.d("Volley= ", response);
                if (pDialog.isShowing())
                    pDialog.hide();

                if (response != null) {
                    try {
                        jsonOBJ = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    listOfJsnKeys = GetAllJsonKeys.getAllJsonKeyList(jsonOBJ);

                    if (listOfJsnKeys.contains("coupon_discount")) {
                        try {
                            coupon_discount = jsonOBJ.getString("coupon_discount");
                            System.out.println("Coupon Discount" + coupon_discount);
                        } catch (JSONException e) {
                        }
                    }
                    if (listOfJsnKeys.contains("coupon_status")) {
                        try {
                            int status = jsonOBJ.getInt("coupon_status");
                            if (status == 1) {
                                Log.d(TAG, "Got valid coupon " + coupon + "");
                                float discount = Float.parseFloat(coupon_discount);
                                MyDataBaseAdapter.SetCoupon(coupon, discount);
                                Activity activity = getActivity();
                                String validCoupon="";
                                if(isAdded()&&activity!=null) {
                                     validCoupon= String.format(getActivity().getResources().getString(R.string.validCoupon), coupon);
                                }
                                new ShowAlertDialogBox().showCustomeDialogBoxWithoutTitle(getActivity(),validCoupon);
                            } else {
                                String invalidCoupon="";
                                Activity activity = getActivity();
                                if(isAdded()&&activity!=null) {
                                    invalidCoupon = String.format(AppController.getContext().getString(R.string.invalidCoupon), coupon);
                                }
                                new ShowAlertDialogBox().showCustomeDialogBoxWithoutTitle(getActivity(), invalidCoupon);
                            }
                        } catch (JSONException e) {

                        }
                    }

                }
                calculatePrice();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_shipping_method_same_address, null);
        setAddressLayout(rootView);
        initialized();
        getViewControlls(rootView);
        addProductList();
        hitServiceToGetShippingMethod();
        txt_proceed.setOnClickListener(this);

        spinner_shipping_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    ll_show_item_details.setVisibility(View.GONE);

                } else {
                    ll_show_item_details.setVisibility(View.VISIBLE);
                    dbAdapter.SetShippingMethod(listOfMethod.get(i));
                    calculatePrice();
                    shipp_method_code = listOfMethod.get(i).getCode();
                    shipp_carrier_code=listOfMethod.get(i).getCarrier();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        txtV_apply_coupan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String coupon = edt_coupan_code.getText().toString().trim();
                if (coupon.isEmpty()) {
                    Activity activity = getActivity();
                    if(isAdded()&&activity!=null) {
                        new ShowAlertDialogBox().showCustomeDialogBoxWithoutTitle(getActivity(), getActivity().getResources().getString(R.string.please_enter_coupon));
                    }
                } else {
                    hitServiceForCoupon(coupon);
                }
            }
        });
        return  rootView;
    }


    private void calculatePrice(){
        RelativeLayout discountLabelView = (RelativeLayout) rootView.findViewById(R.id.RLDiscount);
        discountLabelView.setVisibility(View.GONE);

        if (coupon_discount != null && !coupon_discount.isEmpty()) {
            double discount = Float.parseFloat(coupon_discount);
            if (discount < 0.0) {
                TextView txtVDiscountLabel = (TextView) rootView.findViewById(R.id.txtV_discount_label);
                TextView txtVTotalDiscount = (TextView) rootView.findViewById(R.id.txtV_total_discount);
                txtVTotalDiscount.setText("-" + Utils.appendWithCurrencySymbol(Math.abs(discount)));
                Activity activity = getActivity();
                if(isAdded()&&activity!=null) {
                    String discLabel;
                    discLabel = getActivity().getResources().getString(R.string.Discount) + "(" + MyDataBaseAdapter.getCoupon() + ")";
//                else
//                    discLabel = getActivity().getResources().getString(R.string.Discount) + "(" + getActivity().getResources().getString(R.string.default_discount) + ")";
                    txtVDiscountLabel.setText(discLabel);

                    discountLabelView.setVisibility(View.VISIBLE);
                }
            }
        }

        ShippingMethodItem shippingItem =  dbAdapter.getShippingMethod();

        if (!Utils.checkCartItemforDownloadable(new ArrayList<ShoppingCartItem>(ShoppingCart.getInstance().getCartItems()))) {
            double shippingCost = shippingItem.getPrice();
            txtV_ship_charge.setVisibility(View.VISIBLE);
            txtV_shipping_type.setVisibility(View.VISIBLE);
            double tax_amount_price = Double.parseDouble(tax_amount);
            if(tax_amount_price>0.0) {
                rl_tax.setVisibility(View.VISIBLE);
                txtV_tax_charge.setVisibility(View.VISIBLE);
                txtV_tax_charge.setText(Utils.appendWithCurrencySymbol(tax_amount_price));
            }
            else
                rl_tax.setVisibility(View.GONE);
            txtV_ship_charge.setText(Utils.appendWithCurrencySymbol(shippingCost));
            Activity activity = getActivity();
            String ship_handle="";
            if(isAdded()&& activity!=null)
                ship_handle = activity.getResources().getString(R.string.ship_handle);
            txtV_shipping_type.setText(ship_handle);
        } else {
            txtV_ship_charge.setVisibility(View.GONE);
            txtV_shipping_type.setVisibility(View.GONE);
        }

        double totalPrice = dbAdapter.getTotalPrice();
        if(tax_amount!=null){
            totalPrice += Double.parseDouble(tax_amount);}
        txtV_grnd_total.setText(Utils.appendWithCurrencySymbol(totalPrice));
    }


    private boolean checkDownloadable() {
        return Utils.checkCartItemforDownloadable(new ArrayList<ShoppingCartItem>(ShoppingCart.getInstance().getCartItems()));
    }

    private void addProductList() {

        Collection<ShoppingCartItem> cartItemList = ShoppingCart.getInstance().getCartItems();
        for(ShoppingCartItem item : cartItemList) {
            double pro_total_price = ShoppingCart.getInstance().getSubTotal(item.getShoppingItem());

            View  row=inflaterLayout.inflate(R.layout.row_order_item_list, null);
            TextView txtV_pro_name=(TextView) row.findViewById(R.id.txtV_pro_name);
            txtV_pro_name.setText(item.getShoppingItem().getName());
            ImageView img_product = (ImageView) row.findViewById(R.id.imgV_pro);
            String img_str= item.getShoppingItem().getThumbnail();

            if (img_str != null) {
                imageLoader.get(img_str, ImageLoader.getImageListener(img_product, R.drawable.default_mofluid, R.drawable.default_mofluid));
            }

            TextView txtV_pro_qnty=(TextView) row.findViewById(R.id.txtV_pro_qnty);
            txtV_pro_qnty.setText("x" + item.getCount());

            TextView txtV_pro_price=(TextView) row.findViewById(R.id.txtV_pro_price);
            txtV_pro_price.setText(Utils.appendWithCurrencySymbol(item.getShoppingItem().getFinalPrice()));

            TextView txtV_pro_total_price=(TextView) row.findViewById(R.id.txtV_pro_total_price);
            txtV_pro_total_price.setText(Utils.appendWithCurrencySymbol(pro_total_price));
            ll_list_of_order_item.addView(row);
        }

        double productsTotalPrice = ShoppingCart.getInstance().getSubTotal();
        txtV_total_price.setText(Utils.appendWithCurrencySymbol(productsTotalPrice));
    }

    private void initialized() {
        inflaterLayout= getActivity().getLayoutInflater();
        open_sans_regular = Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
        open_Sans_semibold= Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
        tf_h= Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.HELVETICA_FONT_STYLE);

        txt_proceed=(TextView) rootView.findViewById(R.id.txt_proceed);
        dbAdapter=new MyDataBaseAdapter(getActivity());
        TAG=getClass().getSimpleName();
        pro_list_place_ordr_jsonArray =new JSONArray();
        SharedPreferences preference = getActivity().getSharedPreferences(ConstantDataMember.USER_INFO_SESSION, Context.MODE_PRIVATE);
        customer_id= preference.getString(ConstantDataMember.USER_INFO_USER_ID, "");

        listOfJsnKeys=new ArrayList<>();
        listOfJsonKeysForShippMethd=new ArrayList<>();
        listOfMethod=new ArrayList<>();
    }

    private void getViewControlls(View rootView) {
        ll_list_of_order_item=(LinearLayout) rootView.findViewById(R.id.ll_list_of_order_item);
        txtV_total_price=(TextView) rootView.findViewById(R.id.txtV_total_price);
        spinner_shipping_method=(Spinner) rootView.findViewById(R.id.spinner_shipping_method);
        ll_spinner_shipping_method=(LinearLayout) rootView.findViewById(R.id.ll_spinner_shipping_method);
        ll_show_item_details=(LinearLayout) rootView.findViewById(R.id.ll_show_item_details);
        txtV_ship_charge=(TextView) rootView.findViewById(R.id.txtV_ship_charge);
        txtV_grnd_total=(TextView) rootView.findViewById(R.id.txtV_grnd_total);
        txtV_tax_charge=(TextView) rootView.findViewById(R.id.txtV_tax_charge);
        edt_coupan_code=(EditText) rootView.findViewById(R.id.edt_coupan_code);
        txtV_apply_coupan=(TextView) rootView.findViewById(R.id.txtV_apply_coupan);
        txtV_shipping_type=(TextView)  rootView.findViewById(R.id.txtV_shipping_type);
        rl_tax = (RelativeLayout) rootView.findViewById(R.id.rl_tax);
    }

    @Override
    public void onClick(View view) {
        boolean isBothAddressSame=shipping_address.getAddress_id()==billing_address.getAddress_id();
        AddressList ob;
        switch (view.getId()) {
            case R.id.txt_proceed:
                PaymentTypeFragment fragment;
                BaseFragment bfragment;
                if(isGuest==false) {
                    if (isBothAddressSame) {
                        fragment = new PaymentTypeFragment(isBothAddressSame,shipp_carrier_code, shipp_method_code, pro_list_place_ordr_jsonArray);
                    } else
                        fragment = new PaymentTypeFragment(isBothAddressSame,shipp_carrier_code, shipp_method_code, pro_list_place_ordr_jsonArray);
                }
                else
                {
                    if (isBothAddressSame) {
                        fragment = new PaymentTypeFragment(isBothAddressSame,shipp_carrier_code, shipp_method_code, pro_list_place_ordr_jsonArray,isGuest,billingGuest,billingGuest);
                    } else
                        fragment = new PaymentTypeFragment(isBothAddressSame,shipp_carrier_code, shipp_method_code, pro_list_place_ordr_jsonArray,isGuest,billingGuest,shippingGuest);
                }
                callFragment(fragment);
                break;
           // case R.id.add_new:
            //   ob=new AddressList();
             //   ob.setBillingOrShipping(AddressList.BILLING_ADDRESS);
             //   callFragment(ob,"AddressList");
              //  break;
            case R.id.edit:
                 bfragment = new com.mofluid.magento2.fragment.BillingAndShippingAddressFragment();
                callFragment(bfragment);
                break;
            //case R.id.shipping_add_new:
               // ob=new AddressList();
               // ob.setBillingOrShipping(AddressList.SHIPPING_ADDRESS);
               // callFragment(ob,"AddressList");
            //    break;
            case R.id.shipping_edit:
                 bfragment = new com.mofluid.magento2.fragment.BillingAndShippingAddressFragment();
                callFragment(bfragment);
                break;
        }
    }

    private void hitServiceToGetShippingMethod() {
        String product=getProducts();
        String address=getAddresses();
        product=EncodeString.encodeStrBase64Bit(product);
        address=EncodeString.encodeStrBase64Bit(address);
        Log.d(TAG, "hitServiceToGetShippingMethod() called with:product " +product+ "");
        Log.d(TAG, "hitServiceToGetShippingMethod() called with:address " +address+ "");
        Activity activity = getActivity();
        String validCoupon="";
        String shippingmethod="";
        if(isAdded()&&activity!=null) {
             shippingmethod= getActivity().getResources().getString(R.string.select_shiping);
        }

        final ShippingMethodItem titleMethod = new ShippingMethodItem( "",  "",  shippingmethod,  "",  "",  "",  "",  "",  "",  0,  "");
        listOfMethod.add(titleMethod);
        showAllShippMethod();

        String is_create_quote = "0";
        String theme="modern";
        String find_shipping = "1";
        String shipmethod="Select";
        String url;
        /*if(!Utils.checkCartItemforDownloadable(new ArrayList<ShoppingCartItem>(ShoppingCart.getInstance().getCartItems())))
            url=String.format(WebApiManager.getInstance().getShippingMethodURL(getActivity()),customer_id,Config.getInstance().getCurrencyCode(),product,address, is_create_quote, find_shipping);
        else
            url=String.format(WebApiManager.getInstance().getDownloadableShippingMethodURL(getActivity()),customer_id,Config.getInstance().getCurrencyCode(),product,address, is_create_quote, shipmethod, theme);*/
      url=String.format(WebApiManager.getInstance().getShippingMethodWithoutAddressURL(),customer_id);
        Log.d(TAG, "hitServiceToGetShippingMethod() called with:final " + url + "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity(),R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.show();

        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                Log.d("Volley= ", response);
                if(pDialog.isShowing())
                    pDialog.hide();

                if(response!=null) {
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object=jsonArray.getJSONObject(i);
                            address_id=null;
                            carrier=object.getString("carrier_code");
                            carrier_title=object.getString("carrier_title");
                            code=object.getString("method_code");
                            created_at=null;
                            description=null;
                            error_message=object.getString("error_message");
                            id=object.getString("carrier_code");
                            method_title=object.getString("method_title");
                            price=object.getInt("price_incl_tax");
                            updated_at=null;

                            listOfMethod.add(new ShippingMethodItem(address_id, carrier, carrier_title, code, created_at, description, error_message, id, method_title, price, updated_at));
                        }
                        tax_amount="0.0";
                        total_amount="0.0";
                    }catch(Exception e){}
                    /*
                    try {
                        jsonOBJ = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    listOfJsnKeys= GetAllJsonKeys.getAllJsonKeyList(jsonOBJ);

                    if (listOfJsnKeys.contains("coupon_discount")) {
                        try {
                            coupon_discount=jsonOBJ.getString("coupon_discount");
                            float discount = Float.parseFloat(coupon_discount);
                            Activity activity= getActivity();
                            if(isAdded() && activity !=null)
                            MyDataBaseAdapter.SetCoupon(getActivity().getResources().getString(R.string.default_discount), discount);
                        } catch (JSONException e) {

                        }
                    }

                    if (listOfJsnKeys.contains("tax_amount")) {
                        try {
                            tax_amount = jsonOBJ.getString("tax_amount");
                        } catch (JSONException e) {
                        }
                    }

                    if (listOfJsnKeys.contains("total_amount")) {
                        try {
                            total_amount  = jsonOBJ.getString("total_amount");
                        } catch (JSONException e) {
                        }
                    }

                    try {
                        shipping_methodJsonArray=jsonOBJ.getJSONArray("shipping_method");
                    } catch (JSONException e) {


                    }

                    listOfMethod.clear();

                    listOfMethod.add(titleMethod);
                    if(shipping_methodJsonArray!=null) {
                        for (int i = 0; i < shipping_methodJsonArray.length(); i++) {
                            JSONObject obj = null;
                            try {
                                obj = shipping_methodJsonArray.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            listOfJsonKeysForShippMethd = GetAllJsonKeys.getAllJsonKeyList(obj);

                            if (listOfJsonKeysForShippMethd.contains("address_id")) {
                                try {
                                    address_id = obj.getString("address_id");
                                } catch (JSONException e) {


                                }
                            }
                            if (listOfJsonKeysForShippMethd.contains("carrier")) {
                                try {
                                    carrier = obj.getString("carrier");
                                } catch (JSONException e) {


                                }
                            }
                            if (listOfJsonKeysForShippMethd.contains("carrier_title")) {
                                try {
                                    carrier_title = obj.getString("carrier_title");
                                } catch (JSONException e) {


                                }
                            }
                            if (listOfJsonKeysForShippMethd.contains("code")) {
                                try {
                                    code = obj.getString("code");
                                } catch (JSONException e) {


                                }
                            }
                            if (listOfJsonKeysForShippMethd.contains("created_at")) {
                                try {
                                    created_at = obj.getString("created_at");
                                } catch (JSONException e) {


                                }
                            }
                            if (listOfJsonKeysForShippMethd.contains("description")) {
                                try {
                                    description = obj.getString("description");
                                } catch (JSONException e) {
                                }
                            }
                            if (listOfJsonKeysForShippMethd.contains("error_message")) {
                                try {
                                    error_message = obj.getString("error_message");
                                } catch (JSONException e) {

                                }
                            }
                            if (listOfJsonKeysForShippMethd.contains("id")) {
                                try {
                                    id = obj.getString("id");
                                } catch (JSONException e) {


                                }
                            }
                            if (listOfJsonKeysForShippMethd.contains("method_title")) {
                                try {
                                    method_title = obj.getString("method_title");
                                } catch (JSONException e) {


                                }
                            }
                            if (listOfJsonKeysForShippMethd.contains("price")) {
                                try {
                                    price = obj.getInt("price");
                                } catch (JSONException e) {


                                }
                            }
                            if (listOfJsonKeysForShippMethd.contains("updated_at")) {
                                try {
                                    updated_at = obj.getString("updated_at");
                                } catch (JSONException e) {


                                }
                            }
                            listOfMethod.add(new ShippingMethodItem(address_id, carrier, carrier_title, code, created_at, description, error_message, id, method_title, price, updated_at));

                        }
                    }
                    */
                    showAllShippMethod();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });
    }

    private void showAllShippMethod() {
        if(!checkDownloadable()) {
            Activity activity=getActivity();
            myShippingMethodAdapter adapter = new myShippingMethodAdapter(activity, listOfMethod);
            spinner_shipping_method.setAdapter(adapter);
            //calculatePrice();
        } else {
            ll_spinner_shipping_method.setVisibility(View.GONE);
            spinner_shipping_method.setVisibility(View.GONE);
            ll_show_item_details.setVisibility(View.VISIBLE);
            dbAdapter.SetShippingMethod(new ShippingMethodItem());
            calculatePrice();
            shipp_method_code = "Select";
        }
    }


    private String getProducts() {
        JSONObject jsonObject;
        Collection<ShoppingCartItem> cartItemList = ShoppingCart.getInstance().getCartItems();
        pro_list_place_ordr_jsonArray = new JSONArray();

        for (ShoppingCartItem item : cartItemList) {
            ShoppingItem it = item.getShoppingItem();
            jsonObject=it.createJSON(item.getCount());
            pro_list_place_ordr_jsonArray.put(jsonObject);
        }
        String str = String.valueOf(pro_list_place_ordr_jsonArray);
        String noSlashes = str.replace("\\", "");
        return noSlashes;
    }

    private String getAddresses() {
        if(billing_address==null && shipping_address==null){
            billing_address=shipping_address=AddressManager.getInstance().getGuest_address();
        }
        JSONObject jsonObj = new JSONObject();
        try{
            jsonObj.put("billing",billing_address.getJSON());
            jsonObj.put("shipping",  shipping_address.getJSON());
        }catch (JSONException e){Log.d(TAG,e.getMessage());}


        return jsonObj.toString();
    }

    @Override
    public void onResume() {
        if(!checkDownloadable()) {
            Activity activity = getActivity();
            if (isAdded() && activity != null) {
                headerText = getActivity().getResources().getString(R.string.shipping_same_address_header);
            }
        }
        else {
            Activity activity = getActivity();
            if(isAdded()&&activity!=null) {
                headerText = getActivity().getResources().getString(R.string.downloadable_billing_header);
            }
        }

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
