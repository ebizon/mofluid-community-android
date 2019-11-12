package com.mofluid.magento2.manager;

import android.widget.CheckBox;

/**
 * Created by ebizon201 on 13/8/16.
 */
public class FilterManager {
    public String sortType,sortOrder, previousCategoryId,initialCategory;
    public int wichSortedApply;
    public CheckBox previousCheckBox;
    public String selectedFilterOption;
    public String selectedFilterOptionID;

    private static FilterManager ourInstance = new FilterManager();


    public static FilterManager getInstance() {
        return ourInstance;
    }

    private FilterManager() {
    }
}
