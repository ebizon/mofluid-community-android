package com.mofluid.magento2.fragment;

import android.util.Log;

import com.ebizon.fluid.model.FilterKeyItem;
import com.ebizon.fluid.model.FilterOptionItem;
import com.mofluid.magento2.manager.FilterManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by saddam on 13/2/18.
 */

public class FilterData {
    private static FilterData instance;
    private ArrayList<FilterKeyItem> filterCategoryList;
    private HashMap<String, ArrayList<FilterOptionItem>> filterListMap;
    private String sortType,sortOrder,curCategoryId;
    private final String TAG="FilterData";
    private FilterData(){
        this.filterCategoryList=new ArrayList<FilterKeyItem>();
        this.filterListMap=new HashMap<>();
        this.setSortOrder("asc");
        this.setSortType("name");
    }
    public void clearAll(){
        if(this.filterCategoryList!=null)
            this.filterCategoryList.clear();
       if(this.filterListMap!=null)
           this.filterListMap.clear();
    }
    public static FilterData getInstance(){
        if(instance==null) instance=new FilterData();
        return instance;
    }

    public String getSortType() {
        return this.sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getCurCategoryId() {
        return this.curCategoryId;
    }

    public void setCurCategoryId(String curCategoryId) {
        this.curCategoryId = curCategoryId;
    }

  public boolean isFilterEmpty(){
      if(this.filterListMap.size()==0 || this.filterCategoryList.size()==0)
          return true;
      return false;
  }

   public ArrayList<FilterKeyItem> getFilterCategoryList(){
        return this.filterCategoryList;
   }
   public String getFilterKeyItemName(int i){
        if(this.filterCategoryList==null || this.filterCategoryList.size()<=0)
            return null;
       return this.filterCategoryList.get(i).getName();
   }
    public String getFilterKeyItemID(int i){
        if(this.filterCategoryList==null || this.filterCategoryList.size()<=0)
            return  null;
        return this.filterCategoryList.get(i).getId();
    }
  public void setFilterKeyItem(FilterKeyItem item){
        this.filterCategoryList.add(item);
  }
 public HashMap<String, ArrayList<FilterOptionItem>> getFilterListMap(){
      return this.filterListMap;
 }
 public void setFilterOptionItem(String key,ArrayList<FilterOptionItem> value){
     this.filterListMap.put(key,value);
 }
 public ArrayList<FilterOptionItem> getFilterOptionItem(String key){
     return this.filterListMap.get(key);
 }
 public ArrayList<FilterOptionItem> getFilterOptionItemList(int i){

    return this.getFilterOptionItemList(this.getFilterKeyItemName(i));
 }
    public ArrayList<FilterOptionItem> getFilterOptionItemList(String key){
        return this.filterListMap.get(key);
    }

 public String getSelectedOption(){
     String selectedOption="";
     boolean isFirstItem=true;
     for(Map.Entry entry:this.filterListMap.entrySet()){
         ArrayList<FilterOptionItem> optionsItem=(ArrayList<FilterOptionItem>)entry.getValue();
         for(FilterOptionItem item:optionsItem){
             if(item.isChecked()) {
                 if(isFirstItem) {
                     selectedOption =item.getId();
                     isFirstItem=false;
                 }
                 else
                     selectedOption = selectedOption +","+item.getId();
             }
         }
     }
     Log.d(TAG,"selectedOption="+selectedOption);
     return selectedOption;
 }
public JSONArray getSelectedOptionInJson(){
    JSONArray res = new JSONArray();
    HashMap<String,String> map = new HashMap<>();
    String selectedOption="";
    boolean isFirstItem=true;
    for(Map.Entry entry:this.filterListMap.entrySet()){
        isFirstItem=true;
        selectedOption="";
        String key=(String)entry.getKey();
        ArrayList<FilterOptionItem> optionsItem=(ArrayList<FilterOptionItem>)entry.getValue();
        map.put("code",key.toLowerCase());
        for(FilterOptionItem item:optionsItem){
            if(item.isChecked()) {
                if(isFirstItem) {
                    selectedOption =item.getId();
                    isFirstItem=false;
                }
                else
                    selectedOption = selectedOption +","+item.getId();
            }
            map.put("id",selectedOption);
        }
        if(selectedOption!=null && !selectedOption.equals(""))
            res.put(new JSONObject(map));
    }
Log.d(TAG,"Json filter Data="+res.toString());
    return res;
}
public void uncheckAll(){
    for(Map.Entry entry:this.filterListMap.entrySet()){
        String key=(String)entry.getKey();
        ArrayList<FilterOptionItem> optionsItem=(ArrayList<FilterOptionItem>)entry.getValue();
        for(FilterOptionItem item:optionsItem)
               item.setChecked(false);
        this.setFilterOptionItem(key,optionsItem);
    }
  Log.d(TAG,"All Items Unchecked");
}
}
