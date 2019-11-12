package com.ebizon.fluid.StripePayment;

import java.io.Serializable;

/**
 * Created by piyush-ios on 28/12/16.
 */
public class StripeuserCard implements Serializable {

    String card_id ;
    String object ;
    String brand;
    String country;
    String cvc_check;
    String exp_month;
    String exp_year;
    String last4;
    String name;/**/

    public StripeuserCard(String card_id, String object, String brand, String country, String cvc_check, String exp_month, String exp_year, String last4, String name) {
        this.card_id = card_id;
        this.object = object;
        this.brand = brand;
        this.country = country;
        this.cvc_check = cvc_check;
        this.exp_month = exp_month;
        this.exp_year = exp_year;
        this.last4 = last4;
        this.name = name;
    }

    public String getCard_id() {
        return card_id;
    }

    public String getObject() {
        return object;
    }

    public String getBrand() {
        return brand;
    }

    public String getCountry() {
        return country;
    }

    public String getCvc_check() {
        return cvc_check;
    }

    public String getExp_month() {
        return exp_month;
    }

    public String getExp_year() {
        return exp_year;
    }

    public String getLast4() {
        return last4;
    }

    public String getName() {
        return name;
    }


}
