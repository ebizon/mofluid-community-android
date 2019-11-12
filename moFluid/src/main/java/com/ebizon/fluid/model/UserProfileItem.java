package com.ebizon.fluid.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.io.StringReader;

/**
 * Created by ebizon on 16/12/15.
 */
public class UserProfileItem {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String id;

    public String getStripeID() {
        return stripeID;
    }

    public void setStripeID(String stripeID) {
        this.stripeID = stripeID;
    }

    private String stripeID;

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    private String login_status;
    private AddressData billingAddress;
    private AddressData shippingAddress;

    public UserProfileItem(String firstname, String id, String lastname, String login_status, String password, String username) {
        this.firstname = firstname;
        this.id = id;
        this.lastname = lastname;
        this.login_status = login_status;
        this.password = password;
        this.username = username;
        this.stripeID = "0";
    }

    public UserProfileItem(String firstname, String id, String lastname, String login_status, String password, String username,String stripeID) {
        this.firstname = firstname;
        this.id = id;
        this.lastname = lastname;
        this.login_status = login_status;
        this.password = password;
        this.username = username;
        this.stripeID = stripeID;
    }

    protected UserProfileItem(Parcel in) {
        username = in.readString();
        password = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        id = in.readString();
        login_status = in.readString();
    }



    public String getFirstname() {
        return firstname;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin_status() {
        return login_status;
    }

    public void setLogin_status(String login_status) {
        this.login_status = login_status;
    }

    public String getLastname() {
        return lastname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBillingAddress(AddressData addressData){
        this.billingAddress = addressData;
    }

    public AddressData getBillingAddress(){
        return this.billingAddress;
    }

    public void setShippingAddress(AddressData addressData){
        this.shippingAddress = addressData;
    }

    public AddressData getShippingAddress(){
        return this.shippingAddress;
    }



}
