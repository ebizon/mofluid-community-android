package com.ebizon.fluid.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.ebizon.fluid.model.FilterKeyItem;
import com.ebizon.fluid.model.FilterOptionItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.EncodeString;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.MyFilterKeyAdapter;
import com.mofluid.magento2.adapter.MyFilterOptionAdapter;
import com.mofluid.magento2.fragment.BaseFragment;
import com.mofluid.magento2.fragment.FilterData;
import com.mofluid.magento2.fragment.HomeFragment;
import com.mofluid.magento2.fragment.ProductDetailListFragment;
import com.mofluid.magento2.manager.FilterManager;

import org.json.JSONArray;

import java.util.ArrayList;


/**
 * Created by saddam on 13/2/18.
 */

public class GetFilter{
    private static GetFilter instance;
    private ListView listViewFilterKey;
    private ListView listViewOptionItem;
    private TextView textViewFilter;
    private TextView textViewSelect;
    private TextView textViewApply;
    private TextView textViewReset;
    private LinearLayout linearLayoutCloseWindow;
    private Dialog dialog;
    private Context con;
    private Activity activity;
    private Typeface open_sans_semibold;
    private Typeface open_sans_semiregular;
    private FilterData FDins;
    private boolean isDialogueForSearch;
    private String searchText=null;
    private String TAG="ApplyFilter";
    public static GetFilter getInstance(Activity activity){
        if(instance==null)
            instance=new GetFilter(activity);
        return instance;
    }
    private GetFilter(Activity activity){
        this.activity=activity;
        FDins=FilterData.getInstance();
        this.isDialogueForSearch=false;
        this.searchText=null;
        this.intialize();
    }
    public void getDialogue(boolean isforSearch,String searchText){
        if(FDins.isFilterEmpty())
        {
            Toast.makeText(this.activity,this.activity.getResources().getString(R.string.filter_not_avaiblable), Toast.LENGTH_SHORT).show();
            return;
        }
        this.isDialogueForSearch=isforSearch;
        this.searchText=searchText;
        this.dialog.show();
    }
    private void intialize(){
        this.dialog=new Dialog(this.activity, R.style.DialogAnimationFilterProducts);
        this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.dialog.setContentView(R.layout.dialog_filter_products);
        this.listViewFilterKey=(ListView) this.dialog.findViewById(R.id.listViewFilerKey);
        this.listViewOptionItem=(ListView)this.dialog.findViewById(R.id.listViewOptionItem);
        this.textViewFilter=(TextView) this.dialog.findViewById(R.id.txtvFilter);
        this.textViewSelect=(TextView)this.dialog.findViewById(R.id.txtvSelect);
        this.textViewApply=(TextView)this.dialog.findViewById(R.id.btnApply);
        this.textViewReset=(TextView)this.dialog.findViewById(R.id.btnRest);
        this.linearLayoutCloseWindow=(LinearLayout) this.dialog.findViewById(R.id.llCloseWindow);
        this.open_sans_semibold=Typeface.createFromAsset(this.activity.getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
        this.open_sans_semiregular=Typeface.createFromAsset(this.activity.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
        this.textViewApply.setTypeface(this.open_sans_semibold);
        this.textViewReset.setTypeface(this.open_sans_semibold);
        this.textViewFilter.setTypeface(this.open_sans_semiregular);
        this.textViewSelect.setTypeface(this.open_sans_semiregular);
        this.populateFilterKeyListView();
        this.populateFilterOptionListView(FilterData.getInstance().getFilterOptionItemList(0),FilterData.getInstance().getFilterKeyItemID(0));
        this.textViewReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FDins.uncheckAll();
                populateFilterKeyListView();
                populateFilterOptionListView(FilterData.getInstance().getFilterOptionItemList(0),FilterData.getInstance().getFilterKeyItemID(0));
            }
        });
        this.textViewApply.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
             dialog.cancel();
             applyFilter();
            }
        });
        this.linearLayoutCloseWindow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }
    public void changeFilterOption(FilterKeyItem categoryItem)
    {
        ArrayList<FilterOptionItem> filterOptionList = FilterData.getInstance().getFilterOptionItemList(categoryItem.getName());
        populateFilterOptionListView(filterOptionList, categoryItem.getId());

    }
    private void populateFilterOptionListView(ArrayList<FilterOptionItem> filterOptionList, String id) {
        if(filterOptionList==null || filterOptionList.size()<=0)
            return;
        MyFilterOptionAdapter optionAdapter=new MyFilterOptionAdapter(this.activity,filterOptionList,FilterMyProducts.getInstance(), id);
        this.listViewOptionItem.setAdapter(optionAdapter);

    }
    private void populateFilterKeyListView() {
        MyFilterKeyAdapter optionAdapter=new MyFilterKeyAdapter(this.activity,FilterData.getInstance().getFilterCategoryList());
        this.listViewFilterKey.setAdapter(optionAdapter);
    }

private void callFragment(BaseFragment fragment, String fragmentName) {
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(activity, fragmentTransaction, new HomeFragment(), fragment, R.id.content_frame);
        fragmentTransactionExtended.addTransition(7);
        fragmentTransactionExtended.commit();
    }
private void applyFilter(){
    String urlProductListDetails=null;
    JSONArray jsonArray=FDins.getSelectedOptionInJson();
    String filterdata= EncodeString.encodeStrBase64Bit(jsonArray.toString());
    if(this.isDialogueForSearch){
     urlProductListDetails=WebApiManager.getInstance().getSearchFilteredProdcutListURL(this.activity);
     String finalSearchtext = EncodeString.encodeStrBase64Bit(this.searchText);
     urlProductListDetails = String.format(urlProductListDetails, FDins.getSortType(), FDins.getSortOrder(), finalSearchtext,filterdata);
    } else {
        urlProductListDetails = WebApiManager.getInstance().getCategoryProductsURL();
        urlProductListDetails = String.format(urlProductListDetails,FDins.getCurCategoryId(),FDins.getSortOrder(), FDins.getSortType(),1,200, filterdata);
    }
    ProductDetailListFragment fragment = new ProductDetailListFragment(urlProductListDetails, true,"");
    this.callFragment(fragment,fragment.getClass().getSimpleName());
}
}
