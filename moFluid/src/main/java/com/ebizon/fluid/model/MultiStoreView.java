package com.ebizon.fluid.model;

/**
 * Created by piyush-ios on 20/4/16.
 */
public class MultiStoreView {
    String name;
    String id;
    String currency;
    String language_code;

    public String getLanguage_code() {
        return language_code;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public MultiStoreView()
    {

    }
    public MultiStoreView(String name, String id, String currency, String language_code)
    {
        this.name= name;
        this.id= id;
        this.currency=currency;
        this.language_code=language_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }





}
