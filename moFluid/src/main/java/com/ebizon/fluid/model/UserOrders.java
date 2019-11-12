package com.ebizon.fluid.model;

import com.ebizon.fluid.model.OrderedProduct;

import java.util.ArrayList;

/**
 * Created by prashant on 29/3/16.
 */
public class UserOrders {
    String order_id;
    String order_date;
    String order_status;
    ArrayList<OrderedProduct> orderedProducts;
    String firstname;
    String lastname;
    String contactnumber;
    String country;
    String zipcode;
    String city;
    String state;
    String firstname_billing;
    String lastname_billing;
    String contactnumber_billing;
    String country_billing;
    String zipcode_billing;
    String city_billing;
    String state_billing;
    String shipping_billing;
    String payment_method;
    String shippingAmount;
    String taxAmount;
    String shipping_method;

    public static String getOrderCurrency() {
        return orderCurrency;
    }

    public void setOrderCurrency(String orderCurrency) {
        this.orderCurrency = orderCurrency;
    }

   static String orderCurrency;

    public String getShipping_method() {
        return shipping_method;
    }

    public void setShipping_method(String shipping_method) {
        this.shipping_method = shipping_method;
    }

    public String getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(String shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getShipping_billing() {
        return shipping_billing;
    }

    public void setShipping_billing(String shipping_billing) {
        this.shipping_billing = shipping_billing;
    }

    public String getFirstname_billing() {
        return firstname_billing;
    }

    public void setFirstname_billing(String firstname_billing) {
        this.firstname_billing = firstname_billing;
    }

    public String getLastname_billing() {
        return lastname_billing;
    }

    public void setLastname_billing(String lastname_billing) {
        this.lastname_billing = lastname_billing;
    }

    public String getContactnumber_billing() {
        return contactnumber_billing;
    }

    public void setContactnumber_billing(String contactnumber_billing) {
        this.contactnumber_billing = contactnumber_billing;
    }

    public String getCountry_billing() {
        return country_billing;
    }

    public void setCountry_billing(String country_billing) {
        this.country_billing = country_billing;
    }

    public String getZipcode_billing() {
        return zipcode_billing;
    }

    public void setZipcode_billing(String zipcode_billing) {
        this.zipcode_billing = zipcode_billing;
    }

    public String getCity_billing() {
        return city_billing;
    }

    public void setCity_billing(String city_billing) {
        this.city_billing = city_billing;
    }

    public String getState_billing() {
        return state_billing;
    }

    public void setState_billing(String state_billing) {
        this.state_billing = state_billing;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getShippingaddress() {
        return shippingaddress;
    }

    public void setShippingaddress(String shippingaddress) {
        this.shippingaddress = shippingaddress;
    }

    String shippingaddress;

    public String getAmount_Payble() {
        return amount_Payble;
    }

    public void setAmount_Payble(String amount_Payble) {
        this.amount_Payble = amount_Payble;
    }

    String amount_Payble;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public ArrayList<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(ArrayList<OrderedProduct> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }
}
