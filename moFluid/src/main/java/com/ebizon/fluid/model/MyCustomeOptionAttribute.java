package com.ebizon.fluid.model;

import java.util.ArrayList;

/**
 * Created by ebizon on 26/11/15.
 */
class MyCustomeOptionAttribute {


    private String custom_option_name;
    private String custom_option_id;
    private String custom_option_is_required;
    private String custom_option_type;
    private String sort_order;

    private String all_option_id;
    private String all_product_id;
    private String all_type;
    private String all_is_require;
    private String all_sku;
    private String all_max_characters;
    private String all_file_extension;
    private String all_image_size_x;
    private String all_image_size_y;
    private String all_sort_order;
    private String all_default_title;
    private String all_store_title;
    private String all_title;
    private String all_default_price;
    private String all_default_price_type;
    private String all_store_price;
    private String all_store_price_type;
    private String all_price;
    private String all_price_type;
    private ArrayList<CustomeOptionValueArray> custom_option_value_array_list;


    public MyCustomeOptionAttribute(String all_default_price, String all_default_price_type, String all_default_title, String all_file_extension, String all_image_size_x, String all_image_size_y, String all_is_require, String all_max_characters, String all_option_id, String all_price, String all_price_type, String all_product_id, String all_sku, String all_sort_order, String all_store_price, String all_store_price_type, String all_store_title, String all_title, String all_type, String custom_option_id, String custom_option_is_required, String custom_option_type, String custom_option_name, String sort_order,ArrayList<CustomeOptionValueArray> custom_option_value_array_list) {
        this.all_default_price = all_default_price;
        this.all_default_price_type = all_default_price_type;
        this.all_default_title = all_default_title;
        this.all_file_extension = all_file_extension;
        this.all_image_size_x = all_image_size_x;
        this.all_image_size_y = all_image_size_y;
        this.all_is_require = all_is_require;
        this.all_max_characters = all_max_characters;
        this.all_option_id = all_option_id;
        this.all_price = all_price;
        this.all_price_type = all_price_type;
        this.all_product_id = all_product_id;
        this.all_sku = all_sku;
        this.all_sort_order = all_sort_order;
        this.all_store_price = all_store_price;
        this.all_store_price_type = all_store_price_type;
        this.all_store_title = all_store_title;
        this.all_title = all_title;
        this.all_type = all_type;
        this.custom_option_id = custom_option_id;
        this.custom_option_is_required = custom_option_is_required;
        this.custom_option_type = custom_option_type;
        this.custom_option_name = custom_option_name;
        this.sort_order = sort_order;
        this.custom_option_value_array_list=custom_option_value_array_list;
    }

    public String getAll_default_price() {
        return all_default_price;
    }

    public void setAll_default_price(String all_default_price) {
        this.all_default_price = all_default_price;
    }

    public String getAll_default_price_type() {
        return all_default_price_type;
    }

    public void setAll_default_price_type(String all_default_price_type) {
        this.all_default_price_type = all_default_price_type;
    }

    public String getAll_default_title() {
        return all_default_title;
    }

    public void setAll_default_title(String all_default_title) {
        this.all_default_title = all_default_title;
    }

    public String getAll_file_extension() {
        return all_file_extension;
    }

    public void setAll_file_extension(String all_file_extension) {
        this.all_file_extension = all_file_extension;
    }

    public String getAll_image_size_x() {
        return all_image_size_x;
    }

    public void setAll_image_size_x(String all_image_size_x) {
        this.all_image_size_x = all_image_size_x;
    }

    public String getAll_image_size_y() {
        return all_image_size_y;
    }

    public void setAll_image_size_y(String all_image_size_y) {
        this.all_image_size_y = all_image_size_y;
    }

    public String getAll_is_require() {
        return all_is_require;
    }

    public void setAll_is_require(String all_is_require) {
        this.all_is_require = all_is_require;
    }

    public String getAll_max_characters() {
        return all_max_characters;
    }

    public void setAll_max_characters(String all_max_characters) {
        this.all_max_characters = all_max_characters;
    }

    public String getAll_option_id() {
        return all_option_id;
    }

    public void setAll_option_id(String all_option_id) {
        this.all_option_id = all_option_id;
    }

    public String getAll_price() {
        return all_price;
    }

    public void setAll_price(String all_price) {
        this.all_price = all_price;
    }

    public String getAll_price_type() {
        return all_price_type;
    }

    public void setAll_price_type(String all_price_type) {
        this.all_price_type = all_price_type;
    }

    public String getAll_product_id() {
        return all_product_id;
    }

    public void setAll_product_id(String all_product_id) {
        this.all_product_id = all_product_id;
    }

    public String getAll_sku() {
        return all_sku;
    }

    public void setAll_sku(String all_sku) {
        this.all_sku = all_sku;
    }

    public String getAll_sort_order() {
        return all_sort_order;
    }

    public void setAll_sort_order(String all_sort_order) {
        this.all_sort_order = all_sort_order;
    }

    public String getAll_store_price() {
        return all_store_price;
    }

    public void setAll_store_price(String all_store_price) {
        this.all_store_price = all_store_price;
    }

    public String getAll_store_price_type() {
        return all_store_price_type;
    }

    public void setAll_store_price_type(String all_store_price_type) {
        this.all_store_price_type = all_store_price_type;
    }

    public String getAll_title() {
        return all_title;
    }

    public void setAll_title(String all_title) {
        this.all_title = all_title;
    }

    public String getAll_store_title() {
        return all_store_title;
    }

    public void setAll_store_title(String all_store_title) {
        this.all_store_title = all_store_title;
    }

    public String getAll_type() {
        return all_type;
    }

    public void setAll_type(String all_type) {
        this.all_type = all_type;
    }

    public String getCustom_option_id() {
        return custom_option_id;
    }

    public void setCustom_option_id(String custom_option_id) {
        this.custom_option_id = custom_option_id;
    }

    public String getCustom_option_is_required() {
        return custom_option_is_required;
    }

    public void setCustom_option_is_required(String custom_option_is_required) {
        this.custom_option_is_required = custom_option_is_required;
    }

    public String getCustom_option_name() {
        return custom_option_name;
    }

    public void setCustom_option_name(String custom_option_name) {
        this.custom_option_name = custom_option_name;
    }

    public String getCustom_option_type() {
        return custom_option_type;
    }

    public void setCustom_option_type(String custom_option_type) {
        this.custom_option_type = custom_option_type;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public ArrayList<CustomeOptionValueArray> getCustom_option_value_array_list() {
        return custom_option_value_array_list;
    }

    public void setCustom_option_value_array_list(ArrayList<CustomeOptionValueArray> custom_option_value_array_list) {
        this.custom_option_value_array_list = custom_option_value_array_list;
    }
}
