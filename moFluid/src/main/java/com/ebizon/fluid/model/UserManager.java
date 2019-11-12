package com.ebizon.fluid.model;

import com.ebizon.fluid.StripePayment.StripeUser;

import java.io.Serializable;

/**
 * Created by manish on 02/02/16.
 */
public class UserManager implements Serializable {
    private static UserManager ourInstance = new UserManager();

    public static UserManager getInstance() {
        return ourInstance;
    }

    private  UserProfileItem userProfileItem = null;

    public StripeUser getStripeUserDetails() {
        return stripeUserDetails;
    }

    public void setStripeUserDetails(StripeUser stripeUserDetails) {
        this.stripeUserDetails = stripeUserDetails;
    }

    private StripeUser stripeUserDetails = null;

    private UserManager() {
    }

    public void setUser(UserProfileItem userProfileItem){
        this.userProfileItem = userProfileItem;
    }

    public UserProfileItem getUser(){
        return this.userProfileItem;
    }

}
