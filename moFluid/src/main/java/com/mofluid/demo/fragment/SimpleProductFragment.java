package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.Utils.Validation;
import com.ebizon.fluid.model.AdditionalInfoItem;
import com.ebizon.fluid.model.ConfigurableAttribute;
import com.ebizon.fluid.model.ConfigurableShoppingItem;
import com.ebizon.fluid.model.DownloadableShoppingItem;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.ShoppingCartItem;
import com.ebizon.fluid.model.ShoppingItem;
import com.ebizon.fluid.model.ShoppingItemManager;
import com.ebizon.fluid.model.SimpleShoppingItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.GetDataForAdditionalInfo;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.ProductDetailBannerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by ebizon on 18/11/15.
 */
@SuppressLint("ValidFragment")
public class SimpleProductFragment extends BaseFragment implements View.OnClickListener {
    private static final int numberOfItem = 1;
    public static String pid;
    public static ArrayList<AdditionalInfoItem> additionalInfoList;
    public static Drawable imageDrawable;
    public static String cartImage;
    private static LinearLayout linearlayout;
    private final ArrayList<ConfigurableShoppingItem> configurableItems = new ArrayList<>();
    private final HashMap<String, ConfigurableAttribute> selectedAttributeMap = new HashMap<>();
    Activity contextreorder;
    private String TAG = "";
    private String PRODUCT_ID;
    private String PRODUCT_NAME = "";
    private View rootView;
    private LinearLayout ll_show_progress_bar;
    private ScrollView scroll_bar_show_product;
    private String description;
    private String url;
    private String type;
    private String has_custom_option;
    private TextView txtV_specila_price;
    private ArrayList<ShoppingCartItem> cartItemList;
    private TextView txtV_price;
    private TextView txtV_description;
    private LinearLayout ll_custome_option;
    private ArrayList<String> imgListStrArry;
    private ProgressBar progressBar_imgLoading;
    private ViewPager viewPager_banner;
    private ImageView imageview_Product;
    private ProductDetailBannerAdapter productDetailBannerAdapter;
    private Typeface lat0_Font_ttf, open_sans_regular, open_sans_semibold;
    private TextView txtV_is_instock;
    private TextView txtV_selling;
    private TextView txtV_descripton_title;
    private TextView txtV_additionam_info;
    private TextView txtV_custome_option_title;
    private LinearLayout ll_show_add_to_cart;
    private String PRODUCT_PRICE;
    private String short_description;
    private ShoppingItem currentItem = null;
    private Activity act;
    private View view1, view2 = null;
    private TextView txtV_add_to_cart;
    private ShoppingItem reorderItem;
    private int totalConfigurable = 0;
    private int selectedConfigurable = 0;
    private int temp = 0;
    private LinearLayout custom_options;
    private TextView custom_addtocart;
    private JSONObject downloadable_object ;
    private JSONArray downloadable_items;
    private TextView download_heading;
    private LinearLayout download_layout;
    private ArrayList<CheckBox> checbox_list;
    private HashSet<String> checkbox_links;

    public SimpleProductFragment(ShoppingItem reorderItem, Activity contextfromreorder) {
        this.reorderItem = reorderItem;
        addItemToCart();
        temp = 0;
        totalConfigurable = 0;
        selectedConfigurable = 0;
        this.contextreorder = contextfromreorder;
    }

    public SimpleProductFragment() {
        temp = 0;
        totalConfigurable = 0;
        selectedConfigurable = 0;
    }

    public static String getProductID() {
        return pid;
    }

    static public String truncate(String str) {

        StringBuilder myName;
        int length = str.length();
        if (length > 25) {
            str = str.substring(0, 25);
            myName = new StringBuilder(str);
            myName.setCharAt(22, '.');
            myName.setCharAt(23, '.');
            myName.setCharAt(24, '.');
        } else
            myName = new StringBuilder(str);

        return myName.toString();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_simple_product, null);
            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
            initialized();
            gteValueFromArgument();
            getViewControls(rootView);
            MainActivity.ivAppLogo.setVisibility(View.INVISIBLE);

