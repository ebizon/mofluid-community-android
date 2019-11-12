package com.ebizon.fluid.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by manish on 12/01/16.
 */
public class ShoppingItemManager {
    private static final ShoppingItemManager ourInstance = new ShoppingItemManager();

    public static ShoppingItemManager getInstance() {
        return ourInstance;
    }

    private final HashMap<String, ShoppingItem> shoppingItemHashMap = new HashMap<String, ShoppingItem>();

    private ShoppingItemManager() {
        //Empty
    }

    public void addShoppingItem(ShoppingItem item){
        this.shoppingItemHashMap.put(item.getId(), item);
    }

    public ShoppingItem getShoppingItem(String itemId){
        return this.shoppingItemHashMap.get(itemId);
    }
}
