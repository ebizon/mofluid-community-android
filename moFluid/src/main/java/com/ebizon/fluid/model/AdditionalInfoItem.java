package com.ebizon.fluid.model;

/**
 * Created by ebizon on 23/11/15.
 */
public class AdditionalInfoItem {
    private String attr_code;
    private String attr_label;
    private String attr_value;

    public AdditionalInfoItem(String attr_code, String attr_label, String attr_value) {
        this.attr_code = attr_code;
        this.attr_label = attr_label;
        this.attr_value = attr_value;
    }

    public String getAttr_code() {
        return attr_code;
    }

    public void setAttr_code(String attr_code) {
        this.attr_code = attr_code;
    }

    public String getAttr_label() {
        return attr_label;
    }

    public void setAttr_label(String attr_label) {
        this.attr_label = attr_label;
    }

    public String getAttr_value() {
        return attr_value;
    }

    public void setAttr_value(String attr_value) {
        this.attr_value = attr_value;
    }
}
