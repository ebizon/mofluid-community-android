package com.ebizon.fluid.model;

/**
 * Created by ebizon201 on 1/11/16.
 */
public class ReoredrItem {
    String name,img,price,sprice;

    public ReoredrItem(String name, String sprice, String price, String img) {
        this.name = name;
        this.sprice = sprice;
        this.price = price;
        this.img = img;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSprice() {
        return sprice;
    }

    public void setSprice(String sprice) {
        this.sprice = sprice;
    }
}
