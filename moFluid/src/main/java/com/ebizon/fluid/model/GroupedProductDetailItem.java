package com.ebizon.fluid.model;

/**
 * Created by ebizon on 19/11/15.
 */
public class GroupedProductDetailItem {
    private String grouped_pro_status;
    private String grouped_sku;
    private String grouped_status;
    private String grouped_general_name;
    private String grouped_general_sku;
    private String grouped_general_weight;
    private String grouped_price_final;
    private String grouped_price_regular;
    private String grouped_image;
    private String grouped_stock_item_id;
    private String grouped_stock_product_id;
    private String grouped_stock_stock_id;
    private String grouped_stock_qty;
    private String grouped_stock_is_in_stock;
    private String description_short;

    public String getProduct_Stock_max_quantity() {
        return product_Stock_max_quantity;
    }

    public void setProduct_Stock_max_quantity(String product_Stock_max_quantity) {
        this.product_Stock_max_quantity = product_Stock_max_quantity;
    }

    private String description_pull ;
    private String grouped_id;
    private int no_of_produc;
    private String type;

    private String product_Stock_max_quantity;
    private int quantity;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getQuantity() {
        return quantity;
    }

    public GroupedProductDetailItem(int no_of_produc, String grouped_id, String grouped_pro_status, String grouped_sku, String grouped_status, String grouped_general_name, String grouped_general_sku, String grouped_general_weight, String grouped_price_final, String grouped_price_regular, String grouped_image, String grouped_stock_item_id, String grouped_stock_product_id, String grouped_stock_stock_id, String grouped_stock_qty, String grouped_stock_is_in_stock, String description_short, String description_pull, String type,int quantity,String product_Stock_max_quantity) {

        this.no_of_produc=no_of_produc;
        this.grouped_id=grouped_id;
        this.grouped_pro_status = grouped_pro_status;
        this.grouped_sku = grouped_sku;
        this.grouped_status = grouped_status;
        this.grouped_general_name = grouped_general_name;
        this.grouped_general_sku = grouped_general_sku;
        this.grouped_general_weight = grouped_general_weight;
        this.grouped_price_final = grouped_price_final;
        this.grouped_price_regular = grouped_price_regular;
        this.grouped_image = grouped_image;
        this.grouped_stock_item_id = grouped_stock_item_id;
        this.grouped_stock_product_id = grouped_stock_product_id;
        this.grouped_stock_stock_id = grouped_stock_stock_id;
        this.grouped_stock_qty = grouped_stock_qty;
        this.grouped_stock_is_in_stock = grouped_stock_is_in_stock;
        this.description_short = description_short;
        this.description_pull = description_pull;
        this.type=type;
        this.quantity=quantity;
        this.product_Stock_max_quantity=product_Stock_max_quantity;
    }

    public String getGrouped_id() {
        return grouped_id;
    }

    public void setGrouped_id(String grouped_id) {
        this.grouped_id = grouped_id;
    }

    public int getNo_of_produc() {
        return no_of_produc;
    }

    public void setNo_of_produc(int no_of_produc) {
        this.no_of_produc = no_of_produc;
    }

    public String getDescription_short() {
        return description_short;
    }

    public void setDescription_short(String description_short) {
        this.description_short = description_short;
    }

    public String getDescription_pull() {
        return description_pull;
    }

    public void setDescription_pull(String description_pull) {
        this.description_pull = description_pull;
    }
    public String getGrouped_pro_status() {
        return grouped_pro_status;
    }

    public void setGrouped_pro_status(String grouped_pro_status) {
        this.grouped_pro_status = grouped_pro_status;
    }

    public String getGrouped_sku() {
        return grouped_sku;
    }

    public void setGrouped_sku(String grouped_sku) {
        this.grouped_sku = grouped_sku;
    }

    public String getGrouped_status() {
        return grouped_status;
    }

    public void setGrouped_status(String grouped_status) {
        this.grouped_status = grouped_status;
    }

    public String getGrouped_general_name() {
        return grouped_general_name;
    }

    public void setGrouped_general_name(String grouped_general_name) {
        this.grouped_general_name = grouped_general_name;
    }

    public String getGrouped_general_sku() {
        return grouped_general_sku;
    }

    public void setGrouped_general_sku(String grouped_general_sku) {
        this.grouped_general_sku = grouped_general_sku;
    }

    public String getGrouped_general_weight() {
        return grouped_general_weight;
    }

    public void setGrouped_general_weight(String grouped_general_weight) {
        this.grouped_general_weight = grouped_general_weight;
    }

    public String getGrouped_price_final() {
        return grouped_price_final;
    }

    public void setGrouped_price_final(String grouped_price_final) {
        this.grouped_price_final = grouped_price_final;
    }

    public String getGrouped_price_regular() {
        return grouped_price_regular;
    }

    public void setGrouped_price_regular(String grouped_price_regular) {
        this.grouped_price_regular = grouped_price_regular;
    }

    public String getGrouped_image() {
        return grouped_image;
    }

    public void setGrouped_image(String grouped_image) {
        this.grouped_image = grouped_image;
    }

    public String getGrouped_stock_item_id() {
        return grouped_stock_item_id;
    }

    public void setGrouped_stock_item_id(String grouped_stock_item_id) {
        this.grouped_stock_item_id = grouped_stock_item_id;
    }

    public String getGrouped_stock_product_id() {
        return grouped_stock_product_id;
    }

    public void setGrouped_stock_product_id(String grouped_stock_product_id) {
        this.grouped_stock_product_id = grouped_stock_product_id;
    }

    public String getGrouped_stock_stock_id() {
        return grouped_stock_stock_id;
    }

    public void setGrouped_stock_stock_id(String grouped_stock_stock_id) {
        this.grouped_stock_stock_id = grouped_stock_stock_id;
    }

    public String getGrouped_stock_qty() {
        return grouped_stock_qty;
    }

    public void setGrouped_stock_qty(String grouped_stock_qty) {
        this.grouped_stock_qty = grouped_stock_qty;
    }

    public String getGrouped_stock_is_in_stock() {
        return grouped_stock_is_in_stock;
    }

    public void setGrouped_stock_is_in_stock(String grouped_stock_is_in_stock) {
        this.grouped_stock_is_in_stock = grouped_stock_is_in_stock;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
