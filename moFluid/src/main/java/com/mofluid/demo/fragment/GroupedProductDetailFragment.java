package com.mofluid.magento2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.Validation;
import com.ebizon.fluid.model.GroupedProductDetailItem;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.ShoppingCartItem;
import com.ebizon.fluid.model.ShoppingItem;
import com.ebizon.fluid.model.SimpleShoppingItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.GroupedProductDetailListAdapter;
import com.mofluid.magento2.adapter.ProductDetailBannerAdapter;
import com.mofluid.magento2.custome.widget.ShowAlertDialogBox;
import com.mofluid.magento2.fragment.BaseFragment;
import com.mofluid.magento2.service.AppController;
import com.mofluid.magento2.service.MyCommanMethod;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by ebizon on 19/11/15.
 */
public class GroupedProductDetailFragment extends BaseFragment {
    private  String TAG = "";
    private  String PRODUCT_ID;
    private String PRODUCT_NAME;
    private View rootView;
    private LinearLayout ll_show_progress_bar;
    private ScrollView scroll_bar_show_product;
    private String type;
    private String status;
    private TextView txtV_groped;
    private TextView txtV_group_item;
    private TextView txtV_description;
    private ArrayList<String> imgListStrArry;
    private ProgressBar progressBar_imgLoading;
    private ViewPager viewPager_banner;
    private ProductDetailBannerAdapter productDetailBannerAdapter;
    private Typeface lat0_Font_ttf;
    private TextView txtV_is_instock;
    private TextView txtV_descripton_title;
    private ListView gridV_grouped_item;
    private  ArrayList<GroupedProductDetailItem>  groupedProductDetailArrayList;
    private String grouped_pro_status;
    private String grouped_sku;
    private  String grouped_status;
    private String grouped_general_name;
    private String grouped_general_sku;
    private  String grouped_general_weight;
    private  String grouped_price_final;
    private  String grouped_price_regular;
    private  String grouped_image;
    private  String grouped_stock_item_id;
    private  String grouped_stock_product_id;
    private  String grouped_stock_stock_id;
    private  String grouped_stock_qty;
    private  String grouped_stock_is_in_stock;
    private String description_short;
    private String description_pull ;
    private GroupedProductDetailListAdapter groupedProductDetailListAdapter;
    public HashMap<String,ShoppingCartItem> addToCartItemMapList;
    private String grouped_id;
    private TextView txtV_add_to_cart;
    private String show_stock_status;

