package com.mofluid.model_new;

import com.mofluid.interfaces.PaymentMethod;
import com.mofluid.utility_new.Callback;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> items;
    private static Cart instance;

    private Cart() {
        items = new ArrayList<>();
    }

    public static Cart getCart() {
        if (instance == null)
            instance = new Cart();
        return instance;
    }

    public void addItem(Product item) {
        this.items.add(item);
    }

    public void addItem(List<Product> products) {
        this.items.addAll(products);
    }
private double total(){
double total_amount=0.0;
for(Product product: this.items){
    double price=product.getPrice();
    total_amount+=price;
}
return total_amount;
}
public void pay(double amount, PaymentMethod method, Callback callback){
method.pay(amount,callback);
}
}
