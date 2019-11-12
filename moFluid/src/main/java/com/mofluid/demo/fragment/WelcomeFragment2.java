package com.mofluid.magento2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.Helper;
import com.ebizon.fluid.Utils.MySharedPreferences;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.Utils.ViewUtils;
import com.mofluid.utility_new.WishListManager;
import com.ebizon.fluid.model.AddressData;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.OrderedProduct;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserOrders;
import com.ebizon.fluid.model.UserProfileItem;
import com.ebizon.fluid.model.WebApiManager;
import com.facebook.login.LoginManager;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.UserSession;
import com.mofluid.magento2.adapter.RecyclerAdapter;
import com.mofluid.magento2.custome.widget.ShowAlertDialogBox;
import com.mofluid.magento2.database.MyDataBaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by ebizon on 15/12/15.
 */
public class WelcomeFragment2 extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private TextView txtV_logout;
    private String TAG;

    static boolean isOrdering = false;
    public static boolean isSocialLogin= false;
    private ShowAlertDialogBox showAlertDialogBoxObj;
    ProgressDialog pDialog;
    private RelativeLayout change_password_arrow;
    ArrayList<DownloadProducts> listDownloadProducts;
    private ArrayList<UserOrders> complete_order;
    private RequestQueue requestQueue;
    private ArrayList<OrderedProduct> order_products;
    private TextView myorder;
    private TextView mydownloads;
    private String customerId;
    private Activity context;
    private TextView txt_loading;
    private RecyclerView my_order;
    private ListView my_downloads;
    private TextView txtv_user_profile_name;
    private ImageView imgv_user_profile_image;
    private LinearLayout rl_profile_password;
    private TextView contact_info;
    private TextView current_password;
    private TextView email_id;
    private ArrayList<UserOrders> complete_order_small;
    private ArrayList<DownloadProducts> listDownloadProductsSmall;
    private TextView navigatetext;
    private TextView no_product;
    private RelativeLayout rl_profile_name_edit, rl_profile_basic_info;
    private String PROFILE= "PROFILE";
    private RelativeLayout rl_profile_tab_options;
    private NestedScrollView scroll_user_profile;
    private Typeface open_sans_semibold;
    private Typeface open_sans_regular;
    private RelativeLayout rl_edit_address;
    private TextView txtv_user_profile_edit_address;
    private TextView txtv_user_profile_display_address;
    private final String COMMA= ", ";
    private TextView txtV_logout_error;
    private FrameLayout fl_error_layout;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;


    public WelcomeFragment2() {
    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(isOrdering)
        {
            callFragment(new BillingAndShippingAddressFragment(), "BillingAndShippingAddressFragment");
        }

        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_welcome_2, null);
            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
            initialized();
            getViewControlls(rootView);
            setFontStyle();
            hitServiseGetUSRProfile();
            setemailtoprefrences();
            if(isSocialLogin==true)
                rl_profile_password.setVisibility(View.GONE);
            else
                rl_profile_password.setVisibility(View.VISIBLE);
        }
        else
        {
            scroll_user_profile.scrollTo(0,0);
        }

        return  rootView;
    }

    private void setFontStyle() {

        txtv_user_profile_name.setTypeface(open_sans_semibold);
        myorder.setTypeface(open_sans_semibold);
        mydownloads.setTypeface(open_sans_semibold);
        no_product.setTypeface(open_sans_regular);
        txtv_user_profile_edit_address.setTypeface(open_sans_semibold);
        txtv_user_profile_display_address.setTypeface(open_sans_regular);
    }
    private void showdataerror() {

        fl_error_layout.setVisibility(View.VISIBLE);
        scroll_user_profile.setVisibility(View.GONE);

    }


    private void setemailtoprefrences() {
        final SharedPreferences sharedPreferences=getActivity().getSharedPreferences("lockapprating", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("emailID",HomeFragment.userEmail);
        editor.commit();
    }

    private void initialized() {
        pDialog=HomeFragment.pDialog;
        TAG=getClass().getSimpleName();
        showAlertDialogBoxObj=new ShowAlertDialogBox();
        listDownloadProducts=new ArrayList<DownloadProducts>();
        customerId= UserManager.getInstance().getUser().getId();
        complete_order=new ArrayList<>();
        complete_order_small=new ArrayList<>();
        listDownloadProductsSmall=new ArrayList<>();
        HomeFragment.userEmail=UserManager.getInstance().getUser().getUsername();
        context=getActivity();
        open_sans_semibold = Typeface.createFromAsset(context.getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
        open_sans_regular = Typeface.createFromAsset(context.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);


    }

    private void getViewControlls(View rootView) {

        txtv_user_profile_name = (TextView)rootView.findViewById(R.id.txtv_user_profile_name);
        imgv_user_profile_image = (ImageView)rootView.findViewById(R.id.imgv_user_profile_image);
        rl_profile_basic_info = (RelativeLayout)rootView.findViewById(R.id.rl_profile_basic_info);
        rl_profile_name_edit  = (RelativeLayout)rootView.findViewById(R.id.rl_profile_name_edit);
        rl_profile_tab_options = (RelativeLayout)rootView.findViewById(R.id.rl_profile_tab_options);
        fl_error_layout = (FrameLayout)rootView.findViewById(R.id.layout_error_service_profile);

        scroll_user_profile= (NestedScrollView)rootView.findViewById(R.id.scroll_user_profile);
        rl_edit_address = (RelativeLayout)rootView.findViewById(R.id.rl_edit_address);
        rl_edit_address.setClickable(true);
        rl_edit_address.setOnClickListener(this);
        txtv_user_profile_edit_address = (TextView)rootView.findViewById(R.id.txtv_user_profile_edit_address);
        txtv_user_profile_display_address = (TextView)rootView.findViewById(R.id.txtv_user_profile_display_address);
        //Contact info

        txtV_logout=(TextView) rootView.findViewById(R.id.btn_logout);
        txtV_logout_error=(TextView) rootView.findViewById(R.id.btn_logout_error);
        txtV_logout.setOnClickListener(this);
        txtV_logout_error.setOnClickListener(this);
        txtV_logout.setClickable(true);
        rl_profile_password = (LinearLayout) rootView.findViewById(R.id.rl_profile_password);
        contact_info=(TextView)rootView.findViewById(R.id.contact_info_value);
        current_password=(TextView)rootView.findViewById(R.id.current_password_value);
        email_id=(TextView)rootView.findViewById(R.id.email_address_value);
        change_password_arrow=(RelativeLayout)rootView.findViewById(R.id.pass);
        change_password_arrow.setOnClickListener(this);
        contact_info=(TextView)rootView.findViewById(R.id.contact_info_value);
        myorder=(TextView)rootView.findViewById(R.id.my_order_userprofile);
        no_product=(TextView)rootView.findViewById(R.id.no_product);
        mydownloads=(TextView)rootView.findViewById(R.id.my_downloads_userprofile);
        my_order=(RecyclerView) rootView.findViewById(R.id.my_order);
//        my_order.setExpanded(true);
        my_downloads=(ListView)rootView.findViewById(R.id.my_downloads);
        myorder.setOnClickListener(this);
        mydownloads.setOnClickListener(this);
    }

 /*   private void callfragment(BaseFragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(getActivity(), fragmentTransaction, new HomeFragment(), fragment, R.id.content_new);
        *//*
        index 7 is used to slide fragment Left to right and vise versa
         *//*
        fragmentTransactionExtended.addTransition(7);
//        if(fragmentName != null) {
//            fragmentTransaction.addToBackStack(fragmentName);
//        }
        fragmentTransactionExtended.commit();

    }
*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rl_edit_address:
                rl_edit_address.setClickable(false);
                BaseFragment fragment = new EditAddressFragment(false);
                fragment =new EditAddressFragment(true);
                callFragment(fragment);
                break;
            case R.id.pass:
                callFragment(new ChangePasswordFragment());
                break;
            case R.id.my_order_userprofile:
                Activity activity = getActivity();
                if(isAdded()&&activity!=null) {
                    mydownloads.setBackground(getResources().getDrawable(R.drawable.simple_rectangle_background));
                    myorder.setBackground(getResources().getDrawable(R.drawable.black_bottom));
                }
                //my_order.setVisibility(View.VISIBLE);
                myorder.setTextColor(Color.BLACK);
                my_downloads.setVisibility(View.GONE);
                mydownloads.setTextColor(Color.GRAY);
                if(complete_order.size()==0)
                {
                    setnavigatetext();
                }
                else{
                    no_product.setVisibility(View.GONE);
                    my_order.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.my_downloads_userprofile:
                activity = getActivity();
                if(isAdded()&&activity!=null) {
                    myorder.setBackground(getResources().getDrawable(R.drawable.simple_rectangle_background));
                    mydownloads.setBackground(getResources().getDrawable(R.drawable.black_bottom));
                }
                my_downloads.setVisibility(View.VISIBLE);
                my_order.setVisibility(View.GONE);
                myorder.setTextColor(Color.GRAY);
                mydownloads.setTextColor(Color.BLACK);
                if(listDownloadProducts.size()==0)
                {
                    setnavigatetext();
                }
                else{
                    no_product.setVisibility(View.GONE);
                    my_downloads.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_logout:
            case R.id.btn_logout_error:
                txtV_logout.setClickable(false);
                txtV_logout_error.setClickable(false);
                SharedPreferences preferences = getActivity().getSharedPreferences("lockapprating", 0);
                preferences.edit().remove("emailID").commit();
                SharedPreferences mySharedPreference=getActivity().getSharedPreferences(ConstantDataMember.USER_INFO_SESSION, Context.MODE_PRIVATE);
                SharedPreferences.Editor preference_editor_session = mySharedPreference.edit();
                preference_editor_session.putString(ConstantDataMember.USER_INFO_STRIPE_ID,"0");
                preference_editor_session.commit();
                MySharedPreferences.getInstance().clear();
                LoginManager.getInstance().logOut();
                if(SignInSignUpFragment.Fb_tracker==1) {
                    SignInSignUpFragment.Fb_tracker = 2;
//                    FragmentManager fm = getActivity().getFragmentManager();
//                    fm.popBackStackImmediate();
                }
                else
                    SignInSignUpFragment.Fb_tracker = 0;

                UserProfileItem activeUser = UserManager.getInstance().getUser();
                activeUser.setLogin_status("0");
                activeUser.setStripeID("0");
                MyDataBaseAdapter dbAdapter = new MyDataBaseAdapter(getActivity());
                dbAdapter.updateOnlyUserProfileStatus(activeUser);
                UserSession.setSession(getActivity());
                clearCart(dbAdapter);
                showAlertDialogBoxObj. showCustomeDialogBoxWithoutTitle(getActivity(), "Logged out Successfully");
                FragmentManager fm = getActivity().getFragmentManager();
                try {
                    fm.popBackStackImmediate();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                fm.popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                callFragment(new SignInSignUpFragment());
                break;
           /* case R.id.txtV_my_order:
                callFragment(new MyOrderFragment());
                break;
            case R.id.txtV_my_downloads:
                callFragment(new MyDownloadsFragment());*/
        }
    }

    private void clearCart(MyDataBaseAdapter dbAdapter) {
        ShoppingCart.getInstance().clearCart();
        dbAdapter.clearCart();
        setCounterItemAddedCart();
    }
    private void setCounterItemAddedCart() {
        MainActivity.txtV_item_counter.setVisibility(View.INVISIBLE);
    }
    private void hitServiseGetUSRProfile() {
        String url = WebApiManager.getInstance().getUserProfileURL(getActivity());
        UserProfileItem user = UserManager.getInstance().getUser();
        String finalUrl=String.format(url,user.getId());
        Log.d(TAG, "hitServiseGetUSRProfile() called with:finalUrl  " + finalUrl + "");
        GetUserProfile userProfile = new GetUserProfile();
        userProfile.execute(finalUrl);

    }

    private void hitserviceformydownloads() {
        String urlmyorder = WebApiManager.getInstance().getAlldownloadsURL(getActivity());
        final String mydownloads_URL=String.format(urlmyorder, customerId);
        Log.d(TAG, "hitserviceformydownloads called with url : ->  "+ mydownloads_URL);
        GetUserDownloads userDownloads = new GetUserDownloads();
        userDownloads.execute(mydownloads_URL);

    }

    private void setnavigatetext() {
        no_product.setVisibility(View.VISIBLE);
        my_order.setVisibility(View.GONE);
        my_downloads.setVisibility(View.GONE);
    }

    private void hitserviceformyorder() {
        String urlmyorder = WebApiManager.getInstance().getAllOrderURL(getActivity());
        String  myorder_URL=String.format(urlmyorder, customerId);
        Log.e("MYORDERURL", myorder_URL);
        GetUserOrders userOrders = new GetUserOrders();
        userOrders.execute(myorder_URL);
    }

    private void setBillPrflDefaultValue() {
        UserProfileItem activeUser = UserManager.getInstance().getUser();
        AddressData address = activeUser.getBillingAddress();
        if (address != null) {
            String name = address.getFirstName()+ " "+ address.getLastName();
            ViewUtils.setToTextView(rootView, R.id.txtv_user_profile_name, name);
            ViewUtils.setToTextView(rootView, R.id.contact_info_value, address.getContactNumber());
            String completeAddress = address.getStreet()+COMMA+address.getCity()+COMMA+address.getState();
            ViewUtils.setToTextView(rootView, R.id.txtv_user_profile_display_address, completeAddress);
            ViewUtils.setToTextView(rootView, R.id.email_address_value, HomeFragment.userEmail);

        }
    }

    private void setUserDetailsUI()
    {
        UserProfileItem activeUser = UserManager.getInstance().getUser();
        if(activeUser!=null)
        {
            String name = activeUser.getFirstname() + " " +activeUser.getLastname();
            ViewUtils.setToTextView(rootView, R.id.txtv_user_profile_name, name);
            Activity activity = getActivity();
            if(isAdded()&&activity!=null)
                ViewUtils.setToTextView(rootView, R.id.contact_info_value, activity.getResources().getString(R.string.no_contact_entered));
            ViewUtils.setToTextView(rootView, R.id.email_address_value, HomeFragment.userEmail);
        }
    }

    //    private void setPrflDefaultValue() {
