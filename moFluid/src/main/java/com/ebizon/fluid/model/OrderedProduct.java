package com.ebizon.fluid.model;

/**
 * Created by prashant on 29/3/16.
 */
public class OrderedProduct {
    String product_image;
    String product_unit_price;
    String product_name;
    String Total_Price;
    String product_quantity;
    String product_id;
    String sku;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_unit_price() {
        return product_unit_price;
    }

    public void setProduct_unit_price(String product_unit_price) {
        this.product_unit_price = product_unit_price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getTotal_Price() {
        return Total_Price;
    }

    public void setTotal_Price(String total_Price) {
        Total_Price = total_Price;
    }
}
