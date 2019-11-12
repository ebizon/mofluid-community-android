package com.ebizon.fluid.model;

/**
 * Created by ebizon on 18/11/15.
 */
class SimpleProductItem {
    private String name;
    private String price;
    private String sprice;
    private String description;
    private String is_in_stock;
    private String id;
    private String url;
    private String type;
    private String has_custom_option;
    private String currencysymbol;
    private String status;
    private String sku;
    private String visibility;

    public SimpleProductItem(String name, String price, String sprice, String description, String is_in_stock, String id, String url, String type, String has_custom_option, String currencysymbol, String status, String sku, String visibility) {
        this.name = name;
        this.price = price;
        this.sprice = sprice;
        this.description = description;
        this.is_in_stock = is_in_stock;
        this.id = id;
        this.url = url;
        this.type = type;
        this.has_custom_option = has_custom_option;
        this.currencysymbol = currencysymbol;
        this.status = status;
        this.sku = sku;
        this.visibility = visibility;
    }

    public SimpleProductItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSprice() {
        return sprice;
    }

    public void setSprice(String sprice) {
        this.sprice = sprice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIs_in_stock() {
        return is_in_stock;
    }

    public void setIs_in_stock(String is_in_stock) {
        this.is_in_stock = is_in_stock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHas_custom_option() {
        return has_custom_option;
    }

    public void setHas_custom_option(String has_custom_option) {
        this.has_custom_option = has_custom_option;
    }

    public String getCurrencysymbol() {
        return currencysymbol;
    }

    public void setCurrencysymbol(String currencysymbol) {
        this.currencysymbol = currencysymbol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }


}
