package com.ebizon.fluid.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by piyush-ios on 20/4/16.
 */
public class MultiStoreData  implements Parcelable {

    String store_Name;
    String id;
    ArrayList<MultiStoreView> views_Store;

    public MultiStoreData()
    {
        this.views_Store = new ArrayList<>();
    }
    public MultiStoreData(String store_Name, String id, ArrayList<MultiStoreView> views_store)
    {
        this.store_Name= store_Name;
        this.id = id;
        this.views_Store = views_store;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }





    public String getStore_Name() {
        return store_Name;
    }

    public void setStore_Name(String store_Name) {
        this.store_Name = store_Name;
    }

    public ArrayList<MultiStoreView> getViews_Store() {
        return views_Store;
    }

    public void setViews_Store(ArrayList<MultiStoreView> views_Store) {
        this.views_Store = views_Store;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
