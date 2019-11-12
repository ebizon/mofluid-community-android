package com.ebizon.fluid.model;

import android.util.Log;

/**
 * Created by ebizon on 23/11/15.
 */
public class ShoppingCartItem {
    private final ShoppingItem shoppingItem;
    private int count;
    private boolean qualifies_checkout;

    public ShoppingCartItem(ShoppingItem item, int count){
        this.shoppingItem = item;
        this.count = count;
        setQualifies_checkout(true);
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ShoppingItem getShoppingItem(){
        return this.shoppingItem;

    }

    public int getCount(){
        return this.count;
    }

    public boolean isQualifies_checkout() {
        return qualifies_checkout;
    }

    public void setQualifies_checkout(boolean qualifies_checkout) {
        this.qualifies_checkout = qualifies_checkout;
    }
}
