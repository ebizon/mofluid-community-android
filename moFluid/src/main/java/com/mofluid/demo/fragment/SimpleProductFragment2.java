package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.Utils.Validation;
import com.mofluid.utility_new.Callback;
import com.mofluid.utility_new.WishListManager;
import com.ebizon.fluid.model.AdditionalInfoItem;
import com.ebizon.fluid.model.ConfigurableChild;
import com.ebizon.fluid.model.DownloadableShoppingItem;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.ProductReview;
import com.ebizon.fluid.model.ReviewVote;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.ShoppingCartItem;
import com.ebizon.fluid.model.ShoppingItem;
import com.ebizon.fluid.model.ShoppingItemManager;
import com.ebizon.fluid.model.SimpleShoppingItem;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserProfileItem;
import com.ebizon.fluid.model.WebApiManager;
import com.ebizon.fluid.model.WishListItem;
import com.mofluid.demo.adapter.ProductAdapter;
import com.mofluid.magento2.GetDataForAdditionalInfo;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.ProductDetailBannerAdapter;
import com.mofluid.magento2.adapter.ProductReviewAdapter;
import com.viewpagerindicator.CirclePageIndicator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import com.mofluid.magento2.fragment.BaseFragment;
import at.blogc.android.views.ExpandableTextView;

/**
 * Created by piyush on 9/5/16.
 */
@SuppressLint("ValidFragment")
public class SimpleProductFragment2 extends BaseFragment implements View.OnClickListener, ProductAdapter.RecyclerViewClickListener{
    private static final String PRODUCT_ID_PARAM = "product_id";
    private static final String PRODUCT_NAME_PARAM = "product_name";
    public static String pid;
    public static ArrayList<AdditionalInfoItem> additionalInfoList;
    public static String cartImage;
    public static ProgressDialog pDialog;
    private static int numberOfItem = 1;
    public LinkedHashMap<String, LinkedHashSet<String>> configDisplayListMap;
    public LinkedHashMap<String, LinkedHashSet<Integer>> configDisplayHashMap;
    public LinkedHashMap<Integer, LinkedHashSet<String>> configButtonsListMap;
    Activity contextreorder;
    private String TAG = "";
    private String PRODUCT_ID;
    private String PRODUCT_NAME = "";
    private View rootView;
    private RelativeLayout ll_show_progress_bar;
    private ScrollView scroll_bar_show_product;
    private String description;
    private String url;
    private String type;
    private String has_custom_option;
    private TextView txtV_specila_price;
    private ArrayList<ShoppingCartItem> cartItemList;
    private TextView txtV_price;
    private ExpandableTextView txtV_description;
    private LinearLayout ll_custome_option;
    private ArrayList<String> imgListStrArry;
    private ViewPager viewPager_banner;
    private ImageView imageview_Product;
    private ProductDetailBannerAdapter productDetailBannerAdapter;
    private Typeface open_sans_regular, open_sans_semibold;
    private CirclePageIndicator indicator;
    private String PRODUCT_PRICE;
    private String short_description;
    private ShoppingItem currentItem = null;
    private Activity act;
    private ImageView txtV_add_to_cart;
    private ShoppingItem reorderItem;
    private JSONObject downloadable_object;
    private JSONArray downloadable_items;
    private TextView download_heading;
    private LinearLayout download_layout;
    private ArrayList<CheckBox> checbox_list;
    private HashSet<String> checkbox_links;
    private TextView productname_pdp2;
    private TextView txt_desc;
    private TextView txt_specification;
    private TextView txt_specification_value;
    private TextView btn_pdp_buy_now;
    private ImageButton pdp_image_share;
    private String shareurl;
    private RelativeLayout rl_pdp_extra_options, rl_pdp_specifications;
    private RelativeLayout rl_pdp_description;
    private LinearLayout ll_configurable_heading_scrollable;
    private LinearLayout configurable_layout;
    private ArrayList<TextView> config_heading;
    private ArrayList<ProductReview> productReviewList;
    private TextView txtv_total_Reviews;
    private RatingBar average_ratingbar;
    private TextView average_rating_value;
    private ListView listv_pdp_review_list;
    private TextView btn_view_all_reviews;
    private Button btn_write_review;
    private LinearLayout ll_avg_rating;
    private TextView first_to_review;
    private RelativeLayout rl_pdp_review;
    private int totalReviews = 0;
    private TextView txtv_review_heading;
    private TextView txt_not_login_review;
    private SharedPreferences mySharedPreference;
    private RelativeLayout rl_no_product_found;
    private boolean isNoProduct = false;
    private ImageView imgv_add_to_wishlist;
    private AlertDialog.Builder builder;
    private RelativeLayout rl_pdp_load_data;
    private TextView txtv_pdp_price_discount;
    private RelativeLayout rl_pdp_price_normal;
    private TextView txtv_pdp_read_more_description;
    private TextView line_above_extra;
    private ImageView imgV_additional_info;
    private TextView txtv_pdp_price_stock;
    private String img;
    //Related product list
    private RecyclerView relatedProductRecyclerView;
    private ProductAdapter relatedProductAdapter;
    private List<ShoppingItem> relatedProductListDetails;
    //#//
    private ArrayList<ShoppingItem> featuredProductListDetails;
    // added ends here
    private TextView tvNewFeature;
    //CONFIGURABLE
    private RelativeLayout featureProduct_Container;
    private LinkedHashMap<String, ConfigurableChild> configChildrenListMap;
    private LinkedHashMap<String, String> config_option_items;
    private String BaseOption = "";
    private ArrayList<String> config_option_list;
    private HashSet<Integer> finalRemoveList;
    private boolean isFirstConfigurable;
    private View firstView = null;

    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;


    public SimpleProductFragment2(ShoppingItem reorderItem, Activity contextfromreorder) {
        this.reorderItem = reorderItem;
        addItemToCart();
        this.contextreorder = contextfromreorder;
    }

    public SimpleProductFragment2() {

    }

    public static String getProductID() {
        return pid;
    }

    public static void cancelToast(final Toast toast) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1500);
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + 120;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.simple_product_layout, null, false);
            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
            initialized();
            gteValueFromArgument();
            getViewControls(rootView);
            setDatatoView();
            //newly added by avnish
            setdataToRelatedProduct();

            MainActivity.ivAppLogo.setVisibility(View.INVISIBLE);
