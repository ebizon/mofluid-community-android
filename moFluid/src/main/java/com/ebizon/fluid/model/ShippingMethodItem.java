package com.ebizon.fluid.model;

/**
 * Created by ebizon on 22/12/15.
 */
public class ShippingMethodItem {

    private String address_id,carrier,carrier_title,code,created_at,description,error_message,id,method_title,updated_at;
    private int price;

    public ShippingMethodItem()
    {
        this.address_id = "";
        this.carrier = "";
        this.carrier_title = "";
        this.code = "";
        this.created_at = "";
        this.description = "";
        this.error_message = "";
        this.id = "";
        this.method_title = "";
        this.price = 0;
        this.updated_at = "";
    }

    public ShippingMethodItem(String address_id, String carrier, String carrier_title, String code, String created_at, String description, String error_message, String id, String method_title, int price, String updated_at) {
        this.address_id = address_id;
        this.carrier = carrier;
        this.carrier_title = carrier_title;
        this.code = code;
        this.created_at = created_at;
        this.description = description;
        this.error_message = error_message;
        this.id = id;
        this.method_title = method_title;
        this.price = price;
        this.updated_at = updated_at;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getCarrier_title() {
        return carrier_title;
    }

    public void setCarrier_title(String carrier_title) {
        this.carrier_title = carrier_title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod_title() {
        return method_title;
    }

    public void setMethod_title(String method_title) {
        this.method_title = method_title;
    }

    public double getPrice() {
        return (double)price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
