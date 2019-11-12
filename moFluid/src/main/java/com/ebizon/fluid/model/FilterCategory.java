package com.ebizon.fluid.model;

/**
 * Created by ebizon201 on 7/7/16.
 */
public class FilterCategory {
    String name,id;

    public FilterCategory(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
