package com.ebizon.fluid.model;

/**
 * Created by ebizon on 16/11/15.
 */
public class MyCountryItem {
    private String country_name;
    private String country_id;

    public MyCountryItem(String country_name, String country_id) {
        this.country_name = country_name;
        this.country_id = country_id;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }
}
