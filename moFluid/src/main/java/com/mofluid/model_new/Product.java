package com.mofluid.model_new;

import java.util.HashMap;
import java.util.Map;

public  class Product {
    private int id,attribute_set_id,status,visibility,qty;
    private double price;
    private boolean is_in_stock;
    private String sku,name,type_id,description,image,small_image,thumbnail,has_options;
    private Map<String,String> custom_attributes;
    public Product(){
        this.custom_attributes=new HashMap<>();
    }
    public void addcustom_attributes(String key,String value){
        this.custom_attributes.put(key,value);
    }
    private String getcustom_attributes(String key){
        return this.custom_attributes.get(key);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAttribute_set_id() {
        return attribute_set_id;
    }

    public void setAttribute_set_id(int attribute_set_id) {
        this.attribute_set_id = attribute_set_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }
   public void setImage(String url){this.custom_attributes.put("image",url);}
    public String getDescription() {
        return this.getcustom_attributes("description");
    }
    public String getImage() {
        return this.getcustom_attributes("image");
    }
    public String getSmall_image() {
        return this.getcustom_attributes("small_image");
    }

    public String getThumbnail() {
        return this.getcustom_attributes("thumbnail");
    }

    public String getHas_options() {
        return this.getcustom_attributes("has_options");
    }
    public boolean getIs_in_stock(){return  this.is_in_stock;}
    public void setIs_in_stock(boolean is_in_stock){this.is_in_stock=is_in_stock;}
    public int getQty(){return this.qty;}
    public void setQty(int qty){this.qty=qty;}
}