//
            String str = SimpleProductFragment.truncate(PRODUCT_NAME);
            MainActivity.product_name.setText(str);
            MainActivity.product_name.setVisibility(View.VISIBLE);
            setFontStyle();
            hitServiceForFeaturedProducts();
            hitServiceForImages();
            hitserviceforratings();
        }

        return rootView;
    }

    private void hitServiceForRelatedProducts() {

        String id = getProductID();
        String urlRelatedProducts = WebApiManager.getInstance().getRelatedProductsURL(getActivity());
        urlRelatedProducts = String.format(urlRelatedProducts, PRODUCT_ID);

        RelatedProductService rps = new RelatedProductService();
        rps.execute(urlRelatedProducts);

    }

    // newly added by avnish
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

    // newly added by avnish
    private void setdataToRelatedProduct() {
        if (featuredProductListDetails != null) {
            relatedProductAdapter.setData(featuredProductListDetails);
        }
    }

    private void hitserviceforratings() {

        productReviewList = new ArrayList<>();
        String urlProductRating = WebApiManager.getInstance().getProductReviewURL(act);
        urlProductRating = String.format(urlProductRating, PRODUCT_ID.trim());
        Log.d("PiyushK", "Service for rating called with :" + urlProductRating);
        RatingService serviceRating = new RatingService();
        serviceRating.execute(urlProductRating);
    }

    private void setReviewDatatoUI(String average, String total) {
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            float avg = Float.parseFloat(average);
            totalReviews = Integer.parseInt(total);
            String str = String.format(getActivity().getResources().getString(R.string.total_reviews), total);
            Log.d("PiyushKatyal", "Total value set to :" + str);
            txtv_total_Reviews.setText("( " + str + " )");
            rl_pdp_review.setVisibility(View.VISIBLE);
            if (totalReviews == 0) {
                setNoReviewUI();

            }
            if (checkIfIsLoggedIn() == false) {
                btn_write_review.setVisibility(View.GONE);
                txt_not_login_review.setVisibility(View.VISIBLE);
            }
            int averageValue = Math.round(avg) / 20;
            average_rating_value.setText("" + averageValue + "/5");
            Log.d("PiyushKatyal", "average value set  to :" + "" + avg + "/5");
            if (totalReviews > 2)
                btn_view_all_reviews.setVisibility(View.VISIBLE);

            average_ratingbar.setStepSize(1.0f);
            average_ratingbar.setRating((averageValue));
            average_ratingbar.setIsIndicator(true);
            changeStarColor(average_ratingbar);

            //set adapter to list of product reviews
            ArrayList<ProductReview> smallReviewList = new ArrayList<>();
            if (productReviewList.size() > 2) {
                smallReviewList.add(productReviewList.get(0));
                smallReviewList.add(productReviewList.get(1));
            } else
                smallReviewList = productReviewList;

            ProductReviewAdapter reviewadapter = new ProductReviewAdapter(smallReviewList, getActivity());
            listv_pdp_review_list.setAdapter(reviewadapter);

            setListViewHeightBasedOnItems(listv_pdp_review_list);

        }

    }

    private void changeStarColor(RatingBar ratingBar) {
//        Drawable drawable = ratingBar.getProgressDrawable();
//        drawable.setColorFilter(Color.parseColor("#2e8ab8"), PorterDuff.Mode.SRC_ATOP);
    }

    private void setNoReviewUI() {
        ll_avg_rating.setVisibility(View.GONE);
        listv_pdp_review_list.setVisibility(View.GONE);
        average_rating_value.setVisibility(View.GONE);
        first_to_review.setVisibility(View.VISIBLE);
    }

    private void setDatatoView() {
        productname_pdp2.setText("Loading Product...");
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.showFooterOptionsMenu(false);
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void gteValueFromArgument() {
        try {
            PRODUCT_ID = getArguments().getString(ConstantDataMember.PRO_ID);
            pid = PRODUCT_ID;
            PRODUCT_NAME = getArguments().getString(ConstantDataMember.PRO_NAME);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getViewControls(View rootView) {
        ll_show_progress_bar = (RelativeLayout) rootView.findViewById(R.id.ll_show_progress_bar);
        ll_show_progress_bar.setVisibility(View.VISIBLE);
        scroll_bar_show_product = (ScrollView) rootView.findViewById(R.id.scrollview_pdp_product_details);
        txtV_specila_price = (TextView) rootView.findViewById(R.id.txtv_pdp_special_price);
        txtV_price = (TextView) rootView.findViewById(R.id.txtv_pdp_price);
        txtV_description = (ExpandableTextView) rootView.findViewById(R.id.txtv_pdp_product_description);
        imgV_additional_info = (ImageView) rootView.findViewById(R.id.imgv_pdp_specifications_open);
        txtV_description.setAnimationDuration(1000L);
        line_above_extra = (TextView) rootView.findViewById(R.id.line_above_extra);
        txtv_pdp_price_stock = (TextView) rootView.findViewById(R.id.txtv_pdp_price_stock);
        txtV_description.setInterpolator(new OvershootInterpolator());
        txtV_description.setExpandInterpolator(new OvershootInterpolator());
        txtV_description.setCollapseInterpolator(new OvershootInterpolator());

        /// listen for expand / collapse events
        txtV_description.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(final ExpandableTextView view) {
                Log.d(TAG, "ExpandableTextView expanded");
                if (!txtV_description.getText().equals(description))
                    txtV_description.setText(Html.fromHtml(description));
                txtv_pdp_read_more_description.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCollapse(final ExpandableTextView view) {
                Log.d(TAG, "ExpandableTextView collapsed");
                if (txtV_description.getLineCount() == 3) {
                    txtV_description.setText(Html.fromHtml(short_description.concat("...")));
                } else {
                    if (description.trim().length() > 1) {
                        txtV_description.setText(Html.fromHtml(description));
                    } else {
                        Activity activity = getActivity();
                        if (isAdded() && activity != null)
                            txtV_description.setText(getActivity().getResources().getString(R.string.no_description_added));
                    }
                }
            }
        });

        productname_pdp2 = (TextView) rootView.findViewById(R.id.txtv_pdp_product_name);
        txt_desc = (TextView) rootView.findViewById(R.id.desc_pdp);
        txt_specification_value = (TextView) rootView.findViewById(R.id.txtv_pdp_product_specification);
        txt_specification = (TextView) rootView.findViewById(R.id.txt_specification_pdp);
       /* txt_wishList_pdp = (TextView) rootView.findViewById(R.id.txtv_pdp_wishlist_text);*/
        btn_pdp_buy_now = (TextView) rootView.findViewById(R.id.btn_pdp_buy_now);
        pdp_image_share = (ImageButton) rootView.findViewById(R.id.pdp_image_share);
        rl_pdp_extra_options = (RelativeLayout) rootView.findViewById(R.id.rl_pdp_extra_options);
        rl_pdp_description = (RelativeLayout) rootView.findViewById(R.id.rl_pdp_description);
        rl_pdp_specifications = (RelativeLayout) rootView.findViewById(R.id.rl_pdp_specifications);
        rl_pdp_review = (RelativeLayout) rootView.findViewById(R.id.rl_pdp_review);
        rl_pdp_load_data = (RelativeLayout) rootView.findViewById(R.id.rl_pdp_load_data);
        rl_pdp_specifications.setOnClickListener(this);
        rl_pdp_description.setOnClickListener(this);
//        imgv_pdp_description_open = (ImageView) rootView.findViewById(R.id.imgv_pdp_description_open);
        txtv_pdp_read_more_description = (TextView) rootView.findViewById(R.id.txtv_pdp_read_more_description);
        txtv_pdp_read_more_description.setOnClickListener(this);
        rl_no_product_found = (RelativeLayout) rootView.findViewById(R.id.no_product_found);
        ll_configurable_heading_scrollable = (LinearLayout) rootView.findViewById(R.id.ll_configurable_heading_scrollable);
//        imgv_pdp_description_open.setOnClickListener(this);

        txtv_pdp_price_discount = (TextView) rootView.findViewById(R.id.txtv_pdp_price_discount);
        rl_pdp_price_normal = (RelativeLayout) rootView.findViewById(R.id.rl_pdp_price_normal);


        imgv_add_to_wishlist = (ImageView) rootView.findViewById(R.id.imgv_add_to_wishlist);
        imgv_add_to_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkIfIsAddedToWishlist(true);
            }


        });
        pdp_image_share.setOnClickListener(this);
        indicator = (CirclePageIndicator) rootView.findViewById(R.id.pdp_image_indicator);
        ll_custome_option = (LinearLayout) rootView.findViewById(R.id.custom_llayout);
        viewPager_banner = (ViewPager) rootView.findViewById(R.id.viewPager_pdp_images);
        imageview_Product = (ImageView) rootView.findViewById(R.id.default_image_pdp_product);

        download_layout = (LinearLayout) rootView.findViewById(R.id.ll_pdp_downloadable_attribute);
        download_heading = (TextView) rootView.findViewById(R.id.pdp_downloadable_heading);

        configurable_layout = (LinearLayout) rootView.findViewById(R.id.ll_pdp_configurable_attribute);

        ImageView imgV_additional_info = (ImageView) rootView.findViewById(R.id.imgv_pdp_specifications_open);
        imgV_additional_info.setOnClickListener(this);

        btn_pdp_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    performAddToCartAction(v, true);

            }
        });

        txtV_add_to_cart = (ImageView) rootView.findViewById(R.id.btn_pdp_add_to_cart);
        txtV_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performAddToCartAction(view, false);
            }
        });

        //PRODUCT REVIEW UI
        txtv_total_Reviews = (TextView) rootView.findViewById(R.id.txtv_total_Reviews);
        average_ratingbar = (RatingBar) rootView.findViewById(R.id.average_ratingbar);
        average_rating_value = (TextView) rootView.findViewById(R.id.average_rating_value);
        listv_pdp_review_list = (ListView) rootView.findViewById(R.id.listv_pdp_review_list);
        ll_avg_rating = (LinearLayout) rootView.findViewById(R.id.ll_avg_rating);
        txtv_review_heading = (TextView) rootView.findViewById(R.id.txtv_review_heading);
        btn_view_all_reviews = (TextView) rootView.findViewById(R.id.btn_view_all_reviews);
        first_to_review = (TextView) rootView.findViewById(R.id.first_to_review);
        txt_not_login_review = (TextView) rootView.findViewById(R.id.txt_not_login_review);
        txt_not_login_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragment(new SignInSignUpFragment(), "SignInSignUpFragment");
            }
        });
        btn_view_all_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllReviewFragment allreview = new AllReviewFragment(productReviewList, totalReviews, act, PRODUCT_ID, PRODUCT_NAME);
                callFragment(allreview, "AllReviewFragment");

            }
        });
        btn_write_review = (Button) rootView.findViewById(R.id.btn_write_review);
        btn_write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileItem activeUser = UserManager.getInstance().getUser();
                WriteReviewFragment writeReview = new WriteReviewFragment(PRODUCT_ID, PRODUCT_NAME, activeUser.getId());
                callFragment(writeReview, "WriteReviewFragment");

            }
        });
        // NEWLY ADDED BY AVNISH
        featureProduct_Container = (RelativeLayout) rootView.findViewById(R.id.fl_feature_product_container);
        //RecyclerView for showing related product
        relatedProductRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_related_products);
        relatedProductAdapter = new ProductAdapter(null,this);
        relatedProductRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        relatedProductRecyclerView.setAdapter(relatedProductAdapter);
        //#//
        tvNewFeature = (TextView) rootView.findViewById(R.id.tvRelated);
        Typeface font_type = Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.HELVETICA_FONT_STYLE);
        tvNewFeature.setTypeface(font_type);

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

    private boolean checkStockofProduct() {
        return currentItem.isInStock();
    }

    private void performAddToCartAction(View v, boolean isRedirect) {
        if (type.equalsIgnoreCase("configurable")) {
            boolean isReadyforAddCart = true;
            String selected_value = null;
            Iterator it = config_option_items.entrySet().iterator();
            while (it.hasNext()) {
                LinkedHashMap.Entry<String, String> entry = (LinkedHashMap.Entry<String, String>) it.next();
                String option = entry.getKey();
                String selected_option = entry.getValue();
                Log.d("PKCONFG", "Selection option : " + option + ", Selected value :" + selected_option);
                if (selected_option == null)// if no option is selected for that particular label
                {
                    isReadyforAddCart = false;
                    Toast.makeText(getActivity(), "Please select " + option + " to continue", Toast.LENGTH_SHORT).show();
                    selected_value = null;
                    break;
                }
                if (selected_value == null)
                    selected_value = selected_option;
                else
                    selected_value = selected_value + "," + selected_option;
            }


            if (isReadyforAddCart) {
                // All options are selected and product is ready to be added in cart.
                if (selected_value != null) {
                    if (configChildrenListMap.containsKey(selected_value)) {
                        ConfigurableChild selected_child = configChildrenListMap.get(selected_value);
                        currentItem = selected_child; // set current item to item to be sent further
                        if (isRedirect)
                            addItemToCart(true);
                        else
                            addItemToCart();
                    } else {
                        //should never reach here but in case
                        Toast.makeText(act, "This combination is not available", Toast.LENGTH_SHORT).show();
                    }

                }
            }

        } else {
            if (!Utils.checkCartItemforDownloadable(new ArrayList<ShoppingCartItem>(ShoppingCart.getInstance().getCartItems()))) {
                if (type != null) {
                    if (!type.equals("downloadable")) {
                        // Simple Product
                        if (isRedirect)
                            addItemToCart(true);
                        else
                            addItemToCart();

                    } else if (type.equals("downloadable")) {
                        if (validateDownloadOptions() == false) {
                            if (isAdded() && act != null) {
                                final Toast toast = Toast.makeText(act, act.getResources().getString(R.string.configurable_message), Toast.LENGTH_LONG);
                                toast.show();
                                cancelToast(toast);
                                download_layout.setBackground(act.getResources().getDrawable(R.drawable.background_downloadable_product_unselected));
                            }
                        } else {
                            if (ShoppingCart.getInstance().getCartItems().size() == 0) {
                                setDownloadableCheckList();
                                if (isRedirect)
                                    addItemToCart(true);
                                else
                                    addItemToCart();

                            } else {
                                if (isAdded() && act != null) {
                                    final Toast toast = Toast.makeText(act, act.getResources().getString(R.string.one_downloadable_allowed), Toast.LENGTH_LONG);
                                    toast.show();
                                    cancelToast(toast);
                                }
                            }
                        }
                    }
                } else {
                    if (isAdded() && act != null) {
                        final Toast toast = Toast.makeText(act, act.getResources().getString(R.string.still_loading_detail), Toast.LENGTH_LONG);
                        toast.show();
                        cancelToast(toast);
                    }
                }
            } // end of if


            else {
                if (currentItem.getType().toString().equals("downloadable")) {
                    setDownloadableCheckList();
                    if (isRedirect)
                        addItemToCart(true);
                    else
                        addItemToCart();

                } else {
                    if (isAdded() && act != null) {
                        final Toast toast = Toast.makeText(act, act.getResources().getString(R.string.one_downloadable_allowed), Toast.LENGTH_LONG);
                        toast.show();
                        cancelToast(toast);
                    }
                }
            }
        }
    }

    private void addItemToUserWishlist() {
        // check for user login or not else display dialog box and redirect to login page
        if (checkIfIsLoggedIn() == false)
            createalert();
        else {

            setItemAsWishListAdded();
            WishListManager.getInstance().addItemToWishList(PRODUCT_ID, new Callback() {
                @Override
                public void callback(Object o, int response_code) {

                }
            });
        }

    }

    private void createalert() {
        builder = new AlertDialog.Builder(act);
        if (isAdded() && act != null) {
            builder.setTitle(act.getResources().getString(R.string.wishlist_message_login));

            builder.setMessage(act.getResources().getString(R.string.login_to_add_wishlist));
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                dialog.dismiss();
                BaseFragment frmt = new SignInSignUpFragment();
                callFragment(frmt);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    private void setItemAsWishListAdded() {
            imgv_add_to_wishlist.setImageDrawable(act.getResources().getDrawable(R.drawable.heart_selected));
    }


    private void setItemAsWishListNotAdded() {
            imgv_add_to_wishlist.setImageDrawable(act.getResources().getDrawable(R.drawable.heart));
    }


    private boolean checkIfIsLoggedIn() {
        Boolean isLoggedin = false;
        if (mySharedPreference.getString(ConstantDataMember.USER_INFO_USER_LOGIN_STATUS, "0").equals("1")) {
            isLoggedin = true;
        } else {
            isLoggedin = false;
        }
        return isLoggedin;
    }


    public void showToast() {
        if (isAdded() && act != null)
            Toast.makeText(getActivity(), act.getResources().getString(R.string.item_added), Toast.LENGTH_SHORT).show();
    }

    private void setDownloadableCheckList() {
        for (CheckBox chk : checbox_list) {
            if (chk.isChecked()) {
                checkbox_links.add(String.valueOf(chk.getId()));
            }
        }

    }

    private boolean validateDownloadOptions() {
        for (int q = 0; q < checbox_list.size(); q++) {
            if (checbox_list.get(q).isChecked() == true)
                return true;
        }
        return false;
    }

    private void addItemToCart() {
        if(currentItem==null || !currentItem.isInStock()){
            Toast.makeText(act,R.string.default_error_out_of_stock,Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentItem != null || reorderItem != null) {
            ShoppingCartItem item;
            if (currentItem != null) {
                item = new ShoppingCartItem(currentItem, numberOfItem);

            } else {
                item = new ShoppingCartItem(reorderItem, numberOfItem);
            }
            //test

            Collection<ShoppingCartItem> sp = ShoppingCart.getInstance().getCartItems();
            int numofcartitems = sp.size();
            if (numofcartitems != 0) {
                int FLAG = 0;
                for (int i = 0; i < sp.size(); i++) {
                    Iterator<ShoppingCartItem> it = sp.iterator();
                    while (it.hasNext()) {
                        ShoppingCartItem sci = it.next();
                        if (currentItem != null) {
                            if (sci.getShoppingItem().getId().equals(currentItem.getId())) {
                                if (isAdded() && act != null) {
                                    final Toast toast = Toast.makeText(act, act.getResources().getString(R.string.item_already_added), Toast.LENGTH_SHORT);
                                    toast.show();
                                    cancelToast(toast);
                                }
                                FLAG = 0;
                                break;
                            } else {
                                FLAG = 1;
                            }
                        } else {
                            if (sci.getShoppingItem().getId().equals(reorderItem.getId())) {
                               /*// final Toast toast = Toast.makeText(contextreorder, contextreorder.getResources().getString(R.string.item_already_added), Toast.LENGTH_SHORT);
                                toast.show();
                                cancelToast(toast);*/
                                FLAG = 0;
                                break;
                            } else {
                                FLAG = 1;
                            }
                        }
                    }

                }

                if (FLAG == 1) {
                    ShoppingCart.getInstance().addItem(item, this);
                    if (currentItem != null) {
                        showToast();
                    }
                }
            } else {
                ShoppingCart.getInstance().addItem(item, this);
                if (currentItem != null) {
                    showToast();
                }
            }

            //end test
            cartItemList = new ArrayList<>(ShoppingCart.getInstance().getCartItems());

            if (cartItemList.size() > 0) {
                MainActivity.txtV_item_counter.setText(cartItemList.size() + "");
                MainActivity.txtV_item_counter.setText(cartItemList.size() + "");
                MainActivity.txtV_item_counter.setVisibility(View.VISIBLE);
            } else {
                MainActivity.txtV_item_counter.setVisibility(View.INVISIBLE);

            }
        }
    }


    private void callAdditionalInfoFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(ConstantDataMember.PRO_NAME, PRODUCT_NAME);
        bundle.putString(ConstantDataMember.PRO_FINAL_PRICE, PRODUCT_PRICE);

        // if description is not available then set short descroption
        if (description.length() < 1)
            bundle.putString(ConstantDataMember.PRO_SHORT_DESCRI, short_description);
        else
            bundle.putString(ConstantDataMember.PRO_SHORT_DESCRI, description);
        BaseFragment fragment = new MyAditionalFragmenrt();
        fragment.setArguments(bundle);

        callFragment(fragment);
    }

    private void initialized() {

        act = getActivity();
        TAG = getClass().getSimpleName();
        open_sans_semibold = Typeface.createFromAsset(act.getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
        open_sans_regular = Typeface.createFromAsset(act.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
        additionalInfoList = new ArrayList<>();
        mySharedPreference = getActivity().getSharedPreferences(ConstantDataMember.USER_INFO_SESSION, Context.MODE_PRIVATE);
        checbox_list = new ArrayList<>();
        checkbox_links = new HashSet<>();
        config_heading = new ArrayList<>();
        productReviewList = new ArrayList<>();
        shareurl = "";

        // newly added by avnish
        featuredProductListDetails = new ArrayList<ShoppingItem>();
    }

    private void setFontStyle() {

        productname_pdp2.setTypeface(open_sans_semibold); // product name
      /*  txt_wishList_pdp.setTypeface(open_sans_regular); // Favourite text*/
        txtV_specila_price.setTypeface(open_sans_semibold); //Special price
        txtV_price.setTypeface(open_sans_semibold); // price

        txtV_description.setTypeface(open_sans_regular); // Description heading
        txt_desc.setTypeface(open_sans_semibold); //Description text

        txt_specification.setTypeface(open_sans_semibold); //Specification heading
        txt_specification_value.setTypeface(open_sans_regular); // Speicifcation text

        btn_pdp_buy_now.setTypeface(open_sans_regular);
        txtv_pdp_price_discount.setTypeface(open_sans_regular);
        txtv_pdp_read_more_description.setTypeface(open_sans_regular);

        // Extra options : Custom, downloadable, configurable
        //Downloadable
        download_heading.setTypeface(open_sans_semibold);


        //Review

        txtv_review_heading.setTypeface(open_sans_semibold);
        txtv_total_Reviews.setTypeface(open_sans_semibold);
        average_rating_value.setTypeface(open_sans_regular);
        first_to_review.setTypeface(open_sans_semibold);
        btn_view_all_reviews.setTypeface(open_sans_semibold);
        btn_write_review.setTypeface(open_sans_semibold);

    }

    private void setViewPager() {
        productDetailBannerAdapter = new ProductDetailBannerAdapter(act, imgListStrArry, PRODUCT_ID);
        FullScreenImageDialog.FLAG = 1;
        if (imgListStrArry != null) {
            viewPager_banner.setAdapter(productDetailBannerAdapter);
            if(imgListStrArry.size()>0) {
                imageview_Product.setVisibility(View.GONE);
            }
            CirclePageIndicator mIndicator = indicator;
            indicator.setViewPager(viewPager_banner);
            Activity activity = getActivity();
            if (isAdded() && activity != null) {
                final float density = activity.getResources().getDisplayMetrics().density;
                indicator.setRadius(4.2f * density);
            }
        } else
            SimpleProductFragment.cartImage = null;
    }

    private void hitServiceForFeaturedProducts() {
        String urlFeatureProducts = WebApiManager.getInstance().getProductDetailURL(act);
        urlFeatureProducts = String.format(urlFeatureProducts, PRODUCT_ID.trim());
        Log.d("productdetails", urlFeatureProducts);
        ServiceForProduct productService = new ServiceForProduct();
        productService.execute(urlFeatureProducts);
    }

    private void populateDownloadableProduct(String heading) {
        if (downloadable_items != null) {
            LinearLayout checkbox_layout = new LinearLayout(getActivity());
            checkbox_layout.setOrientation(LinearLayout.VERTICAL);
            checkbox_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            checkbox_layout.setPadding(15, 5, 0, 0);
            download_heading.setText(heading);
            rl_pdp_extra_options.setVisibility(View.VISIBLE);
            line_above_extra.setVisibility(View.VISIBLE);
            download_layout.setVisibility(View.VISIBLE);
            rl_pdp_description.setPadding(0, 0, 0, 2);

            for (int i = 0; i < downloadable_items.length(); i++) {
                try {
                    JSONObject object = downloadable_items.getJSONObject(i);
                    String title = object.getString("title");
                    String id2 = object.getString("link_id");
                    int id = Integer.parseInt(id2);
                    //add new checkbox
                    CheckBox chk = new CheckBox(getActivity());
                    chk.setId(id);
                    chk.setTypeface(open_sans_regular);
                    chk.setText(title);
                    checkbox_layout.addView(chk);
                    checbox_list.add(chk);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
//          checkbox_layout.setBackgroundColor(getActivity().getResources().getColor(R.color.app_color));
            download_layout.addView(checkbox_layout);
            setDataToUI();
            setCheckBoxClick();
        } else {
            rl_pdp_extra_options.setVisibility(View.INVISIBLE);
            line_above_extra.setVisibility(View.GONE);
            download_layout.setVisibility(View.INVISIBLE);
            rl_pdp_description.setPadding(0, 4, 0, 2);
        }
//        ll_show_progress_bar.setVisibility(View.GONE);

    }

    private void setCheckBoxClick() {
        for (int i = 0; i < checbox_list.size(); i++) {
            checbox_list.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAdded() && act != null)
                        download_layout.setBackground(act.getResources().getDrawable(R.drawable.background_downloadable_product));

                }
            });

        }
    }

    private void changeBackground(View header_view, int drawabl) {
        Activity activity = getActivity();
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            if (isAdded() && activity != null)
                header_view.setBackgroundDrawable(activity.getResources().getDrawable(drawabl));
        } else {

            if (isAdded() && activity != null)
                header_view.setBackground(activity.getResources().getDrawable(drawabl));
        }
    }


    private LinearLayout createLayoutNew(String headerName) {
        LayoutInflater layoutInflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View singleConfig = layoutInflater.inflate(R.layout.configurable_heading, null);
        TextView textView = (TextView) singleConfig.findViewById(R.id.scroll_heading);
        textView.setTypeface(open_sans_semibold);
        textView.setId(headerName.hashCode());
        textView.setTag(headerName);
        Activity activity = getActivity();
        if (isAdded() && activity != null)
            textView.setText(getActivity().getResources().getString(R.string.select_select) + " " + headerName + " : ");

        LinearLayout single_config_options = (LinearLayout) singleConfig.findViewById(R.id.single_config_options);
        single_config_options.setTag("Hellooooo");
        addButtonsToSingleOption(headerName, single_config_options);

        ll_configurable_heading_scrollable.addView(singleConfig);

        return single_config_options; // will return options linear layout created
    }


    private void addButtonsToSingleOption(String headerName, LinearLayout single_config_options) {

        // create layout for all buttons of a single option.
        LinkedHashSet<String> options = configDisplayListMap.get(headerName);
        Iterator<String> it = options.iterator();
        while (it.hasNext()) {
            String label = it.next();
            View attributeView = getCreatedButtonViews(label);
            single_config_options.addView(attributeView);
            if (headerName.equals(BaseOption)) // set automatic click of first option;
                if (!isFirstConfigurable) {
                    firstView = attributeView;
                    isFirstConfigurable = true;
                }

        }
    }

    private View getCreatedButtonViews(String label) {
        finalRemoveList = new HashSet<>();
        Set<Integer> allButtons = configButtonsListMap.keySet();
        ArrayList<Integer> allButtonList = new ArrayList<>(allButtons);
        // label is text to be displayed
        // create button views for each button
        LayoutInflater layoutInflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View attributeView = layoutInflater.inflate(R.layout.configurable_attribute, null);
        FrameLayout.LayoutParams lpp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lpp.setMargins(10, 0, 10, 0);
        TextView txtView = (TextView) attributeView.findViewById(R.id.configurable_attribute);
        attributeView.setId(Math.abs(label.hashCode()));
        attributeView.setTag(label);
        attributeView.setLayoutParams(lpp);
        attributeView.setClickable(true);


        txtView.setText(label);
        if (label.length() >= 6 && label.length() <= 12) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) act.getResources().getDimension(R.dimen.height_config));
            txtView.setLayoutParams(lp);
        }

        attributeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change background of selected view.
                displayAvailableOptions(v);
                changebackgroundofViews(v);

            }
        });
        return attributeView;
    }

    private void displayAvailableOptions(View v) {
        Integer id = v.getId();
        String header = findAttributeofSelectedButton(v);
        LinkedHashSet<Integer> toHideList = new LinkedHashSet<>();

        // remove selected button from removal list
        //Should never reach here
        if (!toHideList.contains(id))
            toHideList.add(id);

        // add all buttons which are not in available list of this selected button
        ArrayList<Integer> availableButtons = getAvailableButtonsforSelected(id);
        toHideList.addAll(getNonSelectedButtons(availableButtons));

//      first option never unselected
        LinkedHashSet<Integer> children = configDisplayHashMap.get(BaseOption);
        toHideList.removeAll(children); // remove base

        // remove siblings which are available
        ArrayList<Integer> availableSiblings = getAllSiblings(header);
        toHideList.removeAll(availableSiblings);

        // remove all children of upper hierarchy
        toHideList.removeAll(removeUpperHierarchyOptions(header));

        // Finally set background and remove from view
        ArrayList<Integer> toHide = new ArrayList<>(toHideList);
        setViewtoAllConfigButtons(toHide);
    }

    private LinkedHashSet<Integer> removeUpperHierarchyOptions(String header) {
        LinkedHashSet<Integer> myList = new LinkedHashSet<>();
        int end = config_option_list.indexOf(header);
        for (int i = 1; i < end; i++) {
            LinkedHashSet<Integer> list = configDisplayHashMap.get(config_option_list.get(i));
            myList.addAll(list);
        }
        return myList;
    }

    private ArrayList<Integer> getNonSelectedButtons(ArrayList<Integer> availableButtons) {
        Set<Integer> allButtons = configButtonsListMap.keySet();
        ArrayList<Integer> allButtonList = new ArrayList<>(allButtons);
        allButtonList.removeAll(availableButtons);
        return allButtonList;
    }


    private ArrayList<Integer> getAllSiblings(String header) {
        LinkedHashSet<Integer> set = configDisplayHashMap.get(header);
        ArrayList<Integer> list = new ArrayList<>(set);
        for (int i = 0; i < list.size(); i++) {
            if (finalRemoveList.contains(list.get(i)))
                list.remove(i);
        }
        return list;
    }

    private ArrayList<Integer> getAvailableButtonsforSelected(Integer id) {
        HashSet<String> available = configButtonsListMap.get(id);
        ArrayList<Integer> availableButtons = new ArrayList<>();
        Iterator<String> it = available.iterator();
        while (it.hasNext()) {
            String name = it.next();
            Integer code = Math.abs(name.hashCode());
            availableButtons.add(code);
            if (finalRemoveList.contains(code))
                finalRemoveList.remove(code);
        }
        return availableButtons;
    }

    private void setViewtoAllConfigButtons(ArrayList<Integer> hideList) {

        Set<Integer> allButtons = configButtonsListMap.keySet();
        ArrayList<Integer> allButtonList = new ArrayList<>(allButtons);
        finalRemoveList.addAll(hideList);

        for (int i = 0; i < allButtonList.size(); i++) {
            Integer btnId = allButtonList.get(i);
            if (finalRemoveList.contains(btnId)) {
                //strikeout
                FrameLayout btn = (FrameLayout) rootView.findViewById(btnId);
                btn.findViewById(R.id.config_strikethru).setVisibility(View.VISIBLE);
                btn.setEnabled(false);
                btn.setClickable(false);
            } else {
                // show
                FrameLayout btn = (FrameLayout) rootView.findViewById(btnId);
                btn.findViewById(R.id.config_strikethru).setVisibility(View.GONE);
                btn.setEnabled(true);
                btn.setClickable(true);

            }
        }

    }

    private void changebackgroundofViews(View v) {
        // find the parent option of which user has selected
        String selected_option = findAttributeofSelectedButton(v);
        clearBelowHierarchy(v);
        setColorBasedonSelected(selected_option, v);
        setSelectedValueOption(selected_option, v.getTag().toString());
    }

    private void clearBelowHierarchy(View v) {

        String header = findAttributeofSelectedButton(v);
        ArrayList<Integer> myList = new ArrayList<>();
        int start = config_option_list.indexOf(header);
        for (int i = start; i < config_option_list.size(); i++) {
            LinkedHashSet<Integer> list = configDisplayHashMap.get(config_option_list.get(i));
            myList.addAll(list);
        }
        for (int i = 0; i < myList.size(); i++) {
            Integer btn = myList.get(i);
            rootView.findViewById(btn).setBackgroundDrawable(act.getResources().getDrawable(R.drawable.custome_rectangle_border));
        }
    }


    private String findAttributeofSelectedButton(View v) {
        String selected_option = "";
        Iterator it = configDisplayHashMap.entrySet().iterator();
        while (it.hasNext()) {
            LinkedHashMap.Entry<String, LinkedHashSet<Integer>> entry = (LinkedHashMap.Entry<String, LinkedHashSet<Integer>>) it.next();
            if (entry.getValue().contains(v.getId()))
                selected_option = entry.getKey();
        }
        return selected_option;
    }

    private void setSelectedValueOption(String selected_option, String tag) {
        // Save the selected value for that particular option

        if (config_option_items.containsKey(selected_option)) {
            config_option_items.put(selected_option, tag);
        } else {
            // should logically not come ever in this else but error in case coming here
        }
    }

    private void setColorBasedonSelected(String selected_option, View v) {
        // Set color of all the children of that parent layout.
        LinkedHashSet<Integer> allButtons = configDisplayHashMap.get(selected_option);
        Iterator it2 = allButtons.iterator();

        while (it2.hasNext()) {
            Integer id = (Integer) it2.next();
            if (id == v.getId())
                v.setBackgroundDrawable(act.getResources().getDrawable(R.drawable.custom_selected_config));
            else
                rootView.findViewById(id).setBackgroundDrawable(act.getResources().getDrawable(R.drawable.custome_rectangle_border));
        }
    }

    private void hitServiceForImages() {
        //imageview_Product.setImageDrawable(SimpleProductFragment.imageDrawable);
        ll_show_progress_bar.setVisibility(View.VISIBLE);
        String urlImages = WebApiManager.getInstance().getProductImagesURL(act);
        urlImages = String.format(urlImages, PRODUCT_ID.trim());
        ServiceForImages imageService = new ServiceForImages();
        imageService.execute(urlImages);


    }

    private void setDataToUI() {
        if (currentItem != null) {
            setPrice(txtV_price, txtV_specila_price, currentItem.getPrice(), currentItem.getSpecialPrice());

            if (currentItem.getType().equalsIgnoreCase("configurable")) //configurable product
                setQuantity(1);
            else {
                if(currentItem.isInStock())
                   setQuantity(1);
                else setQuantity(0);
            }
            if (short_description.trim().length() > 1 && !short_description.equals("null")) {
                txtV_description.setText(Html.fromHtml(short_description));
            } else {
                if (description.trim().length() > 1) {
                    txtV_description.setText(Html.fromHtml(description));
                } else {
                    Activity activity = getActivity();
                    if (isAdded() && activity != null)
                        txtV_description.setText(getActivity().getResources().getString(R.string.no_description_added));
                }
            }

            if (additionalInfoList.size() > 0)
                setSpecification();
        }
        checkIfIsAddedToWishlist(false);
//        progressBar_imgLoading.setVisibility(View.GONE);
        Log.d("Progress", "Gone 5");
        ll_show_progress_bar.setVisibility(View.GONE);
        rl_pdp_load_data.setVisibility(View.VISIBLE);

        if (type.equalsIgnoreCase("configurable"))
            if (firstView != null)
                firstView.callOnClick();

        ViewTreeObserver vto = txtV_description.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Layout l = txtV_description.getLayout();
                if (l != null) {
                    int lines = l.getLineCount();
                    if (lines > 0)
                        if (l.getEllipsisCount(lines - 1) > 0) {
                            txtv_pdp_read_more_description.setVisibility(View.VISIBLE);
                            if (Build.VERSION.SDK_INT < 16) {
                                txtV_description.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                txtV_description.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        } else
                            txtv_pdp_read_more_description.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setQuantity(int stockQuantity) {
        if (stockQuantity == 0) {
            txtv_pdp_price_stock.setVisibility(View.VISIBLE);
            btn_pdp_buy_now.setClickable(false);
            txtV_add_to_cart.setClickable(false);
            btn_pdp_buy_now.setAlpha(0.5f);
            txtV_add_to_cart.setAlpha(0.5f);
        } else {
            txtv_pdp_price_stock.setVisibility(View.GONE);
            btn_pdp_buy_now.setClickable(true);
            txtV_add_to_cart.setClickable(true);
            btn_pdp_buy_now.setAlpha(1f);
            txtV_add_to_cart.setAlpha(1f);
        }
    }

    private void checkIfIsAddedToWishlist(final boolean add) {
        WishListManager.getInstance().getWishList(new Callback() {
            @Override
            public void callback(Object o, int response_code) {
                                 if(o==null){
                                     return;
                                 }
               boolean flag=false;
                ArrayList<WishListItem> list=(ArrayList<WishListItem>)o;
                for(int i=0;i<list.size();i++){
                    String pid=list.get(i).getId();
                    if(pid.equalsIgnoreCase(PRODUCT_ID)){
                        setItemAsWishListAdded();
                        flag=true;
                        break;
                    }

                }
             if(!flag)
              setItemAsWishListNotAdded();
                if(add){
                    if(!flag) {
                        addItemToUserWishlist();
                    }
                  else {setItemAsWishListNotAdded();
                        WishListManager.getInstance().deleteWishListItem(PRODUCT_ID, new Callback() {
                            @Override
                            public void callback(Object o, int response_code) {
                                if(o==null){
                                    return;
                                }

                            }
                        });
                    }
                }
            }
        });

    }

    private void setSpecification() {
        int total_fields = additionalInfoList.size();
        int count = 0;
        int loopcounter;
        String speicifcation_text = "";
        if (total_fields < 3)
            loopcounter = total_fields;
        else
            loopcounter = total_fields;

        for (int i = (loopcounter - 1); i > 0; i--) {
            if (count == 3)
                break;
            if (i != (loopcounter - 1))
                speicifcation_text += "\n";
            AdditionalInfoItem item = additionalInfoList.get(i);
            speicifcation_text += item.getAttr_label() + " : " + item.getAttr_value();
            count++;
        }
        if (total_fields >= 3)
            speicifcation_text = speicifcation_text + "...";
        if (speicifcation_text.length() > 0) {
            imgV_additional_info.setVisibility(View.VISIBLE);
            rl_pdp_specifications.setClickable(true);
            rl_pdp_specifications.setOnClickListener(this);
            txt_specification_value.setText(Html.fromHtml(description));
        }
    }

    private void setPrice(TextView txtV_price, TextView txtV_specila_price, double price, double sprice) {
        if (sprice < price && sprice > 0) {
            PRODUCT_PRICE = Utils.appendWithCurrencySymbol(sprice);
            txtV_price.setVisibility(View.VISIBLE);
            txtV_specila_price.setText(PRODUCT_PRICE);
            int disc_value = (int) (((price - sprice) / price) * 100);
            Activity activity = getActivity();
            if (isAdded() && activity != null) ;
            String off = "";
            if (isAdded() && activity != null)
                off = activity.getResources().getString(R.string.off);
            String disc_str = "";
            if (isAdded() && activity != null)
                disc_str = getActivity().getResources().getString(R.string.price_discount);
            String discount = String.format(disc_str, String.valueOf(disc_value));
            txtv_pdp_price_discount.setText(discount + " % " + off);

            setTextThrougLine(Utils.appendWithCurrencySymbol(price), txtV_price);
        } else {
            PRODUCT_PRICE = Utils.appendWithCurrencySymbol(price);
            txtV_specila_price.setText(PRODUCT_PRICE);
            txtV_price.setVisibility(View.GONE);
            rl_pdp_price_normal.setVisibility(View.GONE);
            txtv_pdp_price_discount.setVisibility(View.GONE);
        }
    }

    private void setTextThrougLine(String off_money_discounted, TextView txtTitle) {
        SpannableString discounted = new SpannableString(off_money_discounted);
        discounted.setSpan(new StrikethroughSpan(), 0, off_money_discounted.length(), 0);
        txtTitle.setText(discounted);

        String text = "dvs" + " calls";
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), 0);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txtV_additionam_info:
            case R.id.rl_pdp_specifications:
            case R.id.imgv_pdp_specifications_open:
                callAdditionalInfoFragment();
                break;
            case R.id.rl_pdp_description:
            case R.id.txtv_pdp_read_more_description:
                openDescription();
                break;
            case R.id.pdp_image_share:
                if (shareurl != "") {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Sharing Product");
                    i.putExtra(Intent.EXTRA_TEXT, shareurl);
                    startActivity(Intent.createChooser(i, "Share URL"));
                }
                break;

        }
    }


    private void openDescription() {
        txtV_description.toggle();
        txtv_pdp_read_more_description.setText(txtV_description.isExpanded() ? R.string.read_more : R.string.read_less);
    }

    private void addItemToCart(boolean b) {
        if(currentItem==null || !currentItem.isInStock()){
            Toast.makeText(act,R.string.default_error_out_of_stock,Toast.LENGTH_SHORT).show();
            return;
        }
        if (currentItem != null || reorderItem != null) {
            ShoppingCartItem item;
            if (currentItem != null) {
                item = new ShoppingCartItem(currentItem, numberOfItem);

            } else {
                item = new ShoppingCartItem(reorderItem, numberOfItem);
            }

            Collection<ShoppingCartItem> sp = ShoppingCart.getInstance().getCartItems();
            int numofcartitems = sp.size();
            if (numofcartitems != 0) {
                int FLAG = 0;
                for (int i = 0; i < sp.size(); i++) {
                    Iterator<ShoppingCartItem> it = sp.iterator();
                    while (it.hasNext()) {
                        ShoppingCartItem sci = it.next();
                        if (currentItem != null) {
                            if (sci.getShoppingItem().getId().equals(currentItem.getId())) {

                                FLAG = 0;
                                break;
                            } else {
                                FLAG = 1;
                            }
                        } else {
                            if (sci.getShoppingItem().getId().equals(reorderItem.getId())) {
                                FLAG = 0;
                                break;
                            } else {
                                FLAG = 1;
                            }
                        }
                    }

                }

                if (FLAG == 1) {
                    // item does not match any cart item
                    UserProfileItem currentuser = UserManager.getInstance().getUser();
                    if (currentuser != null && currentuser.getLogin_status() != "0")
                        ll_show_progress_bar.setVisibility(View.VISIBLE);
                    ShoppingCart.getInstance().addItem(item, this);

                } else {
//                    // Item is already added
//                    UserProfileItem currentuser = UserManager.getInstance().getUser();
//                    if(currentuser!=null && currentuser.getLogin_status()!="0")
                    callF();
                }
            } else {
                UserProfileItem currentuser = UserManager.getInstance().getUser();
                if (currentuser != null && currentuser.getLogin_status() != "0")
                    ll_show_progress_bar.setVisibility(View.VISIBLE);
                ShoppingCart.getInstance().addItem(item, this);
            }

            cartItemList = new ArrayList<>(ShoppingCart.getInstance().getCartItems());

            if (cartItemList.size() > 0) {
                MainActivity.txtV_item_counter.setText(cartItemList.size() + "");
                MainActivity.txtV_item_counter.setText(cartItemList.size() + "");
                MainActivity.txtV_item_counter.setVisibility(View.VISIBLE);
            } else {
                MainActivity.txtV_item_counter.setVisibility(View.INVISIBLE);
            }

            //  callFragment(new MyCartFragment(), "MyCartFragment");
        }


    }

    public void callF() {
        if (ll_show_progress_bar != null) {
            if (ll_show_progress_bar.getVisibility() == View.VISIBLE)
                ll_show_progress_bar.setVisibility(View.GONE);
            callFragment(new MyCartFragment(), "MyCartFragment");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cartImage != null) ;
        cartImage = null;
//        String str = SimpleProductFragment2.truncate(PRODUCT_NAME);
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            String str = getActivity().getResources().getString(R.string.product_detail_header);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,str.toUpperCase());
            //MainActivity.setHeaderText();
        }
        isFirstConfigurable = false;

    }

    @Override
    public void onStart() {
        super.onStart();
        numberOfItem = 1;
        MainActivity.showFooterOptionsMenu(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void populateConfigurableItems(JSONArray config_attributes, String cartImage, JSONArray config_options) {

        configChildrenListMap = new LinkedHashMap<>();
        configDisplayListMap = new LinkedHashMap<>();
        configDisplayHashMap = new LinkedHashMap<>();
        configButtonsListMap = new LinkedHashMap<>();
        ShoppingItem first=null;
        for (int i = 0; i < config_attributes.length(); i++) {
            try {
                JSONObject singleChild = config_attributes.getJSONObject(i);
                String pid = singleChild.getString("prod_id");
                String name = singleChild.getString("name");
                String sku = singleChild.getString("sku");
                String price = singleChild.getString("price");
                String spclprice = singleChild.getString("spclprice");
                String is_in_stock = singleChild.getString("is_in_stock");
                String stock_qty = singleChild.getString("stock_quantity");
                String type = singleChild.getString("type");
                JSONObject data = singleChild.getJSONObject("data");
                ConfigurableChild item = new ConfigurableChild(pid, name, sku, cartImage, price, spclprice, is_in_stock, stock_qty, type, cartImage, data, SimpleProductFragment2.this, PRODUCT_ID);
                // add this item to hashmap of all items
                configChildrenListMap.put(item.getKeyOFChild(), item);
                if(first==null) first=item;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("CONFIGG", "" + configChildrenListMap);
        Log.d("CONFIGG", "" + configDisplayListMap);
        Log.d("CONFIGG", "" + configButtonsListMap);
        Log.d("CONFIGG", "" + configDisplayHashMap);
   currentItem=first;
        // Create HashMap for Display options
        config_option_items = new LinkedHashMap<String, String>();
        config_option_list = new ArrayList<>();
        for (int i = 0; i < config_options.length(); i++) {
            try {
                if (i == 0) {
                    BaseOption = config_options.getString(i);
                }
                config_option_list.add(config_options.getString(i));
                config_option_items.put(config_options.getString(i), null);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        Log.d("CONFIGG", "All options header is " + config_option_items);
        createConfigurableUI();
    }

    private void createConfigurableUI() {
        rl_pdp_extra_options.setVisibility(View.VISIBLE);
        line_above_extra.setVisibility(View.VISIBLE);
        configurable_layout.setVisibility(View.VISIBLE);
        Iterator<String> it = config_option_items.keySet().iterator();
        while (it.hasNext()) {
            String label = it.next();
            createLayoutNew(label);
        }
        // Show UI and hide progressbar .
        ll_show_progress_bar.setVisibility(View.GONE);
        setDataToUI();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }

    private class ServiceForProduct extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(final String... params) {

            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(params[0], new Response.Listener<String>() {
                public String totalStr;
                public JSONArray custom_attribute_dataJSNOArray, config_attributes = null, config_options = null;
                public JSONObject custom_attributeJSNObj;

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    Log.d("Volley= ", response);
                    String links_heading = "";
                    try {
                        JSONObject strJSNobj = new JSONObject(response);
                        String name = strJSNobj.getString("name");
                        PRODUCT_NAME = name;
                        productname_pdp2.setText(PRODUCT_NAME);
                        int quantity = strJSNobj.getInt("quantity");
                        double price = strJSNobj.getDouble("price");
                        double sprice = strJSNobj.getDouble("sprice");
                        String sku = strJSNobj.getString("sku");
                        description = strJSNobj.getString("description");
                        short_description = strJSNobj.getString("shortdes");
                        String isInSTock=strJSNobj.getString("is_in_stock");
                        url = strJSNobj.getString("url");
                        shareurl = strJSNobj.getString("url");
                        img = strJSNobj.getString("img");
                        Log.d("onlyurl ", url);
                        type = strJSNobj.getString("type");
                        if (type.equals("downloadable")) {
                            downloadable_object = strJSNobj.getJSONObject("downloadable_pro_data");
                            links_heading = downloadable_object.getString("links_title");
                            downloadable_items = downloadable_object.getJSONArray("links_data");
                        }

                        if (type.equals("configurable")) {
                            if (strJSNobj.has("config_attributes"))
                                config_attributes = strJSNobj.getJSONArray("config_attributes");
                            if (strJSNobj.has("config_option"))
                                config_options = strJSNobj.getJSONArray("config_option");
                        }
                        has_custom_option = strJSNobj.getString("has_custom_option")+"";
                        custom_attributeJSNObj = strJSNobj.getJSONObject("custom_attribute");
                        totalStr = custom_attributeJSNObj.getInt("total")+"";
                        if (cartImage == null)
                            cartImage = "";
                        if (!Validation.isNull(PRODUCT_ID, name, sku, cartImage, price, sprice, quantity)) {
                            if (!type.equals("downloadable")) {
                                currentItem = new SimpleShoppingItem(PRODUCT_ID, name, sku, cartImage, ""+price, ""+sprice, isInSTock+"", ""+quantity, type, img);// edited by prashant
                                ShoppingItemManager.getInstance().addShoppingItem(currentItem);
                            } else {
                                currentItem = new DownloadableShoppingItem(PRODUCT_ID, name, sku, cartImage, ""+price, ""+sprice, "1", ""+quantity, type, checkbox_links, img);
                                ShoppingItemManager.getInstance().addShoppingItem(currentItem);
                            }
                        }

                    } catch (JSONException e) {
                        rl_no_product_found.setVisibility(View.VISIBLE);
                        isNoProduct = true;
//                    getActivity().findViewById(R.id.fotter).setVisibility(View.VISIBLE);
                        if (isAdded() && act != null)
                            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,act.getResources().getString(R.string.txt_no_product_found));
                            //MainActivity.setHeaderText(;
                        e.printStackTrace();
                    }
                    if (!isNoProduct) {
                        additionalInfoList.clear();
                        if (totalStr != null) {
                            int total = Integer.parseInt(totalStr);
                            //if there are more than 1 additional attribute
                            if (total > 0) {

                                additionalInfoList = new GetDataForAdditionalInfo().getListOfAdditionalInfo(custom_attributeJSNObj);
                            }
                        }


                        if (type.equals("configurable")) {
                            //hitServiceForConfigurableProducts();
                            populateConfigurableItems(config_attributes, cartImage, config_options);
                        } else if (type.equals("downloadable")) {
                            populateDownloadableProduct(links_heading);
                        } else {
                            setDataToUI();
//                        ll_show_progress_bar.setVisibility(View.GONE);
                            scroll_bar_show_product.setVisibility(View.VISIBLE);

                            if (has_custom_option.equals("1")) {
                                new CustomOptionFragment(PRODUCT_ID, getActivity(), rootView, SimpleProductFragment2.this);
                              //  new CustomOptionFragment(PRODUCT_ID, getActivity(), rootView, SimpleProductFragment2.this);
                                ll_custome_option.setVisibility(View.VISIBLE);
                                rl_pdp_extra_options.setVisibility(View.VISIBLE);
                                line_above_extra.setVisibility(View.VISIBLE);
                                //custom_addtocart.setVisibility(View.VISIBLE);
                                //ll_show_add_to_cart.setVisibility(View.GONE);
                            } else {
                                rl_pdp_extra_options.setVisibility(View.INVISIBLE);
                                line_above_extra.setVisibility(View.GONE);
                                ll_custome_option.setVisibility(View.GONE);
//                        ll_show_add_to_cart.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                    Log.d("Progress", "Gone 1");
                    ll_show_progress_bar.setVisibility(View.GONE);
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

    private class ServiceForImages extends AsyncTask<String, String, String> {

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
                    try {
                        JSONObject strJSNobj = new JSONObject(response);
                        JSONArray imageJSNArray = strJSNobj.getJSONArray("image");
                        imgListStrArry = new ArrayList<>();
                        for (int i = 0; i < imageJSNArray.length(); i++) {
                            if (i == 0) {
                                cartImage = imageJSNArray.getString(i);
                            }
                            imgListStrArry.add(imageJSNArray.getString(i));
                        }
                        ShoppingItem item = ShoppingItemManager.getInstance().getShoppingItem(PRODUCT_ID);
                        if (item != null) {
                            // put null checker
                            item.setImageList(imgListStrArry);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                progressBar_imgLoading.setVisibility(View.GONE);
//                Log.d("Progress", "Gone 3");
//                ll_show_progress_bar.setVisibility(View.GONE);
                    viewPager_banner.setVisibility(View.VISIBLE);
//                    if (cartImage != null && cartImage != "")
//                        imageview_Product.setVisibility(View.INVISIBLE);
                    setViewPager();
                    //productDetailBannerAdapter.notifyDataSetChanged();

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
//                progressBar_imgLoading.setVisibility(View.GONE);
                    Log.d("Progress", "Gone 4");
                    ll_show_progress_bar.setVisibility(View.GONE);
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

    private class RatingService extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(params[0], new Response.Listener<String>() {
                //            public String totalStr;
                public JSONArray reviews_array;

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    Log.d("Volley= ", response);
                    String total = "0", average = "0";
//
                    try {
                        JSONObject strJSNobj = new JSONObject(response);
                        total = strJSNobj.getString("total");
                        if (Integer.parseInt(total) != 0) {
                            average = strJSNobj.getString("average");

                            reviews_array = strJSNobj.getJSONArray("all");
                            for (int i = 0; i < reviews_array.length(); i++) {
                                ArrayList<ReviewVote> votelist = new ArrayList<>();
                                JSONObject reviewdata = reviews_array.getJSONObject(i);
                                String createdate = reviewdata.getString("createdat");
                                ;
                                String detail = reviewdata.getString("detail");
                                String title = reviewdata.getString("title");
                                String nickname = reviewdata.getString("nickname");
                                JSONArray vote = reviewdata.getJSONArray("vote");
                                for (int j = 0; j < vote.length(); j++) {
                                    JSONObject voteData = vote.getJSONObject(j);
                                    String name = voteData.getString("name");
                                    String percent = voteData.getString("percent");
                                    String value = voteData.getString("value");
                                    votelist.add(new ReviewVote(name, percent, value));
                                }
                                productReviewList.add(new ProductReview(createdate, detail, title, nickname, votelist));
                            }
                        }
                        setReviewDatatoUI(average, total);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    setReviewDatatoUI(average, total);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Product Review" + error.getMessage());

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hitServiceForRelatedProducts();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
    private class RelatedProductService extends AsyncTask<String, String, String> {
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
//                    if(pDialog.isShowing())
//                        pDialog.hide();
                    try {
                        JSONObject strJSNobj = new JSONObject(response);
                        imageJSNArray = strJSNobj.getJSONArray("products_list");
                        featuredProductListDetails = parseProducts(imageJSNArray);
                        int total = 0;
                        String totalRelated = strJSNobj.getString("total");
                        try {
                            total = Integer.parseInt(totalRelated);
                        } catch (Exception e) {
                            total = 0;
                        }
                        if (total > 0) {
                            featureProduct_Container.setVisibility(View.VISIBLE);
                        } else
                            featureProduct_Container.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        featureProduct_Container.setVisibility(View.GONE);
                    }

                    setdataToRelatedProduct();

                    Log.d("Volley Size ", featuredProductListDetails.size() + "");
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
//                    if(pDialog.isShowing())
//                        pDialog.hide();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param productId Id of the product, used in url
     * @param productName name of the product
     * @return A new instance of fragment SimpleProductFragment.
     */
    public static SimpleProductFragment2 newInstance(String productId, String productName) {
        SimpleProductFragment2 fragment = new SimpleProductFragment2();
        Bundle args = new Bundle();
        args.putString(PRODUCT_ID_PARAM, productId);
        args.putString(PRODUCT_NAME_PARAM, productName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        //Unregister the listener
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
