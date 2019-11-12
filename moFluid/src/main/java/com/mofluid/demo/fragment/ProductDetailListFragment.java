package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.Utils.Config;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.FilterMyProducts;
import com.ebizon.fluid.Utils.GetFilter;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.Utils.ViewUtils;
import com.ebizon.fluid.model.FilterKeyItem;
import com.ebizon.fluid.model.FilterOptionItem;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.WebApiManager;
import com.ebizon.fluid.model.productDetailListItem;
import com.mofluid.magento2.EncodeString;
import com.mofluid.magento2.GetProductsListDetails;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.ProductDetailListAdapter;
import com.mofluid.magento2.custome.widget.ShowAlertDialogBox;
import com.mofluid.magento2.manager.FilterManager;
import com.mofluid.magento2.service.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.mofluid.magento2.fragment.FilterData;

public class ProductDetailListFragment extends BaseFragment implements View.OnClickListener {
    //private Button txtSortBy;
    AlertDialog levelDialog;
    String headerText;
    ArrayList<FilterKeyItem> filterCategoryList;
    HashMap<String, ArrayList<FilterOptionItem>> filterListMap;
    int numberOfRecordInEachPage = 200;
    int     indexOfCurrentPage = 1;
    boolean isForFilter;
    String filterUrl, filterItem;
    ProgressBar progressBar_paging, progressbar_end;
    private GridView gridViewProductdetailList;
    private ProductDetailListAdapter productDetailListAdapter;
    private final String TAG="ProductDetailListFra";
    private ArrayList<productDetailListItem> productListDetails;
    private String subCategoryId, categoryId;
    private ConnectionDetector cd;
    private View rootView;
    private String searchText = null;
    private int lastInScreen;
    private boolean loadingMore;
    private int totalRecords;
    private String category_name;
    private ArrayList<productDetailListItem> newDataList;
    private boolean isComesForFilter;
    private boolean comesFromReset;
    private ListView MyLisView;
    private String previousCategoryId;
    private String sortType = "name";
    private String sortOrder = "ASC";
    private boolean isPagination = false;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    @SuppressLint("ValidFragment")
    public ProductDetailListFragment(String name) {
        headerText = name;
    }

    public ProductDetailListFragment() {
    }

