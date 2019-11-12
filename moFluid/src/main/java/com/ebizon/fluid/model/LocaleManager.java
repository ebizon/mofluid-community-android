package com.ebizon.fluid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by manish on 01/02/16.
 */
public class LocaleManager {
    private static LocaleManager ourInstance = new LocaleManager();

    public static LocaleManager getInstance() {
        return ourInstance;
    }

    private HashMap<String, MyCountryItem> countryIdMap = new HashMap<>();
    private ArrayList<MyCountryItem> countryItemArrayList = new ArrayList<>();
    private HashMap<String, ArrayList<MyStateItem>> countryStateMap = new HashMap<>();
    private String defaultCountryID;
    private ArrayList<MultiStoreData> multiStoreList = new ArrayList<>();

    private LocaleManager() {
    }

    public void addCountry(MyCountryItem countryItem){
        this.countryIdMap.put(countryItem.getCountry_id(), countryItem);
    }

    public void addMultiStoreData(MultiStoreData multiStoreData){
        this.multiStoreList.add(multiStoreData);
    }

    public Collection<MyCountryItem> getCountries(){return this.countryIdMap.values();}

    public ArrayList<MyCountryItem> getCountryList(){
        return this.countryItemArrayList;
    }

    public void setStateList(String countryCode, ArrayList<MyStateItem> stateItems){
        this.countryStateMap.put(countryCode, stateItems);
    }

    public ArrayList<MyStateItem> getStateList(String countryCode){
        return this.countryStateMap.get(countryCode);
    }

    public MyCountryItem getCountryAtIndex(int index){
        assert(-1 < index && index < this.countryItemArrayList.size());
        return this.countryItemArrayList.get(index);
    }

    public int getCountryIndex(String countryId){
        int index = -1;
        for(int i=0;i<countryItemArrayList.size();i++) {
            if(countryItemArrayList.get(i).getCountry_id().equals(countryId)) {
                index = i;
            }
        }

        return index;
    }

    public MyCountryItem getCountry(String countryId){
        return this.countryIdMap.get(countryId);
    }

    public MyStateItem getStateById(String countryId, String stateId){
        ArrayList<MyStateItem> stateItems = this.countryStateMap.get(countryId);
        MyStateItem stateItem = null;

        if(stateItems != null){
            for(MyStateItem item : stateItems){
                if(item.getRegion_id().equals(stateId)){
                    stateItem = item;
                    break;
                }
            }
        }

        return stateItem;
    }

    public MyStateItem getStateByName(String countryId, String stateName){
        ArrayList<MyStateItem> stateItems = this.countryStateMap.get(countryId);
        MyStateItem stateItem = null;

        if(stateItems != null){
            for(MyStateItem item : stateItems){
                if(item.getRegion_name().equals(stateName)){
                    stateItem = item;
                    break;
                }
            }
        }

        return stateItem;
    }

    public MyStateItem getStateAtIndex(String countryId, int index){
        MyStateItem stateItem = null;

        ArrayList<MyStateItem> stateItems = this.getStateList(countryId);

        if(stateItems != null) {
            assert(-1 < index && index < stateItems.size());
            stateItem = stateItems.get(index);
        }

        return stateItem;
    }

    public int getStateIndex(String countryId, String stateName){
        int index = -1;
        ArrayList<MyStateItem> stateItems = this.getStateList(countryId);
        if(stateItems != null) {
            for (int i = 0; i < stateItems.size(); i++) {
                if (stateItems.get(i).getRegion_id().equals(stateName)) {
                    index = i;
                }
            }
        }

        return index;
    }

    public void setDefaultCountry(String countryId){
        this.defaultCountryID = countryId;
    }

    public String getDefaultCountry() {
        assert(this.defaultCountryID != null);
        return this.defaultCountryID;
    }

    public static void createCountry(JSONObject jsonObject){
        try {
            JSONArray mofluid_countries = jsonObject.getJSONArray("mofluid_countries");

            for (int i = 0; i < mofluid_countries.length(); i++) {
                JSONObject jsnObj = mofluid_countries.getJSONObject(i);
                String country_name = jsnObj.getString("country_name");
                String id = jsnObj.getString("country_id");
                LocaleManager.getInstance().addCountry(new MyCountryItem(country_name, id));
            }

            JSONObject mofluidDefaultCountryJSNObj = jsonObject.getJSONObject("mofluid_default_country");
            String defaultCountryId = mofluidDefaultCountryJSNObj.getString("country_id");
            LocaleManager.getInstance().setDefaultCountry(defaultCountryId);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
