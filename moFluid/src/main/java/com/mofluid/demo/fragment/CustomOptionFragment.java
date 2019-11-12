package com.mofluid.magento2.fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;

import android.support.annotation.IdRes;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.Utils.Validation;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.ShoppingCartItem;
import com.ebizon.fluid.model.CustomOption;
import com.ebizon.fluid.model.CustomOptionSet;
import com.ebizon.fluid.model.CustomShoppingItem;
import com.ebizon.fluid.model.ShoppingItem;
import com.ebizon.fluid.model.ShoppingItemManager;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.CustomOptionSetView;
import com.mofluid.magento2.CustomOptionView;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.CustomValueDrpDwnAdapter;
import com.mofluid.magento2.custome.widget.ShowAlertDialogBox;
import com.mofluid.magento2.service.ConnectionDetector;
import com.mofluid.magento2.service.GetDataForCustomeOption;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Prashant on 25/11/15.
 */


public class CustomOptionFragment  implements View.OnClickListener, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    private final String select;
    private final SimpleProductFragment2 fragment;
    private View rootView;
    private String TAG;
    private String PRODUCT_ID;
    private LinearLayout ll_container;
    ArrayList<TextView> customlist;
    private boolean selected= false;
    private  int counter=0;
    private ArrayList<CustomOptionSet> customOptionCombinations = new ArrayList<>();
    private final HashMap<Integer,CustomOptionSetView> customOptionViewMap = new HashMap<Integer,CustomOptionSetView>();
    CustomShoppingItem customShoppingItem;
    Activity activity;
    private ArrayList<String> isCustomSelected;
    Spinner spinner_custome_value_array;
    private TextView txtVTitle;
    ArrayList<String> custom_default_list;
    ArrayList<CustomOptionSet> customdropDowns;
    ArrayList<CustomOptionSet>customCheckBoxes;
    ArrayList<CustomOptionSet>customRadioButton;
    ArrayList<CustomOptionSet>customtextFeilds;
    ArrayList<CustomOptionSet>customtextArea;
    ArrayList<View> dropdownViewList;
    ArrayList<View>areaViewList;
    ArrayList<View>textViewList;
    ArrayList<View>CheckboxViewList;
    ArrayList<View>radioButtonboxViewList;
    private Double customPriceValue=0.0;
    private Double radioButtonLastPrice=0.0;
    private Double dropDownlastprice=0.0;

    HashMap<String,ArrayList<CustomOptionSet>> categorizedCustomOptionSet;
    private TextView txtPriceView;
    private HashMap<String,String> textmessages;


    public CustomOptionFragment(String product_id, Activity activity, View rootView, SimpleProductFragment2 simpleProductFragment2) {
        this.activity=activity;
        this.PRODUCT_ID=product_id;
        this.rootView=rootView;
        this.select=activity.getResources().getString(R.string.select_small);
        this.fragment = simpleProductFragment2;
        initialized();
        getViewControlls(rootView);
        enableAddToCart();
        /*getValueFromArgument();*/
       /* setNameAndPrice();*/
        hitServiceForFeaturedProducts();

        ConnectionDetector cd = new ConnectionDetector(activity);
        if (!cd.isConnectingToInternet()) {
            new ShowAlertDialogBox().showCustomeDialogBox(activity, activity.getResources().getString(R.string.internet_connection), activity.getResources().getString(R.string.internet_not_avalable));
        }
    }

    private void addViewDynamically() {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (this.customOptionCombinations.size() > 0) {
            View row = layoutInflater.inflate(R.layout.row_custome_option_value_array, null);
            LinearLayout right_linear_layout = (LinearLayout) row.findViewById(R.id.ll_right_layout);
            LinearLayout left_linear_layout = (LinearLayout) row.findViewById(R.id.ll_left_layout);
            TextView txtV_title = (TextView) left_linear_layout.findViewById(R.id.txtV_title);

            for (CustomOptionSet customOptionSet : this.customOptionCombinations) {
                row = layoutInflater.inflate(R.layout.row_custome_option_value_array, null);
                right_linear_layout = (LinearLayout) row.findViewById(R.id.ll_right_layout);
                left_linear_layout = (LinearLayout) row.findViewById(R.id.ll_left_layout);
                txtV_title = (TextView) left_linear_layout.findViewById(R.id.txtV_title);

                String titleName = customOptionSet.getName();
                if (customOptionSet.getIsRequired().equalsIgnoreCase("1")) {
                    titleName = titleName + "<sup>*</sup>";
                }

                txtV_title.setText(Html.fromHtml(titleName));
                txtV_title.setId(customOptionSet.getId());
                customlist.add(txtV_title);

                if (customOptionSet.getType().equalsIgnoreCase("checkbox")) {
                    custom_default_list.add("c");
                    createCheckBox(layoutInflater, right_linear_layout, customOptionSet);
                    ll_container.addView(row);
                } else if (customOptionSet.getType().equalsIgnoreCase("area") || customOptionSet.getType().equalsIgnoreCase("field")) {
                    createAreaField(layoutInflater, right_linear_layout);
                    ll_container.addView(row);
                } else if (customOptionSet.getType().equalsIgnoreCase("radio")) {
                    custom_default_list.add("r");
                    createRadioButton(layoutInflater, right_linear_layout, customOptionSet);
                    ll_container.addView(row);
                } else if (customOptionSet.getType().equalsIgnoreCase("drop_down")) {
                    custom_default_list.add("d");
                    createDropDown(layoutInflater, right_linear_layout, customOptionSet);
                    ll_container.addView(row);
                }
            }

            createGetPriceView(layoutInflater);
        }
    }

    private void createGetPriceView(LayoutInflater layoutInflater){
        View view = layoutInflater.inflate(R.layout.row_custome_option_value_array_final, null);
        LinearLayout left_linear_layout = (LinearLayout) view.findViewById(R.id.ll_left_layout_f);
        txtVTitle = (TextView) left_linear_layout.findViewById(R.id.txtV_title_f);
        ShoppingItem item = ShoppingItemManager.getInstance().getShoppingItem(this.PRODUCT_ID);
        if(item != null) {
            txtVTitle.setText(item.getFinalPriceWithCurrency());
        }
        ll_container.addView(view);

    }

    private void enableAddToCart() {
        ImageView txtVAddToCart = (ImageView) rootView.findViewById(R.id.btn_pdp_add_to_cart);
        TextView txtVBuyNow = (TextView) rootView.findViewById(R.id.btn_pdp_buy_now);
        txtVBuyNow.setOnClickListener(this);
        txtVAddToCart.setOnClickListener(this);
    }



    private boolean validateCustomOptions() {
        // check fro dropdown list
        boolean result = false;
        if(spinner_custome_value_array!=null) {
            if (spinner_custome_value_array.getSelectedItemPosition() != 0)
                if (!isCustomSelected.contains("d"))
                    isCustomSelected.add("d");

        }
        // check for radio and checkbox.
        boolean radio = false;
        boolean check = false;

        Iterator<HashMap.Entry<Integer,CustomOptionSetView>> it = customOptionViewMap.entrySet().iterator();
        while(it.hasNext()) {
            HashMap.Entry<Integer, CustomOptionSetView> entry = it.next();
            CustomOptionSetView no_of_views = entry.getValue();
            Integer key = entry.getKey();
            CustomOptionSet cos = no_of_views.getOptionSet();
            if (!cos.getIsRequired().equalsIgnoreCase("0")) {
                Iterator<CustomOptionView> it2 = no_of_views.getViews().iterator();
                while (it2.hasNext()) {
                    CustomOptionView cst = (CustomOptionView) it2.next();
                    View v = cst.getView();
                    if (cos.getType().equalsIgnoreCase("radio")) {
                        if (((CompoundButton) v).isChecked() == true)
                            radio = true;
                    } else if (cos.getType().equalsIgnoreCase("checkbox")) {
                        int count = ((LinearLayout) v).getChildCount();
                        for (int i = 0; i < count; i++) {
                            View vv = ((LinearLayout) v).getChildAt(i);
                            if (vv instanceof CheckBox)
                                if (((CheckBox) vv).isChecked() == true)
                                    check = true;
                        }
                    }
                }
            }
        }
        if(check==true)
            if(!isCustomSelected.contains("c"))
            isCustomSelected.add("c");

        if(radio==true)
            if(!isCustomSelected.contains("r"))
            isCustomSelected.add("r");
        result = checkandsetcolors(isCustomSelected);

        Log.d("a", "a");
        isCustomSelected.clear();
        return result;
    }
    private boolean checkandsetcolors(ArrayList<String> isCustomSelected) {
        boolean result=false;
        Map<String, Integer> myViewList = getViewIDlist();
        ArrayList<Integer> boxIdList = new ArrayList<>();
        ArrayList<String> fList = new ArrayList<>(custom_default_list);
        fList.removeAll(isCustomSelected);
        if(fList.size()!=0) {
            Iterator<String> it = fList.iterator();
            while (it.hasNext()) {
                switch (it.next()) {
                    case "c":
                        boxIdList.add(myViewList.get("checkbox"));
                        break;
                    case "r":
                        boxIdList.add(myViewList.get("radio"));
                        break;
                    case "d":
                        boxIdList.add(myViewList.get("drop_down"));
                        break;

                }
            }
        }
        else // in case all are selected
        {
            boxIdList.add(0);
        }
        Integer[] arr = boxIdList.toArray(new Integer[boxIdList.size()]);


        Iterator<TextView> it2 = customlist.iterator();
        while(it2.hasNext())
        {
            TextView txt = it2.next();
            for (int i = 0; i <arr.length ; i++) {
                if(txt.getId()==arr[i]) {
                    for(int k = 0; k< customOptionCombinations.size(); k++) {
                        if(String.valueOf(arr[i]).equalsIgnoreCase(String.valueOf(customOptionCombinations.get(k).getId()))){
                            if(customOptionCombinations.get(k).getIsRequired().equalsIgnoreCase("0")){
                                txt.setTextColor(Color.BLACK);
                                result=true;
                            }else{
                                txt.setTextColor(Color.RED);
                                result=false;
                            }
                        }
                    }
                    break;
                }
                else
                    txt.setTextColor(Color.BLACK);
            }

        }
       /* if(isCustomSelected.size()==customOptionCombinations.size())
            result = true;
        else
            result=false;*/

        isCustomSelected.clear();
        fList.clear();
        boxIdList.clear();

        return result;
    }

    private void createAreaField(LayoutInflater layoutInflater, LinearLayout linearLayout) {
        View row_area = layoutInflater.inflate(R.layout.row_area_custome_option, null);
        linearLayout.addView(row_area);
    }

    private void createDropDown(LayoutInflater layoutInflater, LinearLayout linearLayout, CustomOptionSet customOptionSet) {
        ArrayList<CustomOption> list = new ArrayList<>();
        list.add(new CustomOption("", "", "", "", "",select ));
        Iterator<CustomOption> optionIterator = customOptionSet.getOptions();
        while (optionIterator.hasNext()) {
            CustomOption option = optionIterator.next();
            list.add(option);
        }

        View dropDownView = layoutInflater.inflate(R.layout.row_drop_down, null);
        spinner_custome_value_array = (Spinner) dropDownView.findViewById(R.id.spinner_custome_value_array);
        linearLayout.addView(dropDownView);

        CustomValueDrpDwnAdapter adapter = new CustomValueDrpDwnAdapter(activity, list);
        spinner_custome_value_array.setAdapter(adapter);
        spinner_custome_value_array.setPrompt("Please Select");
        spinner_custome_value_array.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CustomValueDrpDwnAdapter drpDwnAdapter = (CustomValueDrpDwnAdapter) adapterView.getAdapter();
                if (drpDwnAdapter != null) {
                    drpDwnAdapter.select(i);
                }
                validateCustomOptions();
                updateFinalPrice();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Empty

            }
        });
    }

    private void createRadioButton(LayoutInflater layoutInflater, LinearLayout linearLayout, CustomOptionSet customOptionSet) {
        Iterator<CustomOption> optionIterator = customOptionSet.getOptions();
        ArrayList<CustomOptionView> views = new ArrayList<>();
        while (optionIterator.hasNext()) {
            CustomOption option = optionIterator.next();

            View radioButtonView = layoutInflater.inflate(R.layout.row_radio_btn, null);
            RadioButton radioButton = (RadioButton) radioButtonView.findViewById(R.id.rdo_btn_id);
            radioButton.setText(option.getTitleWithPrice());
            linearLayout.addView(radioButtonView);

            radioButton.setId(option.getId());
            radioButton.setTag(customOptionSet.getId());
            radioButton.setOnClickListener(this);
            CustomOptionView customOptionView = new CustomOptionView(option, radioButton);
            views.add(customOptionView);
        }
        this.customOptionViewMap.put(customOptionSet.getId(), new CustomOptionSetView(customOptionSet, views));
    }

    private void createCheckBox(LayoutInflater layoutInflater, LinearLayout linearLayout, CustomOptionSet customOptionSet) {
        Iterator<CustomOption> optionIterator = customOptionSet.getOptions();
        ArrayList<CustomOptionView> views = new ArrayList<>();

        while (optionIterator.hasNext()){
            CustomOption option = optionIterator.next();

            View checkBoxView = layoutInflater.inflate(R.layout.row_check_box, null);
            CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.chk_id);
            checkBox.setText(option.getTitleWithPrice());
            linearLayout.addView(checkBoxView);

            checkBox.setId(option.getId());
            checkBox.setTag(customOptionSet.getId());
            checkBox.setOnClickListener(this);

            CustomOptionView customOptionView = new CustomOptionView(option, checkBoxView);
            views.add(customOptionView);
            this.customOptionViewMap.put(customOptionSet.getId(), new CustomOptionSetView(customOptionSet, views));
        }
    }

    private void getViewControlls(View rootView) {
        ll_container = (LinearLayout) rootView.findViewById(R.id.ll_container);
        textmessages=new HashMap<>();

    }

    private void initialized() {
        TAG = getClass().getSimpleName();
        customlist = new ArrayList<>();
        isCustomSelected = new ArrayList<>();
        custom_default_list= new ArrayList<>();
    }

    private void hitServiceForFeaturedProducts() {
        String urlCustomOptionAttribute = WebApiManager.getInstance().getProductDetailURL(activity);
        urlCustomOptionAttribute = String.format(urlCustomOptionAttribute, PRODUCT_ID);

        final ProgressDialog pDialog = new ProgressDialog(activity, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.show();

        NetworkAPIManager.getInstance(activity).sendGetRequest(urlCustomOptionAttribute, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (pDialog.isShowing())
                    pDialog.cancel();
                Log.d(TAG, response);
                Log.d("Volley= ", response);

                try {
                    JSONObject strJSNobj = new JSONObject(response);
                    JSONArray customOptionJSNArray = strJSNobj.getJSONArray("custom_option");
                    customOptionCombinations = new GetDataForCustomeOption().getCustomeOptionAttribute(customOptionJSNArray);
                    ShoppingItem item = ShoppingItemManager.getInstance().getShoppingItem(PRODUCT_ID.trim());
                    if(item != null) {
                        customShoppingItem = new CustomShoppingItem(item.getId(), item.getName(), item.getSku(), item.getImage(), item.getPriceStr(), item.getSpecialPriceStr(), item.getInStock(), item.getStock(), item.getType(), customOptionCombinations, item.getThumbnail(),textmessages);
                     /*   customPriceValue=customShoppingItem.getPrice();
                        txtPriceView.setText(String.valueOf(customPriceValue));*/
                        ShoppingItemManager.getInstance().addShoppingItem(customShoppingItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               categorizedCustomOptions(customOptionCombinations);
                //addViewDynamically();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());

                if (pDialog.isShowing())
                    pDialog.cancel();
            }
        });

    }

    private void categorizedCustomOptions(ArrayList<CustomOptionSet> customOptionCombinations) {
        initializeLists();
        for(int i=0;i<customOptionCombinations.size();i++){
            CustomOptionSet combination=customOptionCombinations.get(i);
            String type=combination.getType();
            switch (type){
                case "drop_down":
                    customdropDowns.add(combination);
                    break;
                case "radio":
                    customRadioButton.add(combination);
                    break;
                case "checkbox":
                    customCheckBoxes.add(combination);
                    break;
                case "area":
                    customtextArea.add(combination);
                    break;
                case "field":
                    customtextFeilds.add(combination);
                    break;
            }

        }
        createAllLayout();
    }

    private void createAllLayout() {
        createDropDowns();
        createTextAreaFeilds();
        creaTextFeilds();
        createRadioButtonslayout();
        createCheckBoxLayout();
        createcustomProductPrice();
    }

    private void createcustomProductPrice() {
        LayoutInflater inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.custom_price,null);
        txtPriceView = (TextView) view.findViewById(R.id.custom_pricevalue);
        customPriceValue=customShoppingItem.getPrice();
        txtPriceView.setText(Utils.appendWithCurrencySymbol(customPriceValue));
        ll_container.addView(view);
    }

    private void createCheckBoxLayout() {
        CheckboxViewList=new ArrayList<>();
        LayoutInflater inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(customCheckBoxes!=null&&customCheckBoxes.size()>0){
            for(int i=0;i<customCheckBoxes.size();i++) {
                View radioButtonView = inflater.inflate(R.layout.row_check_boxes, null);
                TextView dropDownLabel = (TextView) radioButtonView.findViewById(R.id.checkboxlabel);
                TextView required = (TextView) radioButtonView.findViewById(R.id.checkboxrequiredstar);
                dropDownLabel.setText(customCheckBoxes.get(i).getName());
                LinearLayout checkboxes = (LinearLayout) radioButtonView.findViewById(R.id.checkboxlayout);
                if(!customCheckBoxes.get(i).getIsRequired().equalsIgnoreCase("0")){
                    required.setVisibility(View.VISIBLE);
                }
                ArrayList<CustomOption> options=customCheckBoxes.get(i).getoptionsList();
                setcheckboxgroup(options,checkboxes);
                radioButtonView.setId(customCheckBoxes.get(i).getId());
                CheckboxViewList.add(radioButtonView);
                ll_container.addView(radioButtonView);
            }

        }
    }

    private void setcheckboxgroup(ArrayList<CustomOption> options, LinearLayout layout) {
        if(options.size()>0) {
            for (int i = 0; i < options.size(); i++) {
                CheckBox checkbox=new CheckBox(activity);
                checkbox.setId(options.get(i).getId());
                checkbox.setText(options.get(i).getTitleWithPrice());
                checkbox.setTextColor(Color.DKGRAY);
                checkbox.setOnCheckedChangeListener(this);
                layout.addView(checkbox);
            }
        }
    }

    public boolean validateCheckbox(){
        boolean isvalid=true;
        ArrayList<Boolean> validateList=new ArrayList<>();
        if(customCheckBoxes.size()>0){
            for(int i=0;i<customCheckBoxes.size();i++){
                CustomOptionSet set=customCheckBoxes.get(i);
                int id=set.getId();
                if(set.getIsRequired().equalsIgnoreCase("0")){
                    for(int j=0;j<CheckboxViewList.size();j++){
                        View buttonrow=CheckboxViewList.get(j);
                        int rowid=CheckboxViewList.get(j).getId();
                        if(id==rowid){
                            ArrayList<CheckBox> notrequiredcheckBoxes=validateIsChecked(set.getoptionsList(),buttonrow);
                        }
                    }
                    continue;
                }
                for(int j=0;j<CheckboxViewList.size();j++){
                    View buttonrow=CheckboxViewList.get(j);
                    int rowid=CheckboxViewList.get(j).getId();
                    if(id==rowid){
                        ArrayList<CheckBox> requiredcheckBoxes=validateIsChecked(set.getoptionsList(),buttonrow);
                        if(requiredcheckBoxes.size()>0){
                            validateList.add(true);
                            TextView textView=(TextView)buttonrow.findViewById(R.id.checkboxlabel);
                            textView.setTextColor(Color.GRAY);
                        }else{
                            validateList.add(false);
                            TextView textView=(TextView)buttonrow.findViewById(R.id.checkboxlabel);
                            textView.setTextColor(Color.RED);
                        }


                    }
                }
            }
            for (int i = 0; i < validateList.size(); i++) {
                isvalid = true;
                if (validateList.get(i) == false) {
                    isvalid = false;
                    break;
                }
            }

        }
        return isvalid;
    }

    private ArrayList<CheckBox> validateIsChecked(ArrayList<CustomOption> list, View buttonrow) {
        ArrayList<CheckBox> checkBoxes=new ArrayList<>();
        boolean isvalid=false;
        LinearLayout linearLayout=(LinearLayout)buttonrow.findViewById(R.id.checkboxlayout);
        int count=linearLayout.getChildCount();
        for(int i=0;i<count;i++){
            CheckBox checkBox=(CheckBox)linearLayout.getChildAt(i);
            boolean ischecked= checkBox.isChecked();
            if(ischecked){
                list.get(i).setSelected(true);
                checkBoxes.add(checkBox);
            }
        }


        return checkBoxes;
    }







    private void createRadioButtonslayout() {
        radioButtonboxViewList=new ArrayList<>();
        LayoutInflater inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(customRadioButton!=null&&customRadioButton.size()>0){
            for(int i=0;i<customRadioButton.size();i++) {
                View radioButtonView = inflater.inflate(R.layout.row_radio_button, null);
                TextView dropDownLabel = (TextView) radioButtonView.findViewById(R.id.radiolabel);
                TextView required = (TextView) radioButtonView.findViewById(R.id.radiorequiredstar);
                dropDownLabel.setText(customRadioButton.get(i).getName());
                RadioGroup radiogroup = (RadioGroup) radioButtonView.findViewById(R.id.radiogroup);
                if(!customRadioButton.get(i).getIsRequired().equalsIgnoreCase("0")){
                    required.setVisibility(View.VISIBLE);
                }
                ArrayList<CustomOption> options=customRadioButton.get(i).getoptionsList();
                radiogroup.setOnCheckedChangeListener(this);
                setRadioButtonsToradioGroup(options,radiogroup);
                radioButtonView.setId(customRadioButton.get(i).getId());
                radiogroup.setTag(customRadioButton.get(i).getId());
                radioButtonboxViewList.add(radioButtonView);
                ll_container.addView(radioButtonView);
            }

        }
    }

    private void setRadioButtonsToradioGroup(ArrayList<CustomOption> options, RadioGroup radiogroup) {
        if(options.size()>0) {
            for (int i = 0; i < options.size(); i++) {
                RadioButton radiobutton=new RadioButton(activity);
                radiobutton.setId(options.get(i).getId());
                radiobutton.setText(options.get(i).getTitleWithPrice());
                radiobutton.setTextColor(Color.DKGRAY);
                radiogroup.addView(radiobutton);
            }
        }

    }

    public boolean validateRadioButtons(){
        boolean isvalid=true;
        ArrayList<Boolean> validateList=new ArrayList<>();
        if(customRadioButton.size()>0){
            for(int i=0;i<customRadioButton.size();i++){
                CustomOptionSet set=customRadioButton.get(i);
                int id=set.getId();
                if(set.getIsRequired().equalsIgnoreCase("0")){
                    continue;
                }
                for(int j=0;j<radioButtonboxViewList.size();j++){
                    View buttonrow=radioButtonboxViewList.get(j);
                    int rowid=radioButtonboxViewList.get(j).getId();
                    ArrayList<CustomOption>list=set.getoptionsList();
                    if(id==rowid){
                        boolean ischecked=validatecheckboxisChecked(list,id,buttonrow);
                        validateList.add(ischecked);

                    }
                }
            }
            for (int i = 0; i < validateList.size(); i++) {
                isvalid = true;
                if (validateList.get(i) == false) {
                    isvalid = false;
                    break;
                }
            }

        }
        return isvalid;
    }

    private boolean validatecheckboxisChecked( ArrayList<CustomOption>list,int id, View buttonrow) {
        boolean isvalid=false;
        RadioGroup group=(RadioGroup) buttonrow.findViewById(R.id.radiogroup);
        TextView label=(TextView) buttonrow.findViewById(R.id.radiolabel);
        int checkedid=group.getCheckedRadioButtonId();
        if(checkedid>0){
            isvalid=true;
            label.setTextColor(Color.GRAY);
         /*   for (int i=0;i<list.size();i++){
                if(checkedid==list.get(i).getId()){
                    list.get(i).setSelected(true);
                }
            }*/
        }else{
            label.setTextColor(Color.RED);
            label.setFocusable(true);
            label.setFocusableInTouchMode(true);
        }
        return isvalid;
    }

    private void creaTextFeilds() {
        textViewList=new ArrayList<>();
        if(customtextFeilds.size()>0) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < customtextFeilds.size(); i++) {
                View textfeild = inflater.inflate(R.layout.row_area_custome_option, null);
                TextView areaLabel = (TextView) textfeild.findViewById(R.id.textarealabel);
                TextView required = (TextView) textfeild.findViewById(R.id.arearequiredstar);
                EditText edtText = (EditText) textfeild.findViewById(R.id.txtV_row_area_id);
                edtText.setMinHeight(30);
                areaLabel.setText(customtextFeilds.get(i).getName());
                if (!customtextFeilds.get(i).getIsRequired().equalsIgnoreCase("0")) {
                    required.setVisibility(View.VISIBLE);
                }
                textfeild.setId(customtextFeilds.get(i).getId());
                textViewList.add(textfeild);
                ll_container.addView(textfeild);
            }
        }
    }

    public  boolean validateTextFeilds(){
        boolean isvalidate=true;
        ArrayList<Boolean> isvalid=new ArrayList<>();
        if(customtextFeilds.size()>0){
            for(int i=0;i<customtextFeilds.size();i++){
                CustomOptionSet set=customtextFeilds.get(i);
                int id=set.getId();
                if(set.getIsRequired().equalsIgnoreCase("0")){
                    continue;
                }
                for (int j=0;j<textViewList.size();j++){
                    if(id==textViewList.get(j).getId()){
                        boolean isvalidfeild=checkforvalidtextfeild(id,textViewList.get(j));
                        isvalid.add(isvalidfeild);
                    }
                }
            }
            for (int i = 0; i < isvalid.size(); i++) {
                isvalidate = true;
                if (isvalid.get(i) == false) {
                    isvalidate = false;
                    break;
                }
            }
        }
        return isvalidate;
    }

    private boolean checkforvalidtextfeild(int id, View view) {
        boolean isvalid=true;
        EditText text=(EditText)view.findViewById(R.id.txtV_row_area_id);
        TextView label=(TextView)view.findViewById(R.id.textarealabel);
        String value=text.getText().toString();
        if(Validation.isStrNotEmpty(value)){
            label.setTextColor(Color.GRAY);
            textmessages.put(String.valueOf(id),value);
        }else{
            isvalid=false;
            label.setTextColor(Color.RED);
        }
        return isvalid;
    }



    private void createTextAreaFeilds() {
        areaViewList=new ArrayList<>();
        if(customtextArea.size()>0) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < customtextArea.size(); i++) {
                View area = inflater.inflate(R.layout.row_area_custome_option, null);
                TextView areaLabel = (TextView) area.findViewById(R.id.textarealabel);
                TextView required = (TextView) area.findViewById(R.id.arearequiredstar);
                areaLabel.setText(customtextArea.get(i).getName());
                if (!customtextArea.get(i).getIsRequired().equalsIgnoreCase("0")) {
                    required.setVisibility(View.VISIBLE);
                }
                area.setId(customtextArea.get(i).getId());
                areaViewList.add(area);
                ll_container.addView(area);
            }
        }
    }


    public  boolean validateAreaFeilds(){
        boolean isvalidate=true;
        ArrayList<Boolean> isvalid=new ArrayList<>();
        if(customtextArea.size()>0){
            for(int i=0;i<customtextArea.size();i++){
                CustomOptionSet set=customtextArea.get(i);
                int id=set.getId();
                if(set.getIsRequired().equalsIgnoreCase("0")){
                    continue;
                }
                for (int j=0;j<areaViewList.size();j++){
                    if(id==areaViewList.get(j).getId()){
                        boolean isvalidfeild=checkforvalidAreafeild(id,areaViewList.get(j));
                        isvalid.add(isvalidfeild);
                    }
                }
            }
            for(int i=0;i<isvalid.size();i++){
                isvalidate=true;
                if(isvalid.get(i)==false){
                    isvalidate=false;
                    break;
                }
            }
        }
        return isvalidate;
    }
    private boolean checkforvalidAreafeild(int id, View view) {
        boolean isvalid=true;
        EditText text=(EditText)view.findViewById(R.id.txtV_row_area_id);
        TextView textlabelk=(TextView)view.findViewById(R.id.textarealabel);
        String value=text.getText().toString();
        if(Validation.isStrNotEmpty(value)){
            textlabelk.setTextColor(Color.GRAY);
            textmessages.put(String.valueOf(id),value);
        }else{
            textlabelk.setTextColor(Color.RED);
            isvalid=false;
        }
        return isvalid;
    }



    private void createDropDowns() {
        dropdownViewList=new ArrayList<>();
        LayoutInflater inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(customdropDowns!=null&&customdropDowns.size()>0){
            for(int i=0;i<customdropDowns.size();i++) {
                View dropdownrow = inflater.inflate(R.layout.row_custom_option_drop_down, null);
                TextView dropDownLabel = (TextView) dropdownrow.findViewById(R.id.dropdwnlabel);
                TextView required = (TextView) dropdownrow.findViewById(R.id.requiredstar);
                dropDownLabel.setText(customdropDowns.get(i).getName());
                Spinner dropDown = (Spinner) dropdownrow.findViewById(R.id.spinerDropDown);
                if(!customdropDowns.get(i).getIsRequired().equalsIgnoreCase("0")){
                    required.setVisibility(View.VISIBLE);
                }
                ArrayList<CustomOption> options=customdropDowns.get(i).getoptionsList();
                CustomOption customOption=new CustomOption("0","0.0","","","","Please Select");
                options.add(0,customOption);
                CustomValueDrpDwnAdapter adapter = new CustomValueDrpDwnAdapter(activity, options);
                dropDown.setAdapter(adapter);
                dropDown.setOnItemSelectedListener(this);
                dropdownrow.setId(customdropDowns.get(i).getId());
                dropDown.setTag(customdropDowns.get(i).getId());
               // dropDown.setId(customdropDowns.get(i).getId());
                dropdownViewList.add(dropdownrow);
                ll_container.addView(dropdownrow);
            }

        }
    }

    public boolean validateDropDowns(){
        boolean isvalidate=true;
        ArrayList<Boolean> isvalid=new ArrayList<>();
        if(customdropDowns.size()>0){
            for(int i=0;i<customdropDowns.size();i++){
                CustomOptionSet set=customdropDowns.get(i);
                int  id=set.getId();
                if(set.getIsRequired().equalsIgnoreCase("0")){
                    continue;
                }
                for (int j=0;j<dropdownViewList.size();j++){
                    int viewID=dropdownViewList.get(j).getId();
                    if(id==viewID){
                        View view=dropdownViewList.get(j);
                        ArrayList<CustomOption>list=customdropDowns.get(i).getoptionsList();
                       Spinner spinner=(Spinner)view.findViewById(R.id.spinerDropDown);
                        boolean is=checkforSelection(dropdownViewList.get(j),set,list);
                        isvalid.add(is);
                    }
                }
            }

            for(int i=0;i<isvalid.size();i++){
                isvalidate=true;
                if(isvalid.get(i)==false){
                    isvalidate=false;
                    break;
                }
            }
        }

        return isvalidate;
    }

    private boolean checkforSelection(View view, CustomOptionSet set, ArrayList<CustomOption>list) {
        boolean isSelected=true;
        TextView dropDownLabel = (TextView) view.findViewById(R.id.dropdwnlabel);
        TextView required = (TextView) view.findViewById(R.id.requiredstar);
        Spinner dropDown = (Spinner) view.findViewById(R.id.spinerDropDown);
        int position=dropDown.getSelectedItemPosition();
        if(position==0){
            dropDownLabel.setTextColor(Color.RED);
            isSelected=false;
        }else {
           // CustomOption customOption=(CustomOption) dropDown.getSelectedItem();
          /*  for(int l=0;l<list.size();l++){
                if(list.get(l).getSelected()){
                    list.get(l).setSelected(false);
                    //decreaseCustomPriceValue(list.get(l).getPrice());
                    break;
                }
            }*/
          //  customOption.setSelected(true);
            dropDownLabel.setTextColor(Color.GRAY);
        }
       return isSelected;
    }

    private void initializeLists() {
        customdropDowns=new ArrayList<>();
        customCheckBoxes=new ArrayList<>();
        customRadioButton=new ArrayList<>();
        customtextArea=new ArrayList<>();
        customtextFeilds=new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_pdp_buy_now:
            case R.id.btn_pdp_add_to_cart:
            {
                if (customShoppingItem==null) {
                    throw new NullPointerException("null");
                }
                boolean isValid=validatecustomoptionsnew();
                if(isValid==true) {
                    if (checkStockofProduct()) {
                        createNewCustomOptionproduct();
                        ShoppingItem item = ShoppingItemManager.getInstance().getShoppingItem(PRODUCT_ID.trim());
                        customShoppingItem = new CustomShoppingItem(item.getId(), item.getName(), item.getSku(), item.getImage(), item.getPriceStr(), item.getSpecialPriceStr(), item.getInStock(), item.getStock(), item.getType(), customOptionCombinations, item.getThumbnail(), textmessages);
                        String price = customShoppingItem.getPriceExpandedStr();
                        ShoppingCartItem itemObj = new ShoppingCartItem(customShoppingItem, 1);
                        ShoppingCart.getInstance().addItem(itemObj, fragment);
                        if (view.getId() == R.id.btn_pdp_buy_now) {
                            MyCartFragment myCartFragment = new MyCartFragment();
                            FragmentManager fm = activity.getFragmentManager();
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(activity.getApplicationContext(), fragmentTransaction, new ViewOrder(), myCartFragment, R.id.content_frame);
                            String fragmentName = myCartFragment.getClass().getSimpleName();
                            fragmentTransactionExtended.addTransition(7);
                            if (fragmentName != null) {
                                fragmentTransaction.addToBackStack(fragmentName);
                            }
                            fragmentTransactionExtended.commit();
                        } else {
                            Toast.makeText(activity, activity.getResources().getString(R.string.item_added), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(activity.getApplicationContext(), "Please select above mandatory options", Toast.LENGTH_SHORT).show();
                }

            }
                break;
            default:
            {
                int tagOfSet = (int) view.getTag();
                int optionTag = view.getId();

                CustomOptionSetView optionView = this.customOptionViewMap.get(tagOfSet);
                if (optionView != null){
                    optionView.check(optionTag);
                    updateFinalPrice();
                    validateCustomOptions();
                }
            }
        }

    }
    private boolean checkStockofProduct() {
        boolean isvalid=true;
        int quantity=customShoppingItem.getStockQuantity();
        if(quantity<1){
            isvalid=false;
        }
        return isvalid;
    }

    private void createNewCustomOptionproduct() {
        customOptionCombinations.clear();
        for(int i=0;i<customdropDowns.size();i++){
            customOptionCombinations.add(customdropDowns.get(i));
        }
        for(int i=0;i<customRadioButton.size();i++){
            customOptionCombinations.add(customRadioButton.get(i));
        }
        for(int i=0;i<customCheckBoxes.size();i++){
            customOptionCombinations.add(customCheckBoxes.get(i));
        }
    }

    private boolean validatecustomoptionsnew() {
        boolean isValidCustomOptions=false;
        boolean isValid=validateDropDowns();
        boolean isvalidareas=validateAreaFeilds();
        boolean isvalidradiobuttons=validateRadioButtons();
        boolean isValidCheckBox=validateCheckbox();
        boolean isvalidtextfeilds=validateTextFeilds();
        if(isValid&&isvalidareas&&isvalidradiobuttons&&isValidCheckBox&&isvalidtextfeilds){
            isValidCustomOptions=true;
        }
        return isValidCustomOptions;
    }

    private void updateFinalPrice() {
        if (customShoppingItem==null) {
            throw new NullPointerException();
        }
        String price = customShoppingItem.getPriceExpandedStr();
        txtVTitle.setText(price);
//        enableAddToCart();


    }

    public Map<String,Integer> getViewIDlist() {
        Map<String,Integer> viewIDlist = new HashMap<>();
        for(CustomOptionSet optionSet : customOptionCombinations)
        {
            viewIDlist.put(optionSet.getType(), optionSet.getId());
        }
        return viewIDlist;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long p) {
        int id= (int) adapterView.getTag();
      //  int id=((View) adapterView.getParent()).getId();
        CustomOption customOption = (CustomOption) adapterView.getSelectedItem();
        int customID=customOption.getId();
            for (int k=0;k<customdropDowns.size();k++){
                if(id==customdropDowns.get(k).getId()){
                    ArrayList<CustomOption> set=customdropDowns.get(k).getoptionsList();
                    for(int l=0;l<set.size();l++){
                        if(set.get(l).getSelected()){
                            set.get(l).setSelected(false);
                            decreaseCustomPriceValue(set.get(l).getPrice());
                            break;
                        }
                    }
                    for(int m=1;m<set.size();m++) {
                        if(set.get(m).getId()==customID){
                            double price1 = set.get(m).getPrice();
                            increaseCustomPriceValue(price1);
                            set.get(m).setSelected(true);
                            break;
                        }
                    }
                    break;
                }

        }
        }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        int checkid= (int) radioGroup.getTag();
        for (int k=0;k<customRadioButton.size();k++){
            if(checkid==customRadioButton.get(k).getId()){
                ArrayList<CustomOption> set=customRadioButton.get(k).getoptionsList();
                for(int l=0;l<set.size();l++){
                    if(set.get(l).getSelected()){
                        set.get(l).setSelected(false);
                        decreaseCustomPriceValue(set.get(l).getPrice());
                        break;
                    }
                }
                for(int m=0;m<set.size();m++) {
                    if(set.get(m).getId()==i){
                        double price = set.get(m).getPrice();
                        increaseCustomPriceValue(price);
                        set.get(m).setSelected(true);
                        break;
                    }
                }
                break;
            }
        }
    }
    private void decreaseCustomPriceValue(double price) {
        customPriceValue=customPriceValue-price;
        txtPriceView.setText(Utils.appendWithCurrencySymbol(customPriceValue));
        txtPriceView.setTextColor(Color.RED);
    }

    private void increaseCustomPriceValue(double price) {
       customPriceValue=customPriceValue+price;
        txtPriceView.setText(String.valueOf(Utils.appendWithCurrencySymbol(customPriceValue)));
        txtPriceView.setTextColor(Color.RED);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int checkid=compoundButton.getId();
        for (int k=0;k<customCheckBoxes.size();k++){
            ArrayList<CustomOption> set=customCheckBoxes.get(k).getoptionsList();
                for(int l=0;l<set.size();l++){
                    if(set.get(l).getId()==checkid){
                        if(set.get(l).getSelected()) {
                            double price = set.get(l).getPrice();
                            set.get(l).setSelected(false);
                            decreaseCustomPriceValue(price);

                        }else{
                            double price = set.get(l).getPrice();
                            set.get(l).setSelected(true);
                            increaseCustomPriceValue(price);
                        }
                    }
                }
            }

    }
}