    @SuppressLint("ValidFragment")
    public ProductDetailListFragment(String filterUrl, boolean isForFilter, String filterItem) {
        this.filterUrl = filterUrl;
        this.filterItem = filterItem;
        this.isForFilter = isForFilter;
        this.isPagination = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_product_detail_list, null);
            try {
                searchText = getArguments().getString(MainActivity.KEY_SEARCH_TEXT);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                subCategoryId = getArguments().getString(MainActivity.KEY_SUB_CATEGORY_ID);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (subCategoryId == null) {
                try {
                    categoryId = getArguments().getString(MainActivity.KEY_CATEGORY_ID);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else
                categoryId = subCategoryId;

            previousCategoryId = categoryId;
            Log.d(TAG, "subCateogory id=" + subCategoryId);

            initialized();
            getViewControlls(rootView);

            if (cd.isConnectingToInternet()) {
                setdataToProductDelailList();
                if (!isForFilter) {
                    callProductListApi();
                    hitServiceForFilter();
                } else {
                    hitServiceForProductDetails(filterUrl);
                    //hitServiceForFilter(filterItem);
                }


            } else {
                Activity activity = getActivity();
                if (isAdded() && activity != null) {
                    new ShowAlertDialogBox().showCustomeDialogBox(getActivity(), getActivity().getResources().getString(R.string.internet_connection), getActivity().getResources().getString(R.string.internet_not_avalable));
                }
            }

		/*
                show menu's back button
			 */
            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);


            MyOnScrollListener myOnScrollListener = new MyOnScrollListener();
            gridViewProductdetailList.setOnScrollListener(myOnScrollListener);
            MyLisView.setOnScrollListener(myOnScrollListener);

			/*gridViewProductdetailList.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {


				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

					if(!isComesForFilter) {
						lastInScreen = firstVisibleItem + visibleItemCount;
						if ((lastInScreen == totalItemCount) && !(loadingMore) && (totalItemCount != totalRecords)) {
							Log.d(TAG, "onScroll() called with: " + "view = [" + view + "], firstVisibleItem = [" + firstVisibleItem + "], visibleItemCount = [" + visibleItemCount + "], totalItemCount = [" + totalItemCount + "]");
							indexOfCurrentPage++;

							try {
								callProductListApi();
							} catch (Exception e) {

							}

						}
					}
				}
			});*/
            MyOInItemClickLister myOInItemClickLister = new MyOInItemClickLister();
            gridViewProductdetailList.setOnItemClickListener(myOInItemClickLister);
            MyLisView.setOnItemClickListener(myOInItemClickLister);

			/*gridViewProductdetailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					ImageView img = (ImageView) view.findViewById(R.id.imgV_id);
					SimpleProductFragment.imageDrawable= img.getDrawable();
					BaseFragment fragment;
					productDetailListItem productItemObj = (productDetailListItem) gridViewProductdetailList.getAdapter().getItem(i);
					if (productItemObj.getType().equalsIgnoreCase("simple"))
						fragment = new SimpleProductFragment2();
					else if (productItemObj.getType().equalsIgnoreCase("grouped"))
						fragment = new GroupedProductDetailFragment();
					else
						fragment = new SimpleProductFragment2();

					Bundle bundle = new Bundle();
					bundle.putString(ConstantDataMember.PRO_ID, productItemObj.getId());
					bundle.putString(ConstantDataMember.PRO_NAME, productItemObj.getName());
					fragment.setArguments(bundle);
					try {
						callFragment(fragment, fragment.getClass().getSimpleName());
					}catch (IllegalStateException e)
					{
						e.printStackTrace();
					}
				}
			});*/
        }
        return rootView;
		/**/
    }
    private void hitServiceForFilter(){
        String url=null;
        if(searchText!=null){
           String base_url=WebApiManager.getInstance().getSearchFilterURL(getActivity());
            String finalSearchtext = EncodeString.encodeStrBase64Bit(searchText);
            url=String.format(base_url,finalSearchtext);
        }
        else{

            String base_url = WebApiManager.getInstance().getFilterProductsURL(getActivity());
            FilterManager.getInstance().previousCategoryId = categoryId;
            FilterManager.getInstance().initialCategory = categoryId;
            FilterData.getInstance().setCurCategoryId(categoryId);
            url = String.format(base_url, categoryId, "category_id", "",sortType,sortOrder);

        }
        hitServiceForFilter(url);
    }
    private void callProductListApi() {

        String urlProductListDetails = null;
        if (searchText != null) {
            urlProductListDetails = WebApiManager.getInstance().getSearchURL(getActivity());
            String finalSearchtext = EncodeString.encodeStrBase64Bit(searchText);
            urlProductListDetails = String.format(urlProductListDetails, sortType, sortOrder, finalSearchtext, indexOfCurrentPage, numberOfRecordInEachPage);
//			urlProductListDetails = String.format(urlProductListDetails,searchText,indexOfCurrentPage,numberOfRecordInEachPage);
        } else {
            //urlProductListDetails = WebApiManager.getInstance().getProductListURL(getActivity());
            //urlProductListDetails = String.format(urlProductListDetails, sortType, sortOrder, categoryId, indexOfCurrentPage, numberOfRecordInEachPage);
//			//urlProductListDetails = String.format(urlProductListDetails,categoryId,indexOfCurrentPage,numberOfRecordInEachPage);
            urlProductListDetails=WebApiManager.getInstance().getCategoryProductsURL();
            JSONArray jsonArray=null;
            String filterdata=null;
            if(isForFilter){
                categoryId= com.mofluid.magento2.fragment.FilterData.getInstance().getCurCategoryId();
                jsonArray= com.mofluid.magento2.fragment.FilterData.getInstance().getSelectedOptionInJson();
               filterdata=EncodeString.encodeStrBase64Bit(jsonArray.toString());
            }
            urlProductListDetails=String.format(urlProductListDetails,categoryId,sortOrder,sortType,indexOfCurrentPage,numberOfRecordInEachPage,filterdata);
        }
        hitServiceForProductDetails(urlProductListDetails);
    }

    public void dialogToSortProducts() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
       /* wmlp.x = 50;   //x position
        wmlp.y = 50;   //y position*/

        String lang = Config.getInstance().getLanguage();
        if(lang.equals("ar"))
            dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        else
            dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        dialog.setContentView(R.layout.dialog_to_sort_products);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        setCLickListenersDialog(dialog);


        dialog.setCancelable(true);


