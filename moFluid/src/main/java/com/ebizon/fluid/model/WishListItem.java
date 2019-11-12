package com.ebizon.fluid.model;

/**
 * Created by piyush-ios on 25/5/16.
 */
public class WishListItem {

    String id;
    String name;
    String price;
    String sprice;
    String image;
    String sku;
    String item_id;

    public WishListItem(String id, String name, String price, String sprice, String image,String sku,String item_id) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.sprice = sprice;
        this.image = image;
        this.sku=sku;
        this.item_id=item_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        price=price.replace(",","");
        return Double.parseDouble(price);
    }

    public double getSprice() {
        sprice=sprice.replace(",","");
        return Double.parseDouble(sprice);
    }

    public String getImage() {
        return image;
    }
}
