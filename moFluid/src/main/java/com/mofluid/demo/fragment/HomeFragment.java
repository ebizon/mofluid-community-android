package com.mofluid.magento2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.MyOnClickListener;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.mofluid.utility_new.WishListManager;
import com.ebizon.fluid.model.BannerItem;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.ShoppingItem;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.demo.adapter.ProductAdapter;
import com.mofluid.magento2.EncodeString;
import com.mofluid.magento2.GetBannerDetails;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.BannerAdapter;
import com.mofluid.magento2.custome.widget.ShowAlertDialogBox;
import com.mofluid.magento2.service.ConnectionDetector;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements ProductAdapter.RecyclerViewClickListener {
    public static String userEmail;
    public static ProgressDialog pDialog;
    private final String TAG = "HomeFragment";
    //New product list
    private RecyclerView newProductRecyclerView;
    private ProductAdapter newProductAdapter;
    private List<ShoppingItem> newProductListDetails;
    //Best seller list
    private RecyclerView bestSellerProductRecyclerView;
    private ProductAdapter bestSellerProductAdapter;
    private List<ShoppingItem> bestSellerListDetails;
    //Featured product list
    private RecyclerView featureProductRecyclerView;
    private ProductAdapter featureProductAdapter;
    private List<ShoppingItem> featuredProductListDetails;

    private ViewPager mPager;
    private CirclePageIndicator indicator;
    private Handler handler;
    private int slideImgCounter;
    private boolean reverseFlag;
    private ArrayList<BannerItem> bannerListData;
    private BannerAdapter adapter;
    private ConnectionDetector cd;
    private TextView tvNewFeature, tvNewProduct;
    private LinearLayout newPRoduct_Container, bestseller_Container, featureProduct_Container;
    private TextView tvBestSeller;
    private Runnable runnable;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    /**
     * This event fires 1st, before creation of fragment or any views
     * The onAttach method is called when the Fragment instance is associated with an Activity.
     * This does not mean the Activity is fully initialized.
     **/
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

    /**
     * This event fires 2nd, before views are created for the fragment
     * The onCreate method is called when the Fragment instance is being created, or re-created.
     * Use onCreate for any standard setup that does not require the activity to be fully created
     **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        bannerListData = new ArrayList<BannerItem>();
        featuredProductListDetails = new ArrayList<ShoppingItem>();
        newProductListDetails = new ArrayList<ShoppingItem>();
        bestSellerListDetails = new ArrayList<ShoppingItem>();
        cd = new ConnectionDetector(getActivity());
        pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
    }

    /**
     * The onCreateView method is called when Fragment should create its View object hierarchy,
     **/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    /**
     * This event is triggered soon after onCreateView().
     * onViewCreated() is only called if the view returned from onCreateView() is non-null.
     * Any view setup should occur here.  E.g., view lookups and attaching view listeners.
     **/
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //view lookups
        initViews(view);
        Bundle bundle = this.getArguments();
        String myValue = null;
        if (bundle != null)
            myValue = bundle.getString("storeDetails");
        setdataToNewProduct();
        setdatatoBestSeller();
        setdataToFeatureProduct();
        //setViewPager();
        hitService(myValue);
        hitServiceForFeatruredProducts();
        hitserviceforNewProducts();
        hitserviceforBestsellers();
        setemailifalreaylogin();
        if (!cd.isConnectingToInternet()) {
            Activity activity = getActivity();
            if (isAdded() && activity != null)
                new ShowAlertDialogBox().showCustomeDialogBox(getActivity(), getActivity().getResources().getString(R.string.internet_connection), getActivity().getResources().getString(R.string.internet_not_avalable));
        }

        moveBannerImages();

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageSelected(int arg0) {
                slideImgCounter = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                //Log.d(TAG, "onPageScrolled "+arg0);
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                //Log.d(TAG, "onPageScrollStateChanged "+arg0);

            }
        });
    }

    /**
     * Function to initialize views and set basic properties to them
     */
    private void initViews(View rootView) {

        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        newPRoduct_Container = (LinearLayout) rootView.findViewById(R.id.newproduct_container);
        bestseller_Container = (LinearLayout) rootView.findViewById(R.id.bestseller_container);
        featureProduct_Container = (LinearLayout) rootView.findViewById(R.id.fl_feature_product_container);
        indicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        //RecyclerView for showing Featured product
        featureProductRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_feature_product);
        featureProductAdapter = new ProductAdapter(null, this);
        featureProductRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        featureProductRecyclerView.setAdapter(featureProductAdapter);
        //RecyclerView for showing New Product
        newProductRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_new_product);
        newProductAdapter = new ProductAdapter(null, this);
        newProductRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        newProductRecyclerView.setAdapter(newProductAdapter);
        //RecyclerView for showing Best Seller Product
        bestSellerProductRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_best_seller_product);
        bestSellerProductAdapter = new ProductAdapter(null, this);
        bestSellerProductRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        bestSellerProductRecyclerView.setAdapter(bestSellerProductAdapter);

        tvNewFeature = (TextView) rootView.findViewById(R.id.tvNewFeature);
        tvNewProduct = (TextView) rootView.findViewById(R.id.tvNewProduct);
        tvBestSeller = (TextView) rootView.findViewById(R.id.tvBestSeller);

        Typeface font_type = Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.HELVETICA_FONT_STYLE);

        tvNewFeature.setTypeface(font_type);
        tvNewProduct.setTypeface(font_type);
        tvBestSeller.setTypeface(font_type);
    }

    /**
     * onStart() is called once the fragment is ready to be displayed on screen.
     */
    @Override
    public void onStart() {
        super.onStart();
        mListener.onFragmentMessage(ConstantDataMember.SET_IMAGE_TAG, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }

    /**
     * This method is called when the fragment is no longer connected to the Activity
     * Any references saved in onAttach should be nulled out here to prevent memory leaks.
     */
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

    /**
     * Called when, an item in RecyclerView is clicked
     */
    @Override
    public void onClick(ShoppingItem item) {
        if (item == null) return;
        // FIXME: 8/4/2018 Extract this functionality in fragment itself
        BaseFragment frmnt;
        if (item.getType().equalsIgnoreCase("simple"))
            frmnt = new SimpleProductFragment2();
        else if (item.getType().equalsIgnoreCase("grouped"))
            frmnt = new GroupedProductDetailFragment();
        else
            frmnt = new SimpleProductFragment2();
        Bundle bundle = new Bundle();
        bundle.putString(ConstantDataMember.PRO_ID, item.getId());
        bundle.putString(ConstantDataMember.PRO_NAME, item.getName());
        //ImageView img = (ImageView)view.findViewById(R.id.item_image);
        //SimpleProductFragment.imageDrawable=img.getDrawable();
        frmnt.setArguments(bundle);
        callFragment(frmnt, frmnt.getClass().getSimpleName());
    }


    private void hitserviceforNewProducts() {
        String urlNewProduct = WebApiManager.getInstance().getNewProductsURL(getActivity());
        Log.d(TAG, "hitserviceforNewProducts() called = " + urlNewProduct);
        NewProductService newproduct_Service = new NewProductService();
        newproduct_Service.execute(urlNewProduct);
    }

    private void setdatatoBestSeller() {
        if (bestSellerListDetails != null) {
            bestSellerProductAdapter.setData(bestSellerListDetails);
        }

    }

    private void hitserviceforBestsellers() {

        String urlBestSellerProducts = WebApiManager.getInstance().getBestSellerURL(getActivity());
        Log.d(TAG, "hitserviceforBestsellers() called = " + urlBestSellerProducts);
        BestSellerService bestseller = new BestSellerService();
        bestseller.execute(urlBestSellerProducts);


    }

    private void setemailifalreaylogin() {
        SharedPreferences preferences = getActivity().getSharedPreferences("lockapprating", 0);
        userEmail = preferences.getString("emailID", null);

    }


    private void hitServiceForFeatruredProducts() {

        String urlFeatureProducts = WebApiManager.getInstance().getFeatureProductsURL(getActivity());
        Log.d(TAG, "hitServiceForFeatruredProducts() called = " + urlFeatureProducts);
        FeaturedProductService feature_service = new FeaturedProductService();
        feature_service.execute(urlFeatureProducts);
    }

    private ArrayList<ShoppingItem> parseProducts(JSONArray jsonArray) {
        ArrayList<ShoppingItem> items = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                ShoppingItem item = ShoppingItem.create(jsonArray.getJSONObject(i));
                if (item != null) {
                    items.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return items;
    }

    private void hitService(String myValue) {
        if (myValue == null) {
            String url = WebApiManager.getInstance().getStoreDetailsURL(getActivity());
            pDialog.show();

            StoreDetailService storeService = new StoreDetailService();
            storeService.execute(url);
        } else
            parseStoreDetailsResponse(myValue);
    }

    private void parseStoreDetailsResponse(String response) {
        JSONArray imageJSNArray = null;
        JSONObject themeJSNobj = null;
        JSONObject bannerJSNobj = null;
        JSONObject strJSNobj = null;
        Log.d(TAG, response);
        Log.d("Volley= ", response);
        if (pDialog.isShowing())
            pDialog.hide();
        try {
            strJSNobj = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            themeJSNobj = strJSNobj.getJSONObject("theme");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            bannerJSNobj = themeJSNobj.getJSONObject("banner");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            imageJSNArray = bannerJSNobj.getJSONArray("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bannerListData = new GetBannerDetails().getBannerDetails(imageJSNArray);

        Log.d("Volley Size ", bannerListData.size() + "");
        //adapter.notifyDataSetChanged();
        setViewPager();

    }

    private void moveBannerImages() {
        //   Log.d("HomeFragment", "Enter in Thread Handler");
        runnable = new Runnable() {

            public void run() {
                // Log.d("HomeFragment", "Running in Thread Handler");
                slideBanner();
                handler.postDelayed(runnable, 3000);
            }
        };
        runnable.run();
    }

    private void slideBanner() {
        if (slideImgCounter == bannerListData.size() - 1) {
            reverseFlag = true;
        }

        if (slideImgCounter == 0)
            reverseFlag = false;
        mPager.setCurrentItem(slideImgCounter, true);
        if (mPager.getAdapter() != null) {
            indicator.setViewPager(mPager);
        }
//        indicator.setCurrentItem(slideImgCounter);
        if (reverseFlag)
            slideImgCounter = 0; // -- if in reverse order
        else
            slideImgCounter++;
    }

    private void setViewPager() {
        final ArrayList<BannerItem> bannerListData = this.bannerListData;
        adapter = new BannerAdapter(bannerListData, new MyOnClickListener() {
            @Override
            public void myOnClick(View view, String tag, int position) {
                String action = bannerListData.get(position).getMofluid_image_action();
                String dataTemp = EncodeString.decodeStrBase64Bit(action);
                try {
                    JSONObject jsonObject = new JSONObject(dataTemp);
                    String act = jsonObject.getString("action");
                    String base = jsonObject.getString("base");
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    if (act.equalsIgnoreCase("open") && base.equalsIgnoreCase("category")) {
                        BaseFragment fragment = new ProductDetailListFragment(name);
                        Bundle subCategoryBundle = new Bundle();
                        subCategoryBundle.putString(MainActivity.KEY_CATEGORY_ID, id);
                        fragment.setArguments(subCategoryBundle);
                        callFragment(fragment, "ProductDetailListFragment");
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        mPager.setAdapter(adapter);
        mPager.setCurrentItem(0, true);
        indicator.setViewPager(mPager);
        Activity activity = getActivity();

        if (isAdded() && activity != null) {
            final float density = getResources().getDisplayMetrics().density;
            indicator.setRadius(3.2f * density);
        }

    }

    private void setdataToFeatureProduct() {
        if (featuredProductListDetails != null) {
            featureProductAdapter.setData(featuredProductListDetails);
        }
    }

    private void setdataToNewProduct() {
        if (newProductListDetails != null) {
            newProductAdapter.setData(newProductListDetails);
        }
    }


    private class FeaturedProductService extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(params[0], new Response.Listener<String>() {
                public JSONArray imageJSNArray;

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    Log.d("Volley= ", response);
                    if (pDialog.isShowing())
                        pDialog.hide();
                    try {
                        JSONObject strJSNobj = new JSONObject(response);
                        imageJSNArray = strJSNobj.getJSONArray("products_list");
                        featuredProductListDetails = parseProducts(imageJSNArray);
                        featureProduct_Container.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        featureProduct_Container.setVisibility(View.GONE);
                    }

                    //newFeatureAdapter.notifyDataSetChanged();
                    setdataToFeatureProduct();

                    Log.d("Volley Size ", featuredProductListDetails.size() + "");
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                    if (pDialog.isShowing())
                        pDialog.hide();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    private class NewProductService extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(params[0], new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    Log.d("Volley= ", response);
                    if (pDialog.isShowing())
                        pDialog.hide();
                    try {
                        JSONObject strJSNobj = new JSONObject(response);
                        JSONArray jsonArray = strJSNobj.getJSONArray("products_list");
                        newProductListDetails = parseProducts(jsonArray);
                        newPRoduct_Container.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        newPRoduct_Container.setVisibility(View.GONE);
                    }

                    //productAdapter.notifyDataSetChanged();
                    setdataToNewProduct();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: ne Product " + error.getMessage());
                    if (pDialog.isShowing())
                        pDialog.hide();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    private class StoreDetailService extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(params[0], new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    parseStoreDetailsResponse(response);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    if (pDialog.isShowing())
                        pDialog.hide();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    private class BestSellerService extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(params[0], new Response.Listener<String>() {
                public JSONArray imageJSNArray;

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    Log.d("Volley= ", response);
                    if (pDialog.isShowing())
                        pDialog.hide();
                    try {
                        JSONObject strJSNobj = new JSONObject(response);
                        imageJSNArray = strJSNobj.getJSONArray("products_list");
                        bestSellerListDetails = parseProducts(imageJSNArray);
                        bestseller_Container.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        bestseller_Container.setVisibility(View.GONE);
                    }

                    setdatatoBestSeller();

                    Log.d("Volley Size ", bestSellerListDetails.size() + "");
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                    if (pDialog.isShowing())
                        pDialog.hide();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}