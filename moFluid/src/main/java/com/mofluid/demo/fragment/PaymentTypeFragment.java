package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import com.ebizon.fluid.StripePayment.StripeConstants;
import com.ebizon.fluid.StripePayment.StripeUserActivity;
import com.ebizon.fluid.StripePayment.StripeuserCard;
import com.ebizon.fluid.Utils.Config;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.GuestStockCheck;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.model.AddressData;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.PaymentMethod;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.ShoppingCartItem;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserProfileItem;
import com.ebizon.fluid.model.WebApiManager;

import com.mofluid.magento2.CcAvenue;
import com.mofluid.magento2.EncodeString;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.MyPaymentTypeAdapter;
import com.mofluid.magento2.custome.widget.ShowAlertDialogBox;
import com.mofluid.magento2.database.MyDataBaseAdapter;
import com.mofluid.model_new.RespoonseCodeMap;
import com.mofluid.utility_new.Callback;
import com.mofluid.utility_new.PaymentManager;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sampleapplication.LoginActivity2;

/**
 * Created by ebizon on 18/12/15.
 */
@SuppressLint("ValidFragment")
public class PaymentTypeFragment extends BaseFragment implements GuestStockCheck {
    String CONFIG_ENVIRONMENT ="-1";
    // note that these credentials will differ between live & sandbox environments.
    private static final int REQUEST_CODE_PAYMENT = 1;

    private final boolean isBothAddressSame;
    private final JSONArray pro_list_place_ordr_jsonArray;
    private final String shipp_method_code,shipp_carrier_code;
    public String customer_id;
    private Spinner spinner_payment_method_type;
    private ArrayList<String> paymentTypeList;
    private ArrayList<PaymentMethod> paymentList;
    private TextView txt_submit, txt_payment_type;
    private String TAG;
    private String paymentmethod;
    private String orderid;
    private boolean isGuest = false;
    private AddressData billingGuest, shippingGuest;
    private String headerText;
    private int FLAG = 0;
    private ArrayList<ShoppingCartItem> cartItemList;
    private PaymentMethod selected_payment ;
    private boolean isccAvenue=false;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    private WebView wview;
    private EditText mob;

    public PaymentTypeFragment(boolean isBothAddressSame,String shipp_carrier_code,String shipp_method_code, JSONArray pro_list_place_ordr_jsonArray) {
        this.isBothAddressSame = isBothAddressSame;
        this.pro_list_place_ordr_jsonArray = pro_list_place_ordr_jsonArray;
        this.shipp_method_code = shipp_method_code;
        this.shipp_carrier_code=shipp_carrier_code;
    }

    public PaymentTypeFragment(boolean isBothAddressSame,String shipp_carrier_code, String shipp_method_code, JSONArray pro_list_place_ordr_jsonArray, boolean isGuest, AddressData billingGuest, AddressData shippingGuest) {
        this.isBothAddressSame = isBothAddressSame;
        this.pro_list_place_ordr_jsonArray = pro_list_place_ordr_jsonArray;
        this.shipp_method_code = shipp_method_code;
        this.billingGuest = billingGuest;
        this.shippingGuest = shippingGuest;
        this.isGuest = isGuest;
        this.shipp_carrier_code=shipp_carrier_code;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_payment_type, null);
        getViewControll(rootView);
        initialized();
        hitserviceforPaymentType();
        //setAdapter();
        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkcartitemsforUser()) {
                    Log.d("cartsync", "true returned by quantity check");
                    if(selected_payment==null)
                        return;
                    switch (selected_payment.getPay_code()) {
                        case "Select Payment":
                        case "Wählen Sie Zahlungs":
                        case "Sélectionnez Paiement":
                            Activity activity = getActivity();
                            if (isAdded() && activity != null)
                                new ShowAlertDialogBox().showCustomeDialogBox(getActivity(), getActivity().getResources().getString(R.string.app_name), getActivity().getResources().getString(R.string.please_select));
                            break;
                        case "cashondelivery":
                            paymentmethod = "cashondelivery";
                            processPlaceOrder("");
                            break;
                        case "checkmo":
                            paymentmethod = "checkmo";
                            processPlaceOrder(paymentmethod);
                            break;
                        case "paypal_standard":
                            paymentmethod = "paypal_standard";
                            payByPayPal(selected_payment);
                            break;
                        case "authorizenet":
                            paymentmethod = "authorizenet";
                            authorizenet(selected_payment);
                            break;
                    case "ccavenue":
                        isccAvenue=true;
                        paymentmethod="ccavenue";//"ccavenue";
                        payUsingCCAvenue();
                        break;
                     case "md_stripe":
                            paymentmethod = selected_payment.getPay_code();
                            payusingStripe(selected_payment);
                            break;
                        case "directpayonline_plug":
                            payUsingDPO();
                        break;
                     default:
                         processPlaceOrder(selected_payment.getPay_code());
                    }
                } else {
                    Log.d("cartsync", "false returned by quantity check");
                }

            }
        });

        spinner_payment_method_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PaymentMethod paymentMethod= (PaymentMethod) adapterView.getAdapter().getItem(i);
                selected_payment = paymentMethod;
                setDescription();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootView;
    }
