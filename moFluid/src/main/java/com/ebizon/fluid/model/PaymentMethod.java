package com.ebizon.fluid.model;

/**
 * Created by piyush-ios on 19/12/16.
 */
public class PaymentMethod {

    String title;
    String pay_code;
    String activeStatus;
    boolean activeStatusBool;
    String acc_id;
    String acc_key;
    String acc_email_linked;
    String mode;
    String description;

    public String getMode() {
        return mode;
    }

    public PaymentMethod(String title, String pay_code, String activeStatus, String acc_id, String acc_key, String acc_email_linked, String mode,String description) {
        this.title = title;
        this.pay_code = pay_code;
        this.activeStatus = activeStatus;
        this.acc_id = acc_id;
        this.acc_key = acc_key;
        this.acc_email_linked = acc_email_linked;
        this.mode=mode;
        this.description=description;

        if(activeStatus.equals("1"))
            this.activeStatusBool= true;
        else
            this.activeStatusBool = false;
    }

    public String getTitle() {
        return title;
    }

    public String getPay_code() {
        return pay_code;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public boolean isActiveStatusBool() {
        return activeStatusBool;
    }

    public String getAcc_id() {
        return acc_id;
    }

    public String getAcc_key() {
        return acc_key;
    }

    public String getAcc_email_linked() {
        return acc_email_linked;
    }
    public String getDescription(){
        return this.description;
    }


}
