package com.ebizon.fluid.model;

/**
 * Created by ebizon on 17/12/15.
 */
public class MyStateItem {
    private String region_id,region_name;

    public MyStateItem(String region_id, String region_name) {
        this.region_id = region_id;
        this.region_name = region_name;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }
}