        dialog.show();

    }

    private void setCLickListenersDialog(final Dialog dialog) {


        CheckBox tvNameZA = (CheckBox) dialog.findViewById(R.id.tvNameZA);
        CheckBox tvNameAZ = (CheckBox) dialog.findViewById(R.id.tvNameAZ);
        CheckBox txtPriceLH = (CheckBox) dialog.findViewById(R.id.txtPriceLH);
        CheckBox txtPriceHL = (CheckBox) dialog.findViewById(R.id.txtPriceHL);

//        CheckBox brandZA = (CheckBox) dialog.findViewById(R.id.brandZA);
//        CheckBox brandAZ = (CheckBox) dialog.findViewById(R.id.brandAZ);
//        CheckBox conditionAZ = (CheckBox) dialog.findViewById(R.id.conditionAZ);
//        CheckBox conditionZA = (CheckBox) dialog.findViewById(R.id.conditionZA);
//
//        CheckBox mem_densityAZ = (CheckBox) dialog.findViewById(R.id.mem_densityAZ);
//        CheckBox mem_densityZA = (CheckBox) dialog.findViewById(R.id.mem_densityZA);
//        CheckBox newAZ = (CheckBox) dialog.findViewById(R.id.newAZ);
//        CheckBox newZA = (CheckBox) dialog.findViewById(R.id.newZA);
//
//        CheckBox savingsAZ = (CheckBox) dialog.findViewById(R.id.savingsAZ);
//        CheckBox savingsZA = (CheckBox) dialog.findViewById(R.id.savingsZA);
//        CheckBox bestsellersAZ = (CheckBox) dialog.findViewById(R.id.bestsellersAZ);
//        CheckBox bestsellersZA = (CheckBox) dialog.findViewById(R.id.bestsellersZA);
//
//        CheckBox mostviewedAZ = (CheckBox) dialog.findViewById(R.id.mostviewedAZ);
//        CheckBox mostviewedZA = (CheckBox) dialog.findViewById(R.id.mostviewedZA);
//        CheckBox topratedAZ = (CheckBox) dialog.findViewById(R.id.topratedAZ);
//        CheckBox topratedZA = (CheckBox) dialog.findViewById(R.id.topratedZA);
//
//        CheckBox reviewsAZ = (CheckBox) dialog.findViewById(R.id.reviewsAZ);
//        CheckBox reviewsZA = (CheckBox) dialog.findViewById(R.id.reviewsZA);
//        CheckBox wishlistAZ = (CheckBox) dialog.findViewById(R.id.wishlistAZ);
//        CheckBox wishlistZA = (CheckBox) dialog.findViewById(R.id.wishlistZA);

        settypefacecheckbox(tvNameAZ);
        settypefacecheckbox(tvNameZA);
//        settypefacecheckbox(brandAZ);
//        settypefacecheckbox(brandZA);
//        settypefacecheckbox(conditionAZ);
//        settypefacecheckbox(conditionZA);
//        settypefacecheckbox(mem_densityAZ);
//        settypefacecheckbox(mem_densityZA);
//        settypefacecheckbox(newAZ);
//        settypefacecheckbox(newZA);
//        settypefacecheckbox(savingsAZ);
//        settypefacecheckbox(savingsZA);
//        settypefacecheckbox(bestsellersAZ);
//        settypefacecheckbox(bestsellersZA);
//        settypefacecheckbox(mostviewedAZ);
//        settypefacecheckbox(mostviewedZA);
//        settypefacecheckbox(topratedAZ);
//        settypefacecheckbox(topratedZA);
//        settypefacecheckbox(reviewsAZ);
//        settypefacecheckbox(reviewsZA);
//        settypefacecheckbox(wishlistAZ);
//        settypefacecheckbox(wishlistZA);

        CheckBox box = FilterManager.getInstance().previousCheckBox;
        if (FilterManager.getInstance().previousCheckBox == null)
            tvNameAZ.setChecked(true);
        else {
            if (FilterManager.getInstance().previousCheckBox.getId() == tvNameAZ.getId())
                tvNameAZ.setChecked(true);
            else if (FilterManager.getInstance().previousCheckBox.getId() == tvNameZA.getId())
                tvNameZA.setChecked(true);
            else if (FilterManager.getInstance().previousCheckBox.getId() == txtPriceLH.getId())
                txtPriceLH.setChecked(true);
            else if (FilterManager.getInstance().previousCheckBox.getId() == txtPriceHL.getId())
                txtPriceHL.setChecked(true);

//            else if (FilterManager.getInstance().previousCheckBox.getId() == brandAZ.getId())
//                brandAZ.setChecked(true);
//            else if (FilterManager.getInstance().previousCheckBox.getId() == brandZA.getId())
//                brandZA.setChecked(true);
//
//            else if (FilterManager.getInstance().previousCheckBox.getId() == conditionAZ.getId())
//                conditionAZ.setChecked(true);
//            else if (FilterManager.getInstance().previousCheckBox.getId() == conditionZA.getId())
//                conditionZA.setChecked(true);
//
//            else if (FilterManager.getInstance().previousCheckBox.getId() == mem_densityAZ.getId())
//                mem_densityAZ.setChecked(true);
//            else if (FilterManager.getInstance().previousCheckBox.getId() == mem_densityZA.getId())
//                mem_densityZA.setChecked(true);
//
//            else if (FilterManager.getInstance().previousCheckBox.getId() == newAZ.getId())
//                newAZ.setChecked(true);
//            else if (FilterManager.getInstance().previousCheckBox.getId() == newZA.getId())
//                newZA.setChecked(true);
//
//            else if (FilterManager.getInstance().previousCheckBox.getId() == savingsAZ.getId())
//                savingsAZ.setChecked(true);
//            else if (FilterManager.getInstance().previousCheckBox.getId() == savingsZA.getId())
//                savingsZA.setChecked(true);
//
//            else if (FilterManager.getInstance().previousCheckBox.getId() == bestsellersAZ.getId())
//                bestsellersAZ.setChecked(true);
//            else if (FilterManager.getInstance().previousCheckBox.getId() == bestsellersZA.getId())
//                bestsellersZA.setChecked(true);
//
//            else if (FilterManager.getInstance().previousCheckBox.getId() == mostviewedAZ.getId())
//                mostviewedAZ.setChecked(true);
//            else if (FilterManager.getInstance().previousCheckBox.getId() == mostviewedZA.getId())
//                mostviewedZA.setChecked(true);
//
//            else if (FilterManager.getInstance().previousCheckBox.getId() == topratedAZ.getId())
//                topratedAZ.setChecked(true);
//            else if (FilterManager.getInstance().previousCheckBox.getId() == topratedZA.getId())
//                topratedZA.setChecked(true);
//
//            else if (FilterManager.getInstance().previousCheckBox.getId() == reviewsAZ.getId())
//                reviewsAZ.setChecked(true);
//            else if (FilterManager.getInstance().previousCheckBox.getId() == reviewsZA.getId())
//                reviewsZA.setChecked(true);
//
//            else if (FilterManager.getInstance().previousCheckBox.getId() == wishlistAZ.getId())
//                wishlistAZ.setChecked(true);
//            else if (FilterManager.getInstance().previousCheckBox.getId() == wishlistZA.getId())
//                wishlistZA.setChecked(true);
        }


        tvNameZA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortOrder = "DESC";
                sortType = "name";
                setActiontoSort(sortOrder,sortType,dialog,view);
            }
        });
        tvNameAZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortOrder = "ASC";
                sortType = "name";
                setActiontoSort(sortOrder,sortType,dialog,view);
            }
        });
        txtPriceLH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortOrder = "ASC";
                sortType = "price";
                setActiontoSort(sortOrder,sortType,dialog,view);
            }
        });
        txtPriceHL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortOrder = "DESC";
                sortType = "price";
                setActiontoSort(sortOrder,sortType,dialog,view);
            }
        });