private void setDescription(){
if(selected_payment.getDescription()==null || selected_payment.getDescription().equalsIgnoreCase("null"))
{
    mob.setVisibility(View.GONE);
    wview.setVisibility(View.GONE);
    return;
}
Log.d("saddam","description="+selected_payment.getDescription());
wview.loadData(selected_payment.getDescription(),"text/html; charset=utf-8", "UTF-8");
mob.setVisibility(View.VISIBLE);
wview.setVisibility(View.VISIBLE);
}
private void showToast(int code){
    Toast.makeText(getActivity().getApplicationContext(), RespoonseCodeMap.getMessage(code),Toast.LENGTH_SHORT).show();
}
private void payUsingDPO(){
    MyDataBaseAdapter dbAdapter = new MyDataBaseAdapter(getActivity());
    double totalPrice = dbAdapter.getTotalPrice();
    PaymentManager.getInstance().openDirectPay(totalPrice, getActivity(), new Callback() {
        @Override
        public void callback(Object o, int response_code) {
                             if(o==null){
                                 return;
                             }
                   showToast(response_code);
                   if(response_code==RespoonseCodeMap.PAYMENT_SUCCESS) processPlaceOrder(selected_payment.getPay_code());

        }
    });
}
    private void payusingStripe(PaymentMethod selected_payment) {
        String stripeID="0";
        MyDataBaseAdapter dbAdapter = new MyDataBaseAdapter(getActivity());
        double totalPrice = dbAdapter.getTotalPrice();
        Intent intent = new Intent(getActivity(), StripeUserActivity.class);
        intent.putExtra(StripeConstants.STRIPE_CARD_PRICE, String.valueOf(totalPrice));

        SharedPreferences preference = getActivity().getSharedPreferences(ConstantDataMember.USER_INFO_SESSION, Context.MODE_PRIVATE);
        stripeID = preference.getString(ConstantDataMember.USER_INFO_STRIPE_ID, "0");
        intent.putExtra(StripeConstants.USER_STRIPE_ID, stripeID);
        startActivityForResult(intent, StripeConstants.STRIPE_REQUEST_CODE);


    }

    private void hitserviceforPaymentType() {
        String url = WebApiManager.getInstance().getcheckoutURL();
        url=String.format(url,Integer.parseInt(customer_id),shipp_method_code,shipp_carrier_code);
        GetPaymentMethods get_payment = new GetPaymentMethods();
        get_payment.execute(url);
    }


    private boolean checkcartitemsforUser() {
        final String productsList = ShoppingCart.getInstance().getCartItemsId();
        final boolean[] result = new boolean[1];
        //final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
       // pDialog.setCancelable(true);
        //pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        //pDialog.show();
        // close progress dialogbox after 1 second
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
               // result[0] = CartSync.getInstance().getAnonymousCartQuantity(productsList, PaymentTypeFragment.this);
                //setCounterItemAddedCart();
                //pDialog.dismiss();
//
//            }
//        }, 1000);
        return true;




    }


    private void payUsingCCAvenue() {
        processPlaceOrder("");
      /*  MyDataBaseAdapter dbAdapter = new MyDataBaseAdapter(getActivity());
        double totalPrice = dbAdapter.getTotalPrice();
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("amount", String.valueOf(totalPrice));
        startActivityForResult(intent, 3);*/

    }

    private void authorizenet(PaymentMethod selected_payment) {
        String zipcode = "";
if(UserManager.getInstance().getUser()!=null) {
    if (UserManager.getInstance().getUser().getShippingAddress() != null)
        zipcode = UserManager.getInstance().getUser().getShippingAddress().getZipCode();
    else
        zipcode = UserManager.getInstance().getUser().getBillingAddress().getZipCode();
}
        boolean atleastOneAlpha = zipcode.matches(".*[a-zA-Z]+.*");
        if (atleastOneAlpha == true) {
            zipcode = "20006";
        }
        if(zipcode.equals(""))
            zipcode="20006";

        Intent intent = new Intent(getActivity(), LoginActivity2.class);
        intent.putExtra("ZIPCODE", zipcode);
        startActivityForResult(intent, 2);
    }


    private void processPlaceOrder(String transID) {
        hitServiceToPlaceOrder(transID);
    }

    private void processPlaceOrder(String transID,StripeuserCard card) {
        hitServiceToPlaceOrder(transID,card);
    }

    private void payByPayPal(PaymentMethod selected_payment) {

        if(selected_payment.getMode()=="null"||selected_payment.getAcc_key()=="null")
            Toast.makeText(getActivity().getApplicationContext(),"PayPal currently unavailable",Toast.LENGTH_SHORT).show();
        else {

            if (selected_payment.getMode().equals("0"))
                CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
            if (selected_payment.getMode().equals("1"))
                CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;


        PayPalConfiguration config = new PayPalConfiguration()
                .environment(CONFIG_ENVIRONMENT)
                .clientId(selected_payment.getAcc_key());

        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
		}
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        MyDataBaseAdapter dbAdapter = new MyDataBaseAdapter(getActivity());
        double totalPrice = dbAdapter.getTotalPrice();
        Activity activity = getActivity();
        String appName;
        if (isAdded() && activity != null)
            appName = getActivity().getResources().getString(R.string.app_name);
        else
            appName = "Mofluid";
        return new PayPalPayment(new BigDecimal(totalPrice), Config.getInstance().getCurrencyCode(), appName, paymentIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 || requestCode == 2 || requestCode == 3)
            if (resultCode == Activity.RESULT_OK)
            {
                String transID = "";
                if(requestCode == 1) {
                    // Paypal used

                    PaymentConfirmation confirm = data
                            .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    if (confirm != null) {
                        try {
                            transID = confirm.toJSONObject().getJSONObject("response").getString("id");
                            Log.d("PAYPALL","Trans ID is "+ transID);
                            //    Toast.makeText(getActivity(), "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            System.out
                                    .println("an extremely unlikely failure occurred: " + e);
                        }

                    }
                }

                    processPlaceOrder(transID,null);
                }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Activity activity = getActivity();
                if (isAdded() && activity != null)
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.payment_return), Toast.LENGTH_SHORT).show();
            } else if (resultCode == 4) {
                Toast.makeText(getActivity(), getActivity().getIntent().getStringExtra("transStatus"), Toast.LENGTH_SHORT).show();
            } else {
                Activity activity = getActivity();
                if (isAdded() && activity != null)
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.payment_cancel), Toast.LENGTH_SHORT).show();
            }

        if(requestCode == StripeConstants.STRIPE_REQUEST_CODE) {
            if (data != null) {
                Log.d("Stripe", "Result came back from Stripe Activity");
                String result = data.getStringExtra(StripeConstants.STRIPE_PAYMENT_RESULT);
                String transID = data.getStringExtra(StripeConstants.STRIPE_TRANS_ID);

                Log.d("Stripe", "Result is " + result + "transID is " + transID);

                if (resultCode == Activity.RESULT_OK) {
                    Log.d("Stripe", "Stripe result come as RESULT_OK ");
                    if (result != null && result.equals("succeeded") && transID != null) {
                        StripeuserCard card = (StripeuserCard) data.getSerializableExtra(StripeConstants.STRIPE_CARD_SELECTED_CARD);
//                        intent.putExtra(StripeConstants.STRIPE_CARD_SELECTED_CARD,selected_card);
                        processPlaceOrder(transID,card);
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.d("Stripe", "Stripe result come as RESULT_CANCELED ");
                    String message = data.getStringExtra(StripeConstants.STRIPE_MESSAGE);
                    if (result != null && result.equals("back")) ;
                        // do nothing
                    else
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                } else {
                    // result not sent from last activity.
                    Log.d("Stripe", "Stripe result come as none of methods ");
                    Activity activity = getActivity();
                    if (isAdded() && activity != null)
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.payment_cancel), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                // result not sent from last activity.
                Log.d("Stripe", "Stripe result come as none of methods ");
                Activity activity = getActivity();
                if (isAdded() && activity != null)
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.payment_cancel), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void initialized() {
        paymentList = new ArrayList<>();
        paymentTypeList = new ArrayList<>();
        TAG = getClass().getSimpleName();
        SharedPreferences preference = getActivity().getSharedPreferences(ConstantDataMember.USER_INFO_SESSION, Context.MODE_PRIVATE);
        customer_id = preference.getString(ConstantDataMember.USER_INFO_USER_ID, "");

    }

    private void setAdapter() {
        createPaymentList();
        MyPaymentTypeAdapter adapter = new MyPaymentTypeAdapter(getActivity(), paymentList);
        spinner_payment_method_type.setAdapter(adapter);
    }

    private void createPaymentList() {
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            String select_text = getActivity().getResources().getString(R.string.select_payment);
            paymentList.add(0,new PaymentMethod(select_text,select_text,"1","","","","0",null));

        }
      /*  for (int i = 0; i < paymentList.size(); i++) {
            if(paymentList.get(i).isActiveStatusBool()) {
                if(!paymentList.get(i).getPay_code().equals("banktransfer"))
                paymentTypeList.add(paymentList.get(i).getTitle());
            }
        }*/
        //paymentList.add(new PaymentMethod("CCAvenue","ccavenue","1","","","","0")); // ccavenue if added manually
        //paymentList.add(new PaymentMethod("Stripe","stripe","1","","","","0")); // ccavenue if added manually
    }

    private String getPaymentData(String orderId) {
        HashMap<String,String> map=new HashMap<>();
        MyDataBaseAdapter dbAdapter = new MyDataBaseAdapter(getActivity());
        double totalPrice = dbAdapter.getTotalPrice();
        String valuediscount=new DecimalFormat("#00.000").format(totalPrice);
       UserProfileItem userProfileItem= UserManager.getInstance().getUser();
        map.put("orderid",orderId);
        //   map.put("vps_locale",Config.getInstance().getCurrencyCode());
        map.put("shopid","");
        map.put("first_name","");
        map.put("last_name","");
        map.put("siteurl","http://brooksbicycle.com/index.php/");
        map.put("amount",valuediscount);
        map.put("platform","android");
        if(userProfileItem!=null){
            map.put("first_name",userProfileItem.getFirstname());
            map.put("last_name",userProfileItem.getLastname());
        }
        JSONObject json=new JSONObject(map);
        String str=json.toString();
        str=EncodeString.encodeStrBase64Bit(str);
        return str;
    }

    private void getViewControll(View rootView) {
        spinner_payment_method_type = (Spinner) rootView.findViewById(R.id.spinner_payment_method_type);
        txt_submit = (TextView) rootView.findViewById(R.id.txt_submit);
        txt_payment_type = (TextView) rootView.findViewById(R.id.text_payment_type);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
        txt_payment_type.setTypeface(tf);
        wview=(WebView) rootView.findViewById(R.id.wview);
        mob=(EditText) rootView.findViewById(R.id.mob);
    }
private boolean isNumber(String s){
        try
        { int i = Integer.parseInt(s); return true; }

        catch(NumberFormatException er)
        { return false; }
}
private void error(String msg){
    Toast.makeText(getActivity().getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
}
    private void hitServiceToPlaceOrder(String payment_method) {
        /*String product = getProducts();
        String address = getAddresses();
        product = EncodeString.encodeStrBase64Bit(product);
        address = EncodeString.encodeStrBase64Bit(address);

        String is_create_quote = "1";
        String url = String.format(WebApiManager.getInstance().getPlaceOrderURL(getActivity()), customer_id, Config.getInstance().getCurrencyCode(), product, address, is_create_quote, shipp_method_code, paymentmethod);
        if (MyDataBaseAdapter.getCoupon() != null) {
            url = url + "&couponCode=" + MyDataBaseAdapter.getCoupon();
        }
        Log.d(TAG, "hitServiceToPlaceOrder() called with:final " + url + "");*/
        String url=WebApiManager.getInstance().getPlaceOrderUrl();
        url=String.format(url,Integer.parseInt(customer_id),payment_method);
        if(mob.getVisibility()==View.VISIBLE){
            if(mob.getText().toString()==null || mob.getText().toString().length()<=2)
            {
                Toast.makeText(getActivity(),"Please enter mobile number.",Toast.LENGTH_SHORT).show();
                return;
            }
            url+="&mob="+mob.getText().toString();
        }

        final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
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
                        JSONObject jsonOBJ = new JSONObject(response);
                        orderid = jsonOBJ.getString("orderId");
                        if(!isNumber(orderid)){
                            error(jsonOBJ.getJSONObject("orderId").getString("message"));
                            return;
                        }
                        if(!isccAvenue) {
                            if (isGuest == false)
                                callFragment(new OrderAcknowledgeFragment(orderid, paymentmethod != "cashondelivery"));
                            else
                                callFragment(new OrderAcknowledgeFragment(orderid, paymentmethod != "cashondelivery", billingGuest, shippingGuest, isGuest));

                        }else{
                            startCcAvenue(orderid);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showGenericError();
                    }
                } else {
                    showGenericError();
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

    private void startCcAvenue(String orderid) {
        CcAvenue payment;
        String paymentdata=getPaymentData(orderid);
        String finalUrl=WebApiManager.getInstance().geCCAvenuePaymentURL();
        finalUrl=finalUrl+paymentdata;
        if(isGuest){
            payment=new CcAvenue(finalUrl,orderid,false,billingGuest,shippingGuest,true);
        }else{
            payment=new CcAvenue(finalUrl,orderid,false);
        }

        callFragment(payment);

    }

    private void hitServiceToPlaceOrder(String transID,StripeuserCard card) {
        String product = getProducts();
        String address = getAddresses();
        product = EncodeString.encodeStrBase64Bit(product);
        address = EncodeString.encodeStrBase64Bit(address);
//        String carddata = null;
//        if(card!=null) {
//            HashMap<String, String> carddetails = new HashMap<>();
//            carddetails.put("cc_type", card.getBrand());
//            carddetails.put("cc_number", "424242424242" + card.getLast4());
//            carddetails.put("expiration", card.getExp_month());
//            carddetails.put("expiration_yr", card.getExp_year());
//
//            JSONObject obj = new JSONObject(carddetails);
//            carddata = obj.toString();
//            carddata = EncodeString.encodeStrBase64Bit(carddata);
//        }
        String is_create_quote = "1";
        String url = String.format(WebApiManager.getInstance().getPlaceOrderURLStripe(getActivity()), customer_id, Config.getInstance().getCurrencyCode(), product, address, is_create_quote, shipp_method_code, paymentmethod,transID,"Successfull");
        if (MyDataBaseAdapter.getCoupon() != null) {
            url = url + "&couponCode=" + MyDataBaseAdapter.getCoupon();
        }
        Log.d(TAG, "hitServiceToPlaceOrder() called with:final " + url + "");

        final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
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
                        JSONObject jsonOBJ = new JSONObject(response);
                        orderid = jsonOBJ.getString("orderId");
                        if (isGuest == false)
                            callFragment(new OrderAcknowledgeFragment(orderid, paymentmethod != "cashondelivery"));
                        else
                            callFragment(new OrderAcknowledgeFragment(orderid, paymentmethod != "cashondelivery", billingGuest, shippingGuest, isGuest));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showGenericError();
                    }
                } else {
                    showGenericError();
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

    private String getAddresses() {
        JSONObject jsonObj = new JSONObject();
        UserProfileItem activeUser = UserManager.getInstance().getUser();
        if (activeUser != null) {
            try {
                AddressData billingAddress = activeUser.getBillingAddress();
                if (billingAddress != null) {
                    jsonObj.put("billing", billingAddress.getJSON());
                }

                if (isBothAddressSame) {
                    if (billingAddress != null) {
                        jsonObj.put("shipping", billingAddress.getJSON());
                    }
                } else {
                    AddressData shippingAddress = activeUser.getShippingAddress();
                    if (shippingAddress != null) {
                        jsonObj.put("shipping", shippingAddress.getJSON());
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (isGuest == true) {
            try {
                AddressData billingAddress = billingGuest;
                if (billingAddress != null) {
                    jsonObj.put("billing", billingAddress.getJSON());
                }

                if (isBothAddressSame) {
                    if (billingAddress != null) {
                        jsonObj.put("shipping", billingAddress.getJSON());
                    }
                } else {
                    AddressData shippingAddress = shippingGuest;
                    if (shippingAddress != null) {
                        jsonObj.put("shipping", shippingAddress.getJSON());
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return jsonObj.toString();
    }

    private String getProducts() {
        return pro_list_place_ordr_jsonArray.toString();
    }

    @Override
    public void onResume() {
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            headerText = getActivity().getResources().getString(R.string.payment_type_header);
            //MainActivity.setHeaderText(headerText);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
        }
        super.onResume();
    }



    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
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

    @Override
    public void processFinish(HashMap<String, String> output) {
       /* checkCartWithGuestProductStock(output);
        if (FLAG == 0) {
            //perform task of success
        } else {
            //perform task of failure
            Activity act = getActivity();

            final Dialog alertDialog = new Dialog(act);
            alertDialog.setContentView(R.layout.custom_dialog);
            TextView text = (TextView) alertDialog.findViewById(R.id.textDialog);
            Button declineButton = (Button) alertDialog.findViewById(R.id.declineButton);
            declineButton.setOnClickListener(new View.RecyclerViewClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    callFragment(new MyCartFragment(), "MyCartFragment");
                }
            });
            alertDialog.show();
            alertDialog.setCancelable(false);


        }*/
    }

    private void checkCartWithGuestProductStock(HashMap<String, String> output) {
        // loop on items
        FLAG = 0;
        Iterator it = output.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            ShoppingCartItem item = ShoppingCart.getInstance().getSingleCartItemfromID((String) pair.getKey());
            if (item.getShoppingItem().getStockQuantity() != (int) Float.parseFloat((String) pair.getValue()))
                FLAG = 1;
            item.getShoppingItem().setStockQuantity((String) pair.getValue());
            ShoppingCart.getInstance().addItem(item, this);
            cartItemList = new ArrayList<>(ShoppingCart.getInstance().getCartItems());
            //setDataToAdapter();
        }
        //adapter.notifyDataSetChanged();

    }

    private class GetPaymentMethods extends AsyncTask<String, String, String> {
        final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
                         //pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setCancelable(false);
            //pDialog.setProgressStyle(and);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {

            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(params[0], new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    Log.d("Volley= ", response);
                     try{
                         JSONObject obj=new JSONObject(response);
                         JSONArray payment_methods=obj.getJSONArray("payment_methods");
                         for(int i=0;i<payment_methods.length();i++){
                             JSONObject payment_method=payment_methods.getJSONObject(i);
                             String title =payment_method.getString("title");
                             String pay_code =payment_method.getString("code");
                             String description=null;
                             if(payment_method.has("description"))
                             description=payment_method.getString("description");
                             paymentList.add(new PaymentMethod(title, pay_code, "1", null, null, null, null,description));
                             publishProgress("Success");
                             pDialog.dismiss();
                         }

                     }catch (JSONException e){}
                   /* try {
                        JSONArray strJSNobj = new JSONArray(response);
                        for (int i = 0; i < strJSNobj.length(); i++) {
                            JSONObject current_payment = strJSNobj.getJSONObject(i);
                            String title = current_payment.getString("payment_method_title");
                            String pay_code = current_payment.getString("payment_method_order_code");
                            String activeStatus = current_payment.getString("payment_method_status");
                            String acc_id = current_payment.getString("payment_method_account_id");
                            String acc_key = current_payment.getString("payment_method_account_key");;
                            String acc_email_linked = current_payment.getString("payment_account_email");
                            String mode = current_payment.getString("payment_method_mode");
                            if(!activeStatus.equalsIgnoreCase("0")) {
                                paymentList.add(new PaymentMethod(title, pay_code, activeStatus, acc_id, acc_key, acc_email_linked, mode));
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                }
            });

            return null;


        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            setAdapter();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //if(values[0].equals("Success"))
            //setAdapter();
        }
    }
}
