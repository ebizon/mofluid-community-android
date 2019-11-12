package com.ebizon.fluid.model;

/**
 * Created by ebizon on 1/7/16.
 */
public class FilterKeyItem {
    String name,id;

    public FilterKeyItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public FilterKeyItem setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FilterKeyItem setName(String name) {
        this.name = name;
        return this;
    }
}
