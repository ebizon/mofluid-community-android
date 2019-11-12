package com.ebizon.fluid.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.ebizon.fluid.model.FilterKeyItem;
import com.ebizon.fluid.model.FilterOptionItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.EncodeString;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.MyFilterKeyAdapter;
import com.mofluid.magento2.adapter.MyFilterOptionAdapter;
import com.mofluid.magento2.fragment.BaseFragment;
import com.mofluid.magento2.fragment.HomeFragment;
import com.mofluid.magento2.fragment.ProductDetailListFragment;
import com.mofluid.magento2.manager.FilterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ebizon on 1/7/16.
 */
public class FilterMyProducts {
    private static FilterMyProducts ourInstance = new FilterMyProducts();
    HashMap<String, ArrayList<FilterOptionItem>> filterListMap;
    Activity activity;
    private ListView listViewOptionItem;
    private ProductDetailListFragment productDetailListFragment;
    private String categoryKey;
    public static ArrayList<FilterOptionItem> updateOptionList;
    private HashMap<String, ArrayList<FilterOptionItem>> selectedFilterList;

    public static FilterMyProducts getInstance() {
        return ourInstance;
    }

    private FilterMyProducts() {
    }

    public void showFilterDialogBox(final Activity activity, final ArrayList<FilterKeyItem> filterCategoryList, final HashMap<String, ArrayList<FilterOptionItem>> filterListMap, final ProductDetailListFragment productDetailListFragment)
    {
        updateOptionList=new ArrayList<>();
        selectedFilterList = new HashMap<>();
        this.filterListMap=filterListMap;
        this.activity=activity;
        this.productDetailListFragment=productDetailListFragment;
        final Dialog dialog=new Dialog(activity,R.style.DialogAnimationFilterProducts);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Typeface open_sans_semibold = Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
        Typeface open_sans_semiregular = Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);

        dialog.setContentView(R.layout.dialog_filter_products);
       // dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
      //  dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationFilterProducts;
        ListView listViewFilerKey = (ListView) dialog.findViewById(R.id.listViewFilerKey);
        TextView txtvFilter = (TextView) dialog.findViewById(R.id.txtvFilter);
        TextView txtvSelect = (TextView) dialog.findViewById(R.id.txtvSelect);
        TextView btnRest = (TextView) dialog.findViewById(R.id.btnRest);
        TextView btnApply = (TextView) dialog.findViewById(R.id.btnApply);
        btnApply.setTypeface(open_sans_semibold);
        btnRest.setTypeface(open_sans_semibold);
        txtvFilter.setTypeface(open_sans_semiregular);
        txtvSelect.setTypeface(open_sans_semiregular);
        listViewOptionItem = (ListView) dialog.findViewById(R.id.listViewOptionItem);
        LinearLayout llCloseWindow = (LinearLayout) dialog.findViewById(R.id.llCloseWindow);
        final String keys="";

        populateFilterKeyListView(activity, listViewFilerKey,filterCategoryList);
        categoryKey=filterCategoryList.get(0).getId();
        final ArrayList<FilterOptionItem> filterOptionList = filterListMap.get(filterCategoryList.get(0).getName());
        updateOptionList=filterOptionList;
        populateFilterOptionListView(activity, listViewOptionItem,filterOptionList, filterCategoryList.get(0).getId());

        llCloseWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }

        }); btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                HashMap<String, ArrayList<FilterOptionItem>> selected = selectedFilterList;
                String selectedOption=getSelectedOptin(updateOptionList);
                if(selectedOption!=null && !selectedOption.equalsIgnoreCase("") && selectedOption.length()>1)
               // productDetailListFragment.filterProduct(categoryKey,selectedOption);
                {
                    String urlProductListDetails = WebApiManager.getInstance().getFilteredProductListURL(activity);

                    String sortType=FilterManager.getInstance().sortType;
                    String sortOrder=FilterManager.getInstance().sortOrder;

//                    if(sortType==null)
//                        sortType = "";
//                    if(sortOrder==null)
//                        sortOrder = "";

                    HashMap<String,String> filtermap = new HashMap<String, String>();
                    JSONArray array = new JSONArray();
//                    selectedFilterList
                    try {
                        array = getJsonFromMap(selectedFilterList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    filtermap.put("code",categoryKey);
//                    filtermap.put("id",selectedOption);
//                    JSONObject obj = new JSONObject(filtermap);
//
//                    array.put(obj);
                    String filterdata= EncodeString.encodeStrBase64Bit(array.toString());
                    Log.d("Filter","Array to string is"+ array.toString());
                    Log.d("Filter","encoded is "+ filterdata);
                    urlProductListDetails = String.format(urlProductListDetails,sortType,sortOrder, FilterManager.getInstance().previousCategoryId,filterdata);
                  //  isComesForFilter=true;
                   // productListDetails=new ArrayList<>();
                   // lastInScreen=0;
                   // hitServiceForProductDetails(urlProductListDetails);
                    String urlStr = WebApiManager.getInstance().getFilterProductsURL(activity);
                    String finalUrl="";
                    if(categoryKey.equalsIgnoreCase("category_id")) {
                        finalUrl=String.format(urlStr,selectedOption,categoryKey,"","","");
                        FilterManager.getInstance().previousCategoryId=selectedOption;
                    }
                    else {
                        finalUrl=String.format(urlStr,FilterManager.getInstance().previousCategoryId,categoryKey,selectedOption,"name","asc");
                    }
                   // hitServiceForFilter(finalUrl);
                    FilterManager.getInstance().selectedFilterOption=null;
                    FilterManager.getInstance().selectedFilterOptionID=null;
                    ProductDetailListFragment fragment = new ProductDetailListFragment(urlProductListDetails, true, finalUrl);
                    callFragment(fragment,fragment.getClass().getSimpleName());
                }

            }
        }); btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productDetailListFragment.resetFilterKeys();
                dialog.cancel();

            }
        });
        dialog.show();

        listViewFilerKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listViewOptionItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String getSelectedOptin(ArrayList<FilterOptionItem> updateOptionList) {
        String selectedOption="";
        boolean isFirstItem=true;
        for(int i=0;i<updateOptionList.size();i++)
        {
            FilterOptionItem optionItem = updateOptionList.get(i);
            if(optionItem.isChecked()) {
                FilterManager.getInstance().selectedFilterOption=optionItem.getName();
                FilterManager.getInstance().selectedFilterOptionID=optionItem.getId();

                if(isFirstItem) {
                    selectedOption = selectedOption + optionItem.getId();
                    isFirstItem=false;
                }
                else
                    selectedOption = selectedOption +","+ optionItem.getId();
            }
        }
        return selectedOption;
    }

    private void populateFilterOptionListView(Activity activity, ListView listViewOptionItem, ArrayList<FilterOptionItem> filterOptionList, String id) {
        MyFilterOptionAdapter optionAdapter=new MyFilterOptionAdapter(activity,filterOptionList,FilterMyProducts.getInstance(), id);
        listViewOptionItem.setAdapter(optionAdapter);

    }

    private void populateFilterKeyListView(Activity activity, ListView listViewOptionItem, ArrayList<FilterKeyItem> filterCategoryList) {
        MyFilterKeyAdapter optionAdapter=new MyFilterKeyAdapter(activity,filterCategoryList,FilterMyProducts.getInstance());
        listViewOptionItem.setAdapter(optionAdapter);
    }
    public void changeFilterOption(FilterKeyItem categoryItem)
    {
        categoryKey=categoryItem.getId();
        ArrayList<FilterOptionItem> filterOptionList = this.filterListMap.get(categoryItem.getName());
        populateFilterOptionListView(this.activity, listViewOptionItem,filterOptionList, categoryItem.getId());

    }

    public void setUpdateList(ArrayList<FilterOptionItem> arrayList) {
        updateOptionList=arrayList;

    }

    public void setSelectedList(String key, ArrayList<FilterOptionItem> filterlist) {
//        if(selectedFilterList.containsKey(key)) {
//            selectedFilterList.get(key).clear();
//            selectedFilterList.get(key).addAll(filterlist);
//        }
//            else
        selectedFilterList.put(key, filterlist);

    }

    public HashMap<String, ArrayList<FilterOptionItem>> getSelectedList(){
        return  selectedFilterList;
    }

    void callFragment(BaseFragment fragment, String fragmentName) {
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(activity, fragmentTransaction, new HomeFragment(), fragment, R.id.content_frame);
        /*
        index 7 is used to slide fragment Left to right and vise versa
         */
        fragmentTransactionExtended.addTransition(7);
//        if(fragmentName != null) {
//            fragmentTransaction.addToBackStack(fragmentName);
//        }7
        fragmentTransactionExtended.commit();
    }
    private JSONArray getJsonFromMap(HashMap<String, ArrayList<FilterOptionItem>> map) throws JSONException {
        JSONArray finalr = new JSONArray();
        String optionstring="";
        HashMap<String,String> result = new HashMap<>();
        for (String key : map.keySet()) {
            optionstring = "";
            boolean isFirst = true;
            ArrayList<FilterOptionItem> value = map.get(key);
            result.put("code",key.toLowerCase());
//            for (int i = 0; i < value.size(); i++) {
//                optionstring+=value.get(i).getId();
//            }
            // error is here
            for (int i = 0; i < value.size(); i++) {
                if(value.get(i).isChecked()) {
                    if(isFirst)
                    {
                        optionstring += value.get(i).getId();
                        isFirst=false;
                    }
                    else
                    optionstring +=","+value.get(i).getId();
                }
            }
            result.put("id",optionstring);
            if(optionstring!=null&&!optionstring.equalsIgnoreCase(""))
            finalr.put(new JSONObject(result));

        }
        Log.d("MULTIFILTER", finalr.toString());
        return finalr;
    }
}