//        UserProfileItem activeUser = UserManager.getInstance().getUser();
//        AddressData address = activeUser.getShippingAddress();
//
//        if(address != null) {
//            ViewUtils.setToTextView(rootView, R.id.txtv_user_profile_address_shipping, address.getStreet());
//
//        }
//    }
    @Override
    public void onResume() {
        Activity activity = getActivity();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if(isAdded()&&activity!=null) {
            String headerText = getActivity().getResources().getString(R.string.welcome_header);
//            MainActivity.setHeaderText(headerText);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
            if(rl_edit_address!=null)
                rl_edit_address.setClickable(true);
        }


        super.onResume();
    }

    private class GetUserProfile extends  AsyncTask<String,String, String>{

        @Override
        protected void onPreExecute() {
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(params[0], new Response.Listener<String>() {
                JSONObject strJSNobj = null;

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    try {
                        strJSNobj=new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONObject billingAddressJSn;
                        billingAddressJSn = strJSNobj.getJSONObject("BillingAddress");
                        JSONObject userinfo=strJSNobj.getJSONObject("CustomerInfo");
                        if(userinfo!=null)
                        {
                            UserProfileItem activeUser = UserManager.getInstance().getUser();
                            activeUser.setFirstname(userinfo.getString("firstname"));
                            activeUser.setLastname(userinfo.getString("lastname"));
                        }
                        if(billingAddressJSn !=null) {
                            AddressData address = AddressData.create(billingAddressJSn);
                            UserProfileItem activeUser = UserManager.getInstance().getUser();
                            activeUser.setBillingAddress(address);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                    try {
                        JSONObject shippingAddressJSn = strJSNobj.getJSONObject("ShippingAddress");
                        if(shippingAddressJSn!=null) {

                            AddressData address =  AddressData.create(shippingAddressJSn);

                            UserProfileItem activeUser = UserManager.getInstance().getUser();
                            activeUser.setShippingAddress(address);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                    UserProfileItem activeUser = UserManager.getInstance().getUser();
                    if (activeUser != null) {
                        if (activeUser.getBillingAddress() != null) {//
                            // set the text to values of user profile page with address billing
                            setBillPrflDefaultValue();
                            publishProgress(PROFILE);
                        }else{

                            setUserDetailsUI(); // set default values to only user and no address
                            publishProgress(PROFILE);
                            Activity activity = getActivity();
                            if(isAdded()&&activity!=null)
                                txtv_user_profile_display_address.setText(getActivity().getResources().getString(R.string.no_default_address_found));
                        }
                        publishProgress(PROFILE);
//                        if (activeUser.getShippingAddress() != null) {
////                            setPrflDefaultValue();
//
//                        }else {
////                            Activity activity = getActivity();
////                            if(isAdded()&&activity!=null)
////                                txtv_user_profile_address_shipping.setText(getActivity().getResources().getString(R.string.no_default_address_found));
//                        }
                    }

//                    pDialog.dismiss();
                    Log.d("DIALOG","1");

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                    Log.d("DIALOG","2");
                    showdataerror();
//                    pDialog.dismiss();
//                    pDialog.cancel();

                }
            });

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if(values[0].equals(PROFILE))
            {
//                rl_profile_name_edit.setVisibility(View.VISIBLE);
//                rl_profile_basic_info.setVisibility(View.VISIBLE);
//                rl_profile_tab_options.setVisibility(View.VISIBLE);
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hitserviceformyorder();
        }
    }

    private class GetUserDownloads extends  AsyncTask<String,String, String>{

        @Override
        protected String doInBackground(String... params) {
            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(params[0], new Response.Listener<String>() {
                @Override
                public void onResponse(String response)  {

                    try {
                        JSONObject mainObject = new JSONObject(response);
                        JSONArray mydownloads = mainObject.getJSONArray("mydownloads");
                        if(mydownloads.length()>0) {

                            for (int i = 0; i < mydownloads.length(); i++) {
                                JSONObject download_product = mydownloads.getJSONObject(i);
                                DownloadProducts downloadproducts = new DownloadProducts();
                                downloadproducts.setProduct_name(download_product.getString("product_name"));
                                downloadproducts.setOrder_date(download_product.getString("order_date"));
                                downloadproducts.setOrder_status(download_product.getString("status"));
                                downloadproducts.setOrder_remain(download_product.getString("remaining_download"));
                                downloadproducts.setDownload_url(download_product.getString("download_url"));
                                listDownloadProducts.add(downloadproducts);
                            }

                        }

                    } catch (JSONException e) {
                        showdata();
                        e.printStackTrace();
                    }
                    if(listDownloadProducts!=null) {

                        if(listDownloadProducts.size()>=5)
                        {
                            no_product.setVisibility(View.GONE);
                            for(int i=0;i<6;i++)
                            {
                                listDownloadProductsSmall.add(listDownloadProducts.get(i));
                            }
                            my_downloads.setAdapter(new DownloadlistAdapter(context, listDownloadProductsSmall,listDownloadProducts));
                            Helper.setListViewHeightBasedOnItems(my_downloads);
//
                        }else{
                            my_downloads.setAdapter(new DownloadlistAdapter(context, listDownloadProducts));
                            Helper.setListViewHeightBasedOnItems(my_downloads);
//
                        }

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("DIALOG","3");
                    showdata();
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

    }

    private class GetUserOrders extends  AsyncTask<String,String, String>{
        @Override
        protected String doInBackground(String... params) {
            NetworkAPIManager.getInstance(context).sendGetRequest(params[0], new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        int total;
                        JSONObject mainobject = new JSONObject(response);
                        String totalorders = mainobject.getString("total_count");
                        try {
                            total = Integer.parseInt(totalorders);
                        }catch (Exception e)
                        {
                            total = 0;
                        }
                        if(total > 0 ) {
                            JSONArray jsonArray = mainobject.getJSONArray("items");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                UserOrders userOrders = new UserOrders();
                                userOrders.setOrder_id(jsonObject.getString("entity_id"));
                                userOrders.setOrder_date(jsonObject.getString("updated_at"));
                                userOrders.setOrder_status(jsonObject.getString("status"));
                                userOrders.setAmount_Payble(jsonObject.getString("grand_total"));
                                userOrders.setShippingAmount(jsonObject.getString("base_shipping_amount"));
                                userOrders.setTaxAmount(jsonObject.getString("tax_amount"));
                                userOrders.setShipping_method(jsonObject.getString("shipping_description"));
                                userOrders.setOrderCurrency(jsonObject.getString("store_currency_code"));


                                //getting shipping details
                                try{


                                JSONObject shippingdetail = jsonObject.getJSONObject("billing_address");
                                Log.e("shippingdetail", shippingdetail.toString());
                                userOrders.setFirstname(shippingdetail.getString("firstname"));
                                userOrders.setLastname(shippingdetail.getString("lastname"));
                                userOrders.setContactnumber(shippingdetail.getString("telephone"));
                                userOrders.setCountry(shippingdetail.getString("country_id"));
                                userOrders.setZipcode(shippingdetail.getString("postcode"));
                                userOrders.setCity(shippingdetail.getString("city"));
                                userOrders.setState(shippingdetail.getString("region"));
                                //userOrders.setShippingaddress(shippingdetail.getString("street"));
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                                //set billing address
                                try {


                                    JSONObject billingdetail = jsonObject.getJSONObject("billing_address");
                                    userOrders.setFirstname_billing(billingdetail.getString("firstname"));
                                    userOrders.setLastname_billing(billingdetail.getString("lastname"));
                                    userOrders.setContactnumber_billing(billingdetail.getString("telephone"));
                                    userOrders.setCountry_billing(billingdetail.getString("country_id"));
                                    userOrders.setZipcode_billing(billingdetail.getString("postcode"));
                                    userOrders.setCity_billing(billingdetail.getString("city"));
                                    userOrders.setState_billing(billingdetail.getString("region"));
                                   // userOrders.setShipping_billing(null);

                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }

                                JSONObject paymentmethod=jsonObject.getJSONObject("payment");
                                userOrders.setPayment_method(paymentmethod.getJSONArray("additional_information").get(0)+"");
                                //getting product detail
                                JSONArray products = jsonObject.getJSONArray("items");
                                //int prod_count=products.getInt("count");
                                order_products = new ArrayList<OrderedProduct>();
                                for(int j=0;j<products.length();j++){
                                    JSONObject product = products.getJSONObject(j);
                                    OrderedProduct orderedProduct = new OrderedProduct();
                                    orderedProduct.setProduct_name(product.getString("name"));
                                  orderedProduct.setProduct_image("http://mofluid2.ebizontech.biz"
                                  );
                                    orderedProduct.setProduct_unit_price(product.getString("base_price"));
                                    orderedProduct.setProduct_quantity(product.getString("qty_ordered"));
                                    orderedProduct.setProduct_id(product.getString("order_id"));
                                    orderedProduct.setSku(product.getString("sku"));
                                    order_products.add(orderedProduct);
                                }
                                userOrders.setOrderedProducts(order_products);
                                complete_order.add(userOrders);
                            }
                        } // end of total condition

                        if(complete_order!=null) {
                            if(complete_order.size()>=3)
                            {
                                for(int i=0;i<=2;i++)
                                {
                                    complete_order_small.add(complete_order.get(i));

                                }
                                my_order.setAdapter(new RecyclerAdapter(complete_order_small,context,complete_order));
                                my_order.setNestedScrollingEnabled(false);
                                LinearLayoutManager layoutManager
                                        = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                my_order.setLayoutManager(layoutManager);
                                showdata();
//
                            }else{
                                my_order.setAdapter(new RecyclerAdapter(complete_order_small,context,complete_order));
                                my_order.setNestedScrollingEnabled(false);
                                LinearLayoutManager layoutManager
                                        = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                                my_order.setLayoutManager(layoutManager);
                                my_order.setHasFixedSize(true);

                                showdata();
//
                            }

//                            Log.d("DIALOG","4");
//                            pDialog.cancel();
                            if(complete_order.size()==0)
                            {
                                setnavigatetext();
                                my_order.setVisibility(View.GONE);
                            }
                        }
                        Log.d("DIALOG","10");
                        showdata();

                    } catch (JSONException e) {
                        showdata();
                        setnavigatetext();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    pDialog.cancel();
                    showdata();
                    Log.d("DIALOG","5");
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            hitserviceformydownloads();
            super.onPostExecute(s);
        }
    }

    private void showdata() {
        pDialog.cancel();
        rl_profile_name_edit.setVisibility(View.VISIBLE);
        rl_profile_basic_info.setVisibility(View.VISIBLE);
        rl_profile_tab_options.setVisibility(View.VISIBLE);
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
