package com.ebizon.fluid.Utils;

import com.ebizon.fluid.model.ShoppingCartItem;

import java.util.ArrayList;

/**
 * Created by piyush-ios on 13/10/16.
 */
public interface IResponseListener {

    public void onResponse(boolean result, ArrayList<ShoppingCartItem> data);
}