    private int no_of_item_out_of_stock_counter;
    private boolean allItemOutOfStock;
    private ShowAlertDialogBox showAlertDialogBoxObj;
    private ArrayList<ShoppingCartItem> cartItemList;
    private Activity activity;
    private ImageView defaultImage;
    private ImageButton pdp_image_share;
    private String shareUrl;
    private   String productId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_grouped_product_detail, null);

            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
            initialized();
            gteValueFromArgument();

            MainActivity.ivAppLogo.setVisibility(View.VISIBLE);
            MainActivity.product_name.setVisibility(View.INVISIBLE);
            MainActivity.product_name.setText(activity.getString(R.string.product_detail_header));
            getViewControlls(rootView);
            setFontStyle();

            hitServiceForFeatruredProducts();
            hitServiceForRelatedProducts();
            addToCartItemMapList.clear();

            hitServiceForImages();
        }

        txtV_add_to_cart.setBackgroundColor(MyCommanMethod.getColor(getActivity(), R.color.app_color_light));
        txtV_add_to_cart.setEnabled(false);

        gridV_grouped_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {

                View v = gridV_grouped_item.getChildAt(position);
                ImageView imgV_plus = (ImageView) v.findViewById(R.id.imgV_plus);
                ImageView imgV_minus = (ImageView) view.findViewById(R.id.imgV_minus);

                //Toast.makeText(getActivity(), "position "+position, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onItemClick() called with: " + "adapterView = [" + adapterView + "], view = [" + view + "], position = [" + position + "], l = [" + l + "]");

                imgV_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GroupedProductDetailItem itemObj = groupedProductDetailArrayList.get(position);
                        //getGroupId means item id
                        int count = ShoppingCart.getInstance().getCount(itemObj.getGrouped_id());
                        if (count > 0) {
                            Activity activity = getActivity();
                            if(isAdded()&&activity!=null)
                                showAlertDialogBoxObj.showCustomeDialogBox(getActivity(), getActivity().getResources().getString(R.string.app_name), getActivity().getResources().getString(R.string.item_already_added_to_cart));
                        } else {
                            itemObj.setNo_of_produc(addItemToCartList(itemObj));
                            groupedProductDetailListAdapter.notifyDataSetChanged();
                            //Enable if item put on cart List else disable
                            enableDisableAddToCartButton();
                        }
                    }
                });
                imgV_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GroupedProductDetailItem itemObj = groupedProductDetailArrayList.get(position);

                        itemObj.setNo_of_produc(removeItemToCartList(itemObj));
                        groupedProductDetailListAdapter.notifyDataSetChanged();

                        //Enable if item put on cart List else disable
                        enableDisableAddToCartButton();
                    }
                });

            }
        });

        txtV_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collection<ShoppingCartItem> itemsToAdd = addToCartItemMapList.values();
                for (ShoppingCartItem item : itemsToAdd) {
                    ShoppingCart.getInstance().addItem(item);
                }
                MyCartFragment frgment = new MyCartFragment();
                callFragment(frgment);
            }
        });
        return  rootView;
    }

    private int removeItemToCartList(GroupedProductDetailItem itemObj) {
        int no_of_item=itemObj.getNo_of_produc();
        Log.d(TAG, "getItemDetal() called with: " + "item_quantity = [" + itemObj.getGrouped_stock_qty() + "]");
        Log.d(TAG, "removeItemToCartList() called with: " + "no_of_item = [" + no_of_item + "]");
        if(no_of_item>0) {
            no_of_item--;
            //getGroupId means item id
            addToCartItemMapList.put(itemObj.getGrouped_id(), getItemDetal(itemObj, no_of_item));
        }
        else
        {
            Activity activity = getActivity();
            if(isAdded()&&activity!=null)
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.you_are_exceeded_number_products), Toast.LENGTH_SHORT).show();
        }
        return  no_of_item;

    }

    private ShoppingCartItem getItemDetal(GroupedProductDetailItem item, int no_of_item) {
        ShoppingItem shoppingItem = new SimpleShoppingItem(item.getGrouped_id(), item.getGrouped_general_name(), item.getGrouped_sku(), item.getGrouped_image(),
                item.getGrouped_price_regular(), item.getGrouped_price_final(), item.getGrouped_stock_is_in_stock(), item.getGrouped_stock_qty(), item.getType(),"");
        ShoppingCartItem shoppingCartItem =new ShoppingCartItem( shoppingItem, no_of_item);
        addToCartItemMapList.put(item.getGrouped_id(), shoppingCartItem);

        return shoppingCartItem;
    }

    private void enableDisableAddToCartButton() {
        boolean isItemAddedToCart=false;
        Collection<ShoppingCartItem> items = addToCartItemMapList.values();

        for(ShoppingCartItem item : items){
            isItemAddedToCart = item.getShoppingItem().getStockQuantity() > 0;
            if(isItemAddedToCart)
                break;
        }

        if(isItemAddedToCart) {
            txtV_add_to_cart.setBackgroundColor(MyCommanMethod.getColor(getActivity(), R.color.app_color));
            txtV_add_to_cart.setClickable(true);
            txtV_add_to_cart.setEnabled(true);
        } else {
            txtV_add_to_cart.setBackgroundColor(MyCommanMethod.getColor(getActivity(), R.color.app_color_light));
            txtV_add_to_cart.setClickable(false);
            txtV_add_to_cart.setEnabled(false);
        }
    }

    private int addItemToCartList(GroupedProductDetailItem itemObj) {
        int no_of_item=itemObj.getNo_of_produc();
        float item_quantity=0;
        try {
            item_quantity = Float.parseFloat(itemObj.getGrouped_stock_qty());
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

        Log.d(TAG, "addItemToCartList() called with: " + "item_quantity = [" + item_quantity + "]");

        if(item_quantity>no_of_item) {
            no_of_item++;
            addToCartItemMapList.put(itemObj.getGrouped_id(), getItemDetal(itemObj, no_of_item));
        } else {
            Activity activity = getActivity();
            if(isAdded()&&activity!=null)
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.you_are_exceeded_number_products), Toast.LENGTH_SHORT).show();
        }

        return  no_of_item;
    }

    private void gteValueFromArgument() {
        try {
            PRODUCT_ID=getArguments().getString(ConstantDataMember.PRO_ID);
        }catch (Exception ex) {
        }

        try {
            PRODUCT_NAME=getArguments().getString(ConstantDataMember.PRO_NAME);
        }catch (Exception ex) {
        }
    }
    private void hitServiceForRelatedProducts() {
        String urlRelatedProducts = WebApiManager.getInstance().getRelatedProductsURL(getActivity());
        urlRelatedProducts= String.format(urlRelatedProducts,PRODUCT_ID);
    /*    RelatedProductService relatedProductService=new RelatedProductService(activity,GroupedProductDetailFragment.this);
        relatedProductService.execute(urlRelatedProducts);*/

    }
  /*  public void callBackRelatedProducts(JSONArray jsonArray) {
        View view=(View)rootView.findViewById(R.id.grouped_relatedProducts_layout);
        if(jsonArray!=null) {
            ArrayList<ShoppingItem> relatedProducts = parseProducts(jsonArray);
            int size = relatedProducts.size();
            if (size > 0) {
                view.setVisibility(View.VISIBLE);
                setLayoutForGroupedProducts(relatedProducts);
            }else{
                view.setVisibility(View.GONE);
            }
        }else{
            view.setVisibility(View.GONE);
        }
    }*/

 /*   private void setLayoutForGroupedProducts(final ArrayList<ShoppingItem> relatedProducts) {
        use recycler view
         listView=()rootView.findViewById(R.id.hlvCustomListNewFeature);
        LayoutInflater inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.row_grouped_products,null);
        final NewFeatureAdapter newFeatureAdapter = new NewFeatureAdapter(getActivity(), relatedProducts,view);
        listView.setAdapter(newFeatureAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShoppingItem item = (ShoppingItem) newFeatureAdapter.getItem(position);
                BaseFragment frmnt = new SimpleProductFragment2();
                Bundle bundle = new Bundle();
                bundle.putString(ConstantDataMember.PRO_ID, item.getId());
                bundle.putString(ConstantDataMember.PRO_NAME, item.getName());
                ImageView img = (ImageView) view.findViewById(R.id.item_image);
                SimpleProductFragment.imageDrawable=img.getDrawable();
                frmnt.setArguments(bundle);
                callFragment(frmnt, frmnt.getClass().getSimpleName());


            }
        });
    }*/

    private ArrayList<ShoppingItem> parseProducts(JSONArray jsonArray){
        ArrayList<ShoppingItem> items = new ArrayList<>();

        for(int i=0; i<jsonArray.length(); ++i) {
            try {
                ShoppingItem item = ShoppingItem.create(jsonArray.getJSONObject(i));
                if(item != null){
                    items.add(item);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return items;
    }

    private void getViewControlls(View rootView) {
        ll_show_progress_bar=(LinearLayout) rootView.findViewById(R.id.ll_show_progress_bar);
        scroll_bar_show_product=(ScrollView) rootView.findViewById(R.id.scroll_bar_show_product);

        txtV_groped =(TextView) rootView.findViewById(R.id.txtV_groped);
        txtV_group_item =(TextView) rootView.findViewById(R.id.txtV_group_item);
        txtV_description=(TextView) rootView.findViewById(R.id.txtV_description);
        txtV_is_instock=(TextView) rootView.findViewById(R.id.txtV_is_instock);
        txtV_add_to_cart=(TextView) rootView.findViewById(R.id.txtV_add_to_cart);
       /* final EditText edtV_pinCode=(EditText) rootView.findViewById(R.id.edt_pincode);
        TextView txtV_checkPin=(TextView) rootView.findViewById(R.id.check_pin_button);
        final TextView txtV_pinStatus=(TextView) rootView.findViewById(R.id.pin_status);*/
        /*txtV_checkPin.setOnClickListener(new View.RecyclerViewClickListener() {
            @Override
            public void onClick(View v) {
                String pincode=edtV_pinCode.getText().toString();
                pincode=pincode.trim();
                int length=pincode.length();
                if(Validation.isStrNotEmpty(pincode)) {
                    if (length == 6) {
                        ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
                        pDialog.show();
                        pDialog.setCancelable(false);
                        String url = WebApiManager.getInstance().getCheckPinCodeURL(activity);
                        url=String.format(url,pincode);
                        ServiceValidatePin serviceValidatePin = new ServiceValidatePin(activity, GroupedProductDetailFragment.this, txtV_pinStatus, pincode, pDialog);
                        serviceValidatePin.execute(url);
                    }else{
                        txtV_pinStatus.setVisibility(View.VISIBLE);
                        txtV_pinStatus.setText(getActivity().getResources().getString(R.string.invalidPin));
                        txtV_pinStatus.setTextColor(getActivity().getResources().getColor(R.color.font_red_color));
                    }
                }else{
                    Toast.makeText(getActivity(),"Please Enter Pincode",Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        txtV_descripton_title=(TextView) rootView.findViewById(R.id.txtV_descripton_title);
        pdp_image_share = (ImageButton) rootView.findViewById(R.id.pdp_image_share);
        pdp_image_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareUrl != "") {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Sharing Product");
                    i.putExtra(Intent.EXTRA_TEXT, shareUrl);
                    startActivity(Intent.createChooser(i, "Share URL"));
                }
            }
        });


        progressBar_imgLoading=(ProgressBar) rootView.findViewById(R.id.progressBar_imgLoading);
        viewPager_banner=(ViewPager) rootView.findViewById(R.id.viewPager_pdp_images);
        defaultImage=(ImageView)rootView.findViewById(R.id.default_image_pdp_product);
        TextView product_name=(TextView)rootView.findViewById(R.id.txtv_pdp_product_name);
        product_name.setText(PRODUCT_NAME);

        gridV_grouped_item=(ListView) rootView.findViewById(R.id.gridV_grouped_item);





    }


    private void initialized() {
        TAG=getClass().getSimpleName();
        lat0_Font_ttf = Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.REGULAR_FONT_STYLE);
        groupedProductDetailArrayList=new ArrayList<>();
        addToCartItemMapList=new HashMap<>();
        showAlertDialogBoxObj=new ShowAlertDialogBox();
        activity=getActivity();
    }

    private void setFontStyle() {
        txtV_groped.setTypeface(lat0_Font_ttf);
        txtV_group_item.setTypeface(lat0_Font_ttf);
        txtV_description.setTypeface(lat0_Font_ttf);
        txtV_is_instock.setTypeface(lat0_Font_ttf);
        txtV_descripton_title.setTypeface(lat0_Font_ttf);

    }

    private void setViewPager() {
        productDetailBannerAdapter = new ProductDetailBannerAdapter(getActivity(), imgListStrArry, PRODUCT_ID);
        FullScreenImageDialog.FLAG = 1;
        if (imgListStrArry != null) {
            defaultImage.setVisibility(View.GONE);
            viewPager_banner.setAdapter(productDetailBannerAdapter);
        } else {
            defaultImage.setVisibility(View.VISIBLE);
        }

    }

    private void hitServiceForFeatruredProducts() {
        String urlFeatureProducts = WebApiManager.getInstance().getGroupedProductURL(getActivity());
        urlFeatureProducts = String.format(urlFeatureProducts, PRODUCT_ID);
        progressBar_imgLoading.setVisibility(View.VISIBLE);

        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(urlFeatureProducts, new Response.Listener<String>() {
            public JSONObject descriptionJSNObj;
            public JSONObject groupedSNObj;
            public JSONArray groupedSNArray;
            public JSONObject productsJSNobj;
            public JSONObject stockJSNobj;
            //   public String imageJSNArray;
            public String priceJSNobj;
            public String productQuantity;
            public JSONObject generalJSNobj;
            JSONObject JSNobj = null;


            @Override
            public void onResponse(String response) {

                groupedProductDetailArrayList.clear();
                Log.d(TAG, response);

                try {
                    JSNobj =new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    type=JSNobj.getString("type");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    String productId=JSNobj.getString("id");
                    setProductID(productId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    shareUrl=JSNobj.getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    show_stock_status=JSNobj.getString("isInStock");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    status=JSNobj.getString("status");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    description_pull=JSNobj.getString("description");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    description_short=JSNobj.getString("shortdes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               /* try {
                    description_pull=descriptionJSNObj.getString("full");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
*/
                try {
                    productsJSNobj =JSNobj.getJSONObject("products");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    groupedSNArray =JSNobj.getJSONArray("groupdata");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                no_of_item_out_of_stock_counter=0;

                //if status ==1 then show item in list
                if(status.equalsIgnoreCase("1"))
                    for(int i=0;i<groupedSNArray.length();i++)
                    {
                        try {
                            groupedSNObj=groupedSNArray.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            grouped_id=groupedSNObj.getString("product_Id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            grouped_pro_status= groupedSNObj.getString("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            grouped_sku= groupedSNObj.getString("sku");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String grouped_stock_item_type = null;
                        try {
                            grouped_stock_item_type= groupedSNObj.getString("type");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                       /* try {
                            generalJSNobj = groupedSNObj.getJSONObject("general");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/

                        try {
                            grouped_general_name=groupedSNObj.getString("product_name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       /* try {
                            grouped_general_sku=generalJSNobj.getString("sku");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        try {
                            grouped_general_weight=groupedSNObj.getString("weight");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            productQuantity=groupedSNObj.getString("quantity");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d(TAG, "ShoppingCartItem() called with: " + "getGrouped_sku = [" + type + "]");
                        Log.d(TAG, "ShoppingCartItem() called with: " + "grouped_general_sku = [" + grouped_general_sku + "]");

                        try {
                            priceJSNobj=groupedSNObj.getString("price");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            grouped_price_final= groupedSNObj.getString("finalPrice");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            grouped_price_regular= groupedSNObj.getString("price");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        try {
                            grouped_image=groupedSNObj.getString("image");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       /* if(imageJSNArray.length()>0)
                            try {
                                grouped_image=imageJSNArray.getString(0);
                                showGroupImgae(grouped_image);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/


                      /*  try {
                            stockJSNobj=groupedSNObj.getJSONObject("stock");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            grouped_stock_item_id= stockJSNobj.getString("item_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                       /* try {
                            grouped_stock_product_id= stockJSNobj.getString("product_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            grouped_stock_stock_id= stockJSNobj.getString("stock_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        try {
                            grouped_stock_qty= groupedSNObj.getString("maxsQty");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            grouped_stock_is_in_stock= groupedSNObj.getString("isInStock");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if(grouped_stock_is_in_stock.equalsIgnoreCase("false"))
                            no_of_item_out_of_stock_counter++;

                        // show that item which have status 1
                        if(grouped_pro_status.equalsIgnoreCase("1"))
                            groupedProductDetailArrayList.add( new GroupedProductDetailItem(0,grouped_id,grouped_pro_status,  grouped_sku,  grouped_status,  grouped_general_name,  grouped_general_sku,  grouped_general_weight,  grouped_price_final,  grouped_price_regular,  grouped_image,  grouped_stock_item_id,  grouped_stock_product_id,  grouped_stock_stock_id,  grouped_stock_qty,  grouped_stock_is_in_stock,  description_short,  description_pull,grouped_stock_item_type,1,productQuantity));
                        else
                        {
                            Log.d(TAG, "onResponse() called with: " + "grouped_general_name = [" + grouped_general_name + "]" +"with Id = [" + grouped_id + "]"+ "status value  = [" + grouped_status + "] will not show in list");
                        }

                    }
                if(no_of_item_out_of_stock_counter==groupedProductDetailArrayList.size())
                {
                    allItemOutOfStock=true;

                    // if all item is out of stock then list not be show
                    groupedProductDetailArrayList.clear();
                }
                else
                    allItemOutOfStock=false;

                setValueToGroupedListAdapter();
                progressBar_imgLoading.setVisibility(View.GONE);
                ll_show_progress_bar.setVisibility(View.GONE);
                scroll_bar_show_product.setVisibility(View.VISIBLE);
                setDataToUI();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                ll_show_progress_bar.setVisibility(View.GONE);

            }
        });
    }

    private void showGroupImgae(String image) {
        ImageLoader imageLoaderVolley = AppController.getInstance().getImageLoader();
        imageLoaderVolley.get(image, ImageLoader.getImageListener(
                defaultImage, R.drawable.default_mofluid, R.drawable.default_mofluid));
    }

    private void setProductID(String productId) {
        this.productId=productId;
    }

    public void  setQuantityPlus(int position,TextView view){
        int quantity=groupedProductDetailArrayList.get(position).getQuantity();
        int max;
        String maxquantity=groupedProductDetailArrayList.get(position).getGrouped_stock_qty();
        String productStockQuantity=groupedProductDetailArrayList.get(position).getProduct_Stock_max_quantity();
        try{
            max=Integer.parseInt(maxquantity);
            int pro=(int) Double.parseDouble(productStockQuantity);
            if(quantity<max&&quantity<pro){
                int quant=++quantity;
                groupedProductDetailArrayList.get(position).setQuantity(quant);
                //   view.setText(groupedProductDetailArrayList.get(position).getQuantity());
                groupedProductDetailListAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.max_quantity),Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
        }
    }


    public void  setQuantityMinus(int position,TextView view){
        int quantity=groupedProductDetailArrayList.get(position).getQuantity();
        int min=1;
        String maxquantity=groupedProductDetailArrayList.get(position).getGrouped_stock_qty();
        try{
            if(quantity>min){
                int quant=--quantity;
                groupedProductDetailArrayList.get(position).setQuantity(quant);
                //   view.setText(groupedProductDetailArrayList.get(position).getQuantity());
                groupedProductDetailListAdapter.notifyDataSetChanged();
            }else{
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.least_quantity),Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
        }
    }

    private void setValueToGroupedListAdapter() {
        groupedProductDetailListAdapter=new GroupedProductDetailListAdapter(getActivity(),groupedProductDetailArrayList,GroupedProductDetailFragment.this);
        gridV_grouped_item.setAdapter(groupedProductDetailListAdapter);
        setListViewHeightBasedOnChildren(gridV_grouped_item);
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void hitServiceForImages() {
        String urlImages = WebApiManager.getInstance().getProductImagesURL(getActivity());
        urlImages = String.format(urlImages, PRODUCT_ID);

        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(urlImages, new Response.Listener<String>() {
            public JSONArray imageJSNArray;
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
                    imageJSNArray = strJSNobj.getJSONArray("image");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                imgListStrArry=new ArrayList<>();
                for(int i=0;i<imageJSNArray.length();i++)
                {
                    try {
                        imgListStrArry.add(imageJSNArray.getString(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressBar_imgLoading.setVisibility(View.GONE);
                if(imgListStrArry!=null && imgListStrArry.size()>0) {
                    viewPager_banner.setVisibility(View.VISIBLE);
                    setViewPager();
                    productDetailBannerAdapter.notifyDataSetChanged();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());

            }
        });
    }

    private void setDataToUI() {
        //  description_short=decodeStr64Bit(description_short);
        //   description_pull=decodeStr64Bit(description_pull);
        txtV_group_item.setText(PRODUCT_NAME);
        if(Validation.isStrNotEmpty(description_pull))
            txtV_description.setText(Html.fromHtml(description_pull) );
        else
            txtV_description.setText(Html.fromHtml(description_short));

        //show_stock_status.equalsIgnoreCase("true") &&
        if(! allItemOutOfStock) {
            Activity activity = getActivity();
            if(isAdded()&&activity!=null) {
                txtV_is_instock.setText(getActivity().getResources().getString(R.string.in_stock));
                txtV_is_instock.setTextColor(Color.parseColor(getActivity().getResources().getString(R.string.font_green_color)));
            }
        }
        else {
            Activity activity = getActivity();
            if(isAdded()&&activity!=null) {
                txtV_is_instock.setTextColor(Color.parseColor(getActivity().getResources().getString(R.string.font_red_color)));
                txtV_is_instock.setText(getActivity().getResources().getString(R.string.out_of_stock));
            }
        }
    }

    public void addItemToCart(ShoppingItem currentItem,int numberOfItem) {
        if (currentItem != null) {
            ShoppingCartItem item = null;
            if (currentItem != null) {
                item = new ShoppingCartItem(currentItem, numberOfItem);

            } /*else {
                item = new ShoppingCartItem(reorderItem, numberOfItem);
            }*/
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
                                if (isAdded() && getActivity() != null) {
                                    final Toast toast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.item_already_added), Toast.LENGTH_SHORT);
                                    toast.show();

                                }
                                FLAG = 0;
                                break;
                            } else {
                                FLAG = 1;
                            }
                        } else {

                            FLAG = 1;}
                    }

                }

                if (FLAG == 1) {
                    ShoppingCart.getInstance().addItem(item);
                    if (currentItem != null) {
                        setitemCounter();
                        showToast();
                    }
                }
            } else {
                ShoppingCart.getInstance().addItem(item);
                if (currentItem != null) {
                    setitemCounter();
                    showToast();
                }
            }
        }
    }
    public void setitemCounter()
    {
        cartItemList = new ArrayList<>(ShoppingCart.getInstance().getCartItems());

        if (cartItemList.size() > 0) {
            MainActivity.txtV_item_counter.setVisibility(View.VISIBLE);
            MainActivity.txtV_item_counter.setText(cartItemList.size() + "");
            MainActivity.txtV_item_counter.setText(cartItemList.size() + "");
            MainActivity.txtV_item_counter.setVisibility(View.VISIBLE);
        } else {
            MainActivity.txtV_item_counter.setVisibility(View.INVISIBLE);
        }

    }
    public void showToast() {
        if(isAdded() && getActivity()!=null)
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.item_added), Toast.LENGTH_SHORT).show();
    }
    private String decodeStr64Bit(String encodeedStr) {
        byte[] data1 = Base64.decode(encodeedStr, Base64.DEFAULT);
        String decodedStr="";
        try {
            decodedStr = new String(data1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d(TAG," Decoded Password  "+decodedStr);

        return decodedStr;
    }

    @Override
    public void onDetach() {
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

  /*  public void callBackForDelivery(Boolean status,TextView txtViewStatus) {
        if(status){
            txtViewStatus.setVisibility(View.VISIBLE);
            txtViewStatus.setText(activity.getResources().getString(R.string.status_true));
            txtViewStatus.setTextColor(activity.getResources().getColor(R.color.app_color));
        }else{
            txtViewStatus.setVisibility(View.VISIBLE);
            txtViewStatus.setText(activity.getResources().getString(R.string.status_false));
            txtViewStatus.setTextColor(activity.getResources().getColor(R.color.font_red_color));
        }
    }*/

    public  String getProductID() {
        return productId ;
    }
}
