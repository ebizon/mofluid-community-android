package com.mofluid.magento2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.ebizon.fluid.Utils.Config;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.FilterMyProducts;
import com.ebizon.fluid.Utils.MySharedPreferences;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.model.BannerItem;
import com.ebizon.fluid.model.LogoHeaderItem;
import com.ebizon.fluid.model.MultiCurrencyModel;
import com.ebizon.fluid.model.MultiStoreData;
import com.ebizon.fluid.model.MultiStoreView;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.SlideMenuListItem;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.WebApiManager;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mofluid.fragment_new.LangCurrecncyFragment;
import com.mofluid.fragment_new.WishListFragment;
import com.mofluid.magento2.adapter.MyFilterOptionAdapter;
import com.mofluid.magento2.adapter.SlideMenuAdapter;
import com.mofluid.magento2.custome.widget.CustomeExpandablelListView;
import com.mofluid.magento2.custome.widget.ShowAlertDialogBox;
import com.mofluid.magento2.database.MyDataBaseAdapter;
import com.mofluid.magento2.downlod.images.ImageLoader;
import com.mofluid.magento2.fragment.BaseFragment;
import com.mofluid.magento2.fragment.CmsFragment;
import com.mofluid.magento2.fragment.HomeFragment;
import com.mofluid.magento2.fragment.MultiCurrencyFragment;
import com.mofluid.magento2.fragment.MyCartFragment;
import com.mofluid.magento2.fragment.OrderAcknowledgeFragment;
import com.mofluid.magento2.fragment.ProductDetailListFragment;
import com.mofluid.magento2.fragment.SignInSignUpFragment;
import com.mofluid.magento2.fragment.SimpleProductFragment2;
import com.mofluid.magento2.fragment.WelcomeFragment2;
import com.mofluid.magento2.manager.FilterManager;
import com.mofluid.magento2.service.AppController;
import com.mofluid.magento2.service.ConnectionDetector;
import com.mofluid.utility_new.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements OnClickListener, OnFragmentInteractionListener {
    public static final String KEY_CATEGORY_ID = "CategoryId";
    public static final String KEY_SUB_CATEGORY_ID = "subCategoryId";
    public static final String KEY_SEARCH_TEXT = "SearchTextKey";
    public static final String MULTI_STORE = "MultiStore";
    public static final String STORE_ID = "StoreID";
    private static final String GOOGLE_PLAY_PACKAGE_NAME = "com.android.vending";
    public static MainActivity INSTANCE = null;
    public static String qrtext = null;
    public static TextView product_name;
    public static String searchText = null; //Temp : fix it
    public static int viewId = 0; //Temp : fix it
    public static ImageView ivAppLogo;
    public static ImageView ivBackMenu;
    public static ArrayList<BannerItem> bannerListData;
    public static RequestQueue queue;
    public static List<SlideMenuListItem> childListForAllProductListFragment;
    public static TextView txtV_item_counter;
    public static CallbackManager mCallbackManager;
    public static String cms_about_us_id = "0";
    public static String cms_term_con_id = "0";
    public static String cms_privacy_id = "0";
    public static String cms_r_privacy_policy = "0";
    public static String currency_code = "$";
    public static boolean iswishlist = false;
    static PopupMenu popupMenu;
    private static int MY_SOCKET_TIMEOUT_MS = 10000000;
    private static ArrayList<LogoHeaderItem> logoListData;
    private static ImageView home;
    public Activity activity = MainActivity.this;
    public Context con = this;
    String id, currency, language;
    ProgressDialog pDialog;
    boolean shouldshow;
    LinearLayout overflowmenu;
    List<SlideMenuListItem> childListData;
    private String STR_ALL = "";
    private String GOOGLE_PLAY = "https://play.google.com/store/apps/details?id=";
    private Locale myLocale;
    private Typeface font_type_header;
    private DrawerLayout mDrawerLayout;
    private CustomeExpandablelListView expListView;
    private LinearLayout footer;
    private TextView exit_demo;

    private ArrayList<MultiCurrencyModel> currenciesList;
    private boolean isFromcurrency;
    private boolean isMultipleCurrency = false;
    private List<SlideMenuListItem> parentListData;
    private HashMap<String, List<SlideMenuListItem>> slideMenuListData;
    private String TAG;
    private SlideMenuAdapter slideMenuAdapter;
    private ConnectionDetector cd;
    private LinearLayout ll_home;
    private ImageLoader imageLoader;
    private Fragment fragment;
    private MyDataBaseAdapter dbAdapter;
    private SharedPreferences mySharedPreference, mySharedPrefPush;
    private TextView foot_home, foot_search, foot_user, foot_cart;
    private Typeface tf_foot_text;
    private boolean doubleBackToExitPressedOnce = false;
    private int multi_store_position;
    private ArrayList<MultiStoreData> storelist;
    private ArrayList<MultiStoreView> view_list;
    private AlertDialog.Builder builder;
    private MyDataBaseAdapter dataBaseAdapter = new MyDataBaseAdapter(AppController.getContext());

    //Display brand logo on app bar
    private ImageView brandLogoImageView;
    //Display product name or page title in app bar
    private TextView headerTextView;
    //footer
    private BottomNavigationView footerBNV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAndGetIntentData();
        // CartSync.getInstance().setContext(this); // to set context for both anonymous and logged in user carts
        setAppLanguage();
        Log.d("PiyushK", "value get is " + Config.getInstance().getStoreValue());
        String fontPath = "fonts/Shadow Boxing.ttf";
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //Setting up app bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Registering above app bar as ActionBar
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowTitleEnabled(false);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_material_menu);
        }


        initViews();
        initialized();
        //checkPushNotifyEnabled();
        setFontStyle();
        UserSession.setSession(MainActivity.this);

        setCounterItemAddedCartfromDB();
        // TODO: 08/08/18 Remove below line 
        home.setOnClickListener(homeOnclickListener);

        Typeface tf = Typeface.createFromAsset(this.getAssets(), fontPath);

        hitservicetocreateToken();
        setUpDrawer();
        slideMenuAdapter = new SlideMenuAdapter(MainActivity.this, parentListData, slideMenuListData);
        expListView.setAdapter(slideMenuAdapter);

        ivBackMenu.setVisibility(View.GONE);

        ivBackMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                closingDrawer();
                Fragment visiBleFragment = MainActivity.this.getFragmentManager().findFragmentById(R.id.content_frame);
                if (visiBleFragment instanceof OrderAcknowledgeFragment) {
                    FragmentManager fm = MainActivity.this.getFragmentManager();
//            fm.popBackStackImmediate();
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    try {
                        if (visiBleFragment instanceof ProductDetailListFragment) {
                            FilterManager.getInstance().previousCheckBox = null;
                            MyFilterOptionAdapter.resetAllChecked(FilterMyProducts.updateOptionList);
                            if (FilterMyProducts.updateOptionList != null && FilterMyProducts.updateOptionList.size() > 0) {
                                MyFilterOptionAdapter.resetAllChecked(FilterMyProducts.updateOptionList);
                            }
                        }
                        popCurrentFragment();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        //Listen to fragment stack, and change HomeAsUp icon accordingly
        getFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        int stackCount = getFragmentManager().getBackStackEntryCount();
                        if (stackCount != 0) {
                            //Not on HomeFragment, set back button
                            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
                        } else {
                            //On HomeFragment, set hamburger icon for drawer navigation
                            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_material_menu);
                        }
                    }
                });

        footerBNV.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                actOnClick(R.id.ll_home);
                                return true;
                            case R.id.action_search:
                                actOnClick(R.id.ll_search);
                                return true;
                            case R.id.action_user:
                                actOnClick(R.id.ll_sign_in_sign_up);
                                return true;
                            case R.id.action_cart:
                                actOnClick(R.id.ll_my_cart);
                                return true;
                        }
                        return false;
                    }
                });
        addFCMRegisterListner();
    }

    private void initialized() {
        INSTANCE = MainActivity.this;
        TAG = getClass().getSimpleName();
        queue = Volley.newRequestQueue(this);
        logoListData = new ArrayList<LogoHeaderItem>();
        overflowmenu = (LinearLayout) findViewById(R.id.overflow);
        overflowmenu.setOnClickListener(new OnAlbumOverflowSelectedListener());
        popupMenu = new PopupMenu(MainActivity.this, overflowmenu);
        popupMenu.inflate(R.menu.overflow_menu);
        imageLoader = new ImageLoader(getApplicationContext());
        cd = new ConnectionDetector(getApplicationContext());
        bannerListData = new ArrayList<BannerItem>();
        logoListData = new ArrayList<LogoHeaderItem>();
        parentListData = new ArrayList<SlideMenuListItem>();
        slideMenuListData = new HashMap<String, List<SlideMenuListItem>>();
        mCallbackManager = CallbackManager.Factory.create();
        mySharedPreference = getSharedPreferences(ConstantDataMember.USER_INFO_SESSION, Context.MODE_PRIVATE);
        mySharedPrefPush = getSharedPreferences("pushenabled", Context.MODE_PRIVATE);
        multi_store_position = 1;
        storelist = new ArrayList<>();
        view_list = new ArrayList<>();
        childListData = new ArrayList<>();
        pDialog = new ProgressDialog(MainActivity.this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

    }

    /**
     * initialize views
     */
    private void initViews() {
        font_type_header = Typeface.createFromAsset(this.getAssets(), ConstantDataMember.HELVETICA_FONT_STYLE);
        // exit_demo=(TextView) findViewById(R.id.exit_demo);
        product_name = (TextView) findViewById(R.id.product_name);
        product_name.setTypeface(font_type_header);
        product_name.setVisibility(View.INVISIBLE);
        //headerTextView configs
        // TODO: 03/08/18 Extract the style of font in styles.xml
        headerTextView = (TextView) findViewById(R.id.appbar_title);
        headerTextView.setTypeface(font_type_header);
        headerTextView.setVisibility(View.INVISIBLE);

        home = (ImageView) findViewById(R.id.home);
        ivBackMenu = (ImageView) findViewById(R.id.ivBackMenu);
        ivAppLogo = (ImageView) findViewById(R.id.ivAppLogo);
        brandLogoImageView = (ImageView) findViewById(R.id.toolbar_logo); //#//
        LinearLayout ll_header = (LinearLayout) findViewById(R.id.ll_header);
        ImageView iv_sign_in_sign_up = (ImageView) findViewById(R.id.iv_sign_in_sign_up);
        iv_sign_in_sign_up.setOnClickListener(this);
        LinearLayout ll_my_cart = (LinearLayout) findViewById(R.id.ll_my_cart);
        ll_my_cart.setOnClickListener(this);

        txtV_item_counter = (TextView) findViewById(R.id.txtV_item_counter);
        ll_home = (LinearLayout) findViewById(R.id.ll_home);
        ll_home.setOnClickListener(this);

        LinearLayout ll_sign_in_sign_up = (LinearLayout) findViewById(R.id.ll_sign_in_sign_up);
        ll_sign_in_sign_up.setOnClickListener(this);

        LinearLayout linearLayoutSearch = (LinearLayout) findViewById(R.id.ll_search);
        linearLayoutSearch.setOnClickListener(this);

        ImageView imageViewSearch = (ImageView) findViewById(R.id.iv_search);
        imageViewSearch.setOnClickListener(this);
        setFooterFOntStyle();
        footer = (LinearLayout) findViewById(R.id.footer_layout);
        //footer
        footerBNV = (BottomNavigationView) findViewById(R.id.bottom_navigation);
    }

    /**
     * Inflate option menu to be shown in app bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return true;
    }

    /**
     * Show/Hide action items depending on values from backend
     */
    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        //Close Navigation drawer if opened, when user has clicked on option menu
        closingDrawer();
        //*//
        MenuItem aboutItem = menu.findItem(R.id.about_us);
        MenuItem termsConditionsItem = menu.findItem(R.id.term_conditions);
        MenuItem privacyItem = menu.findItem(R.id.privacy_policy);
        MenuItem returnPrivacyPolicyItem = menu.findItem(R.id.return_privacy_policy);
        MenuItem changeCurrencyItem = menu.findItem(R.id.change_currency);

        if (Integer.parseInt(cms_about_us_id) != 0)
            aboutItem.setVisible(true);
        if (Integer.parseInt(cms_term_con_id) != 0)
            termsConditionsItem.setVisible(true);
        if (Integer.parseInt(cms_privacy_id) != 0)
            privacyItem.setVisible(true);
        if (Integer.parseInt(cms_r_privacy_policy) != 0)
            returnPrivacyPolicyItem.setVisible(true);
        if (isMultipleCurrency)
            changeCurrencyItem.setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }


    /**
     * Perform action depending upon which action item is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment visiBleFragment = MainActivity.this.getFragmentManager().findFragmentById(R.id.content_frame);
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    //On HomeFragment, open or close navigation drawer
                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                        mDrawerLayout.openDrawer(GravityCompat.START);
                    }
                } else {
                    //Not on HomeFragment, pop the fragment from stack
                    onBackPressed();
                }
                return true;
            case R.id.apprate:
                Uri link = Uri.parse(GOOGLE_PLAY + getApplicationContext().getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, link);
                if (isPackageExist(getApplicationContext(), GOOGLE_PLAY_PACKAGE_NAME)) {
                    intent.setPackage(GOOGLE_PLAY_PACKAGE_NAME);
                }
                startActivity(intent);
                return true;
            case R.id.about_us:
                if (!(visiBleFragment instanceof CmsFragment)) {
                    Fragment cmsfragment = new CmsFragment(cms_about_us_id);
                    callFragment(cmsfragment, "CmsFragment");
                }
                return true;
            case R.id.term_conditions:
                if (!(visiBleFragment instanceof CmsFragment)) {
                    Fragment term = new CmsFragment(cms_term_con_id);
                    callFragment(term, "CmsFragment");
                }
                return true;
            case R.id.privacy_policy:
                if (!(visiBleFragment instanceof CmsFragment)) {
                    Fragment privacy = new CmsFragment(cms_privacy_id);
                    callFragment(privacy, "CmsFragment");
                }
                return true;
            case R.id.return_privacy_policy:
                if (!(visiBleFragment instanceof CmsFragment)) {
                    Fragment r_privacy = new CmsFragment(cms_r_privacy_policy);
                    callFragment(r_privacy, "CmsFragment");
                }
                return true;
            case R.id.change_currency:
                if (!(visiBleFragment instanceof MultiCurrencyFragment)) {
                    Fragment choose_store = new MultiCurrencyFragment(currenciesList);
                    callFragment(choose_store, "MultiCurrencyFragment");
                }
                return true;
            case R.id.select_currency:
                LangCurrecncyFragment currency=new LangCurrecncyFragment();
                currency.setView(currency.CURRENCY_VIEW);
                callFragment(currency,"LangCurrecncyFragment");
                return  true;
            case R.id.select_lang:
                LangCurrecncyFragment lang=new LangCurrecncyFragment();
                lang.setView(lang.CURRENCY_VIEW);
                callFragment(lang,"LangCurrecncyFragment");
                return  true;
            case R.id.wishlist:
                if (!(visiBleFragment instanceof WishListFragment)) showWishlist();
                return true;
            case R.id.menu_home:
                actOnClick(R.id.ll_home);
                return true;
            case R.id.menu_account:
                actOnClick(R.id.ll_sign_in_sign_up);
                return true;
            case R.id.menu_cart:
                actOnClick(R.id.ll_my_cart);
                return true;
            case R.id.menu_search:
                actOnClick(R.id.ll_search);
                return true;
            /* case R.id.refresh:
                Intent restart = new Intent(MainActivity.this, MainActivity.class);
                getIntentData();
                if (!Validation.isNull(id, currency, language)) {
                    restart.putExtra("StoreID", id);
                    restart.putExtra("Currency", currency);
                    restart.putExtra("Language", language);
                }
                startActivity(restart);
                MainActivity.this.finish();
                return true;*/
             /*case R.id.choosestore:
                if(visiBleFragment instanceof MultiStoreFragment);
                else {
                    Fragment choose_store = new MultiStoreFragment(storelist);
                    callFragment(choose_store, "MultiStoreFragment");
                }
                return true;*/

        }

        return super.onOptionsItemSelected(item);
    }


    // TODO: 06/08/18 Implemented this, now delete this function 
    public class OnAlbumOverflowSelectedListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            closingDrawer();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                Fragment visiBleFragment = MainActivity.this.getFragmentManager().findFragmentById(R.id.content_frame);

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                       /* case R.id.refresh:
                            Intent restart = new Intent(MainActivity.this, MainActivity.class);
                            getIntentData();
                            if (!Validation.isNull(id, currency, language)) {
                                restart.putExtra("StoreID", id);
                                restart.putExtra("Currency", currency);
                                restart.putExtra("Language", language);
                            }
                            startActivity(restart);
                            MainActivity.this.finish();
                            return true;*/
                        case R.id.apprate:
                            /*final Intent intentToAppstore = hotchemi.android.rate.IntentHelper.createIntentForGooglePlay(getApplicationContext());
                            getApplicationContext().startActivity(intentToAppstore);*/
                            Uri link = Uri.parse(GOOGLE_PLAY + getApplicationContext().getPackageName());
                            Intent intent = new Intent(Intent.ACTION_VIEW, link);
                            if (isPackageExist(getApplicationContext(), GOOGLE_PLAY_PACKAGE_NAME)) {
                                intent.setPackage(GOOGLE_PLAY_PACKAGE_NAME);
                            }
                            startActivity(intent);
                            return true;
                        case R.id.about_us:
                            if(visiBleFragment instanceof CmsFragment);
                            else{
                                Fragment cmsfragment = new CmsFragment(cms_about_us_id);
                                callFragment(cmsfragment, "CmsFragment");
                            }
                            return true;
                        case R.id.term_conditions:
                            if(visiBleFragment instanceof CmsFragment);
                            else{
                                Fragment term = new CmsFragment(cms_term_con_id);
                                callFragment(term, "CmsFragment");}
                            return true;
                        case R.id.privacy_policy:
                            if(visiBleFragment instanceof CmsFragment);
                            else {
                                Fragment privacy = new CmsFragment(cms_privacy_id);
                                callFragment(privacy, "CmsFragment");
                            }
                            return true;
                        case R.id.return_privacy_policy:
                            if(visiBleFragment instanceof CmsFragment);
                            else {
                                Fragment r_privacy = new CmsFragment(cms_r_privacy_policy);
                                callFragment(r_privacy, "CmsFragment");
                            }
                            return true;
                        /*case R.id.choosestore:
                            if(visiBleFragment instanceof MultiStoreFragment);
                            else {
                                Fragment choose_store = new MultiStoreFragment(storelist);
                                callFragment(choose_store, "MultiStoreFragment");
                            }
                            return true;*/
                        case R.id.change_currency:
                            if (visiBleFragment instanceof MultiCurrencyFragment) ;
                            else {
                                Fragment choose_store = new MultiCurrencyFragment(currenciesList);
                                callFragment(choose_store, "MultiCurrencyFragment");
                            }
                            return true;
                        case R.id.menu_home:
                            actOnClick(R.id.ll_home);
                            return true;
                        case R.id.menu_account:
                            actOnClick(R.id.ll_sign_in_sign_up);
                            return true;
                        case R.id.menu_cart:
                            actOnClick(R.id.ll_my_cart);
                            return true;
                        case R.id.menu_search:
                            actOnClick(R.id.ll_search);
                            return true;
                        case R.id.wishlist:
                            if(visiBleFragment instanceof WishListFragment);
                            else
                                showWishlist();
                            return true;
                        case R.id.select_currency:
                            LangCurrecncyFragment currency=new LangCurrecncyFragment();
                            currency.setView(currency.CURRENCY_VIEW);
                            callFragment(currency,"LangCurrecncyFragment");
                            return  true;
                        case R.id.select_lang:
                            LangCurrecncyFragment lang=new LangCurrecncyFragment();
                            lang.setView(lang.CURRENCY_VIEW);
                            callFragment(lang,"LangCurrecncyFragment");
                            return  true;
                        default:
                            return false;
                    }
                }
            });