            String str = SimpleProductFragment.truncate(PRODUCT_NAME);
            MainActivity.product_name.setText(str);
            MainActivity.product_name.setVisibility(View.VISIBLE);
            setFontStyle();
            hitServiceForFeaturedProducts();
            hitServiceForImages();
        }

        return rootView;
    }

    private void gteValueFromArgument() {
        try {
            PRODUCT_ID = getArguments().getString(ConstantDataMember.PRO_ID);
            pid = PRODUCT_ID;
            PRODUCT_NAME = getArguments().getString(ConstantDataMember.PRO_NAME);

        } catch (Exception ex) {
        }
    }

    private void getViewControls(View rootView) {
        ll_show_progress_bar = (LinearLayout) rootView.findViewById(R.id.ll_show_progress_bar);
        scroll_bar_show_product = (ScrollView) rootView.findViewById(R.id.scroll_bar_show_product);

        txtV_specila_price = (TextView) rootView.findViewById(R.id.txtV_specila_price);
        txtV_price = (TextView) rootView.findViewById(R.id.txtV_price);
        txtV_description = (TextView) rootView.findViewById(R.id.txtV_description);
        txtV_is_instock = (TextView) rootView.findViewById(R.id.txtV_is_instock);
        ll_custome_option = (LinearLayout) rootView.findViewById(R.id.ll_custome_option);
        txtV_selling = (TextView) rootView.findViewById(R.id.txtV_selling);
        custom_addtocart=(TextView)rootView.findViewById(R.id.TxtVAddToCart);
        txtV_descripton_title = (TextView) rootView.findViewById(R.id.txtV_descripton_title);
        txtV_additionam_info = (TextView) rootView.findViewById(R.id.txtV_additionam_info);
        txtV_additionam_info.setOnClickListener(this);

        progressBar_imgLoading = (ProgressBar) rootView.findViewById(R.id.progressBar_imgLoading);
        viewPager_banner = (ViewPager) rootView.findViewById(R.id.viewPager_banner);
        imageview_Product = (ImageView) rootView.findViewById(R.id.image_product);
        
        download_layout = (LinearLayout)rootView.findViewById(R.id.downloadable_attribute);
        download_heading = (TextView)rootView.findViewById(R.id.downloadable_heading); 


        ll_show_add_to_cart = (LinearLayout) rootView.findViewById(R.id.ll_show_add_to_cart);
        custom_options=(LinearLayout)rootView.findViewById(R.id.custom_llayout);


        ImageView imgV_additional_info = (ImageView) rootView.findViewById(R.id.imgV_additional_info);
        imgV_additional_info.setOnClickListener(this);




        txtV_add_to_cart = (TextView) rootView.findViewById(R.id.txtV_add_to_cart);


        txtV_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (!Utils.checkCartItemforDownloadable(new ArrayList<ShoppingCartItem>(ShoppingCart.getInstance().getCartItems())))
                    {
                        if (type != null) {
                            if (!type.equals("configurable") && !type.equals("downloadable")) {
                                addItemToCart();
                            } else if (type.equals("downloadable")) {
                                if (validateDownloadOptions() == false) {
                                    Activity activity = getActivity();
                                    if(isAdded()&&activity!=null) {
                                        final Toast toast = Toast.makeText(act, act.getResources().getString(R.string.configurable_message), Toast.LENGTH_LONG);
                                        toast.show();
                                        cancelToast(toast);
                                        download_layout.setBackground(getActivity().getResources().getDrawable(R.drawable.background_downloadable_product_unselected));
                                    }
                                } else {
                                    if(ShoppingCart.getInstance().getCartItems().size()==0) {
                                        setDownloadableCheckList();
                                        addItemToCart();
                                    }
                                    else
                                    {
                                        Activity activity = getActivity();
                                        if(isAdded()&&activity!=null) {
                                            final Toast toast = Toast.makeText(act, act.getResources().getString(R.string.one_downloadable_allowed), Toast.LENGTH_LONG);
                                            toast.show();
                                            cancelToast(toast);
                                        }
                                    }
                                }
                            } else {
                                if (view1 != null || view2 != null) {
                                    addItemToCart();
                                } else {
                                    Activity activity = getActivity();
                                    if(isAdded()&&activity!=null) {
                                        final Toast toast = Toast.makeText(act, act.getResources().getString(R.string.configurable_message), Toast.LENGTH_LONG);
                                        toast.show();
                                        cancelToast(toast);
                                    }
                                }
                            }
                        } else {
                            Activity activity = getActivity();
                            if(isAdded()&&activity!=null) {
                                final Toast toast = Toast.makeText(act, act.getResources().getString(R.string.still_loading_detail), Toast.LENGTH_LONG);
                                toast.show();
                                cancelToast(toast);
                            }
                        }
            } // end of if
                else {
                        if (currentItem.getType().toString().equals("downloadable")) {
                            setDownloadableCheckList();
                            addItemToCart();
                        }
                        else {
                            Activity activity = getActivity();
                            if(isAdded()&&activity!=null) {
                                final Toast toast = Toast.makeText(act, act.getResources().getString(R.string.one_downloadable_allowed), Toast.LENGTH_LONG);
                                toast.show();
                                cancelToast(toast);
                            }
                        }
                    }
        }
        });
    }

    private void setDownloadableCheckList() {
        for(CheckBox chk : checbox_list)
        {
            if(chk.isChecked())
            {
                checkbox_links.add(String.valueOf(chk.getId()));
            }
        }

    }

    private boolean validateDownloadOptions() {
        for (int q = 0; q < checbox_list.size(); q++) {
            if(checbox_list.get(q).isChecked()==true)
              return true;
        }
        return false;
    }

    private void addItemToCart() {
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
                                Activity activity = getActivity();
                                if(isAdded()&&activity!=null) {
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
                }
            } else {
                ShoppingCart.getInstance().addItem(item, this);
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
            //new MyCartFragment().setCounterItemAddedCart(cartItemList);
            //callFragment(new MyCartFragment());
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
        lat0_Font_ttf = Typeface.createFromAsset(act.getAssets(), ConstantDataMember.REGULAR_FONT_STYLE);
        open_sans_semibold = Typeface.createFromAsset(act.getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
        open_sans_regular = Typeface.createFromAsset(act.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
        additionalInfoList = new ArrayList<>();
        checbox_list= new ArrayList<>();
        checkbox_links= new HashSet<>();
    }

    private void setFontStyle() {
        txtV_specila_price.setTypeface(open_sans_semibold);
        txtV_price.setTypeface(open_sans_semibold);
        txtV_description.setTypeface(open_sans_regular);
        txtV_is_instock.setTypeface(open_sans_regular);
        txtV_selling.setTypeface(open_sans_regular);
        txtV_descripton_title.setTypeface(open_sans_regular);
        txtV_additionam_info.setTypeface(open_sans_regular);

    }

    private void setViewPager() {
        //productDetailBannerAdapter = new ProductDetailBannerAdapter(act, imgListStrArry, progress_image);
        FullScreenImageDialog.FLAG = 1;
        if (imgListStrArry != null)
            viewPager_banner.setAdapter(productDetailBannerAdapter);
        else
            SimpleProductFragment.cartImage=null;
    }

    private void hitServiceForFeaturedProducts() {
        String urlFeatureProducts = WebApiManager.getInstance().getProductDetailURL(act);
        urlFeatureProducts = String.format(urlFeatureProducts, PRODUCT_ID.trim());
        Log.d("productdetails", urlFeatureProducts);
        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(urlFeatureProducts, new Response.Listener<String>() {
            public String totalStr;
            public JSONArray custom_attribute_dataJSNOArray;
            public JSONObject custom_attributeJSNObj;

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                Log.d("Volley= ", response);
                String links_heading="";

                try {
                    JSONObject strJSNobj = new JSONObject(response);
                    String name = strJSNobj.getString("name");
                    String quantity = strJSNobj.getString("quantity");
                    String price = strJSNobj.getString("price");
                    String sprice = strJSNobj.getString("sprice");
                    String sku = strJSNobj.getString("sku");
                    description = strJSNobj.getString("description");
                    short_description = strJSNobj.getString("shortdes");

                    url = strJSNobj.getString("url");
                    Log.d("onlyurl ", url);
                    type = strJSNobj.getString("type");
                    if(type.equals("downloadable"))
                    {
                        downloadable_object = strJSNobj.getJSONObject("downloadable_pro_data");
                        links_heading = downloadable_object.getString("links_title");
                        downloadable_items= downloadable_object.getJSONArray("links_data");
                    }
                    has_custom_option = strJSNobj.getString("has_custom_option");
                    custom_attributeJSNObj = strJSNobj.getJSONObject("custom_attribute");
                    custom_attribute_dataJSNOArray = custom_attributeJSNObj.getJSONArray("data");
                    totalStr = custom_attributeJSNObj.getString("total");
                    if(cartImage==null)
                        cartImage="";
                    if (!Validation.isNull(PRODUCT_ID, name, sku, cartImage, price, sprice, quantity)) {
                        if(!type.equals("downloadable"))
                        {
                            currentItem = new SimpleShoppingItem(PRODUCT_ID, name, sku, cartImage, price, sprice, "1", quantity, type,url);// edited by prashant
                            ShoppingItemManager.getInstance().addShoppingItem(currentItem);
                        }
                        else
                        {
                            currentItem = new DownloadableShoppingItem(PRODUCT_ID, name, sku, cartImage, price, sprice, "1", quantity, type, checkbox_links,url);                            ShoppingItemManager.getInstance().addShoppingItem(currentItem);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                additionalInfoList.clear();
                if (totalStr != null) {
                    int total = Integer.parseInt(totalStr);
                    //if there are more than 1 additional attribute
                    if (total > 0)
                        additionalInfoList = new GetDataForAdditionalInfo().getListOfAdditionalInfo(custom_attributeJSNObj);
                }

                setDataToUI();

                if (type.equals("configurable")) {
                    hitServiceForConfigurableProducts();
                }
                else if(type.equals("downloadable"))
                {
                    populateDownloadableProduct(links_heading);
                }
                else {
                    ll_show_progress_bar.setVisibility(View.GONE);
                    scroll_bar_show_product.setVisibility(View.VISIBLE);

                    if (has_custom_option.equals("1")) {
                        //new CustomOptionFragment(PRODUCT_ID, getActivity(), rootView, SimpleProductFragment.this);
                        ll_custome_option.setVisibility(View.VISIBLE);
                        custom_addtocart.setVisibility(View.VISIBLE);
                        ll_show_add_to_cart.setVisibility(View.GONE);
                    } else {
                        ll_custome_option.setVisibility(View.GONE);
                        ll_show_add_to_cart.setVisibility(View.VISIBLE);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());

            }
        });
    }

    private void populateDownloadableProduct(String heading) {
        if(downloadable_items!=null)
        {
            LinearLayout checkbox_layout = new LinearLayout(getActivity());
            checkbox_layout.setOrientation(LinearLayout.VERTICAL);
            checkbox_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            checkbox_layout.setPadding(5,10,5,5);
            download_heading.setText(heading);
            download_layout.setVisibility(View.VISIBLE);
            for (int i = 0; i <downloadable_items.length(); i++)
            {
                try
                {
                    JSONObject object = downloadable_items.getJSONObject(i);
                    String title = object.getString("title");
                    String id2 = object.getString("link_id");
                    int id = Integer.parseInt(id2);
                    //add new checkbox
                    CheckBox chk = new CheckBox(getActivity());
                    chk.setId(id);
                    chk.setText(title);
                    chk.setPadding(5,5,5,5);
                    checkbox_layout.addView(chk);
                    checbox_list.add(chk);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
            Activity activity = getActivity();
            if(isAdded()&&activity!=null) {
                checkbox_layout.setBackgroundColor(getActivity().getResources().getColor(R.color.app_color));
                download_layout.addView(checkbox_layout);
                setCheckBoxClick();
            }
        }
        else
        {
            download_layout.setVisibility(View.INVISIBLE);
        }

    }

    private void setCheckBoxClick() {
        for (int i = 0; i < checbox_list.size() ; i++) {
            checbox_list.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = getActivity();
                    if(isAdded()&&activity!=null)
                    download_layout.setBackground(getActivity().getResources().getDrawable(R.drawable.background_downloadable_product));

                }
            });

        }
    }

    private void hitServiceForConfigurableProducts() {
        Activity context = act;
        if (context != null) {
            url = WebApiManager.getInstance().getConfigurableProductURL(act);
            url = String.format(url, PRODUCT_ID.trim());
            Log.d(TAG, url);
            configurableItems.clear();
        }

        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Iterator iter = jsonObject.keys();
                    while (iter.hasNext()) {
                        String key = (String) iter.next();
                        JSONArray jsonArray = jsonObject.getJSONArray(key);
                        ConfigurableShoppingItem configurableShoppingItem = ConfigurableShoppingItem.create(jsonArray,"");
                        if (!Validation.isNull(configurableShoppingItem)) {
                            configurableItems.add(configurableShoppingItem);
                            ShoppingItemManager.getInstance().addShoppingItem(configurableShoppingItem);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (act != null)
                    populateConfigurableAttribute();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: Configurable product service " + error.getMessage());
            }
        });
    }

    private void populateConfigurableAttribute() {
        HashMap<String, Set<String>> allAttributes = getAllAttributes();
        totalConfigurable = allAttributes.size();
        selectedConfigurable = 0;
        for (Map.Entry<String, Set<String>> attribute : allAttributes.entrySet()) {
            String labelName = attribute.getKey();
            selectedConfigurable++;
            if (act != null) {
                LinearLayout layout = createLayout(labelName);
                linearlayout = layout;

                for (String label : attribute.getValue()) {
                    addAttributeView(layout, labelName, label);
                }
            }
        }
    }

    private void clearAll() {
        for (int i = 0; i < linearlayout.getChildCount(); i++) {
            Activity activity = getActivity();
            if(isAdded()&&activity!=null)
            linearlayout.getChildAt(i).findViewById(R.id.configurable_attribute).setBackground(getResources().getDrawable(R.drawable.custome_rectangle_border));
        }
    }

    private LinearLayout createLayout(String attributeName) {
        LinearLayout configurableAttribute = (LinearLayout) this.rootView.findViewById(R.id.configurable_attribute);
        if (act != null) {
            LinearLayout layout = new LinearLayout(act);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(4, 4, 4, 4);
            layout.setLayoutParams(layoutParams);
            temp++;
            layout.setId(temp);

            TextView textView = new TextView(act);
            Activity activity = getActivity();
            if(isAdded()&&activity!=null) {
            String attributeStr = String.format(act.getResources().getString(R.string.Select), attributeName);
            textView.setText(attributeStr);}

            configurableAttribute.addView(textView);
            configurableAttribute.addView(layout);
            //temp++;
            return layout;
        } else
            return null;
    }

    private void addAttributeView(final LinearLayout layout, String labelName, String text) {
        LayoutInflater layoutInflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View attributeView = layoutInflater.inflate(R.layout.configurable_attribute, null);
        TextView txtView = (TextView) attributeView.findViewById(R.id.configurable_attribute);
        attributeView.setId(text.hashCode());
        attributeView.setTag(labelName.hashCode());
        txtView.setText(text);
        layout.addView(attributeView);
        int number_of_child = layout.getChildCount();
        attributeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Activity activity = getActivity();
                if(isAdded()&&activity!=null)
                setBackground(layout, view);//created by prashant
                int attributeNameTag = (int) view.getTag();
                int attributeValueTag = view.getId();
                ConfigurableAttribute attribute = getConfigurableAttribute(attributeNameTag, attributeValueTag);
                if (attribute != null) {
                    selectedAttributeMap.put(attribute.getLabel(), attribute);
                }
                ConfigurableShoppingItem foundItem = findSelectedProduct();
                if (foundItem != null) {
                    currentItem = foundItem;
                    ll_show_add_to_cart.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    //method created by prashant
    public void setBackground(LinearLayout layout, View view) {

        int number_of_child = layout.getChildCount();
        int parentId = ((View) view.getParent()).getId();
        int layoutId = layout.getId();


        if (view1 != null && ((View) view1.getParent()).getId() == parentId) {
//            int view1parentID = ((View) view1.getParent()).getId();
//
//            if (view1parentID == parentId) {
                view1.findViewById(R.id.configurable_attribute).setBackground(getResources().getDrawable(R.drawable.custome_rectangle_border));
//            }
        }

        if (view2 != null && ((View) view2.getParent()).getId() == parentId) {
//            int view1parentID = ((View) view1.getParent()).getId();
//
//            if (view1parentID == parentId) {
            view2.findViewById(R.id.configurable_attribute).setBackground(getResources().getDrawable(R.drawable.custome_rectangle_border));
//            }
        }

        for(int i = 0; i < number_of_child; i++) {
            if (view.getId() == layout.getChildAt(i).getId() && layoutId == parentId) {
                view.findViewById(R.id.configurable_attribute).setBackgroundColor(Color.parseColor("#87CEFA"));
                if (view1 != null) {
                    if (parentId == ((View) view1.getParent()).getId())
                        view1 = view;
                    else
                        view2=view;
                }
                else
                    view1 = view;

            }
        }

    }

    private HashMap<String, Set<String>> getAllAttributes() {
        HashMap<String, Set<String>> attributesMap = new HashMap<>();
        for (ConfigurableShoppingItem item : configurableItems) {
            Collection<ConfigurableAttribute> attributes = item.getAllAttributes();
            for (ConfigurableAttribute attribute : attributes) {
                Set<String> set = attributesMap.get(attribute.getLabel());
                if (set == null) {
                    set = new HashSet<String>();
                    attributesMap.put(attribute.getLabel(), set);
                }
                set.add(attribute.getLabelValue());
            }
        }

        return attributesMap;
    }

    private ConfigurableAttribute getConfigurableAttribute(int attributeNameTag, int attributeValueTag) {
        ConfigurableAttribute foundAttribute = null;
        HashMap<String, Set<String>> allAttributes = getAllAttributes();

        for (Map.Entry<String, Set<String>> attribute : allAttributes.entrySet()) {
            String labelName = attribute.getKey();
            if (labelName.hashCode() == attributeNameTag) {
                for (String label : attribute.getValue()) {
                    if (label.hashCode() == attributeValueTag) {
                        foundAttribute = findAttribute(labelName, label);
                        break;
                    }
                }
            }
        }

        return foundAttribute;
    }

    private ConfigurableAttribute findAttribute(String attributeName, String attributeValue) {
        ConfigurableAttribute foundAttribute = null;
        for (ConfigurableShoppingItem item : configurableItems) {
            for (ConfigurableAttribute attribute : item.getAllAttributes()) {
                if (attributeName.equals(attribute.getLabel()) && attributeValue.equals(attribute.getLabelValue())) {
                    foundAttribute = attribute;
                    break;
                }
            }
        }

        return foundAttribute;
    }

    private ConfigurableShoppingItem findSelectedProduct() {
        ConfigurableShoppingItem foundItem = null;
        for (ConfigurableShoppingItem item : configurableItems) {
            int count = 0;
            for (ConfigurableAttribute attribute : item.getAllAttributes()) {
                for (ConfigurableAttribute selectedAttribute : selectedAttributeMap.values()) {
                    if (selectedAttribute.equals(attribute)) {
                        count++;
                    }
                    if (count == item.getAllAttributes().size()) {
                        foundItem = item;
                        break;
                    }
                }
            }
        }

        return foundItem;
    }

    private void hitServiceForImages() {
        imageview_Product.setImageDrawable(SimpleProductFragment.imageDrawable);

        String urlImages = WebApiManager.getInstance().getProductImagesURL(act);
        urlImages = String.format(urlImages, PRODUCT_ID.trim());

        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(urlImages, new Response.Listener<String>() {

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

                progressBar_imgLoading.setVisibility(View.GONE);
                viewPager_banner.setVisibility(View.VISIBLE);
                if (cartImage != null &&cartImage!="")
                        imageview_Product.setVisibility(View.INVISIBLE);
                setViewPager();
                productDetailBannerAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
            }
        });
    }

    private void setDataToUI() {
        if (currentItem != null) {
            setPrice(txtV_price, txtV_specila_price, currentItem.getPrice(), currentItem.getSpecialPrice());


            if (short_description.trim().length() > 1) {
                txtV_description.setText(short_description);
            } else {
                txtV_description.setText(description);
            }
        }
    }

    private void setPrice(TextView txtV_price, TextView txtV_specila_price, double price, double sprice) {

        if (sprice < price && sprice > 0) {
            PRODUCT_PRICE = Utils.appendWithCurrencySymbol(sprice);
            txtV_price.setVisibility(View.VISIBLE);
            txtV_specila_price.setText(PRODUCT_PRICE);

            setTextThrougLine(Utils.appendWithCurrencySymbol(price), txtV_price);
        } else {
            PRODUCT_PRICE = Utils.appendWithCurrencySymbol(price);
            txtV_specila_price.setText(PRODUCT_PRICE);
            txtV_price.setVisibility(View.GONE);
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
            case R.id.imgV_additional_info:
                callAdditionalInfoFragment();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(cartImage!=null);
        cartImage=null;
    }
}