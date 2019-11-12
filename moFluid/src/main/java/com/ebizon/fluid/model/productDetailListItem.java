package com.ebizon.fluid.model;

/**
 * Created by ebizon on 30/10/15.
 */
public class productDetailListItem {
    private String id;
    private String name;
    private String imageurl;
    private String sku;
    private String type;
    private String spclprice;
    private String currencysymbol;
    private String price;
    private String created_date;
    private String is_in_stock;
    private String hasoptions;
    private String stock_quantity;
    private String initialQuantity;

    public productDetailListItem(String id, String name, String imageurl, String sku, String type, String spclprice, String currencysymbol, String price, String created_date, String is_in_stock, String hasoptions, String stock_quantity) {
        this.id = id;
        this.name = name;
        this.imageurl = imageurl;
        this.sku = sku;
        this.type = type;
        this.spclprice = spclprice;
        this.currencysymbol = currencysymbol;
        this.price = price;
        this.created_date = created_date;
        this.is_in_stock = is_in_stock;
        this.hasoptions = hasoptions;
        this.stock_quantity = stock_quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpclprice() {
        return spclprice;
    }

    public void setSpclprice(String spclprice) {
        this.spclprice = spclprice;
    }

    public String getCurrencysymbol() {
        return currencysymbol;
    }

    public void setCurrencysymbol(String currencysymbol) {
        this.currencysymbol = currencysymbol;
    }

    public String getPrice() {
        return price;
    }

    public double getFinalPrice(){
        double specialPrice = Double.parseDouble(this.getSpclprice());
        double price = Double.parseDouble(this.getPrice());
        double finalPrice = price;

        if(specialPrice > 0 && specialPrice < price) {
            finalPrice = specialPrice;
        }

        return finalPrice;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getIs_in_stock() {
        return is_in_stock;
    }

    public void setIs_in_stock(String is_in_stock) {
        this.is_in_stock = is_in_stock;
    }

    public String getHasoptions() {
        return hasoptions;
    }

    public void setHasoptions(String hasoptions) {
        this.hasoptions = hasoptions;
    }

    public String getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(String stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public productDetailListItem setInitialQuantity(String initialQuantity) {
        this.initialQuantity = initialQuantity;
        return this;
    }
    public String getInitialQuantity() {
        return initialQuantity;
    }


}