//            if(popupMenu.getMenu().size()==0)
//            popupMenu.inflate(R.menu.overflow_menu);
            Menu menu = popupMenu.getMenu();
            MenuItem about = (MenuItem) menu.findItem(R.id.about_us);
            MenuItem term_conditions = (MenuItem) menu.findItem(R.id.term_conditions);
            MenuItem privacy = (MenuItem) menu.findItem(R.id.privacy_policy);
            MenuItem r_privacy = (MenuItem) menu.findItem(R.id.return_privacy_policy);
            MenuItem r_mul_currency = (MenuItem) menu.findItem(R.id.change_currency);
            //store_view = (MenuItem) menu.findItem(R.id.choosestore);
            if (Integer.parseInt(cms_about_us_id) != 0)
                about.setVisible(true);
            if (Integer.parseInt(cms_term_con_id) != 0)
                term_conditions.setVisible(true);
            if (Integer.parseInt(cms_privacy_id) != 0)
                privacy.setVisible(true);
            if (Integer.parseInt(cms_r_privacy_policy) != 0)
                r_privacy.setVisible(true);
            if (Integer.parseInt(cms_r_privacy_policy) != 0)
                r_privacy.setVisible(true);
            if (!isMultipleCurrency)
                r_mul_currency.setVisible(false);
            else
                r_mul_currency.setVisible(true);

            popupMenu.show();

        }
    }


    // TODO: 06/08/18 Implemented this, close this now
    private final View.OnClickListener homeOnclickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            HideKeyBoard.hideSoftKeyboard(MainActivity.this);
            if (mDrawerLayout.isDrawerOpen(expListView))
                mDrawerLayout.closeDrawer(expListView);
            else
                mDrawerLayout.openDrawer(expListView);
        }
    };


    public static void retryRequest(StringRequest strRetryRequest) {
        strRetryRequest.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    /**
     * Hide image on toolbar and show text in its place
     */
    private void setAppbarText(String text) {
        // TODO: 06/08/18 Removed first three lines
        ivAppLogo.setVisibility(View.INVISIBLE);
        product_name.setVisibility(View.VISIBLE);
        product_name.setText(text);
        //#//
        brandLogoImageView.setVisibility(View.GONE);
        headerTextView.setVisibility(View.VISIBLE);
        headerTextView.setText(text);

    }

    /**
     * Hide the text on toolbar and show image in its place
     */
    private void setAppbarImage() {
        brandLogoImageView.setVisibility(View.VISIBLE);
        headerTextView.setVisibility(View.GONE);
    }

    public static void setCounterItemAddedCart() {
        if (UserManager.getInstance().getUser() != null && UserManager.getInstance().getUser().getLogin_status() != "0") {
            ShoppingCart.getInstance().setCartFromServer();
        }
        int count = ShoppingCart.getInstance().getNumDifferentItems();
        if (count > 0) {
            txtV_item_counter.setText(String.valueOf(count));
            txtV_item_counter.setVisibility(View.VISIBLE);
        } else {
            txtV_item_counter.setVisibility(View.INVISIBLE);
        }
    }


    public void setCounterItemAddedCartfromDB() {
        dataBaseAdapter.getCartItems();
        int count = ShoppingCart.getInstance().getNumDifferentItems();
        if (count > 0) {
            txtV_item_counter.setText(String.valueOf(count));
            txtV_item_counter.setVisibility(View.VISIBLE);
        } else {
            txtV_item_counter.setVisibility(View.INVISIBLE);
        }
    }

    public static JSONArray convertJsonObjecttoArray(JSONObject json_object) {
        JSONArray convertedArray = new JSONArray();
        Iterator x = json_object.keys();
        try {
            while (x.hasNext()) {
                String key = (String) x.next();
                convertedArray.put(json_object.get(key));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedArray;


    }

    public static void showFooterOptionsMenu(boolean isShown) {
        Menu menu = popupMenu.getMenu();
        MenuItem home = (MenuItem) menu.findItem(R.id.menu_home);
        MenuItem cart = (MenuItem) menu.findItem(R.id.menu_cart);
        MenuItem search = (MenuItem) menu.findItem(R.id.menu_search);
        MenuItem account = (MenuItem) menu.findItem(R.id.menu_account);
        home.setVisible(isShown);
        cart.setVisible(isShown);
        search.setVisible(isShown);
        account.setVisible(isShown);
    }

    static boolean isPackageExist(Context context, String targetPackage) {
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage)) return true;
        }
        return false;
    }

    // TODO: 06/08/18 Redundant code, once app bar functionality implemented, remove it
    public static void showDrawerOption(boolean toShow) {
        if (toShow)
            home.setVisibility(View.VISIBLE);
        else
            home.setVisibility(View.GONE);
    }


    private void gotoLoginDemo() {
        Intent intent = new Intent(MainActivity.this, LoginDemo.class);
        startActivity(intent);
        finish();
    }

    private void hitservicetocreateToken() {
        pDialog.show();
        NetworkAPIManager.getInstance(this).setDeviceID(Utils.fetchDeviceID(this));
        String finalUrl = WebApiManager.getInstance().getTokenFromServer(this);
        finalUrl = String.format(finalUrl, Utils.fetchDeviceID(this));
        Log.d(TAG, "Service to fetch token called with final URL :" + finalUrl);
        GetTokenKey token = new GetTokenKey();
        token.execute(finalUrl);

    }

    private void setAppLanguage() {
        String lang = Config.getInstance().getLanguage();
        if (lang.equals("ar"))
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        else
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    private void getIntentData() {
        id = getIntent().getStringExtra("StoreID");
        currency = getIntent().getStringExtra("Currency");
        language = getIntent().getStringExtra("Language");
        String currency_code_temp = getIntent().getStringExtra("CurrSymbol");
        if (currency_code_temp != null)
            currency_code = currency_code_temp;
        isFromcurrency = getIntent().getBooleanExtra("isFromcurrency", false);
    }

    private void checkAndGetIntentData() {
        getIntentData();
        Config.getInstance().load(MainActivity.this);
        if (id != null) {
            Config.getInstance().setStoreValue(id);
        }
        if (currency != null) {
            Config.getInstance().setCurrencyCode(currency);
        }
        if (currency_code != null && !currency_code.equalsIgnoreCase("null")) {
            Config.getInstance().setCurrency_symbol(currency_code);
        } else {
            Config.getInstance().setCurrency_symbol(currency);
        }
        if (language != null) {
            boolean check = checkLanguage(language);
            if (check == false)
                language = Config.getInstance().getLanguage();
            Config.getInstance().setLanguage(language);
        }
        if (language == null) {
            language = Config.getInstance().getLanguage();
            Config.getInstance().setLanguage(language);
        }
    }

    private boolean checkLanguage(String inputString) {
        String[] items = new String[4];
        items[0] = "en";
        items[1] = "fr";
        items[2] = "de";
        items[3] = "ar";

        for (int i = 0; i < items.length; i++) {
            if (inputString.contains(items[i])) {
                return true;
            }
        }
        return false;

    }

    private void setFontStyle() {
        Typeface lat0_Font_ttf = Typeface.createFromAsset(getAssets(), ConstantDataMember.REGULAR_FONT_STYLE);

        txtV_item_counter.setTypeface(lat0_Font_ttf);
    }

    @Override
    public void onBackPressed() {
        closingDrawer();
        Fragment visiBleFragment = MainActivity.this.getFragmentManager().findFragmentById(R.id.content_frame);
        if (visiBleFragment instanceof HomeFragment) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, fetchStringresource(R.string.back_to_exit), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else if (visiBleFragment instanceof OrderAcknowledgeFragment) {
            FragmentManager fm = this.getFragmentManager();
//            fm.popBackStackImmediate();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        /*else if(visiBleFragment instanceof WishListFragment){
            callFragment(new HomeFragment(),);
        }*/
        else if (visiBleFragment instanceof ProductDetailListFragment) {
            MyFilterOptionAdapter.resetAllChecked(FilterMyProducts.updateOptionList);
            FilterManager.getInstance().previousCheckBox = null;

            if (FilterMyProducts.updateOptionList != null && FilterMyProducts.updateOptionList.size() > 0) {
                MyFilterOptionAdapter.resetAllChecked(FilterMyProducts.updateOptionList);
            }
            try {
                popCurrentFragment();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } else {
            try {
                popCurrentFragment();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    private void popCurrentFragment() {
        HideKeyBoard.hideSoftKeyboard(MainActivity.this);
        FragmentManager fm = getFragmentManager();
        int count = fm.getBackStackEntryCount();
        if (count > 0) {
            fm.popBackStackImmediate();
        }
        if (count == 1)
            ivBackMenu.setVisibility(View.GONE);
    }

    /**
     * Pop all the fragment from stack except HomeFragment which is at bottom
     */
    private void popAllFragmentFromStack() {
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


    private void setFooterFOntStyle() {

        tf_foot_text = Typeface.createFromAsset(this.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
        foot_home = (TextView) findViewById(R.id.txt_foot_home);
        foot_home.setTypeface(tf_foot_text);
        foot_search = (TextView) findViewById(R.id.txt_foot_search);
        foot_search.setTypeface(tf_foot_text);
        foot_user = (TextView) findViewById(R.id.txt_foot_user);
        foot_user.setTypeface(tf_foot_text);
        foot_cart = (TextView) findViewById(R.id.txt_foot_cart);
        foot_cart.setTypeface(tf_foot_text);
    }

    private void callFragment(Fragment fragment, String fragmentName) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(getApplicationContext(), fragmentTransaction, new HomeFragment(), fragment, R.id.content_frame);

        //index 7 is used to slide fragment Left to right and vise versa

        fragmentTransactionExtended.addTransition(7);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransactionExtended.commit();
    }

    /**
     * Get the names and icons references to build the drawer menu...
     */
    private void setUpDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        //mDrawerLayout.setDrawerListener(mDrawerListener);
        expListView = (CustomeExpandablelListView) findViewById(R.id.lvExp);

        DisplayMetrics metrics = new DisplayMetrics();
        MainActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expListView.setIndicatorBounds(GetDipsFromPixel(550) - GetDipsFromPixel(30), GetDipsFromPixel(250) - GetDipsFromPixel(5));

        } else {
            expListView.setIndicatorBoundsRelative(GetDipsFromPixel(250) - GetDipsFromPixel(30), GetDipsFromPixel(250) - GetDipsFromPixel(5));

        }

        mDrawerLayout.closeDrawer(expListView);

        expListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String name = slideMenuListData.get(parentListData.get(groupPosition).getName()).get(childPosition).getName();

                Log.d(TAG, groupPosition + "  P & C " + childPosition);

                //Logic for multistore child
//                if (groupPosition == multi_store_position) {
//
//                    fragment = new MultiStoreViewListFragment(name);
//                    Bundle storeBundle = new Bundle();
//                    String s = String.valueOf(childPosition);
//                    Log.d("PiyushK", "value of clicked being sent is : "+ s);
//                    storeBundle.putString(STORE_ID,s );
//                    storeBundle.putParcelableArrayList(MULTI_STORE, storelist);
//                    fragment.setArguments(storeBundle);
//                    FragmentManager fm = getFragmentManager();
//                    callFragment(fragment, "MultiStoreViewListFragment");
//                    mDrawerLayout.closeDrawer(expListView);
//
//                } else
//                {
                if (slideMenuAdapter.getChild(groupPosition, childPosition) == null)
                    fragment = new ProductDetailListFragment(name);
                else
                    fragment = new ProductDetailListFragment(name);
                Bundle subCategoryBundle = new Bundle();
                subCategoryBundle.putString(KEY_SUB_CATEGORY_ID, String.valueOf(slideMenuListData.get(parentListData.get(groupPosition).getName()).get(childPosition).getId()));
                fragment.setArguments(subCategoryBundle);
                FragmentManager fm = getFragmentManager();
                callFragment(fragment, "ProductDetailListFragment");

                mDrawerLayout.closeDrawer(expListView);

//                }
                return false;
            }
        });
        /*
         * Close all expanded child List
         */
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {

				/*
				if clicked on Shop by Category then no action performed
				 */
//                Log.d("PiyushK" , " "+ slideMenuAdapter.getGroupCount());
//                multi_store_position = slideMenuAdapter.getGroupCount()-1;
//                if (groupPosition != 0)
                if (slideMenuAdapter.getChildrenCount(groupPosition) == 0) {
                    mDrawerLayout.closeDrawer(expListView);
                    fragment = new ProductDetailListFragment();
                    Bundle subCategoryBundle = new Bundle();
                    subCategoryBundle.putString(KEY_CATEGORY_ID, String.valueOf(parentListData.get(groupPosition).getId()));
                    fragment.setArguments(subCategoryBundle);
                    callFragment(fragment, "ProductDetailListFragment");
                }

                return false;
            }

        });
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
            private int lastExpandedPosition = -1;

            @Override
            public void onGroupExpand(int groupPosition) {

                //
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                    // expListView.setGroupIndicator(getResources().getDrawable(R.drawable.ic_launcher));
                }
                lastExpandedPosition = groupPosition;

            }
        });
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });
    }

    private int GetDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    /**
     * Volley library to get json by making HTTP call
     */
    private void hitServiceForAppLogo() {
        pDialog.show();
        WebApiManager.setInstance();
        String url = WebApiManager.getInstance().getStoreDetailsURL(MainActivity.this);

        Log.d("PiyushK", "first URL is [ " + url + " ]");
        AppLogoService apl = new AppLogoService();
        apl.execute(url);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String name = "SEARCH ITEMS";
        if (searchText != null) {
            Fragment fragment = new ProductDetailListFragment(name);
            Bundle searchBundle = new Bundle();
            String searchTextValue = searchText;
            searchBundle.putString(KEY_SEARCH_TEXT, searchTextValue);
            searchText = null;
            fragment.setArguments(searchBundle);

            callFragment(fragment, "ProductDetailListFragment");
        } else if (viewId > 0) {
            int id = viewId;
            viewId = 0;
            this.actOnClick(id);
        }
        if (qrtext != null) {
//            byte[] b = cz.msebera.android.httpclient.extras.Base64.decode(qrtext, android.util.Base64.DEFAULT);
//            byte[] b = Base64.decode(qrtext,Base64.DEFAULT);
            String txt = EncodeString.decodeStrBase64Bit(qrtext);
//            String txt=new String(b);
            String[] data = txt.split(",", 2);
            if (data.length == 2) {
                Fragment fragment = SimpleProductFragment2.newInstance(data[0], data[1]);
                callFragment(fragment, "SimpleProductFragment2");
            } else {

                Fragment fragment = SimpleProductFragment2.newInstance("0", "NA");
                callFragment(fragment, "SimpleProductFragment2");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    private void hitServiceForSlideMenuItem() {
        pDialog.show();

        String url = WebApiManager.getInstance().getCategoryURL(MainActivity.this);

        ServiceForSliderMenu sliderService = new ServiceForSliderMenu();
        sliderService.execute(url);


    }

    private String fetchStringresource(int stringID) {
        return getResources().getString(stringID);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "clicked on Cart img ");
        actOnClick(v.getId());
    }

    // TODO: 8/13/2018 Replace the id with action_id from bottom_menu
    private void actOnClick(int id) {
        Fragment visiBleFragment = MainActivity.this.getFragmentManager().findFragmentById(R.id.content_frame);
        switch (id) {
            case R.id.ll_my_cart:
                closingDrawer();
                if (visiBleFragment instanceof MyCartFragment) {
                    Log.d(TAG, visiBleFragment.getClass().getSimpleName());
                    showProgressDialogbox();
                    ((MyCartFragment) visiBleFragment).UpdateCartFromServer();
                } else {
                    callFragment(new MyCartFragment(), "MyCartFragment");
                }
                break;

            case R.id.iv_sign_in_sign_up:
            case R.id.ll_sign_in_sign_up:
                closingDrawer();
                if (visiBleFragment instanceof SignInSignUpFragment || visiBleFragment instanceof WelcomeFragment2) {
                    Log.d(TAG, visiBleFragment.getClass().getSimpleName());
                    showProgressDialogbox();
                } else {
                    if (mySharedPreference.getString(ConstantDataMember.USER_INFO_USER_LOGIN_STATUS, "0").equals("1"))
                        fragment = new WelcomeFragment2();
                    else
                        fragment = new SignInSignUpFragment();
                    callFragment(fragment, "SignInSignUpFragment");
                }
                break;

            case R.id.ll_home:
                closingDrawer();
                if (visiBleFragment instanceof HomeFragment) {
                    Log.d(TAG, visiBleFragment.getClass().getSimpleName());
                    showProgressDialogbox();
                } else if (visiBleFragment instanceof SignInSignUpFragment) {
                    fragment = new HomeFragment();
                    callFragment(fragment, "HomeFragment");
                } else {
                    popAllFragmentFromStack();
                }
                break;
            case R.id.ll_search:
            case R.id.iv_search:
                closingDrawer();
                fragment = new com.mofluid.magento2.SearchActivity();
                callFragment(fragment, "SearchActivity");
                break;


        }
    }

    /**
     * Logic to close the Navigation drawer, if opened
     */
    private void closingDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void showProgressDialogbox() {
        pDialog.show();
        // close progress dialogbox after 1 second
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                pDialog.dismiss();
            }
        }, 1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Main Activity onActivityResult() called with: " + resultCode + " requestCode= " + requestCode);
        //Toast.makeText(MainActivity.this, "Main Activity resultCode "+resultCode, Toast.LENGTH_SHORT).show();
    }

    private void showWishlist() {
        if (Utils.checkIfIsLoggedIn(activity)) {
            BaseFragment fragment = new WishListFragment();
            callFragment(fragment, "WishListfragment");
        } else {
            createalert();
        }
    }

    private void createalert() {
        builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getResources().getString(R.string.wishlist_message_login));
        builder.setMessage(activity.getResources().getString(R.string.login_to_wishlist));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                dialog.dismiss();
                iswishlist = true;
                BaseFragment frmt = new SignInSignUpFragment();
                callFragment(frmt, "SignInSignUpFragment");

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }

    private ArrayList<MultiCurrencyModel> parseCurrency(JSONArray list) {

        ArrayList<MultiCurrencyModel> currencylist = new ArrayList<>();
        for (int i = 0; i < list.length(); i++) {

            try {
                JSONObject singleCurrency = list.getJSONObject(i);
                String currencyCode = singleCurrency.getString("currency_code");
                String currencyName = singleCurrency.getString("currency_name");
                String currencySymbol = singleCurrency.getString("currency_symbol");

                MultiCurrencyModel singleModel = new MultiCurrencyModel(currencyCode, currencyName, currencySymbol);
                if (Config.getInstance().getCurrencyCode().equalsIgnoreCase(currencyCode))
                    currencylist.add(0, singleModel);
                else
                    currencylist.add(singleModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return currencylist;


    }

    private class AppLogoService extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            NetworkAPIManager.getInstance(MainActivity.this).sendGetRequest(params[0], new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
//                    if (pDialog.isShowing())
//                        pDialog.hide();
                    Log.d(TAG, response);

                    bannerListData.clear();

                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        JSONObject store_details = jsonObj.getJSONObject("store");
                        JSONObject themeJSNobj = jsonObj.getJSONObject("theme");
                        JSONObject logoJSNobj = themeJSNobj.getJSONObject("logo");
                        JSONObject currencyJSNobj = jsonObj.getJSONObject("currency");
                        JSONObject currency_base = currencyJSNobj.getJSONObject("current");
//                        currency_code = currency_base.getString("symbol");
//                        if (currency_code != null) {
//                            Config.getInstance().setCurrency_symbol(currency_code);
//                        }
                        JSONArray logoImageJSNArray = logoJSNobj.getJSONArray("image");
                        cms_about_us_id = store_details.getString("about_us");
                        cms_term_con_id = store_details.getString("term_condition");
                        cms_privacy_id = store_details.getString("privacy_policy");
                        cms_r_privacy_policy = store_details.getString("return_privacy_policy");
                        logoListData = new GetHeaderLogoDetails().getLogoDetails(logoImageJSNArray);

                        if (jsonObj.has("store_available_currency")) {
                            JSONArray currencyList = jsonObj.getJSONArray("store_available_currency");
                            currenciesList = parseCurrency(currencyList);
                            isMultipleCurrency = true;
                        } else {
                            isMultipleCurrency = false;
                            Log.d(TAG, "@vnish - Multiple store currency not available from backend");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
//                        pDialog.hide();
                    }

                    if (logoListData != null && logoListData.size() > 0) {
                        //TODO : Remove below line
                        com.mofluid.magento2.LoadImageWithPicasso.getInstance().loadImage(ivAppLogo,logoListData.get(0).getMofluid_image_value());
                        com.mofluid.magento2.LoadImageWithPicasso.getInstance().loadImage(brandLogoImageView,logoListData.get(0).getMofluid_image_value());
                    }


                    fragment = new HomeFragment();
                    if (pDialog.isShowing())
                        pDialog.hide();
                    Bundle bundle = new Bundle();
                    bundle.putString("storeDetails", response);
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
//                    if (pDialog.isShowing())
//                        pDialog.hide();;
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

//            if (pDialog.isShowing())
//                pDialog.hide();
            hitServiceForSlideMenuItem();

        }
    }

    private class ServiceForSliderMenu extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            final String TAG_CATEGORIES = "categories";
            final String TAG_NAME = "name";
            final String TAG_ID = "id";
            final String TAG_Ch_NAME = "name";
            final String TAG_CH_ID = "id";
            final String TAG_CH_JSON_ARRAY = "children";

            Log.d("PiyushK", "URL CATEGORY IS [ " + params[0] + " ]");

            NetworkAPIManager.getInstance(MainActivity.this).sendGetRequest(params[0], new Response.Listener<String>() {
                public int index;
                JSONArray menuJsonArray = null;
                JSONArray menucChildJsonArray = null;
                Object intervention;

                @Override
                public void onResponse(String response) {
                    //                    if (pDialog.isShowing())
//                        pDialog.hide();
                    Log.d(TAG, response);
                    //Log.d("TA", response.toString());

                    slideMenuListData = new HashMap<String, List<SlideMenuListItem>>();
                    List<SlideMenuListItem> childListData = new ArrayList<SlideMenuListItem>();

                    try {
                        JSONObject jsonObj = null;
                        try {
                            jsonObj = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        parentListData = new ArrayList<SlideMenuListItem>();
                        // Getting JSON Array node
                        menuJsonArray = jsonObj.getJSONArray(TAG_CATEGORIES);

                        // looping through All menuJsonArray
                        for (int i = 0; i < menuJsonArray.length(); i++) {
                            JSONObject c = menuJsonArray.getJSONObject(i);
                            String id = c.getString(TAG_ID);
                            String name = c.getString(TAG_NAME);


                            childListData = new ArrayList<SlideMenuListItem>();

                            try {
                                if (c.has(TAG_CH_JSON_ARRAY) == false) {
                                    parentListData.add(new SlideMenuListItem(name, id));
                                    slideMenuListData.put(parentListData.get(index++).getName(), new ArrayList<SlideMenuListItem>());
                                } else {
                                    intervention = c.get(TAG_CH_JSON_ARRAY);
                                    if (intervention instanceof JSONArray) {
                                        menucChildJsonArray = c.getJSONArray(TAG_CH_JSON_ARRAY);
                                        parentListData.add(new SlideMenuListItem(name, id));
                                        if (menucChildJsonArray.length() > 0)
                                                /*
                                        Add 1 extra subcategory in each child element
                                     */
                                            STR_ALL = MainActivity.this.getResources().getString(R.string.all);
                                        childListData.add(new SlideMenuListItem(STR_ALL + " " + name, id));

                                        for (int j = 0; j < menucChildJsonArray.length(); j++) {
                                            JSONObject childJSONobj = menucChildJsonArray.getJSONObject(j);
                                            String child_Id = childJSONobj.getString(TAG_CH_ID);
                                            String child_Name = childJSONobj.getString(TAG_Ch_NAME);
                                            childListData.add(new SlideMenuListItem(child_Name, child_Id));

                                        }

                                        Log.e(TAG + " Pare Size", parentListData.size() + "");
                                        slideMenuListData.put(parentListData.get(index++).getName(), childListData);
                                        Log.e(TAG + "Size", slideMenuListData.size() + "");

                                        slideMenuAdapter.notifyDataSetChanged();

                                    }
                                }// end of else

                            } catch (Exception ex) {
                                ex.printStackTrace();

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    View view = getLayoutInflater().inflate(R.layout.list_footer, expListView, false);
                    exit_demo = (TextView) view.findViewById(R.id.exit_demo);
                    if (MySharedPreferences.getInstance().get(MySharedPreferences.BASE_URL) != null)
                        exit_demo.setText(R.string.previe_demo);
                    exit_demo.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gotoLoginDemo();
                            MySharedPreferences.getInstance().clear();
                            finish();
                        }
                    });
                    footer = (LinearLayout) view.findViewById(R.id.footer_layout);
                    // Add the footer before the setAdapter() method
                    expListView.addFooterView(footer);
                    slideMenuAdapter = new SlideMenuAdapter(MainActivity.this, parentListData, slideMenuListData);
                    //expListView.addFooterView(footer);
                    expListView.setAdapter(slideMenuAdapter);

//                parentListData.add(new SlideMenuListItem(fetchStringresource(R.string.choose_store), " "));
                    hitServiceForMultiStoreData();

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
//            if (pDialog.isShowing())
//                pDialog.hide();
            if (cd.isConnectingToInternet()) {
//                queue.add(strReqSlideMenu);
//                retryRequest(strReqSlideMenu);
            } else {
                new ShowAlertDialogBox().showCustomeDialogBox(MainActivity.this, fetchStringresource(R.string.internet_connection), getResources().getString(R.string.internet_not_avalable));
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    private class GetTokenKey extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            NetworkAPIManager.getInstance(MainActivity.this).sendGetRequestNonToken(params[0], new Response.Listener<String>() {
                @Override
                public void onResponse(String str) {
                    Log.d(TAG, "Response from Token creation API is " + str);

                    try {
                        JSONObject response = new JSONObject(str);
                        if (response.has("token")) {
                            String token = response.getString("token");
                            NetworkAPIManager.getInstance(MainActivity.this).setTokenID(token);
                        }

                        if (response.has("secretkey")) {
                            String secretKey = response.getString("secretkey");
                            NetworkAPIManager.getInstance(MainActivity.this).setSecretKey(secretKey);
                        }
                        publishProgress("Success");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();

                }
            });


            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values[0].equals("Success")) {
                pDialog.dismiss();
                hitServiceForAppLogo();
            }
        }
    }

    /*No Async Task used, direct call*/
    private void hitServiceForMultiStoreData() {

        pDialog.show();
        view_list = new ArrayList<>();

        String url = WebApiManager.getInstance().getMultiStoreDataURL(MainActivity.this);

        NetworkAPIManager.getInstance(MainActivity.this).sendGetRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String strJson) {
                Log.e("Response", strJson);
                pDialog.dismiss();
                JSONObject response = null;
                try {
                    response = new JSONObject(strJson);

                    JSONObject main_website = response.getJSONObject("1");
                    JSONObject websideobject = main_website.getJSONObject("webside");
                    JSONArray store_array = convertJsonObjecttoArray(websideobject);

                    for (int i = 0; i < store_array.length(); i++) {
                        view_list = new ArrayList<>();
                        JSONObject store_Details = store_array.getJSONObject(i);
                        MultiStoreData storedata = new MultiStoreData();
                        storedata.setId(store_Details.getString("store_id"));
                        storedata.setStore_Name(store_Details.getString("store"));
                        JSONObject view_object = null;
                        try {
                            view_object = store_Details.getJSONObject("view");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (view_object != null) {
                            JSONArray view_array = convertJsonObjecttoArray(view_object);
                            for (int j = 0; j < view_array.length(); j++) {
                                JSONObject view_Details = view_array.getJSONObject(j);
                                MultiStoreView view = new MultiStoreView();
                                view.setId(view_Details.getString("store_id"));
                                view.setName(view_Details.getString("name"));
                                view.setCurrency(view_Details.getString("current_currency_code"));
                                view.setLanguage_code(view_Details.getString("store_lang_code"));
                                //add view to view arraylist
                                view_list.add(view);
                            }

                        }
                        storedata.setViews_Store(view_list);
                        storelist.add(storedata);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                        slideMenuListData.put(parentListData.get((parentListData.size()-1)).getName(), childListData);
//                        slideMenuAdapter = new SlideMenuAdapter(MainActivity.this, parentListData, slideMenuListData);
//                        expListView.setAdapter(slideMenuAdapter);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Log.e("Error", "Some Thing Going Wrong");
            }
        });

    }

    /**
     * Called by Fragments to initiate some action in Activity
     * use TAG = ConstantDataMember.SET_TITLE_TAG to, Set the title in appbar
     * use TAG = ConstantDataMember.SET_IMAGE_TAG to, Set the image in appbar
     * use TAG = ConstantDataMember.POP_ALL_FRAGMENT_FROM_STACK to, Pop all fragment except HomeFragment
     */
    @Override
    public void onFragmentMessage(String TAG, Object data) {
        if (ConstantDataMember.SET_TITLE_TAG.equals(TAG)) {
            setAppbarText((String) data);
        } else if (ConstantDataMember.SET_IMAGE_TAG.equals(TAG)) {
            setAppbarImage();
        } else if (ConstantDataMember.POP_ALL_FRAGMENT_FROM_STACK.equals(TAG)) {
            popAllFragmentFromStack();
        }
    }
    private void addFCMRegisterListner(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( MainActivity.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                L.d(TAG,"FCM new token="+newToken);

            }
        });
    }
}

