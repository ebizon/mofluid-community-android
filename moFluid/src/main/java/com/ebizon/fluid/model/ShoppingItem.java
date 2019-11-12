package com.ebizon.fluid.model;

import android.support.annotation.Nullable;

import com.ebizon.fluid.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by manish on 20/01/16.
 */
public class ShoppingItem extends Entity {
    @Override
    public String getId() {
        return id;
    }

    private final String id;
    private final String name;
    private final String sku;
    private final String type;
    private final String specialPrice;
    private String price;
    private final String inStock;
    private final String parentID;
    private String stockQuantity;
    private final String image;
    private String thumbnail = "";
    private String manageStock;
    private ArrayList<String> images;


    public void setStockQuantity(String stockQuantity) {
        this.stockQuantity = stockQuantity;
    }


    public String getParentID() {
        return parentID;
    }


    public String getManageStock() {
        return manageStock;
    }

    @Nullable
    public static ShoppingItem create(JSONObject jsonObject) {
        ShoppingItem item = null;
        try {
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String type = jsonObject.getString("type");
            String image = jsonObject.getString("image");
            String price = jsonObject.getString("price");
            String specialPrice = jsonObject.getString("special_price");
            String isStockStatus = jsonObject.getString("is_stock_status");

            item = new ShoppingItem(id, name, "sku", image, price, specialPrice, isStockStatus, "1", type, image, id);

            ShoppingItemManager.getInstance().addShoppingItem(item);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return item;
    }

    public ShoppingItem(String id, String name, String sku, String image, String price, String specialPrice, String inStock, String stockQuantity, String type, String thumbnail, String parentID, String manageStock) {
        super(id);
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.image = image;
        this.price = price;
        this.specialPrice = specialPrice;
        this.inStock = inStock;
        this.stockQuantity = stockQuantity;
        this.type = type;
        this.thumbnail = thumbnail;
        this.parentID = parentID;
        this.manageStock = manageStock;
    }

    public ShoppingItem(String id, String name, String sku, String image, String price, String specialPrice, String inStock, String stockQuantity, String type, String thumbnail, String parentID) {
        super(id);
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.image = image;
        this.price = price;
        this.specialPrice = specialPrice;
        this.inStock = inStock;
        this.stockQuantity = stockQuantity;
        this.type = type;
        this.thumbnail = thumbnail;
        this.parentID = parentID;
    }

    public ShoppingItem(String id, String name, String sku, String image, String price, String specialPrice, String inStock, String stockQuantity, String type) {
        super(id);
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.image = image;
        this.price = price;
        this.specialPrice = specialPrice;
        this.inStock = inStock;
        this.stockQuantity = stockQuantity;
        this.type = type;
        this.parentID = null;
    }

    public String getName() {
        return this.name;
    }

    public String getImage() {
        if (this.images != null && this.images.size() > 0) {
            return this.images.get(0);
        } else {
            return this.image;
        }
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getPriceStr() {
        return this.price;
    }

    public String getSpecialPriceStr() {
        return this.specialPrice;
    }

    public double getPrice() {
        price = price.replace(",", "");
        return Double.parseDouble(this.price);
    }

    public double getSpecialPrice() {
        return Double.parseDouble(this.specialPrice);
    }

    public double getFinalPrice() {
        double finalPrice = getPrice();
        double spPrice = getSpecialPrice();

        if (spPrice > 0.0 && finalPrice > spPrice) {
            finalPrice = spPrice;
        }

        return finalPrice;
    }

    public String getFinalPriceWithCurrency() {
        return Utils.appendWithCurrencySymbol(this.getFinalPrice());
    }

    public String getInStock() {
        return this.inStock;
    }

    public String getStock() {
        return this.stockQuantity;
    }

    public boolean isInStock() {
        boolean haveStock = this.inStock.equals("1") || this.inStock.equals("true");
        return haveStock && this.getStockQuantity() > 0;
    }

    public int getStockQuantity() {
        return (int) Float.parseFloat(this.stockQuantity);
    }


    public String getSku() {
        return this.sku;
    }

    public String getType() {
        return this.type;
    }

    public JSONObject createJSON(int quantity) {
        HashMap<String, String> productMap = this.createJSONMAP(quantity);
        JSONObject jsnObj = new JSONObject(productMap);

        return jsnObj;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.images = imageList;
    }

    protected HashMap<String, String> createJSONMAP(int quantity) {
        HashMap<String, String> productMap = new HashMap<String, String>();
        productMap.put("id", this.getId());
        if (!this.getType().toString().equals("downloadable"))
            productMap.put("type", this.getType());

        productMap.put("sku", this.getSku());
        productMap.put("quantity", String.valueOf(quantity));

        return productMap;
    }


}