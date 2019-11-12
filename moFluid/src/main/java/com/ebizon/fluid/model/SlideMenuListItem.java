package com.ebizon.fluid.model;

/**
 * Created by ebizon on 26/10/15.
 */
public class SlideMenuListItem {
    private String name;
    private String id;

    public SlideMenuListItem(String name, String id) {
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
