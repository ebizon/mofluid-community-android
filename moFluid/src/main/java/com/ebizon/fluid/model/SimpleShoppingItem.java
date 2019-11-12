package com.ebizon.fluid.model;

/**
 * Created by manish on 20/01/16.
 */
public class SimpleShoppingItem extends ShoppingItem {
    public SimpleShoppingItem(String id, String name, String sku, String image, String price, String specialPrice, String inStock, String stockQuantity, String type, String thumbnail){
        super(id, name, sku, image, price, specialPrice, inStock, stockQuantity, type, thumbnail,id);
    }
}