//        brandAZ.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "asc";
//                sortType = "brand";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        brandZA.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "desc";
//                sortType = "name";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        conditionAZ.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "asc";
//                sortType = "condition";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        conditionZA.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "desc";
//                sortType = "condition";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//
//        mem_densityAZ.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "asc";
//                sortType = "mem_density";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        mem_densityZA.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "desc";
//                sortType = "mem_density";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        newAZ.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "asc";
//                sortType = "created_at";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        newZA.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "desc";
//                sortType = "created_at";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//
//        savingsAZ.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "asc";
//                sortType = "saving";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        savingsZA.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "desc";
//                sortType = "saving";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        bestsellersAZ.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "asc";
//                sortType = "bestsellers";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        bestsellersZA.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "desc";
//                sortType = "bestsellers";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//
//        mostviewedAZ.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "asc";
//                sortType = "most_viewed";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        mostviewedZA.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "desc";
//                sortType = "most_viewed";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        topratedAZ.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "asc";
//                sortType = "rating_summary";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        topratedZA.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "desc";
//                sortType = "rating_summary";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        reviewsAZ.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "asc";
//                sortType = "reviews_count";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        reviewsZA.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "desc";
//                sortType = "reviews_count";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        wishlistAZ.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "asc";
//                sortType = "wished";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
//        wishlistZA.setOnClickListener(new View.RecyclerViewClickListener() {
//            @Override
//            public void onClick(View view) {
//                sortOrder = "desc";
//                sortType = "wished";
//                setActiontoSort(sortOrder,sortType,dialog,view);
//            }
//        });
    }



    private void setActiontoSort(String sortOrder, String sortType, Dialog dialog, View view) {
        FilterManager.getInstance().sortType = sortType;
        FilterManager.getInstance().sortOrder = sortOrder;
        //FilterManager.getInstance().wichSortedApply = 1;
        sortProductsandhitService();
        FilterManager.getInstance().previousCheckBox = (CheckBox) view;
        dialog.cancel();
    }

    private void sortProductsandhitService() {
        productListDetails.clear();
        indexOfCurrentPage = 1;
        String url = filterUrl;
        String item = filterItem;
        productDetailListAdapter.notifyDataSetChanged();
       if (!isForFilter) {
            Log.d("FILTERPIYUSH", "Called from 1");
            callProductListApi();
            String urlStr = WebApiManager.getInstance().getFilterProductsURL(getActivity());
            FilterManager.getInstance().previousCategoryId = categoryId;
            FilterManager.getInstance().initialCategory = categoryId;
            FilterData.getInstance().setCurCategoryId(categoryId);
            String finalUrl = String.format(urlStr, categoryId, "category_id", "",sortType,sortOrder);
            hitServiceForFilter(finalUrl);

        } else {
            callProductListApi();
            //hitServiceForProductDetails(filterUrl);
           // hitServiceForFilter(filterItem);
        }

    }

    private void settypefacecheckbox(CheckBox box) {
        Typeface open_sans_semibold = Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
        box.setTypeface(open_sans_semibold);
    }

    private void showRadioButtonDialog() {
        Activity activity= getActivity();
        List<String> listSortItems = new ArrayList<>();
        if(isAdded()&&activity!=null) {
            listSortItems.add(getActivity().getResources().getString(R.string.txt_posion));
            listSortItems.add(getActivity().getResources().getString(R.string.txt_name));
            listSortItems.add(getActivity().getResources().getString(R.string.txt_price_));
            listSortItems.add(getActivity().getResources().getString(R.string.txt_direction));
        }


        final CharSequence[] items = listSortItems.toArray(new CharSequence[listSortItems.size()]);
        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(getActivity().getResources().getString(R.string.txt_sort_products));
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        sortProductAToZ();
                        break;
                    case 1:
                        sortProductZToA();

                        break;
                    case 2:
                        sortProductLowToHigh();
                        break;
                    case 3:
                        sortProductHighToLow();
                        break;

                }
                levelDialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();

    }

    private void hitServiceForProductDetails(String urlProductListDetails) {
        newDataList = new ArrayList<>();
        //final ProgressDialog pDialog;
        String dialog = "";
        if (!isPagination) {
            progressBar_paging = (ProgressBar) rootView.findViewById(R.id.progressBar_normal);
            dialog = "Theme1";
        } else {
            //progressBar_paging = (ProgressBar) rootView.findViewById(R.id.progressBar_paging);
            dialog = "Theme 2";
        }
        Log.d("PAGING", "dialog is " + dialog);
        progressBar_paging.setVisibility(View.VISIBLE);
        loadingMore = true;
        Log.d(TAG, "service for product list called with final URl : " + urlProductListDetails);

        ServiceForProducts productSevice = new ServiceForProducts();
        productSevice.execute(urlProductListDetails);

    }

    private void initialized() {
        isComesForFilter = false;
        productListDetails = new ArrayList<>();
        cd = new ConnectionDetector(getActivity());
        filterListMap = new HashMap<>();
        filterCategoryList = new ArrayList<>();
    }

    private void setdataToProductDelailList() {
        productDetailListAdapter = new ProductDetailListAdapter(getActivity(), productListDetails);
//		mypListingAdapter=new MypListingAdapter(getActivity(),productListDetails);
        gridViewProductdetailList.setAdapter(productDetailListAdapter);
//		MyLisView.setAdapter(mypListingAdapter);
    }

    private void getViewControlls(View rootView) {
        gridViewProductdetailList = (GridView) rootView.findViewById(R.id.gridViewProductdetailList);
        MyLisView = (ListView) rootView.findViewById(R.id.MyLisView);
        progressBar_paging = (ProgressBar) rootView.findViewById(R.id.progressBar_normal);
        RelativeLayout rlFilter = (RelativeLayout) rootView.findViewById(R.id.rlFilter);
        rlFilter.setOnClickListener(this);
        RelativeLayout rlSort = (RelativeLayout) rootView.findViewById(R.id.rlSort);
        rlSort.setOnClickListener(this);
        RelativeLayout rlView = (RelativeLayout) rootView.findViewById(R.id.rlView);
        rlView.setOnClickListener(this);

    }

    private void sortProductAToZ() {
        Collections.sort(productListDetails, new Comparator<productDetailListItem>() {
            @Override
            public int compare(productDetailListItem item1, productDetailListItem item2) {
                return item1.getName().compareToIgnoreCase(item2.getName());
            }
        });

        productDetailListAdapter.notifyDataSetChanged();
//		mypListingAdapter.notifyDataSetChanged();
    }

    private void sortProductZToA() {
        Collections.sort(productListDetails, new Comparator<productDetailListItem>() {
            @Override
            public int compare(productDetailListItem item1, productDetailListItem item2) {
                return item2.getName().compareToIgnoreCase(item1.getName());
            }
        });

        productDetailListAdapter.notifyDataSetChanged();
//		mypListingAdapter.notifyDataSetChanged();
    }

    private void sortProductLowToHigh() {
        Collections.sort(productListDetails, new Comparator<productDetailListItem>() {
            @Override
            public int compare(productDetailListItem item1, productDetailListItem item2) {
                double price1 = item1.getFinalPrice();
                double price2 = item2.getFinalPrice();
                if (price1 < price2) return -1;
                else return 1;
            }
        });

        productDetailListAdapter.notifyDataSetChanged();
//		mypListingAdapter.notifyDataSetChanged();
    }

    private void sortProductHighToLow() {
        Collections.sort(productListDetails, new Comparator<productDetailListItem>() {
            @Override
            public int compare(productDetailListItem item1, productDetailListItem item2) {
                double price1 = item1.getFinalPrice();
                double price2 = item2.getFinalPrice();

                if (price1 < price2) return 1;
                else return -1;
            }
        });

        productDetailListAdapter.notifyDataSetChanged();
//		mypListingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        if (headerText != null && headerText.length() > 0)
//            MainActivity.setHeaderText(headerText);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
        if(filterListMap!=null)

        super.onResume();
    }

    @Override
    public void onPause() {
        if (progressBar_paging.isShown())
            progressBar_paging.setVisibility(View.GONE);
        if(progressbar_end.isShown())
            progressbar_end.setVisibility(View.GONE);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlFilter:
                if(searchText!=null)
                    GetFilter.getInstance(getActivity()).getDialogue(true,searchText);
                else
                  GetFilter.getInstance(getActivity()).getDialogue(false,null);
                break;
            case R.id.rlSort:
                dialogToSortProducts();
                break;
            case R.id.rlView:
                ShowGridOrListView();
                break;
        }

    }

    private void ShowGridOrListView() {
        ImageView imgView = (ImageView) rootView.findViewById(R.id.imgView);
        if (MyLisView.getVisibility() == View.VISIBLE) {
            MyLisView.setVisibility(View.GONE);
            gridViewProductdetailList.setVisibility(View.VISIBLE);
            imgView.setImageResource(R.drawable.square);

        } else {
            MyLisView.setVisibility(View.VISIBLE);
            gridViewProductdetailList.setVisibility(View.GONE);
            imgView.setImageResource(R.drawable.grid_view);

        }

    }

    private void hitServiceForFilter(String finalUrl) {
       Log.d(TAG, "hitServiceForFilter() called with:url " + finalUrl);
        resetFiltermap();
        ServiceForFilter filterService = new ServiceForFilter();
        filterService.execute(finalUrl);

    }

    public void filterProduct(String categoryKey, String selectedOption) {
        String urlProductListDetails = WebApiManager.getInstance().getFilteredProductListURL(getActivity());
        urlProductListDetails = String.format(urlProductListDetails, sortType, sortOrder, previousCategoryId, categoryKey, selectedOption);
        isComesForFilter = true;
        productListDetails = new ArrayList<>();
        lastInScreen = 0;
        hitServiceForProductDetails(urlProductListDetails);
        String urlStr = WebApiManager.getInstance().getFilterProductsURL(getActivity());
        String finalUrl = "";
        if (categoryKey.equalsIgnoreCase("category_id")) {
            finalUrl = String.format(urlStr, selectedOption, categoryKey, "",sortType,sortOrder);
            previousCategoryId = selectedOption;
        } else {
            finalUrl = String.format(urlStr, previousCategoryId, categoryKey, selectedOption);
        }

        hitServiceForFilter(finalUrl);

        Log.d(TAG, "filterProduct() called with: " + "categoryKey = [" + categoryKey + "], selectedOption = [" + selectedOption + "]");
    }

    public void resetFilterKeys() {
        productListDetails = new ArrayList<>();
        lastInScreen = 0;
        indexOfCurrentPage = 1;
        isComesForFilter = true;
        comesFromReset = true;
        categoryId = FilterManager.getInstance().initialCategory;
        Log.d("FILTERPIYUSH", "Called from 3");
        callProductListApi();
        String urlStr = WebApiManager.getInstance().getFilterProductsURL(getActivity());
        String finalUrl = String.format(urlStr, FilterManager.getInstance().initialCategory, "category_id", "",sortType,sortOrder);
        hitServiceForFilter(finalUrl);

    }

    private void setItemInNextIndex(final int index) {
        gridViewProductdetailList.clearFocus();
        gridViewProductdetailList.post(new Runnable() {
            @Override
            public void run() {
                gridViewProductdetailList.setSelection(index - 1);
            }
        });

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
       searchText=null;

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

    class MyOnScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            if(scrollState== SCROLL_STATE_IDLE&& view.getLastVisiblePosition()==view.list)
//            {
//                progressBar_paging = (ProgressBar) rootView.findViewById(R.id.progressBar_paging);
//                progressBar_paging.setVisibility(View.VISIBLE);
//            }


        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            boolean progressbar_end_shown=false;
            if (!isComesForFilter) {
                lastInScreen = (firstVisibleItem + visibleItemCount);
                if(lastInScreen >= totalItemCount && totalItemCount != totalRecords)
                {
                    progressbar_end = (ProgressBar) rootView.findViewById(R.id.progressBar_paging);
                    progressbar_end.setVisibility(View.VISIBLE);
                    progressbar_end_shown=true;
                }
                else
                {
                    progressbar_end = (ProgressBar) rootView.findViewById(R.id.progressBar_paging);
                    progressbar_end.setVisibility(View.GONE);
                }

                if ((lastInScreen == totalItemCount/2) && !(loadingMore) && (totalItemCount != totalRecords)) {
                    //Log.d(TAG, "onScroll() called with: " + "view = [" + view + "], firstVisibleItem = [" + firstVisibleItem + "], visibleItemCount = [" + visibleItemCount + "], totalItemCount = [" + totalItemCount + "]");
                    indexOfCurrentPage++;
                    isPagination = true;

                    try {
                        Log.d("FILTERPIYUSH", "Called from 2");
                        callProductListLazyApi();
                        progressbar_end_shown=false;
                    } catch (Exception e) {

                    }

                }
                if(progressbar_end_shown)
                   progressbar_end.setVisibility(View.GONE);
            }

        }
    }

    private void callProductListLazyApi() {

        String urlProductListDetails = null;
        if (searchText != null) {
            urlProductListDetails = WebApiManager.getInstance().getSearchURL(getActivity());
            String finalSearchtext = EncodeString.encodeStrBase64Bit(searchText);
            urlProductListDetails = String.format(urlProductListDetails, sortType, sortOrder, finalSearchtext, indexOfCurrentPage, numberOfRecordInEachPage);
//			urlProductListDetails = String.format(urlProductListDetails,searchText,indexOfCurrentPage,numberOfRecordInEachPage);
        } else {
            urlProductListDetails=WebApiManager.getInstance().getCategoryProductsURL();
            urlProductListDetails=String.format(urlProductListDetails,categoryId,sortOrder,sortType,indexOfCurrentPage,numberOfRecordInEachPage,null);
//			urlProductListDetails = String.format(urlProductListDetails,categoryId,indexOfCurrentPage,numberOfRecordInEachPage);
        }
        if(progressbar_end.isShown()) {
         Log.d("PROGRESS","inside shown progressbar which s"+ progressbar_end);
            progressbar_end.setVisibility(View.GONE);
        }
        hitServiceForLazyProductDetails(urlProductListDetails);
    }

    private void hitServiceForLazyProductDetails(String urlProductListDetails) {

        newDataList = new ArrayList<>();
        //final ProgressDialog pDialog;
        String dialog = "";
        if (!isPagination) {
//            progressBar_paging = (ProgressBar) rootView.findViewById(R.id.progressBar_normal);
//            dialog = "Theme1";
        } else {
//            progressBar_paging = (ProgressBar) rootView.findViewById(R.id.progressBar_paging);
//            dialog = "Theme 2";
        }
        Log.d("PAGING", "dialog is " + dialog);
//        progressBar_paging.setVisibility(View.VISIBLE);
        loadingMore = true;
        Log.d(TAG, "service for product list called with final URl : " + urlProductListDetails);

        ServiceForLazyProducts productSevice = new ServiceForLazyProducts();
        productSevice.execute(urlProductListDetails);


    }

    class MyOInItemClickLister implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ImageView img = (ImageView) view.findViewById(R.id.imgV_id);
            SimpleProductFragment.imageDrawable = img.getDrawable();
            BaseFragment fragment;
            productDetailListItem productItemObj = (productDetailListItem) gridViewProductdetailList.getAdapter().getItem(position);
            if (productItemObj.getType().equalsIgnoreCase("simple"))
                fragment = new SimpleProductFragment2();
            else if (productItemObj.getType().equalsIgnoreCase("grouped"))
                fragment = new GroupedProductDetailFragment();
            else
                fragment = new SimpleProductFragment2();

            Bundle bundle = new Bundle();
            bundle.putString(ConstantDataMember.PRO_ID, productItemObj.getId());
            bundle.putString(ConstantDataMember.PRO_NAME, productItemObj.getName());
            fragment.setArguments(bundle);
            try {
                callFragment(fragment, fragment.getClass().getSimpleName());
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    private class ServiceForProducts extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(final String... params) {
            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(params[0],
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            if (progressBar_paging.isShown())
                                progressBar_paging.setVisibility(View.GONE);
//						HideKeyBoard.hideKeyboardFrom(getActivity(), rootView);
                            try {
                                JSONObject strJSNobj = new JSONObject(response);
                                totalRecords = strJSNobj.getInt("total_count");
                                if (totalRecords > 0) {
                                    if (strJSNobj.has("items")) {
                                        JSONArray imageJSNArray = strJSNobj.getJSONArray("items");

									if (strJSNobj.has("category_name")) {
										category_name = strJSNobj.getString("category_name");
//										headerText = category_name;

									}
									else
									{
										if(FilterManager.getInstance().selectedFilterOption!=null);
//										headerText=FilterManager.getInstance().selectedFilterOption;
									}

//									MainActivity.setHeaderText(headerText);
                                        if (imageJSNArray != null) {
                                            newDataList = new GetProductsListDetails().getProductListDetails(imageJSNArray);
                                            int size = productListDetails.size();
                                            if(size!=0)
                                                productListDetails.addAll(size-1,newDataList);
                                            else
                                                productListDetails.addAll(newDataList);
                                        }
                                    }
                                } else {
                                    Activity activity= getActivity();
                                    if(isAdded()&&activity!=null)
                                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.txt_no_product_found), Toast.LENGTH_SHORT).show();
                                    setdataToProductDelailList();
                                    ViewUtils.setTextViewVisibility(rootView, R.id.no_products_found, View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (productListDetails.size() > 0) {
                   /* ViewUtils.setTextViewVisibility(rootView, R.id.txtSortBy, View.VISIBLE);*/
                                if (isComesForFilter) {
                                    isComesForFilter = false;
                                    if (comesFromReset) {
                                        comesFromReset = false;
                                        loadingMore = false;
                                    } else
                                        loadingMore = true;

                                    setdataToProductDelailList();

                                } else {
//                                    if (FilterManager.getInstance().wichSortedApply == 0)
//                                        sortProductAToZ();
//                                    else if (FilterManager.getInstance().wichSortedApply == 1)
//                                        sortProductZToA();
//                                    else if (FilterManager.getInstance().wichSortedApply == 2) {
//                                        sortProductLowToHigh();
//                                    } else
//                                        sortProductHighToLow();

                                    productDetailListAdapter.notifyDataSetChanged();
						/*mypListingAdapter.notifyDataSetChanged();*/
                                    loadingMore = false;
                                }


					/*if (lastInScreen > 1) {
						setItemInNextIndex(lastInScreen);
					}*/
                            } else {
                                Activity activity= getActivity();
                                if(isAdded()&&activity!=null)
                                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.txt_no_product_found), Toast.LENGTH_SHORT).show();
                                setdataToProductDelailList();
                                ViewUtils.setTextViewVisibility(rootView, R.id.no_products_found, View.VISIBLE);
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: ne Product " + error.getMessage());
                            if (progressBar_paging.isShown())
                                progressBar_paging.setVisibility(View.GONE);
                        }
                    });
            return null;
        }
    }

    private class ServiceForLazyProducts extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(params[0],
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            if (progressbar_end.isShown())
                                progressbar_end.setVisibility(View.GONE);
//						HideKeyBoard.hideKeyboardFrom(getActivity(), rootView);
                            try {
                                JSONObject strJSNobj = new JSONObject(response);
                                totalRecords = strJSNobj.getInt("total");
                                if (totalRecords > 0) {
                                    if (strJSNobj.has("data")) {
                                        JSONArray imageJSNArray = strJSNobj.getJSONArray("data");

                                        if (strJSNobj.has("category_name")) {
                                            category_name = strJSNobj.getString("category_name");
//                                            headerText = category_name;

                                        }
                                        else
                                        {
                                            if(FilterManager.getInstance().selectedFilterOption!=null);
//                                                headerText=FilterManager.getInstance().selectedFilterOption;
                                        }

//                                        MainActivity.setHeaderText(headerText);
                                        if (imageJSNArray != null) {
                                            newDataList = new GetProductsListDetails().getProductListDetails(imageJSNArray);

                                            productListDetails.addAll(productListDetails.size()-1,newDataList);
                                        }
                                    }
                                } else {
                                    Activity activity= getActivity();
                                    if(isAdded()&&activity!=null)
                                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.txt_no_product_found), Toast.LENGTH_SHORT).show();
                                    setdataToProductDelailList();
                                    ViewUtils.setTextViewVisibility(rootView, R.id.no_products_found, View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (productListDetails.size() > 0) {
                   /* ViewUtils.setTextViewVisibility(rootView, R.id.txtSortBy, View.VISIBLE);*/
                                if (isComesForFilter) {
                                    isComesForFilter = false;
                                    if (comesFromReset) {
                                        comesFromReset = false;
                                        loadingMore = false;
                                    } else
                                        loadingMore = true;

                                    setdataToProductDelailList();

                                } else {
                                    productDetailListAdapter.notifyDataSetChanged();
						/*mypListingAdapter.notifyDataSetChanged();*/
                                    loadingMore = false;
                                }


					/*if (lastInScreen > 1) {
						setItemInNextIndex(lastInScreen);
					}*/
                            } else {
                                Activity activity= getActivity();
                                if(isAdded()&&activity!=null)
                                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.txt_no_product_found), Toast.LENGTH_SHORT).show();
                                setdataToProductDelailList();
                                ViewUtils.setTextViewVisibility(rootView, R.id.no_products_found, View.VISIBLE);
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: ne Product " + error.getMessage());
                            if (progressbar_end.isShown())
                                progressbar_end.setVisibility(View.GONE);
                        }
                    });
            if (progressbar_end.isShown())
                progressbar_end.setVisibility(View.GONE);
            return null;
        }
    }

    private class ServiceForFilter extends AsyncTask<String, String, String> {
        final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);

        @Override
        protected void onPreExecute() {
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            FilterData.getInstance().clearAll();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(params[0], new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    Log.d("hitServiceForFilter= ", response);
                    if (pDialog.isShowing())
                        pDialog.hide();
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        filterCategoryList = new ArrayList<FilterKeyItem>();
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                String filterName = jsonObj.getString("label");
                                String id = jsonObj.getString("code");
                                JSONArray childFilterArray = jsonObj.getJSONArray("values");
                                if (childFilterArray != null && childFilterArray.length() > 0) {
                                    ArrayList<FilterOptionItem> filterOptionList = new ArrayList<FilterOptionItem>();
                                    for (int j = 0; j < childFilterArray.length(); j++) {
                                        JSONObject jsonObjFilterOpt = childFilterArray.getJSONObject(j);
                                        String optName = jsonObjFilterOpt.getString("label");
                                        String optId = jsonObjFilterOpt.getString("id");
                                        String counter = "";
                                        if (jsonObjFilterOpt.has("count"))
                                            counter = jsonObjFilterOpt.getString("count");
                                        boolean selectedFlag = false;
                                        if (FilterManager.getInstance().previousCategoryId != null && FilterManager.getInstance().previousCategoryId.equalsIgnoreCase(optId))
                                            selectedFlag = true;

                                        FilterOptionItem filterOption = new FilterOptionItem(optId, optName, selectedFlag, counter);
                                        if (optName != null && !optName.equalsIgnoreCase("false"))
                                            filterOptionList.add(filterOption);
                                    }

                                    FilterKeyItem filterCategory = new FilterKeyItem(id, filterName);
                                    //filterCategoryList.add(filterCategory);
                                   // filterListMap.put(filterName, (filterOptionList));
                                    if(FilterData.getInstance().getFilterOptionItemList(filterName)!=null)
                                        filterOptionList.addAll(FilterData.getInstance().getFilterOptionItemList(filterName));
                                    else FilterData.getInstance().setFilterKeyItem(filterCategory);
                                    FilterData.getInstance().setFilterOptionItem(filterName,filterOptionList);

                                    //filterListMap.put(filterName,sortFilterOptionAToZ(filterOptionList));
                                }

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
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
            if(pDialog!=null&&pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("TEST", "Filter map is "+filterListMap.toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        //if(filterListMap!=null)
         //   resetFiltermap();
        Log.d("TEST", "Filter map is "+filterListMap.toString());
    }

    private void resetFiltermap() {

        Iterator it = filterListMap.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry thiss = (Map.Entry) it.next();
            ArrayList<FilterOptionItem> itemlist = (ArrayList<FilterOptionItem>) thiss.getValue();
            Iterator it2 = itemlist.iterator();
            while(it2.hasNext())
            {
                FilterOptionItem item = (FilterOptionItem) it2.next();
                item.setChecked(false);
            }

        }
    }
}
