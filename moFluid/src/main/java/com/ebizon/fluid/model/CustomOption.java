package com.ebizon.fluid.model;

import com.ebizon.fluid.Utils.Utils;

/**
 * Created by manish on 12/01/16.
 */
public class CustomOption {
    private final String id;
    private final String price;
    private final String title;
    private Boolean isSelected;

    public String getTitle() {
        return title;
    }

    public CustomOption(String id, String price, String priceType, String sku, String sortOrder, String title){
        this.id = id;
        this.price = price;
        this.title = title;
        this.isSelected = false;

    }

    public String getTitleWithPrice() {
        String value=title;
        if(!this.getPriceWithCurrency().equalsIgnoreCase("")) {
            value = this.title + " + " + this.getPriceWithCurrency();
        }
        return value;
    }

    public double getPrice(){
        double priceValue = 0.0;
        try {
            priceValue = (double) Double.parseDouble(this.price);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        return priceValue;
    }

    public String getPriceWithCurrency() {
        double p=this.getPrice();
        if(p>0) {
            return Utils.appendWithCurrencySymbol(this.getPrice());
        }else {
            return "";
        }
    }

    public int getId(){
        return Integer.parseInt(this.id);
    }

    public String getIdStr(){
        return this.id;
    }

    public void setSelected(Boolean isSelected){
        this.isSelected = isSelected;
    }

    public Boolean getSelected(){return this.isSelected;}
}
