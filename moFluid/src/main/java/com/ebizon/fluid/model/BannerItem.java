package com.ebizon.fluid.model;

import android.util.Log;

/**
 * Created by ebizon on 27/10/15.
 */
public class BannerItem {
    private  String mofluid_theeme_id;
    private  String mofluid_store_id;
    private  String mofluid_image_id;
    private  String mofluid_image_type;
    private  String mofluid_image_label;
    private  String mofluid_image_value;
    private  String mofluid_image_helptext;
    private  String mofluid_image_helplink;
    private  String mofluid_image_required;
    private  String mofluid_image_sort_order;
    private  String mofluid_image_isdefault;
    private  String mofluid_image_action;
    private  String mofluid_image_data;

    public BannerItem(String mofluid_theeme_id, String mofluid_store_id, String mofluid_image_id, String mofluid_image_type, String mofluid_image_label, String mofluid_image_value, String mofluid_image_helptext, String mofluid_image_helplink, String mofluid_image_required, String mofluid_image_sort_order, String mofluid_image_isdefault, String mofluid_image_action, String mofluid_image_data) {
        this.mofluid_theeme_id = mofluid_theeme_id;
        this.mofluid_store_id = mofluid_store_id;
        this.mofluid_image_id = mofluid_image_id;
        this.mofluid_image_type = mofluid_image_type;
        this.mofluid_image_label = mofluid_image_label;
        this.mofluid_image_helptext = mofluid_image_helptext;
        this.mofluid_image_value = mofluid_image_value;
        this.mofluid_image_helplink = mofluid_image_helplink;
        this.mofluid_image_required = mofluid_image_required;
        this.mofluid_image_sort_order = mofluid_image_sort_order;
        this.mofluid_image_isdefault = mofluid_image_isdefault;
        this.mofluid_image_action = mofluid_image_action;
        this.mofluid_image_data = mofluid_image_data;

        String ATG = BannerItem.class.getName();
        Log.d(ATG,mofluid_image_value+" "+mofluid_image_id);
    }

    public String getMofluid_theeme_id() {
        return mofluid_theeme_id;
    }

    public void setMofluid_theeme_id(String mofluid_theeme_id) {
        this.mofluid_theeme_id = mofluid_theeme_id;
    }

    public String getMofluid_store_id() {
        return mofluid_store_id;
    }

    public void setMofluid_store_id(String mofluid_store_id) {
        this.mofluid_store_id = mofluid_store_id;
    }

    public String getMofluid_image_id() {
        return mofluid_image_id;
    }

    public void setMofluid_image_id(String mofluid_image_id) {
        this.mofluid_image_id = mofluid_image_id;
    }

    public String getMofluid_image_type() {
        return mofluid_image_type;
    }

    public void setMofluid_image_type(String mofluid_image_type) {
        this.mofluid_image_type = mofluid_image_type;
    }

    public String getMofluid_image_label() {
        return mofluid_image_label;
    }

    public void setMofluid_image_label(String mofluid_image_label) {
        this.mofluid_image_label = mofluid_image_label;
    }

    public String getMofluid_image_value() {
        return mofluid_image_value;
    }

    public void setMofluid_image_value(String mofluid_image_value) {
        this.mofluid_image_value = mofluid_image_value;
    }

    public String getMofluid_image_helptext() {
        return mofluid_image_helptext;
    }

    public void setMofluid_image_helptext(String mofluid_image_helptext) {
        this.mofluid_image_helptext = mofluid_image_helptext;
    }

    public String getMofluid_image_helplink() {
        return mofluid_image_helplink;
    }

    public void setMofluid_image_helplink(String mofluid_image_helplink) {
        this.mofluid_image_helplink = mofluid_image_helplink;
    }

    public String getMofluid_image_required() {
        return mofluid_image_required;
    }

    public void setMofluid_image_required(String mofluid_image_required) {
        this.mofluid_image_required = mofluid_image_required;
    }

    public String getMofluid_image_sort_order() {
        return mofluid_image_sort_order;
    }

    public void setMofluid_image_sort_order(String mofluid_image_sort_order) {
        this.mofluid_image_sort_order = mofluid_image_sort_order;
    }

    public String getMofluid_image_isdefault() {
        return mofluid_image_isdefault;
    }

    public void setMofluid_image_isdefault(String mofluid_image_isdefault) {
        this.mofluid_image_isdefault = mofluid_image_isdefault;
    }

    public String getMofluid_image_action() {
        return mofluid_image_action;
    }

    public void setMofluid_image_action(String mofluid_image_action) {
        this.mofluid_image_action = mofluid_image_action;
    }

    public String getMofluid_image_data() {
        return mofluid_image_data;
    }

    public void setMofluid_image_data(String mofluid_image_data) {
        this.mofluid_image_data = mofluid_image_data;
    }
}
